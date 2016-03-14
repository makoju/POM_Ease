package com.ability.ease.testapi;

import java.util.Map;

import com.ability.ease.auto.enums.tests.SelectTimeframe;
import com.ability.ease.auto.enums.tests.Status;


public interface IMyDDE {

	boolean verifyPageViewAndOptions() throws Exception;
	boolean verifyOptionsUnderReportsForHHA_NonHHA(String fromDate,String toDate, String expectedReports, String agency, String expectedColumns) throws Exception;
	boolean verifyColumnsAndSorting(String report, String expectedColumns, String columnName, int columnIndex) throws Exception;
	boolean verifyOptionsUnderTimeframe(String fromDate, String toDate) throws Exception;
	boolean verifyOptionsUnderAgency(String agency) throws Exception;
	boolean verifySaveProfileOption(String agency, String saveProfileNameAS) throws Exception;
	boolean verifyTimeFrameOptionsUnderAdvanced(String fromDate, String toDate, String expTimFrameOptions) throws Exception;
	boolean verifyAdvancedSearchForMAXFields(Map<String, String> mapAttrValues) throws Exception;
	boolean verifyAdvancedSearchForStatusLocation() throws Exception;

	//boolean verifyPieAndPrintOptions() throws Exception;
	boolean verifyOptionUB04Form() throws Exception;
	boolean verifyAdvancedOption() throws Exception;
	boolean verifyLiveSearchOption(String hic) throws Exception;
	boolean verifyOptionsUnderAdvancedView() throws Exception;
	boolean verifyOptionsUnderAdvancedSearch(Map<String, String> mapAttrValues) throws Exception;
	//boolean verifySaveProfileUnderAdvancedSearch() throws Exception;
	//boolean verifySavedProfileData() throws Exception;
	boolean verifyColumnHeadersInSearchResults(String expectedColumns) throws Exception;
	//boolean verifyTotalsForAdvancedSearchResult(String savedProfile) throws Exception;
	//boolean verifyFiltersInAdvancedSearchResults() throws Exception;
	boolean verifySaveProfileOptionWithDuplicateName(String agency, String monthsAgo, String saveProfileNameAS) throws Exception;
	//boolean verifySaveSummaryReportToPDF() throws Exception;
	//boolean verifySaveSummaryReportToExcel() throws Exception;
	//boolean verifyCompleteReportToPDF() throws Exception;
	//boolean verifyCompleteReportToExcel() throws Exception;
	//boolean verifyHomeUnderMyDDE() throws Exception;
	boolean verifyAdvancedSearchForCustomDates(String agency, String betweenDate, String andDate) throws Exception;
	boolean verifyAdvancedSearchForGivenMonths(String agencyFromJSystem,String monthsAgo) throws Exception;
	boolean verifyAdvancedSearchForGivenMonthsAndDateRange(String agencyFromJSystem,String betweenDate, String andDate,String monthsAgo) throws Exception;



}
