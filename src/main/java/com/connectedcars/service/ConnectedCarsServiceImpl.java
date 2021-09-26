package com.connectedcars.service;

import com.connectedcars.model.FileDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Revanasidd Namadev.
 * User: rnamade1
 * Date: 9/26/2021
 * Time: 2:37
 */

@Service
public class ConnectedCarsServiceImpl implements ConnectedCarsService {

    @Autowired
    KafkaServiceImpl kafkaService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String PublishData(FileDataModel fileDataModel, String fileType) throws IOException, ValidationException {
        Map<String,Object> record = new HashMap<>();
        record.put("fileDataModel", fileDataModel);
        record.put("fileType", fileType);
        kafkaService.publishMessage(record);
        return "Published to Kafka Successfully";
    }

    @Override
    public String getAllData() {
        return restTemplate.getForObject("http://localhost:8082/connected/cars/consumer/filedata", String.class);
    }
}
