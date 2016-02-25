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
import com.ability.ease.testapi.IEligibility;
import com.ability.ease.testapi.IMyAccount;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

public class MyAccountTests extends BaseTest {

	IMyAccount myaccount;
	
	private AttributePair[] attrpair;
	
	
	private String timeZone;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private  String agency;
	private String claimsTime;
	private String eligibilityTime;
	private String eFTTime;
	
	
	private String rowOneStartday;
	private String rowOneendday;
	private String rowOneruntime;
	private String rowOnecredentails;
	
	private String rowTwoStartday;
	private String rowTwoendday;
	private String rowTworuntime;
	private String rowTwocredentails;
	
	private String rowThreeStartday;
	private String rowThreeendday;
	private String rowThreeruntime;
	private String rowThreecredentails;

	
	

	public MyAccountTests() {
		super();
		
	}

	@Before
	public void setUpTests() throws Exception{
		myaccount = (IMyAccount)context.getBean("myaccount");
	}
	
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		report.report(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\myaccount\\ChangeSchedule.xml");
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\myaccount\\ChangeSchedule.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Change Schedule", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangeSchedule() throws Exception 
	{

	  Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
	
	/*globalParamMap.put("Timezone", mapAttrValues.get("Timezone"));
		globalParamMap.put("Agency", mapAttrValues.get("Agency"));
		globalParamMap.put("claimsTimeDropDown", mapAttrValues.get("claimsTimeDropDown"));
		globalParamMap.put("EligibilityTimeDropDown", mapAttrValues.get("EligibilityTimeDropDown"));
		globalParamMap.put("EFTTimeDropDown", mapAttrValues.get("EFTTimeDropDown"));*/
		
		if(myaccount.verifyChangeSchedule(mapAttrValues)){
			report.report("Succesfully updated timezones !!", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to updated timeZones", Reporter.FAIL);
		}

	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Custom Schedule", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void customSchedule() throws Exception 
	{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
			
		String[] customschedulevalue = mapAttrValues.get("Custom").split(";");
		int count = Integer.parseInt(customschedulevalue[0]);
		for(int i=1;i<=count;i++){
			String[] schedule = customschedulevalue[i].split(",");
			globalParamMap.put("start"+i, schedule[0].trim());
			globalParamMap.put("end"+i, schedule[1].trim());
			globalParamMap.put("runtime"+i, schedule[2].trim());
			globalParamMap.put("credential"+i, schedule[3].trim());
		}
		
		if(myaccount.submitCustomSchedule(mapAttrValues)){
			report.report("Succesfully updated CustomConfiguration !!", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to updated customconfigurationForUser", Reporter.FAIL);
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

	public String getRowOneStartday() {
		return rowOneStartday;
	}

	public void setRowOneStartday(String rowOneStartday) {
		this.rowOneStartday = rowOneStartday;
	}

	public String getRowOneendday() {
		return rowOneendday;
	}

	public void setRowOneendday(String rowOneendday) {
		this.rowOneendday = rowOneendday;
	}

	public String getRowOneruntime() {
		return rowOneruntime;
	}

	public void setRowOneruntime(String rowOneruntime) {
		this.rowOneruntime = rowOneruntime;
	}

	public String getRowOnecredentails() {
		return rowOnecredentails;
	}

	public void setRowOnecredentails(String rowOnecredentails) {
		this.rowOnecredentails = rowOnecredentails;
	}

	public String getRowTwoStartday() {
		return rowTwoStartday;
	}

	public void setRowTwoStartday(String rowTwoStartday) {
		this.rowTwoStartday = rowTwoStartday;
	}

	public String getRowTwoendday() {
		return rowTwoendday;
	}

	public void setRowTwoendday(String rowTwoendday) {
		this.rowTwoendday = rowTwoendday;
	}

	public String getRowTworuntime() {
		return rowTworuntime;
	}

	public void setRowTworuntime(String rowTworuntime) {
		this.rowTworuntime = rowTworuntime;
	}

	public String getRowTwocredentails() {
		return rowTwocredentails;
	}

	public void setRowTwocredentails(String rowTwocredentails) {
		this.rowTwocredentails = rowTwocredentails;
	}

	public String getRowThreeStartday() {
		return rowThreeStartday;
	}

	public void setRowThreeStartday(String rowThreeStartday) {
		this.rowThreeStartday = rowThreeStartday;
	}

	public String getRowThreeendday() {
		return rowThreeendday;
	}

	public void setRowThreeendday(String rowThreeendday) {
		this.rowThreeendday = rowThreeendday;
	}

	public String getRowThreeruntime() {
		return rowThreeruntime;
	}

	public void setRowThreeruntime(String rowThreeruntime) {
		this.rowThreeruntime = rowThreeruntime;
	}

	public String getRowThreecredentails() {
		return rowThreecredentails;
	}

	public void setRowThreecredentails(String rowThreecredentails) {
		this.rowThreecredentails = rowThreecredentails;
	}
	
}
