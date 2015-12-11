package com.ability.ease.misc;

import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.UserActionType;
import com.ability.ease.misc.MiscPage;
import com.ability.ease.testapi.IMiscellaneous;

public class MiscSelenium2Impl implements IMiscellaneous{

	@Override
	public boolean validLogin(String username, String password)
			throws Exception {	
		MiscPage login = new MiscPage();
		login.validLogin(username, password);
		return login.isLogedIn();
	}

	@Override
	public boolean invalidLogin(String username, String password)
			throws Exception {
		MiscPage login = new MiscPage();
		return login.invalidLogin(username, password);
	}

	@Override
	public boolean verifyHelp()throws Exception{
		MiscPage help = new MiscPage();
		return help.verifyHelp();
	}

	@Override
	public boolean verifySupport() throws Exception {
		MiscPage support = new MiscPage();
		return support.verifySupport();
	}

	@Override
	public boolean verifyPageRefresh() throws Exception {
		MiscPage refresh = new MiscPage();
		return refresh.verifyPageRefresh();
	}

	@Override
	public boolean verifyFWDBACK() throws Exception {
		MiscPage misc = new MiscPage();
		return misc.verifyFWDBACK();
	}

	@Override
	public boolean verifyPersonalInfo(Map<String, String> mapAttrVal, UserActionType userAction) throws Exception {
		MiscPage misc = new MiscPage();
		return misc.verifyPersonalInfo(mapAttrVal, userAction);
	}

	@Override
	public boolean verifyLeftMenuInMyAccountTab(MyAccountSubMenu subMenuItem, String sExpectedOutput) throws Exception {
		MiscPage misc = new MiscPage();
		return misc.verifyLeftMenuInMyAccountTab(subMenuItem, sExpectedOutput);
	}
	
	@Override
	public boolean verifyAlertOptions(Map<String, String> mapAttrVal,String userName)throws Exception {
		MiscPage misc = new MiscPage();
		return misc.verifyAlertOptions(mapAttrVal,userName);
	}
	
	@Override
	public boolean verifyAddHICs(Map<String, String> mapAttrVal)throws Exception {
		MiscPage misc = new MiscPage();
		return misc.verifyAddHICs(mapAttrVal);
	}
}
