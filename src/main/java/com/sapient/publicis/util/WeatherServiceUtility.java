/**
 * 
 */
package com.sapient.publicis.util;

/**
 * @author LabB
 *
 */
public class WeatherServiceUtility {

	private WeatherServiceUtility() {

	}

	public static double kelvinToDegreeCelcius(final double kelvin) {
		return kelvin - 273.15;
	}

}
