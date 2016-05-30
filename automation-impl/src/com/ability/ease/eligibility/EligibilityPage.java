package com.ability.ease.eligibility;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.report.ReporterHelper;
import junit.framework.Assert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.MySQLDBUtil;
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

	/*public boolean verifyEligibilityStatus(String firstname, String lastname, String status) throws Exception {
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
	}*/

	/*
	 * added by nageswar.bodduri
	 */
	public boolean verifyEligibilityStatus(String firstname, String lastname, String status) throws Exception {
		navigateToPage();
		//validation
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();

		if(status.equalsIgnoreCase("pending")){
			if(!verifyEligibilityRequestStatusPending(firstlastname.toUpperCase()))
				return false;
		}
		else if(status.equalsIgnoreCase("completed")){
			if(!verifyEligibilityRequestStatusCompleted(firstname))
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

		//String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
		String firstnamesuffix = firstname.replaceAll("[^0-9]", "");

		navigateToPage();
		WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		//Acknowledge the Request - To do - we've to get the first record from the table not any record matches with the given name
		int goodactivitycountprev = getActivitycount("tdGoodActivity");

		moveToElement(tblcompletedactivity);
		WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstnamesuffix+"')]/../td[1]/a"));
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

		String firstnamesuffix = firstname.replaceAll("[^0-9]", "");
		//String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();
		if(!verifyEligibilityRequestStatusCompleted(firstnamesuffix))
			return false;

		//Handle the report link of Eligibility Check Request and data validation
		String expectedreportheader = "ELIGIBILITY CHECK REPORT";
		if(!navigatetoEligibilityReport(firstnamesuffix))
			return false;

		//WebElement reportheader = waitForElementVisibility(By.xpath("//td[@class='headergreen']"));
		WebElement reportheader = waitForElementToBeClickable(ByLocator.xpath, "//td[@class='headergreen']", 30);
		if(reportheader!=null && !Verify.StringEquals(reportheader.getText(),expectedreportheader)){
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
		if(!(waitForElementVisibility(By.xpath("//span[text()='Other HHA Information']"))!=null)){
			report.report("Unable to find General Patient Information Section", Reporter.WARNING);
			failurecount++;
		}
		if(!(waitForElementVisibility(By.xpath("//span[text()='Hospice Information']"))!=null)){
			report.report("Unable to find Add to HMO Catcher link", Reporter.WARNING);
			failurecount++;
		}

		/*if(!Verify.isElementPresent(By.xpath("//pre/h2[contains(text(), 'Agency: "+agency+"')]"))){
			  report.report("Unable to find agency information in Eligibility Check Report. Expected Agency: "+agency, Reporter.WARNING);
			  failurecount++;
		}
		if(!Verify.isElementPresent(By.xpath("//pre/h2[contains(text(), 'HIC: "+hic+"')]"))){
			  report.report("Unable to find hic information in Eligibility Check Report. Expected HIC: "+hic, Reporter.WARNING);
			  failurecount++;
		}*/
		return failurecount==0?true:false;
	}

	public boolean verifyNavigationToUB04FromPatientInfoScreen(String hic,String agency, String firstname, String lastname) throws Exception {
		int failurecount=0;
		navigateToPage();
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();

		if(!navigatetoPatientInfoScreen(firstlastname, hic))
			return false;
		
		clickOnElement(ByLocator.id, "reportNewUB04", 20);

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

	public boolean verifyNavigationToHomeScreenFromPatientInfoScreen(String hic) throws Exception {

		navigateToPage();
		if(!navigatetoPatientInfoScreen(hic))
			return false;
		clickLink("reporthome");

		Thread.sleep(5000);

		if(isTextPresent("OVERNIGHT SUMMARY REPORT"))
		{
			report.report("Successfully navigated to home page", ReportAttribute.BOLD);
			return true;
		}
		else
		{
			report.report("Failed to navigate to home page", Reporter.WARNING);
			return false;
		}
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


	public boolean verifyOptionsUnderPatientInformationScreen(String hic, String firstname, String lastname) throws Exception {
		int failurecount=0;
		String firstlastname = (firstname==null || firstname.trim().equalsIgnoreCase(""))? lastname.toUpperCase(): (firstlastname = lastname +", "+firstname).toUpperCase();

		if(!navigatetoPatientInfoScreen(firstlastname, hic))
			return false;
		/*Verify  the following options
		1.Patient information
		2.Latest HMO Plans
		3.MSP information
		4.Show Previous eligibility checks link
		5.Expand Eligibility results link
		6.View271 link
		7.Refresh Data
		8. Admissions*/

		//1.Patient information
		String headertext = getElementText(By.className("headergreen"));
		if (headertext!=null && headertext.contains("PATIENT INFORMATION")){
			report.report("Header Patient Information is found", ReportAttribute.BOLD);
		}
		else{
			report.report("Header Patient Information not found", Reporter.WARNING);
			failurecount++;
		}
		//TODO - Yet to verify the following options; Seems to be these options doesn't appear for Eligibility Check requests but for claim Request we can see these
		//2. HMO Plans
		//3. MSP Information
		//8. Admissions

		//4.Show Previous eligibility checks link
		WebElement showprevelig = waitForElementVisibility(By.partialLinkText("Show previous eligibility checks"));
		if(showprevelig!=null)
		{
			report.report("Show previous eligibility checks link is found", ReportAttribute.BOLD);
			showprevelig.click();
			WebElement preveligcheckreqtable = waitForElementVisibility(By.id("datatable"));
			if(preveligcheckreqtable == null){
				report.report("Unable to find previously submitted eligibility checks", Reporter.WARNING);
				failurecount++;
			}
		}
		else{
			report.report("Show previous eligibility checks link not found", Reporter.WARNING);
			failurecount++;
		}

		//5. + Expand Eligibility Results
		WebElement expandeligresults = waitForElementVisibility(By.partialLinkText("Expand Eligibility Results"));
		if(expandeligresults!=null)
		{
			report.report("Expand Eligibility Results link is found", ReportAttribute.BOLD);
			expandeligresults.click();
			WebElement preveligcheckreqtable = waitForElementVisibility(By.id("collapseResults"));
			if(preveligcheckreqtable!=null){
				report.report("unable to find previous eligibility results", Reporter.WARNING);
				failurecount++;
			}
			String[] expectedHeaders = {"Insured/Subscriber Information", "Provider Information", "Eligibility", "Deductibles / Caps", "Plan Coverage"};
			String[] actualHeaders;
			int i=0;

			List<WebElement> lseligresultsheaders = findElements(By.className("resultHeading"));
			actualHeaders = new String[lseligresultsheaders.size()];

			for(WebElement we:lseligresultsheaders)
				actualHeaders[i++] = we.getText();

			if(!Verify.verifyArrayofStrings(actualHeaders, expectedHeaders, true)){
				report.report("Actual and expected headers of eligibility results not matched", Reporter.WARNING);
				failurecount++;
			}

		}
		else{
			report.report("Expand Eligibility Results link not found", Reporter.WARNING);
			failurecount++;
		}

		//6. View 271
		WebElement  view271element = waitForElementVisibility(By.partialLinkText("View 271"));
		if(view271element!=null)
		{
			report.report("View 271 is found", ReportAttribute.BOLD);
			view271element.click();
			if(waitForElementVisibility(By.xpath("//span[text()='Raw X12 271 Response']"), 20)!=null)
			{
				report.report("271 response page is not displayed", Reporter.WARNING);
				failurecount++;
			}
		}
		else{
			report.report("View 271 link not found", Reporter.WARNING);
			failurecount++;
		}

		//7. Refresh Data
		if(waitForElementVisibility(By.partialLinkText("Refresh Data"))!=null)
		{
			report.report("Refresh Data is found", ReportAttribute.BOLD);
			//TODO - Need to Validate Refresh Data Functionality here
		}
		else{
			report.report("Refresh Data link not found", Reporter.WARNING);
			failurecount++;
		}

		return failurecount==0?true:false;
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

	public boolean navigatetoEligibilityReport(String firstlastname) {
		WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		if(tblcompletedactivity!=null){
			moveToElement(tblcompletedactivity);
			WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstlastname+"')]/following-sibling::td/a[text()='Report']"));
			if(we!=null){
				we.click();
			}
			else
			{
				report.report("Specified activity was not found in good activity table", Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Good Activity Table was not found", Reporter.WARNING);
			return false;
		}
		return true;
	}

	public boolean navigatetoPatientInfoScreen(String firstlastname, String hic) {

		/*
		 * added by nageswar.bodduri
		 */
		String firstnamesuffix = firstlastname.replaceAll("[^0-9]", "");

		WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		moveToElement(tblcompletedactivity);
		WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td[contains(text(),'"+firstnamesuffix+"')]/preceding-sibling::td/a[text()='"+hic+"']"));
		if(we!=null){
			we.click();
		}
		else
		{
			report.report("Specified activity was not found in good activity table", Reporter.WARNING);
			return false;
		}

		WebElement wepatientinfo = waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"));
		if(wepatientinfo!=null)
			report.report("Successfully navigated to Patient Information Screen", ReportAttribute.BOLD);
		else
		{
			report.report("Navigation to Patient Information Page has failed", Reporter.WARNING);
			return false;
		}

		return true;
	}

	public boolean navigatetoPatientInfoScreen(String hic){
		WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		moveToElement(tblcompletedactivity);
		WebElement we = waitForElementVisibility(By.xpath("//table[@id='goodActivity']//td/a[text()='"+hic+"']"));
		if(we!=null){
			we.click();
		}
		else
		{
			report.report("Specified activity with hic: "+hic+"was not found in good activity table", Reporter.WARNING);
			return false;
		}

		WebElement wepatientinfo = waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"));
		if(wepatientinfo!=null)
			report.report("Successfully navigated to Patient Information Screen", ReportAttribute.BOLD);
		else
		{
			report.report("Navigation to Patient Information Page has failed", Reporter.WARNING);
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

	private boolean verifyEligibilityRequestStatusCompleted(String firstname){

		WebElement tblcompletedactivity = waitForElementVisibility(By.id("tdGoodActivity"));
		moveToElement(tblcompletedactivity);

		String firstnamesuffix = firstname.replaceAll("[^0-9]", "");
		String firstlastname = Verify.getTableData("goodActivity", 1, 6);
		if (firstlastname!=null && firstlastname.contains(firstnamesuffix)){
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

	public boolean searchactivitylogByHIC(String status, String hic) throws Exception {
		navigateToPage();
		if(status.equalsIgnoreCase("completed"))
			clickButton("tdGoodActivity");
		else if(status.equalsIgnoreCase("failed"))
			clickButton("tdFailedActivity");
		else if(status.equalsIgnoreCase("pending"))
			clickButton("tdPendingActivity");

		else{
			report.report("Wrong Status option supplied", Reporter.WARNING);
			return false;
		}
		//move to search icon and enter HIC
		WebElement element = waitForElementVisibility(By.id("reportHICSearch"));
		if(element==null){
			report.report("HIC Search ICON is not found", Reporter.WARNING);
			return false;
		}
		moveToElement(element);
		typeEditBox("reportHICEntry", hic);
		clickButton("GO");

		return waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"))!=null?true:false;
	}

	//apadavala: A7 in response is not coming in time. So, It can't be verified right now as per srinivas Bandari. So, will consider later
	public boolean verifyView271RespPagehasA7() throws Exception{
		clickLink("View271");
		//TODO - Need to validate repsonse page with A7
		return false;
	}

	public boolean verifyActivityLogSearchOnlynotacknowledged() throws Exception {
		navigateToPage();
		clickButton("tdGoodActivity");
		if(!isChecked("non_ack"))
			checkChkBox("non_ack");
		clickButton("Search");
		//TODO - Need to add validation code for validating the not acknowledged eligibility checks
		uncheckChkBox("non_ack");
		clickButton("Search");
		//TODO - Need to add validation code for validating the acknowledged & non-acknowledged eligibility checks
		return false;
	}

	public boolean verifyNavigationToHomeScreenFromCompletedActivityLogScreen() throws Exception {
		navigateToPage();
		clickButton("tdGoodActivity");
		WebElement we = waitForElementVisibility(By.className("headerblue"));
		if(we!=null && we.getText().equalsIgnoreCase("COMPLETED ACTIVITY LOG"))
		{
			clickLink("reportHome");
			Thread.sleep(5000);

			if(isTextPresent("OVERNIGHT SUMMARY REPORT"))
			{
				report.report("Successfully navigated to home page", ReportAttribute.BOLD);
				return true;
			}
			else
			{
				report.report("Failed to navigate to home page", Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Unable to naviagte to Completed Activity Log Screen", Reporter.WARNING);
			return false;
		}
	}

	public boolean verifyPDFExportInCompletedActivityLogScreen() throws Exception{
		navigateToPage();
		clickButton("tdGoodActivity");
		WebElement we = waitForElementVisibility(By.className("headerblue"));
		if(we!=null && we.getText().equalsIgnoreCase("COMPLETED ACTIVITY LOG"))
		{
			WebElement pdfelement = waitForElementToBeClickable(ByLocator.xpath, "//tbody/tr[1]//a[contains(@href,'eligibility.pdf')]/img", 10);
			if(pdfelement == null)
			{
				report.report("PDF report link not found in completed activity log screen", Reporter.WARNING);
				return false;
			}
			else
			{
				//pdfelement.click();
				//need to validate whether report exported to PDF or not
				return false;
			}
		}
		else
		{
			report.report("Unable to naviagte to Completed Activity Log Screen", Reporter.WARNING);
			return false;
		}
	}


	public boolean verifyPrintOptionInCompletedActivityLogScreen() throws Exception {
		navigateToPage();
		clickButton("tdGoodActivity");
		WebElement we = waitForElementVisibility(By.className("headerblue"));
		if(we!=null && we.getText().equalsIgnoreCase("COMPLETED ACTIVITY LOG"))
		{
			WebElement printelement = waitForElementToBeClickable(ByLocator.xpath, "//tbody/tr[1]//a[contains(@href,'eligibility.print')]/img", 10);
			if(printelement == null)
			{
				report.report("Print option not found in completed activity log screen", Reporter.WARNING);
				return false;
			}
			else
			{
				//printelement.click();
				//need to validate whether Print function is working or not
				return false;
			}
		}
		else
		{
			report.report("Unable to naviagte to Completed Activity Log Screen", Reporter.WARNING);
			return false;
		}
	}

	public boolean verifyTrashOptionInCompletedActivityLogScreen() throws Exception{
		int failurecount=0;

		navigateToPage();
		clickButton("tdGoodActivity");
		WebElement we = waitForElementVisibility(By.className("headerblue"));
		if(we!=null && we.getText().equalsIgnoreCase("COMPLETED ACTIVITY LOG"))
		{
			clickLink("reportDelete");
			if(!verifyAlert("You must first check the checkboxes next to the specific items you want to select before you can proceed with this function.")){
				report.report("Expected alert not present", Reporter.WARNING);
				failurecount++;
			}
			else
			{
				String sExpectedAlertmessage = "Once you void an item, it is permanently voided and it cannot be reversed. Are you sure that you want to Void the selected items?If you do not wish to Void the selected items, then click \"Cancel\".";
				WebElement checkbox = waitForElementToBeClickable(ByLocator.xpath, "//tbody/tr[1]//td[1]/input[@type='checkbox']", 10);
				int prevtablesize = findElements(By.xpath("//table[@id='datatable']/tbody/tr")).size();

				if(checkbox!=null)
				{
					checkbox.click();
					clickLink("reportDelete");
					if(!verifyAlert(sExpectedAlertmessage)){
						report.report("Expected alert not present", Reporter.WARNING);
						failurecount++;
					}
					else
					{
						//check the size of the table it should be reduced by one
						int newsize = findElements(By.xpath("//table[@id='datatable']/tbody/tr")).size();
						Assert.assertTrue(newsize == prevtablesize-1);
					}
				}
				else
				{
					report.report("Completed Activity Log item1 check box not found", Reporter.WARNING);
					return false;
				}
			}

		}
		else
		{
			report.report("Unable to naviagte to Completed Activity Log Screen", Reporter.WARNING);
			return false;
		}
		return failurecount==0?true:false;
	}

	public boolean VerifyNavigationOfAdvanceSearchFromLiveSearch() throws Exception {
		navigateToPage();
		clickButton("tdGoodActivity");
		WebElement we = waitForElementVisibility(By.className("headerblue"));
		if(we!=null && we.getText().equalsIgnoreCase("COMPLETED ACTIVITY LOG"))
		{
			//move to search icon and enter HIC
			WebElement element = waitForElementVisibility(By.id("reportHICSearch"));
			if(element==null){
				report.report("HIC Search ICON is not found", Reporter.WARNING);
				return false;
			}
			moveToElement(element);
			clickLink("Advanced Search");
			WebElement advancedsearchheader = waitForElementVisibility(By.className("headerblue"));
			if(advancedsearchheader!=null && advancedsearchheader.getText().equalsIgnoreCase("ADVANCED SEARCH")){
				report.report("Successfully navigated to Advanced Search Screen", ReportAttribute.BOLD);
				return true;
			}
			else
			{
				report.report("Failed to navigate to Advanced Search Screen", Reporter.WARNING);
				return true;
			}
		}
		else
		{
			report.report("Unable to naviagte to Completed Activity Log Screen", Reporter.WARNING);
			return false;
		}
	}

	public boolean enableordisableHETS(String customername, String status) {
		String query = "update ddez.customer set useHets=";
		if(status.equalsIgnoreCase("enable"))
			query+=1;
		else
			query+=0;

		report.report("Running query to enable or disable hets user: "+ query, ReportAttribute.BOLD);

		int result = MySQLDBUtil.getUpdateResultFromMySQLDB(query);

		return result>0?true:false; 
	}


	public boolean enableandVerifyPsychiatricSTCforNPI(String contactname, String agencyDisplayName) throws Exception {

		String expectedalertmsg = "Warning: You are verifying that you are a Psychiatric/Mental Health provider.  Appropriate use of Psychiatric/Mental Health service type code information will be subject to CMS auditing.";
		HomePage.getInstance().navigateTo(Menu.Admin, null);

		WebElement eligconfig = waitForElementToBeClickable(ByLocator.linktext, "Eligibility Configuration", 15);
		eligconfig.click();

		if(!waitForTextVisibility(ByLocator.css, ".headerblue", "CHANGE ELIGIBILITY CONFIGURATION")){
			report.report("Unable to navigate to Eligibility Configuration page");
			return false;
		}

		//Get the NPI of an agency based on contactname, displayname
		String NPI="";
		int providerid=0;

		String sQueryName = "Select id,NPI from ddez.provider where DisplayName='"+agencyDisplayName+"' and customerid=(Select customerid from ddez.customer where contactname='"+contactname+"')";
		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(sQueryName);		

		while(rs.next()){
			providerid = rs.getInt(1);
			NPI = String.valueOf(rs.getInt(2));
		}

		//Get the total row count before enabling STC for an NPI
		ResultSet rs1 = MySQLDBUtil.getResultFromMySQLDB("SELECT count(*) FROM ddez.psychiatricproviderchange");
		int count=0;
		while(rs1.next())
			count = rs1.getInt(1);

		//Write the logic to check the NPI checkbox in the UI
		checkCheckbox(By.xpath("//td[span[text()='"+NPI+"']]/following-sibling::td/input[@type='checkbox']"));

		if(!verifyAlert(expectedalertmsg)){
			report.report("Expected Alert not present", Reporter.WARNING);
		}
		
		clickButtonV2("Submit");

		//Get the total row count after enabling STC for an NPI 
		ResultSet rs2 = MySQLDBUtil.getResultFromMySQLDB("SELECT count(*) FROM ddez.psychiatricproviderchange");

		while(rs2.next()){
			if(count+1 != rs2.getInt(1))
			{
				report.report("Expected count is"+ (count+1)+"But Actual count: "+rs2.getInt(1), Reporter.WARNING);
				return false;
			}
		}

		ResultSet rs3 = MySQLDBUtil.getResultFromMySQLDB("Select providerID, ChangedTo from ddez.psychiatricproviderchange order by id desc limit 1");
		
		int provid=0;
		boolean changedto=false;
		
		while(rs3.next()){
			provid = rs3.getInt(1);
			changedto = rs3.getBoolean(2);
		}
		
		return (provid == providerid && changedto)?true:false;
	}

		public boolean verifyMostBenefitSTC45Fields() {
			//Need to implement the code to get these fields data from EligibilityCheck report page
			return false;
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
