package com.ability.ease.mydde.reports;

import java.util.Map;

import com.ability.ease.testapi.IReports;

public class ReportsSelenium2Impl implements IReports{

	private MyDDEReportsPage mydde = new MyDDEReportsPage();

	@Override
	public boolean verifySummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception{

		return mydde.verifySummaryReportHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifySummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifySummaryReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyChangesReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyChangesReportSortHeaderHelp(mapAttrValues);
	}
	@Override
	public boolean verifyChangesReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyChangesReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyChangesReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyChangesReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyHighLevelPaymentSummaryReportSortHeader(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHighLevelPaymentSummaryReportSortHeader(mapAttrValues);
	}

	@Override
	public boolean verifyHighLevelPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHighLevelPaymentSummaryReportExportPDFExcel(mapAttrValues);
	}
	@Override
	public boolean verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(
			Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(mapAttrValues);
	}
	@Override
	public boolean verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(
			Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(mapAttrValues);
	}

	@Override
	public boolean verifyPaymentSummaryReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPaymentSummaryReportSortHeaderHelp(mapAttrValues);
	}

	@Override
	public boolean verifyPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPaymentSummaryReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyPaymentSummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPaymentSummaryReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyPaymentReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPaymentReportSortHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyPaymentReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPaymentReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyPaymentReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPaymentReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifySubmittedClaimsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifySubmittedClaimsHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifySubmittedClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifySubmittedClaimsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyUnpaidClaimsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyUnpaidClaimsHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyUnpaidClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyUnpaidClaimsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyActiveEpisodesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyActiveEpisodesHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyActiveEpisodesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyActiveEpisodesExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyEpisodesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyEpisodesHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyEpisodesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyEpisodesExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyClaimsReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyClaimsReportHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyClaimsReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyClaimsReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyStuckInSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyStuckInSuspenseHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyStuckInSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyStuckInSuspenseExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyRAPSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPSuspenseHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyRAPSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPSuspenseExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyRAPPaidHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPPaidHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyRAPPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPPaidExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyRAPErrorHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPErrorHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyRAPErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPErrorExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyRAPCancelledHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPCancelledHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyRAPCancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPCancelledExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyFinalSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalSuspenseHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyFinalSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalSuspenseExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyFinalPaidHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalPaidHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyFinalPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalPaidExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyFinalErrorHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalErrorHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyFinalErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalErrorExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyFinalCancelledHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalCancelledHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyFinalCancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalCancelledExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyFinalDueHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalDueHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyFinalDueExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyFinalDueExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyZ_OnHoldHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyZ_OnHoldHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyZ_OnHoldExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyZ_OnHoldExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyRAPsAtRiskHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPsAtRiskHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyRAPsAtRiskExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyRAPsAtRiskExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyADRHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyADRHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyADRExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyADRExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyADRLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyADRLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyEligibilityIssuesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyEligibilityIssuesHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyEligibilityIssuesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyEligibilityIssuesExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyPatientsReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPatientsReportHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyPatientsReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyPatientsReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyEligibilityErrorsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyEligibilityErrorsHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyEligibilityErrorsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyEligibilityErrorsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyHMOPatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHMOPatientsHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyHMOPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHMOPatientsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyMSPPatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyMSPPatientsHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyMSPPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyMSPPatientsExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyOtherHHA1stHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOtherHHA1stHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyOtherHHA1stExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOtherHHA1stExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyOtherHHA2ndHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOtherHHA2ndHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyOtherHHA2ndExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOtherHHA2ndExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyHospicePatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHospicePatientsHeaderandHelpText(mapAttrValues);
	}

	@Override
	public boolean verifyHospicePatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyHospicePatientsExportPDFExcel(mapAttrValues);
	}

	@Override
	public void clickAdvancedTab() throws Exception {
		mydde.clickAdvanced();
	}
}
