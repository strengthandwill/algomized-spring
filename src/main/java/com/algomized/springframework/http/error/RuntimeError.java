package com.algomized.springframework.http.error;

import com.algomized.springframework.http.HttpRuntimeException;

/**
 * RuntimeError that is an extension of {@link Error}.
 * A {@link HttpRuntimeException} object will its pass error code and error 
 * description attributes to this error object.
 * 
 * @author Poh Kah Kong
 *
 */
public class RuntimeError extends Error {
	private String errorDescription;
	
	public RuntimeError() {
		super();
	}
	
	public RuntimeError(String error, String errorDescription) {
		super(error);
		this.errorDescription = errorDescription;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
