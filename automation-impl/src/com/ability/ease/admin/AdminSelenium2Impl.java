package com.ability.ease.admin;

import java.util.Map;
import com.ability.ease.employee.EmployeePage;
import com.ability.ease.testapi.IAdministration;

/**
 * 
 * @author vahini.eruva
 *
 */
public class AdminSelenium2Impl implements IAdministration{
	
	
	
	
	
	@Override
	public boolean addUser(Map<String, String> mapAttrVal) throws Exception {
		AdminPage adminpage = new AdminPage();
		return adminpage.addUser(mapAttrVal);
	}

	@Override
	public boolean editUser(Map<String, String> mapAttrVal) throws Exception {
		AdminPage adminpage = new AdminPage();
		return adminpage.editUser(mapAttrVal);
	}

	@Override
	public boolean addCustomer(Map<String, String> mapAttrVal) throws Exception {
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.addCustomer(mapAttrVal);
	}
	
	@Override
	public boolean findCustomer(String customername) throws Exception {
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.findCustomer(customername);
	}
	//customername,NPI,displayname,name,type,ptan
	@Override
	public boolean verifyProviders(String customername,String NPI,String pname,String type,String ptan, int rownumber) throws Exception
	{ 
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.verifyProviders(customername,NPI,pname,type,ptan,rownumber);
	}
	
  public boolean verifyBIAnalytics(String customername)throws Exception{
	  AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.verifyBIAnalytics(customername);
	}

	public boolean verifyBIAnalyticsUser(String customername)throws Exception
	{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.verifyBIAnalyticsUser(customername);
	}
	/*@Override
	public boolean verifyCustomersList(Map<String, String> mapAttrVal)throws Exception{
		
		return customerpage.verifyCustomersList(mapAttrVal);
	}*/	

	public boolean addEmployee(Map<String, String> mapAttrVal)throws Exception{
		EmployeePage emppage=new EmployeePage();
		return emppage.addEmployee(mapAttrVal);
	}	
	
	@Override
	public boolean assignAlerts(String username,String alerttype)
	throws Exception {
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.assignAlerts(username,alerttype);
	}
	
	@Override
	public boolean verifySetupAlerts(String username,String alerttype)throws Exception {
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.verifySetupAlerts(username,alerttype);
	}
	
	public boolean updatePassword(String username, String newpassword)throws Exception{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.updatePassword(username,newpassword);
	}
	
	public boolean setUpAgencies(String agency,String intermediary,String intermediaryNumber,String region)throws Exception{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.setUpAgencies(agency,intermediary,intermediaryNumber,region);
	}
		
	public boolean changeSchedule(String customerName,String timeZone) throws Exception
	{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.changeSchedule(customerName,timeZone);
	}
	
	public boolean setUpProvidersGroup(String customerName,String groupName,String multiagencies) throws Exception
	{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.setUpProvidersGroup(customerName,groupName,multiagencies);
	}
	
	public boolean configureDDECredentials(String groupName,String ddeUserId,String ddePassword,String verifyPassword) throws Exception
	{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.configureDDECredentials(groupName,ddeUserId,ddePassword,verifyPassword);
	}
	
	public boolean navigateToMyDDE(String groupName,String ddeUserId,String ddePassword,String verifyPassword)throws Exception{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.navigateToMyDDE(groupName,ddeUserId,ddePassword,verifyPassword);
	}
	
	public boolean clickHere()throws Exception{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.clickHere();
	}
	
	public boolean loginNewCustomer(String username,String newpassword)throws Exception{
		AddCustomerPage customerpage = new AddCustomerPage();
		return customerpage.loginNewCustomer(username, newpassword);
	}
	
}
