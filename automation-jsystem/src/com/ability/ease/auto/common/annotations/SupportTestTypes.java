package com.ability.ease.auto.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ability.ease.auto.enums.tests.TestType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SupportTestTypes {
	
	/**
	 * Show supported test types.
	 * @return the test types.
	 */
	TestType[] testTypes() ;

}
