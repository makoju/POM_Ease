package com.ability.ease.testapi;

import java.util.Map;

public interface IClaims {
	
	/*
	 * method to verify all the fields in UB04 form
	 */
	abstract public boolean verifyUB04FormFeatures(Map<String, String> mapAttrVal)throws Exception;

	
}
