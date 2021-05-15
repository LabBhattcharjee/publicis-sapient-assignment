package com.sapient.publicis.service.exchange.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.collect.ImmutableMap;
import com.sapient.publicis.exception.WeatherServiceException;
import com.sapient.publicis.service.exchange.WeatherRestExchange;

@Service
@ConditionalOnExpression("#{'${reactiveMode}' == 'true' }")
public class WebClientHandler implements WeatherRestExchange {

	@Value("${apiKey}")
	private String apiKey;

	@Value("${weatherAPIVersion}")
	private String weatherAPIVersion;

	private Map<String, String> globalMap;

	@PostConstruct
	public void init() {
		globalMap = ImmutableMap.of("APP_KEY", apiKey, "DEST_API_VERSION", weatherAPIVersion);
	}

	@Override
	public <T> T retreive(final Class<T> type, final Map<String, ?> map) {
		final Map<String, Object> mergedMap = new HashMap<>(map);
		mergedMap.putAll(globalMap);

		final T body = WebClient.builder().baseUrl(BASE_URL).defaultUriVariables(mergedMap).build().get().retrieve()
				.bodyToMono(type).block();

		if (body == null) {
			throw new WeatherServiceException("No record found");
		}

		return body;
	}

}
