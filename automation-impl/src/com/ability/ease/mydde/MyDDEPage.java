package com.ability.ease.mydde;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.ReporterHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

/**
 * 
 * @author abhilash.chavva
 *
 */

public class MyDDEPage extends AbstractPageObject{

	ReportsHelper reportshelper = new ReportsHelper();

	public boolean verifyPageViewAndOptionsUnderBasicView() throws Exception {

		int failurecount = 0;
		navigateToPage();
		WebElement verifyAdvanced = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("PAGE_VIEW_XPATH"), 30);
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

	public boolean verifyColumnsAndSorting(String reportText, String expectedColumnsFromJSystem, String columnName, int columnIndex) throws Exception {

		int failurecount = 0;String[] expectedColumns = null;
		if(expectedColumnsFromJSystem != null){
			expectedColumns = expectedColumnsFromJSystem.split(",");
		}
		navigateToPage();
		WebElement verifyAdvanced = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("PAGE_VIEW_XPATH"), 30);
		if ( verifyAdvanced.getText().contains("Basic")) {
			safeJavaScriptClick("Basic");
		}
		Thread.sleep(3000);
		moveToElement("Reports");
		clickLink(reportText);
		if(Verify.verifyTableColumnNames("datatable",expectedColumns)){
			if(!isTextPresent("EASE found no items for this report")){
				if (!Verify.validateTableColumnSortOrder("datatable", columnName, columnIndex))
					failurecount++;
			}
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderReportsForHHA_NonHHA(String fromDate,String toDate, String expectedReportsFromJsystem, String agency,String expOvernightColumnsFromJSystem) throws Exception {

		int failurecount = 0;
		navigateToPage();
		String[] expectedReports = expectedReportsFromJsystem.split(",");
		String[] expOvernightColumns = expOvernightColumnsFromJSystem.split(",");
		Thread.sleep(3000);
		WebElement verifyAdvanced = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("PAGE_VIEW_XPATH"), 30);
		if ( verifyAdvanced.getText().contains("Basic")) {
			safeJavaScriptClick("Basic");
		}
		Thread.sleep(3000);

		moveToElement(elementprop.getProperty("AGENCY_LINKTEXT"));
		selectByNameOrID(elementprop.getProperty("AGENCY_HOVER_DROPDOWN_ID"), agency); 
		clickButton(elementprop.getProperty("CHANGE_AGENCY_BUTTON_VALUE"));
		String[] actual = reportshelper.verifySubMenuLinks(expectedReports, elementprop.getProperty("REPORTS_LINKTEXT"), elementprop.getProperty("REPORTS_DROPDOWN_ID"));
		if (!Verify.verifyArrayofStrings(actual, expectedReports, true))
			failurecount++;

		//Timeframe
		moveToElement(elementprop.getProperty("TIMEFRAME_LINKTEXT"));
		typeEditBox(elementprop.getProperty("TIMEFRAME_FROM_DATE_ID"), fromDate);
		typeEditBox(elementprop.getProperty("TIMEFRAME_TO_DATE_ID"), toDate);
		clickButtonV2(elementprop.getProperty("TIMEFRAME_GO_BUTTON_ID"));

		waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("TYPE_COLUMN_XPATH"), 30);
		//Summary report
		if(Verify.verifyTableColumnNames("datatable",expOvernightColumns)){
			if(!isTextPresent("EASE found no items for this report")){
				if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 5))
					failurecount++;

				if (!Verify.validateTableColumnSortOrder("datatable", "Type", 7))
					failurecount++;

				if (!Verify.validateTableColumnSortOrder("datatable", "Updt", 13))
					failurecount++;
			}
		}
		else{
			report.report("EASE found no items for this report at this time.",ReportAttribute.BOLD);
		}

		moveToElement(elementprop.getProperty("REPORTS_LINKTEXT"));
		clickLink(elementprop.getProperty("TSTATUS_UNDER_REPORTS_LINKTEXT"));
		waitForTextVisibility(ByLocator.xpath, elementprop.getProperty("ADVANCED_SEARCH_HEADER_XPATH"), elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderTimeframe(String fromDate, String toDate) throws Exception {

		int failurecount = 0;
		String[] expected = {"Overnight","Weekly"};
		navigateToPage();
		String[] actual = reportshelper.verifySubMenuLinks(expected, elementprop.getProperty("TIMEFRAME_LINKTEXT"),
				elementprop.getProperty("TIMEFRAME_HOVER_MENU_ID"));
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		reportshelper.clickSubMenuLinks(actual,elementprop.getProperty("TIMEFRAME_LINKTEXT"));

		//verify From and To
		moveToElement(elementprop.getProperty("TIMEFRAME_LINKTEXT"));
		typeEditBox(elementprop.getProperty("TIMEFRAME_FROM_DATE_ID"), fromDate);
		typeEditBox(elementprop.getProperty("TIMEFRAME_TO_DATE_ID"), toDate);
		clickButtonV2(elementprop.getProperty("TIMEFRAME_GO_BUTTON_ID"));
		Thread.sleep(8000);
		waitForElementVisibility(By.xpath("//td[@class='headerblue' or @class='headergreen']"));
		String headerText2 = driver.findElement(By.xpath("//td[@class='headerblue' or @class='headergreen']")).getText();
		String[] getDatesFromHeader2  = reportshelper.regExpToGetDates(headerText2);

		if(getDatesFromHeader2[0].equals(fromDate) && getDatesFromHeader2[1].equals(toDate)){
			report.report("Custom dates matched.");
		}
		else{
			report.report("Custom dates did not matched.");
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderAgency(String agency) throws Exception {
		int failurecount = 0;
		String[] expectedAgenciesFromJsystem = agency.split(",");
		navigateToPage();
		waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("AGENCY_LINKTEXT"), 30);
		moveToElement(elementprop.getProperty("AGENCY_LINKTEXT"));
		String[] actual = selectGetOptions(elementprop.getProperty("AGENCY_HOVER_DROPDOWN_ID"));
		for(int i=0;i<expectedAgenciesFromJsystem.length;i++){
			for(int j=0;j<actual.length;j++){
				if(expectedAgenciesFromJsystem[i].trim().equals(actual[j])){
					report.report("Successfully verified the provider "+expectedAgenciesFromJsystem[i]+ " under this customer.");
					break;
				}
			}
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifySaveProfileOption(String agencyFromJSystem, String saveProfileNameAS) throws Exception {

		int failurecount = 0; boolean isFound = false; String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		for(int i=0;i<agency.length;i++){
			selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency[i]);
		}
		checkChkBox(elementprop.getProperty("AS_SAVE_PROFILE_CHECKBOX_NAME"));
		typeEditBox(elementprop.getProperty("AS_SAVE_PROFILE_TEXTBOX_NAME"),saveProfileNameAS);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("Saved Profile is overwritten.");
		}
		Thread.sleep(10000);	//Wait until results to display, or else the Record Count will always be "0"
		report.report("SEARCH RESULTS RECORD COUNT :"+getElementText(By.id(elementprop.getProperty("AS_TOTAL_ROWS_ID"))));
		WebElement backButton =waitForElementVisibility(By.id(elementprop.getProperty("BACK_BUTTON_ID"))); //Since there is no linkText for Live Search we used this
		safeJavaScriptClick(backButton);
		WebElement savedProfileDropdown = waitForElementVisibility(By.xpath(elementprop.getProperty("AS_SAVED_PROFILE_DROPDOWN_OPTIONS_XPATH")));
		if(savedProfileDropdown != null){
			List<WebElement> savedProfileOptions = driver.findElements(By.xpath(elementprop.getProperty("AS_SAVED_PROFILE_DROPDOWN_OPTIONS_XPATH")));
			for(WebElement option : savedProfileOptions){
				if(option.getText().trim().equalsIgnoreCase(saveProfileNameAS.trim())){
					report.report("Successfully verified the Saved Profile details.");
					isFound = true;
					break;
				}
			}
		}
		if(!isFound){
			report.report("Failed to verify the "+saveProfileNameAS+" in the Saved Profile:");
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTimeFrameOptionsUnderAdvanced(String fromDate, String toDate, String expTimFrameOptionsFromJSystem) throws Exception {

		int failurecount = 0;
		String[] expectedTimeFrameOptions = expTimFrameOptionsFromJSystem.split(",");
		//Click Advanced using clickingAdvancedTab building block in MyDDEReportsTests
		waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("TIMEFRAME_XPATH"), 30);
		String[] actual = reportshelper.verifySubMenuLinks(expectedTimeFrameOptions, elementprop.getProperty("TIMEFRAME_LINKTEXT"),
				elementprop.getProperty("TIMEFRAME_HOVER_MENU_ID"));
		if (!Verify.verifyArrayofStrings(actual, expectedTimeFrameOptions, true))
			failurecount++;

		moveToElement(elementprop.getProperty("TIMEFRAME_LINKTEXT"));
		clickLink(elementprop.getProperty("TIMEFRAME_ALL_UPTO_18_LINKTEXT"));
		Thread.sleep(5000);
		waitForElementVisibility(By.xpath("//td[@class='headerblue' or @class='headergreen']"));
		String headerText = driver.findElement(By.xpath("//td[@class='headerblue' or @class='headergreen']")).getText();
		String[] getDatesFromHeader  = reportshelper.regExpToGetDates(headerText);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date fDate = df.parse(getDatesFromHeader[0]);
		Date tDate = df.parse(getDatesFromHeader[1]);

		int monthsDiff = reportshelper.monthsDifferenceBetween(fDate, tDate);
		if(monthsDiff == 18){
			report.report("Displayed records are upto 18 months ago.");
		}else{
			report.report("Displayed records are not under 18 months ago.");
		}

		moveToElement(elementprop.getProperty("TIMEFRAME_LINKTEXT"));
		typeEditBox(elementprop.getProperty("TIMEFRAME_FROM_DATE_ID"), fromDate);
		typeEditBox(elementprop.getProperty("TIMEFRAME_TO_DATE_ID"), toDate);
		clickButtonV2(elementprop.getProperty("TIMEFRAME_GO_BUTTON_ID"));
		Thread.sleep(5000);
		waitForElementVisibility(By.xpath("//td[@class='headerblue' or @class='headergreen']"));
		String headerText2 = driver.findElement(By.xpath("//td[@class='headerblue' or @class='headergreen']")).getText();
		String[] getDatesFromHeader2  = reportshelper.regExpToGetDates(headerText2);

		if(getDatesFromHeader2[0].equals(fromDate) && getDatesFromHeader2[1].equals(toDate)){
			report.report("Custom dates matched.");
		}
		else{
			report.report("Custom dates did not matched.");
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedSearchForMAXFields(Map<String, String> mapAttrValues) throws Exception {

		String[] agency = null;String hic = null;String monthsAgo = null;String status = null; String location = null;
		List<String> checkboxNames = Arrays.asList("ClaimRap", "ClaimFinal", "ClaimOther");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 25);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\AdvancedSearch.xml", mapAttrValues);
		agency = getValueFromJSystem(lsAttributes, "Agency").split(",");
		hic = getValueFromJSystem(lsAttributes, "HIC");
		monthsAgo = getValueFromJSystem(lsAttributes, "Months Ago");
		status = getValueFromJSystem(lsAttributes, "Status");
		location = getValueFromJSystem(lsAttributes, "Location");

		for(int i=0;i<agency.length;i++){
			selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency[i]);
		}
		typeEditBox(elementprop.getProperty("AS_HIC_TEXTBOX_NAME"), hic);
		checkCheckboxes(checkboxNames);
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		selectByNameOrID(elementprop.getProperty("AS_STATUS_DROPDOWN_ID"), status);
		typeEditBox(elementprop.getProperty("AS_LOCATION_TEXTBOX_ID"), location); 
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));

		WebElement billed = waitForElementVisibility(By.xpath(elementprop.getProperty("AS_BILLED_COLUMN_XPATH")));
		WebElement reimb = waitForElementVisibility(By.xpath(elementprop.getProperty("AS_REIMB_COLUMN_XPATH")));
		return (billed!=null && reimb!=null) ? true : false;
	}

	public boolean verifyAdvancedSearchForStatusLocation() throws Exception {

		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT").trim());
		selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"),"All");
		selectByNameOrID(elementprop.getProperty("AS_STATUS_DROPDOWN_ID"), "Suspense");
		typeEditBox(elementprop.getProperty("AS_LOCATION_TEXTBOX_ID"), "B6001");
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		WebElement dueDate = waitForElementVisibility(By.xpath(elementprop.getProperty("AS_DUE_DATE_COLUMN_XPATH")));
		WebElement dayDueDate = waitForElementVisibility(By.xpath(elementprop.getProperty("AS_DAY_DUE_DATE_COLUMN_XPATH")));
		WebElement daysLeft = waitForElementVisibility(By.xpath(elementprop.getProperty("AS_DAYS_LEFT_COLUMN_XPATH")));
		return (dueDate!=null && dayDueDate!=null && daysLeft!=null) ? true : false;
	}

	public boolean verifyOptionUB04Form() throws Exception {

		int failurecount = 0;
		int count = 0;
		String payerNameExpected = "MEDICARE";
		String payerNameActual = null;
		navigateToPage();
		Thread.sleep(5000);
		WebElement verifyUB04 = waitForElementToBeClickable(ByLocator.id, elementprop.getProperty("UB04_ICON_MYDDEPAGE_ID"), 80);
		if ( verifyUB04 != null){
			clickLinkV2(elementprop.getProperty("UB04_ICON_MYDDEPAGE_ID"));
			report.report("Clicked on UB04 icon under MyDDE page...");
			Thread.sleep(5000);
			do{
				payerNameActual = driver.findElement(By.xpath(elementprop.getProperty("UB04_MEDICARE_TEXT_XPATH"))).getAttribute("value");
				if(payerNameActual != null){
					break;
				}
				count++;
			}while(payerNameActual == null || count < 3);
			report.report("PAYERNAME_ACTUAL : "+payerNameActual);
			if(payerNameExpected.equalsIgnoreCase(payerNameActual.trim())){
				report.report("User is in UB04 Form page.");
			}else{
				failurecount++;
			}
		}else{
			report.report("UB04 icon is not visible on the page");
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedOption() throws Exception {
		
		//use clickingAdvancedTab building block to click on Advanced
		int failurecount = 0;
		WebElement verifyChangesReport = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_CHANGES_REPORT_XPATH"), 30);
		if(verifyChangesReport == null)
			failurecount++;

		return failurecount == 0 ? true : false;
	}

	public boolean verifyLiveSearchOption(String hicFromJSystem) throws Exception {

		int failurecount = 0;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);

		WebElement hicEditField = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("LIVESEARCH_HIC_TEXTBOX_XPATH"), 30);
		WebElement go = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("LIVESEARCH_GO_BUTTON_XPATH"), 30);
		WebElement advancedSearch = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("LIVESEARCH_ADVSEARCH_XPATH"), 30);

		if(hicEditField!=null && go!=null && advancedSearch!=null){
			typeEditBoxByWebElement(hicEditField, hicFromJSystem);
			clickButton(elementprop.getProperty("LIVESEARCH_GOBUTTON_VALUE"));
			waitForElementVisibility(By.xpath(elementprop.getProperty("GREEN_HEADER_XPATH")));
			String verifyHicIsValid = driver.findElement(By.xpath(elementprop.getProperty("GREEN_HEADER_XPATH"))).getText();
			if(verifyHicIsValid.trim().contains("PATIENT INFORMATION")){
				report.report("Patient records are found in EASE.");
			}else if(verifyHicIsValid.trim().contains("ELIGIBILITY CHECK")){
				report.report("Patient records are not found in EASE.");
			}else{
				failurecount++;
			}
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderAdvancedView() throws Exception {

		int failurecount = 0;
		String[] options = {"reportHICSearch","reportHome","reportTimeframe","reportAgency","reportPieChart","reportPrint","reportExport",
				"reportNewUB04","reportComplexity"};

		//Click Advanced using Advanced building block in MyDDEReportsPage

		for(String optionsId : options){
			WebElement verifyOptions = waitForElementVisibility(By.id(optionsId));
			if ( verifyOptions != null) {
				String optionsText = verifyOptions.getText().toUpperCase();
				if(!optionsText.equals("")){
					report.report("Successfully verified "+optionsText+" under Advanced view.");
				}
				else{
					report.report("Successfully verified "+optionsId+" under Advanced view.");
				}
			}else{
				report.report("Failed to verify "+optionsId+" under Advanced view.");
				failurecount++;
			}
		}

		WebElement verifyPaymentReport = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_PAYMENT_REPORT_XPATH"), 30);
		WebElement verifyClaimsReport = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_CLAIMS_REPORT_XPATH"), 30);
		WebElement verifyPatientsReport = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_PATIENTS_REPORT_XPATH"), 30);
		if(verifyPaymentReport == null && verifyClaimsReport==null && verifyPatientsReport==null)
			failurecount++;

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderAdvancedSearch(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		WebElement tobField2 = null;
		String tob1 = null, tob2 = null, agency = null;

		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\AdvancedSearch.xml", mapAttrValues);
		tob1 = getValueFromJSystem(lsAttributes,"Tob1");
		tob2 = getValueFromJSystem(lsAttributes, "Tob2");
		agency= getValueFromJSystem(lsAttributes, "Agency");

		WebElement tobField1 = waitForElementToBeClickable(ByLocator.xpath, "//input[@id='TOBT0']", 30);
		//Add a 3 digit multiple TOB's using the "+" sign
		if(tobField1 != null){
			typeEditBox("TOBT0", tob1);
			clickButton("TOBB0");
			tobField2 = waitForElementToBeClickable(ByLocator.xpath, "//input[@id='TOBT1']", 30);
			if(tobField2 != null){
				typeEditBox("TOBT1", tob2);
			}
		}
		//Remove  the TOB's using the "-"bpeters sign
		if(tobField2 != null){
			clickButton("TOBB0");
			Thread.sleep(3000);
			String updatedTob1 = driver.findElement(By.id("TOBT0")).getAttribute("value");
			if(!tob1.trim().equalsIgnoreCase(updatedTob1.trim())){
				report.report("User is able to remove TOB's.");
			}else{
				failurecount++;
			}
		}
		//Add duplicate TOB's 
		if(tobField2 != null){
			typeEditBox("TOBT1", tob2);
			boolean duplicateTobs = waitForTextVisibility(ByLocator.xpath,"//span[@id='TOBX1']/b","(Duplicate)");
			if(duplicateTobs){
				report.report("User received a message related to the Duplicate TOB's");
			}
		}
		String locator = getLocatorFromXML(lsAttributes,"Agency");
		String[] agencies = agency.split(",");
		for(int i=0;i<agencies.length;i++){
			selectByNameOrID(locator, agencies[i].trim());
		}
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		boolean verifyRecordsDisplayed = waitForTextVisibility(ByLocator.xpath, "//table[@id='datatable']/tbody/tr/td/b", "No records found matching the search criteria");
		if(verifyRecordsDisplayed){
			report.report("No records found matching the search criteria.");
		}else{
			report.report("Record Count: "+driver.findElement(By.id(elementprop.getProperty("AS_TOTAL_ROWS_ID"))).getText());
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifySaveProfileUnderAdvancedSearch(String agency, String monthsAgo, String saveProfileNameAS) throws Exception {

		int failurecount = 0; String saveProfileNameFromDB = null;
		String saveProfileQuery = "SELECT ProfileName from usersearchparam where ProfileName='"+saveProfileNameAS+"';" ;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency);
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		checkChkBox(elementprop.getProperty("AS_SAVE_PROFILE_CHECKBOX_NAME"));
		typeEditBox(elementprop.getProperty("AS_SAVE_PROFILE_TEXTBOX_NAME"),saveProfileNameAS);

		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("Saved Profile is overwritten.");
		}
		boolean noRecords = waitForTextVisibility(ByLocator.xpath, "//b[text()='No records found matching the search criteria']", "No records found matching the search criteria");
		if(!noRecords)
			Thread.sleep(10000);	//Wait until results to display, or else the Record Count will always be "0"
		report.report("SEARCH RESULTS RECORD COUNT : "+getElementText(By.id(elementprop.getProperty("AS_TOTAL_ROWS_ID"))));
		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(saveProfileQuery);
		while(rs.next()){
			saveProfileNameFromDB = rs.getString("ProfileName");
		}
		Verify.StringEquals(saveProfileNameAS, saveProfileNameFromDB);
		return failurecount == 0 ? true : false;
	}

	public boolean verifyColumnHeadersInSearchResults(String expectedColsFromJSystem) throws Exception {

		int failurecount = 0;
		String[] expectedColumns = expectedColsFromJSystem.split(",");
		List<String> expectedHeaderCols = Arrays.asList(expectedColumns); 
		List<String> actualHeaderCols = null;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("AS_SAVED_PROFILE_DROPDOWN_XPATH"), 30);
		selectByNameOrID("SelectedProfileName","T Status Report");
		Thread.sleep(3000);
		String saveProfileName = driver.findElement(By.xpath(elementprop.getProperty("AS_SAVE_PROFILE_TEXTBOX_XPATH"))).getAttribute("value");

		if(saveProfileName.trim().equalsIgnoreCase("T Status Report")){
			clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
			Thread.sleep(5000);
			WebElement agencyColumn = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("AS_AGENCY_COLUMN_XPATH"), 30);
			if(agencyColumn != null){
				List<WebElement> allHeaderColms = driver.findElements(By.xpath(elementprop.getProperty("SEARCH_RESULTS_COLUMNS_XPATH")));
				actualHeaderCols = new ArrayList<String>();
				for(WebElement headerColumn : allHeaderColms){
					String columnName = headerColumn.getText().toString();
					if(!columnName.trim().equals("")){
						actualHeaderCols.add(columnName);
					}
				}
			}
			else{
				failurecount++;
			}
			if (!Verify.listEquals(actualHeaderCols, expectedHeaderCols))
				failurecount++;
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTotalsForAdvancedSearchResult(String betweenDate, String andDate) throws Exception {

		int failurecount = 0;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		typeEditBox(elementprop.getProperty("AS_BETWEEN_TEXTBOX_NAME"), betweenDate);
		typeEditBox(elementprop.getProperty("AS_AND_TEXTBOX_NAME"), andDate);
		driver.findElement(By.id(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"))).clear();
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));

		double expBilledGrandTotals = 0, expReimbGrandTotals = 0, actualBilledGrandTotal=0, actualReimbGrandTotal=0;
		List noOptions = driver.findElements(By.xpath("//select[@class='gotoPage']/option"));
		for(int i=1;i<=noOptions.size()/2;i++){
			selectByNameOrID("gotoPage", i+"");
			waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("AS_BILLED_COLUMN_XPATH"), 30);
			Thread.sleep(5000);

			String billedPageTotals = driver.findElement(By.xpath(elementprop.getProperty("AS_BILLED_COLUMN_PAGE_TOTALS_XPATH"))).getText();
			expBilledGrandTotals += Double.parseDouble(billedPageTotals.replaceAll("[$,]",""));
			report.report("expBilledGrandTotals : "+expBilledGrandTotals);

			String reimbPageTotals = driver.findElement(By.xpath(elementprop.getProperty("AS_REIMB_COLUMN_PAGE_TOTALS_XPATH"))).getText();
			expReimbGrandTotals += Double.parseDouble(reimbPageTotals.replaceAll("[$,]",""));
			report.report("expReimbGrandTotals : "+expReimbGrandTotals);

			String billedGrandTotal = driver.findElement(By.xpath(elementprop.getProperty("AS_BILLED_COLUMN_GRAND_TOTALS_XPATH"))).getText();
			actualBilledGrandTotal = Double.parseDouble(billedGrandTotal.replaceAll("[$,]", ""));
			report.report("actualBilledGrandTotal : "+actualBilledGrandTotal);

			String reimbGrandTotal = driver.findElement(By.xpath(elementprop.getProperty("AS_REIMB_COLUMN_GRAND_TOTALS_XPATH"))).getText();
			actualReimbGrandTotal = Double.parseDouble(reimbGrandTotal.replaceAll("[$,]", ""));
			report.report("actualReimbGrandTotal : "+actualReimbGrandTotal);


		}
		if((expBilledGrandTotals == actualBilledGrandTotal) && (expReimbGrandTotals == actualReimbGrandTotal)){
			report.report("Sum of Billed and Reimb columns are matching with Totals at the bottom of the page", ReportAttribute.BOLD);
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyFiltersInAdvancedSearchResults(String monthsAgo, String agency, String hic, String tob) throws Exception {
		int failurecount = 0;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		Thread.sleep(5000);
		selectByNameOrID("2", agency);
		typeEditBox("3", hic);
		typeEditBox("11", tob);
		String billedPageTotals = driver.findElement(By.xpath(elementprop.getProperty("AS_BILLED_COLUMN_PAGE_TOTALS_XPATH"))).getText();

		String reimbPageTotals = driver.findElement(By.xpath(elementprop.getProperty("AS_REIMB_COLUMN_PAGE_TOTALS_XPATH"))).getText();

		String billedGrandTotals = driver.findElement(By.xpath(elementprop.getProperty("AS_BILLED_COLUMN_GRAND_TOTALS_XPATH"))).getText();

		String reimbGrandTotals = driver.findElement(By.xpath(elementprop.getProperty("AS_REIMB_COLUMN_GRAND_TOTALS_XPATH"))).getText();

		Verify.StringEquals(billedPageTotals, billedGrandTotals);
		Verify.StringEquals(reimbPageTotals, reimbGrandTotals);

		return failurecount == 0 ? true : false;
	}

	public boolean verifySaveProfileOptionWithDuplicateName(String agencyFromJSystem, String monthsAgo, String saveProfileNameAS) throws Exception {

		int failurecount = 0; String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		for(int i=0;i<agency.length;i++){
			selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency[i]);
		}
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		checkChkBox(elementprop.getProperty("AS_SAVE_PROFILE_CHECKBOX_NAME"));
		typeEditBox(elementprop.getProperty("AS_SAVE_PROFILE_TEXTBOX_NAME"),saveProfileNameAS);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("Saved Profile is overwritten.");
		}
		boolean noRecords = waitForTextVisibility(ByLocator.xpath, "//b[text()='No records found matching the search criteria']", "No records found matching the search criteria");
		if(!noRecords)
			Thread.sleep(10000);	//Wait until results to display, or else the Record Count will always be "0"
		report.report("SEARCH RESULTS RECORD COUNT :"+getElementText(By.id(elementprop.getProperty("AS_TOTAL_ROWS_ID"))));
		WebElement liveSearchIcon2 = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon2);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		verifyAlert("I am still processing your previous request, please try again shortly.");//needs to be modified
		waitForElementToBeClickable(ByLocator.xpath, "//input[@id='SaveProfile']", 30);
		checkChkBox(elementprop.getProperty("AS_SAVE_PROFILE_CHECKBOX_NAME"));
		typeEditBox(elementprop.getProperty("AS_SAVE_PROFILE_TEXTBOX_NAME"),saveProfileNameAS);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("User received an appropriate message about existing profile name.");
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedSearchForCustomDates(String agencyFromJSystem, String betweenDate, String andDate) throws Exception {

		int failurecount = 0;
		String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		for(int i=0;i<agency.length;i++){
			selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency[i]);
		}
		typeEditBox(elementprop.getProperty("AS_BETWEEN_TEXTBOX_NAME"), betweenDate);
		typeEditBox(elementprop.getProperty("AS_AND_TEXTBOX_NAME"), andDate);
		driver.findElement(By.id(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"))).clear();

		WebElement monthsAgo = waitForElementToBeClickable(ByLocator.name, elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), 30);
		if(monthsAgo.getText().trim().equals("")){
			clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		}
		WebElement searchResults = waitForElementVisibility(By.xpath(elementprop.getProperty("ADVANCED_SEARCH_RESULTS_PAGE_HEADER")));
		if(searchResults != null){
			report.report("Application is navigated to SEARCH RESULTS page.");
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedSearchForGivenMonths(String agencyFromJSystem,String monthsAgo) throws Exception {

		int failurecount = 0;
		String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		for(int i=0;i<agency.length;i++){
			selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency[i]);
		}
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		WebElement searchResults = waitForElementVisibility(By.xpath(elementprop.getProperty("ADVANCED_SEARCH_RESULTS_PAGE_HEADER")));
		if(searchResults != null){
			report.report("Application is navigated to SEARCH RESULTS page.");
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedSearchForGivenMonthsAndDateRange(String agencyFromJSystem, String betweenDate, String andDate,String monthsAgo) throws Exception {
		int failurecount = 0;
		String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		for(int i=0;i<agency.length;i++){
			selectByNameOrID(elementprop.getProperty("AS_AGENCY_DROPDOWN_ID"), agency[i]);
		}
		typeEditBox(elementprop.getProperty("AS_BETWEEN_TEXTBOX_NAME"), betweenDate);
		typeEditBox(elementprop.getProperty("AS_AND_TEXTBOX_NAME"), andDate);
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		WebElement searchResults = waitForElementVisibility(By.xpath(elementprop.getProperty("ADVANCED_SEARCH_RESULTS_PAGE_HEADER")));
		if(searchResults != null){
			report.report("Application is navigated to SEARCH RESULTS page.");
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}


	public boolean searchByHICAndNavigatetoPatientInfoScreen(String hic) throws Exception {
		navigateToPage();
		//move to search icon and enter HIC
		WebElement element = waitForElementVisibility(By.xpath(elementprop.getProperty("MYDDE_HIC_ICON_XPATH")));
		if(element==null){
			report.report("HIC Search ICON is not found in MyDDE Screen", Reporter.WARNING);
			return false;
		}
		moveToElement(element);
		typeEditBox(element.getAttribute("AS_HIC_TEXTBOX_NAME"), hic);
		clickButton(elementprop.getProperty("LIVESEARCH_GOBUTTON_VALUE"));
		return waitForElementVisibility(By.xpath("//td[contains(text(),'PATIENT INFORMATION')]"))!=null?true:false;
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