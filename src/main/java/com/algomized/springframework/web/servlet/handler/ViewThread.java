package com.algomized.springframework.web.servlet.handler;

import com.algomized.springframework.http.converter.json.JsonViewAwareMessageConverter;
import com.algomized.springframework.web.servlet.handler.JsonViewInterceptor;

/**
 * Can be used to pass thread-local variables from the handler to the converter
 * during a request. 
 * 
 * <p>
 * One use case is to pass {@link Views} filter from  {@link JsonViewInterceptor}
 * to {@link JsonViewAwareMessageConverter}.
 * </p>
 * 
 * @author Poh Kah Kong
 */
public class ViewThread {
	private static final ThreadLocal<Class<?>[]> viewThread = new ThreadLocal<Class<?>[]>(); 

	/**
	 * Set a thread-local variable for passing. 
	 * 
	 * @param key the key to be passed
	 */
    public static void setKey(Class<?>[] key){ 
        viewThread.set(key);
    } 

    /**
     * Get the thread-local variable if present.
     * 
     * @return the key that is passed
     */
    public static Class<?>[] getKey(){
        return viewThread.get(); 
    } 
}
