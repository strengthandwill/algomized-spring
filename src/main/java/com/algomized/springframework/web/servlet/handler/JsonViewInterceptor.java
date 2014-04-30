package com.algomized.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.algomized.springframework.http.converter.json.JsonViewAwareMessageConverter;
import com.fasterxml.jackson.annotation.JsonView;


/**
 * Extracts {@link JsonView @JsonView} annotation from method and set {@link Views} filter
 * into a {@link ViewThread} object.
 * 
 * <p> 
 * {@link JsonViewAwareMessageConverter} will extract the view filter and 
 * serialize objects using the view filter.
 * </p>
 * 
 * @author Poh Kah Kong
 *
 */
public class JsonViewInterceptor extends HandlerInterceptorAdapter {
    @Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {    	
    	HandlerMethod handlerMethod = (HandlerMethod) handler;
        JsonView jsonViewAnnotation = handlerMethod.getMethodAnnotation(JsonView.class);     	
    	 if (jsonViewAnnotation != null) { 
             ViewThread.setKey(jsonViewAnnotation.value());
    	 }        
		return true;
	}
}
