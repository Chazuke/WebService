package com.web.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebServiceExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<WebServiceException> defaultExceptionHandler(Exception e) {
		WebServiceException error = new WebServiceException(e.getMessage());
		return new ResponseEntity<WebServiceException>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
