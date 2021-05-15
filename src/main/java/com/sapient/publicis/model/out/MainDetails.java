package com.sapient.publicis.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MainDetails {
	@JsonProperty("temp")
	private double temp;
	@JsonProperty("temp_min")
	private double tempMin;
	@JsonProperty("temp_max")
	private double tempMax;
	@JsonProperty("pressure")
	private double pressure;
	@JsonProperty("sea_level")
	private double seaLevel;
	@JsonProperty("grnd_level")
	private double grndLevel;
	@JsonProperty("humidity")
	private int humidity;
	@JsonProperty("temp_kf")
	private double tempKf;
}
