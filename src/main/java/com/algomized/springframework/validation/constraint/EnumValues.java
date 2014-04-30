package com.algomized.springframework.validation.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.algomized.springframework.validation.constraintvalidator.EnumValuesValidator;

/**
 * The annotated element must be one of the given enum values.
 * 
 * @author Kah Kong
 */
@Constraint(validatedBy = EnumValuesValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValues {
	 String message() default "Invalid enum value";     
	 Class<?>[] groups() default {};	     
	 Class<? extends Payload>[] payload() default {};
	 Class<? extends Enum<?>> enumClass();
}
