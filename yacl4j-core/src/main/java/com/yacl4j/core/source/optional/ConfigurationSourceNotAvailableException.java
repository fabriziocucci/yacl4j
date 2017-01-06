package com.yacl4j.core.source.optional;

public class ConfigurationSourceNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigurationSourceNotAvailableException() {
		super();
	}

	public ConfigurationSourceNotAvailableException(String message) {
		super(message);
	}

	public ConfigurationSourceNotAvailableException(Throwable cause) {
		super(cause);
	}
	
	public ConfigurationSourceNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConfigurationSourceNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
