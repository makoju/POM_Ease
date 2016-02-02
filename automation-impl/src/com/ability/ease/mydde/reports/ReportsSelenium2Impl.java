package com.ability.ease.mydde.reports;

import java.util.Map;

import com.ability.ease.misc.MiscPage;
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
	public boolean verifyHighLevelPaymentSummaryReportExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
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
//		return mydde.verifyClaimsReportHeaderandHelpText(mapAttrValues);
		return false;
	}

	@Override
	public boolean verifyClaimsReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
//		return mydde.verifyClaimsReportExportPDFExcel(mapAttrValues);
		return false;
	}

	@Override
	public boolean verifyStuckInSuspenseHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyStuckInSuspenseExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPSuspenseHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPSuspenseExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPPaidHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPPaidExportPDFExcel(Map<String, String> mapAttrValues)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPErrorHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPErrorExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPCancelledHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyRAPCancelledExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalSuspenseHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalSuspenseExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalPaidHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalPaidExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalErrorHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalErrorExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalCancelledHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalCancelledExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalDueHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyFinalDueExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyZ_OnHoldHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyZ_OnHoldExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
