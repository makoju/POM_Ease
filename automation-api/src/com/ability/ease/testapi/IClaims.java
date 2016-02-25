package com.ability.ease.testapi;

import java.util.Map;

public interface IClaims {
	
	/*
	 * method to verify all the fields in UB04 form
	 */
	public boolean verifyUB04FormFeatures(Map<String, String> mapAttrVal)throws Exception;

	/*
	 * method to verify add/remove claim lines in UB04 form
	 */
	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal,String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)throws Exception;
	
	/*
	 * method to select S/Loc in advance search page
	 */
	public boolean selectStatusLocationInAdvanceSearchPageAndSearch(String statusLocationToSelect)throws Exception;	
	/*
	 * method to select claim record from search result page based on PCN
	 */
	public boolean selectClaimRecordFromSearchResults(String patientControlNumber)throws Exception;
	
	/*
	 * method to select claim record from search result page based on PCN
	 */
	public boolean verifyDataInEditClaimLinePopUpWindow(Map<String, String> mapAttrValues,String claimLineNumberToEdit)throws Exception;

	//Medium prioirty test cases
	/*
	 * method to verify help text in UB04 form fields
	 */
	public boolean verifyHelpTextInUB04Form()throws Exception; 
}
