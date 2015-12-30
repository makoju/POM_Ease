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
			report.report("Successfully Verified Header and Help Text of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Summary Report Export Options", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySummaryReportExportPDFExcel() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifySummaryReportExportPDFExcel(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of report.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Agency Changes Report Sort Header and Help Text", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyChangesReportSortHeaderHelp() throws Exception{	
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(reports.verifyChangesReportSortHeaderHelp(mapAttrValues)){
			report.report("Successfully Verified Header and Help Text of report:", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Header and Help Text of report.Please see the JSystem report log for more details", Reporter.FAIL);
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
