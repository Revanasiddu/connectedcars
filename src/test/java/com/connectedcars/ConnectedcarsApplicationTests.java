package com.connectedcars;

import com.connectedcars.controller.ConnectedCarsController;
import com.connectedcars.model.FileDataModel;
import com.connectedcars.service.ConnectedCarsService;
import com.sun.deploy.net.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.bind.ValidationException;
import java.io.IOException;

import static com.ctc.wstx.shaded.msv_core.grammar.Expression.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ConnectedcarsApplicationTests {

	@InjectMocks
	ConnectedCarsController carsController;

	@Mock
	ConnectedCarsService carsService;

	@Test
	void saveTest() throws IOException, ValidationException {
		when(carsController.StoreData("xml", new FileDataModel())).thenReturn(null);
		Assertions.assertEquals(carsService.PublishData(new FileDataModel(), "xml"),null);
	}

	@Test
	void updateTest() throws IOException, ValidationException {
		when(carsController.UpdateData("xml", new FileDataModel())).thenReturn(null);
		Assertions.assertEquals(carsService.PublishData(new FileDataModel(), "xml"),null);
	}

	@Test
	void getFileData() throws IOException {
		when(carsController.getAllData()).thenReturn(null);
		Assertions.assertEquals(carsService.getAllData(),null);
	}

}
