package com.ability.ease.common;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.junit.Before;
import org.junit.Test;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.easeServerStatus;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IAuditDoc;
import com.ability.ease.testapi.ICommon;

public class CommonTests extends BaseTest{
	
	private ICommon common;
	private easeServerStatus easeSrvStauts;

	@Before
	public void setupTests()throws Exception{
		common = (ICommon)context.getBean("common");
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "${easeSrvStauts} Ease Server", paramsInclude = { "easeSrvStauts", "testType" })
	public void stopStartEaseServer()throws Exception{
		if(CommonUtils.stopStartEaseServer(easeSrvStauts.toString())){
			report.report("EASE server " + easeSrvStauts.toString() + " successful!!!");
		}else{
			report.report("EASE server " + easeSrvStauts.toString() + " failed !!!");
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Parse Test Result and Push The Data to ART DB", paramsInclude = { "testType" })
	public void parseTestResultAndPushDataToArtDB()throws Exception{
		TestResultParser.parseTestResultAndPushDataToARTDB();

	}


	/*####
	# Getters and Setters
	####*/
	
	public easeServerStatus getEaseSrvStauts() {
		return easeSrvStauts;
	}

	@ParameterProperties(description = "Select start or stop from drop down")
	public void setEaseSrvStauts(easeServerStatus easeSrvStauts) {
		this.easeSrvStauts = easeSrvStauts;
	}

}
