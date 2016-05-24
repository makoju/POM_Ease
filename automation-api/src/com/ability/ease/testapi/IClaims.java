package com.ability.ease.testapi;

import java.util.Map;

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

	public boolean addOrRemoveClaimLinesInExistingClaim(String claimLineEntries,String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)throws Exception;

	public boolean verifyHelpTextInUB04Form()throws Exception;

	public boolean verifyEditClaimLineOptions(String patientControlNumberHIC, String claimLineToEdit)throws Exception;

}
