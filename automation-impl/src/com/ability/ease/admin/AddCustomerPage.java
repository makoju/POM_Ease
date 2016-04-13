package com.ability.ease.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.misc.MiscPage;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

/**
 * 
 * @author vahini.eruva
 *
 */
public class AddCustomerPage extends AbstractPageObject{
	public static final int ON = 1;
	public static final int OFF = 0;
	public static final int expectedmaxusers = 5; 
	public static final int expectedtrackmonths = 18; 
	int failCounter = 0;

	public boolean addCustomer(Map<String, String> mapAttrVal)throws Exception{
		navigateToPage();
		WebElement link = waitForElementVisibility(By.linkText("Add Customer"));
		if ( link != null) 			
			safeJavaScriptClick("Add Customer");
		else{
			report.report("Add Customer link not found hence returning false");
			return false;
		}
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\Admin.xml", mapAttrVal);
		UIActions admin = new UIActions();
		admin.fillScreenAttributes(lsAttributes);

		checkChkBox("alpha_split_eanble");
		checkChkBox("audit_docs_eanble");
		checkChkBox("analytics_eanble");		

		clickLink("Setup");			

		int failurecount=0;

		if(!isTextPresent("EDIT AGENCY"))
		{
			report.report("Edit Agency Text is not present.", Reporter.WARNING);
			failurecount++;
		}

		typeEditBox("setup_maxusers","5");	  

		/* validation for Track Months Default value */
		String trackmonthsval = getEditBoxValue("setup_track_months");
		int actualtrackmonths = Integer.parseInt(trackmonthsval);
		if(actualtrackmonths==expectedtrackmonths){
			report.report("track months default value is 18");
		}
		else{            
			report.report("Track months default value is not 18", Reporter.WARNING);
			failurecount++;
		}

		/* Verify the checkboxes under claimsrun,eligibility and eftrun*/ 
		Select sel = new Select(driver.findElement(By.id("setup_run_0"))); 
		String actualdefaultclaimval= sel.getFirstSelectedOption().getText();
		String expecteddefaultclaim="Claims Run";	   
		if(!Verify.StringEquals(expecteddefaultclaim,actualdefaultclaimval)){
			report.report("actual and expected default selection of claims run are not matched",ReportAttribute.BOLD); 
			failurecount++;	   
		} 

		/*  Verify the checkboxes under claimsrun,eligibility and eftrun*/ 
		Select sel1 = new Select(driver.findElement(By.id("setup_run_1"))); 
		String actualdefaulteligiblityrun= sel1.getFirstSelectedOption().getText();
		String expecteddefaulteligibilityrun="Eligibility Run";
		if(!Verify.StringEquals(expecteddefaulteligibilityrun,actualdefaulteligiblityrun))
		{
			report.report("expected and actual default selection of eligiblity run are not matched",ReportAttribute.BOLD); 
			failurecount++;			   
		}

		/* Verify the checkboxes under claimsrun,eligibility and eftrun*/ 
		Select sel2 = new Select(driver.findElement(By.id("setup_run_2"))); 
		String actualdefaulteftftrun = sel2.getFirstSelectedOption().getText();
		String expecteddefaulteftrun="EFT Run";
		if(!Verify.StringEquals(expecteddefaulteftrun,actualdefaulteftftrun))
		{
			report.report("expected and actual default selection of EFT Run are not matched",ReportAttribute.BOLD);
			failurecount++;
		}
		clickButtonV2("Add Run");

		/*HMO Alert DropDown Row*/ 			   			   
		selectByNameOrID("setup_run_3","HMO Alert Run");					

		ArrayList<String> listofruncheckboxes = new ArrayList<String>(	Arrays.asList("setup_day2_0", "setup_day3_0", "setup_day4_0","setup_day5_0","setup_day6_0","setup_custom_0","setup_day2_1","setup_day3_1",
				"setup_day4_1","setup_day5_1","setup_day6_1","setup_custom_1","setup_day2_2","setup_day3_2","setup_day4_2","setup_day5_2","setup_day6_2","setup_custom_2",
				"setup_day2_3","setup_day3_3","setup_day4_3","setup_day5_3","setup_day6_3","setup_custom_3"));					
		for (String checkbox : listofruncheckboxes) {
			if (!getCheckBoxState(checkbox)) {
				report.report("State of Claims run,Eligibility run and EFT Run check box should be checked. But it's Unchecked");							
				//System.out.println(checkbox);							
				//failurecount++;
			}
		}

		clickButton("Save Changes");		


		if(!isTextPresent("CREATE CUSTOMER"))
		{
			report.report("Create Customer Text is not present.", Reporter.WARNING);
			failurecount++;
		}					
		clickButtonV2("Submit");
		String sExpected= "Customer "+ mapAttrVal.get("Company Name") + " added!";
		if(!verifyAlert(sExpected))
		{
			report.report("Expected Alert: "+sExpected+" Not displayed", Reporter.WARNING);
			failurecount++;
		}
		//clickOK();
		report.report("Fail counter from add customer method : " + failurecount);
		return failurecount == 0 ? true : false;
	}	

	public boolean loginNewCustomer(String username,String newpassword)throws Exception{
		//int failurecount=0;
		//newpassword="test1234";
		//updatePassword(username,newpassword);
		if((new MiscPage().validLogin(username,newpassword)))
		{
			report.report("Logged in with the new customer successfully", ReportAttribute.BOLD);
			//report.report( username + " employee added succssfully !!", ReportAttribute.BOLD);
		}
		else
		{
			report.report("Not able to login with the new customer",Reporter.WARNING);
			return false;
		}
		return true;
	}

	/* find the above newly added customer */
	public boolean findCustomer(String customerName) throws Exception
	{
		clickLink("Find Customer...");
		typeEditBox("customer_name",customerName);	
		clickButtonV2("searchSubmitButton");
		if(waitForElementVisibility(By.linkText(customerName)) != null){
			return true;
		}
		return false;
	}

	public boolean editCustomer(String customerName) throws Exception
	{   
		boolean result = false;
		String custNameActual = null;
		String xpathToCustomerLink = "//div[text()='"+ customerName + "']";
		WebElement link = waitForElementVisibility(By.linkText("ADMINISTRATION"));
		if ( link != null) {			
			clickLink("Edit Customer");		
			Actions actions = new Actions(driver);			
			WebElement cust = waitForElementVisibility(By.xpath(xpathToCustomerLink));		
			//moveToElementAndClick(customer);
			//customer.click();
			Thread.sleep(2000);
			actions.moveToElement(cust).click().build().perform();
			Thread.sleep(1000);
			custNameActual =getEditBoxValue("custname");	

			if( custNameActual.equalsIgnoreCase(customerName)){
				report.report("navigate to edit customer screen" + customerName + " successful");
				result = true;
			}else{
				report.report( "Failed to navigate to edit customer screen " + customerName);
				result = false;
			}/*
			if(isTextPresent(custname))	{
				report.report("navigate to edit customer screen" + customerName + " successful");
			}else{
				report.report( "Failed to navigate to edit customer screen " + customerName);
				failCounter++;	
			}*/
		}
		return result;		
	}

	public boolean verifyProviders(String customerName,String NPI,String pname,String type,String ptan, int rownumber) throws Exception
	{
		String[] expected = {NPI,pname,type,ptan};
		String[] actual = new String[expected.length];


		if(editCustomer(customerName)){
			Thread.sleep(5000);
			actual[0] = getEditBoxValue("npi_"+(rownumber-1));
			actual[1] = getEditBoxValue("provname_"+(rownumber-1));
			WebElement drop_down =driver.findElement(By.id("provtype_"+(rownumber-1)));
			Select se = new Select(drop_down);			
			actual[2] = se.getFirstSelectedOption().getText();
			actual[3] = getEditBoxValue("ptan_"+(rownumber-1));

			if(!Verify.verifyArrayofStrings(actual, expected, true)) {
				report.report("actual and expected Provider details are not equal",Reporter.WARNING);
				failCounter++;
			}else{
				report.report("actual and expected Provider details are equal",ReportAttribute.BOLD);
			}

		}else{
			report.report("Verify customers" + customerName + " unsuccessful");
			failCounter++;
		}
		return ( failCounter == 0 ) ? true : false;
	} 


	public boolean setUpAgencies(String agency,String intermediary,String intermediaryNumber,String region) throws Exception
	{
		boolean result = false;	
		if(isTextPresent("SETUP AGENCY"))
		{
			String testnpi = driver.findElement(By.xpath("//span[contains(text(),'Agency')]/following-sibling::span")).getText();			
			if(Verify.StringEquals(agency, testnpi)){
				selectByNameOrID("intermediary",intermediary);
				typeEditBox("interno",intermediaryNumber);
				selectByNameOrID("region",region);
				clickButton("Submit");
				String sExpected= "Setup completed";
				verifyAlert(sExpected);
				clickOK();
				result =  true;
			}else{
				result = false;
			}
		}
		else
		{
			report.report("not able to setup agencies",Reporter.WARNING);
			result = false;
		}
		return result;
	}




	public boolean changeSchedule(String timeZone) throws Exception
	{
		boolean result=false;
		
		Thread.sleep(5000);
		if(isTextPresent("CHANGE SCHEDULE"))
		{
			selectByNameOrID("user_timezone",timeZone);
			checkChkBox("schedule_onoff_0");
			checkChkBox("schedule_onoff_1");
			checkChkBox("schedule_onoff_2");		
			clickButton("next");
			result = true;
		}
		else
		{
			result = false;

		}
		return result;
	}


	public boolean clickHere()throws Exception
	{
		WebElement link = waitForElementVisibility(By.linkText("Click here"));
		if ( link != null)
		{
			if(isTextPresent("CHANGE SCHEDULE"))
			{
				safeJavaScriptClick("Click here");
			}

			else{
				report.report("Change schedule is not displaying",Reporter.WARNING);
				return false;
			}
		}

		else{
			report.report("Click Here Link not found",Reporter.WARNING);
			return false;
		}	

		return true;	
	}

	public boolean setUpProvidersGroup(String customerName,String groupName,String multiagencies) throws Exception
	{
		boolean result = false;

		waitForElementVisibility(By.xpath("//td[text()='ADD FISS/DDE SETUP']"));
		if(isTextPresent("ADD FISS/DDE SETUP"))
		{
			typeEditBox("name",groupName);			
			String[] multiAgency = multiagencies.split(",");
			for(String agency:multiAgency){			  
				selectByNameOrID("UserProvID",agency);
			}
			clickButtonV2("Submit");
			String sExpected="Information saved!";
			if (verifyAlert(sExpected)){
				result = true;
			}

		}else{
			report.report("ADD FISS/DDE SETUP is not displaying",Reporter.WARNING);
			result = false;
		}

		return result;

	}	


	public boolean configureDDECredentials(String groupName,String ddeUserId,String ddePassword,String verifyPassword) throws Exception
	{
		int failCounter = 0;
		String xpathToManageDDESettings = "//td[contains(text(),'MANAGE FISS/DDE SETTINGS')]";
		
		WebElement blueHeader = waitForElementToBeClickable(ByLocator.xpath, xpathToManageDDESettings, 10);
		if(blueHeader != null){		
			Thread.sleep(5000);
			typeEditBox("ddeuser",ddeUserId);
			typeEditBox("ddepassword",ddePassword);
			typeEditBox("Verify",verifyPassword);
			clickButtonV2("Submit");

			WebDriverWait wait = new WebDriverWait(driver, 85);
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();	
    	}
		else{
				report.report("Manage FISS/DDE Settings not visible",Reporter.WARNING);
				failCounter++;		
			}
		return failCounter == 0 ? true : false;
	}



	public boolean navigateToMyDDE(String groupName,String ddeUserId,String ddePassword,String verifyPassword)throws Exception
	{

		WebElement link = waitForElementToBeClickable(ByLocator.linktext, "MY DDE", 10);
		
		if ( link != null) 			
			safeJavaScriptClick("MY DDE");
		
		else{
			report.report("Not able to navigate to MY DDE");
			return false;
		}
		Thread.sleep(5000);
		String actualgroupname=driver.findElement(By.xpath("//span[contains(text(),'Group') and contains(text(),'Name:')]/following-sibling::span")).getText();
		String atualddeuserid=driver.findElement(By.id("ddeuser")).getAttribute("value");	

		if(actualgroupname.contains(groupName))
		{
			if(atualddeuserid.equalsIgnoreCase(atualddeuserid))
			{
				typeEditBox("ddepassword",ddePassword);
				typeEditBox("Verify",verifyPassword);
				clickButtonV2("Submit");
				
				WebDriverWait wait = new WebDriverWait(driver, 85);
				wait.until(ExpectedConditions.alertIsPresent());
				driver.switchTo().alert().accept();	

			}
			else
			{
				report.report("ddeuserid and atualddeuserid are not matching",Reporter.WARNING);
				failCounter++;		
			}

		}

		else
		{
			report.report("Group Names are not matching",Reporter.WARNING);
			failCounter++;		
		}	

		return failCounter == 0 ? true : false;

	}

	public boolean updatePassword(String username,String newpassword)
	{
		boolean updatestatus = false;
		int noOfrowsUpdated = 0;
		noOfrowsUpdated = MySQLDBUtil
				.getUpdateResultFromMySQLDB("UPDATE User SET Password=PASSWORD(CONCAT('"+newpassword+"','PeakRevenue',UserName)),ForcePasswordChange=0 WHERE Username='"+username+"'");
		if (noOfrowsUpdated > 0) {
			updatestatus = true;
		}
		return updatestatus;
	}


	public boolean verifyBIAnalytics(String customername)throws Exception
	{
		//customername=null;		
		if(editCustomer(customername))
		{
			if(isChecked("analytics_eanble"))
			{
				//logoutbbneed to call
				return true;				  
			}
		}
			else{
				report.report("not able to click on customer name in edit customer screen",Reporter.WARNING);
			}
		

		return false;
	}

	/* Once BI Server is up i can validate the username and signout functionality in the popup window*/
	public boolean verifyBIAnalyticsUser(String username)throws Exception
	{
		int failCounter = 0;
		//String password="test1234";

		//clickLinkV2("MY DDE");
		waitForElementVisibility(By.linkText("Analytics"));
		Thread.sleep(1000);

		if(clickLinkOnlyIfExists("Analytics"))
		{
			report.report( "Analytics option is displaying in basic", ReportAttribute.BOLD);			

		}
		else{
			report.report("Analytics option is not displaying in basic",Reporter.WARNING);
			failCounter++;
		}
		Thread.sleep(1000);
		WebElement link1 = waitForElementVisibility(By.linkText("Advanced"));
		if ( link1 != null) {
				
		safeJavaScriptClick("Advanced");
		Thread.sleep(1000);
		
		waitForElementVisibility(By.linkText("Analytics"));
		Thread.sleep(1000);
		if(clickLinkOnlyIfExists("Analytics"))
		{

			report.report( "Analytics option is displaying in advance", ReportAttribute.BOLD);
		}
		else{
			report.report("Analytics option is not displaying in advance",Reporter.WARNING);
			failCounter++;
		}
		}
		else
		{
			report.report("not able to click on advance",Reporter.WARNING);
			failCounter++;
		}
		//clickLink("LOGOUT");
		//TODO - Once BI Configuaration set up done will validate username and click signout
		return (failCounter == 0) ? true : false;
	}




	public boolean assignAlerts(String username,String alerttype)throws Exception
	{

		if ( waitForElementVisibility(By.linkText("ADMINISTRATION")) != null ) {
			safeJavaScriptClick("Assign Alerts");			    
			selectByNameOrID("alerttype",alerttype);				
			try{
				selectByNameOrID("notused",username);
			}
			catch(Exception e){
				WebElement we = waitForElementVisibility(By.id("used"));
				Select selected = new Select(we);
				List<WebElement> selectedusers = selected.getOptions();
				for(WebElement element: selectedusers){
				    if(element.getText().equalsIgnoreCase(username)){
				    	report.report("User was already been assigned with the specified alerttype: "+alerttype, ReportAttribute.BOLD);
				    	return true;
				    }
				}
			}
			clickButton(">>");				
			clickButtonV2("Submit");
			String sExpected4= "Alerts assignment updated!";
			verifyAlert(sExpected4);
			clickOK();				
			return true;
		}
		return false;
	}

	public boolean verifySetupAlerts(String username,String alerttype)throws Exception
	{		
		WebElement link = waitForElementVisibility(By.linkText("MY ACCOUNT"));
		if ( link != null) {
			clickLinkV2("MY ACCOUNT");
		}else{
			report.report("MY ACCOUNT link is not visible");
			return false;
		}
		waitForElementVisibility(By.linkText("Setup Alerts"));
		safeJavaScriptClick("Setup Alerts");
		String alertOptionXpath = getAlertOptionXpath(alerttype);
		WebElement alertOption= driver.findElement(By.xpath(alertOptionXpath));
		String alertstatus = getAlertStatus(alertOption);
		if(!Verify.StringEquals(alertstatus, "Off"))
			report.report("Alert status is off");									

		return true;
	}

	public String getAlertStatus(WebElement alertelement){
		WebElement status = alertelement.findElement(By.xpath("../following-sibling::td/label"));
		if(status!=null)
			return status.getText();
		else
			return null;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}


	@Override
	public void navigateToPage() throws Exception {
		if(!isTextPresent("Current Status"))
			HomePage.getInstance().navigateTo(Menu.Admin, null);

	}

}
