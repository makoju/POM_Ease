package com.ability.ease.eligibility;

import java.util.Map;

import com.ability.ease.testapi.IEligibility;

public class EligibilitySelenium2Impl implements IEligibility{
	EligibilityPage eligPage = new EligibilityPage();
	
	@Override
	public boolean verifyEligibility(Map<String, String> mapAttrVal)
			throws Exception {
		return eligPage.verifyEligibility(mapAttrVal);
	}
	

}
