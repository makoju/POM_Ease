package com.ability.ease.testapi;

import java.util.Map;

public interface IEligibility {

	/*
	 * Verify the Eligibility check for HETS user with a valid HIC id
	 */
	abstract public boolean submitEligibilityCheck(Map<String, String> mapAttrVal)throws Exception;
	abstract public boolean verifyEligibilityStatus(String firstname, String lastname, String status) throws Exception;
	abstract public boolean navigatetoClaimDetails(String firstname, String lastname, String hic)throws Exception;
	abstract public boolean acknoweldgeEligibility(String firstname, String lastname) throws Exception;
	abstract public int getActivityCount(String status) throws Exception;
	abstract public boolean verifyHETSActivitiesCompletedStatusReport(String hic, String agency, String firstname,String lastname)throws Exception;
	abstract public boolean verifyNavigationToUB04FromPatientInfoScreen(String hic, String agency, String firstname,String lastname)throws Exception;
	abstract public boolean verifyNavigationToClaimInfoFromPatientInfoScreen(String hic, String agency, String firstname,String lastname)throws Exception;
	abstract public boolean verifyOptionsUnderPendingActivityLogScreen() throws Exception;
	abstract public boolean verifyOptionsUnderCompletedActivityLogScreen() throws Exception;
	abstract public boolean verifyOptionsUnderFailedActivityLogScreen() throws Exception;
	abstract public boolean verifyOptionsUnderPatientInformationScreen(String hic, String firstname, String lastname) throws Exception;
	abstract public boolean searchactivitylogByHIC(String status, String hic) throws Exception;
	abstract public boolean verifyView271RespPagehasA7() throws Exception;
	abstract public boolean navigatetoPatientInfoScreen(String firstname, String lastname, String hic) throws Exception;
}

