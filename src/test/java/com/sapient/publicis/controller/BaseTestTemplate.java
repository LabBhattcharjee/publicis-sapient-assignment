package com.sapient.publicis.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.sapient.publicis.util.WeatherServiceConstants;

import lombok.Data;

public abstract class BaseTestTemplate {
	// bind the above RANDOM_PORT
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MessageSource messages;

	@Data
	public static class UnitSuccessResponse {
		List<Map<String,Object>> message;
	}
	
	@Data
	public static class UnitResponse {
		String message;
	}

	@ParameterizedTest
	@ValueSource(strings = { "city1=London&thresholdDate=2017-01-17", "city1=London&maxNDays=10" })
	void testWeatherReport(final String queryString) throws Exception {
		final ResponseEntity<UnitResponse> response = restTemplate
				.getForEntity("http://localhost:" + port + "/weather/1.0/forecast?" + queryString, UnitResponse.class);
		assertThat(response.getStatusCode().is5xxServerError()).isTrue();
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "city1=London&thresholdDate=2017-01-17", "city1=London&maxNDays=10" })
	void testWeatherReport2(final String queryString) throws Exception {
		final ResponseEntity<UnitResponse> response = restTemplate
				.getForEntity("http://localhost:" + port + "/weather/2.0/forecast?" + queryString, UnitResponse.class);

		assertThat(response.getStatusCode().is5xxServerError()).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = { "1.0", "2.0" })
	void testGetWeatherPredictionByLocationForInvalidRequest3(final String version)
			throws RestClientException, MalformedURLException {

		final ResponseEntity<UnitResponse> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/weather/" + version + "/forecast?" + "city=London&maxNDays=10",
				UnitResponse.class);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = { "1.0", "2.0" })
	void testGetWeatherPredictionByLocationForEmptyResponse(final String version) throws RestClientException, MalformedURLException {
		final ResponseEntity<UnitResponse> response = restTemplate
				.getForEntity("http://localhost:" + port + "/weather/"
						+ version
						+ "/forecast?city=London", UnitResponse.class);

		final UnitResponse body = response.getBody();

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		final Object message = body.getMessage();
		assertThat(message).isInstanceOf(String.class);
	}

	@ParameterizedTest
	@ValueSource(strings = { "1.0", "2.0" })
	void testGetWeatherPredictionByLocationMinGtMax(final String version) throws RestClientException, MalformedURLException {
		final ResponseEntity<UnitResponse> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/weather/"
						+ version
						+ "/forecast?city=London&minNDays=4&maxNDays=2",
				UnitResponse.class);

		final UnitResponse body = response.getBody();

		assertThat(response.getStatusCode().is4xxClientError()).isTrue();
		final Object message = body.getMessage();
		assertThat(message).isInstanceOf(String.class);
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "1.0", "2.0" })
	void testGetWeatherPredictionByLocationForValidResponse(final String version) throws RestClientException, MalformedURLException {

		final ResponseEntity<UnitSuccessResponse> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/weather/"
						+ version
						+ "/forecast?" + "city=London&thresholdDate=2017-01-17",
				UnitSuccessResponse.class);

		final UnitSuccessResponse body = response.getBody();

		final List<Map<String, Object>> message = body.getMessage();
		assertThat(message).hasSize(3);
		final Map<String, Object> leafMap = message.iterator().next();
		final Object object3 = leafMap.get("warning");
		assertThat(object3).isEqualTo(messages
				.getMessage(WeatherServiceConstants.WEATHER_SERVICE_CONSTANTS_RAIN_WARNING, null, Locale.getDefault()));
	}
}
