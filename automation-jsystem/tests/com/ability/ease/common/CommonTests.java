package com.ability.ease.common;

import jsystem.framework.TestProperties;

import org.junit.Test;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;

public class CommonTests extends BaseTest{

	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Parse Test Result and Push The Data to ART DB", paramsInclude = { "testType" })
	public void parseTestResultAndPushDataToArtDB()throws Exception{

		TestResultParser.parseTestResultAndPushDataToARTDB();
	
	}

}
