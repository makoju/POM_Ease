package com.ability.ease.eligibility;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.report.ReporterHelper;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class EligibilityPage extends AbstractPageObject{


	/*public boolean verifyEligibility(Map<String, String> mapAttrVal)throws Exception{
		int failurecount=0;
		navigateToPage();
		clickLink("Eligiblity Check");
		
		//Fill Screen
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute>	lsAttributes = parser.getUIAttributesFromXMLV2
				(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml", mapAttrVal);
		UIActions eligibility = new UIActions();
		eligibility.fillScreenAttributes(lsAttributes);
		
		WebElement submit = waitForElementVisibility(By.xpath("//input[@value='Submit']"));
		safeJavaScriptClick(submit);

		if(!verifyAlert("Eligibility check accepted!")){
			report.report("Required alert dialog was not found", Reporter.WARNING);
			failurecount++;
		}
		
		//validation
		String firstlastname = getFirstLastName(mapAttrVal).trim();
		
		if(!verifyEligibilityRequestStatus(firstlastname))
			failurecount++;
		//To Do - we've to get the first record from the table not any record matches with the given name
		navigatetoclaimdetails(firstlastname);
		Thread.sleep(5000);
		String claimdetails = getElementText(By.xpath(".//*[@id='reportarea']//td[@class='headergreen']"));
		String expectedclaimdetails = "CLAIM CHANGE REQUEST STATUS - "+firstlastname+" ("+mapAttrVal.get("HIC")+")";
		
		if(!Verify.StringEquals(claimdetails, expectedclaimdetails))
			failurecount++;
		
		//Acknowledge the Request - To do - we've to get the first record from the table not any record matches with the given name
		int goodactivitycountprev = getActivitycount("tdGoodActivity");
		acknowledgerequest(firstlastname);
		Thread.sleep(3000);
		int goodactivitycountlatest = getActivitycount("tdGoodActivity");
		
		if(goodactivitycountprev-1!=goodactivitycountlatest){
		  report.report("actual and expected activity count doesn't match in good activity table. actual: "+goodactivitycountlatest+ "Expected: "+(goodactivitycountprev-1));
		  failurecount++;
		}
		return failurecount==0?true:false;
	}
	
	public boolean verifyHETSActivitiesCompletedStatusReport(Map<String, String> mapAttrVal) throws Exception {
		int failurecount=0;
		navigateToPage();
		clickLink("Eligiblity Check");
		
		//Fill Screen
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute>	lsAttributes = parser.getUIAttributesFromXMLV2
				(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml", mapAttrVal);
		UIActions eligibility = new UIActions();
		eligibility.fillScreenAttributes(lsAttributes);
		
		WebElement submit = waitForElementVisibility(By.xpath("//input[@value='Submit']"));
		safeJavaScriptClick(submit);

		if(!verifyAlert("Eligibility check accepted!")){
			report.report("Required alert dialog was not found", Reporter.WARNING);
			failurecount++;
		}
		
		//validation
		String firstlastname = getFirstLastName(mapAttrVal).trim();
		
		if(!verifyEligibilityRequestStatus(firstlastname))
			failurecount++;
		
		//Handle the report link of Eligibility Check Request and data validation
		String expectedreportheader = "ELIGIBILITY CHECK REPORT";
		navigatetoEligibilityReport(firstlastname);
		
		WebElement reportheader = waitForElementVisibility(By.xpath("//td[@class='headergreen']"));
		if(reportheader!=null && !Verify.StringEquals(expectedreportheader, reportheader.getText())){
			report.report("Unable to find Expected Header of a Report"+expectedreportheader, Reporter.WARNING);
			failurecount++;
		}
		
		if(!(waitForElementVisibility(By.linkText("Add patient to my HMO Move Catcher list."))!=null)){
		  report.report("Unable to find Add to HMO Catcher link", Reporter.WARNING);
		  failurecount++;
		}
		
		if(!(waitForElementVisibility(By.xpath("//span[text()='General Information']"))!=null)){
			  report.report("Unable to find General Patient Information Section", Reporter.WARNING);
			  failurecount++;
		}
		if(!(waitForElementVisibility(By.xpath("//span[text()='MSP Information']"))!=null)){
			  report.report("Unable to find Add to HMO Catcher link", Reporter.WARNING);
			  failurecount++;
		}
		
		String agency = mapAttrVal.get("Agency");
		String hic = mapAttrVal.get("HIC");
				
		if(!Verify.isElementPresent(By.xpath("//pre/h2[contains(text(), 'Agency: "+agency+"')]"))){
			  report.report("Unable to find agency information in Eligibility Check Report. Expected Agency: "+agency, Reporter.WARNING);
			  failurecount++;
		}
		if(!Verify.isElementPresent(By.xpath("//pre/h2[contains(text(), 'HIC: "+hic+"')]"))){
			  report.report("Unable to find hic information in Eligibility Check Report. Expected HIC: "+hic, Reporter.WARNING);
			  failurecount++;
		}
		return failurecount==0?true:false;
	}
	
	public boolean verifyNavigationToUB04FromPatientInfoScreen(Map<String, String> mapAttrVal) throws Exception {
		int failurecount=0;
		navigateToPage();
		clickLink("Eligiblity Check");
		//Fill Screen
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute>	lsAttributes = parser.getUIAttributesFromXMLV2
				(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml", mapAttrVal);
		UIActions eligibility = new UIActions();
		eligibility.fillScreenAttributes(lsAttributes);

		WebElement submit = waitForElementVisibility(By.xpath("//input[@value='Submit']"));
		safeJavaScriptClick(submit);

		if(!verifyAlert("Eligibility check accepted!")){
			report.report("Required alert dialog was not found", Reporter.WARNING);
			failurecount++;
		}

		//validation
		String firstlastname = getFirstLastName(mapAttrVal);
		String hic = mapAttrVal.get("HIC");
		navigatetoPatientInfoScreen(firstlastname, hic);
		WebElement we = waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"));
		if(we!=null)
			report.report("Successfully navigated to Patient Information Screen", ReportAttribute.BOLD);
		else
		{
			report.report("Navigation to Patient Information Page has failed", Reporter.WARNING);
			failurecount++;
		}
		
		return failurecount==0?true:false;
	}*/
	
	public boolean submitEligibilityCheck(Map<String, String> mapAttrVal) throws Exception{
		navigateToPage();
		
		//Fill Screen
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute>	lsAttributes = parser.getUIAttributesFromXMLV2
				(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml", mapAttrVal);
		UIActions eligibility = new UIActions();
		eligibility.fillScreenAttributes(lsAttributes);
		
		WebElement submit = waitForElementVisibility(By.xpath("//input[@value='Submit']"));
		safeJavaScriptClick(submit);

		if(!verifyAlert("Eligibility check accepted!")){
			report.report("Required alert dialog was not found", Reporter.WARNING);
			return false;
		}
		else
			return true;
	}
	
	public boolean verifyEligibilityStatus(String firstname, String lastname, String status) throws Exception {
		navigateToPage();
		//validation
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname: (firstlastname = lastname +", "+firstname);

		if(status.equalsIgnoreCase("pending")){
			if(!verifyEligibilityRequestStatusPending(firstlastname))
				return false;
		}
		else if(status.equalsIgnoreCase("completed")){
			if(!verifyEligibilityRequestStatusCompleted(firstlastname))
				return false;
		}
		return true;
	}
	
	public boolean navigatetoClaimDetails(String firstname, String lastname, String hic) throws InterruptedException{
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname: (firstlastname = lastname +", "+firstname);

		//To Do - we've to get the first record from the table not any record matches with the given name
		if(!navigatetoclaimdetails(firstlastname))
			return false;
		
		Thread.sleep(5000);
		String claimdetails = getElementText(By.xpath(".//*[@id='reportarea']//td[@class='headergreen']"));
		String expectedclaimdetails = "CLAIM CHANGE REQUEST STATUS - "+firstlastname+" ("+hic+")";

		if(!Verify.StringEquals(claimdetails, expectedclaimdetails))
			return false;
		
		return true;
	}
	
	public boolean acknoweldgeEligibility(String firstname, String lastname) throws InterruptedException{
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname: (firstlastname = lastname +", "+firstname);
		WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		//Acknowledge the Request - To do - we've to get the first record from the table not any record matches with the given name
		int goodactivitycountprev = getActivitycount("tdGoodActivity");
	
		moveToElement(tblcompletedactivity);
		WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstlastname+"')]/../td[1]/a"));
		  if(we!=null){
			  we.click();
		  }
		  else
		  {
			  report.report("Specified activity was not found in good activity table", Reporter.WARNING);
			  return false;
		  }
			Thread.sleep(3000);
			int goodactivitycountlatest = getActivitycount("tdGoodActivity");

			if(goodactivitycountprev-1!=goodactivitycountlatest){
				report.report("actual and expected activity count doesn't match in good activity table. actual: "+goodactivitycountlatest+ "Expected: "+(goodactivitycountprev-1));
			}
		  return true;
	}

	public boolean verifyHETSActivitiesCompletedStatusReport(String hic,String agency, String firstname, String lastname) throws Exception {
		int failurecount=0;
		navigateToPage();
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname: (firstlastname = lastname +", "+firstname);
		if(!verifyEligibilityRequestStatusCompleted(firstlastname))
			failurecount++;
		
		//Handle the report link of Eligibility Check Request and data validation
		String expectedreportheader = "ELIGIBILITY CHECK REPORT";
		if(!navigatetoEligibilityReport(firstlastname))
			return false;
		
		WebElement reportheader = waitForElementVisibility(By.xpath("//td[@class='headergreen']"));
		if(reportheader!=null && !Verify.StringEquals(expectedreportheader, reportheader.getText())){
			report.report("Unable to find Expected Header of a Report"+expectedreportheader, Reporter.WARNING);
			failurecount++;
		}
		
		if(!(waitForElementVisibility(By.linkText("Add patient to my HMO Move Catcher list."))!=null)){
		  report.report("Unable to find Add to HMO Catcher link", Reporter.WARNING);
		  failurecount++;
		}
		
		if(!(waitForElementVisibility(By.xpath("//span[text()='General Information']"))!=null)){
			  report.report("Unable to find General Patient Information Section", Reporter.WARNING);
			  failurecount++;
		}
		if(!(waitForElementVisibility(By.xpath("//span[text()='MSP Information']"))!=null)){
			  report.report("Unable to find Add to HMO Catcher link", Reporter.WARNING);
			  failurecount++;
		}
					
		if(!Verify.isElementPresent(By.xpath("//pre/h2[contains(text(), 'Agency: "+agency+"')]"))){
			  report.report("Unable to find agency information in Eligibility Check Report. Expected Agency: "+agency, Reporter.WARNING);
			  failurecount++;
		}
		if(!Verify.isElementPresent(By.xpath("//pre/h2[contains(text(), 'HIC: "+hic+"')]"))){
			  report.report("Unable to find hic information in Eligibility Check Report. Expected HIC: "+hic, Reporter.WARNING);
			  failurecount++;
		}
		return failurecount==0?true:false;
	}

	public boolean verifyNavigationToUB04FromPatientInfoScreen(String hic,String agency, String firstname, String lastname) throws Exception {
		int failurecount=0;
		navigateToPage();
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname: (firstlastname = lastname +", "+firstname);
		if(!navigatetoPatientInfoScreen(firstlastname, hic))
			return false;
		
		WebElement we = waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"));
		if(we!=null)
			report.report("Successfully navigated to Patient Information Screen", ReportAttribute.BOLD);
		else
		{
			report.report("Navigation to Patient Information Page has failed", Reporter.WARNING);
			return false;
		}
		if(!isTextPresent("New Claim UB04")){
			report.report("Unable to Navigate to UB04 Form from Patient Info. Screen", Reporter.WARNING);
			failurecount++;
		}
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyNavigationToClaimInfoFromPatientInfoScreen(String hic,String agency, String firstname, String lastname) throws Exception {
		int failurecount=0;
		navigateToPage();
		
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname: (firstlastname = lastname +", "+firstname);
		if(!navigatetoPatientInfoScreen(firstlastname, hic))
			return false;
		
		WebElement we = waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"));
		if(we!=null)
			report.report("Successfully navigated to Patient Information Screen", ReportAttribute.BOLD);
		else
		{
			report.report("Navigation to Patient Information Page has failed", Reporter.WARNING);
			return false;
		}
		navigatetoClaimInfoScreen();
		if(waitForElementVisibility(By.xpath("//td[contains(text()='CLAIM INFORMATION')]"))!=null)
		{
			if(!isTextPresent(firstlastname))
				failurecount++;
		}
		else{
			report.report("Failed to Navigate to Claim Information page", Reporter.WARNING);
			failurecount++;
		}

		return failurecount==0?true:false;
	}


	private void navigatetoClaimInfoScreen() {
		WebElement startdatelink = waitForElementVisibility(By.xpath("//tr[@class='tableline1']/td[3]/a"));
		if (startdatelink!=null)
			startdatelink.click();
	}

	private boolean navigatetoclaimdetails(String firstlastname) {
		  WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		  moveToElement(tblcompletedactivity);
		  WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstlastname+"')]/following-sibling::td/a[text()='Details']"));
		  if(we!=null){
			  we.click();
		  }
		  else
		  {
			  report.report("Specified activity was not found in good activity table", Reporter.WARNING);
			  return false;
		  }
		  return true;
	}
	
	private boolean navigatetoEligibilityReport(String firstlastname) {
		  WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		  moveToElement(tblcompletedactivity);
		  WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstlastname+"')]/following-sibling::td/a[text()='Report']"));
		  if(we!=null){
			  we.click();
		  }
		  else
		  {
			  report.report("Specified activity was not found in good activity table");
			  return false;
		  }
		  return true;
 	 }
	
	private boolean navigatetoPatientInfoScreen(String firstlastname, String hic) {
		  WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		  moveToElement(tblcompletedactivity);
		  WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstlastname+"')]/preceding-sibling::td/a[text()='"+hic+"']"));
		  if(we!=null){
			  we.click();
		  }
		  else
		  {
			  report.report("Specified activity was not found in good activity table", Reporter.WARNING);
			  return false;
		  }
		  return true;
	 }
	
/*	private String getFirstLastName(Map<String,String> mapAttrVal){
		String lastname = mapAttrVal.get("Last Name");
		String firstname = mapAttrVal.get("First Name");
		String firstlastname = null;
		if(firstname==null || firstname.trim().equalsIgnoreCase(""))
			firstlastname = lastname;
		else
			firstlastname = lastname +", "+firstname;
		
		return firstlastname;
	}*/
	
	private int getActivitycount(String activitytablename){
		return Integer.parseInt(getElementText(By.xpath("//table[@id='activityTable']//td[@id='"+activitytablename+"']")));
	}
	
	private boolean verifyEligibilityRequestStatusPending(String lastname){
		//clickOnElement(ByLocator.id, "tdPendingActivity", 10);
		WebElement tblpendingactivity = waitForElementVisibility(By.id("tdPendingActivity"));
		moveToElement(tblpendingactivity);

		String firstlastname = Verify.getTableData("pendingActivity", 1, 5);
		if (firstlastname!=null && firstlastname.contains(lastname)){
			report.report("Submitted Patient Eligibility was found in pending table(Orange).", ReportAttribute.BOLD);
			return true;
		}
		else{
			report.report("Submitted Patient Eligibility was not found in pending table(Orange).", Reporter.WARNING);
			return false;
		}
	}
	
	private boolean verifyEligibilityRequestStatusCompleted(String lastname){
			
			WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
			moveToElement(tblcompletedactivity);

			String firstlastname = Verify.getTableData("goodActivity", 1, 5);
			if (firstlastname!=null && firstlastname.contains(lastname)){
				report.report("Submitted Patient Eligibility was found in Good Activity table(Green).", ReportAttribute.BOLD);
				return true;
			}
			else{
				report.report("Submitted Patient Eligibility was not found in Good Activity table(Green).", Reporter.WARNING);
				return false;
			}
	}
	
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		if(!isTextPresent("Enter the patient search information below")){
			HomePage.getInstance().navigateTo(Menu.ELIGIBILITY, null);
			clickLink("Eligiblity Check");
		}
	}
}
