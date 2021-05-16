package com.sapient.publicis.controller;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.publicis.model.in.WeatherProcessingRequest;
import com.sapient.publicis.model.in.WeatherProcessingResponse;
import com.sapient.publicis.service.WeatherService;

@RestController
public class WeatherServiceController {
	private final WeatherService weatherService;

	public WeatherServiceController(@Autowired final WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@GetMapping("/weather/1.0/forecast")
	@Valid
	public ResponseEntity<WeatherProcessingResponse> getWeatherPredictionByLocation(
			@RequestParam(value = "city") final String location,
			@RequestParam(value = "minNDays", defaultValue = "1") @Min(1) final int minNDays,
			@RequestParam(value = "maxNDays", defaultValue = "3") @Max(5) final int maxNDays,
			@RequestParam(value = "thresholdDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final Date thresholdDate) {

		final WeatherProcessingRequest request = new WeatherProcessingRequest(location, thresholdDate, minNDays, maxNDays);
		final WeatherProcessingResponse response = weatherService.process(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
