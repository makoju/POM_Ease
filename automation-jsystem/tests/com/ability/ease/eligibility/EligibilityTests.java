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
	private String hic,agency,firstname,lastname,description;

	//Constructor
	public EligibilityTests()throws Exception{
		super();
	}

	@Before
	public void setupTests()throws Exception{
		elig = (IEligibility)context.getBean("elig");
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
		globalParamMap.put("hic", mapAttrValues.get("HIC"));
		globalParamMap.put("agency", mapAttrValues.get("Agency"));
		globalParamMap.put("lastname", mapAttrValues.get("Last Name"));
		globalParamMap.put("firstname", mapAttrValues.get("First Name"));
		
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
	@TestProperties(name = "Verify Eligibility ${description}", paramsInclude = { "description,hic,agency,firstname,lastname,testType" })
	public void verifyEligibility() throws Exception {
		report.report("Inside verifyEligibility tests method");
		if(!elig.verifyEligibility(hic,agency,firstname,lastname)) {
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
	@TestProperties(name = "Verify HETS Activities Completed Status", paramsInclude = { "hic,agency,firstname,lastname,testType" })
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
			report.report("Failed to verify Navigation to Claim Info Screnn!!!",	Reporter.FAIL);
		} else {
			report.report("Navigation to Claim Info Page is verified !!!", Reporter.PASS);
		}	
	}
	
	@Override
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName) throws Exception {	
		super.handleUIEvent(map, methodName);
		if(methodName.matches("verifyEligibility")) {
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml");
		}
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
