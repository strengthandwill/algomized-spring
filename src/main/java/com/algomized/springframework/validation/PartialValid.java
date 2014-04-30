package com.algomized.springframework.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Annotation that binds a method parameter with {@link javax.validation.Valid @Valid} 
 * for partial validation such that only the field(s) of the object that are not null 
 * are validated.
 *
 * @author Poh Kah Kong
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PartialValid {
}