package com.ability.ease.testapi;

import java.util.Map;

public interface IEligibility {

	/*
	 * Verify the Eligibility check for HETS user with a valid HIC id
	 */
	abstract public boolean verifyEligibility(Map<String, String> mapAttrVal)throws Exception;
}
