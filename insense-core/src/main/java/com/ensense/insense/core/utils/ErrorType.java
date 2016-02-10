package com.ensense.insense.core.utils;

public enum ErrorType {
	
	TIME_OUT("Page timed out."), UNABLE_TO_TEST("Unable to Test"), ERROR_PAGE("Error page"), HTTP_STATUS("Invalid HTTP Status"), UNKNOWN_EXCEPTION("Unknown Exception");
	
	@SuppressWarnings("unused")
	private String errorType;
	
	private ErrorType(String value) {
		this.errorType = value;
	}
	public String getErrorType(){
		return errorType;
	}
}
