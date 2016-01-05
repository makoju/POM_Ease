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
}
