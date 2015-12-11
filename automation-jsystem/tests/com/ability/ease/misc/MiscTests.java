package com.ability.ease.misc;


import java.util.HashMap;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;
import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.UserActionType;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IMiscellaneous;

public class MiscTests extends BaseTest{

	private IMiscellaneous misc;
	private String userName;
	private String password;
	private MyAccountSubMenu subMenuItems;
	private String sExpectedOutput;
	private UserActionType userAction;
	
	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	
	@Before
	public void setupTests()throws Exception{
		misc = (IMiscellaneous)context.getBean("misc");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Login as ${userName}", paramsInclude = { "userName, password, testType" })
	public void loginToEase()throws Exception{

		if(misc.validLogin(userName, password)){
			report.report("Successfully Logged in as user: "+userName, Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to login with username: "+userName, Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Invalid login as ${userName}", paramsInclude = { "userName, password, testType" })
	public void invalidLoginToEase()throws Exception{

		if(misc.invalidLogin(userName, password)){
			report.report("User access is denied successfully...", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("User access not denied...", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Help", paramsInclude = { "testType" })
	public void verifyHelp()throws Exception{
		if(misc.verifyHelp()){
			report.report("Help link present in the page and contains a valid reference...", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("NO help link exist in the page", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Support", paramsInclude = { "testType" })
	public void verifySupport()throws Exception{
		if(misc.verifySupport()){
			report.report("Support link present in the page and contains a valid reference...", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Support link present the page is not valid", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Page Refresh", paramsInclude = { "testType" })
	public void verifyPageRefresh()throws Exception{
		if(misc.verifyPageRefresh()){
			report.report("Support link present in the page and contains a valid reference...", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Support link present the page is not valid", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify FWDBACK", paramsInclude = { "testType" })
	public void verifyFWDBACK()throws Exception{
		if(misc.verifyFWDBACK()){
			report.report("Successfully verified forward and backward links functionality", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify forward and backward links!!!", Reporter.FAIL);
		}
	}
	
	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 * @param - userAction :: user action type whether to read the personal information or update the personal information
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Personal Information : ${userAction}", paramsInclude = { "AttributeNameValueDialogProvider, userAction, testType" })
	public void verifyPersonalInfo()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(misc.verifyPersonalInfo(mapAttrValues, userAction)){
			report.report("Successfully verified the fields present in personal information tab", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify data in personal inforamtion tab", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Left Menu Items in My Account Tab", paramsInclude = { "testType, subMenuItems, sExpectedOutput"})
	public void verifyLeftMenuInMyAccountTab()throws Exception{
		if(misc.verifyLeftMenuInMyAccountTab(subMenuItems, sExpectedOutput)){
			report.report("Successfully verified the left menu item in my account tab", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify the left menu item in my account tab", Reporter.FAIL);
		}
	}

	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Setup Alert Option For User: ${userName} ", paramsInclude = { "userName, AttributeNameValueDialogProvider, testType" })
	public void verifyAlertOptions()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(misc.verifyAlertOptions(mapAttrValues,userName)){
			report.report("Successfully verified setup alert options for user:" + userName, Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify setting up alert options!", Reporter.FAIL);
		}
	}
	
	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Add HIC Under Basic View", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void verifyAddHICs()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(misc.verifyAddHICs(mapAttrValues)){
			report.report("Successfully added HIC", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to add HIC under basic view", Reporter.FAIL);
		}
	}
	
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		if(!methodName.equalsIgnoreCase("verifyAddHICs")){
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Misc\\Misc.xml");
		}
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Misc\\MyDDE.xml");
	}
	/*####
	##Getters and Setters methods
	######*/

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MyAccountSubMenu getSubMenuItems() {
		return subMenuItems;
	}

	@ParameterProperties(description = "Select submenu item to select" )
	public void setSubMenuItems(MyAccountSubMenu subMenuItems) {
		this.subMenuItems = subMenuItems;
	}

	public String getsExpectedOutput() {
		return sExpectedOutput;
	}

	@ParameterProperties(description = "Expected output" )
	public void setsExpectedOutput(String sExpectedOutput) {
		this.sExpectedOutput = sExpectedOutput;
	}

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

	public UserActionType getUserAction() {
		return userAction;
	}

	@ParameterProperties(description = "Select action type Read: to read, Modify: to change values")
	public void setUserAction(UserActionType userAction) {
		this.userAction = userAction;
	}
	
	
}
