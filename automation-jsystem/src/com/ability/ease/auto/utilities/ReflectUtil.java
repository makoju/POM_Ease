package com.ability.ease.auto.utilities;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.ability.ease.auto.common.annotations.GlobalParametersTest;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.enums.tests.TestType;

public class ReflectUtil {
	
	/**
	 * returns the test name that has the 
	 * @return
	 */
	public static TestType[] getSupportedTestTypes(Class<?> cls, String testName){
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(testName)) {
				Annotation[] annotations = method.getDeclaredAnnotations();
				for (Annotation annotation : annotations) {
					if(annotation instanceof SupportTestTypes)
						return ((SupportTestTypes) annotation).testTypes();
				}
			}
		}
		return null;
	}
	
	/**
	 * returns the test name that has the 
	 * @return
	 */
	public static String getClassGlobalParametersTest(Class<?> cls){
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				if(annotation instanceof GlobalParametersTest)
					return method.getName();
			}
		}
		return null;
	}
	
	

}
