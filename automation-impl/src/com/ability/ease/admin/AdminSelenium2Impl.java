package com.ability.ease.admin;

import java.util.List;
import java.util.Map;

import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.testapi.IAdministration;

public class AdminSelenium2Impl implements IAdministration{
	
	AdminPage adminpage = new AdminPage();
	AddCustomerPage customerpage = new AddCustomerPage();
	
	@Override
	public boolean addUser(Map<String, String> mapAttrVal) throws Exception {
		return adminpage.addUser(mapAttrVal);
	}

	@Override
	public boolean editUser(Map<String, String> mapAttrVal) throws Exception {
		
		return adminpage.editUser(mapAttrVal);
	}

	@Override
	public boolean addCustomer(Map<String, String> mapAttrVal) throws Exception {
		
		return customerpage.addCustomer(mapAttrVal);
	}
	
	@Override
	public boolean findCustomer(String customername) throws Exception {
		
		return customerpage.findCustomer(customername);
	}
	//customername,NPI,displayname,name,type,ptan
	@Override
	public boolean verifyProviders(String customername,String NPI,String pname,String type,String ptan, int rownumber) throws Exception
	{
		return customerpage.verifyProviders(customername,NPI,pname,type,ptan,rownumber);
	}
	
  public boolean verifyBIAnalytics(String customername)throws Exception{
		
		return customerpage.verifyBIAnalytics(customername);
	}

	public boolean verifyBIAnalyticsUser(String customername)throws Exception
	{
		return customerpage.verifyBIAnalyticsUser(customername);
	}
	/*@Override
	public boolean verifyCustomersList(Map<String, String> mapAttrVal)throws Exception{
		
		return customerpage.verifyCustomersList(mapAttrVal);
	}*/	

	public boolean addEmployee(Map<String, String> mapAttrVal)throws Exception{
		
		return customerpage.addEmployee(mapAttrVal);
	}	

	
	@Override
	public boolean assignAlerts(String username,String alerttype)
			throws Exception {
		return customerpage.assignAlerts(username,alerttype);
	}
	
	@Override
	public boolean verifySetupAlerts(String username,String alerttype)throws Exception {
		return customerpage.verifySetupAlerts(username,alerttype);
	}
	
	
	public boolean verifyLogout(String username) throws Exception{
		return customerpage.verifyLogout(username);
	}
	
	public boolean updatePassword(String username, String newpassword)throws Exception{
		return customerpage.updatePassword(username,newpassword);
	}
	
	/*public boolean setUpAgencies(String agency,String intermediary,String intermediaryNumber,String region)throws Exception{
		return customerpage.setUpAgencies(agency,intermediary,intermediaryNumber,region);
	}*/
	
}
