package com.ability.ease.admin;

import java.util.HashMap;
import java.util.Map;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

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

public class AdminTests extends BaseTest{

	private IAdministration admin;
	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String username;
	private String alerttype;
	private String password;
	private String analytics;
	private String customername;
	private String NPI;
	private String displayname;
    private String pname;
    private String type;
    private String ptan;
    private String intermediary;
    private String region;    
    private String intermediaryNumber;
    private String agency;
    private int rownumber;

	

	//Constructor
	public AdminTests()throws Exception{
		super();
	}

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
		globalParamMap.put("companyname", mapAttrValues.get("Company Name"));
		String providerinformation = mapAttrValues.get("ProvidersInformation");
		String[] providersinfo = providerinformation.split("~");
		for(int i=0;i<providersinfo.length;i++){
			String provider = providersinfo[i];
			String[] providerdata = provider.split(",");
			
			globalParamMap.put("npi"+(i+1), providerdata[0]);
			globalParamMap.put("ptan"+(i+1), providerdata[2]);
			globalParamMap.put("providername"+(i+1), providerdata[3]);
			globalParamMap.put("type"+(i+1), providerdata[4]);
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
		@TestProperties(name = "Find Customer", paramsInclude = { "customername","testType" })
		public void findCustomer() throws Exception {
			if(!admin.findCustomer(customername)) {
				report.report("Failed to find customer !!!",	Reporter.FAIL);
			} else {
				report.report("customer successfully found !!!", Reporter.PASS);
			}	
		}
		
		
		/* Verify Providers */
		@Test(timeout = TEST_TIMEOUT)
		@SupportTestTypes(testTypes = { TestType.Selenium2 })
		@TestProperties(name = "Verify Providers", paramsInclude = { "customername","NPI","pname","type","ptan","rownumber","testType" })
		public void verifyProviders() throws Exception {
			if(!admin.verifyProviders(customername,NPI,pname,type,ptan,rownumber)) {
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
		@TestProperties(name = "verifyBIAnalytics", paramsInclude = { "customername","testType" })
		public void verifyBIAnalytics() throws Exception {
			
			if(!admin.verifyBIAnalytics(customername)) {
				report.report("Failed to verify BiAnalytics customer !!!",	Reporter.FAIL);
			} else {
				report.report("Customer is having BI Analytics option !!!", Reporter.PASS);
			}	
		}	
		
		 /* Added for BI Analytics in admin page*/ 
			@Test(timeout = TEST_TIMEOUT)
			@SupportTestTypes(testTypes = { TestType.Selenium2 })
			@TestProperties(name = "verifyBIAnalyticsUser", paramsInclude = { "customername", "testType" })
			public void verifyBIAnalyticsUser() throws Exception {
				
				if(!admin.verifyBIAnalyticsUser(customername)) {
					report.report("BI Analytics option is not displayed !!!",	Reporter.FAIL);
				} else {
					report.report("I Analytics option is displayed,click on analytics option !!!", Reporter.PASS);
				}	
			}
			
			/* Logout */
			@Test(timeout = TEST_TIMEOUT)
			@SupportTestTypes(testTypes = { TestType.Selenium2 })
			@TestProperties(name = "verifyLogout", paramsInclude = {"username","testType" })
			public void verifyLogout() throws Exception {
				
				if(!admin.verifyLogout(username)) {
					report.report("not able to logout successfully !!!",Reporter.FAIL);
				} else {
					report.report("able to logout successfully !!!", Reporter.PASS);
				}	
			}
			
			
			/* updatepassword */
			@Test(timeout = TEST_TIMEOUT)
			@SupportTestTypes(testTypes = { TestType.Selenium2 })
			@TestProperties(name = "updatePassword", paramsInclude = {"username","password","testType" })
			public void updatePassword() throws Exception {
				
				if(!admin.updatePassword(username,password)) {
					report.report("not able to logout successfully !!!",Reporter.FAIL);
				} else {
					report.report("able to logout successfully !!!", Reporter.PASS);
				}	
			}
			/*
			 for new customer setting up agencies 
			@Test(timeout = TEST_TIMEOUT)
			@SupportTestTypes(testTypes = { TestType.Selenium2 })
			@TestProperties(name = "setUpAgencies", paramsInclude = {"agency","intermediary","intermediaryNumber","region","testType" })
			public void setUpAgencies() throws Exception {
				
				if(!admin.setUpAgencies(agency,intermediary,intermediaryNumber,region)) {
					report.report("not able to logout successfully !!!",Reporter.FAIL);
				} else {
					report.report("able to logout successfully !!!", Reporter.PASS);
				}	
			}
			*/
		
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

	public String getNPI() {
		return NPI;
	}

	public void setNPI(String nPI) {
		NPI = nPI;
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
	}

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

}
