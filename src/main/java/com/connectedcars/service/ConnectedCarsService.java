package com.connectedcars.service;

import com.connectedcars.model.FileDataModel;

import javax.xml.bind.ValidationException;
import java.io.IOException;

/**
 * Created by Revanasidd Namadev.
 * User: rnamade1
 * Date: 9/26/2021
 * Time: 2:37
 */

public interface ConnectedCarsService {

    String PublishData(FileDataModel FileDataModel, String fileType) throws IOException, ValidationException;

    String getAllData();
}
