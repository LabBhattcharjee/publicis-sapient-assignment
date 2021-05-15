package com.sapient.publicis.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sapient.publicis.model.in.WeatherProcessingRequest;
import com.sapient.publicis.model.in.WeatherProcessingResponse;

@SpringBootTest
class WeatherServiceTest {

	@Autowired
	private WeatherService weatherService;

	@Test
	void testProcess() throws ParseException {
		final WeatherProcessingRequest weatherProcessingRequest = new WeatherProcessingRequest("London",
				new SimpleDateFormat("yyyy-MM-DD").parse("2017-01-17"), 1, 3);
		final WeatherProcessingResponse weatherProcessingResponse = weatherService.process(weatherProcessingRequest);

		System.out.println(weatherProcessingResponse);

		System.out.println(weatherService.process(weatherProcessingRequest));
		
		System.out.println(weatherService.process(weatherProcessingRequest));

	}

}
