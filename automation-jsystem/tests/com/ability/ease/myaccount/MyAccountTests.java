package com.ability.ease.myaccount;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IMyAccount;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

public class MyAccountTests extends BaseTest {

	IMyAccount myaccount;

	private AttributePair[] attrpair;



	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String timeZone,username,groupname,agency,claimsTime, eligibilityTime, eFTTime, sDay, EndDay, runtime, credential, timezone,ddeuserid,ddepassword,grouporagencycheckboxname,grouporagencyname,disableuntil,expectedalertmessage;
	private int rownumber;
	private boolean isnewcustomer;


	private String expectedColor,agency1;
	private String username1;
	private String sUsername;
	private String dAgency;
	private String agencyOne;
	private String oldpassword;
	private String newpassword;
	private String expectedmessage;
	private String verifypassword;
	private String description;
	


	@Before
	public void setUpTests() throws Exception{
		myaccount = (IMyAccount)context.getBean("myaccount");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\myaccount\\ChangeSchedule.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Change Schedule", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangeSchedule() throws Exception 
	{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);

		if(myaccount.verifyChangeSchedule(mapAttrValues)){
			report.report("Succesfully updated timezones !!", Reporter.PASS);
		}else{
			report.report("Failed to updated timeZones", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Submit Custom Schedule", paramsInclude = { "AttributeNameValueDialogProvider,testType, expectedalertmessage" })
	public void submitCustomSchedule() throws Exception 
	{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);

		String[] customschedulevalue = mapAttrValues.get("Custom").split(";");
		int count = Integer.parseInt(customschedulevalue[0]);
		keepAsGloablParameter("Agency", mapAttrValues.get("Agency"));
		keepAsGloablParameter("Timezone", mapAttrValues.get("Timezone"));
		for(int i=1;i<=count;i++){
			String[] schedule = customschedulevalue[i].split(",");
			keepAsGloablParameter("sDay"+i, schedule[0].trim());
			keepAsGloablParameter("EndDay"+i, schedule[1].trim());
			keepAsGloablParameter("Runtime"+i, schedule[2].trim());
			keepAsGloablParameter("Credential"+i, schedule[3].trim());
		}

		if(myaccount.submitCustomSchedule(mapAttrValues,expectedalertmessage)){
			report.report("Succesfully updated CustomConfiguration !!", Reporter.PASS);
		}else{
			report.report("Failed to updated customconfigurationForUser", Reporter.FAIL);
		}

	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Custom Schedule", paramsInclude = { "agency,sDay, EndDay, runtime, credential, timezone,rownumber,testType" })
	public void verifyCustomSchedule() throws Exception 
	{
		if(myaccount.verifyCustomSchedule(agency,sDay, EndDay, runtime, credential, timezone,rownumber)){
			report.report("Succesfully updated CustomConfiguration !!", Reporter.PASS);
		}else{
			report.report("Failed to updated customconfigurationForUser", Reporter.FAIL);
		}

	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify JobSchedule CurrentAction for ${agency}", paramsInclude = { "agency,testType" })
	public void verifyJobScheduleCurrentAction()
	{
		if(myaccount.verifyJobScheduleCurrentAction(agency)){
			report.report("Succesfully verified Job Schedule Current Action as 'Initializing connection' !!", Reporter.PASS);
		}else{
			report.report("Failed to Verify Job Schedule Current Action as 'Initializing connection' ", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Options Under Add FISS/DDE Setup page", paramsInclude = { "testType" })
	public void verifyOptionsUnderAddFISSDDESetup() throws Exception
	{
		if(myaccount.verifyOptionsUnderAddFISSDDESetup()){
			report.report("Succesfully verified Options under Add FISS/DDE Setup page", Reporter.PASS);
		}else{
			report.report("Failed to Verify Options under Add FISS/DDE Setup page", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "ADD FISS/DDE SETUP", paramsInclude = { "username, groupname,agency,isnewcustomer,testType" })
	public void addFISSDDESetup() throws Exception
	{
		if(myaccount.addFISSDDESetup(username,groupname,agency,isnewcustomer)){
			report.report("Succesfully Setted up FISS/DDE Settings", Reporter.PASS);
		}else{
			report.report("Failed to Set up FISS/DDE Settings", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Setup DDE Credential", paramsInclude = { "groupname,ddeuserid,ddepassword,testType" })
	public void setupDDECredential() throws Exception
	{
		if(myaccount.setupDDECredential(groupname,ddeuserid,ddepassword)){
			report.report("Succesfully Setted up FISS/DDE Settings", Reporter.PASS);
		}else{
			report.report("Failed to Set up FISS/DDE Settings", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Edit FISS/DDE SETUP", paramsInclude = { "groupname,agency,testType" })
	public void editFISSDDESetup() throws Exception
	{
		if(myaccount.editFISSDDESetup(groupname,agency)){
			report.report("Succesfully Edited up FISS/DDE Settings", Reporter.PASS);
		}else{
			report.report("Failed to Edit FISS/DDE Settings", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "edit DDE Credential", paramsInclude = { "groupname,ddeuserid,ddepassword,testType" })
	public void editDDECredential() throws Exception
	{
		if(myaccount.editDDECredential(groupname,ddeuserid,ddepassword)){
			report.report("Succesfully Edited DDE Credential", Reporter.PASS);
		}else{
			report.report("Failed to Edit DDE Credential", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Remove FISS/DDE SETUP", paramsInclude = { "groupname,testType" })
	public void removeFISSDDESetup() throws Exception
	{
		if(myaccount.removeFISSDDESetup(groupname)){
			report.report("Succesfully Removed FISS/DDE Settings", Reporter.PASS);
		}else{
			report.report("Failed to Remove FISS/DDE Settings", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Disable Ease ", paramsInclude = { "grouporagencycheckboxname,grouporagencyname,disableuntil,expectedalertmessage,testType" })
	public void disableEase() throws Exception
	{
		if(myaccount.disableEase(grouporagencycheckboxname,grouporagencyname,disableuntil,expectedalertmessage)){
			report.report("Succesfully Disabled Ease", Reporter.PASS);
		}else{
			report.report("Failed to Disable Ease", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Resume Ease ", paramsInclude = { "groupname,ddeuserid,ddepassword,testType" })
	public void resumeEase() throws Exception
	{
		if(myaccount.resumeEase(groupname,ddeuserid,ddepassword)){
			report.report("Succesfully Resumed Group", Reporter.PASS);
		}else{
			report.report("Failed to Resume Group", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Delete All Custom Schedule for ${agency} ", paramsInclude = { "agency,testType" })
	public void deleteAllCustomSchedule() throws Exception
	{
		if(myaccount.deleteAllCustomSchedule(agency)){
			report.report("Succesfully Deleted Custom Schedule for agency: "+agency, Reporter.PASS);
		}else{
			report.report("Failed to Delete Custom Schedule for agency: "+agency, Reporter.FAIL);
		}
	}


/*	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Custom ScheduleDelete", paramsInclude = { "agency,testType" })
	public void verifyCustomScheduleDeleteTwoRows() throws Exception 
	{
		if(myaccount.verifyCustomScheduleDelete(agency)){
			report.report("Succesfully updated CustomConfiguration !!", Reporter.PASS);
		}else{
			report.report("Failed to updated customconfigurationForUser", Reporter.FAIL);
		}

	}*/
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Custom Schedule Delete last two rows", paramsInclude = { " dAgency,testType" })
	public void CustomScheduleDeleteTwoRows() throws Exception 
	{
		if(myaccount.deletecustomScheduleRows(dAgency)){
			report.report("Succesfully updated CustomConfiguration !!", Reporter.PASS);
		}else{
			report.report("Failed to updated customconfigurationForUser", Reporter.FAIL);
		}

	}



	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "verifyRedColorTextInDropDownforInvalidUser", paramsInclude = { "agency,credential,testType" })
	public void verifyInvalidDDEcredentials() throws Exception
	{
		if(myaccount.verifyInvalidDDEcredentials(agency,credential)){
			report.report("Found Invalid Users in dropdown which are in RED color : "+agency, Reporter.PASS);
		}else{
			report.report("Found valid Users in dropdown: "+agency, Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "updateDDEpasswordProblem ", paramsInclude = { "username1,testType" })
	public void updateDDEpasswordProblem() throws Exception
	{
		if(myaccount.updateDDEpasswordProblem(username1)){
			report.report("UserName is successfully made invalid : "+username1, Reporter.PASS);
		}else{
			report.report("UserName is UNsuccessfully made invalid: "+username1, Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "VerifySetUpAlertsFunctionlity", paramsInclude = { "testType,sUsername" })

	public void verifysetUpAlerts()throws Exception{

		if(myaccount.verifySetupalerts(sUsername)){
			report.report("Set up Alerts modified", Reporter.PASS);
		}else {
			report.report(" updated user password", Reporter.FAIL);
		}
		
	}
		
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )

	@TestProperties(name = "verifyDeleteOptionNotEnableforFirstSchedule", paramsInclude = { "agency,testType" })
	public void verifyDeleteOptionNotEnableforFirstSchedule() throws Exception 
	{
		if(myaccount.verifyDeleteOptionNotEnableforFirstSchedule(agency)){
			report.report("Succesfully updated CustomConfiguration !!", Reporter.PASS);
		}else{
			report.report("Failed to updated customconfigurationForUser", Reporter.FAIL);
		}

	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Change Password ${description}", paramsInclude = { "testType,oldpassword, newpassword,verifypassword,expectedmessage,description" })
	public void verifyChangePassword()throws Exception{

		if(myaccount.verifyChangePassword(oldpassword, newpassword,verifypassword,expectedmessage)){
			report.report("Succesfully Changed user password !!", Reporter.PASS);
		}else {
			report.report("Failed to Change user password", Reporter.FAIL);
		}
	}
	
	/*##
	# Getters and Setters
	##*/

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public AttributePair[] getAttrpair() {
		return attrpair;
	}

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

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getClaimsTime() {
		return claimsTime;
	}

	public void setClaimsTime(String claimsTime) {
		this.claimsTime = claimsTime;
	}

	public String getEligibilityTime() {
		return eligibilityTime;
	}

	public void setEligibilityTime(String eligibilityTime) {
		this.eligibilityTime = eligibilityTime;
	}

	public String geteFTTime() {
		return eFTTime;
	}

	public void seteFTTime(String eFTTime) {
		this.eFTTime = eFTTime;
	}

	public String getsDay() {
		return sDay;
	}

	public void setsDay(String sDay) {
		this.sDay = sDay;
	}

	public String getEndDay() {
		return EndDay;
	}

	public void setEndDay(String endDay) {
		EndDay = endDay;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public int getRownumber() {
		return rownumber;
	}

	public void setRownumber(int rownumber) {
		this.rownumber = rownumber;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDdeuserid() {
		return ddeuserid;
	}

	public void setDdeuserid(String ddeuserid) {
		this.ddeuserid = ddeuserid;
	}

	public String getDdepassword() {
		return ddepassword;
	}

	public void setDdepassword(String ddepassword) {
		this.ddepassword = ddepassword;
	}

	public boolean isIsnewcustomer() {
		return isnewcustomer;
	}

	public void setIsnewcustomer(boolean isnewcustomer) {
		this.isnewcustomer = isnewcustomer;
	}

	public String getGrouporagencycheckboxname() {
		return grouporagencycheckboxname;
	}

	public void setGrouporagencycheckboxname(String grouporagencycheckboxname) {
		this.grouporagencycheckboxname = grouporagencycheckboxname;
	}

	public String getGrouporagencyname() {
		return grouporagencyname;
	}

	public void setGrouporagencyname(String grouporagencyname) {
		this.grouporagencyname = grouporagencyname;
	}

	public String getDisableuntil() {
		return disableuntil;
	}

	public void setDisableuntil(String disableuntil) {
		this.disableuntil = disableuntil;
	}

	public String getExpectedColor() {
		return expectedColor;
	}

	public void setExpectedColor(String expectedColor) {
		this.expectedColor = expectedColor;
	}

	public String getAgency1() {
		return agency1;
	}

	public void setAgency1(String agency1) {
		this.agency1 = agency1;
	}

	public String getUsername1() {
		return username1;
	}

	public void setUsername1(String username1) {
		this.username1 = username1;
	}

	public String getsUsername() {
		return sUsername;
	}

	public void setsUsername(String sUsername) {
		this.sUsername = sUsername;
	}

	public String getAgencyOne() {
		return agencyOne;
	}

	public void setAgencyOne(String agencyOne) {
		this.agencyOne = agencyOne;
	}

	public String getdAgency() {
		return dAgency;
	}

	public void setdAgency(String dAgency) {
		this.dAgency = dAgency;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getExpectedmessage() {
		return expectedmessage;
	}

	public void setExpectedmessage(String expectedmessage) {
		this.expectedmessage = expectedmessage;
	}

	public String getVerifypassword() {
		return verifypassword;
	}

	public void setVerifypassword(String verifypassword) {
		this.verifypassword = verifypassword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}
	
	public String getExpectedalertmessage() {
		return expectedalertmessage;
	}

	public void setExpectedalertmessage(String expectedalertmessage) {
		this.expectedalertmessage = expectedalertmessage;
	}
}
