package com.ability.ease.claims;

import java.util.Map;

import com.ability.ease.testapi.IClaims;

public class ClaimsSelenium2Impl implements IClaims{

	
	@Override
	public boolean verifyUB04FormFeatures(Map<String, String> mapAttrVal) throws Exception {

		ClaimsPage claims = new ClaimsPage();
		return claims.verifyUB04FormFields(mapAttrVal);
	}

	@Override
	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal)
			throws Exception {
		ClaimsPage claims = new ClaimsPage();
		return claims.verifyAddRemoveClaimLines(mapAttrVal);
	}

}
