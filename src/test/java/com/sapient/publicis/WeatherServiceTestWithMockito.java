package com.sapient.publicis;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.publicis.model.in.DateSpecifcAggregateData;
import com.sapient.publicis.model.in.WeatherProcessingRequest;
import com.sapient.publicis.model.in.WeatherProcessingResponse;
import com.sapient.publicis.model.out.WeatherResponse;
import com.sapient.publicis.service.WeatherService;
import com.sapient.publicis.service.exchange.WeatherRestExchange;
import com.sapient.publicis.service.impl.WeatherServiceImpl;
import com.sapient.publicis.util.WeatherServiceConstants;

@SpringBootTest
class WeatherServiceTestWithMockito {

	@Mock
	private WeatherRestExchange weatherRestExchange;

	@BeforeEach
	void setMockOutput() throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();

		final WeatherResponse retVal = objectMapper
				.readValue(WeatherServiceTestWithMockito.class.getResource("/test1.json"), WeatherResponse.class);
		// When using matchers, all arguments have to be provided by matchers.
		Mockito.when(weatherRestExchange.retreive(Mockito.any(), Mockito.anyMap())).thenReturn(retVal);
	}

	@Test
	void testSuccess() throws ParseException {
		int date = 17;
		final WeatherProcessingRequest weatherProcessingRequest = new WeatherProcessingRequest("London",
				new SimpleDateFormat("yyyy-MM-DD").parse("2017-01-" + date++), 1, 3);

		final WeatherService weatherService = new WeatherServiceImpl(weatherRestExchange);
		final WeatherProcessingResponse weatherProcessingResponse = weatherService.process(weatherProcessingRequest);

		System.out.println(weatherProcessingResponse);

		final Object message = weatherProcessingResponse.getMessage();
		assertThat(message).isInstanceOf(Set.class);
		final Set<?> set = (Set<?>) message;
		assertThat(set.size()).isGreaterThanOrEqualTo(3);

		for (final Object object : set) {
			assertThat(object).isInstanceOf(DateSpecifcAggregateData.class);
			final DateSpecifcAggregateData aggregateData = (DateSpecifcAggregateData) object;
			assertThat(aggregateData.getDateValue()).isEqualTo("2017-01-" + date++);
		}

		final DateSpecifcAggregateData aggregateData = (DateSpecifcAggregateData) set.iterator().next();

		assertThat(aggregateData.getWarning()).isEqualTo(WeatherServiceConstants.RAIN_WARNING);


	}

}
