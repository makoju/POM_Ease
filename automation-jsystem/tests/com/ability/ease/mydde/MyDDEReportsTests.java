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
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IReports;

public class MyDDEReportsTests  extends BaseTest{
	private IReports reports;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		reports = (IReports)context.getBean("reports");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MyDDE.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Summary Report Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySummaryReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySummaryReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Summary report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Summary report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Summary Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Summary Report Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report Sort Header and Help Text", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportSortHeaderHelp(mapAttrValues)){
			report.report("Successfully Verified Changes report Sort Header and Help Text:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Changes report Sort Header and Help Text. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Changes Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Changes Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Changes Report LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Changes Report LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report Sort Header", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportSortHeader() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportSortHeader(mapAttrValues)){
			report.report("Successfully Verified High Level Payment Summary Report Sort Header", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify High Level Payment Summary Report Sort Header. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified High Level Payment Summary Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify High Level Payment Summary Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report Check Amount and Projected Amount", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(mapAttrValues)){
			report.report("Successfully Verified High Level Payment Summary Report Check Amount and Projected Amount", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify High Level Payment Summary Report Check Amount and Projected Amount.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify High Level Payment Summary Report MultiAgency Select and Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(mapAttrValues)){
			report.report("Successfully Verified High Level Payment Summary Report MultiAgency Select and Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify High Level Payment Summary Report MultiAgency Select and Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Summary Report Sort,Header and Help Text", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentSummaryReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentSummaryReportSortHeaderHelp(mapAttrValues)){
			report.report("Successfully Verified Payment Summary Report Sort,Header and Help Text", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Payment Summary Report Sort, Header and Help Text.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentSummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentSummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Payment Summary Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Payment Summary Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Summary Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentSummaryReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentSummaryReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Payment Summary Report LastUpdateDate column", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Payment Summary Report LastUpdateDate column. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Report Sort, Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Payment Report Sort, Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Payment Report Sort, Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Payment Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Payment Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Payment Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPaymentReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPaymentReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Payment Report LastUpdateDate column", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Payment Report LastUpdateDate column. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Submitted Claims Report Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySubmittedClaimsSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySubmittedClaimsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Submitted Claims Report Sort, Header and Help", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Submitted Claims Report Sort, Header and Help. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Submitted Claims Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySubmittedClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySubmittedClaimsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Submitted Claims Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Submitted Claims Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Unpaid Claims Report Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyUnpaidClaimsSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyUnpaidClaimsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Submitted Claims Report Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Submitted Claims Report Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Unpaid Claims Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyUnpaidClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyUnpaidClaimsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Submitted Claims Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Submitted Claims Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Active Episodes Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyActiveEpisodesSortHeaderHelp() throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyActiveEpisodesHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Active Episodes Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Active Episodes Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Active Episodes Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyActiveEpisodesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyActiveEpisodesExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Active Episodes Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Active Episodes Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Episodes Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEpisodesSortHeaderHelp() throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEpisodesHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Episodes Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Episodes Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Episodes Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEpisodesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEpisodesExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Episodes Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Episodes Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Claims Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyClaimsReportSortHeaderandHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyClaimsReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Claims Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Claims Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Claims Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyClaimsReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyClaimsReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Claims Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Claims Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Stuck In Suspense Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyStuckInSuspenseSortHeaderandHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyStuckInSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Stuck In Suspense Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Stuck In Suspense Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPSuspenseSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAP Suspense Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Suspense Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Suspense Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPSuspenseSortHeaderandHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAP Suspense Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Suspense Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPSuspenseExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPSuspenseExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified RAP Suspense Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Suspense Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Paid Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPPaidSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPPaidHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAP Paid Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Paid Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Paid Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPPaidExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPPaidExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified RAP Paid Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Paid Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Error Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPErrorSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPErrorHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAP Error Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Error Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Error Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPErrorExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPErrorExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified RAP Error Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Error Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Cancelled Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPCancelledHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPCancelledHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAP Cancelled Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Cancelled Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAP Cancelled Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPCancelledExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPCancelledExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified RAP Cancelled Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAP Cancelled Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Suspense Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalSuspenseHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalSuspenseHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Final Suspense Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Suspense Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalSuspenseExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalSuspenseExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Final Suspense Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Suspense Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Paid Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalPaidHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalPaidHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Final Paid Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Paid Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Paid Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalPaidExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalPaidExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Final Paid Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Paid Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Error Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalErrorSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalErrorHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Final Error Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Error Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Error Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalErrorExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalErrorExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Final Error Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Error Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Cancelled Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalCancelledHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalCancelledHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Final Cancelled Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Cancelled Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Cancelled Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalCancelledExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalCancelledExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Final Cancelled Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Cancelled Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Due Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalDueSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Final Due Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Due Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Final Due Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyFinalDueExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Final Due Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Final Due Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Z_On Hold Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyZ_OnHoldSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Z_On Hold Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Z_On Hold Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Z_On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyZ_OnHoldExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyFinalDueExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Z_On Hold Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Z_On Hold Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAPs At Risk Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPsAtRiskSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPsAtRiskHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAPs At Risk Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAPs At Risk Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify RAPs At Risk Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyRAPsAtRiskExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyRAPsAtRiskExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified RAPs At Risk Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAPs At Risk Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyADRSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyADRHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified RAPs At Risk Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify RAPs At Risk Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyADRExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyADRExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified ADR Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify ADR Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyADRLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyADRLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified ADR LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify ADR LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Issues Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityIssuesSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityIssuesHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Eligibility Issues Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Eligibility Issues Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Issues Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityIssuesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityIssuesExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Eligibility Issues Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Eligibility Issues Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Patients Report Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPatientsReportSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPatientsReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Patients Report Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Patients Report Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Patients Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyPatientsReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyPatientsReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Patients Report Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Patients Report Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Errors Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityErrorsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityErrorsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Eligibility Errors Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Eligibility Errors Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Eligibility Errors Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyEligibilityErrorsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyEligibilityErrorsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Eligibility Errors Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Eligibility Errors Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify HMO Patients Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHMOPatientsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHMOPatientsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified HMO Patients Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify HMO Patients Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify HMO Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHMOPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHMOPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified HMO Patients Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify HMO Patients Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify MSP Patients Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyMSPPatientsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyMSPPatientsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified MSP Patients Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify MSP Patients Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify MSP Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyMSPPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyMSPPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified MSP Patients Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify MSP Patients Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 1st Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA1stSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA1stHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Other HHA 1st Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Other HHA 1st Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 1st Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA1stExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA1stExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Other HHA 1st Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Other HHA 1st Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 2nd Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA2ndSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA2ndHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Other HHA 2nd Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Other HHA 2nd Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Other HHA 2nd Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOtherHHA2ndExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyOtherHHA2ndExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Other HHA 2nd Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Other HHA 2nd Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Hospice Patients Sort Header and HelpText", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHospicePatientsSortHeaderandHelpText() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHospicePatientsHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Hospice Patients Sort Header and HelpText", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Hospice Patients Sort Header and HelpText. Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Hospice Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyHospicePatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyHospicePatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Hospice Patients Export Options", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Hospice Patients Export Options. Please see the JSystem report log for more details", Reporter.FAIL);
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

}
