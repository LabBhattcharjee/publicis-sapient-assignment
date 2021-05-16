package com.sapient.publicis.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
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

	@Data
	public static class UnitResponse {
		Object message;
	}

	@ParameterizedTest
	@ValueSource(strings = { "city1=London&thresholdDate=2017-01-17", "city1=London&maxNDays=10"
	// , "city=London&maxNDays=10"
	})
	void testWeatherReport(final String queryString) throws Exception {
		final ResponseEntity<UnitResponse> response = restTemplate
				.getForEntity("http://localhost:" + port + "/weather/1.0/forecast?" + queryString, UnitResponse.class);

		assertThat(response.getStatusCode().is5xxServerError()).isTrue();
	}

	@Test
	void testGetWeatherPredictionByLocationForInvalidRequest3() throws RestClientException, MalformedURLException {

		final ResponseEntity<UnitResponse> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/weather/1.0/forecast?" + "city=London&maxNDays=10", UnitResponse.class);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
	}

	@Test
	void testGetWeatherPredictionByLocationForEmptyResponse() throws RestClientException, MalformedURLException {
		final ResponseEntity<UnitResponse> response = restTemplate
				.getForEntity("http://localhost:" + port + "/weather/1.0/forecast?city=London", UnitResponse.class);

		final UnitResponse body = response.getBody();

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		final Object message = body.getMessage();
		assertThat(message).isInstanceOf(String.class);
	}

	@Test
	void testGetWeatherPredictionByLocationMinGtMax() throws RestClientException, MalformedURLException {
		final ResponseEntity<UnitResponse> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/weather/1.0/forecast?city=London&minNDays=4&maxNDays=2",
				UnitResponse.class);

		final UnitResponse body = response.getBody();

		assertThat(response.getStatusCode().is4xxClientError()).isTrue();
		final Object message = body.getMessage();
		assertThat(message).isInstanceOf(String.class);
	}

	@Test
	void testGetWeatherPredictionByLocationForValidResponse() throws RestClientException, MalformedURLException {

		final ResponseEntity<UnitResponse> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/weather/1.0/forecast?" + "city=London&thresholdDate=2017-01-17",
				UnitResponse.class);

		final UnitResponse body = response.getBody();

		final Object message = body.getMessage();
		assertThat(message).isInstanceOf(Map.class);

		final Map<?, ?> responseMap = (Map<?, ?>) message;
		assertThat(responseMap).hasSize(1);
		final Object object = responseMap.get("dateWiseWeatherReport");

		assertThat(object).isInstanceOf(List.class);

		final List<?> list = (List<?>) object;
		final Object object2 = list.iterator().next();
		assertThat(object2).isInstanceOf(Map.class);
		final Map<?, ?> leafMap = (Map<?, ?>) object2;
		final Object object3 = leafMap.get("warning");
		assertThat(object3).isEqualTo(WeatherServiceConstants.RAIN_WARNING);
	}
}
