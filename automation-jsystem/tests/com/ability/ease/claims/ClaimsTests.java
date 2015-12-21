package com.ability.ease.claims;

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
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IClaims;

public class ClaimsTests extends BaseTest{

	private IClaims claims;
	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		claims = (IClaims)context.getBean("claims");
	}

	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify UB04 Form Fields", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void verifyUB04FormFieldss()throws Exception{
		Map<String,String> mapAttrValues = AttrStringstoMapConvert.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		if(!claims.verifyUB04FormFeatures(mapAttrValues)){
			report.report("Failed to verify the fields in UB04 form!", Reporter.FAIL);
		}else{
			report.report("Successfully verified all the fields in UB04 form!", Reporter.ReportAttribute.BOLD);
		}

	}

	/*
	 * Handle UI event method
	 */
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
		if(!methodName.equalsIgnoreCase("verifyUB04FormFields")){
			UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml");
		}
	}

	/*###
	##Getters and setters
	###*/

	public AttributePair[] getAttrpair() {
		return attrpair;
	}

	@ParameterProperties(description = "AttributeNames, AttributeValues container")
	public void setAttrpair(AttributePair[] attrpair) {
		this.attrpair = attrpair;
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
