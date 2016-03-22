package com.ability.ease.eligibility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import com.ability.ease.testapi.IEligibility;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.TestType;

public class EligibilityTests extends BaseTest{

	private IEligibility elig;
	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String hic,agency,firstname,lastname,description, status;

	@Before
	public void setupTests()throws Exception{
		elig = (IEligibility)context.getBean("elig");
	}
	
	
	@Override
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName) throws Exception {	
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml");
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Submit Eligibility Check", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void submitEligibilityCheck() throws Exception {
		report.report("Inside submitEligibilityCheck tests method");
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		//adding milliseconds to firstname to avoid data duplicate problems
		mapAttrValues.put("First Name", mapAttrValues.get("First Name")+System.currentTimeMillis());

		keepAsGloablParameter("hic", mapAttrValues.get("HIC"));
		keepAsGloablParameter("agency", mapAttrValues.get("Agency"));
		keepAsGloablParameter("lastname", mapAttrValues.get("Last Name"));
		keepAsGloablParameter("firstname", mapAttrValues.get("First Name"));
		
		if(!elig.submitEligibilityCheck(mapAttrValues)) {
			report.report("Failed to Submit eligibility Check!!!",	Reporter.FAIL);
		} else {
			report.report("Eligibility Check Successfully Submitted !!!", Reporter.PASS);
		}
	}

	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Eligibility ${description}", paramsInclude = { "description,firstname,lastname,status, testType" })
	public void verifyEligibilityStatus() throws Exception {
		report.report("Inside verifyEligibility tests method");
		if(!elig.verifyEligibilityStatus(firstname, lastname, status)) {
			report.report("Failed to verify eligibility !!!", Reporter.FAIL);
		} else {
			report.report("Eligibility verified !!!", Reporter.PASS);
		}	
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation to Claim Details Screen", paramsInclude = { "hic,agency,firstname,lastname,testType" })
	public void navigatetoClaimDetails() throws Exception {
		report.report("Inside verifyNavigationToClaimDetailsScreen test method");
		if(!elig.navigatetoClaimDetails(firstname, lastname,hic)) {
			report.report("Failed to verify Navigation to Claim Details Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully Navigated to Claim Details Screen!!!", Reporter.PASS);
		}	
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Eligibility acknowledge", paramsInclude = { "hic,agency,firstname,lastname,testType" })
	public void acknoweldgeEligibility() throws Exception {
		report.report("Inside acknowledge eligibility test method");
		if(!elig.acknoweldgeEligibility(firstname, lastname)) {
			report.report("Failed to acknowledge Eligibility!!!",	Reporter.FAIL);
		} else {
			report.report("Eligibility is successfully acknowledged!!!", Reporter.PASS);
		}	
	}

	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify HETS Activities Completed Status Report", paramsInclude = { "hic,agency,firstname,lastname,testType" })
	public void verifyHETSActivitiesCompletedStatusReport() throws Exception {
		report.report("Inside verifyHETSActivitiesCompletedStatusReport tests method");
		if(!elig.verifyHETSActivitiesCompletedStatusReport(hic, agency, firstname, lastname)) {
			report.report("Failed to verify HETS Activities Completed!!!",	Reporter.FAIL);
		} else {
			report.report("HETS Activities Completed is verified !!!", Reporter.PASS);
		}	
	}
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation to UB04", paramsInclude = { "hic,agency,firstname,lastname,testType" })
	public void verifyNavigationToUB04FromPatientInfoScreen() throws Exception {
		report.report("Inside verifyNavigationToUB04FromPatientInfoScreen tests method");
		if(!elig.verifyNavigationToUB04FromPatientInfoScreen(hic, agency, firstname, lastname)) {
			report.report("Failed to verify Navigation to UB04 !!!",	Reporter.FAIL);
		} else {
			report.report("Navigation to UB04 is verified !!!", Reporter.PASS);
		}	
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation to Claim Info Screen", paramsInclude = { "hic,agency,firstname,lastname,testType" })
	public void verifyNavigationToClaimInfoFromPatientInfoScreen() throws Exception {
		report.report("Inside verifyNavigationToClaimInfoFromPatientInfoScreen tests method");
		if(!elig.verifyNavigationToClaimInfoFromPatientInfoScreen(hic, agency, firstname, lastname)) {
			report.report("Failed to verify Navigation to Claim Info Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Navigation to Claim Info Page is verified !!!", Reporter.PASS);
		}	
	}
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Options Under Pending Activity Log Screen", paramsInclude = { "testType" })
	public void verifyOptionsUnderPendingActivityLogScreen() throws Exception {
		report.report("Inside verifyOptionsUnderPendingActivityLogScreen tests method");
		if(!elig.verifyOptionsUnderPendingActivityLogScreen()) {
			report.report("Failed to verify Options Under Pending Activity Log Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Options Under Pending Activity Log Screen!!!", Reporter.PASS);
		}	
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Options Under Completed Activity Log Screen", paramsInclude = { "testType" })
	public void verifyOptionsUnderCompletedActivityLogScreen() throws Exception {
		report.report("Inside verifyOptionsUnderCompletedActivityLogScreen tests method");
		if(!elig.verifyOptionsUnderCompletedActivityLogScreen()) {
			report.report("Failed to verify Options Under Completed Activity Log Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Options Under Completed Activity Log Screen!!!", Reporter.PASS);
		}	
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Options Under Failed Activity Log Screen", paramsInclude = { "testType" })
	public void verifyOptionsUnderFailedActivityLogScreen() throws Exception {
		report.report("Inside verifyOptionsUnderFailedActivityLogScreen tests method");
		if(!elig.verifyOptionsUnderFailedActivityLogScreen()) {
			report.report("Failed to verify Options Under Failed Activity Log Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Options Under Failed Activity Log Screen!!!", Reporter.PASS);
		}	
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Get the count of ${status} Activites", paramsInclude = { "status,testType" })
	public void getActivityCount() throws Exception {
		report.report("Inside getActivityCount test method");
		globalParamMap.put(status+"activitycount", Integer.toString(elig.getActivityCount(status)));
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify ${status} Activities CountIncreasedByOne", paramsInclude = { "status,testType" })
	public void VerifyActivityCountIncreasedByOne() throws Exception {
		report.report("Inside VerifyActivityCountIncreasedByOne test method");
		int activitycount = Integer.parseInt(globalParamMap.get(status+"activitycount"));
		Thread.sleep(18000);//wait for 10 secs to get the activity table update
		int latestactivitycount = elig.getActivityCount(status);
		if(activitycount+1 == latestactivitycount)
			report.report("Activity: "+status+" count increased by one", Reporter.PASS);
		else
			report.report("Activity: "+status+" count was not increased by one Actual: "+latestactivitycount+" Expected: "+(activitycount+1), Reporter.FAIL);
	}
	
	/**
	 * 
	 */
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify ${status} Activities CountDecreasedByOne", paramsInclude = { "status,testType" })
	public void VerifyActivityCountDecreasedByOne() throws Exception {
		report.report("Inside VerifyActivityCountDecreasedByOne test method");
		int activitycount = Integer.parseInt(globalParamMap.get(status+"activitycount"));
		Thread.sleep(18000);//wait for 10 secs to get the activity table update
		int latestactivitycount = elig.getActivityCount(status);
		if(activitycount-1 == latestactivitycount)
			report.report("Activity: "+status+" count decreased by one", Reporter.PASS);
		else
			report.report("Activity: "+status+" count was not decreased by one Actual: "+latestactivitycount+" Expected: "+(activitycount-1), Reporter.FAIL);
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
	
	public String getHic() {
		return hic;
	}

	public void setHic(String hic) {
		this.hic = hic;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getFirstname() {
		return firstname;
	}
	
	@ParameterProperties(description = "Provide the FirstName Value it suffix current time in milliseconds to it")
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}
