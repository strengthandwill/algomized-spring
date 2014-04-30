package com.algomized.springframework.validation.constraintvalidator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.algomized.springframework.validation.constraint.EnumValues;

/**
 * Validate that the object is one of the given enum values.
 * 
 * @author Kah Kong
 */
public class EnumValuesValidator implements ConstraintValidator<EnumValues, String> {
	private List<String> values = new ArrayList<String>();

	@Override
	public void initialize(EnumValues enumValue) {
		values = new ArrayList<String>();
		Enum<?> [] enumConstants = enumValue.enumClass().getEnumConstants();
		for (Enum<?> enumConstant : enumConstants) {
			values.add(enumConstant.toString());
		}
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}		
		return values.contains(value);
	}
}
