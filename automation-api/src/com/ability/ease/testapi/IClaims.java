package com.ability.ease.testapi;

import java.util.Map;

import com.ability.ease.auto.enums.tests.NewUB04;

public interface IClaims {

	public boolean verifyUB04FormFeatures(Map<String, String> mapAttrVal)throws Exception;

	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal,String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)throws Exception;

	public boolean selectStatusLocationInAdvanceSearchPageAndSearch(String statusLocationToSelect)throws Exception;	

	public boolean selectClaimRecordFromSearchResults(String patientControlNumber)throws Exception;

	public boolean verifyDataInEditClaimLinePopUpWindow(Map<String, String> mapAttrValues,String claimLineNumberToEdit)throws Exception;

	public boolean openExistingClaimFromPendingAQB(String HIC)throws Exception;

	public boolean fillUB04FormValuesOnly(Map<String, String> mapAttrValues)throws Exception;

	public boolean getCountFromPendingActivity()throws Exception;

	public boolean addOrRemoveClaimLinesInExistingClaim(String claimLineEntries,
			String claimLineNumberToDelete,	String claimLineNumberToAdd, String newClaimLineEntry)throws Exception;

	public boolean verifyHelpTextInUB04Form()throws Exception;

	public boolean verifyEditClaimLineOptionAvailability(String patientControlNumberHIC, 
			String claimLineToEdit)throws Exception;

	public boolean openClaimRecordFromAdvanceSearchPage(Map<String, String> mapAttrVal, 
			String patientControlNumber)throws Exception;

	public boolean verifyDataInClaimLinePopupWindow()throws Exception;

	public boolean verifyTotChargesWithIndividualCharges(String coveredOrNonCovered)throws Exception;

	public boolean checkContentInRelatedClaims(String coveredOrNonCovered) throws Exception;

	public float getClaimsTotalCharges(String coveredOrNonCovered) throws Exception;

	public boolean unlockClaim(String unlockOption)throws Exception;

	public void addClaimNewLine(String claimLineDetails, String claimLinePosition)throws Exception;

	public boolean resubmitAdjustedClaim() throws Exception;

	public boolean tallyClaimChargesAfterAdjustments(String coveredOrNonCovered,float previousClaimTotals, 
			String claimLineDetails) throws Exception;

	public boolean undoChanges(String undoOption)throws Exception;

	public boolean verifyPatientInformation(String patientControlNumber)throws Exception;

	public boolean verifyValidationFor0001RevCode(Map<String, String> mapAttrValues)throws Exception;

	public boolean openNewUB04Form()throws Exception;

	public boolean fillUB04FormAndValidateXMLFile(Map<String, String> mapAttrValues)throws Exception;

	public boolean openUB04FormWithPatientDetails(Map<String, String> mapAttrValues)throws Exception;
	
	public boolean navigateToPatientInfoPageFromAdvancedSearchPage(Map<String, String> mapAttrValues, 
			String patientControlNumber)throws Exception;
	
	public boolean comeBackToHomePage()throws Exception;
	
	public boolean openNewClaimFormFromExistingClaim(NewUB04 relatedORUnRelated)throws Exception;
	
}
