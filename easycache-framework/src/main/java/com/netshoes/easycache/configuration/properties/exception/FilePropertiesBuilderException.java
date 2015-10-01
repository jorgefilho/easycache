package com.netshoes.easycache.configuration.properties.exception;

public class FilePropertiesBuilderException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FilePropertiesBuilderException(String message) {
		super(message);
	}

	public FilePropertiesBuilderException(String message, Exception cause) {
		super(message, cause);
	}
}
