package com.ability.ease.common;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.easeServerStatus;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.auto.utilities.HttpClientUtil;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IAuditDoc;
import com.ability.ease.testapi.ICommon;

public class CommonTests extends BaseTest{
	
	private ICommon common;
	private easeServerStatus easeSrvStauts;
	private String sReferenceURI;

	@Before
	public void setupTests()throws Exception{
		common = (ICommon)context.getBean("common");
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "${easeSrvStauts} Ease Server", paramsInclude = { "easeSrvStauts", "testType" })
	public void stopStartEaseServer()throws Exception{
		if(CommonUtils.stopStartEaseServer(easeSrvStauts.toString())){
			report.report("EASE server " + easeSrvStauts.toString() + " successful!!!",ReportAttribute.BOLD);
		}else{
			report.report("EASE server " + easeSrvStauts.toString() + " failed !!!", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Parse Test Result and Push The Data to ART DB", paramsInclude = { "testType" })
	public void parseTestResultAndPushDataToArtDB()throws Exception{
		TestResultParser.parseTestResultAndPushDataToARTDB();
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "HTTP Send POST", paramsInclude = { "sReferenceURI, testType" })
	public void httpSendPOST()throws Exception{
		int response = HttpClientUtil.sendPOST(sReferenceURI);
		if( response == 200){
			report.report( response + " : HTTP request has been sent successfully !!! ",ReportAttribute.BOLD);
		}else{
			report.report( response + " : HTTP request failed !!! ", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Send e-mail notification", paramsInclude = { "testType" })
	public void sendEmailNotification()throws Exception{
		Thread.sleep(5000);
		if (CommonUtils.sendEmailNotification()){
			report.report("E-mail has been sent successfully !!!", ReportAttribute.BOLD);
		}else{
			report.report("E-mail sending failed !!!", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Launch ART Dashboard URL", paramsInclude = { "sReferenceURI, testType" })
	public void launchARTDashboard()throws Exception{
		if (common.launchARTDashboardURL(sReferenceURI)){
			report.report("ART Dashboard launch successful!!!", ReportAttribute.BOLD);
		}else{
			report.report("ART Dashboard launch failed !!!", Reporter.FAIL);
		}
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


	public String getsReferenceURI() {
		return sReferenceURI;
	}

	@ParameterProperties(description = "Provide reference URL to get the reposnse for with out 'http'")
	public void setsReferenceURI(String sReferenceURI) {
		this.sReferenceURI = sReferenceURI;
	}

	
}
