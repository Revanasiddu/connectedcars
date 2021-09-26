package com.connectedcars.controller;

import com.connectedcars.model.FileDataModel;
import com.connectedcars.service.ConnectedCarsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Revanasidd Namadev.
 * User: rnamade1
 * Date: 9/26/2021
 * Time: 3:45
 */

@RestController
@RequestMapping(value = "${app.context.path}")
@Slf4j
@Validated
public class ConnectedCarsController {

    @Autowired
    ConnectedCarsService carsService;

    @PostMapping(value = "/save",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity StoreData(@RequestHeader(value = "file-type", required = true) String fileType,
                                    @Valid @RequestBody FileDataModel fileDataModel) throws IOException, ValidationException {
        return new ResponseEntity(carsService.PublishData(fileDataModel, fileType), HttpStatus.CREATED);
    }


    @PutMapping(value = "/update",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity UpdateData(@RequestHeader(value = "file-type") String fileType,
                                     @Valid @RequestBody FileDataModel fileDataModel) throws IOException, ValidationException {
        return new ResponseEntity(carsService.PublishData(fileDataModel, fileType), HttpStatus.OK);
    }

    @GetMapping(value = "/filedata",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE})
    public ResponseEntity getAllData() throws IOException {
        return new ResponseEntity(carsService.getAllData(), HttpStatus.OK);
    }

}
