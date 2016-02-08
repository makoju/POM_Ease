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
import com.ability.ease.testapi.INonHHAReports;

public class MyDDENonHHAReportsTests extends BaseTest {

	private INonHHAReports reports;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		reports = (INonHHAReports)context.getBean("nonhhareports");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MyDDE.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Summary Report Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASummaryReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASummaryReportSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Summary report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Summary report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Summary Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Summary Report Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASummaryReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASummaryReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Summary Report LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Summary Report LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Payment Summary Report Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaymentSummarySortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaymentSummarySortReportHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Payment Summary report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Payment Summary report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Payment Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaymentSummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaymentSummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Payment Summary Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Payment Summary Report Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Payment Summary Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaymentSummaryReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaymentSummaryReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Payment Summary Report LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Payment Summary Report LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Payment Report Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaymentSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaymentReportSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Payment report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Payment report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Payment Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaymentReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaymentReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Payment Report Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Payment Report Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Payment Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaymentReportLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaymentReportLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Payment Report LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Payment Report LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Submitted Claims Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASubmittedClaimsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASubmittedClaimsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Submitted Claims:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Submitted Claims.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Submitted Claims Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASubmittedClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASubmittedClaimsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Submitted Claims Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Submitted Claims Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA UnPaid Claims Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAUnPaidClaimsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAUnPaidClaimsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of UnPaid Claims :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of UnPaid Claims.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA UnPaid Claims Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAUnPaidClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAUnPaidClaimsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA UnPaid Claims Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA UnPaid Claims Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Claims Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAClaimsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAClaimsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Claims :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Claims.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Claims Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAClaimsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAClaimsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Claims Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Claims Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Stuck In Suspense Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAStuckInSuspenseSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAStuckInSuspenseSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Stuck In Suspense :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Stuck In Suspense.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Stuck In Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAStuckInSuspenseExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAStuckInSuspenseExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Stuck In Suspense Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Stuck In Suspense Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA On Hold Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAOnHoldSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAOnHoldSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of On Hold :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of On Hold.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAOnHoldExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAOnHoldExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA On Hold Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA On Hold Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA ADR Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAADRSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAADRSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of ADR :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of ADR.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAADRExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAADRExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA ADR Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA ADR Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA ADR Report LastUpdateDate column", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAADRLastUpdateColumn() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAADRLastUpdateColumn(mapAttrValues)){
			report.report("Successfully Verified Non-HHA ADR Report LastUpdateDate column of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA ADR Report LastUpdateDate column of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Eligibility Issues Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAEligibilityIssuesSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAEligibilityIssuesSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Eligibility Issues :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Eligibility Issues.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAEligibilityIssuesExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAEligibilityIssuesExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Eligibility Issues Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Eligibility Issues Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Patients Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPatientsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPatientsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Patients :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Patients.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Patients Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Patients Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Eligibility Errors Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAEligibilityErrorsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAEligibilityErrorsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Eligibility Errors :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Eligibility Errors.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA On Hold Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAEligibilityErrorsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAEligibilityErrorsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Eligibility Errors Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Eligibility Errors Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA MSP Patients Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAMSPPatientsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAMSPPatientsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of MSP Patients :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of MSP Patients.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA MSP Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAMSPPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAMSPPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA MSP Patients Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA MSP Patients Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA HHA Patients Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAHHAPatientsSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAHHAPatientsSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of HHA Patients :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of HHA Patients.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA HHA Patients Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAHHAPatientsExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAHHAPatientsExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA HHA Patients Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA HHA Patients Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Overlapping Hospice Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAOverlappingHospiceSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAOverlappingHospiceSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Overlapping Hospice :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Overlapping Hospice.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Overlapping Hospice Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAOverlappingHospiceExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAOverlappingHospiceExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Overlapping Hospice Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Overlapping Hospice Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Suspense Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASuspenseSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASuspenseSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Suspense :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Suspense.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Suspense Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHASuspenseExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHASuspenseExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Suspense Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Suspense Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Error Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAErrorSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAErrorSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Error :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Error.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Error Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAErrorExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAErrorExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Error Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Error Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Cancelled Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHACancelledSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHACancelledSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Cancelled :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Cancelled.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Cancelled Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHACancelledExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHACancelledExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Cancelled Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Cancelled Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Paid Sort Header and Help text ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaidSortReportHeaderandHelpText()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaidSortHeaderandHelpText(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of Paid :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of Paid.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Non-HHA Paid Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyNonHHAPaidExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyNonHHAPaidExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Non-HHA Paid Export Options:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Non-HHA Paid Export Options.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	/*@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Clicking Advanced Tab", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void clickingAdvancedTab() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(){
			report.report("Successfully clicked on Advanced :", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to click on Advanced tab.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}*/

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
