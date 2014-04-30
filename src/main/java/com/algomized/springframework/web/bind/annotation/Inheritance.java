package com.algomized.springframework.web.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Annotation to indicate that the annotated parameter is  a super class entity. 
 * Its sub classes entity must be in the same package and their name are specified 
 * using value attribute, with default value attribute is "type".
 *
 * @author Poh Kah Kong 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Inheritance {
	String value() default "type";	
}