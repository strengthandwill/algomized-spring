package com.algomized.springframework.http.errorhandler;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.algomized.springframework.http.error.Error;
import com.algomized.springframework.http.error.ValidationError;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;

/**
 * Handles {@link BindException} throws validation during web binding 
 * for rest api request.
 * 
 * <p> 
 * Extracts the field errors and their messages from {@link BindingResult} 
 * to construct a list of {@link ValidationError} objects as response. 
 * </p>
 * 
 * @author Kah Kong
 */
public class RestValidationErrorHandler implements RestErrorHandler {
	@Autowired
	private MessageSource messageSource;
	
    @Autowired
    private PropertyNamingStrategyBase propertyNamingStrategyBase;	
	
    /**
     *  Handles {@link BindException} throws validation during web binding
     *  for a rest api request. Returns a list of {@link ValidationError} objects
     *  as response.
     * 
     */
	public ResponseEntity<Error> handleException(Exception e) {
		BindException bindException = (BindException) e;
		BindingResult bindingResult = bindException.getBindingResult();
		ValidationError validationError = processFieldErrors(bindingResult.getFieldErrors());
		return new ResponseEntity<Error>(validationError, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Process field errors to construct a list of {@link ValidationError} objects. 
	 * 
	 * @param fieldErrors the field errors
	 * @return a list of validation error objects
	 */
	private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
		ValidationError validationError = new ValidationError();
		for (FieldError fieldError: fieldErrors) {
			validationError.addFieldError(propertyNamingStrategyBase.translate(fieldError.getField()), 
					fieldError.getDefaultMessage());
		}
		return validationError;
	}
	
	/**
	 * Resolves the localized error message for the given field error.
	 * 
	 * @param fieldError the field error
	 * @return the localized error message
	 */
	@SuppressWarnings("unused")
	private String resolveLocalizedErrorMessage(FieldError fieldError) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(fieldError, currentLocale);		
	}
}