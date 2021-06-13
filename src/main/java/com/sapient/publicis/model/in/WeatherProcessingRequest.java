package com.sapient.publicis.model.in;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.Data;

@Data
public class WeatherProcessingRequest {

	private final String locations;
	private final Date minDate;
	private final Date maxDate;
	private final Locale locale;

	public WeatherProcessingRequest(final String locations, final Date thresholdDate, final int minNextDays,
			final int maxNextDays, final String language) {
		this.locations = locations;

		final Calendar cal = Calendar.getInstance();
		if (thresholdDate != null) {
			cal.setTime(thresholdDate);
		}
		cal.add(Calendar.DATE, minNextDays);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		this.minDate = cal.getTime();

		cal.add(Calendar.DATE, maxNextDays + 1 - minNextDays);
		this.maxDate = cal.getTime();
		
		this.locale = new Locale(language);
	}

}
