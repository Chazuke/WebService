package com.web.service.error;

public class WebServiceError extends Error {

	private static final long serialVersionUID = -1733602873411196205L;

	WebServiceError(String message) {
		super(message);
	}

	WebServiceError(String message, Throwable cause) {
		super(message, cause);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
