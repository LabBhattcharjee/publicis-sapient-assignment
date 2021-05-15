package com.sapient.publicis.model.out;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sapient.publicis.model.in.WeatherProcessingRequest;

import json.deserializers.DateTxtDeserializer;
import json.deserializers.TimeStampDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListData {

	private static final ThreadLocal<DateFormat> FORMAT = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-DD"));

	@JsonDeserialize(using = TimeStampDeserializer.class)
	@JsonProperty("dt")
	private Date dt;
	@Setter(AccessLevel.NONE)
	private String dateOnly;
	@JsonProperty("main")
	private MainDetails main;
	@JsonProperty("weather")
	private List<Weather> weather;
	@JsonProperty("clouds")
	private Clouds clouds;
	@JsonProperty("wind")
	private Wind wind;
	@JsonProperty("sys")
	private Sys sys;

	@Setter(AccessLevel.NONE)
	@JsonProperty("dt_txt")
	private Date dtTxt;

	@JsonDeserialize(using = DateTxtDeserializer.class)
	public void setDtTxt(final Date dtTxt) {
		this.dtTxt = dtTxt;
		this.dateOnly = FORMAT.get().format(dtTxt);
	}

	public boolean isInExpectedDuration(final WeatherProcessingRequest weatherProcessingRequest) {
		return dtTxt.compareTo(weatherProcessingRequest.getMinDate()) >= 0
				&& dtTxt.compareTo(weatherProcessingRequest.getMaxDate()) < 0;
	}

}
