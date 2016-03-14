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
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

public class MyAccountTests extends BaseTest {

	IMyAccount myaccount;
	
	private AttributePair[] attrpair;
	
	
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String timeZone,username,groupname,agency,claimsTime, eligibilityTime, eFTTime, sDay, EndDay, runtime, credential, timezone,ddeuserid,ddepassword,grouporagencycheckboxname,grouporagencyname,disableuntil;
	private int rownumber;
	private boolean isnewcustomer;

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
			report.report("Succesfully updated timezones !!", ReportAttribute.BOLD);
		}else{
			report.report("Failed to updated timeZones", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Submit Custom Schedule", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
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
		
		if(myaccount.submitCustomSchedule(mapAttrValues)){
			report.report("Succesfully updated CustomConfiguration !!", ReportAttribute.BOLD);
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
			report.report("Succesfully updated CustomConfiguration !!", ReportAttribute.BOLD);
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
			report.report("Succesfully verified Job Schedule Current Action as 'Initializing connection' !!", ReportAttribute.BOLD);
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
			report.report("Succesfully verified Options under Add FISS/DDE Setup page", ReportAttribute.BOLD);
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
			report.report("Succesfully Setted up FISS/DDE Settings", ReportAttribute.BOLD);
		}else{
			report.report("Failed to Set up FISS/DDE Settings", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "ADD FISS/DDE SETUP", paramsInclude = { "groupname,ddeuserid,ddepassword,testType" })
	public void setupDDECredential() throws Exception
	{
		if(myaccount.setupDDECredential(groupname,ddeuserid,ddepassword)){
			report.report("Succesfully Setted up FISS/DDE Settings", ReportAttribute.BOLD);
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
			report.report("Succesfully Edited up FISS/DDE Settings", ReportAttribute.BOLD);
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
			report.report("Succesfully Edited DDE Credential", ReportAttribute.BOLD);
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
			report.report("Succesfully Removed FISS/DDE Settings", ReportAttribute.BOLD);
		}else{
			report.report("Failed to Remove FISS/DDE Settings", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Disable Ease ", paramsInclude = { "grouporagencycheckboxname,grouporagencyname,disableuntil,testType" })
	public void disableEase() throws Exception
	{
		if(myaccount.disableEase(grouporagencycheckboxname,grouporagencyname,disableuntil)){
			report.report("Succesfully Disabled Ease", ReportAttribute.BOLD);
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
			report.report("Succesfully Resumed Group", ReportAttribute.BOLD);
		}else{
			report.report("Failed to Resume Group", Reporter.FAIL);
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

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
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

}
