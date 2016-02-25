package com.ability.ease.HMO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.home.HomePage.SubMenu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;
import com.ability.ease.testapi.IHMO;

public class HmoPage extends AbstractPageObject  {
	
	/*
	 * Entering Patient details into HMO Catcher Page
	 */
	public void FillHmo(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex) throws Exception{
		clickLink("ELIG.");
		clickLink("Add to HMO/Adv. Catcher");
		selectByNameOrID("ProvID", sAgency);
		typeEditBox("hic", sHIC);
		typeEditBox("lname",sLastName);
		typeEditBox("fname",sFirstName);
		typeEditBox("dob",sDob);
		selectByNameOrID("sex", sSex);
		clickButton("submit");
	}

	/*
	 * Adding a Patient to HMO Catcher
	 */
	public boolean addToHMO(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception{
		boolean alertverify;
		FillHmo(sAgency, sHIC, sLastName,sFirstName, sDob, sSex);
		alertverify=verifyAlert("Patient was successfully added to HMO Advantage Move Catcher!");
		String sQueryToGetDays = "SELECT DATEDIFF(Termination,createdate) as Days from ddez.hmocatcherpatient where providerid=" + sAgency +" and HIC='"+sHIC+"'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		String daysVal="";
		while(results.next()){
			daysVal = results.getString("Days");	
		}
		if(alertverify && daysVal.equalsIgnoreCase("75"))   {		
			return true;
		}
		else
		{
			return false;
		}
		
	}
	/*
	 * Adding a Patient to HMO Catcher, which already being tracked
	 */
	public boolean addToHMODuplicate(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception{
		FillHmo(sAgency, sHIC, sLastName,sFirstName, sDob, sSex);
		return verifyAlert("Your request to add this patient to the HMO Advantage Move Catcher was not accepted because this patient is already being tracked by HMO Advantage Move Catcher!");		
	}
	/*
	 * Extending the HMO Catcher by 75 days,need to have data with less than 10 days in the application
	 */
	public boolean extendHMO() throws Exception{
		int iRowCount; 
		String sHmoTrackDays = null;
		String sHmoExtendDays;
		int iHmoTrackDays = 0;
		int iHmoExtendDays = 0;
		boolean isExtended=false;
		String sHIC = null;
		String sAgency = null;
		clickLink("ELIG.");
		clickLink("HMO/Adv Catcher Patients");
		WebElement hmoWebTable=driver.findElement(By.xpath("//*[@id='datatable']/tbody"));
		hmoWebTable.findElements(By.xpath("//*[@id='datatable']/tbody/tr"));
		iRowCount=hmoWebTable.findElements(By.xpath("//*[@id='datatable']/tbody/tr")).size();
		for(int i=1;i<=iRowCount;i++){
		sHmoTrackDays=hmoWebTable.findElement(By.xpath("//*[@id='datatable']/tbody/tr["+i+"]/td[6]")).getText();
		iHmoTrackDays=Integer.parseInt(sHmoTrackDays);	
		if(iHmoTrackDays<10){
			hmoWebTable.findElement(By.xpath("//*[@id='datatable']/tbody/tr["+i+"]/td[1]/input")).sendKeys(Keys.SPACE);
			sHmoExtendDays=hmoWebTable.findElement(By.xpath("//*[@id='datatable']/tbody/tr["+i+"]/td[6]")).getText();
			iHmoExtendDays=Integer.parseInt(sHmoTrackDays);
			sHIC=hmoWebTable.findElement(By.xpath("//*[@id='datatable']/tbody/tr["+i+"]/td[2]")).getText();
//			((JavascriptExecutor) driver).executeScript("$('#reportExport').click();");
			clickLinkV2("Extend");
			isExtended = verifyAlert("Patient(s) successfully changed on HMO Advantage Catcher!");
		}

			break;	
		}
		String sQueryToGetDays = "SELECT DATEDIFF(Termination,createdate) as Days from ddez.hmocatcherpatient where providerid=" + sAgency +" and HIC='"+sHIC+"'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		String sExtendedVals="";
		while(results.next()){
		sExtendedVals = results.getString("Days");	
		}
		return(isExtended && Integer.parseInt(sExtendedVals)==iHmoExtendDays+75);			
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
	
	
