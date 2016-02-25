package com.ability.ease.myaccount;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.testapi.IMyAccount;

public class MyAccountSelenium2Impl implements IMyAccount {
	
	CustomSchedulePage custschedpage = new CustomSchedulePage();
	ChangeSchedulePage csp = new ChangeSchedulePage();

	@Override
	public boolean verifyChangePasswordValidCase(String sOldPassword, String sNewPassword, String expectedMessage,
			String verifypassword) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyChangePasswordinValidCase(String sOldPassword, String sNewPassword, String expectedMessage,
			String verifypassword) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyChangeSchedule(Map<String, String> mapAttrVal) throws Exception {
		return csp.verifyChangeSchedule(mapAttrVal);
	}

	@Override
	public boolean submitCustomSchedule(Map<String, String> mapAttrValues) throws Exception {
		
		return custschedpage.submitCustomSchedule(mapAttrValues);
	}

	@Override
	public boolean verifyCustomSchedule(String agencyName, String schedulername, String sDay, String EndDay,
			String runtime, String credential, String timezone, int rownumber) {
		try {
			return custschedpage.verifyCustomSchedule(agencyName, schedulername, sDay, EndDay, runtime, credential, timezone, rownumber);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*@Override
	public boolean verifyChangeSchedule(String sTimeZone, String sAgency, String sClaimsTime, String sEligibilityTime, String sEFTTime)
			throws Exception{
		ChangeSchedulePage csp = new ChangeSchedulePage();
		return csp.verifyChangeSchedule(jobScheduleMap, timezone, agency);
	}*/
}
