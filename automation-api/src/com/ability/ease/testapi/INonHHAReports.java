package com.ability.ease.testapi;

import java.util.Map;

public interface INonHHAReports {

	boolean verifyNonHHASummaryReportSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHASummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHASummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAPaymentSummarySortReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAPaymentSummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAPaymentReportSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAPaymentReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAPaymentReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHASubmittedClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHASubmittedClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAUnPaidClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAUnPaidClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAStuckInSuspenseSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAStuckInSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAOnHoldSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAOnHoldExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAADRSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAADRExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAADRLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAEligibilityIssuesSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAEligibilityIssuesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAEligibilityErrorsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAEligibilityErrorsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAMSPPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAMSPPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAHHAPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAHHAPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAOverlappingHospiceSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAOverlappingHospiceExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHASuspenseSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHASuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAErrorSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHACancelledSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHACancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyNonHHAPaidSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyNonHHAPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
}
