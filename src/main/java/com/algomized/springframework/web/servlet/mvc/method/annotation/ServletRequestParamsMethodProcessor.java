package com.algomized.springframework.web.servlet.mvc.method.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import com.algomized.springframework.util.ReflectionUtils;
import com.algomized.springframework.validation.PartialValid;
import com.algomized.springframework.web.bind.annotation.Inheritance;
import com.algomized.springframework.web.bind.annotation.RequestParams;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;

import javax.validation.Valid;

/**
 * Method processor supports {@link RequestParams @RequestParams} for parameters renaming, 
 * {@link PartialValid @Partial} for partial validation and {@link Inheritance @Inherit} for
 * conversion of target object to its subclass.
 * 
 * <p>
 * Uses PropertyNamingStrategyBase for the renaming of properties. The PropertyNamingStrategyBase
 * is specified using {@link PropertyNamingStrategy Jackson's PropertyNamingStrategy}. 
 * Default PropertyNamingStrategyBase is LowerCaseWithUnderscoresStrategy.
 * </p>
 * 
 * <p>Performs partial validation by only validating field(s) that are not null.</p>
 * 
 * <p>
 * Converts of target object to its subclass if {@link Inheritance @Inherit} is present. 
 * The subclass is specified by value of request parameter "type".
 * </p>
 * 
 * @author Poh Kah Kong
 */

public class ServletRequestParamsMethodProcessor extends ServletModelAttributeMethodProcessor {
	protected final boolean annotationNotRequired;
	
    protected final Map<Class<?>, Map<String, String>> replaceMap = new ConcurrentHashMap<Class<?>, Map<String, String>>();
	
	@Autowired
    protected RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    
    protected PropertyNamingStrategyBase propertyNamingStrategyBase;
    
    public ServletRequestParamsMethodProcessor(boolean annotationNotRequired) {
		super(annotationNotRequired);
    	this.annotationNotRequired = annotationNotRequired;
	}    

    @Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestParams.class);
	}
    
	/**
	 * Return {@code true} if there is a method-level {@link RequestParams @RequestParams}
	 * or if it is a non-simple type when {@code annotationNotRequired=true}.
	 */
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		if (returnType.getMethodAnnotation(RequestParams.class) != null) {
			return true;
		}
		else if (this.annotationNotRequired) {
			return !BeanUtils.isSimpleProperty(returnType.getParameterType());
		}
		else {
			return false;
		}
	}

	@Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
		if (propertyNamingStrategyBase == null) {
			return;
		}
		
        Object target = binder.getTarget();
        Class<?> targetClass = target.getClass();
        if (!replaceMap.containsKey(targetClass)) {
            Map<String, String> mapping = analyzeClass(targetClass);
            replaceMap.put(targetClass, mapping);
        }
        Map<String, String> mapping = replaceMap.get(targetClass);
        RequestParamsDataBinder requestParamsDataBinder = new RequestParamsDataBinder(target, binder.getObjectName(), mapping);
        requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(requestParamsDataBinder, nativeWebRequest);
        super.bindRequestParameters(requestParamsDataBinder, nativeWebRequest);
    }	
	
	/**
	 * Analyze target class by its declared fields and methods and
	 * return the renamed map.
	 * 
	 * @param targetClass the target class
	 * @return
	 */
    protected Map<String, String> analyzeClass(Class<?> targetClass) {
        Map<String, String> renameMap = new HashMap<String, String>();
        analyzeClassByFields(targetClass, renameMap);
        analyzeClassByMethods(targetClass, renameMap);
        if (renameMap.isEmpty()) return Collections.emptyMap();
        return renameMap;
    }    

    /**
     * Analyze target class by its declared fields and add to the renamed map.
     * 
     * @param targetClass the target class
     * @param renameMap the map that contains the renamed fields
     * @return
     */
    protected void analyzeClassByFields(Class<?> targetClass, Map<String, String> renameMap) {
        Field[] fields = targetClass.getDeclaredFields();        
        for (Field field : fields) {
        	renameMap.put(propertyNamingStrategyBase.translate(field.getName()), field.getName());
        }
    }

    /**
     * Analyze target class by its declared methods and add to the renamed map.
     * 
     * @param targetClass the target class
     * @param renameMap the map that contains the renamed fields
     * @return
     */    
	protected void analyzeClassByMethods(Class<?> targetClass, Map<String, String> renameMap) {
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
        	try {
	        	String fieldName = ReflectionUtils.findFieldFromMethod(targetClass, method).getName();
	        	renameMap.put(propertyNamingStrategyBase.translate(fieldName), fieldName);
        	} catch (NoSuchFieldException e) {}
        }
    } 
    
    
    /**
     * RequestParamsDataBinder which supports fields renaming using {@link RequestParams @RequestParams}
     */
    class RequestParamsDataBinder extends ExtendedServletRequestDataBinder {

        private final Map<String, String> renameMapping;

        public RequestParamsDataBinder(Object target, String objectName, Map<String, String> renameMapping) {
            super(target, objectName);
            this.renameMapping = renameMapping;
        }

        @Override
        protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
            super.addBindValues(mpvs, request);
            for (Map.Entry<String, String> entry : renameMapping.entrySet()) {
                String from = entry.getKey();
                String to = entry.getValue();
                if (mpvs.contains(from)) {
                    mpvs.add(to, mpvs.getPropertyValue(from).getValue());
                }
            }
        }
    }    
    
    /**
     * Set custom strategy used for the renaming of properties. The PropertyNamingStrategyBase
     * is specified using {@link PropertyNamingStrategy Jackson's PropertyNamingStrategy}.
     * 
     * <p> Default PropertyNamingStrategyBase is LowerCaseWithUnderscoresStrategy.</p>  
     * 
     * @param propertyNamingStrategyBase the strategy for the naming of properties
     */
	public void setPropertyNamingStrategyBase(
			PropertyNamingStrategyBase propertyNamingStrategyBase) {
		this.propertyNamingStrategyBase = propertyNamingStrategyBase;
	}    
    
    /**
     * Performs full validation on the request params object if {@link Valid @Valid} is present.
     * 
     * <p> 
     * Performs partial validation on the request params object if {@link PartialValid @PartialValid} 
     * is present. Only field(s) that are not null are validated.
     * </p>
     */
    @Override
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(PartialValid.class)) {
			Object hints = AnnotationUtils.getValue(parameter.getParameterAnnotation(PartialValid.class));
			partialValidate(binder, hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});						
		} else {
			super.validateIfApplicable(binder, parameter);
		}		
	}     
    
    /**
     * Performs partial validation by only validating field(s) that are not null.
     * 
     * @param binder the data binder instance to use for the binding
     * @param validationHints one or more hint objects to be passed to a {@link SmartValidator}
     */
    protected void partialValidate(WebDataBinder binder, Object... validationHints) {
		Object target = binder.getTarget();
		BindingResult binderBindingResult = binder.getBindingResult();
		BindingResult bindingResult = new BeanPropertyBindingResult(target, binderBindingResult.getObjectName());    	
		for (Validator validator : binder.getValidators()) {
			if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
				((SmartValidator) validator).validate(target, bindingResult, validationHints);
			}
			else if (validator != null) {
				validator.validate(target, bindingResult);
			}
		}				
		for (FieldError fieldError: bindingResult.getFieldErrors()) {
			if (!fieldError.getCode().equals(NotNull.class.getSimpleName())) {
				binderBindingResult.addError(fieldError);
			}
		}		
    }
    	
	/**
	 * Returns {@code CHECK_INHERIT} if value is null. This is to allow the conversion of
	 * of the target class to its subclass in method {@code createAttributeFromRequestValue()} 
	 * if needed.
	 */
	@Override
	protected String getRequestValueForAttribute(String attributeName,
			NativeWebRequest request) {		
		String value = super.getRequestValueForAttribute(attributeName, request);
		if (value == null) {
			value = "CHECK_INHERIT";
		}		
		return value;		
	}	

	/**
	 * Converts target object to its subclass if {@link Inheritance @Inherit} is present. 
	 * The subclass is specified by value of request parameter "type".
	 */
	@Override
	protected Object createAttributeFromRequestValue(String sourceValue,
			String attributeName, MethodParameter parameter,
			WebDataBinderFactory binderFactory, NativeWebRequest request)
			throws Exception {
		if (sourceValue.equals("CHECK_INHERIT")) {
			return createInheritAttribute(parameter, request);
		} else {
			return super.createAttributeFromRequestValue(sourceValue, attributeName, 
					parameter, binderFactory, request);
		}
	}
	
	/**
	 * Creates an attribute object of the subclass of the target object.
	 * The subclass is specified by value of the request parameter "type".
	 * 
	 * @param parameter the parameter that contains the target object 
	 * @param request the request that contains the request parameter "type"
	 * @return an attribute object of the specified subclass
	 * @throws BeanInstantiationException
	 * @throws ClassNotFoundException
	 */
	protected Object createInheritAttribute(MethodParameter parameter, NativeWebRequest request) 
			throws InheritanceNotFoundException {
		if (!parameter.hasParameterAnnotation(Inheritance.class)) {
			return null;
		}
		String value = parameter.getParameterAnnotation(Inheritance.class).value();
		String type = request.getParameter(value);		
		Class<?> superClass = parameter.getParameterType();
		String name = StringUtils.capitalize(type);		
		try {
			Class<?> subClass = ReflectionUtils.findSubClassByName(superClass, name);
			return BeanUtils.instantiate(subClass);
		} catch (Exception e) {
			throw new InheritanceNotFoundException(type);
		}
	}	
}