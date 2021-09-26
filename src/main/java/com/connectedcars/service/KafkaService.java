package com.connectedcars.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.xml.bind.ValidationException;
import java.util.Map;

/**
 * Created by Revanasidd Namadev.
 * User: rnamade1
 * Date: 9/26/2021
 * Time: 2:37
 */

public interface KafkaService {

    void publishMessage(Map<String, Object> fileDataModel) throws JsonProcessingException, ValidationException;
}
