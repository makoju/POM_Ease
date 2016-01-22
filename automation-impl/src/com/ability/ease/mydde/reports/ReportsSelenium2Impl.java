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
	public boolean verifyChangesReportSortHeaderHelp(
			Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyChangesReportSortHeaderHelp(mapAttrValues);
	}
	@Override
	public boolean verifyChangesReportExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyChangesReportExportPDFExcel(mapAttrValues);
	}

	@Override
	public boolean verifyChangesReportLastUpdateColumn(
			Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyChangesReportLastUpdateColumn(mapAttrValues);
	}

	@Override
	public boolean verifyHighLevelPaymentSummaryReportSortHeader(
			Map<String, String> mapAttrValues) throws Exception {
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
}
