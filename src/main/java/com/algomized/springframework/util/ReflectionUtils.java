package com.algomized.springframework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Class extension of {@link org.springframework.util.ReflectionUtils}
 * 
 * <p>Added static convenience methods for reflection.</p>
 * 
 * @author Poh Kah Kong
 *
 */
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {
	/**
	 * Get all the declared fields of the given target class and all its super classes.
	 * 
	 * @param targetClass the target class
	 * @return an array of all the fields
	 */
	public static Field[] getAllFields(Class<?> targetClass) {
		List<Field> fields = getAllFields(targetClass, new ArrayList<Field>());
		return fields.toArray(new Field[fields.size()]);
	}
	
	/**
	 * Executed recursively to get all the declared fields of the given target class and
	 * its super classes.
	 * 
	 * @param targetClass the target class
	 * @param fields a list to contains all the fields
	 * @return a list of all the fields
	 */
	private static List<Field> getAllFields(Class<?> targetClass, List<Field> fields) {
			Class<?> superClass = targetClass.getSuperclass();
		if (superClass != null) {
			getAllFields(superClass, fields);
		}
		fields.addAll(Arrays.asList(targetClass.getDeclaredFields()));
		return fields;
	}
		
	/**
	 * Find the getter method of the object class from the given field.
	 * 
	 * @param objectClass the object class
	 * @param field the field
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method findGetterFromField(Class<?> objectClass, Field field) 
			throws NoSuchMethodException, SecurityException {
		String methodName;
		if (!field.getType().equals(boolean.class))
			methodName =  "get" + StringUtils.capitalize(field.getName());
		else
			methodName = "is" + StringUtils.capitalize(field.getName());
		return objectClass.getMethod(methodName);
	}
	
	/**
	 * Find the setter method of the object class from the given field.
	 * 
	 * @param objectClass the object class
	 * @param field the field
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */	
	public static Method findSetterFromField(Class<?> objectClass, Field field) 
			throws NoSuchMethodException, SecurityException {
		String methodName = "set" + StringUtils.capitalize(field.getName());
		return objectClass.getMethod(methodName, field.getType());
	}	


	/**
	 * Find the field of the object class from the given method
	 * 
	 * @param objectClass the object class
	 * @param method the method
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field findFieldFromMethod(Class<?> objectClass, Method method) 
			throws NoSuchFieldException, SecurityException {
    	String fieldName = StringUtils.uncapitalize(method.getName().substring(3));
    	return objectClass.getField(fieldName);    	
	}
	
	/**
	 * Check whether the given value is null or zero.
	 * 
	 * @param value the value to check
	 * @return true if value is null or zero
	 * <br> false if value is not null and not zero
	 */
	public static boolean isNullOrZero(Object value) {
		if (value == null) {
			return true;
		}		
		if (value instanceof Number) {
			Number numberValue = (Number) value;
			return numberValue.intValue() == 0;
		}
		return false;		
	}
	
	/**
	 * Find the subclass of the superclass by the given name. 
	 * 
	 * @param superClass the super class
	 * @param name the name of the sub class
	 * @return the class of the subclass if class is found
	 * @throws ClassNotFoundException
	 */
	public static Class<?> findSubClassByName(Class<?> superClass, String name) 
			throws ClassNotFoundException {
		String packageName = superClass.getPackage().getName();				
		Class<?> subClass = Class.forName(packageName + "." + name);
		try {
			subClass.asSubclass(superClass);
			return subClass;
		} catch (ClassCastException e) {
			throw new ClassNotFoundException();
		}
	}
}
