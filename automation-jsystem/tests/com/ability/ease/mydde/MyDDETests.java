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

public class MyDDETests  extends BaseTest{
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
