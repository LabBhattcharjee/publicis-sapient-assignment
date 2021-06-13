package com.sapient.publicis.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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

	@Autowired
	private MessageSource messages;

	@ParameterizedTest
	@ValueSource(strings = { "1.0", "2.0" })
	@DisplayName("GET Weather Report success")
	void testWeatherReportSuccess(final String version) throws Exception {

		final ObjectMapper objectMapper = new ObjectMapper();

		final WeatherResponse retVal = objectMapper
				.readValue(WeatherServiceControllerMockTest.class.getResource("/test1.json"), WeatherResponse.class);
		// When using matchers, all arguments have to be provided by matchers.
		doReturn(retVal).when(weatherRestExchange).retreive(Mockito.any(), Mockito.anyMap());

		// Execute the GET request
		mockMvc.perform(get("/weather/"
				+ version //"1.0"
				+ "/forecast?" + "city=London&thresholdDate=2017-01-17"))
				// Validate the response code and content type
				.andExpect(status().isOk())
				// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				// Validate headers
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

				// Validate the returned fields
				.andExpect(jsonPath("message", hasSize(3)))
				//
				.andExpect(jsonPath("message[0].warning",
						is(messages.getMessage(WeatherServiceConstants.WEATHER_SERVICE_CONSTANTS_RAIN_WARNING, null,
								Locale.getDefault()))))

		;
	}

	@ParameterizedTest
	@ValueSource(strings = { "1.0", "2.0" })
	@DisplayName("GET Weather Report Empty Resposne")
	void testWeatherReportSuccessEmpty(final String version) throws Exception {

		final ObjectMapper objectMapper = new ObjectMapper();

		final WeatherResponse retVal = objectMapper
				.readValue(WeatherServiceControllerMockTest.class.getResource("/test1.json"), WeatherResponse.class);
		// When using matchers, all arguments have to be provided by matchers.
		doReturn(retVal).when(weatherRestExchange).retreive(Mockito.any(), Mockito.anyMap());

		// Execute the GET request
		mockMvc.perform(get("/weather/"
				+ version //"1.0"
				+ "/forecast?" + "city=London"))
				// Validate the response code and content type
				.andExpect(status().isOk())
				// .andExpect(content().contentType(MediaType.APPLICATION_JSON))

				// Validate headers
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))

				// Validate the returned fields

				.andExpect(jsonPath("message",
						is(messages.getMessage(WeatherServiceConstants.WEATHER_SERVICE_CONSTANTS_NO_DATA, null,
								Locale.getDefault()))))
				.andExpect(jsonPath("message.dateWiseWeatherReport").doesNotExist())

		;
	}

	@ParameterizedTest
	@ValueSource(strings = { "city1=London&thresholdDate=2017-01-17", "city1=London&maxNDays=10",
			"city=London&maxNDays=10" })
	void testWeatherReport(final String queryString) throws Exception {
		versioned(queryString, "1.0");
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "city1=London&thresholdDate=2017-01-17", "city1=London&maxNDays=10",
			"city=London&maxNDays=10" })
	void testWeatherReport2(final String queryString) throws Exception {
		versioned(queryString, "2.0");
	}


	/**
	 * @param queryString
	 * @param version
	 * @throws Exception
	 */
	private void versioned(final String queryString, final String version) throws Exception {
		doReturn(Optional.empty()).when(weatherRestExchange).retreive(Mockito.any(), Mockito.anyMap());

		// Execute the GET request
		
		mockMvc.perform(get("/weather/"
				+ version
				+ "/forecast?" + queryString))
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
