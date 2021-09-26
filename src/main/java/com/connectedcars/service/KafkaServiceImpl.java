package com.connectedcars.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Revanasidd Namadev.
 * User: rnamade1
 * Date: 9/26/2021
 * Time: 2:37
 */
@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService, HealthIndicator {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.template.topic}")
    public String topic;

    @Autowired
    ObjectMapper objectMapper;

    private Health health;

    public static final String PRODUCER_MESSAGE_KEY = "Kafka producer";
    public static final String KEY_NOT_AVAILABLE = "Not Available";
    public static final String KEY_AVAILABLE = "Available";

    @Override
    public void publishMessage(Map<String, Object> fileDataModel) throws JsonProcessingException, ValidationException {

        if (kafkaTemplate == null) {
            log.error("Message could not be published. kafkaTemplate not found.");
            throw new ValidationException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), "Message could not be published. kafkaTemplate not found.");
        }

        String finalPayload = objectMapper.writeValueAsString(fileDataModel);
        String key = "1";
        ProducerRecord<String, String> producerRecord = prepareKafkaRecord(key, topic, finalPayload);

        log.info("publish message={} key={} topic={}", producerRecord.value(), key, topic);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("kafka_cluster=Primary Brokers status=success message={} topic={} offset={}",
                        result.getProducerRecord().value(), topic, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                try {
                    publishMessageToSecondaryBroker(fileDataModel);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                log.error("Message could not be published, reason={}"+ex.getMessage());
            }
        });
    }

    private ProducerRecord<String, String> prepareKafkaRecord(String key, String topic, String payload) {
        List<Header> kafkaHeaders =
                Arrays.asList(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        return new ProducerRecord<>(topic, null, key, payload, kafkaHeaders);
    }

    private void publishMessageToSecondaryBroker(Map<String, Object> fileDataModel) throws JsonProcessingException {
        String finalPayload = objectMapper.writeValueAsString(fileDataModel);
        String key = "1";
        ProducerRecord<String, String> producerRecord = prepareKafkaRecord(key, topic, finalPayload);

        log.info("publish message={} key={}",finalPayload, key);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("kafka_cluster=Secondary Brokers status=success key={} message={} topic={} offset={}", key,
                        producerRecord.value(), topic, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("kafka_cluster=Secondary Brokers status=failed  key={} message={} topic={} reason={}", key,
                        producerRecord.value(), topic, ex.getMessage());
                health = Health.down().withDetail(PRODUCER_MESSAGE_KEY, KEY_NOT_AVAILABLE).build();
            }
        });
    }


    @Override
    public Health health() {
        if (health != null) {
            return health;
        }
        return Health.up().withDetail(PRODUCER_MESSAGE_KEY, KEY_AVAILABLE).build();
    }
}
