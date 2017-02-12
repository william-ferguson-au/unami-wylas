package com.unami.wylas.adapter;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.Test;

public class FileMeetManagerServiceTest {

	@Test
	public void testStart() {
		FileMeetManagerService service = new FileMeetManagerService();
		LinkedHashMap<String, String> properties = service.getProperties();

		properties.put(FileMeetManagerService.PROPERTY_NAME_DATA_FILE_LOCATION, "./data");

		service.start();
		assertTrue(service.isStarted());
	}

}
