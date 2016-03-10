package com.ability.ease.myaccount;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.ability.ease.testapi.IMyAccount;

public class MyAccountSelenium2Impl implements IMyAccount {
	
	CustomSchedulePage custschedpage = new CustomSchedulePage();
	ChangeSchedulePage csp = new ChangeSchedulePage();
	ChangeFISSDDESettingsPage cfdp = new ChangeFISSDDESettingsPage();

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
	public boolean verifyCustomSchedule(String agencyName,String sDay, String EndDay,
			String runtime, String credential, String timezone, int rownumber) {
		try {
			return custschedpage.verifyCustomSchedule(agencyName, sDay, EndDay, runtime, credential, timezone, rownumber);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean verifyJobScheduleCurrentAction(String agencyName) {
		return custschedpage.verifyJobScheduleCurrentAction(agencyName);
	}

	@Override
	public boolean verifyOptionsUnderAddFISSDDESetup() throws Exception {
		return cfdp.verifyOptionsUnderAddFISSDDESetup();
	}

	@Override
	public boolean addFISSDDESetup(String username, String groupname, String agencies, boolean isnewcustomer) throws Exception {
		return cfdp.addFISSDDESetup(username, groupname,agencies, isnewcustomer);
	}

	@Override
	public boolean setupDDECredential(String groupname, String ddeuserid,
			String ddepassword) throws Exception {
		return cfdp.setupDDECredential(groupname, ddeuserid, ddepassword);
	}

	@Override
	public boolean editFISSDDESetup(String groupname, String agencies)
			throws Exception {
		return cfdp.editFISSDDESetup(groupname, agencies);
	}

	@Override
	public boolean editDDECredential(String groupname, String ddeuserid,
			String ddepassword) throws Exception {
		return cfdp.editDDECredential(groupname, ddeuserid, ddepassword);
	}

	@Override
	public boolean removeFISSDDESetup(String groupname) throws Exception {
		return cfdp.removeFISSDDESetup(groupname);
	}

	@Override
	public boolean disableEase(String disableeaseforgroupnameoragency,String grouporagencyname, String disabletype) throws Exception {
		return new DisableEasePage().disableEase(disableeaseforgroupnameoragency,grouporagencyname,disabletype);
	}

	@Override
	public boolean resumeEase(String groupname, String ddeuserid,
			String ddepassword) throws Exception {
		return new ResumeEase().resumeEase(groupname, ddeuserid, ddepassword);
	}

	/*@Override
	public boolean verifyChangeSchedule(String sTimeZone, String sAgency, String sClaimsTime, String sEligibilityTime, String sEFTTime)
			throws Exception{
		ChangeSchedulePage csp = new ChangeSchedulePage();
		return csp.verifyChangeSchedule(jobScheduleMap, timezone, agency);
	}*/
}
