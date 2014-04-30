package com.algomized.springframework.http.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindException;

/**
 * ValidationError that is an extension of {@link Error}.
 * A {@link BindException} object will its pass field errors 
 * and descriptions to this error object.
 * 
 * @author Poh Kah Kong
 *
 */
public class ValidationError extends Error {	
    private List<FieldError> fieldErrors = new ArrayList<FieldError>();

    public void addFieldError(String field, String errorDescription) {
        FieldError fieldError = new FieldError(field, errorDescription);
        fieldErrors.add(fieldError);
    }
    
    @Override
    public String getError() {
    	return "invalid_fields";
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
        
    public class FieldError {    
        private String field;
    	
        private String errorDescription;

        public FieldError(String field, String errorDescription) {
            this.field = field;
            this.errorDescription = errorDescription;
        }

        public String getField() {
            return field;
        }

        public String getErrorDescription() {
            return errorDescription;
        }
    }    
}