package com.sapient.publicis.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.publicis.model.in.WeatherProcessingRequest;
import com.sapient.publicis.model.in.WeatherProcessingResponse;
import com.sapient.publicis.service.WeatherService;

@RestController
//@Slf4j
public class WeatherServiceController {
	private final WeatherService weatherService;

	private Date thresholdDate;

	public WeatherServiceController(@Autowired final WeatherService weatherService,
			@Value("${thresholdDate:#{null}}") final String thresholdDateString) throws ParseException {
		this.weatherService = weatherService;
		if (StringUtils.isNotEmpty(thresholdDateString)) {
			thresholdDate = new SimpleDateFormat("yyyy-MM-DD").parse(thresholdDateString);
		}
	}

	@GetMapping("/weather/{version}/forecast")
	@Valid
	public ResponseEntity<WeatherProcessingResponse> getWeatherPredictionByLocation(
			@RequestParam(value = "city") final String location,
			@RequestParam(value = "minNDays", defaultValue = "1") @Min(1) final int minNDays,
			@RequestParam(value = "maxNDays", defaultValue = "3") @Max(5) final int maxNDays) {

		final WeatherProcessingRequest request = new WeatherProcessingRequest(location, thresholdDate, minNDays, maxNDays);
		final WeatherProcessingResponse response = weatherService.process(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
