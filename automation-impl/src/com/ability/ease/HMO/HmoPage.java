package com.ability.ease.HMO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

	HMOHelper hhp=new HMOHelper();
	boolean alertverify;
	String daysVal;
	/*
	 * Adding a Patient to HMO Catcher
	 */
	public boolean addToHMO(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception{
		hhp.FillHmo(sAgency, sHIC, sLastName, sFirstName, sDob, sSex);
		alertverify=verifyAlert("Patient was successfully added to HMO Advantage Move Catcher!");
		daysVal=hhp.HMODBConnection(sHIC);
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
	public boolean addToHMODuplicatePatient(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception{
		hhp.FillHmo(sAgency, sHIC, sLastName,sFirstName, sDob, sSex);
		return verifyAlert("Your request to add this patient to the HMO Advantage Move Catcher was not accepted because this patient is already being tracked by HMO Advantage Move Catcher!");		
	}
	/*
	 * Extending the HMO Catcher by 75 days,need to have data with less than 10 days in the application
	 */
	public boolean extendHMO(String sHIC) throws Exception{
		boolean isExtended=false;
		int idays;
		hhp.navigateToHMOCatcherExtendPage();
		idays=Integer.parseInt(driver.findElement(By.xpath(".//*[contains(text(),"+"'"+sHIC+"'"+")]/../following-sibling::td[4]")).getText())+75;
		driver.findElement(By.xpath(".//*[contains(text(),"+"'"+sHIC+"'"+")]/../preceding-sibling::td/input")).click();
		clickLink("Extend");
		isExtended=verifyAlert("Patient(s) successfully changed on HMO Advantage Catcher!");
		String extendedDays=daysVal=hhp.HMODBConnection(sHIC);
		if( Integer.parseInt(extendedDays)==idays && isExtended ){

			return true;
		}
		else
			return false;
		}
	/*
	 * Adding a Patient to HMO Catcher from patient info page
	 */	
	public boolean addtoHMOFromPatientInfo(String sHIC) throws Exception {
		hhp.navigateToPatientInfoPage(sHIC);
		boolean bHICinHMOCatcher;
		alertverify=verifyAlert("Patient was successfully added to HMO Advantage Move Catcher!");
		clickLink("HMO/Adv Catcher Patients");
		bHICinHMOCatcher=isTextPresent("spatientdays");
		String spatientdays=hhp.HMODBConnection(sHIC);
		if(bHICinHMOCatcher && alertverify && spatientdays.equals("75")){
			return true;
		}
		else{
			return false;
	}
		}

	/*
	 * Adding a Patient to HMO Catcher from patient info page,which is already being tracked
	 */		
	public boolean addDuplicatePatientToHMOFromPatientInfo(String sHIC) throws Exception{
		hhp.navigateToPatientInfoPage(sHIC);
		return verifyAlert("Your request to add this patient to the HMO Advantage Move Catcher was not accepted because this patient is already being tracked by HMO Advantage Move Catcher!");
		
	}
	public boolean trashHMOPatient(String sHIC) throws Exception{
		hhp.navigateToHMOCatcherExtendPage();
		checkChkBox(".//*[contains(text(),"+"'"+sHIC+"'"+")]/../preceding-sibling::td/input");
		clickLink("reportDelete");
		return(isTextExistInTable(".//*[contains(text(),"+"'"+sHIC+"'"+")]"));
		
	}
	
	public boolean printAndAdvanceSearchFromHMO(String sHIC) throws Exception{
		int ifailCount=0;
		hhp.navigateToHMOCatcherExtendPage();
		moveToElement("reportHICSearch");
		selectByNameOrID("reportHICEntry", sHIC);
		clickButton("reportHICButton");
		boolean bliveSearcheligcheck=isTextExistInTable(".//*[@id='mainPageArea']/div/table/tbody/tr/td");
		boolean bliveSearchPatientInfo=isTextExistInTable(".//*[@id='mainPageArea']/div/table/tbody/tr/td");
		if(bliveSearcheligcheck == false || bliveSearchPatientInfo == false){
			ifailCount++;
		}
		hhp.navigateToHMOCatcherExtendPage();
		moveToElement("reportHICSearch");
		selectByNameOrID("reportAdvanceSearch", sHIC);
		boolean bliveSearchAdvance= isTextExistInTable(".//*[@id='mainPageArea']/div/table/tbody/tr/td");
		if(bliveSearchAdvance==false){
			ifailCount++;
		}
		hhp.navigateToHMOCatcherExtendPage();
		clickButton("reportPrint");
		
		if(ifailCount>0){
			return false;
		}
		else
		{
			return true;
		}
		
		
		
		
		
		
		
		
		
		
		
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
	
	
