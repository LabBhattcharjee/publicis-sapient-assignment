package com.sapient.publicis.controller.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sapient.publicis.model.in.WeatherProcessingResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

	private ResponseEntity<WeatherProcessingResponse> createResponseEntity(final HttpStatus status, final String message) {// NOSONAR
		final List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		final HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setAccept(acceptableMediaTypes);
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(new WeatherProcessingResponse(message), responseHeaders, status);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<WeatherProcessingResponse> generic(final MethodArgumentNotValidException e) {// NOSONAR
		log.error(HttpStatus.BAD_REQUEST.getReasonPhrase(), e);
		return createResponseEntity(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<WeatherProcessingResponse> generic(final Exception e) {// NOSONAR
		log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
		return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	}
}