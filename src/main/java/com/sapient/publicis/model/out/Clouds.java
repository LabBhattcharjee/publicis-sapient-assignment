package com.sapient.publicis.model.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Clouds {
	@JsonProperty("all")
	private int all;
}
