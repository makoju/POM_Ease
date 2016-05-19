package com.ability.ease.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


	public enum Times{

		TWELVE_AM("12:00 AM"),
		TWELVE_THIRTY_AM("12:30 AM"),
		ONE_AM("1:00 AM"),
		ONE_THIRTY_AM("1:30 AM"),
		TWO_AM("2:00 AM"),
		TWO_THIRTY_AM("2:30 AM"),
		THREE_AM("3:00 AM"),
		THREE_THIRTY_AM("3:30 AM"),
		FOUR_AM("4:00 AM"),
		FOUR_THIRTY_AM("4:30 AM"),
		FIVE_AM("5:00 AM"),
		FIVE_THIRTY_AM("5:30 AM"),
		SIX_AM("6:00 AM"),
		SIX_THIRTY_AM("6:30 AM"),
		SEVEN_AM("7:00 AM"),
		SEVEN_THIRTY_AM("7:30 AM"),
		EIGHT_AM("8:00 AM"),
		EIGHT_THIRTY_AM("8:30 AM"),
		NINE_AM("9:00 AM"),
		NINE_THIRTY_AM("9:30 AM"),
		TEN_AM("10:00 AM"),
		TENTHIRTY_AM("10:30 AM"),
		ELEVEN_AM("11:00 AM"),
		ELEVEN_THIRTY_AM("11:30 AM"),
		TWELVE_PM("12:00 PM"),
		TWELVE_THIRTY_PM("12:30 PM"),
		ONE_PM("1:00 PM"),
		ONETHIRTY_PM("1:30 PM"),
		TWO_PM("2:00 PM"),
		TWO_THIRTY_PM("2:30 PM"),
		THREE_PM("3:00 PM"),
		THREE_THIRTY_PM("3:30 PM"),
		FOUR_PM("4:00 PM"),
		FOUR_THIRTY_PM("4:30 PM"),
		FIVE_PM("5:00 PM"),
		FIVE_THIRTY_PM("5:30 PM"),
		SIX_PM("6:00 PM"),
		SIX_THIRTY_PM("6:30 PM"),
		SEVEN_PM("7:00 PM"),
		SEVEN_THIRTY_PM("7:30 PM"),
		EIGHT_PM("8:00 PM"),
		EIGHT_THIRTY_PM("8:30 PM"),
		NINE_PM("9:00 PM"),
		NINE_THIRTY_PM("9:30 PM"),
		TEN_PM("10:00 PM"),
		TEN_THIRTY_PM("10:30 PM"),
		ELEVEN_PM("11:00 PM"),
		ELEVEN_THIRTY_PM("11:30 PM");

		private String value;

		Times(String time){
			this.value = time;
		}

		public String getValue(){
			return value;
		}

	};

	private enum TIMEINTERVAL{
		BEFORE_ONE_HOUR_FROM_CURRENTTIME, AFTER_ONE_HOUR_FROMCURRENTTIME, CURRENT_TIME;
	}

	private TIMEINTERVAL time;

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

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "set ${time} as BlackoutTime", paramsInclude = { "testType, time" })
	public void setBlackoutStartandEndTime()throws Exception{
		//Just considering some default time
		String starttime="11:00 AM";
		String endtime="12:00 AM";
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		Date dstarttime;

		if (time == TIMEINTERVAL.AFTER_ONE_HOUR_FROMCURRENTTIME){
			dstarttime = roundTimeMinutes(System.currentTimeMillis()+3600000);

			starttime = sdf.format(dstarttime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dstarttime);
			calendar.add(Calendar.MINUTE, 60);
			endtime = sdf.format(new Date(calendar.getTimeInMillis())); 

		}
		else if (time == TIMEINTERVAL.AFTER_ONE_HOUR_FROMCURRENTTIME){
			dstarttime = roundTimeMinutes(System.currentTimeMillis()-3600000);

			starttime = sdf.format(dstarttime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dstarttime);
			calendar.add(Calendar.MINUTE, 60);
			endtime = sdf.format(new Date(calendar.getTimeInMillis())); 
		}
		else{
			dstarttime = roundTimeMinutes(System.currentTimeMillis());  
			starttime = sdf.format(dstarttime);		
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dstarttime);
			calendar.add(Calendar.MINUTE, 60);
			endtime = sdf.format(new Date(calendar.getTimeInMillis())); 
		}

		keepAsGloablParameter("blackoutstarttime", starttime);
		keepAsGloablParameter("blackoutendtime", endtime);
	}

	public Date roundTimeMinutes(long timeinmillis){
		Date whateverDateYouWant = new Date(timeinmillis);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(whateverDateYouWant);

		int unroundedMinutes = calendar.get(Calendar.MINUTE);
		int mod = unroundedMinutes % 30;
		calendar.add(Calendar.MINUTE, mod < 8 ? -mod : (30-mod));
		return new Date(calendar.getTimeInMillis());
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

	public TIMEINTERVAL getTime() {
		return time;
	}

	public void setTime(TIMEINTERVAL time) {
		this.time = time;
	}

}
