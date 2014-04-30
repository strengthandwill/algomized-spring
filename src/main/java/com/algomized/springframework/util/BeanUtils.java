package com.algomized.springframework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Id;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import com.algomized.springframework.validation.ReadOnly;

/**
 * Class extension of {@link org.springframework.beans.BeanUtils}
 * 
 * <p> Added static convenience methods for JavaBeans.</p>
 * 
 * @author Poh Kah Kong
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {	
	/**
	 * Copy the property values of the given source bean into the target bean.
	 * Properties annotated with {@link ReadOnly @ReadOnly} are ignored.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
	 * @param source the source bean
	 * @param target the target bean
	 * @throws BeansException if the copying failed
	 */
	public static void copyProperties(Object source, Object target) throws BeansException {
		Class<?> sourceClass = source.getClass();		
		Class<?> targetClass = target.getClass();
		
		// object must be of the same type
		if (!sourceClass.equals(targetClass)) {
			return;
		}
		
		Field[] fields = ReflectionUtils.getAllFields(targetClass);		
		for (Field field : fields) {
			try {
				Method sourceGetter = ReflectionUtils.findGetterFromField(sourceClass, field);
				Object value = sourceGetter.invoke(source);
				Method targetSetter = ReflectionUtils.findSetterFromField(targetClass, field);				

				boolean readOnly = field.getAnnotation(Id.class) != null || 
						field.getAnnotation(ReadOnly.class) != null;	
				if (!readOnly && !ReflectionUtils.isNullOrZero(value)) {
					targetSetter.invoke(target, value);
				}
			} catch (Throwable ex) {
				throw new FatalBeanException(
						"Could not copy property '" + target.getClass().getName() + "' from source to target", ex);
			}
		}
	}	
}
