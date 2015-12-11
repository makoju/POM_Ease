package com.ability.ease.eligibility;

import java.util.Map;

import com.ability.ease.testapi.IEligibility;

public class EligibilitySelenium2Impl implements IEligibility{

	@Override
	public boolean verifyEligibility(Map<String, String> mapAttrVal)
			throws Exception {
		EligibilityPage eligPage = new EligibilityPage();
		return eligPage.verifyEligibility(mapAttrVal);
	}
	

}
