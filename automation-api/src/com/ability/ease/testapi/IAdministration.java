package com.ability.ease.testapi;

import java.util.Map;
/**
 * 
 * @author vahini.eruva
 *
 */
public interface IAdministration {
	
	abstract public boolean addUser(Map<String, String> mapAttrVal)throws Exception;
	abstract public boolean editUser(Map<String, String> mapAttrVal)throws Exception;
    abstract public boolean addCustomer(Map<String, String> mapAttrVal)throws Exception;
    abstract public boolean findCustomer(String customername)throws Exception;
    abstract public boolean verifyProviders(String customername,String NPI,String pname,String type,String ptan,int rownumber) throws Exception; 
    abstract public boolean verifyBIAnalytics(String customername)throws Exception;
	abstract public boolean verifyBIAnalyticsUser(String customername)throws Exception;
	abstract public boolean addEmployee(Map<String, String> mapAttrVal)throws Exception;	
	abstract public boolean assignAlerts(String username,String alerttype)throws Exception;
	abstract public boolean verifySetupAlerts(String username,String alerttype)throws Exception;
	abstract public boolean updatePassword(String username, String newpassword)throws Exception;
	abstract public boolean setUpAgencies(String agency,String intermediary,String intermediaryNumber,String region)throws Exception;	
	abstract public boolean changeSchedule(String timeZone) throws Exception;
	abstract public boolean setUpProvidersGroup(String customerName,String groupName,String multiagencies) throws Exception;
	abstract public boolean configureDDECredentials(String groupName,String ddeUserId,String ddePassword,String verifyPassword) throws Exception;
	abstract public boolean navigateToMyDDE(String groupName,String ddeUserId,String ddePassword,String verifyPassword)throws Exception;
	abstract public boolean clickHere()throws Exception;
	abstract public boolean loginNewCustomer(String username,String newpassword)throws Exception;
	
}
