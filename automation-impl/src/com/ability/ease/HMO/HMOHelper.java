package com.ability.ease.HMO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class HMOHelper extends AbstractPageObject {

	/*
	 * Entering Patient details into HMO Catcher Page
	 */
	public void fillHmo(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex) throws Exception{
		String xpathToEligCheckPage = "//td[contains(text(),'ELIGIBILITY CHECK')]";

		waitForElementToBeClickable(ByLocator.linktext, "ELIG.", 20);
		safeJavaScriptClick("ELIG.");
		if (waitForElementToBeClickable(ByLocator.xpath, xpathToEligCheckPage, 30) != null){
			clickLinkV2("Add to HMO/Adv. Catcher");
			if( waitForElementToBeClickable(ByLocator.id,"ProvID",30) != null){
				selectByNameOrID("ProvID", sAgency);
				typeEditBox("hic", sHIC);
				typeEditBox("lname",sLastName);
				typeEditBox("fname",sFirstName);
				typeEditBox("dob",sDob);
				selectByNameOrID("sex", sSex);
				clickButtonV2("submit");
			}else{
				report.report("Fail : Failed to locate Provider ID dropdwon in Eligibility page!!!");
			}
		}else{
			report.report("Fail: Failed to locate Add to HMO/Adv. Catcher link under Elig. tab!!!");
		}
	}

	/*
	 * Connecting to DB to get Extended Days using HIC and NPI
	 */
	public String HMODBConnection(String  sAgency,String sHIC) throws SQLException{
		String sQueryToGetDays = "SELECT DATEDIFF(Termination,createdate) as Days from ddeztest.customer.hmocatcherpatient where providerid=" + sAgency +" and HIC='"+sHIC+"'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		String daysVal="";
		while(results.next()){
			daysVal = results.getString("Days");	
		}
		return daysVal;
	}

	/*
	 * Connecting to DB to get Extended Days using HIC
	 */
	public int getExtendedDaysFromDB(String  sHIC) throws SQLException{
		String sQueryToGetDays = "SELECT datediff(Termination,createdate) as Days from ddez.hmocatcherpatient where  HIC='"+sHIC+"'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		int iextendedDays = 0;
		while(results.next()){
			iextendedDays= results.getInt("Days");
		}
		return iextendedDays;
	}


	/*
	 * Navigate to Patient info from Eligibility-report link
	 */	
	public void navigateToPatientInfoPage(String sHIC) throws Exception	{
		String xpathToGreenBoxAQB = "//td[@id='tdGoodActivity']";
		WebElement greenBox = waitForElementToBeClickable(ByLocator.xpath, xpathToGreenBoxAQB, 20);
		greenBox.click();
	}

	/*
	 * Navigate to HMO/Adv Catcher Patients
	 */	
	public void navigateToHMOCatcherExtendPage() throws Exception	{

		if ((waitForElementToBeClickable(ByLocator.linktext,"ELIG.",20)) != null){
			safeJavaScriptClick("ELIG.");
			report.report("Clicked ELIG. link");
			if(waitForElementToBeClickable(ByLocator.linktext,"HMO/Adv Catcher Patients",60) != null){
				safeJavaScriptClick("HMO/Adv Catcher Patients");
				report.report("Clicked HMO/Adv Catcher Patients Tab...");
			}else{
				report.report("Failed to navigate to HMO/Adv Catcher Patients Tab");
			}
		}else{
			report.report("Failed to navigate to ELIG. page");
		}
	}

	/**
	 * @author nageswar.bodduri
	 * This method just updates the termination time stamp for one of the existing records to perform the extend operation on HMO catcher
	 */

	public void updateTerminationTime(String sHIC){
		String updateQuery = "UPDATE ddez.hmocatcherpatient SET CreateDate=now(),Termination=Date_ADD(CreateDate, INTERVAL 5 DAY) WHERE HIC = '" + sHIC + "'";
		MySQLDBUtil.getUpdateResultFromMySQLDB(updateQuery);
		report.report("Termination date updation successful !!!");
	}


	public void ganerateRandom9DigitNumberfollowedByChar(){

	}
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}
}
