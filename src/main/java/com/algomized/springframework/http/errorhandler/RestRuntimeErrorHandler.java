package com.algomized.springframework.http.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.algomized.springframework.http.HttpRuntimeException;
import com.algomized.springframework.http.error.Error;
import com.algomized.springframework.http.error.RuntimeError;

/**
 * Handles {@link HttpRuntimeException} that occurs during a rest api request.
 * 
 * <p> 
 * Extracts the error code and description to return a {@link RuntimeError} 
 * object as response. 
 * </p>
 * 
 * @author Poh Kah Kong
 *
 */
public class RestRuntimeErrorHandler implements RestErrorHandler {
	
	/**
	 * Handles {@link HttpRuntimeException} that occurs during a rest api request.
	 * Returns a {@link RuntimeError} object as response. 
	 */
	public ResponseEntity<Error> handleException(Exception e) {
		HttpRuntimeException runtimeException = (HttpRuntimeException) e;
		RuntimeError runtimeError = new RuntimeError();
		runtimeError.setError(runtimeException.getErrorCode());
		runtimeError.setErrorDescription(runtimeException.getMessage());
		return new ResponseEntity<Error>(runtimeError, HttpStatus.BAD_REQUEST);
	}	
}

