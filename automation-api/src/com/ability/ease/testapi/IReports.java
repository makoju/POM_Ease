package com.ability.ease.testapi;

import java.util.Map;

public interface IReports {
	boolean verifySummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception;
	boolean verifySummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyChangesReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception;
}
