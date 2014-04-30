package com.algomized.springframework.http;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.algomized.springframework.http.error.RuntimeError;

/**
 * An abstract RuntimeException class that includes an error code and http error code 
 * of 400 Bad Request. These attributes are pass to a {@link RuntimeError} object in 
 * a {@link ResponseEntity} during exceptions handling.
 * 
 * @author Poh Kah Kong
 *
 */
@SuppressWarnings("serial")
public abstract class HttpRuntimeException extends NestedRuntimeException {
	public HttpRuntimeException(String message) {
		super(message);
	}

	/**
	 * Subclass has to implement the getter to return the error code
	 * for the exception.
	 * 
	 * @return the error code
	 */
	public abstract String getErrorCode();

	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	/**
	 * RuntimeException has http error code of 400 Bad Request.
	 * 
	 * @return {@code HttpStatus.BAD_REQUEST} 
	 */
	public HttpStatus getHttpErrorCode() {
		return HttpStatus.BAD_REQUEST;
	}	
}
