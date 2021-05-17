package com.sapient.publicis.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableMap;
import com.sapient.publicis.model.in.DateSpecifcAggregateData;
import com.sapient.publicis.model.in.WeatherProcessingRequest;
import com.sapient.publicis.model.in.WeatherProcessingResponse;
import com.sapient.publicis.model.out.WeatherResponse;
import com.sapient.publicis.service.DateWiseSummaryStatistics;
import com.sapient.publicis.service.WeatherService;
import com.sapient.publicis.service.exchange.WeatherRestExchange;
import com.sapient.publicis.util.WeatherServiceConstants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

	private final WeatherRestExchange restTemplateHandler;

	public WeatherServiceImpl(@Autowired final WeatherRestExchange restTemplateHandler) {
		this.restTemplateHandler = restTemplateHandler;
	}

	@Override
	@Cacheable(value = "weatherCache", key = "#weatherProcessingRequest.locations.concat('_')"
			+ ".concat(#weatherProcessingRequest.maxDate.getTime)"
			+ ".concat(#weatherProcessingRequest.minDate.getTime)")
	public WeatherProcessingResponse process(final WeatherProcessingRequest weatherProcessingRequest) {
		log.error("not returning from cache");
		final WeatherResponse body = restTemplateHandler.retreive(WeatherResponse.class,
				ImmutableMap.of("cityList", weatherProcessingRequest.getLocations()));

		final DateWiseSummaryStatistics dateWiseSummaryStatistics = body.getList().stream()
				.filter(a -> a.isInExpectedDuration(weatherProcessingRequest)).collect(DateWiseSummaryStatistics::new,
						DateWiseSummaryStatistics::accept, DateWiseSummaryStatistics::combine);

		final Set<DateSpecifcAggregateData> dateWiseWeatherReport = dateWiseSummaryStatistics
				.getDateWiseWeatherReport();
		return new WeatherProcessingResponse(CollectionUtils.isEmpty(dateWiseWeatherReport) ? WeatherServiceConstants.getNoDataMessage()
				: dateWiseSummaryStatistics);
	}
}
