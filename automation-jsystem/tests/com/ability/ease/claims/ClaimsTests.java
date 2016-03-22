package com.ability.ease.claims;

import java.util.HashMap;
import java.util.Map;

import jsystem.extensions.report.html.Report;
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
	@TestProperties(name = "Verify Add Remove Claim Lines", paramsInclude = { "AttributeNameValueDialogProvider, claimLineNumberToDelete, claimLineNumberToAdd, newClaimLineEntry, testType" })
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
		keepAsGloablParameter("HIC", HIC);
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


	/*
	 * Handle UI event method
	 */
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml");
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
	
	
}
