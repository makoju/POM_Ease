package com.ability.ease.myaccount;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.ability.ease.testapi.IMyAccount;

public class MyAccountSelenium2Impl implements IMyAccount {
	
	CustomSchedulePage custschedpage = new CustomSchedulePage();
	ChangeSchedulePage csp = new ChangeSchedulePage();
	ChangeFISSDDESettingsPage cfdp = new ChangeFISSDDESettingsPage();
	SetUpAlertsPage sap = new SetUpAlertsPage();
	ChangePasswordPage cpp = new ChangePasswordPage();

	@Override
	public boolean verifyChangePassword(String sOldPassword, String sNewPassword,String verifypassword,String expectedMessage) throws Exception {
		return cpp.verifyChangePassword(sOldPassword,sNewPassword,verifypassword,expectedMessage);
	}

	@Override
	public boolean verifyChangeSchedule(Map<String, String> mapAttrVal) throws Exception {
		return csp.verifyChangeSchedule(mapAttrVal);
	}

	@Override
	public boolean submitCustomSchedule(Map<String, String> mapAttrValues, String expectedalertmessage) throws Exception {
		
		return custschedpage.submitCustomSchedule(mapAttrValues, expectedalertmessage);
	}

	@Override
	public boolean verifyCustomSchedule(String agencyName,String sDay, String EndDay,
			String runtime, String credential, String timezone, int rownumber) {
		try {
			return custschedpage.verifyCustomSchedule(agencyName, sDay, EndDay, runtime, credential, timezone, rownumber);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean insertSchedulersToAgency(String agencyname) throws Exception {
		
		return custschedpage.insertSchedulersToAgency(agencyname);
	}

	@Override
	public boolean verifyJobScheduleCurrentAction(String agencyName, String jobtype, String customerid) {
		return custschedpage.verifyJobScheduleCurrentAction(agencyName,jobtype, customerid);
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
	public boolean disableEase(String disableeaseforgroupnameoragency,String grouporagencyname, String disabletype, String expectedalertmessage) throws Exception {
		return new DisableEasePage().disableEase(disableeaseforgroupnameoragency,grouporagencyname,disabletype,expectedalertmessage);
	}

	@Override
	public boolean resumeEase(String groupname, String ddeuserid,
			String ddepassword) throws Exception {
		return new ResumeEase().resumeEase(groupname, ddeuserid, ddepassword);
	}

	@Override
	public boolean deleteAllCustomSchedule(String agency) throws Exception {
		return custschedpage.deleteAllCustomSchedule(agency);
	}

	@Override
	public boolean verifyInvalidDDEcredentials(String agency,String credential) throws Exception {
		return custschedpage.verifyInvalidDDEcredentials(agency, credential);
	}

	@Override
	public boolean updateDDEpasswordProblem(String username,int value) throws Exception {	
		return custschedpage.updateDDEpasswordProblem(username,value);
	}

	@Override
	public boolean verifySetupalerts(String username) throws Exception {
		return sap.setUpAlerts(username);
	}

	@Override
	public boolean deletecustomScheduleRows(String dAgency) throws Exception {
		return custschedpage.deletecustomScheduleRows(dAgency);
	}

	@Override
	public boolean verifyDeleteOptionNotEnableforFirstSchedule(String agency) throws Exception {
		return custschedpage.verifyDeleteOptionNotEnableforFirstSchedule(agency);
	}

/*	@Override
	public boolean verifyCustomScheduleDelete(String agency) throws Exception {
		return false;
	}*/

	@Override
	public boolean configureBlackoutTime(String groupname, String starttime,
			String endtime, String expectedalertmessage) throws Exception {
		return cfdp.configureBlackoutTime(groupname,starttime,endtime,expectedalertmessage);
	}

	@Override
	public boolean verifyBlackoutTimeHelpTextinChangeandCustomScheduleWindow(String agency, String starttime, String endtime) throws Exception{
		
		return csp.verifyBlackoutTimeHelpTextinChangeandCustomScheduleWindow(agency,starttime,endtime);
	}

	@Override
	public boolean insertRecordintoJobSchedule(String agencyName,
			String jobtype, String customerid) {

		return custschedpage.insertRecordintoJobSchedule(agencyName, jobtype, customerid);
	}

}
