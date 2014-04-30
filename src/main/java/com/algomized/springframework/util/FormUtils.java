package com.algomized.springframework.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Static convenience methods for form.</p>
 * 
 * @author Kah Kong
 *
 */
public class FormUtils {
	/**
	 * Get the label and value for select options of forms from the given enum class.
	 * 
	 * @param enumClass the enum class
	 * @return a map of label and value
	 */
	public static Map<String, String> getOptionsFromEnum(Class<? extends Enum<?>> enumClass) {
		Map<String, String> map = new HashMap<String, String>();
		Enum<?> [] enumConstants = enumClass.getEnumConstants();
		for (Enum<?> enumConstant : enumConstants) {
			map.put(enumConstant.toString(), getLabel(enumConstant.toString()));
		}
		return map;
	}
	
	/**
	 * Get the label from the given value.
	 * 
	 * @param value the value
	 * @return the label
	 */
	private static String getLabel(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
}
