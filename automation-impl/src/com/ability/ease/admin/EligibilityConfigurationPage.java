package com.ability.ease.admin;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class EligibilityConfigurationPage extends AbstractPageObject {
	
	public boolean setPsychiatricSTCCheckBoxForNPI(String npis) throws Exception{
		String[] npiarray=null;
		int failurecount=0; 
		
		navigateToPage();
		waitForElementVisibility(By.className("headerblue"));
		if(!isTextPresent("CHANGE ELIGIBILITY CONFIGURATION")){
			report.report("Failed to navigate to Eligibility Configuration page");
			return false;
		}
		
		if(npis!=null){
			npiarray = npis.split(",");
			for(String npi:npiarray){
				String npixpath = "//span[text()='"+npi+"']";
				WebElement npielement = waitForElementVisibility(By.xpath(npixpath));
				if(npielement!=null)
				{
					WebElement psychiatriccheckbox = waitForElementVisibility(By.xpath(npixpath+"/following-sibling::td/input"));
					if(psychiatriccheckbox.isSelected()){
						report.report("psychiatriccheckbox for this NPI: "+npi+" is already selected so, ignoring");
						continue;
					}
					psychiatriccheckbox.click();
					if(!verifyAlert("Warning: You are verifying that you are a Psychiatric/Mental Health provider.  Appropriate use of Psychiatric/Mental Health service type code information will be subject to CMS auditing."))
					{
						report.report("Expected Alert not found", Reporter.WARNING);
						failurecount++;
					}
				}
				else
				{
					report.report("Specified NPI: "+npi+" is not found in eligibility configuration page", Reporter.WARNING);
					continue;
				}
			}
			clickButton("Submit");
			//There was an error saving the configuration changes. Contact Customer Support if issues persist
		}
		else
		{
			report.report("No npis are provided for this test. Provide atleast one npi", Reporter.WARNING);
			return false;
		}	
		
		return failurecount==0?true:false;
	}

	public boolean verifyPsychiatricSTCCheckBoxForNPI(String npis) throws Exception {
		String[] npiarray=null;
		int failurecount=0; 
		
		navigateToPage();
		//Check that you are in right page
		waitForElementVisibility(By.className("headerblue"));
		if(!isTextPresent("CHANGE ELIGIBILITY CONFIGURATION")){
			report.report("Failed to navigate to Eligibility Configuration page");
			return false;
		}
		
		if(npis!=null){
			npiarray = npis.split(",");
			for(String npi:npiarray){
				String npixpath = "//span[text()='"+npi+"']";
				WebElement npielement = waitForElementVisibility(By.xpath(npixpath));
				if(npielement!=null)
				{
					WebElement psychiatriccheckbox = waitForElementVisibility(By.xpath(npixpath+"/following-sibling::td/input"));
					if(psychiatriccheckbox.isSelected()){
						report.report("psychiatriccheckbox for this NPI: "+npi+" is selected");
						continue;
					}
					else
					{
						report.report("psychiatriccheckbox for this NPI: "+npi+" is selected", Reporter.WARNING);
						failurecount++;
					}
				}
				else
				{
					report.report("Specified NPI: "+npi+" is not found in eligibility configuration page", Reporter.WARNING);
					continue;
				}
			}
		}
		else
		{
			report.report("No npis are provided for this test. Provide atleast one npi", Reporter.WARNING);
			return false;
		}
		return failurecount==0?true:false;
	}
	
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.Admin,  null);
		clickLink("Eligibility Configuration");
	}
}
