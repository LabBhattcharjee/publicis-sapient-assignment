package com.sapient.publicis.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.publicis.model.out.WeatherResponse;
import com.sapient.publicis.service.exchange.WeatherRestExchange;
import com.sapient.publicis.util.WeatherServiceConstants;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherServiceControllerMockTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherRestExchange weatherRestExchange;

	@Test
	@DisplayName("GET Weather Report success")
	void testWeatherReportSuccess() throws Exception {

		final ObjectMapper objectMapper = new ObjectMapper();

		final WeatherResponse retVal = objectMapper
				.readValue(WeatherServiceControllerMockTest.class.getResource("/test1.json"), WeatherResponse.class);
		// When using matchers, all arguments have to be provided by matchers.
		doReturn(retVal).when(weatherRestExchange).retreive(Mockito.any(), Mockito.anyMap());

		// Execute the GET request
		mockMvc.perform(get("/weather/1.0/forecast?" + "city=London&thresholdDate=2017-01-17"))
				// Validate the response code and content type
				.andExpect(status().isOk())
				// .andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate headers
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

				// Validate the returned fields
				.andExpect(jsonPath("message.dateWiseWeatherReport", hasSize(3)))
				//
				.andExpect(	jsonPath("message.dateWiseWeatherReport[0].warning", is(WeatherServiceConstants.getRainWarningMessage())))

		;
	}

	@Test
	@DisplayName("GET Weather Report Empty Resposne")
	void testWeatherReportSuccessEmpty() throws Exception {

		final ObjectMapper objectMapper = new ObjectMapper();

		final WeatherResponse retVal = objectMapper
				.readValue(WeatherServiceControllerMockTest.class.getResource("/test1.json"), WeatherResponse.class);
		// When using matchers, all arguments have to be provided by matchers.
		doReturn(retVal).when(weatherRestExchange).retreive(Mockito.any(), Mockito.anyMap());

		// Execute the GET request
		mockMvc.perform(get("/weather/1.0/forecast?" + "city=London"))
				// Validate the response code and content type
				.andExpect(status().isOk())
				// .andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate headers
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

				// Validate the returned fields

				.andExpect(jsonPath("message", is(WeatherServiceConstants.getNoDataMessage())))
				.andExpect(jsonPath("message.dateWiseWeatherReport").doesNotExist())

		;
	}

	@ParameterizedTest
	@ValueSource(strings = { "city1=London&thresholdDate=2017-01-17", "city1=London&maxNDays=10",
			"city=London&maxNDays=10" })
	void testWeatherReport(final String queryString) throws Exception {

		doReturn(Optional.empty()).when(weatherRestExchange).retreive(Mockito.any(), Mockito.anyMap());

		// Execute the GET request
		mockMvc.perform(get("/weather/1.0/forecast?" + queryString))
				// Validate the response code and content type
				.andExpect(status().is5xxServerError())
				// .andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate headers
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

				// Validate the returned fields
				.andExpect(jsonPath("message", is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())))

		;
	}

}
