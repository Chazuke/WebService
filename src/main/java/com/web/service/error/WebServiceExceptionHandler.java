package com.web.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebServiceExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<WebServiceError> defaultExceptionHandler() {
		WebServiceError error = new WebServiceError("Generic.Error");
		return new ResponseEntity<WebServiceError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
