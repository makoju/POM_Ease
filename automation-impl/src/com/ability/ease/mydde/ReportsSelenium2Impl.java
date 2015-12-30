package com.ability.ease.mydde;

import java.util.Map;

import com.ability.ease.misc.MiscPage;
import com.ability.ease.testapi.IReports;

public class ReportsSelenium2Impl implements IReports{

	private MyDDEPage mydde = new MyDDEPage();
	
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
   
}
