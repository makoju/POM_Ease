package com.ability.ease.claims;

import java.util.Map;

import com.ability.ease.testapi.IClaims;

public class ClaimsSelenium2Impl implements IClaims{

	ClaimsPage claims = new ClaimsPage();
	
	@Override
	public boolean verifyUB04FormFeatures(Map<String, String> mapAttrVal) throws Exception {
		return claims.verifyUB04FormFields(mapAttrVal);
	}

	@Override
	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal)
			throws Exception {
		return claims.verifyAddRemoveClaimLines(mapAttrVal);
	}
}
