package com.ability.ease.testapi;

import java.util.Map;

public interface IEligibility {

	/*
	 * Verify the Eligibility check for HETS user with a valid HIC id
	 */
	abstract public boolean submitEligibilityCheck(Map<String, String> mapAttrVal)throws Exception;
	abstract public boolean verifyEligibility(String hic, String agency, String firstname,String lastname)throws Exception;
	abstract public boolean verifyHETSActivitiesCompletedStatusReport(String hic, String agency, String firstname,String lastname)throws Exception;
	abstract public boolean verifyNavigationToUB04FromPatientInfoScreen(String hic, String agency, String firstname,String lastname)throws Exception;
	abstract public boolean verifyNavigationToClaimInfoFromPatientInfoScreen(String hic, String agency, String firstname,String lastname)throws Exception;
}
