package com.ability.ease.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;
import jsystem.utils.RandomUtils;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IAdministration;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.TestType;

/**
 * 
 * @author vahini.eruva
 *
 */
public class AdminTests extends BaseTest{

	private IAdministration admin;
	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String username;
	private String alerttype;
	private String password;
	private String customername;
	private String companyname;
	private String npi;
	private String displayname;
	private String pname;
	private String type;
	private String ptan;
	private String intermediary;
	private String region;    
	private String intermediaryNumber;
	private String agency;
	private int rownumber;
	private String timeZone;
	private String groupName;
	private String ddeUserId;
	private String ddePassword;
	private String verifyPassword;
	private String multiagencies;
	private String npis;
	

	@Before
	public void setupTests()throws Exception{
		admin = (IAdministration)context.getBean("admin");
	}

	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add User", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void addUser() throws Exception {
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!admin.addUser(mapAttrValues)) {
			report.report("Failed to add user !!!",	Reporter.FAIL);
		} else {
			report.report("user added !!!", Reporter.PASS);
		}	
	}

	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Customer", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void addCustomer() throws Exception {

		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		//Converting companyname to random
		String companyname = mapAttrValues.get("Company Name");
		companyname= companyname+System.currentTimeMillis();
		mapAttrValues.put("Company Name", companyname);
		//Company Name and ContactName should be same otherwise customer failed to add
		mapAttrValues.put("Contact Name", companyname);
		
		keepAsGloablParameter("companyname", mapAttrValues.get("Company Name"));

		//Converting username to random
		String username = mapAttrValues.get("Username");
		username= (username+System.currentTimeMillis()).substring(0, 7); //username permits 7 characters only
		mapAttrValues.put("Username", username);
		
		keepAsGloablParameter("username", mapAttrValues.get("Username"));
		String providerinformation = mapAttrValues.get("ProvidersInformation");
		String[] providersinfo = providerinformation.split("~");
		for(int i=0;i<providersinfo.length;i++){
			String provider = providersinfo[i];
			String[] providerdata = provider.split(",");
			
			keepAsGloablParameter("npi"+(i+1), providerdata[0]);
			keepAsGloablParameter("ptan"+(i+1), providerdata[2]);
			keepAsGloablParameter("pname"+(i+1), providerdata[3]);
			keepAsGloablParameter("type"+(i+1), providerdata[4]);

			/*globalParamMap.put("npi"+(i+1), providerdata[0]);
			globalParamMap.put("ptan"+(i+1), providerdata[2]);
			globalParamMap.put("providername"+(i+1), providerdata[3]);
			globalParamMap.put("type"+(i+1), providerdata[4]);*/
		}


		if(!admin.addCustomer(mapAttrValues)) {
			report.report("Failed to add customer !!!",	Reporter.FAIL);
		} else {
			report.report("customer is successfully added !!!", Reporter.PASS);
		}	
	}


	/* find customer */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Find Customer", paramsInclude = { "companyname","testType" })
	public void findCustomer() throws Exception {
	
		if(!admin.findCustomer(companyname)) {
			report.report("Failed to find customer !!!",	Reporter.FAIL);
		} else {
			report.report("customer successfully found !!!", Reporter.PASS);
		}	
	}


	/* Verify Providers */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Providers", paramsInclude = { "companyname","npi","pname","type","ptan","rownumber","testType" })
	public void verifyProviders() throws Exception {
		if(!admin.verifyProviders(companyname,npi,pname,type,ptan,rownumber)) {
			report.report("Failed to find customer !!!",	Reporter.FAIL);
		} else {
			report.report("customer successfully found !!!", Reporter.PASS);
		}	
	}

	/*Added for Add employee in admin page*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Employee", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void addEmployee() throws Exception {
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		//Converting username to random
		String username = mapAttrValues.get("Username");
		username= (username+System.currentTimeMillis()).substring(0, 7); //username permits 7 characters only
		mapAttrValues.put("Username", username);
		
		keepAsGloablParameter("username", mapAttrValues.get("Username"));
		
		if(!admin.addEmployee(mapAttrValues)) {
			report.report("Failed to add employee !!!",	Reporter.FAIL);
		} else {
			report.report("employee is successfully added !!!", Reporter.PASS);
		}	
	}


	/* Added for Assign alerts in admin page*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "verify assign alerts", paramsInclude = { "username","alerttype", "testType" })
	public void verifyAssignAlerts() throws Exception {
		if(!admin.assignAlerts(username,alerttype)) {
			report.report("Failed to assign alerts !!!",	Reporter.FAIL);
		} else {
			report.report("Alerts successfully added !!!", Reporter.PASS);
		}	
	}

	/* added for newsetupalertsfornewcustomer*/
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "verify new setup alerts", paramsInclude = { "username","alerttype","testType" })
	public void verifyNewSetupAlerts() throws Exception {
		if(!admin.verifySetupAlerts(username,alerttype)) {
			report.report("Failed to assign alerts !!!",	Reporter.FAIL);
		} else {
			report.report("Alerts successfully added !!!", Reporter.PASS);
		}	
	}


	/* Added for BI Analytics in admin page*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "verifyBIAnalytics", paramsInclude = { "username","testType" })
	public void verifyBIAnalytics() throws Exception {

		if(!admin.verifyBIAnalytics(username)) {
			report.report("Failed to verify BiAnalytics customer !!!",	Reporter.FAIL);
		} else {
			report.report("Customer is having BI Analytics option !!!", Reporter.PASS);
		}	
	}	

	/* Added for BI Analytics in admin page*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "verifyBIAnalyticsUser", paramsInclude = { "username", "testType" })
	public void verifyBIAnalyticsUser() throws Exception {

		if(!admin.verifyBIAnalyticsUser(username)) {
			report.report("BI Analytics option is not displayed !!!",	Reporter.FAIL);
		} else {
			report.report("I Analytics option is displayed,click on analytics option !!!", Reporter.PASS);
		}	
	}

	/* updatepassword */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "updatePassword", paramsInclude = {"username","password","testType" })
	public void updatePassword() throws Exception {

		if(!admin.updatePassword(username,password)) {
			report.report("update password not successfull !!!",Reporter.FAIL);
		} else {
			report.report("update password successfull !!!", Reporter.PASS);
		}	
	}

	/*for new customer setting up agencies*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "setUpAgencies", paramsInclude = {"agency","intermediary","intermediaryNumber","region","testType" })
	public void setUpAgencies() throws Exception {

		if(!admin.setUpAgencies(agency,intermediary,intermediaryNumber,region)) {
			report.report("set up agencies not successfull !!!",Reporter.FAIL);
		} else {
			report.report("setup agencies successfull !!!", Reporter.PASS);
		}	
	}

	/*for new customer setting up agencies and change schedule*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "changeSchedule", paramsInclude = {"timeZone","testType" })
	public void changeSchedule() throws Exception {
		
		if(!admin.changeSchedule(timeZone)) {
			report.report("change schedule not successfull !!!",Reporter.FAIL);
		} else {
			report.report("change schedule successfull !!!", Reporter.PASS);
		}	
	}
	
	
	//public boolean changeSchedule(String customerName,String timeZone)
	
	/*for new customer setting up agencies and change schedule*/ 
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "setUpProvidersGroup", paramsInclude = {"username","groupName","multiagencies","testType" })
	public void setUpProvidersGroup() throws Exception {		
		if(!admin.setUpProvidersGroup(username,groupName,multiagencies))
		{
			report.report("not able to setup providers group successfully !!!",Reporter.FAIL);
		} else {
			report.report("able to setup providers group successfully !!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "configureDDECredentials", paramsInclude = {"groupName","ddeUserId","ddePassword","verifyPassword","testType" })
	public void configureDDECredentials() throws Exception {

		//keepAsGloablParameter("ddeUserId", ddeUserId);
		
		if(!admin.configureDDECredentials(groupName,ddeUserId,ddePassword,verifyPassword))
				{
			report.report("not able to configure dde credentials successfully !!!",Reporter.FAIL);
		} else {
			report.report("able to configure dde credentials successfully !!!", Reporter.PASS);
		}	
	}
	

	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "navigateToMyDDE", paramsInclude = {"groupName","ddeUserId","ddePassword","verifyPassword","testType" })
	public void navigateToMyDDE() throws Exception {	
		
		if(!admin.navigateToMyDDE(groupName,ddeUserId,ddePassword,verifyPassword))
				{
			report.report("navigateToMyDDE unsuccessfully !!!",Reporter.FAIL);
		} else {
			report.report("navigateToMyDDE successfully !!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "loginNewCustomer", paramsInclude = {"username","password","testType" })
	public void loginNewCustomer() throws Exception {
		//keepAsGloablParameter("companyname", companyname);
		
		if(!admin.loginNewCustomer(username,password)){
			report.report("not able to login with new customer unsuccessfully !!!",Reporter.FAIL);
		} else {
			report.report("able to login with new customer successfully !!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "clickHere", paramsInclude = {"testType" })
	public void clickHere() throws Exception {
				
		if(!admin.clickHere())
				{
			report.report("navigateToMyDDE unsuccessfully !!!",Reporter.FAIL);
		} else {
			report.report("navigateToMyDDE successfully !!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Set PsychiatricSTC checkbox for NPI", paramsInclude = {"npis,testType" })
	public void setPsychiatricSTCCheckBoxForNPI() throws Exception {

		if(admin.setPsychiatricSTCCheckBoxForNPI(npis))
		{
			report.report("Successfully Configured PsychiatricSTC for Given NPIs !!!",Reporter.PASS);
		} 
		else {
			report.report("Failed to Configure PsychiatricSTC for Given NPIs!!!", Reporter.FAIL);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Set PsychiatricSTC checkbox for NPI", paramsInclude = {"npis,testType" })
	public void verifyPsychiatricSTCCheckBoxForNPI() throws Exception {

		if(admin.verifyPsychiatricSTCCheckBoxForNPI(npis))
		{
			report.report("Successfully Verified PsychiatricSTC checkbox enable for Given NPIs !!!",Reporter.PASS);
		} 
		else {
			report.report("Failed to verify PsychiatricSTC checkbox enable for Given NPIs!!!", Reporter.FAIL);
		}	
	}
	
	
	@Override
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName) throws Exception {	
		super.handleUIEvent(map, methodName);
		if(methodName.matches("addUser"))
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\AddUser.xml");
		else if(methodName.matches("addCustomer"))
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\Admin.xml");
		else if(methodName.matches("addEmployee"))
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\Admin.xml");	
	}


	/*######
	Getters and Setters
	######*/
	public AttributePair[] getAttrpair() {
		return attrpair;
	}

	@ParameterProperties(description = "AttributeNames, AttributeValues container")
	public void setAttrpair(AttributePair[] attrpair) {
		this.attrpair = attrpair;
	}

	public AttributeNameValueDialogProvider[] getAttributeNameValueDialogProvider() {
		return AttributeNameValueDialogProvider;
	}

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(
			AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAlerttype() {
		return alerttype;
	}

	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getNpi() {
		return npi;
	}

	public void setNpi(String npi) {
		this.npi = npi;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}



	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	};

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPtan() {
		return ptan;
	}

	public void setPtan(String ptan) {
		this.ptan = ptan;
	}

	public String getIntermediary() {
		return intermediary;
	}

	public void setIntermediary(String intermediary) {
		this.intermediary = intermediary;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIntermediaryNumber() {
		return intermediaryNumber;
	}

	public void setIntermediaryNumber(String intermediaryNumber) {
		this.intermediaryNumber = intermediaryNumber;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public int getRownumber() {
		return rownumber;
	}

	public void setRownumber(int rownumber) {
		this.rownumber = rownumber;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDdeUserId() {
		return ddeUserId;
	}

	public void setDdeUserId(String ddeUserId) {
		this.ddeUserId = ddeUserId;
	}

	public String getDdePassword() {
		return ddePassword;
	}

	public void setDdePassword(String ddePassword) {
		this.ddePassword = ddePassword;
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public String getMultiagencies() {
		return multiagencies;
	}

	public void setMultiagencies(String multiagencies) {
		this.multiagencies = multiagencies;
	}

	public String getCompanyname() {
		return companyname;
	}

	@ParameterProperties(description = "Company/customer name , this value would come from global paramter")
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getNpis() {
		return npis;
	}

	public void setNpis(String npis) {
		this.npis = npis;
	}	
}
