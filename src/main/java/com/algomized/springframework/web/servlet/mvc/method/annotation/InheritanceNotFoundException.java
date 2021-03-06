package com.algomized.springframework.web.servlet.mvc.method.annotation;

import com.algomized.springframework.http.HttpRuntimeException;
import com.algomized.springframework.web.bind.annotation.Inheritance;

/**
 * RuntimeException thrown when the request parameter "type" required for the conversion 
 * of target object to its subclass is invalid during web binding. This conversion will occur if 
 * {@link Inheritance @Inherit} is present in the method parameter.
 * 
 * <p> Carries invalid value of the type.</p>
 * 
 * @author Poh Kah Kong
 */
@SuppressWarnings("serial")
public class InheritanceNotFoundException extends HttpRuntimeException {
	public InheritanceNotFoundException(String type) {
		super("Invalid type: " + type);
	}

	@Override
	public String getErrorCode() {
		return "invalid_type";
	}	
}