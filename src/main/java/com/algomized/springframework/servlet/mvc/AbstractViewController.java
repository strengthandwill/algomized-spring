package com.algomized.springframework.servlet.mvc;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Abstract controller for view requests.
 * 
 * @author Poh Kah Kong
 */

public abstract class AbstractViewController {		
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
	 * Converts empty strings into null when a form is submitted.
	 * 
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));  
	}	
}
