package com.ability.ease.mydde.reports;

import java.util.Map;

import com.ability.ease.testapi.INonHHAReports;

public class NonHHAReportsSeleniumImpl implements INonHHAReports{
	
	private MyDDENonHHAReportsPage nonHHA = new MyDDENonHHAReportsPage();

	@Override
	public boolean verifyNonHHASummaryReportSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASummaryReportHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHASummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASummaryReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHASummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASummaryReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaymentSummarySortReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaymentSummaryReportHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaymentSummaryReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaymentSummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaymentSummaryReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaymentReportSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaymentReportHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaymentReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaymentReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaymentReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaymentReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHASubmittedClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASubmittedClaimsSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHASubmittedClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASubmittedClaimsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAUnPaidClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAUnPaidClaimsSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAUnPaidClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAUnPaidClaimsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return false;
	}

	@Override
	public boolean verifyNonHHAClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return false;
	}

	@Override
	public boolean verifyNonHHAStuckInSuspenseSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAStuckInSuspenseSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAStuckInSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAStuckInSuspenseExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAOnHoldSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAOnHoldSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAOnHoldExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAOnHoldExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAADRSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAADRSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAADRExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAADRExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAADRLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAADRLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAEligibilityIssuesSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAEligibilityIssuesSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAEligibilityIssuesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAEligibilityIssuesExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPatientsSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPatientsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAEligibilityErrorsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAEligibilityErrorsSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAEligibilityErrorsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAEligibilityErrorsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAMSPPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAMSPPatientsSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAMSPPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAMSPPatientsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAHHAPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPatientsSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAHHAPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAHHAPatientsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAOverlappingHospiceSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAOverlappingHospiceSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAOverlappingHospiceExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAOverlappingHospiceExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHASuspenseSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASuspenseSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHASuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHASuspenseExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAErrorSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAErrorSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAErrorExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHACancelledSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHACancelledSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHACancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHACancelledExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaidSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaidSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyNonHHAPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return nonHHA.verifyNonHHAPaidExportPDFExcel(mapAttrValues);
	}

}
