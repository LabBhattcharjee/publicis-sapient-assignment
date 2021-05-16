package com.sapient.publicis.model.in;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@NoArgsConstructor  // for Junit
public class WeatherProcessingResponse {

	private final Object message;

}
