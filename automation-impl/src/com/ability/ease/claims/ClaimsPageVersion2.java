package com.ability.ease.claims;

import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPageVersion2 extends AbstractPageObject{

	int failCounter = 0;
	public boolean hoverMouseOnClaimCharge()throws Exception{
		
		return failCounter == 0 ? true : false;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}