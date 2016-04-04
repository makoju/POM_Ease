package com.ability.ease.claims;

import java.util.Map;

import com.ability.ease.testapi.IClaims;

public class ClaimsSelenium2Impl implements IClaims{

	ClaimsPage claims = new ClaimsPage();
	
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCountFromPendingActivity() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addOrRemoveClaimLinesInExistingClaim(String claimLineEntries,String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)throws Exception {
		return claims.addOrRemoveClaimLinesInExistingClaim(claimLineEntries,claimLineNumberToDelete, claimLineNumberToAdd, newClaimLineEntry);
	}


	
}
