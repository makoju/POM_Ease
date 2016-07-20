package com.ability.ease.claims;

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
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ClaimStatusType;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IClaims;

public class ClaimsTests extends BaseTest{

	private IClaims claims;

	private String claimLineNumberToDelete;
	private String claimLineNumberToAdd;
	private String claimLineNumberToEdit;
	private String newClaimLineEntry;
	private ClaimStatusType claimStatus;
	private String patientControlNumber;
	private String HIC;
	private String claimLineEntries;
	private String expectedOutput;
	private String coveredOrNonCovered;
	private String claimLinePosition;
	private String undoOption;
	private String unlockOption;

	private float claimTotalChargesCovered;
	private float claimTotalChargesNonCovered;
	private float previousClaimTotals;

	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		claims = (IClaims)context.getBean("claims");
	}

	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify UB04 Form Fields", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void verifyUB04FormFields()throws Exception{

		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!claims.verifyUB04FormFeatures(mapAttrValues)){
			report.report("Failed to verify the fields in UB04 form!", Reporter.FAIL);
		}else{
			report.report("Successfully verified all the fields in UB04 form!", Reporter.ReportAttribute.BOLD);
		}

	}

	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Add Remove Claim Lines For New Claim", paramsInclude = { "AttributeNameValueDialogProvider, claimLineNumberToDelete, "
			+ "claimLineNumberToAdd, newClaimLineEntry, testType" })
	public void verifyAddRemoveClaimLines()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!claims.verifyAddRemoveClaimLines(mapAttrValues, claimLineNumberToDelete, claimLineNumberToAdd, newClaimLineEntry)){
			report.report("Failed to verify add or remove claim line functionality in UB04 form!", Reporter.FAIL);
		}else{
			report.report("Successfully verified add or remove claim line functionality in UB04 form!", Reporter.ReportAttribute.BOLD);
		}

	}

	/**
	 * Use this method to select and search for a claim by selecting status in advance search page
	 * @ClaimStatus : Claim status 
	 * @throws Exception
	 */

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Select Claim Status In Advance Search Page And Search", paramsInclude = { "claimStatus, testType" })
	public void selectStatusLocationInAdvanceSearchPageAndSearch()throws Exception{
		String sClaimStatusToSelect = claimStatus.toString();
		if(!claims.selectStatusLocationInAdvanceSearchPageAndSearch(sClaimStatusToSelect)){
			report.report("Failed to select " + sClaimStatusToSelect +" value from S/Loc dropdown", Reporter.FAIL);
		}else{
			report.report("Selected " + sClaimStatusToSelect +" value from S/Loc dropdown successfully", Reporter.ReportAttribute.BOLD);
		}
	}


	/**
	 * Use this method to select a claim record from search result page based on PCN patient control number
	 * @patientControlNumber : PCN
	 * @throws Exception
	 */

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Select Claim Record From Search Result Page", paramsInclude = { "patientControlNumber, testType" })
	public void selectClaimRecordFromSearchResults()throws Exception{
		if(!claims.selectClaimRecordFromSearchResults(patientControlNumber)){
			report.report("Failed to select record whose PCN is :" + patientControlNumber , Reporter.FAIL);
		}else{
			report.report("Succesfully clicked on rench icon and selected claim record whose PCN is : " + patientControlNumber , Reporter.ReportAttribute.BOLD);
		}
	}


	/**
	 * Use this method to select a claim record from search result page based on PCN patient control number
	 * @patientControlNumber : PCN
	 * @throws Exception
	 */

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Data In Edit Claim Line Pop-Up Window", paramsInclude = { "AttributeNameValueDialogProvider, claimLineNumberToEdit, testType" })
	public void verifyDataInEditClaimLinePopUpWindow()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!claims.verifyDataInEditClaimLinePopUpWindow(mapAttrValues,claimLineNumberToEdit)){
			report.report("Failed to verify data in edit claim line pop-up window", Reporter.FAIL);
		}else{
			report.report("Succesfully verified data in edit claim line pop-up window", Reporter.ReportAttribute.BOLD);
		}
	}


	/**
	 * Use this method only to fill values in UB04 form
	 * @throws Exception
	 */

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Fill values in UB04 Form", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void fillUB04FormValuesOnly()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		HIC = mapAttrValues.get("Insureds Unique ID Primary");
		claimLineEntries = mapAttrValues.get("ClaimLineEntries");
		keepAsGloablParameter("HIC", HIC);
		keepAsGloablParameter("claimLineEntries", claimLineEntries);
		if(!claims.fillUB04FormValuesOnly(mapAttrValues)){
			report.report("Fail to fill values in UB04 form !!", Reporter.FAIL);
		}else{
			report.report("Succesfully filled up values in UB04 form!!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to open an existing claim from pending activity box
	 * @throws Exception
	 */

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Open Claim From Pending Activity Query Box", paramsInclude = { "HIC, testType" })
	public void openExistingClaimFromPendingAQB()throws Exception{

		if(!claims.openExistingClaimFromPendingAQB(HIC)){
			report.report("Failed to open claim from pending AQB", Reporter.FAIL);
		}else{
			report.report("Succesfully opened claim from pending AQB", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method add or remove claim lines in existing claim
	 * @throws Exception
	 */

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Add or Remove Claim Lines in Existing Claim", paramsInclude = { "claimLineEntries, claimLineNumberToDelete, claimLineNumberToAdd, newClaimLineEntry, testType" })
	public void addOrRemoveClaimLinesInExistingClaim()throws Exception{

		if(!claims.addOrRemoveClaimLinesInExistingClaim(claimLineEntries, claimLineNumberToDelete, claimLineNumberToAdd, newClaimLineEntry)){
			report.report("Failed to add or remove claim line(s) in existing claim!!!", Reporter.FAIL);
		}else{
			report.report("Succesfully added or removed claim line(s) in existing claim!!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to verify drop down options in edit claim line
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Edit Claim Line Option Availability", paramsInclude = { "claimLineNumberToEdit, expectedOutput, testType" })
	public void verifyEditClaimLineOptionAvailability()throws Exception{

		if(!claims.verifyEditClaimLineOptionAvailability(claimLineNumberToEdit, expectedOutput)){
			report.report("Failed to verify edit claim line in dropdown option !!!", Reporter.FAIL);
		}else{
			report.report("Successfully verified edit claim line in dropdown option !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to open a claim record from advance search page
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Open Claim Record From Advance Search Page", paramsInclude = { "AttributeNameValueDialogProvider, patientControlNumber, testType" })
	public void openClaimRecordFromAdvanceSearchPage()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		keepAsGloablParameter("patientcontrolnumber", patientControlNumber);
		if(!claims.openClaimRecordFromAdvanceSearchPage(mapAttrValues, patientControlNumber)){
			report.report("Failed to verify edit claim line dropdown options !!!", Reporter.FAIL);
		}else{
			report.report("Successfully verified edit claim line dropdown options !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to verify help text of UB04 form fields
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Help Text Of UB04 Form Fields", paramsInclude = { "testType" })
	public void verifyHelpTextInUB04Form()throws Exception{

		if(!claims.verifyHelpTextInUB04Form()){
			report.report("Failed to verify help text for Ub04 fields !!!", Reporter.FAIL);
		}else{
			report.report("Successfully verified help text for UB04 fields !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to verify individual claim line charges with total
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ${coveredOrNonCovered} Total Charges With Individual Claim Line Charges", paramsInclude = { "coveredOrNonCovered, testType" })
	public void verifyTotChargesWithIndividualCharges()throws Exception{

		if(!claims.verifyTotChargesWithIndividualCharges(coveredOrNonCovered)){
			report.report("Sum of individual charges is not equal to grand total !!!", Reporter.FAIL);
		}else{
			report.report("Sum of individual charges is equal to grand total !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to verify claims content are not swapped between related claims
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Check Content In Related Claims", paramsInclude = { "coveredOrNonCovered, testType" })
	public void checkContentInRelatedClaims()throws Exception{

		if(!claims.checkContentInRelatedClaims(coveredOrNonCovered)){
			report.report("Claim Contents seems to swapped between the related claims !!!", Reporter.FAIL);
		}else{
			report.report("Claim Contents are not swapped !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to click Resubmit button
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Unlock Claim Form With ${unlockOption} Option", paramsInclude = { "unlockOption, testType" })
	public void unlockClaim()throws Exception{

		report.report("Inside unlock claim form method...");
		if(!claims.unlockClaim(unlockOption)){
			report.report("Failed to unlock claim form with " + unlockOption + " !!!", Reporter.FAIL);
		}else{
			report.report("Unlocked claim form with " + undoOption + " option successfully !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to add new claim line
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Add New Claim Line", paramsInclude = { "newClaimLineEntry, claimLinePosition, testType" })
	public void addClaimNewLine()throws Exception{

		report.report("Ïnside add new claim line method...");
		claims.addClaimNewLine(newClaimLineEntry, claimLinePosition);
		globalParamMap.put("newclaimlinedetails", newClaimLineEntry);
	}


	/**
	 * Use this method to verify get claim totals and set in global parameters
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Get Claim Total Charges ", paramsInclude = { "coveredOrNonCovered, testType" })
	public void getClaimsTotalCharges()throws Exception{

		report.report("Ïnside get claim total charges method...");
		if( coveredOrNonCovered.equalsIgnoreCase("COVERED")){
			claimTotalChargesCovered = claims.getClaimsTotalCharges(coveredOrNonCovered);
			if(claimTotalChargesCovered > 0){
				report.report("Claim total charges for covered items are : " + claimTotalChargesCovered);
				globalParamMap.put("claimtotalchargescovered", String.valueOf(claimTotalChargesCovered));
			}
		}else if( coveredOrNonCovered.equalsIgnoreCase("NON-COVERED")){
			claimTotalChargesNonCovered = claims.getClaimsTotalCharges(coveredOrNonCovered);
			if(claimTotalChargesNonCovered > 0){
				report.report("Claim total charges for non-covered items are : " + claimTotalChargesNonCovered);
				globalParamMap.put("claimtotalchargesnoncovered", String.valueOf(claimTotalChargesNonCovered));
			}
		}
	}

	/**
	 * Use this method to tally claim charges after add / remove claim lines
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Tally Claim Total Charges After Add / Remove Claim Lines", paramsInclude = { "coveredOrNonCovered, testType" })
	public void tallyClaimChargesAfterAdjustments()throws Exception{

		report.report("Ïnside tally claim charges after adjustments method...");
		String claimLineDetails = globalParamMap.get("newclaimlinedetails");
		if( coveredOrNonCovered.equalsIgnoreCase("COVERED")){
			previousClaimTotals = Float.valueOf(globalParamMap.get("claimtotalchargescovered"));

		}else if( coveredOrNonCovered.equalsIgnoreCase("NON-COVERED")){
			previousClaimTotals = Float.valueOf(globalParamMap.get("claimtotalchargesnoncovered"));
		}

		if(! claims.tallyClaimChargesAfterAdjustments(coveredOrNonCovered,previousClaimTotals, 
				claimLineDetails)){
			report.report("Claim totals are not tallied after correction  !!!", Reporter.FAIL);
		}else{
			report.report("Claim totals are tallied successfully after correction !!!", Reporter.ReportAttribute.BOLD);
		}

	}

	/**
	 * Use this method to perform undo all changes to claim
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Undo Changes", paramsInclude = { "undoOption, testType" })
	public void undoChanges()throws Exception{

		report.report("Ïnside undo changs method...");
		if(!claims.undoChanges(undoOption)){
			report.report("Faile To Undo " + undoOption , Reporter.FAIL);
		}else{
			report.report("Successfully Undo " + undoOption , Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to verify patient information from claims page
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Patient Information From Claims Page", paramsInclude = { "patientControlNumber, testType" })
	public void verifyPatientInformation()throws Exception{

		report.report("Inside verify patient information method...");

		//patientControlNumber = globalParamMap.get("patientcontrolnumber");
		if(!claims.verifyPatientInformation(patientControlNumber)){
			report.report("Failed to verify patient information from claims page for HIC : " + 
					patientControlNumber , Reporter.FAIL);
		}else{
			report.report("Successfully verified patient information from claims page for HIC : " +
					patientControlNumber , Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to open new claim form
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Open New UB04 Form", paramsInclude = { "testType" })
	public void openNewUB04Form()throws Exception{

		report.report("Inside open new UB04 form method...");

		if(!claims.openNewUB04Form()){
			report.report("Failed to open new UB04 form !!!" , Reporter.FAIL);
		}else{
			report.report("Successfully opened new claim form !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to open new claim form
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Fillup Fields In UB04 Form and Validate XML File", paramsInclude = { "AttributeNameValueDialogProvider, startTime, endTime, testType" })
	public void fillUB04FormAndValidateXMLFile()throws Exception{

		report.report("Inside fill up fields in ub04 form and validate xml file method...");

		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		globalParamMap.putAll(mapAttrValues);
		if(!claims.fillUB04FormAndValidateXMLFile(mapAttrValues)){
			report.report("Failed to fill values in UB04 form and validate XML file !!!" , Reporter.FAIL);
		}else{
			report.report("Successfully filled values in the claim form and also validated the XML file !!!", Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to validate 0001 rev code 
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Validation For 0001 Rev Code", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void verifyValidationFor0001RevCode()throws Exception{

		report.report("Inside verify validation for rev code 0001 method...");

		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!claims.verifyValidationFor0001RevCode(mapAttrValues)){
			report.report("Failed to verify 0001 rev code validation!!!" , Reporter.FAIL);
		}else{
			report.report("Successfully verified 0001 rev code validation !!!", Reporter.ReportAttribute.BOLD);
		}
	}


	/**
	 * Use this method to open new claim form from patient information page 
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Open UB04 Form From Patient Info Page", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void openUB04FormWithPatientDetails()throws Exception{
		report.report("Inside open UB04 form with patient details method");
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!claims.openUB04FormWithPatientDetails(mapAttrValues)){
			report.report("Failed to open UB04 form with the patient details from patient information page!!!" , Reporter.FAIL);
		}else{
			report.report("Successfully opened UB04 form with the patient details from patient information page!!!", 
					Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to navigate to patient information page from advanced search results page 
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Navigate To Patient Info Page From Advanced Search Page", 
	paramsInclude = { "AttributeNameValueDialogProvider, patientControlNumber, testType" })
	public void navigateToPatientInfoPageFromAdvancedSearchPage()throws Exception{
		report.report("Inside navigate to patient info page from advanceed search page method");
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		String hic = mapAttrValues.get("HIC");
		if(!claims.navigateToPatientInfoPageFromAdvancedSearchPage(mapAttrValues, patientControlNumber)){
			report.report("Failed to open UB04 form with the patient details from patient information page for the hic : " + hic, 
					Reporter.FAIL);
		}else{
			report.report("Successfully opened UB04 form with the patient details from patient information page for the hic : " + hic , 
					Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Use this method to come back to EASE home / landing page 
	 * @throws Exception
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Come Back To EASE Home Page", paramsInclude = { "testType" })
	public void comeBackToHomePage()throws Exception{

		report.report("Inside come back to home page method");
		if(!claims.comeBackToHomePage()){
			report.report("Failed to come back to EASE home / landing page!!!",Reporter.FAIL);
		}else{
			report.report("Successfully navigated back to EASE home page!!!",Reporter.ReportAttribute.BOLD);
		}
	}

	/**
	 * Handle UI event method
	 */
	@Override
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		if( methodName.equalsIgnoreCase("openClaimRecordFromAdvanceSearchPage") || 
				methodName.equalsIgnoreCase("navigateToPatientInfoPageFromAdvancedSearchPage")){
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+
					"uiattributesxml\\MyDDE\\AdvancedSearch.xml");
		}else if(methodName.equalsIgnoreCase("openUB04FormWithPatientDetails")){
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+
					"uiattributesxml\\Claims\\PatientInfoPage.xml");
		}else{	
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+
					"uiattributesxml\\Claims\\UB04.xml");
		}
	}

	/*###
	##Getters and setters
	###*/

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

	public String getClaimLineNumberToDelete() {
		return claimLineNumberToDelete;
	}

	@ParameterProperties(description = "Provide claim line number to delete from the existing claim lines")
	public void setClaimLineNumberToDelete(String claimLineNumberToDelete) {
		this.claimLineNumberToDelete = claimLineNumberToDelete;
	}

	public String getClaimLineNumberToAdd() {
		return claimLineNumberToAdd;
	}

	@ParameterProperties(description = "Provide claim line number position and before or after to add in the existing claim lines ex., {2,before}")
	public void setClaimLineNumberToAdd(String claimLineNumberToAdd) {
		this.claimLineNumberToAdd = claimLineNumberToAdd;
	}

	public String getNewClaimLineEntry() {
		return newClaimLineEntry;
	}

	@ParameterProperties(description = "Provide new claim entry you want to add in the position provided above")
	public void setNewClaimLineEntry(String newClaimLineEntry) {
		this.newClaimLineEntry = newClaimLineEntry;
	}

	public ClaimStatusType getClaimStatus() {
		return claimStatus;
	}

	@ParameterProperties(description = "Select claims status type from dropdown")
	public void setClaimStatus(ClaimStatusType claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getPatientControlNumber() {
		return patientControlNumber;
	}

	@ParameterProperties(description = "Provide PCN (patient control number) of claim record to select")
	public void setPatientControlNumber(String patientControlNumber) {
		this.patientControlNumber = patientControlNumber;
	}

	public String getClaimLineNumberToEdit() {
		return claimLineNumberToEdit;
	}

	@ParameterProperties(description = "Provide claim line number to edit from the existing claim lines")
	public void setClaimLineNumberToEdit(String claimLineNumberToEdit) {
		this.claimLineNumberToEdit = claimLineNumberToEdit;
	}

	public String getHIC() {
		return HIC;
	}

	@ParameterProperties(description = "Provide HIC Id of the claim request that should be picked up from AQB")
	public void setHIC(String hIC) {
		HIC = hIC;
	}

	public String getClaimLineEntries() {
		return claimLineEntries;
	}

	@ParameterProperties(description = "Claim Line Entries : Read from Global Parameters")
	public void setClaimLineEntries(String claimLineEntries) {
		this.claimLineEntries = claimLineEntries;
	}

	public String getExpectedOutput() {
		return expectedOutput;
	}

	@ParameterProperties(description = "Please provide expected output")
	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}

	public String getCoveredOrNonCovered() {
		return coveredOrNonCovered;
	}

	@ParameterProperties(description = "Please provide either covered or non-covered")
	public void setCoveredOrNonCovered(String coveredOrNonCovered) {
		this.coveredOrNonCovered = coveredOrNonCovered;
	}

	public String getClaimLinePosition() {
		return claimLinePosition;
	}

	@ParameterProperties(description = "Please provide claim line position to add eg., 2,before or 5,after")
	public void setClaimLinePosition(String claimLinePosition) {
		this.claimLinePosition = claimLinePosition;
	}

	public String getUndoOption() {
		return undoOption;
	}

	@ParameterProperties(description = "Please provide undo option {Undo all changes,Reset claim to original}")
	public void setUndoOption(String undoOption) {
		this.undoOption = undoOption;
	}

	public String getUnlockOption() {
		return unlockOption;
	}

	@ParameterProperties(description = "Please provide unlock option {Resubmit,Adjust,Cancel}")
	public void setUnlockOption(String unlockOption) {
		this.unlockOption = unlockOption;
	}

}
