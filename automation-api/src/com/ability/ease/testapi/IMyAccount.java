package com.ability.ease.testapi;

import java.util.Map;

public interface IMyAccount{
	public boolean verifyChangePasswordValidCase(String sOldPassword,String sNewPassword, String expectedMessage,String verifypassword)throws Exception;
	public boolean verifyChangePasswordinValidCase(String sOldPassword,String sNewPassword, String expectedMessage,String verifypassword)throws Exception;
	public boolean verifyChangeSchedule(Map<String,String> mapAttrValues)throws Exception;
	public boolean submitCustomSchedule(Map<String,String> mapAttrValues)throws Exception;
	public boolean verifyCustomSchedule(String agencyName,String sDay, String EndDay, String runtime, String credential,String timezone, int rownumber);
	public boolean verifyJobScheduleCurrentAction(String agencyName);
	public boolean verifyOptionsUnderAddFISSDDESetup() throws Exception;
	public boolean addFISSDDESetup(String username, String groupname,String agencies, boolean isnewcustomer) throws Exception;
	public boolean setupDDECredential(String groupname, String ddeuserid, String ddepassword) throws Exception;
	public boolean editFISSDDESetup(String groupname, String agencies) throws Exception;
	public boolean editDDECredential(String groupname, String ddeuserid, String ddepassword) throws Exception;
	public boolean removeFISSDDESetup(String groupname) throws Exception;
	public boolean disableEase(String grouporagencycheckboxname,String grouporagencyname,String disableuntil) throws Exception;
	public boolean resumeEase(String groupname, String ddeuserid, String ddepassword) throws Exception;
	public boolean deleteAllCustomSchedule(String agency) throws Exception;
}
