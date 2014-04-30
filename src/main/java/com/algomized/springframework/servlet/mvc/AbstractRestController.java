package com.algomized.springframework.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.algomized.springframework.http.HttpRuntimeException;
import com.algomized.springframework.http.error.Error;
import com.algomized.springframework.http.error.ValidationError;
import com.algomized.springframework.http.error.RuntimeError;
import com.algomized.springframework.http.errorhandler.RestRuntimeErrorHandler;
import com.algomized.springframework.http.errorhandler.RestValidationErrorHandler;

/**
 * Abstract controller for rest requests.
 * 
 * <p> 
 * Handles {@link BindException} and {@link HttpRuntimeException} to 
 * returns error message as response.
 * </p>
 * 
 * @author Poh Kah Kong
 */

public abstract class AbstractRestController {	
	@Autowired
	private RestValidationErrorHandler restValidationErrorHandler;
	
	@Autowired
	private RestRuntimeErrorHandler restRuntimeErrorHandler;
	
	/**
	 * Throw {@link BindException} if {@link BindingResult result} 
	 * has errors.
	 * 
	 * @param result
	 * @throws BindException
	 */
	protected void hasErrors(BindingResult result) throws BindException {
		if (result.hasErrors()) {
			throw new BindException(result);
		}		
	}
	
	/**
	 * Handles {@link BindException} and returns the validation error messages
	 * as response.
	 * 
	 * @param e the bind exception
	 * @return a response entity with {@link ValidationError}
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Error> handleBindException(BindException e) {
		return restValidationErrorHandler.handleException(e);
	}	

	/**
	 * Handles {@link HttpRuntimeException} and returns the runtime error messages
	 * as response.
	 * 
	 * @param e the http runtime exception
	 * @return a response entity with {@link RuntimeError}
	 */
	@ExceptionHandler(HttpRuntimeException.class)
	public ResponseEntity<Error> handleRuntimeException(HttpRuntimeException e) throws Exception {		
		return restRuntimeErrorHandler.handleException(e);
	}
}
