package com.sapient.publicis.service.exchange;

import java.util.Map;

public interface WeatherRestExchange {

	String BASE_URL = "https://samples.openweathermap.org/data/{DEST_API_VERSION}/forecast?q={cityList}&&appid={APP_KEY}";

	<T> T retreive(Class<T> type, Map<String, ?> map);

}
