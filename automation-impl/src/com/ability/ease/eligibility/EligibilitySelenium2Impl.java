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

	@Override
	public int getActivityCount(String status) throws Exception {
		return eligPage.getActivityCount(status);
	}

	@Override
	public boolean searchactivitylogByHIC(String status, String hic) throws Exception {
		return eligPage.searchactivitylogByHIC(status,hic);
	}

	@Override
	public boolean verifyOptionsUnderPatientInformationScreen(String hic, String firstname, String lastname) throws Exception {
		return eligPage.verifyOptionsUnderPatientInformationScreen(hic,firstname,lastname);
	}

	@Override
	public boolean verifyView271RespPagehasA7() throws Exception {
		return eligPage.verifyView271RespPagehasA7();
	}

	@Override
	public boolean navigatetoPatientInfoScreen(String firstname, String lastname, String hic)
			throws Exception {
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
		return eligPage.navigatetoPatientInfoScreen(firstlastname, hic);
	}

	@Override
	public boolean verifyActivityLogSearchOnlynotacknowledged()
			throws Exception {
		return eligPage.verifyActivityLogSearchOnlynotacknowledged();
	}

	@Override
	public boolean verifyNavigationToHomeScreenFromCompletedActivityLogScreen()
			throws Exception {
		return eligPage.verifyNavigationToHomeScreenFromCompletedActivityLogScreen();
	}

	@Override
	public boolean verifyPDFExportInCompletedActivityLogScreen()
			throws Exception {
		return eligPage.verifyPDFExportInCompletedActivityLogScreen();
	}

	@Override
	public boolean verifyPrintOptionInCompletedActivityLogScreen()
			throws Exception {
		return eligPage.verifyPrintOptionInCompletedActivityLogScreen();
	}

	@Override
	public boolean verifyTrashOptionInCompletedActivityLogScreen()
			throws Exception {
		return eligPage.verifyTrashOptionInCompletedActivityLogScreen();
	}

	@Override
	public boolean verifyNavigationToHomeScreenFromPatientInfoScreen(String hic)
			throws Exception {
		return eligPage.verifyNavigationToHomeScreenFromPatientInfoScreen(hic);
	}

	@Override
	public boolean VerifyNavigationOfAdvanceSearchFromLiveSearch()
			throws Exception {
		return eligPage.VerifyNavigationOfAdvanceSearchFromLiveSearch();
	}

	@Override
	public boolean navigatetoEligibilityReport(String firstname, String lastname)
			throws Exception {
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
		return eligPage.navigatetoEligibilityReport(firstlastname);
	}

	@Override
	public boolean verifyMostBenefitSTC45Fields() throws Exception {
		return eligPage.verifyMostBenefitSTC45Fields();
	}

	@Override
	public boolean enableordisableHETS(String customername, String status)
			throws Exception {
		return eligPage.enableordisableHETS(customername,status);
	}

	@Override
	public boolean enableandVerifyPsychiatricSTCforNPI(String contactname,
			String agencyDisplayName) throws Exception {
		return eligPage.enableandVerifyPsychiatricSTCforNPI(contactname,agencyDisplayName);
	}

	@Override
	public boolean validateResponsePageAndRawFile(String firstname, String lastname) throws Exception {
		return eligPage.validateResponsePageAndRawFile(firstname, lastname);
	}
	
	@Override
	public boolean insertEligibilityCheckWithCompletedStatus(
			Map<String, String> mapAttrVal) throws Exception {
	
		return eligPage.insertEligibilityCheckWithCompletedStatus(mapAttrVal);
	}
}
