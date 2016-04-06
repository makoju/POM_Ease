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
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IReports;

public class MyDDEReportsTests  extends BaseTest{
	
	private IReports reports;
	private SelectTimeframe timeframe;
	private String expectedColumns;
	private String expTimFrameOptions;
	private String agency;
	private String fromDate,toDate;
	
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		reports = (IReports)context.getBean("reports");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MyDDE.xml");
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
	@TestProperties(name = "Verify overnight summary report under mydde page", paramsInclude = { "timeframe,fromDate,toDate,agency,expectedColumns,testType" })
	public void verifyOvernightSummaryReportUnderMyDDE()throws Exception{
		if(reports.verifyOvernightSummaryReport(fromDate,toDate,agency,expectedColumns)){
			report.report("Successfully verified OverNight Summary report under MyDDE page.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify OverNight Summart report under MyDDE page.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Summary Report Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySummaryReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySummaryReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Header and Help Text of Summary report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Header and Help Text of Summary report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Summary Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Summary Report Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report Sort Header and Help Text", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportSortHeaderHelp(mapAttrValues)){
			report.report("Successfully verified Changes report Sort Header and Help Text:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Changes report Sort Header and Help Text. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Changes Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Changes Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully verified Changes Report LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Changes Report LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report Sort Header", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportSortHeader() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportSortHeader(mapAttrValues)){
			report.report("Successfully verified High Level Payment Summary Report Sort Header", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify High Level Payment Summary Report Sort Header. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified High Level Payment Summary Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify High Level Payment Summary Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report Check Amount and Projected Amount", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(mapAttrValues)){
			report.report("Successfully verified High Level Payment Summary Report Check Amount and Projected Amount", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify High Level Payment Summary Report Check Amount and Projected Amount.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report MultiAgency Select and Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(mapAttrValues)){
			report.report("Successfully verified High Level Payment Summary Report MultiAgency Select and Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify High Level Payment Summary Report MultiAgency Select and Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Summary Report Sort,Header and Help Text", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentSummaryReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentSummaryReportSortHeaderHelp(mapAttrValues)){
			report.report("Successfully verified Payment Summary Report Sort,Header and Help Text", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Payment Summary Report Sort, Header and Help Text.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentSummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentSummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Payment Summary Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Payment Summary Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Summary Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentSummaryReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentSummaryReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully verified Payment Summary Report LastUpdateDate column", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Payment Summary Report LastUpdateDate column. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Report Sort, Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Payment Report Sort, Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Payment Report Sort, Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Payment Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Payment Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully verified Payment Report LastUpdateDate column", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Payment Report LastUpdateDate column. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Submitted Claims Report Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySubmittedClaimsSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySubmittedClaimsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Submitted Claims Report Sort, Header and Help", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Submitted Claims Report Sort, Header and Help. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Submitted Claims Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySubmittedClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySubmittedClaimsExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Submitted Claims Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Submitted Claims Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Unpaid Claims Report Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyUnpaidClaimsSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyUnpaidClaimsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Submitted Claims Report Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Submitted Claims Report Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Unpaid Claims Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyUnpaidClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyUnpaidClaimsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Submitted Claims Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Submitted Claims Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Active Episodes Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyActiveEpisodesSortHeaderHelp() throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyActiveEpisodesHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Active Episodes Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Active Episodes Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Active Episodes Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyActiveEpisodesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyActiveEpisodesExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Active Episodes Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Active Episodes Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Episodes Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEpisodesSortHeaderHelp() throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEpisodesHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Episodes Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Episodes Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Episodes Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEpisodesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEpisodesExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Episodes Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Episodes Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Claims Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyClaimsReportSortHeaderandHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyClaimsReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Claims Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Claims Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Claims Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyClaimsReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyClaimsReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Claims Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Claims Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Stuck In Suspense Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyStuckInSuspenseSortHeaderandHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyStuckInSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Stuck In Suspense Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Stuck In Suspense Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPSuspenseSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAP Suspense Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Suspense Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Suspense Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPSuspenseSortHeaderandHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAP Suspense Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Suspense Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPSuspenseExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPSuspenseExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified RAP Suspense Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Suspense Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Paid Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPPaidSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPPaidHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAP Paid Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Paid Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Paid Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPPaidExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPPaidExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified RAP Paid Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Paid Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Error Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPErrorSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPErrorHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAP Error Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Error Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Error Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPErrorExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPErrorExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified RAP Error Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Error Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Cancelled Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPCancelledHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPCancelledHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAP Cancelled Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Cancelled Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Cancelled Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPCancelledExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPCancelledExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified RAP Cancelled Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAP Cancelled Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Suspense Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalSuspenseHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Final Suspense Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Suspense Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalSuspenseExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalSuspenseExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Final Suspense Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Suspense Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Paid Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalPaidHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalPaidHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Final Paid Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Paid Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Paid Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalPaidExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalPaidExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Final Paid Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Paid Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Error Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalErrorSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalErrorHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Final Error Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Error Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Error Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalErrorExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalErrorExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Final Error Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Error Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Cancelled Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalCancelledHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalCancelledHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Final Cancelled Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Cancelled Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Cancelled Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalCancelledExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalCancelledExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Final Cancelled Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Cancelled Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Due Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalDueSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Final Due Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Due Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Due Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalDueExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Final Due Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Final Due Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Z_On Hold Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyZ_OnHoldSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Z_On Hold Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Z_On Hold Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Z_On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyZ_OnHoldExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Z_On Hold Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Z_On Hold Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAPs At Risk Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPsAtRiskSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPsAtRiskHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAPs At Risk Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAPs At Risk Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAPs At Risk Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPsAtRiskExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPsAtRiskExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified RAPs At Risk Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAPs At Risk Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyADRSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyADRHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified RAPs At Risk Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify RAPs At Risk Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyADRExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyADRExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified ADR Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify ADR Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyADRLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyADRLastUpdateColumn(mapAttrValues)){
			report.report("Successfully verified ADR LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify ADR LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Issues Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityIssuesSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityIssuesHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Eligibility Issues Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Eligibility Issues Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Issues Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityIssuesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityIssuesExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Eligibility Issues Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Eligibility Issues Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Patients Report Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPatientsReportSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPatientsReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Patients Report Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Patients Report Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Patients Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPatientsReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPatientsReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Patients Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Patients Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Errors Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityErrorsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityErrorsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Eligibility Errors Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Eligibility Errors Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Errors Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityErrorsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityErrorsExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Eligibility Errors Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Eligibility Errors Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify HMO Patients Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHMOPatientsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHMOPatientsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified HMO Patients Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify HMO Patients Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify HMO Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHMOPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHMOPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified HMO Patients Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify HMO Patients Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify MSP Patients Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyMSPPatientsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyMSPPatientsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified MSP Patients Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify MSP Patients Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify MSP Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyMSPPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyMSPPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified MSP Patients Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify MSP Patients Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 1st Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA1stSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA1stHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Other HHA 1st Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Other HHA 1st Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 1st Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA1stExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA1stExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Other HHA 1st Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Other HHA 1st Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 2nd Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA2ndSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA2ndHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Other HHA 2nd Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Other HHA 2nd Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 2nd Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA2ndExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA2ndExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Other HHA 2nd Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Other HHA 2nd Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Hospice Patients Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHospicePatientsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHospicePatientsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully verified Hospice Patients Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Hospice Patients Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Hospice Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHospicePatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHospicePatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully verified Hospice Patients Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Hospice Patients Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Clicking Advanced Tab", paramsInclude = { "testType" })
	public void clickingAdvancedTab() throws Exception{	
		if(reports.clickAdvancedTab()){
			report.report("Successfully clicked on Advanced", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to click on Advanced. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify HMO/Adv. Catcher Report", paramsInclude = { "testType" })
	public void verifyHMOAdvCatcherReport() throws Exception{	
		if(reports.verifyHMOAdvCatcherReport()){
			report.report("Successfully verified HMO Adv. Catcher Report", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify HMO Adv. Catcher Report. Please see the JSystem report log for more details", Reporter.FAIL);
		}
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

	public IReports getReports() {
		return reports;
	}

	public void setReports(IReports reports) {
		this.reports = reports;
	}

	public SelectTimeframe getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(SelectTimeframe timeframe) {
		this.timeframe = timeframe;
	}

	public String getExpectedColumns() {
		return expectedColumns;
	}

	public void setExpectedColumns(String expectedColumns) {
		this.expectedColumns = expectedColumns;
	}

	public String getExpTimFrameOptions() {
		return expTimFrameOptions;
	}

	public void setExpTimFrameOptions(String expTimFrameOptions) {
		this.expTimFrameOptions = expTimFrameOptions;
	}

	public String getAgency() {
		return agency;
	}

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
	
	

}
