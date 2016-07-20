package com.ability.ease.claims;

import java.util.Map;

import com.ability.ease.testapi.IClaims;

public class ClaimsSelenium2Impl implements IClaims{

	ClaimsPage claims = new ClaimsPage();
	ClaimsPageVersion2 claimsv2 = new ClaimsPageVersion2();

	@Override
	public boolean verifyUB04FormFeatures(Map<String, String> mapAttrVal) throws Exception {
		return claims.verifyUB04FormFields(mapAttrVal);
	}

	@Override
	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal, String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)
					throws Exception {
		return claims.verifyAddRemoveClaimLines(mapAttrVal,claimLineNumberToDelete,claimLineNumberToAdd,newClaimLineEntry);
	}

	@Override
	public boolean selectStatusLocationInAdvanceSearchPageAndSearch(
			String statusLocationToSelect) throws Exception {
		return claims.selectStatusLocationInAdvanceSearchPageAndSearch(statusLocationToSelect);
	}

	@Override
	public boolean selectClaimRecordFromSearchResults(String patientControlNumber) throws Exception {
		return claims.selectClaimRecordFromSearchResults(patientControlNumber);
	}

	@Override
	public boolean verifyDataInEditClaimLinePopUpWindow(Map<String, String> mapAttrValues,String claimLineNumberToEdit) throws Exception {
		return claims.verifyDataInEditClaimLinePopUpWindow(mapAttrValues,claimLineNumberToEdit);
	}

	@Override
	public boolean openExistingClaimFromPendingAQB(String HIC) throws Exception {
		return claims.openExistingClaimFromPendingAQB(HIC);
	}


	@Override
	public boolean fillUB04FormValuesOnly(Map<String, String> mapAttrValues)
			throws Exception {
		return claims.fillUB04FormValuesOnly(mapAttrValues);
	}

	@Override
	public boolean verifyHelpTextInUB04Form() throws Exception {
		return claimsv2.verifyHelpTextInUB04Form();
	}

	@Override
	public boolean getCountFromPendingActivity() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addOrRemoveClaimLinesInExistingClaim(String claimLineEntries,String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)throws Exception {
		return claims.addOrRemoveClaimLinesInExistingClaim(claimLineEntries,claimLineNumberToDelete, 
				claimLineNumberToAdd, newClaimLineEntry);
	}


	@Override
	public boolean verifyEditClaimLineOptionAvailability(String claimLineToEdit, String expectedOutput)
			throws Exception {
		return claimsv2.verifyEditClaimLineOptionAvailability(claimLineToEdit, expectedOutput);
	}

	@Override
	public boolean openClaimRecordFromAdvanceSearchPage(
			Map<String, String> mapAttrVal, String patientControlNumber) throws Exception {
		return claimsv2.openClaimRecordFromAdvanceSearchPage(mapAttrVal, patientControlNumber);
	}

	@Override
	public boolean verifyDataInClaimLinePopupWindow() throws Exception {
		return false;
	}

	@Override
	public boolean verifyTotChargesWithIndividualCharges(String coveredOrNonCovered) throws Exception {
		return claimsv2.verifyTotChargesWithIndividualCharges(coveredOrNonCovered);
	}

	@Override
	public boolean checkContentInRelatedClaims(String coveredOrNonCovered) throws Exception {
		return claimsv2.checkContentInRelatedClaims(coveredOrNonCovered);
	}

	@Override
	public float getClaimsTotalCharges(String coveredOrNonCovered)
			throws Exception {
		return claimsv2.getTotalClaimCharges(coveredOrNonCovered);
	}

	@Override
	public boolean unlockClaim(String unlockOption) throws Exception {
		return claimsv2.unlockClaim(unlockOption);
	}

	@Override
	public void addClaimNewLine(String claimLineDetails, String claimLinePosition) throws Exception {
		claimsv2.addClaimNewLine(claimLineDetails, claimLinePosition);
	}

	@Override
	public boolean resubmitAdjustedClaim() throws Exception {
		return false;
	}

	@Override
	public boolean tallyClaimChargesAfterAdjustments(String coveredOrNonCovered,float previousClaimTotals, 
			String claimLineDetails) throws Exception {
		return claimsv2.tallyClaimChargesAfterAdjustments(coveredOrNonCovered, previousClaimTotals, claimLineDetails);
	}

	@Override
	public boolean undoChanges(String undoOption) throws Exception {
		return claimsv2.undoChanges(undoOption);
	}

	@Override
	public boolean verifyPatientInformation(String patientControlNumber) throws Exception {
		return claimsv2.verifyPatientInformation(patientControlNumber);
	}

	@Override
	public boolean verifyValidationFor0001RevCode(Map<String, String> mapAttrValues)throws Exception {
		return claimsv2.verifyValidationFor0001RevCode(mapAttrValues);
	}

	@Override
	public boolean openNewUB04Form() throws Exception {
		return claimsv2.openNewUB04Form();
	}

	@Override
	public boolean fillUB04FormAndValidateXMLFile(Map<String, String> mapAttrValues) throws Exception {
		return claimsv2.fillupFieldsInUB04Form(mapAttrValues);
	}

	@Override
	public boolean openUB04FormWithPatientDetails(Map<String, String> mapAttrValues) throws Exception {
		return claimsv2.openUB04FormWithPatientDetails(mapAttrValues);
	}

	@Override
	public boolean navigateToPatientInfoPageFromAdvancedSearchPage(Map<String, String> mapAttrValues, 
			String patientControlNumber) throws Exception {
		return claimsv2.navigateToPatientInfoPageFromAdvancedSearchPage(mapAttrValues, patientControlNumber);
	}

	@Override
	public boolean comeBackToHomePage() throws Exception {
		return claimsv2.comeBackToHomePage();
	}
}
