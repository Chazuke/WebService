package com.web.service.error;

public class WebServiceException extends Exception {

	private static final long serialVersionUID = -1733602873411196205L;

	WebServiceException(String message) {
		super(message);
	}

	WebServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
