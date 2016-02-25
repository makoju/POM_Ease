package com.ability.ease.testapi;

import java.util.List;
import java.util.Map;

import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;

public interface IAdministration {
	
	abstract public boolean addUser(Map<String, String> mapAttrVal)throws Exception;
	
	abstract public boolean editUser(Map<String, String> mapAttrVal)throws Exception;
    abstract public boolean addCustomer(Map<String, String> mapAttrVal)throws Exception;
    abstract public boolean findCustomer(String customername)throws Exception;
    abstract public boolean verifyProviders(String customername,String NPI,String pname,String type,String ptan,int rownumber) throws Exception;
    
    abstract boolean verifyBIAnalytics(String customername)throws Exception;
	abstract boolean verifyBIAnalyticsUser(String customername)throws Exception;
	//abstract public boolean verifyCustomersList(Map<String, String> mapAttrVal)throws Exception;	
	abstract public boolean addEmployee(Map<String, String> mapAttrVal)throws Exception;	
	abstract boolean assignAlerts(String username,String alerttype)throws Exception;
	abstract boolean verifySetupAlerts(String username,String alerttype)throws Exception;
	abstract boolean verifyLogout(String username)throws Exception;
	abstract boolean updatePassword(String username, String newpassword)throws Exception;
	//abstract boolean setUpAgencies(String agency,String intermediary,String intermediaryNumber,String region)throws Exception;
	
	 
	
}
