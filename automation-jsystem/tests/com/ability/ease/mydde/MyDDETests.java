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
import com.ability.ease.testapi.IMyDDE;

public class MyDDETests extends BaseTest{

	private IMyDDE mydde;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		mydde = (IMyDDE)context.getBean("mydde");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MyDDE.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the page view ", paramsInclude = { "testType" })
	public void verifyPageViewAndOptions()throws Exception{
		if(mydde.verifyPageViewAndOptions()){
			report.report("Successfully Verified Page view and Options under it.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Page view and Options under it.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Reports ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOptionsUnderReports()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(mydde.verifyOptionsUnderReports(mapAttrValues)){
			report.report("Successfully Verified Options under Reports.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Reports.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Timeframe ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOptionsUnderTimeframe()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(mydde.verifyOptionsUnderTimeframe(mapAttrValues)){
			report.report("Successfully Verified Options under Timeframe.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Timeframe.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Agency ", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifyOptionsUnderAgency()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(mydde.verifyOptionsUnderAgency(mapAttrValues)){
			report.report("Successfully Verified Options under Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Agency.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Save Profile option under Advance Search", paramsInclude = { "AttributeNameValueDialogProvider,testType" })
	public void verifySaveProfileOption()throws Exception{
		if(mydde.verifySaveProfileOption()){
			report.report("Successfully Verified Options under Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Agency.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}
	
	
	
	

	public AttributeNameValueDialogProvider[] getAttributeNameValueDialogProvider() {
		return AttributeNameValueDialogProvider;
	}

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}
}
