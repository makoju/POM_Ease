package com.ability.ease.mydde;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.tests.Status;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class MyDDEPage extends AbstractPageObject{

	ReportsHelper reportshelper = new ReportsHelper();

	

	public boolean verifyPageViewAndOptions() {

		int failurecount = 0;
		WebElement verifyAdvanced = waitForElementVisibility(By.id("reportComplexity"));
		if ( verifyAdvanced.getText().contains("Advanced")) {
			report.report("Displayed page content is in BASIC view");
		}else{
			report.report("Displayed page content is in Advanced view");
			failurecount++;
		}

		String[] options = {"reportHICSearch","reportHome","reportList","reportTimeframe","reportAgency","reportPieChart","reportPrint","reportExport",
				"reportNewUB04","reportComplexity"}; //skipped Add HICs

		for(String optionsId : options){

			WebElement verifyOptions = waitForElementVisibility(By.id(optionsId));
			if ( verifyOptions != null) {
				String optionsText = verifyOptions.getText().toUpperCase();
				if(!optionsText.equals("")){
					report.report("Successfully verified "+optionsText+" under Basic view.");
				}
				else{
					report.report("Successfully verified "+optionsId+" under Basic view.");
				}
			}else{
				report.report("Failed to verify the options under Basic view.");
				failurecount++;
			}
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderReportsForHHA() throws Exception {

		int failurecount = 0;
		String[] expected = {"ADR","RAPs At Risk","Stuck In Suspense","Eligibility Issues","T Status Report"};

		/*//Hover on reports and get the links by clicking on each sub link
		boolean compare = reportshelper.verifySubMenuLinksAndClick(expected, "Reports", "reportListMenu");
		if(compare){
			report.report("Actual and expected values are equal.");
		}
		else{
			failurecount++;
		}

		//Read timeframe
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		//For Sorting in Overnight Summary Report
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 5))
			failurecount++;

		//Loop through ADR,RAP's at Risk,Stuck In Suspense & Eligibility Issues
		for(int i=0;i<4;i++){
			String headerText = getElementText(By.xpath("//td[@class='headerblue']"));
			((JavascriptExecutor) driver).executeScript("$('#Report_"+i+"').click();");
			//Column Sorting
			if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
				failurecount++;
		}*/

		//Click on T-Status Report
		((JavascriptExecutor) driver).executeScript("$('#Report_4').click();");

		String expectedText = "ADVANCED SEARCH";
		String advancedSearchText = getElementText(By.xpath("//td[@class='headerblue'"));
		if(advancedSearchText.equals(expectedText)){
			report.report("User is in ADVANCED SEARCH page.");
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderTimeframe() throws Exception {

		int failurecount = 0;
		String[] expected = {"Overnight","Weekly"};

		boolean compare = reportshelper.verifySubMenuLinksAndClick(expected, "Timeframe", "timeframeListMenu");
		if(compare){
			report.report("Actual and expected values are equal.");
		}
		else{
			failurecount++;
		}
		//verify From and To

		//To-Do

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderAgency(String agency) throws Exception {
		int failurecount = 0;
		String[] expectedFromJsystem = agency.split(",");
		String[] actual = selectGetOptions("reportAgencySelect");
		
		if (!Verify.verifyArrayofStrings(actual, expectedFromJsystem, true))
			failurecount++;
		
		return failurecount == 0 ? true : false;
	}

	public boolean verifySaveProfileOption() throws Exception {

		int failurecount = 0;
		String saveProfileNameAS = "Search Result";
		//To-Do mouse hover on Live Search option.....then click on Advanced Search
		Thread.sleep(5000);
		((JavascriptExecutor) driver).executeScript("$('#reportAdvanceSearch').click();");
		selectByNameOrID("UserProvider","1013999473");
		selectByNameOrID("UserProvider","1245347632");
		checkChkBox("SaveProfile");
		typeEditBox("SaveProfileName",saveProfileNameAS);
		clickButtonV2("submit");
		if(verifyAlert("OK to overwrite profile Search Result?")){
			report.report("Save Profile is overwritten.");
		}
		Thread.sleep(5000);	
		report.report("SEARCH RESULTS RECORD COUNT :"+getElementText(By.id("totalRows")));
		clickLinkV2("backNav"); //Hitting back button 5 times, because of while loop in this method

		List<WebElement> savedProfileOptions = driver.findElements(By.xpath("//select[@id='UserProvider']/option"));
		for(WebElement option : savedProfileOptions){
			if(option.getText().equals(saveProfileNameAS)){
				report.report("Verify the Saved Profile details.");
			}else{
				report.report("Failed to verify the Saved Profile details.");
				failurecount++;
			}
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderReportsForNonHHA() {
		int failurecount = 0;
		
		
		
		
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTimeFrameOptionsUnderAdvanced() throws Exception {
		int failurecount = 0;
		String[] expected = {"Overnight","Weekly","All (up to 18 mos ago)"};
		//Click Advanced using Advanced building block
		waitForElementVisibility(By.id("reportTimeframe"));

		boolean compare = reportshelper.verifySubMenuLinksAndClick(expected, "Timeframe", "timeframeListMenu");
		if(compare){
			report.report("Actual and expected values are equal.");
		}
		else{
			failurecount++;
		}
		((JavascriptExecutor) driver).executeScript("$('#reportAll').click();");
		//How to verify that data displayed is under 18 months timespan
		//Custom dates verify
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvanceSearchForMAXFields(String hic,String monthsAgo,Status status,String location) throws Exception {
		List<String> checkboxNames = Arrays.asList("ClaimRap", "ClaimFinal", "ClaimOther");

		Thread.sleep(5000);
		((JavascriptExecutor) driver).executeScript("$('#reportAdvanceSearch').click();");

		selectByNameOrID("UserProvider","All");
		typeEditBox("HIC", hic);
		checkCheckboxes(checkboxNames);
		typeEditBox("ClaimSubmittedLookbackMonths", monthsAgo);
		selectByNameOrID("Status", status.getStatus());
		typeEditBox("Location", location);
		clickButtonV2("submit");
		Thread.sleep(5000);
		WebElement billed = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[14]/div"));
		WebElement reimb = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[15]/div"));
		return (billed!=null && reimb!=null) ? true : false;
	}

	public boolean verifyAdvancedSearchForStatusLocation() throws Exception {
		
		Thread.sleep(5000);
		((JavascriptExecutor) driver).executeScript("$('#reportAdvanceSearch').click();");

		selectByNameOrID("UserProvider","All");
		selectByNameOrID("Status", "Suspense");
		typeEditBox("Location", "B6001");
		clickButtonV2("submit");
		Thread.sleep(5000);
		WebElement dueDate = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[16]/div"));
		WebElement dayDueDate = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[17]/div"));
		WebElement daysLeft = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[18]/div"));
		
		return (dueDate!=null && dayDueDate!=null && daysLeft!=null) ? true : false;
	}
	
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYDDE, null);
	}
	
	
}
