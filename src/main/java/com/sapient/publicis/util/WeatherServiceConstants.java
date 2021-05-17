/**
 * 
 */
package com.sapient.publicis.util;

/**
 * @author LabB
 *
 */
public class WeatherServiceConstants {

	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"; //$NON-NLS-1$

	public static String getNoDataMessage() {
		return Messages.getString("WeatherServiceConstants.no.data");
	}

	public static String getRainWarningMessage() {
		return Messages.getString("WeatherServiceConstants.rain.warning");
	}

	public static String getHeatWarningMessage() {
		return Messages.getString("WeatherServiceConstants.heat.warning");
	}

}
