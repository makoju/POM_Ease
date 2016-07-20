package com.ability.ease.testapi;

import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.UserActionType;

public interface IMiscellaneous {
	
	/*
	 * verify valid login
	 */
	abstract public boolean validLogin(String sUserName, String sPassword)throws Exception;
	
	/*
	 * verify invalid login
	 */
	abstract public boolean invalidLogin(String sUserName, String sPassword)throws Exception;
	
	/*
	 * verify help
	 */
	abstract public boolean verifyHelp()throws Exception;
	
	/*
	 * verify support button functionality
	 */
	abstract public boolean verifySupport()throws Exception;

	/*
	 * verify refresh page functionality
	 */
	abstract public boolean verifyPageRefresh()throws Exception;
	
	/*
	 * verify forward and backward buttons functionality
	 */
	abstract public boolean verifyFWDBACK()throws Exception;
	
	/*
	 * verify forward and backward buttons functionality
	 */
	abstract public boolean verifyPersonalInfo(Map<String, String> mapAttrVal, UserActionType userAction)throws Exception;
	
	/*
	 * Verify left menu items under My Account Page
	 */
	abstract public boolean verifyLeftMenuInMyAccountTab(MyAccountSubMenu subMenuItem, String sExpectedOutput)throws Exception;
	
	/*
	 * Verify setup alerts option
	 */
	abstract public boolean verifyAlertOptions(Map<String, String> mapAttrVal, String userName)throws Exception;
	
	/*
	 * Verify setup alerts option
	 */
	abstract public boolean verifyAddHICs(Map<String, String> mapAttrVal)throws Exception;
	
	abstract public boolean verifyAddSingleHIC(String agency,String hicID,String sExpectedmessage) throws Exception;
	abstract public boolean verifyAddMultipleHICs(String agency,String hicID,String sExpectedmessage) throws Exception;
	abstract public boolean verifyAddMultipleHICs_Basicview(String agency,String hicID,String sExpectedmessage) throws Exception;
	abstract public boolean verifyAddSingleHIC_BasicView(String agency,String hicID,String sExpectedmessage) throws Exception;
}