package com.ability.ease.HMO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class HMOHelper extends AbstractPageObject {
	
	/*
	 * Entering Patient details into HMO Catcher Page
	 */
	public void FillHmo(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex) throws Exception{
		safeJavaScriptClick("ELIG.");
		waitForElementToBeClickable(ByLocator.linktext,"Add to HMO/Adv. Catcher",5);
		safeJavaScriptClick("Add to HMO/Adv. Catcher");
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
		String sQueryToGetDays = "SELECT DATEDIFF(Termination,createdate) as Days from ddez.hmocatcherpatient where providerid=" + sAgency +" and HIC='"+sHIC+"'";
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
	public String HMODBConnection(String  sHIC) throws SQLException{
		String sQueryToGetDays = "SELECT datediff(Termination,createdate) as Days from ddez.hmocatcherpatient where  HIC='"+sHIC+"'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		String extendedDays="";
		while(results.next()){
			extendedDays = results.getString("Days");	
							}
	return extendedDays;
		}


	/*
	 * Navigate to Patient info from Eligibility-report link
	 */	
	public void navigateToPatientInfoPage(String sHIC) throws Exception	{
		String xpathToGreenBoxAQB = "//td[@id='tdGoodActivity']";
		WebElement greenBox = waitForElementToBeClickable(ByLocator.xpath, xpathToGreenBoxAQB, 10);
		greenBox.click();
		driver.findElement(By.xpath("//*[contains(text(),'"+sHIC+"')]/../following-sibling::td/a[text()='Report']")).click();
		clickLink("Add patient to my HMO Move Catcher list.");
	}
	
	/*
	 * Navigate to HMO/Adv Catcher Patients
	 */	
	public void navigateToHMOCatcherExtendPage() throws Exception	{
		safeJavaScriptClick("ELIG.");
		waitForElementToBeClickable(ByLocator.linktext,"ELIG.",8);
		safeJavaScriptClick("HMO/Adv Catcher Patients");
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
