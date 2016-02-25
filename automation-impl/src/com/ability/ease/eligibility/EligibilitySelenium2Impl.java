package com.ability.ease.eligibility;

import java.util.Map;

import com.ability.ease.testapi.IEligibility;

public class EligibilitySelenium2Impl implements IEligibility{
	EligibilityPage eligPage = new EligibilityPage();
	
	@Override
	public boolean submitEligibilityCheck(Map<String, String> mapAttrVal)
			throws Exception {
		return eligPage.submitEligibilityCheck(mapAttrVal);
	}
	
	@Override
	public boolean verifyEligibilityStatus(String firstname, String lastname,String status) throws Exception {
		return eligPage.verifyEligibilityStatus(firstname, lastname, status);
	}

	@Override
	public boolean verifyHETSActivitiesCompletedStatusReport(String hic,String agency, String firstname, String lastname) throws Exception {
		return eligPage.verifyHETSActivitiesCompletedStatusReport(hic, agency,firstname,lastname);
	}

	@Override
	public boolean verifyNavigationToUB04FromPatientInfoScreen(String hic,String agency, String firstname, String lastname) throws Exception {
		return eligPage.verifyNavigationToUB04FromPatientInfoScreen(hic, agency,firstname,lastname);
	}

	@Override
	public boolean verifyNavigationToClaimInfoFromPatientInfoScreen(String hic,String agency, String firstname, String lastname) throws Exception {
		return eligPage.verifyNavigationToClaimInfoFromPatientInfoScreen(hic, agency,firstname,lastname);
	}

	@Override
	public boolean navigatetoClaimDetails(String firstname, String lastname,String hic) throws Exception {
		return eligPage.navigatetoClaimDetails(firstname, lastname, hic);
	}

	@Override
	public boolean acknoweldgeEligibility(String firstname, String lastname) throws Exception {
		return eligPage.acknoweldgeEligibility(firstname, lastname);
	}

	@Override
	public boolean verifyOptionsUnderPendingActivityLogScreen() throws Exception {
		return eligPage.verifyOptionsUnderPendingActivityLogScreen();
	}
	
	@Override
	public boolean verifyOptionsUnderCompletedActivityLogScreen() throws Exception {
		return eligPage.verifyOptionsUnderCompletedActivityLogScreen();
	}

	@Override
	public boolean verifyOptionsUnderFailedActivityLogScreen() throws Exception {
		return eligPage.verifyOptionsUnderFailedActivityLogScreen(); 
	}
}
