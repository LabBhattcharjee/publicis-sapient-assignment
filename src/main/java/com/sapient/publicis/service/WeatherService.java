/**
 * 
 */
package com.sapient.publicis.service;

import com.sapient.publicis.model.in.WeatherProcessingRequest;
import com.sapient.publicis.model.in.WeatherProcessingResponse;

/**
 * @author LabB
 *
 */
public interface WeatherService {

	WeatherProcessingResponse process(WeatherProcessingRequest request);

	WeatherProcessingResponse process2(WeatherProcessingRequest weatherProcessingRequest);

}
