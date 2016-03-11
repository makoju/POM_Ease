package com.ability.ease.eligibility;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.report.ReporterHelper;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();

		if(status.equalsIgnoreCase("pending")){
			if(!verifyEligibilityRequestStatusPending(firstlastname.toUpperCase()))
				return false;
		}
		else if(status.equalsIgnoreCase("completed")){
			if(!verifyEligibilityRequestStatusCompleted(firstlastname.toUpperCase()))
				return false;
		}
		
		else if(status.equalsIgnoreCase("failed")){
			if(!verifyEligibilityRequestStatusFailed(firstlastname.toUpperCase()))
				return false;
		}
		return true;
	}

	public boolean navigatetoClaimDetails(String firstname, String lastname, String hic) throws Exception{
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
		navigateToPage();
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
	
	public boolean acknoweldgeEligibility(String firstname, String lastname) throws Exception{
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
		navigateToPage();
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
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
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
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
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
		
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
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
	
	public boolean verifyOptionsUnderPendingActivityLogScreen() throws Exception {
		int failurecount=0;

		navigateToPage();
		WebElement tdPendingActivity = waitForElementVisibility(By.id("tdPendingActivity"));
		if(tdPendingActivity!=null)
			tdPendingActivity.click();
		
		//Check Agency Selected
		WebElement agency = waitForElementToBeClickable(ByLocator.id, "UserProvIDs", 60);
		if(!isOptionSelected(agency, "All")){
			report.report("Failed to verify Agency Option Selected under Pending Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
	
		//Check Status Selected
		WebElement status = waitForElementToBeClickable(ByLocator.id, "UserStatus", 60);
		if(!isOptionSelected(status, "Pending")){
			report.report("Failed to verify Status Option Selected under Pending Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
				
		//Check Type Selected
		WebElement usertype = waitForElementToBeClickable(ByLocator.id, "UserType", 60);
		if(!isOptionSelected(usertype, "All")){
			report.report("Failed to verify Type Option Selected under Pending Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}

		if(!validateFromTODateNotAcknowledgeOptions())
		{
			report.report("Failed to Verify FromDate, Todate and not acknowledge Options states in Pending Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		//TODO - Need to Implement code for verifying Search and Print Options 

		return failurecount==0?true:false;
	}
	
	public boolean verifyOptionsUnderCompletedActivityLogScreen() throws Exception {
		int failurecount=0;

		navigateToPage();
		WebElement tdGoodActivity = waitForElementToBeClickable(ByLocator.id,"tdGoodActivity",60);
		if(tdGoodActivity!=null)
			tdGoodActivity.click();
		
		//Check Agency Selected
		WebElement agency = waitForElementToBeClickable(ByLocator.id, "UserProvIDs", 60);
		if(!isOptionSelected(agency, "All")){
			report.report("Failed to verify agency Option Selected under Completed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		//Check Status Selected
		WebElement status = waitForElementToBeClickable(ByLocator.id, "UserStatus", 60);
		if(!isOptionSelected(status, "Completed")){
			report.report("Failed to verify Status Option Selected under Completed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		//Check Type Selected
		WebElement usertype = waitForElementToBeClickable(ByLocator.id, "UserType", 60);
		if(!isOptionSelected(usertype, "All")){
			report.report("Failed to verify type Option Selected under Completed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		if(!validateFromTODateNotAcknowledgeOptions())
		{
			report.report("Failed to Verify FromDate, Todate and not acknowledge Options states in Completed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}

		//TODO - Need to Implement code for verifying Search and Print Options 

		return failurecount==0?true:false;
	}

	public boolean verifyOptionsUnderFailedActivityLogScreen() throws Exception {
		int failurecount=0;

		navigateToPage();
		WebElement tdFailedActivity = waitForElementVisibility(By.id("tdFailedActivity"));
		if(tdFailedActivity!=null)
			tdFailedActivity.click();
		
		//Check Agency Selected
		WebElement agency = waitForElementToBeClickable(ByLocator.id, "UserProvIDs", 60);
		if(!isOptionSelected(agency, "All")){
			report.report("Failed to verify agency Option Selected under Failed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		//Check Status Selected
		WebElement status = waitForElementToBeClickable(ByLocator.id, "UserStatus", 60);
		if(!isOptionSelected(status, "Failed")){
			report.report("Failed to verify Status Option Selected under Failed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		//Check type Selected
		WebElement usertype = waitForElementToBeClickable(ByLocator.id, "UserType", 60);
		if(!isOptionSelected(usertype, "All")){
			report.report("Failed to verify type Option Selected under Failed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
			
		if(!validateFromTODateNotAcknowledgeOptions())
		{
			report.report("Failed to Verify FromDate, Todate and not acknowledge Options states in Failed Activity Log Screen", Reporter.WARNING);
			failurecount++;
		}
		
		//TODO - Need to Implement code for verifying Search and Print Options 

		return failurecount==0?true:false;
	}
	
	public int getActivityCount(String status) {
		int count=0;
		if(status.equalsIgnoreCase("pending"))
			count = getActivitycount("tdPendingActivity");
		
		else
			if(status.equalsIgnoreCase("completed"))
			count = getActivitycount("tdGoodActivity");
			
		else
			if(status.equalsIgnoreCase("failed"))
			count = getActivitycount("tdFailedActivity");
		else
		{
			report.report("Invalid Status option: "+status, Reporter.FAIL);
		}
		
		return count;
	}
	
	
	//Helper methods
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
		
	private int getActivitycount(String activitytablename){
		return Integer.parseInt(getElementText(By.xpath("//table[@id='activityTable']//td[@id='"+activitytablename+"']")));
	}
	
	private boolean verifyEligibilityRequestStatusPending(String lastname){
		WebElement tblpendingactivity = waitForElementVisibility(By.id("tdPendingActivity"));
		moveToElement(tblpendingactivity);

		String firstlastname = Verify.getTableData("pendingActivity", 1, 5);
		if (firstlastname!=null && firstlastname.toLowerCase().contains(lastname.toLowerCase())){
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
			if (firstlastname!=null && firstlastname.toLowerCase().contains(lastname.toLowerCase())){
				report.report("Submitted Patient Eligibility was found in Good Activity table(Green).", ReportAttribute.BOLD);
				return true;
			}
			else{
				report.report("Submitted Patient Eligibility was not found in Good Activity table(Green).", Reporter.WARNING);
				return false;
			}
	}
	
	
	private boolean verifyEligibilityRequestStatusFailed(String lastname) {
		WebElement tblfailedactivity = waitForElementVisibility(By.id("tdFailedActivity"));
		moveToElement(tblfailedactivity);

		String firstlastname = Verify.getTableData("failedActivity", 1, 5);
		if (firstlastname!=null && firstlastname.toLowerCase().contains(lastname.toLowerCase())){
			report.report("Submitted Patient Eligibility was found in Failed Activity table(Red).", ReportAttribute.BOLD);
			return true;
		}
		else{
			report.report("Submitted Patient Eligibility was not found in Failed Activity table(Red).", Reporter.WARNING);
			return false;
		}
	}
	
	public boolean isOptionSelected(WebElement select, String option){
		if(select!=null){
			Select agencyselect = new Select(select);
			List<WebElement> SelectedOptions = agencyselect.getAllSelectedOptions();
			if(SelectedOptions!=null && SelectedOptions.size()>1){
				report.report("Only one option should be in selected state. But "+SelectedOptions.size()+ " were selected", Reporter.WARNING);
				return false;
			}
			else if(SelectedOptions!=null && SelectedOptions.get(0).getText().equals(option.trim())){
				report.report("Optioin Selected is "+option.trim(), ReportAttribute.BOLD);
				return true;
			}
			else{
				report.report("Seems No Agency selected", Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Given Select DropDown is null", Reporter.WARNING);
			return false;
		}
	}
	public boolean validateFromTODateNotAcknowledgeOptions(){
		int failurecount=0;
		WebElement fromdate = waitForElementVisibility(By.id("from"));
		WebElement todate = waitForElementVisibility(By.id("to"));
		if(fromdate!=null && fromdate.isEnabled())
			report.report("Edit Box fromdate is enabled", ReportAttribute.BOLD);
		else{
			report.report("edit box From Date was either not found or not enabled", Reporter.WARNING);
			failurecount++;
		}

		if(todate!=null && todate.isEnabled())
			report.report("Edit Box Todate is enabled", ReportAttribute.BOLD);
		else{
			report.report("edit box To Date was either not found or not enabled", Reporter.WARNING);
			failurecount++;
		}

		WebElement onlynotack = waitForElementVisibility(By.id("non_ack"));
		if(onlynotack!=null && onlynotack.isSelected())
			report.report("Check Box Only not ack is selected", ReportAttribute.BOLD);
		else{
			report.report("Check Box Only not ack was not selected", Reporter.WARNING);
			failurecount++;
		}
		
		return failurecount==0?true:false;
	}
	
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("Eligiblity Check") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.ELIGIBILITY, null);
		}
		if(!isTextPresent("Enter the patient search information below"))
			clickLink("Eligiblity Check");
	}
}
