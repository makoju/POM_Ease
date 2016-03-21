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
		Thread.sleep(5000);
		safeJavaScriptClick("ELIG.");
		Thread.sleep(5000);
		waitForElementToBeClickable(ByLocator.linktext,"Add to HMO/Adv. Catcher",9);
		safeJavaScriptClick("Add to HMO/Adv. Catcher");
		waitForElementToBeClickable(ByLocator.name,"ProvID",9);
		selectByNameOrID("ProvID", sAgency);
		typeEditBox("hic", sHIC);
		typeEditBox("lname",sLastName);
		typeEditBox("fname",sFirstName);
		typeEditBox("dob",sDob);
		selectByNameOrID("sex", sSex);
		clickButtonV2("submit");
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
		waitForElementToBeClickable(ByLocator.linktext,"ELIG.",10);
		safeJavaScriptClick("ELIG.");
		waitForElementToBeClickable(ByLocator.linktext,"HMO/Adv Catcher Patients",8);
		safeJavaScriptClick("HMO/Adv Catcher Patients");
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
