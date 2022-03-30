package com.newlight77.kata.survey.exception;

public class CreatedException extends RuntimeException {

	public CreatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreatedException(String message) {
		super(message);
	}

}
