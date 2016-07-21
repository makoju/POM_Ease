package com.ability.ease.mydde;

import java.util.Map;

import com.ability.ease.auto.enums.tests.SelectTimeframe;
import com.ability.ease.auto.enums.tests.Status;
import com.ability.ease.testapi.IMyDDE;

public class MyDDESelenium2Impl implements IMyDDE{
	
	private MyDDEPage mydde = new MyDDEPage();

	@Override
	public boolean verifyPageViewAndOptionsUnderBasicView() throws Exception {
		return mydde.verifyPageViewAndOptionsUnderBasicView();
	}
	
	@Override
	public boolean verifyColumnsAndSorting(String report, String expectedColumns, String columnName, int columnIndex) throws Exception {
		return mydde.verifyColumnsAndSorting(report, expectedColumns, columnName, columnIndex);
	}

	@Override
	public boolean verifyOptionsUnderReportsForHHA_NonHHA(String fromDate,String toDate,String expectedReports,String agency, String expectedColumns) throws Exception {
		return mydde.verifyOptionsUnderReportsForHHA_NonHHA(fromDate, toDate, expectedReports, agency, expectedColumns);
	}

	@Override
	public boolean verifyOptionsUnderTimeframe(String fromDate, String toDate) throws Exception {
		return mydde.verifyOptionsUnderTimeframe(fromDate,toDate);
	}

	@Override
	public boolean verifyOptionsUnderAgency(String agency) throws Exception {
		return mydde.verifyOptionsUnderAgency(agency);
	}

	@Override
	public boolean verifySaveProfileOption(String agency, String saveProfileNameAS) throws Exception {
		return mydde.verifySaveProfileOption(agency,saveProfileNameAS);
	}

	@Override
	public boolean verifyTimeFrameOptionsUnderAdvanced(String fromDate, String toDate, String expTimFrameOptions) throws Exception {
		return mydde.verifyTimeFrameOptionsUnderAdvanced(fromDate,toDate, expTimFrameOptions);
	}

	@Override
	public boolean verifyAdvancedSearchForMAXFields(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyAdvancedSearchForMAXFields(mapAttrValues);
	}
	
	@Override
	public boolean verifyAdvancedSearchForStatusLocation() throws Exception {
		return mydde.verifyAdvancedSearchForStatusLocation();
	}

	@Override
	public boolean verifyOptionUB04Form() throws Exception {
		return mydde.verifyOptionUB04Form();
	}

	/*@Override
	public boolean verifyPieAndPrintOptions() throws Exception {
		return false;
	}*/

	@Override
	public boolean verifyAdvancedOption() throws Exception {
		return mydde.verifyAdvancedOption();
	}

	@Override
	public boolean verifyLiveSearchOption(String hic) throws Exception {
		return mydde.verifyLiveSearchOption(hic);
	}

	@Override
	public boolean verifyOptionsUnderAdvancedView() throws Exception {
		return mydde.verifyOptionsUnderAdvancedView();
	}

	@Override
	public boolean verifyOptionsUnderAdvancedSearch(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOptionsUnderAdvancedSearch(mapAttrValues);
	}
	
	@Override
	public boolean verifySaveProfileUnderAdvancedSearch(String agency, String monthsAgo, String saveProfileNameAS) throws Exception {
		return mydde.verifySaveProfileUnderAdvancedSearch(agency, monthsAgo, saveProfileNameAS);
	}

	@Override
	public boolean verifyColumnHeadersInSearchResults(String expectedColumns) throws Exception {
		return mydde.verifyColumnHeadersInSearchResults(expectedColumns);
	}

	@Override
	public boolean verifyTotalsForAdvancedSearchResult(String betweenDate, String andDate) throws Exception {
		return mydde.verifyTotalsForAdvancedSearchResult(betweenDate, andDate);
	}
	
	@Override
	public boolean verifyFiltersInAdvancedSearchResults(String monthsAgo, String agency, String hic, String tob) throws Exception {
		return mydde.verifyFiltersInAdvancedSearchResults(monthsAgo, agency, hic, tob);
	}
	
	@Override
	public boolean verifySaveProfileOptionWithDuplicateName(String agency, String monthsAgo, String saveProfileNameAS) throws Exception {
		return mydde.verifySaveProfileOptionWithDuplicateName(agency, monthsAgo, saveProfileNameAS);
	}

	@Override
	public boolean verifyAdvancedSearchForCustomDates(String agency, String betweenDate, String andDate) throws Exception {
		return mydde.verifyAdvancedSearchForCustomDates(agency,betweenDate,andDate);
	}

	@Override
	public boolean verifyAdvancedSearchForGivenMonths(String agencyFromJSystem,String monthsAgo)	throws Exception {
		return mydde.verifyAdvancedSearchForGivenMonths(agencyFromJSystem,monthsAgo);
	}

	@Override
	public boolean verifyAdvancedSearchForGivenMonthsAndDateRange(String agencyFromJSystem, String betweenDate, String andDate, String monthsAgo) throws Exception {
		return mydde.verifyAdvancedSearchForGivenMonthsAndDateRange(agencyFromJSystem,betweenDate,andDate,monthsAgo);
	}

	@Override
	public boolean searchByHICAndNavigatetoPatientInfoScreen(String hic) throws Exception {
		return mydde.searchByHICAndNavigatetoPatientInfoScreen(hic);
	}

}
