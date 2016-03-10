package com.ability.ease.mydde;

import java.util.HashMap;
import java.util.Map;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.SelectTimeframe;
import com.ability.ease.auto.enums.tests.Status;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IMyDDE;

public class MyDDETests extends BaseTest{

	private IMyDDE mydde;
	private SelectTimeframe timeframe;
	private String expectedReportsOptions;
	private String expectedColumns;
	private String expTimFrameOptions;
	private String agency;
	private String hic;
	private String monthsAgo;
	private Status status;
	private String location;
	private String fromDate,toDate;
	private String saveProfileNameAS;
	private String description;
	private String reportText;
	private String columnNameToSort;
	private int columnIndex;
	private String savedProfile;
	private String betweenDateAS;
	private String andDateAS;

	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		mydde = (IMyDDE)context.getBean("mydde");

	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\AdvancedSearch.xml");
		map.get("FromDate").setEditable(false);
		map.get("ToDate").setEditable(false);
		if(map.get("Timeframe").getStringValue().equalsIgnoreCase(SelectTimeframe.Date.toString()))
		{
			map.get("FromDate").setEditable(true);
			map.get("ToDate").setEditable(true);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the page view", paramsInclude = { "testType" })
	public void verifyPageViewAndOptions()throws Exception{
		if(mydde.verifyPageViewAndOptions()){
			report.report("Successfully Verified Page view and Options under it.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Page view and Options under it.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify columns and sorting for ${description}", paramsInclude = { "description,reportText,expectedColumns,columnNameToSort,columnIndex,testType" })
	public void verifyColumnsAndSorting()throws Exception{
		if(mydde.verifyColumnsAndSorting(reportText,expectedColumns,columnNameToSort,columnIndex)){
			report.report("Successfully Verified Options under Reports for HHA Agencies.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Reports for HHA Agencies.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Reports for HHA/Non-HHA Agencies", paramsInclude = { "timeframe,fromDate,toDate,expectedReportsOptions,agency,expectedColumns,testType" })
	public void verifyOptionsUnderReportsForHHA_NonHHA()throws Exception{
		if(mydde.verifyOptionsUnderReportsForHHA_NonHHA(fromDate,toDate,expectedReportsOptions,agency,expectedColumns)){
			report.report("Successfully Verified Options under Reports for HHA/Non-HHA Agencies.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Reports for HHA/Non-HHA Agencies.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Timeframe ", paramsInclude = { "timeframe,fromDate,toDate,testType" })
	public void verifyOptionsUnderTimeframe()throws Exception{
		if(mydde.verifyOptionsUnderTimeframe(fromDate,toDate)){
			report.report("Successfully Verified Options under Timeframe.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Timeframe.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Agency ", paramsInclude = { "agency,testType" })
	public void verifyOptionsUnderAgency()throws Exception{
		if(mydde.verifyOptionsUnderAgency(agency)){
			report.report("Successfully Verified Options under Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Agency.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Save Profile option under Advance Search", paramsInclude = { "agency,saveProfileNameAS,testType" })
	public void verifySaveProfileOption()throws Exception{
		if(mydde.verifySaveProfileOption(agency,saveProfileNameAS)){
			report.report("Successfully Verified Options under Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Agency.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify TimeFrame Options Under Advanced", paramsInclude = { "timeframe,fromDate,toDate,expTimFrameOptions,testType" })
	public void verifyTimeFrameOptionsUnderAdvanced()throws Exception{
		if(mydde.verifyTimeFrameOptionsUnderAdvanced(fromDate, toDate,expTimFrameOptions)){
			report.report("Successfully Verified TimeFrame Options Under Advanced.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify TimeFrame Options Under Advanced.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advanced Search For MAX Fields", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyAdvanceSearchForMAXFields() throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(mydde.verifyAdvancedSearchForMAXFields(mapAttrValues)){
			report.report("Successfully Verified Advanced Search For MAX Fields.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verified Advanced Search For MAX Fields.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advance Search report for Status:Suspense & Location:B6001", paramsInclude = { "testType" })
	public void verifyAdvancedSearchForStatusLocation() throws Exception{
		if(mydde.verifyAdvancedSearchForStatusLocation()){
			report.report("Successfully Verified Advanced Search For Status Location.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verified Advanced Search For Status Location.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify UB04 form", paramsInclude = { "testType" })
	public void verifyOptionUB04Form() throws Exception{
		if(mydde.verifyOptionUB04Form()){
			report.report("Successfully Verified UB04 form.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verified UB04 form.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Advanced Option under MY DDE Page", paramsInclude = { "testType" })
	public void verifyAdvancedOption() throws Exception{
		if(mydde.verifyAdvancedOption()){
			report.report("Successfully verified Advanced Option under MY DDE Page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Advanced Option under MY DDE Page..Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Live Search Option under MY DDE Page", paramsInclude = { "hic,testType" })
	public void verifyLiveSearchOption() throws Exception{
		if(mydde.verifyLiveSearchOption(hic)){
			report.report("Successfully verified Live Search Option under MY DDE Page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Live Search Option under MY DDE Page..Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying the options under Advanced View of MY DDE Page", paramsInclude = { "testType" })
	public void verifyOptionsUnderAdvancedView() throws Exception{
		if(mydde.verifyOptionsUnderAdvancedView()){
			report.report("Successfully verified options under Advanced view of MY DDE Page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify options under Advanced view of MY DDE Page..Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying the options under Advanced Search page of MY DDE Page", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOptionsUnderAdvancedSearch() throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(mydde.verifyOptionsUnderAdvancedSearch(mapAttrValues)){
			report.report("Successfully verified options under Advanced Search of MY DDE Page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify options under Advanced Search of MY DDE Page..Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Column Headers in the Search Results.", paramsInclude = { "expectedColumns,testType" })
	public void verifyColumnHeadersInSearchResults() throws Exception{
		if(mydde.verifyColumnHeadersInSearchResults(expectedColumns)){
			report.report("Successfully verified column headers in the Search Results page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify column headers in the Search Results page.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	/*@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Totals for advanced search result page.", paramsInclude = { "savedProfile,testType" })
	public void verifyTotalsForAdvancedSearchResult() throws Exception{
		if(mydde.verifyTotalsForAdvancedSearchResult(savedProfile)){
			report.report("Successfully verified totals for advanced search result page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify totals for advanced search result page.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}*/
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Save Profile option under Advance Search with duplicate name", paramsInclude = { "agency,monthsAgo,saveProfileNameAS,testType" })
	public void verifySaveProfileOptionWithDuplicateName() throws Exception{
		if(mydde.verifySaveProfileOptionWithDuplicateName(agency,monthsAgo,saveProfileNameAS)){
			report.report("Successfully verified Save Profile option under Advance Search with duplicate name.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Save Profile option under Advance Search with duplicate name.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advanced Search report for given date range.", paramsInclude = { "agency,betweenDateAS,andDateAS,testType" })
	public void verifyAdvancedSearchForCustomDates() throws Exception{
		if(mydde.verifyAdvancedSearchForCustomDates(agency,betweenDateAS,andDateAS)){
			report.report("Successfully verified Advanced Search report for given date range.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Advanced Search report for given date range.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advanced Search report for given months.", paramsInclude = { "agency,monthsAgo,testType" })
	public void verifyAdvancedSearchForGivenMonths() throws Exception{
		if(mydde.verifyAdvancedSearchForGivenMonths(agency,monthsAgo)){
			report.report("Successfully verified Advanced Search report for given months.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Advanced Search report for given months.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advanced Search report for given months and date range.", paramsInclude = { "agency,betweenDateAS,andDateAS,monthsAgo,testType" })
	public void verifyAdvancedSearchForGivenMonthsAndDateRange() throws Exception{
		if(mydde.verifyAdvancedSearchForGivenMonthsAndDateRange(agency,betweenDateAS,andDateAS,monthsAgo)){
			report.report("Successfully verified Advanced Search report for given months and date range.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Advanced Search report for given months and date range.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	/*####
	##Getters and Setters methods
	######*/	

	public SelectTimeframe getTimeframe() {
		return timeframe;
	}

	@ParameterProperties(description = "Select Timeframe" )
	public void setTimeframe(SelectTimeframe timeframe) {
		this.timeframe = timeframe;
	}

	public String getAgency() {
		return agency;
	}

	@ParameterProperties(description = "Provide an Agency/Agencies from dropdown" )
	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getHic() {
		return hic;
	}

	@ParameterProperties(description = "Provide HIC")
	public void setHic(String hic) {
		this.hic = hic;
	}

	public String getMonthsAgo() {
		return monthsAgo;
	}

	@ParameterProperties(description = "Months Ago field in Advanced Search")
	public void setMonthsAgo(String monthsAgo) {
		this.monthsAgo = monthsAgo;
	}

	public String getLocation() {
		return location;
	}

	@ParameterProperties(description = "Locations should be B9000,B9099,B9997 etc.,")
	public void setLocation(String location) {
		this.location = location;
	}

	public Status getStatus() {
		return status;
	}

	@ParameterProperties(description = "Select Status")
	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSaveProfileNameAS() {
		return saveProfileNameAS;
	}

	public String getExpectedReportsOptions() {
		return expectedReportsOptions;
	}

	public void setExpectedReportsOptions(String expectedReportsOptions) {
		this.expectedReportsOptions = expectedReportsOptions;
	}

	public String getExpTimFrameOptions() {
		return expTimFrameOptions;
	}

	public void setExpTimFrameOptions(String expTimFrameOptions) {
		this.expTimFrameOptions = expTimFrameOptions;
	}

	public String getExpectedColumns() {
		return expectedColumns;
	}

	public void setExpectedColumns(String expectedColumns) {
		this.expectedColumns = expectedColumns;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReportText() {
		return reportText;
	}

	public void setReportText(String reportText) {
		this.reportText = reportText;
	}

	public String getColumnNameToSort() {
		return columnNameToSort;
	}

	public void setColumnNameToSort(String columnNameToSort) {
		this.columnNameToSort = columnNameToSort;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getSavedProfile() {
		return savedProfile;
	}

	public void setSavedProfile(String savedProfile) {
		this.savedProfile = savedProfile;
	}

	public String getBetweenDateAS() {
		return betweenDateAS;
	}

	public void setBetweenDateAS(String betweenDateAS) {
		this.betweenDateAS = betweenDateAS;
	}

	public String getAndDateAS() {
		return andDateAS;
	}

	public void setAndDateAS(String andDateAS) {
		this.andDateAS = andDateAS;
	}

	@ParameterProperties(description = "Save this search profile as")
	public void setSaveProfileNameAS(String saveProfileNameAS) {
		this.saveProfileNameAS = saveProfileNameAS;
	}

	public AttributeNameValueDialogProvider[] getAttributeNameValueDialogProvider() {
		return AttributeNameValueDialogProvider;
	}

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(
			AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}
}
