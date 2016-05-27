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
	private String hic,agency,firstname,lastname,description, status,contactname;
	private String customername;
	private HETSStatus hetsstatus;
	
	private enum HETSStatus{
		ENABLE("enable"),
		DISABLE("disable");
		
		String value;
		private HETSStatus(String value){
			this.value=value;
		}
		
		public String getValue(){
			return value;
		}
	}

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
		int latestactivitycount,count=0;
		
		do{
			report.report("Activities count not increased so trying again attempt#"+(count+1));
			latestactivitycount = elig.getActivityCount(status);
			Thread.sleep(10000);
		}while(count++ < 5 && latestactivitycount == activitycount);
		
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
		int latestactivitycount,count=0;
		
		do{
			report.report("Activities count not decreased so trying again attempt#"+(count+1));
			latestactivitycount = elig.getActivityCount(status);
			Thread.sleep(5000);
		}while(count++ < 5 && latestactivitycount == activitycount);
		
		if(activitycount-1 == latestactivitycount)
			report.report("Activity: "+status+" count decreased by one", Reporter.PASS);
		else
			report.report("Activity: "+status+" count was not decreased by one Actual: "+latestactivitycount+" Expected: "+(activitycount-1), Reporter.FAIL);
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Options Under Patient Information Screen", paramsInclude = { "hic,firstname,lastname,testType" })
	public void verifyOptionsUnderPatientInformationScreen() throws Exception{
		report.report("Inside verifyOptionsUnderPatientInformationScreen test method");
		if(!elig.verifyOptionsUnderPatientInformationScreen(hic,firstname,lastname)) {
			report.report("Failed to verify Options Under Patient Information Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Options Under Patient Information Screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Search Activity Log By HIC", paramsInclude = { "status,hic,testType" })
	public void searchactivitylogByHIC() throws Exception{
		report.report("Inside searchactivitylogByHIC test method");
		if(!elig.searchactivitylogByHIC(status,hic)) {
			report.report("Failed to search activitylog By HIC!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully searched activitylog By HIC!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "navigate to patient information screen", paramsInclude = { "firstname,lastname,hic,testType" })
	public void navigatetoPatientInfoScreen() throws Exception{
		report.report("Inside navigatetopatientinformation test method");
		if(!elig.navigatetoPatientInfoScreen(firstname,lastname,hic)) {
			report.report("Failed to navigate to patient information screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully navigated to patient information screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "verifyActivityLogSearchOnlynotacknowledged", paramsInclude = { "testType" })
	public void verifyActivityLogSearchOnlynotacknowledged() throws Exception{
		report.report("Inside verifyActivityLogSearchOnlynotacknowledged test method");
		if(!elig.verifyActivityLogSearchOnlynotacknowledged()) {
			report.report("Failed to verify ActivityLogSearch Only notacknowledged!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified ActivityLogSearch Only notacknowledged!!!", Reporter.PASS);
		}	
	}

	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation To HomeScreen From CompletedActivityLogScreen", paramsInclude = { "testType" })
	public void VerifyNavigationToHomeScreenFromCompletedActivityLogScreen() throws Exception {
		report.report("Inside VerifyNavigationToHomeScreenFromCompletedActivityLogScreen tests method");
		if(!elig.verifyNavigationToHomeScreenFromCompletedActivityLogScreen()) {
			report.report("Failed to verify Navigation to Home Screen from Completed Activity Log Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Navigation to Home Screen from Completed Activity Log Screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify PDF Export Functionality In CompletedActivityLogScreen", paramsInclude = { "testType" })
	public void VerifyPDFExportInCompletedActivityLogScreen() throws Exception {
		report.report("Inside VerifyPDFExportInCompletedActivityLogScreen tests method");
		if(!elig.verifyPDFExportInCompletedActivityLogScreen()) {
			report.report("Failed to verify PDF export functionality in Completed Activity Log Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified PDF export functionality in Completed Activity Log Screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation To HomeScreen From Patient information Screen", paramsInclude = { "hic,testType" })
	public void verifyNavigationToHomeScreenFromPatientInfoScreen() throws Exception {
		report.report("Inside verifyNavigationToHomeScreenFromPatientInfoScreen tests method");
		if(!elig.verifyNavigationToHomeScreenFromPatientInfoScreen(hic)) {
			report.report("Failed to verify Navigation to Home Screen from Patient Information Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Navigation to Home Screen from Patient Information Screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation To Advanced Search Screen From LiveSearch Screen", paramsInclude = { "testType" })
	public void VerifyNavigationOfAdvanceSearchFromLiveSearch() throws Exception {
		report.report("Inside VerifyNavigationOfAdvanceSearchFromLiveSearch tests method");
		if(!elig.VerifyNavigationOfAdvanceSearchFromLiveSearch()) {
			report.report("Failed to verify Navigation to Advanced Screen from LiveSearch Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified Navigation to Advanced Search Screen from LiveSearch Screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Navigation to Eligibility Check Report Screen", paramsInclude = { "firstname,lastname,testType" })
	public void navigatetoEligibilityReport() throws Exception {
		report.report("Inside navigatetoEligibilityReport test method");
		if(!elig.navigatetoEligibilityReport(firstname, lastname)) {
			report.report("Failed to verify Navigation to Eligibility Check Report Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully Navigated to Eligibility Check Report Screen!!!", Reporter.PASS);
		}	
	}
	
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify Most Benefit and STC45 Fields", paramsInclude = { "testType" })
	public void verifyMostBenefitSTC45Fields() throws Exception {
		report.report("Inside verifyMostBenefitSTC45Fields tests method");
		if(!elig.verifyMostBenefitSTC45Fields()) {
			report.report("Failed to verify MostBenefit and STC45Fields in Eligibility Check report Screen!!!",	Reporter.FAIL);
		} else {
			report.report("Successfully verified MostBenefit and STC45Fields in Eligibility Check report Screen!!!", Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "${hetsstatus} HETS for ${customername}", paramsInclude = { "testType, customername, hetsstatus" })
	public void enableordisableHETS() throws Exception {
		
		if(!elig.enableordisableHETS(customername,hetsstatus.getValue())) {
			report.report("Failed to "+hetsstatus.getValue()+" HETS for customer"+customername,	Reporter.FAIL);
		} else {
			report.report("Successfully "+hetsstatus.getValue()+" HETS for customer"+customername,	Reporter.PASS);
		}	
	}
	
	@Test(timeout = TEST_TIMEOUT)
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "enableAndVerifyPsychiatricSTCforNPI", paramsInclude = { "testType, customername, agency" })
	public void enableandVerifyPsychiatricSTCforNPI() throws Exception {
		
		if(!elig.enableandVerifyPsychiatricSTCforNPI(customername,agency)) {
			report.report("Failed to enable PsychiatricSTCforNPI of agency: "+agency,	Reporter.FAIL);
		} else {
			report.report("Successfully enable PsychiatricSTCforNPI of agency: "+agency,	Reporter.PASS);
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

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public HETSStatus getHetsstatus() {
		return hetsstatus;
	}

	public void setHetsstatus(HETSStatus hetsstatus) {
		this.hetsstatus = hetsstatus;
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
