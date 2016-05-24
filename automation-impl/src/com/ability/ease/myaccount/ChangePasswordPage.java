package com.ability.ease.myaccount;

import java.util.List;

import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;


public class ChangePasswordPage extends AbstractPageObject{

	public boolean verifyChangePassword(String sOldPassword,String sNewPassword,String sVerifypassword,String sExpectedMessage)throws Exception{
		navigateToPage();
		clickLink("Change Password");
	
		typeEditBox("oldPassword", sOldPassword);
		typeEditBox("newPassword", sNewPassword);
		typeEditBox("Verify", sVerifypassword);
		clickButtonV2("Submit");
		
		if(verifyAlert(sExpectedMessage))
			return true;
		else
			return false;
	}	

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
	}

}

