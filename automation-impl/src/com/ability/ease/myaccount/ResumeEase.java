package com.ability.ease.myaccount;

import org.openqa.selenium.WebElement;

import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ResumeEase extends AbstractPageObject {

	public boolean resumeEase(String groupname, String ddeuserid, String ddepassword) throws Exception {

		return new ChangeFISSDDESettingsPage().editDDECredential(groupname, ddeuserid, ddepassword);
	}


	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("MANAGE FISS/DDE SETTINGS") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
		}
		clickLink("RESUME EASE");
	}

}
