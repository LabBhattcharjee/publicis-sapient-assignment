/**
 * 
 */
package com.sapient.publicis.exception;

/**
 * @author LabB
 *
 */
public class WeatherServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1199693209510344266L;

	public WeatherServiceException() {
	}

	public WeatherServiceException(final String message, final Throwable cause, final boolean enableSuppression,
			final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WeatherServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WeatherServiceException(final String message) {
		super(message);
	}

	public WeatherServiceException(final Throwable cause) {
		super(cause);
	}

	
}
