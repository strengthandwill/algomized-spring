package com.algomized.springframework.http.errorhandler;

import org.springframework.http.ResponseEntity;

import com.algomized.springframework.http.error.Error;

/**
 * Specifies the required methods as a RestErrorHandler.
 * 
 * @author Poh Kah Kong
 */
public interface RestErrorHandler {
	public ResponseEntity<Error> handleException(Exception e);
}
