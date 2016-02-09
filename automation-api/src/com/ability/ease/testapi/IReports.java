package com.ability.ease.testapi;

import java.util.Map;

public interface IReports {
	
	boolean verifySummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifySummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyChangesReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyChangesReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyChangesReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyHighLevelPaymentSummaryReportSortHeader(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyHighLevelPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyPaymentSummaryReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyPaymentSummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;

	boolean verifyPaymentReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyPaymentReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyPaymentReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifySubmittedClaimsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifySubmittedClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyUnpaidClaimsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyUnpaidClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyActiveEpisodesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyActiveEpisodesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyEpisodesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyEpisodesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyClaimsReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyClaimsReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyStuckInSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyStuckInSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyRAPSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyRAPSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyRAPPaidHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyRAPPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyRAPErrorHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyRAPErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyRAPCancelledHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyRAPCancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyFinalSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyFinalSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyFinalPaidHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyFinalPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyFinalErrorHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyFinalErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyFinalCancelledHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyFinalCancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyFinalDueHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyFinalDueExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyZ_OnHoldHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyZ_OnHoldExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyRAPsAtRiskHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyRAPsAtRiskExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyADRHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyADRExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyADRLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyEligibilityIssuesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyEligibilityIssuesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyPatientsReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyPatientsReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyEligibilityErrorsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyEligibilityErrorsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyHMOPatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyHMOPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyMSPPatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyMSPPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyOtherHHA1stHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyOtherHHA1stExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyOtherHHA2ndHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyOtherHHA2ndExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	
	boolean verifyHospicePatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyHospicePatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
}
