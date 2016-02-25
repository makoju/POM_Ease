package com.ability.ease.admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.dataStructure.common.easeScreens.UIAttribute;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.home.HomePage.SubMenu;
import com.ability.ease.misc.MiscPage;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AddCustomerPage extends AbstractPageObject{
	public static final int ON = 1;
	public static final int OFF = 0;
	public static final int expectedmaxusers = 5; 
	public static final int expectedtrackmonths = 18; 
    
	public boolean addCustomer(Map<String, String> mapAttrVal)throws Exception{
		//String customername=null;
		String username=null;
		String newpassword="test1234!@#$";
		String agency=null;
		ResultSet results = null;	
		int counter=0;
		String providerTypeActual=null;		
		String ptan=null;
		String sName=null;
		String displayName=null;
		String providername=null;				
		
		navigateToPage();
		safeJavaScriptClick("Add Customer");
		
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\Admin.xml", mapAttrVal);
		UIActions admin = new UIActions();
		admin.fillScreenAttributes(lsAttributes);
		
		String customername = mapAttrVal.get("Company Name");
        String npi = mapAttrVal.get("NPI");
        String type = mapAttrVal.get("Type"); 
        providername = mapAttrVal.get("Name"); 
        ptan = mapAttrVal.get("PTAN"); 

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
	       if(actualtrackmonths==expectedtrackmonths)
		  report.report("track months default value is 18");  
	       
	  /* Verify the checkboxes under claimsrun,eligibility and eftrun*/ 
	   Select sel = new Select(driver.findElement(By.id("setup_run_0"))); 
	   String actualdefaultclaimval= sel.getFirstSelectedOption().getText();
	   String expecteddefaultclaim="Claims Run";	   
		if(!Verify.StringEquals(expecteddefaultclaim,actualdefaultclaimval))
		report.report("Alert status is off"); 
		failurecount++;	   
	   
	    /*  Verify the checkboxes under claimsrun,eligibility and eftrun*/ 
		   Select sel1 = new Select(driver.findElement(By.id("setup_run_1"))); 
		   String actualdefaulteligiblityrun= sel1.getFirstSelectedOption().getText();
		   String expecteddefaulteligibilityrun="Eligibility Run";
		   if(!Verify.StringEquals(expecteddefaulteligibilityrun,actualdefaulteligiblityrun))
			   report.report("expected and actual default selection of eligiblity run got matched"); 
		       failurecount++;			   
		   

		 /* Verify the checkboxes under claimsrun,eligibility and eftrun*/ 
			   Select sel2 = new Select(driver.findElement(By.id("setup_run_2"))); 
			   String actualdefaulteftftrun = sel2.getFirstSelectedOption().getText();
			   String expecteddefaulteftrun="EFT Run";
			   if(!Verify.StringEquals(expecteddefaulteftrun,actualdefaulteftftrun))
					  report.report("expected and actual default selection of EFT Run got matched");
			      failurecount++;
			   
			   clickButtonV2("Add Run");
			   
					 /*HMO Alert DropDown Row*/ 			   			   
					selectByNameOrID("setup_run_3","HMO Alert Run");					
	               
					ArrayList<String> listofruncheckboxes = new ArrayList<String>(	Arrays.asList("setup_day2_0", "setup_day3_0", "setup_day4_0","setup_day5_0","setup_day6_0","setup_custom_0","setup_day2_1","setup_day3_1",
						"setup_day4_1","setup_day5_1","setup_day6_1","setup_custom_1","setup_day2_2","setup_day3_2","setup_day4_2","setup_day5_2","setup_day6_2","setup_custom_2",
						"setup_day2_3","setup_day3_3","setup_day4_3","setup_day5_3","setup_day6_3","setup_custom_3"));					
						for (String checkbox : listofruncheckboxes) {
							if (!getCheckBoxState(checkbox)) {
								report.report("State of Claims run,Eligibility run and EFT Run check box should be checked. But it's Unchecked",Reporter.WARNING);							
								System.out.println(checkbox);							
								failurecount++;
							}
						}
					 
					clickButton("Save Changes");		
					
				
					if(!isTextPresent("CREATE CUSTOMER"))
					{
							report.report("Create Customer Text is not present.", Reporter.WARNING);
							failurecount++;
						}					
					clickButton("Submit");
					String sExpected= "Customer"+ customername + "added!";
					verifyAlert(sExpected);
					clickOK();
						
	        return failurecount == 0 ? true : false;
	}	
 
    /* find the above newly added customer */
	public boolean findCustomer(String customerName) throws Exception
	{
		clickLink("Find Customer...");
		typeEditBox("customer_name",customerName);	
		clickButtonV2("searchSubmitButton");
		if(waitForElementVisibility(By.linkText(customerName))!=null)
			return true;		

		return false;
	}
	
	public boolean editCustomer(String customerName) throws Exception
	{
		WebElement link = waitForElementVisibility(By.linkText("ADMINISTRATION"));
		if ( link != null) {			
			clickLink("Edit Customer");			
			Actions actions = new Actions(driver);			
			WebElement ele=waitForElementVisibility(By.xpath("//div[(text()= '"+customerName +"')]"));				
			actions.moveToElement(ele).click().build().perform();
		}
		return true;		
	}
 
	public boolean verifyProviders(String customerName,String NPI,String pname,String type,String ptan, int rownumber) throws Exception
	{
		String[] expected = {NPI,pname,type,ptan};
		String[] actual = new String[expected.length];
		
		if(!editCustomer(customerName))
		{
			report.report("Verify customers" + customerName + " unsuccessful");
		}
        Thread.sleep(5000);
      
        
 		actual[0] = getEditBoxValue("npi_"+(rownumber-1));
				
		actual[1] = getEditBoxValue("provname_"+(rownumber-1));
		
		actual[2] = driver.findElement(By.id("provtype_"+(rownumber-1))).getText();
		actual[3] = getEditBoxValue("ptan_"+(rownumber-1));

		if(!Verify.verifyArrayofStrings(actual, expected, true))
		{
			report.report("actual and expected Provider details are not equal"+Reporter.WARNING);
			return false;
		}
		return true; 
	} 

	

   /*
	public boolean setUpAgencies(String agency,String intermediary,String intermediaryNumber,String region) throws Exception
	{
		boolean result = false;
		
		String str = "SETUP AGENCY - " + agency;
		
		if(isTextPresent(str))
		{
			String testnpi = driver.findElement(By.xpath("//span[contains(text(),'Agency')]/following-sibling::span")).getText();
			String testsubmit = driver.findElement(By.xpath("//span[contains(text(),'')]/following-sibling::span/input[@type='submit']")).getText();
			//String testsubmit = driver.findElement(By.xpath("//input[@type='submit']")).submit();

			//long actualCustNPI = Long.parseLong(testnpi);
			if(Verify.StringEquals(s1, s2) actualCustNPI){
				selectByNameOrID("intermediary","17");
				typeEditBox("interno","23244");
				selectByNameOrID("region","7");
				clickButton("Submit");
				//clickButtonV2("Submit");
				result =  true;
			}else{
				result = false;
			}
		}
		return result;
		
		
		
		
	}*/

	public boolean verifyAssignment(String ProviderName) throws Exception
	{
		clickLink("Agency");
		selectByNameOrID("reportAgencySelect",ProviderName);
		clickButton("Change Agency");
		clickLink("Assignment");
		
		return false;
	}
   
	/*public boolean verifyUpdatePassword(String Username, String newpassword)
	{
		boolean updatestatus = false;
		int noOfrowsUpdated = 0;
		noOfrowsUpdated = MySQLDBUtil
				.getUpdateResultFromMySQLDB("UPDATE User SET Password=PASSWORD(CONCAT('"+newpassword+"','PeakRevenue',UserName)),ForcePasswordChange=0 WHERE Username IN (Username)");
		if (noOfrowsUpdated > 0) {
			updatestatus = true;
		}
		return updatestatus;
	}*/
/*
	public boolean verifyCustomersList(Map<String, String> mapAttrVal)throws Exception
	{
	  WebElement link = waitForElementVisibility(By.linkText("ADMINISTRATION"));
		if ( link != null) {
			clickLink("ADMINISTRATION");					
			Thread.sleep(5000);
			clickLink("Customers List");
			Thread.sleep(5000);
			clickLink("Export");
			
		}
		return true;		
	}
	
	public boolean verifyPrint() throws Exception
	{
		clickLinkV2("reportPrint");
		
		return true;
	}
	
*/

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}
	public boolean updatePassword(String Username, String newpassword)
	{
		boolean updatestatus = false;
		int noOfrowsUpdated = 0;
		noOfrowsUpdated = MySQLDBUtil
				.getUpdateResultFromMySQLDB("UPDATE User SET Password=PASSWORD(CONCAT('"+newpassword+"','PeakRevenue',UserName)),ForcePasswordChange=0 WHERE Username IN ("+Username+")");
		if (noOfrowsUpdated > 0) {
			updatestatus = true;
		}
		return updatestatus;
	}
	public boolean addEmployee(Map<String, String> mapAttrVal)throws Exception
	{
		String username=null;
		String newpassword="test1234";
		
		 WebElement link = waitForElementVisibility(By.linkText("ADMINISTRATION"));		
			if ( link != null) {
				
				safeJavaScriptClick("Add Employee");								
				
			}
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\Admin.xml", mapAttrVal);
		UIActions admin = new UIActions();
		admin.fillScreenAttributes(lsAttributes);
		for(Attribute scrAttr:lsAttributes){
			if(scrAttr.getDisplayName().equalsIgnoreCase("Username")){
				username=scrAttr.getValue();
			}
		}
			int checkedCount=0;
			List<WebElement> checkBoxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
	        for(int i=0;i<checkBoxes.size();i++){
	            if(!checkBoxes.get(i).isSelected())
	            {
	            	checkBoxes.get(i).click();
	            	checkedCount++;
	            }
	            }
	        System.out.println(checkBoxes.size()+"size");
	       
	       
		clickButtonV2("Submit");
		
		String sExpected= "employee"+ username + "added!";
		verifyAlert(sExpected);
		clickOK();			
		updatePassword(username,newpassword);		
		if((new MiscPage().validLogin(username,newpassword)))
		{
			 report.report("Logged in with the new employee successfully", Reporter.WARNING);
		}
		return true;
	
	}
	
	public boolean verifyBIAnalytics(String customername)throws Exception
	{
		 customername=null;		
		WebElement link = waitForElementVisibility(By.linkText("ADMINISTRATION"));
		if ( link != null) {			
						
			safeJavaScriptClick("Edit Customer");					
			clickLink(customername);						
			if(isChecked("analytics_eanble"))
			{
			  //logoutbbneed to call
				return true;				  
			}
			
		}
		
		return true;
	}
		
	/* Once BI Server is up i can validate the username and signout functionality in the popup window*/
	public boolean verifyBIAnalyticsUser(String username)throws Exception
	{
		username=null;
		//String password="test1234";
		
		clickLinkV2("MY DDE");
		
			if(clickLinkOnlyIfExists("Analytics"))
			{
				report.report("clicked analytics button in basic");				
					
			}			
			safeJavaScriptClick("Advanced");
			
			if(clickLinkOnlyIfExists("Analytics"))
			{
				
				report.report("MY ACCOUNT link is not visible");	
			}
		return true;
	}
	
	
	
	
	public boolean assignAlerts(String username,String alerttype)throws Exception
	{
		
		WebElement we = null;
		WebElement link = waitForElementVisibility(By.linkText("ADMINISTRATION"));
		
			if ( link != null) {
				
				safeJavaScriptClick("Assign Alerts");			    
				selectByNameOrID("alerttype",alerttype);				
				selectByNameOrID("notused",username);								
				clickButton(">>");				
				clickButtonV2("Submit");
				String sExpected4= "Alerts assignment updated!";
				verifyAlert(sExpected4);
				clickOK();											
			}		
		
		return true;
			
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
		
				clickLinkV2("Setup Alerts");							
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
	public boolean verifyLogout(String username) throws Exception
	{
		if (isLoggedIn) {
			
				clickLink("LOGOUT");
			
		}
		return false;
	}
	
	
	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.Admin, SubMenu.AddCustomer);

	}

}
