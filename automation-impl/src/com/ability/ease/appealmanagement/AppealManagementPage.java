package com.ability.ease.appealmanagement;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

/**
 * 
 * @author abhilash.chavva
 *
 */

public class AppealManagementPage extends AbstractPageObject{

	public boolean verifyValidationsUnderViewNotes(String monthsAgo, String notes) throws Exception {
		int failurecount = 0;
		WebElement tagLabel = null, notesLabel = null, submitButton = null;

		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		Thread.sleep(5000);
		boolean isPresent = driver.findElements(By.linkText(elementprop.getProperty("SEARCH_RESULTS_VIEW_TAGS_LINKTEXT"))).size() > 0;
		if(!isPresent){
			report.report("View tags link identified.....",ReportAttribute.BOLD);
			driver.findElement(By.xpath(elementprop.getProperty("SEARCH_RESULTS_ADD_TAG_IMAGE_XPATH"))).click();
			WebElement notesTextArea = waitForElementToBeClickable(ByLocator.xpath, "//div[@id='add-claim-tag']/descendant::textarea[@id='ClaimTagNoteTextId']", 30); 
			typeEditBoxByWebElement(notesTextArea, notes);
			clickOnElement(ByLocator.xpath, "//div[@id='add-claim-tag']/descendant::button[text()='Submit']", 30);
		}
		driver.findElement(By.linkText(elementprop.getProperty("SEARCH_RESULTS_VIEW_TAGS_LINKTEXT"))).click();
		waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("VIEW_NOTES_UNDER_CLAIM_TAGGING_HISTORY_LINKTEXT"), 30);
		clickLink(elementprop.getProperty("VIEW_NOTES_UNDER_CLAIM_TAGGING_HISTORY_LINKTEXT"));
		tagLabel = driver.findElement(By.xpath(elementprop.getProperty("TAG_LABEL_VIEW_NOTES_XPATH")));
		notesLabel = driver.findElement(By.xpath(elementprop.getProperty("NOTES_LABEL_VIEW_NOTES_XPATH")));
		submitButton = driver.findElement(By.xpath(elementprop.getProperty("SUBMIT_BUTTON_VIEW_NOTES_XPATH")));
		if(tagLabel != null && notesLabel!=null && submitButton!=null){
			report.report("Labels TAG, NOTES $ Submit button are displayed under view notes popup", ReportAttribute.BOLD);
			WebElement notesTextArea = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("NOTES_TEXTAREA_VIEW_NOTES_XPATH"), 30); 
			typeEditBoxByWebElement(notesTextArea, notes);
			clickOnElement(ByLocator.xpath, elementprop.getProperty("SUBMIT_BUTTON_VIEW_NOTES_XPATH"), 30);
			waitForElementVisibility(By.xpath("//table[@id='claimTagsInfoTable']/descendant::td[text()='"+notes+"']"), 30);
			typeEditBoxByWebElement(notesTextArea, "!");
			clickOnElement(ByLocator.xpath, elementprop.getProperty("SUBMIT_BUTTON_VIEW_NOTES_XPATH"), 30);
			if(isTextPresent("Only letters, digits, spaces, new line and _ . , @ $ ? * / - special characters are allowed.")){
				Thread.sleep(5000);
				report.report("User received the validation message Only letters, digits, spaces, new line and _ . , @ $ ? * / - special characters are allowed.",ReportAttribute.BOLD);
				driver.findElement(By.xpath("/html/body/div[7]/div/a/span")).click();
			}
			else{
				failurecount++;
			}
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier() throws Exception {
		int failurecount = 0; List<String> tagsUnderClaimTags = new ArrayList<String>();
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), "26");
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		Thread.sleep(5000);
		driver.findElement(By.linkText(elementprop.getProperty("SEARCH_RESULTS_VIEW_TAGS_LINKTEXT"))).click();

		List<WebElement> claimTags = driver.findElements(By.xpath("//div[@id='claimTagsId']/div/label"));
		for(WebElement claimTag : claimTags){
			tagsUnderClaimTags.add(claimTag.getText());
		}
		clickButtonV2(elementprop.getProperty("ADD_TAG_BUTTON_TEXT"));
		//		List<WebElement> tagsUnderAddTag = driver.findElements(By.xpath("//select[@id='ClaimTagNameDispInputID']"));
		// pending   ---- selectGetOptions("ClaimTagNameDispInputID");
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTimeFrameOptionsFunctionalityUnderAppealsReport() throws Exception {
		String actualheadertext=""; 
		int failurecount=0;

		navigateToPage();
		setTimeFrame("Overnight");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "OVERNIGHT LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FOR.*, FOR AGENCY.*"))
			failurecount++;

		setTimeFrame("Weekly");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "WEEKLY LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FROM.*, FOR AGENCY.*"))
			failurecount++;

		setTimeFrame("All (up to 18 mos ago)");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FROM.*, FOR AGENCY.*"))
			failurecount++;

		setTimeFrame("FromDate(03/03/2014):ToDate(03/03/2016)");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FROM 03/03/2014 TO 03/03/2016, FOR AGENCY.*"))
			failurecount++;

		return failurecount==0?true:false;

	}

	public boolean verifyMenuOptionsAvailableUnderAppealsReport() throws Exception {
		int failurecount=0;
		navigateToPage();
		if(waitForElementVisibility(By.id("reportHICSearchList"))==null)
		{
			report.report("Unable to find HIC Search Icon", Reporter.WARNING);
			failurecount++;
		}
		if(waitForElementVisibility(By.id("timeframeList"))==null)
		{
			report.report("Unable to find Time Frame List Menu Option", Reporter.WARNING);
			failurecount++;
		}
		if(waitForElementVisibility(By.id("reportAgency"))==null)
		{
			report.report("Unable to find Agency List Menu", Reporter.WARNING);
			failurecount++;
		}

		if(waitForElementVisibility(By.id("reportPrint"))==null)
		{
			report.report("Unable to find Report Print Option", Reporter.WARNING);
			failurecount++;
		}

		if(waitForElementVisibility(By.id("reportExport"))==null)
		{
			report.report("Unable to find Report Export Menu Option", Reporter.WARNING);
			failurecount++;
		}

		return failurecount==0?true:false;				
	}


	public boolean verifyHICSearchOptionUnderAppealsReport() throws Exception{
		int failurecount=0;

		navigateToPage();
		//Verify HIC Search Option
		WebElement reporthicsearch = waitForElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH")));
		if(reporthicsearch==null)
		{
			report.report("HIC Search element not found", Reporter.WARNING);
			return false;
		}

		moveToElement(reporthicsearch);
		typeEditBox("reportHICEntry", "000002936A");
		clickButton("GO");
		
		Thread.sleep(3000);
		WebElement patientinformationheader = waitForElementVisibility(By.cssSelector(".headergreen"));
		if(patientinformationheader!=null){
			String headertText = patientinformationheader.getText();
			report.report("Verifying that user navigates to Patient Information Screen");
			if(!Verify.StringMatches(headertText, "PATIENT INFORMATION.*")){
				failurecount++;
				report.report("Unable to navigate to Patient Information Screen", Reporter.WARNING);
			}
		}
		else{
			report.report("Unable to navigate to patient information screen with the providerd hic: 000002936A", Reporter.WARNING);
			failurecount++;
		}
		//Verify HIC AdvacedSearch link
		reporthicsearch = waitForElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH")));
		moveToElement(reporthicsearch);
		clickLink("Advanced Search");
		Thread.sleep(3000);
		WebElement advancesearchheader = waitForElementVisibility(By.cssSelector(".headerblue"));
		if(advancesearchheader!=null){
			String headertText1 = advancesearchheader.getText();
			if(!Verify.StringMatches(headertText1, "ADVANCED SEARCH")){
				failurecount++;
				report.report("Unable to navigate to Advanced Search Screen", Reporter.WARNING);
			}

			//Check the Back Navigation
			clickOnElement(ByLocator.xpath, "//a[@id='backNav']", 20);
			String actualheadertext = getAppealClaimsreportHeaderMessage();
			if(!Verify.StringMatches(actualheadertext, "OVERNIGHT LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FOR .*, FOR AGENCY .*"))
			{
				failurecount++;
				report.report("Unable to navigate to Appeal Claims ESMD Report", Reporter.WARNING);
			}
		}
		else
		{
			report.report("Unable to navigate to Advance Search screen", Reporter.WARNING);
			failurecount++;
		}

		return failurecount==0?true:false;
	}

	public boolean verifyAgencyOptionUnderAppealsReport() throws Exception {
		navigateToPage();
		moveToElement("Agency");
		WebElement selectelement = waitForElementVisibility(By.id("reportAgencySelect"));
		if(selectelement!=null){
			Select select = new Select(selectelement);
			select.selectByIndex(0);
			String selectedagency = select.getFirstSelectedOption().getText().trim();
			clickButton("Change Agency");
			String actualheadertext = getAppealClaimsreportHeaderMessage();
			return Verify.StringMatches(actualheadertext, ".*LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FOR.*, FOR AGENCY "+selectedagency);
		}
		else
		{
			report.report("Agency Select Drop Down not visible", Reporter.WARNING);
			return false;
		}
	}


	/**
	 * Supply the TimeFrame Values like {Overnight, Weekly, All
			(up to 18 mos ago), FromDate(MM/DD/YYYY):ToDate(MM/DD/YYYY)}
	 * @param timeframe
	 * @throws Exception
	 */
	public void setTimeFrame(String timeframe) throws Exception{
		moveToElement(elementprop.getProperty("TIMEFRAME_LIST_MENU"));
		timeframe = timeframe.trim();
		//if the timeframe value starts with fromdate then user is going to set from and todate 
		if (timeframe.toLowerCase().startsWith("fromdate")){
			String fromdate="",todate="";
			String[] dates = timeframe.split(":");
			//getting the from and todate from the user supplied date string say, "fromdate(MM//DD//YYYY):todate(MM//DD//YYYY)"
			if(dates.length>1){
				fromdate = dates[0].substring(dates[0].indexOf("(")+1, dates[0].indexOf(")"));
				todate = dates[1].substring(dates[1].indexOf("(")+1, dates[1].indexOf(")"));;
			}
			/*typeEditBox("reportCustomDateFrom", fromdate);
			typeEditBox("reportCustomDateTo", todate);
			clickButton("reportTimeframeButton");*/
			((JavascriptExecutor) driver).executeScript("$('#reportCustomDateFrom').val('"+fromdate+"');");
			((JavascriptExecutor) driver).executeScript("$('#reportCustomDateTo').val('"+todate+"');");
			((JavascriptExecutor) driver).executeScript("$('#reportTimeframeButton').click();");
		}
		else
			clickLink(timeframe);
	}

	public String getAppealClaimsreportHeaderMessage(){
		//wait for 3 sec for the header to change and appear
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement we = waitForElementVisibility(By.cssSelector(".headerblue"));
		if(we!=null)
			return we.getText();
		return "";
	}

	@Override
	public void assertInPage() {
	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("LEVEL 1 APPEAL CLAIMS") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.ESMD, null);
			WebElement appealSubmission = waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("APPEAL_SUBMISSION_LINKTEXT"), 30);
			if(appealSubmission != null){
				clickLink(elementprop.getProperty("APPEAL_SUBMISSION_LINKTEXT"));
				Thread.sleep(5000);
			}
		}
	}
}