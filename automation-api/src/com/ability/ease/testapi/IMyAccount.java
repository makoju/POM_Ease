package com.ability.ease.testapi;

import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;

public interface IMyAccount{

	//public boolean verifyChangePassword(String sOldPassword,String sNewPassword)throws Exception;

	public boolean verifyChangePasswordValidCase(String sOldPassword,String sNewPassword, String expectedMessage,String verifypassword)throws Exception;

	public boolean verifyChangePasswordinValidCase(String sOldPassword,String sNewPassword, String expectedMessage,String verifypassword)throws Exception;

	public boolean verifyChangeSchedule(Map<String,String> mapAttrValues)throws Exception;
	
	public boolean submitCustomSchedule(Map<String,String> mapAttrValues)throws Exception;
	public boolean verifyCustomSchedule(String agencyName,String schedulername, String sDay, String EndDay, String runtime, String credential,String timezone, int rownumber);
	

	//public boolean verifyChangeSchedule(String sTimeZone, String sAgency, String sClaimsTime, String sEligibilityTime, String sEFTTime) throws Exception;
}
