package com.ability.ease.testapi;

import java.util.Map;




public interface IMyAccount{
	public boolean verifyChangePassword(String sOldPassword,String sNewPassword, String verifypassword,String expectedMessage)throws Exception;
	public boolean verifyChangeSchedule(Map<String,String> mapAttrValues)throws Exception;
	public boolean submitCustomSchedule(Map<String,String> mapAttrValues, String expectedalertmessage)throws Exception;
	public boolean verifyCustomSchedule(String agencyName,String sDay, String EndDay, String runtime, String credential,String timezone, int rownumber);
	public boolean verifyJobScheduleCurrentAction(String agencyName, String jobtype, String customerid);
	public boolean verifyOptionsUnderAddFISSDDESetup() throws Exception;
	public boolean addFISSDDESetup(String username, String groupname,String agencies, boolean isnewcustomer) throws Exception;
	public boolean setupDDECredential(String groupname, String ddeuserid, String ddepassword) throws Exception;
	public boolean editFISSDDESetup(String groupname, String agencies) throws Exception;
	public boolean editDDECredential(String groupname, String ddeuserid, String ddepassword) throws Exception;
	public boolean removeFISSDDESetup(String groupname) throws Exception;
	public boolean disableEase(String grouporagencycheckboxname,String grouporagencyname,String disableuntil,String expectedalertmessage) throws Exception;
	public boolean resumeEase(String groupname, String ddeuserid, String ddepassword) throws Exception;
	public boolean deleteAllCustomSchedule(String agency) throws Exception;
	public boolean verifyInvalidDDEcredentials(String agency, String credential) throws Exception;
	public boolean updateDDEpasswordProblem(String username, int value) throws Exception;
	public boolean verifySetupalerts(String username) throws Exception;
	public boolean deletecustomScheduleRows(String dAgency) throws Exception;
	public boolean verifyDeleteOptionNotEnableforFirstSchedule(String agency) throws Exception;
	public boolean configureBlackoutTime(String groupname, String starttime,String endtime,String expectedalertmessage) throws Exception;
	public boolean insertSchedulersToAgency(String agencyname) throws Exception;
}
