package com.sapient.publicis.model.in;

import java.util.Calendar;
import java.util.Date;

import lombok.Data;

@Data
public class WeatherProcessingRequest {

	private final String locations;
	private final Date minDate;
	private final Date maxDate;
	
	private int minNextDays;
	private int maxNextDays;
	//private String cacheKey;

	public WeatherProcessingRequest(final String locations, final Date thresholdDate, final int minNextDays, final int maxNextDays) {
		this.locations = locations;

		final Calendar cal = Calendar.getInstance();
		cal.setTime(thresholdDate);
		cal.add(Calendar.DATE, minNextDays);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		this.minDate = cal.getTime();

		cal.add(Calendar.DATE, maxNextDays + 1 - minNextDays);
		this.maxDate = cal.getTime();
		
		this.minNextDays = minNextDays;
		this.maxNextDays = maxNextDays;

		//this.cacheKey = String.format("%d_%d_%d_%s", minNextDays, maxNextDays, thresholdDate.getTime(), locations);
	}


}
