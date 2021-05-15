package com.sapient.publicis.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Wind {
	@JsonProperty("speed")
	private float speed;
	@JsonProperty("deg")
	private float deg;
}
