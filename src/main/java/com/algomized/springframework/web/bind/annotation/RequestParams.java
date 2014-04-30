package com.algomized.springframework.web.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Annotation that binds a method parameter or method return value
 * to a named request params, exposed to a web view. Supported
 * for controller classes with {@link RequestMapping @RequestMapping}
 * methods.
 *
 * @author Poh Kah Kong
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParams {
}