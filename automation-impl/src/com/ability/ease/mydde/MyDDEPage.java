package com.ability.ease.mydde;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class MyDDEPage extends AbstractPageObject{

	ReportsHelper reportshelper = new ReportsHelper();

	public boolean verifyPageViewAndOptions() throws Exception {

		int failurecount = 0;
		navigateToPage();
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

	public boolean verifyColumnsAndSorting(String reportText, String expectedColumnsFromJSystem, String columnName, int columnIndex) throws Exception {
		
		int failurecount = 0;String[] expectedColumns = null;
		if(expectedColumnsFromJSystem != null){
			expectedColumns = expectedColumnsFromJSystem.split(",");
		}
		navigateToPage();
		
		WebElement verifyAdvanced = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportComplexity']", 30);
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
		WebElement verifyAdvanced = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportComplexity']", 30);
		if ( verifyAdvanced.getText().contains("Basic")) {
			safeJavaScriptClick("Basic");
		}
		Thread.sleep(3000);
		
		moveToElement("Agency");
		selectByNameOrID("reportAgencySelect", agency);
		clickButton("Change Agency");
		String[] actual = reportshelper.verifySubMenuLinks(expectedReports, "Reports", "reportListMenu");
		if (!Verify.verifyArrayofStrings(actual, expectedReports, true))
			failurecount++;

		//Timeframe
		moveToElement("Timeframe");
		typeEditBox("reportCustomDateFrom", fromDate);
		typeEditBox("reportCustomDateTo", toDate);
		clickButtonV2("reportTimeframeButton");

		waitForElementToBeClickable(ByLocator.xpath, "//thead[@id='datatableheader']/tr/td[7]", 30);
		//Summary report
		if(Verify.verifyTableColumnNames("datatable",expOvernightColumns)){//if(Verify.verifyTableColumnNames("datatable",new String[]{"Type","Claim $"})){
			if(!isTextPresent("EASE found no items for this report")){
				if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 5))
					failurecount++;
			}
		}
		else{
			failurecount++;
		}

		moveToElement("Reports");
		clickLink("T Status Report");
		waitForTextVisibility(ByLocator.xpath, "//td[@class='headerblue']", "ADVANCED SEARCH");

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderTimeframe(String fromDate, String toDate) throws Exception {

		int failurecount = 0;
		String[] expected = {"Overnight","Weekly"};
		navigateToPage();
		clickLink("MY DDE");
		String[] actual = reportshelper.verifySubMenuLinks(expected, "Timeframe", "timeframeListMenu");
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		reportshelper.clickSubMenuLinks(actual,"Timeframe");

		//verify From and To
		moveToElement("Timeframe");
		typeEditBox("reportCustomDateFrom", fromDate);
		typeEditBox("reportCustomDateTo", toDate);
		clickButtonV2("reportTimeframeButton");
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
		waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportAgency']", 30);
		moveToElement("Agency");
		String[] actual = selectGetOptions("reportAgencySelect");
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
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 25);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		for(int i=0;i<agency.length;i++){
			selectByNameOrID("UserProvider", agency[i]);
		}
		checkChkBox("SaveProfile");
		typeEditBox("SaveProfileName",saveProfileNameAS);
		clickButtonV2("submit");
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("Saved Profile is overwritten.");
		}
		Thread.sleep(10000);	//Wait until results to display, or else the Record Count will always be "0"
		report.report("SEARCH RESULTS RECORD COUNT :"+getElementText(By.id("totalRows")));
		WebElement backButton =waitForElementVisibility(By.id("backNav"));//Since there is no linkText for Live Search we used this
		safeJavaScriptClick(backButton);
		WebElement savedProfileDropdown = waitForElementVisibility(By.xpath("//select[@id='SelectedProfileName']/option"));
		if(savedProfileDropdown != null){
			List<WebElement> savedProfileOptions = driver.findElements(By.xpath("//select[@id='SelectedProfileName']/option"));
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
		//Click Advanced using Advanced building block
		waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportTimeframe']", 30);
		String[] actual = reportshelper.verifySubMenuLinks(expectedTimeFrameOptions, "Timeframe", "timeframeListMenu");
		if (!Verify.verifyArrayofStrings(actual, expectedTimeFrameOptions, true))
			failurecount++;

		moveToElement("Timeframe");
		clickLink("All (up to 18 mos ago)");
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

		moveToElement("Timeframe");
		typeEditBox("reportCustomDateFrom", fromDate);
		typeEditBox("reportCustomDateTo", toDate);
		clickButtonV2("reportTimeframeButton");
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
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 25);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\AdvancedSearch.xml", mapAttrValues);
		agency = getValueFromJSystem(lsAttributes, "Agency").split(",");
		hic = getValueFromJSystem(lsAttributes, "HIC");
		monthsAgo = getValueFromJSystem(lsAttributes, "Months Ago");
		status = getValueFromJSystem(lsAttributes, "Status");
		location = getValueFromJSystem(lsAttributes, "Location");

		for(int i=0;i<agency.length;i++){
			selectByNameOrID("UserProvider",agency[i]);
		}
		typeEditBox("HIC", hic);
		checkCheckboxes(checkboxNames);
		typeEditBox("ClaimSubmittedLookbackMonths", monthsAgo);
		selectByNameOrID("Status", status);
		typeEditBox("Location", location);
		clickButtonV2("submit");

		WebElement billed = waitForElementVisibility(By.xpath("//div[text()='Billed']"));
		WebElement reimb = waitForElementVisibility(By.xpath("//div[text()='Reimb']"));
		return (billed!=null && reimb!=null) ? true : false;
	}

	public boolean verifyAdvancedSearchForStatusLocation() throws Exception {

		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 25);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		selectByNameOrID("UserProvider","All");
		selectByNameOrID("Status", "Suspense");
		typeEditBox("Location", "B6001");
		clickButtonV2("submit");
		WebElement dueDate = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[16]/div"));
		WebElement dayDueDate = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[17]/div"));
		WebElement daysLeft = waitForElementVisibility(By.xpath("//thead[@id='datatableheader']/tr[1]/th[18]/div"));
		return (dueDate!=null && dayDueDate!=null && daysLeft!=null) ? true : false;
	}

	public boolean verifyOptionUB04Form() throws Exception {

		int failurecount = 0;
		int count = 0;
		String payerNameExpected = "MEDICARE";
		String payerNameActual = null;
		navigateToPage();
		clickLinkV2("reportNewUB04");
		do{
			payerNameActual = driver.findElement(By.xpath("//input[@name='ub50a']")).getAttribute("value");
			if(payerNameActual != null){
				break;
			}
			count++;
		}while(payerNameActual == null || count < 3);

		if(payerNameExpected.equalsIgnoreCase(payerNameActual.trim())){
			report.report("User is in UB04 Form page.");
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedOption() throws Exception {

		int failurecount = 0;
		navigateToPage();
		//Use Building Block "clickingAdvanceTab" in MyDDEReports to click on Advanced
		WebElement verifyAdvanced = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportComplexity']", 30);
		if ( verifyAdvanced.getText().contains("Basic")) {
			report.report("User is able to see Basic option.");
		}else{
			report.report("User is not able to see Basic option.");
			failurecount++;
		}
		WebElement verifyChangesReport = waitForElementToBeClickable(ByLocator.xpath, "//div[@id='sbarcontainer']/ul/li[2]/a", 30);
		if(verifyChangesReport == null)
			failurecount++;

		return failurecount == 0 ? true : false;
	}

	public boolean verifyLiveSearchOption(String hicFromJSystem) throws Exception {

		int failurecount = 0;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);

		WebElement hicEditField = waitForElementToBeClickable(ByLocator.xpath, "//input[@id='reportHICEntry']", 30);
		WebElement go = waitForElementToBeClickable(ByLocator.xpath, "//input[@id='reportHICButton']", 30);
		WebElement advancedSearch = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportAdvanceSearch']", 30);

		if(hicEditField!=null && go!=null && advancedSearch!=null){
			typeEditBoxByWebElement(hicEditField, hicFromJSystem);
			clickButton("GO");
			waitForElementVisibility(By.xpath("//td[@class='headergreen']"));
			String verifyHicIsValid = driver.findElement(By.xpath("//td[@class='headergreen']")).getText();
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

		WebElement verifyPaymentReport = waitForElementToBeClickable(ByLocator.xpath, "//a[text()='Payment']", 30);
		WebElement verifyClaimsReport = waitForElementToBeClickable(ByLocator.xpath, "//a[text()='Claims']", 30);
		WebElement verifyPatientsReport = waitForElementToBeClickable(ByLocator.xpath, "//a[text()='Patients']", 30);
		if(verifyPaymentReport == null && verifyClaimsReport==null && verifyPatientsReport==null)
			failurecount++;

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderAdvancedSearch(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		WebElement tobField2 = null;
		String tob1 = null, tob2 = null, agency = null;

		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");

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
		clickButtonV2("submit");
		boolean verifyRecordsDisplayed = waitForTextVisibility(ByLocator.xpath, "//table[@id='datatable']/tbody/tr/td/b", "No records found matching the search criteria");
		if(verifyRecordsDisplayed){
			report.report("No records found matching the search criteria.");
		}else{
			report.report("Record Count: "+driver.findElement(By.id("totalRows")).getText());
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyColumnHeadersInSearchResults(String expectedColsFromJSystem) throws Exception {

		int failurecount = 0;
		String[] expectedColumns = expectedColsFromJSystem.split(",");
		List<String> expectedHeaderCols = Arrays.asList(expectedColumns); 
		List<String> actualHeaderCols = null;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		waitForElementToBeClickable(ByLocator.xpath, "//select[@id='SelectedProfileName']", 30);
		selectByNameOrID("SelectedProfileName","T Status Report");
		Thread.sleep(3000);
		String saveProfileName = driver.findElement(By.xpath("//input[@id='SaveProfileName']")).getAttribute("value");

		if(saveProfileName.trim().equalsIgnoreCase("T Status Report")){
			clickButtonV2("submit");
			WebElement agencyColumn = waitForElementToBeClickable(ByLocator.xpath, "//div[text()='Agency']", 30);
			if(agencyColumn != null){
				List<WebElement> allHeaderColms = driver.findElements(By.xpath("//thead[@id='datatableheader']/tr/th/div"));
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

	public boolean verifyTotalsForAdvancedSearchResult(String savedProfile) throws Exception {

		int failurecount = 0;
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");

		selectByNameOrID("SelectedProfileName", savedProfile);
		Thread.sleep(3000);
		String saveProfileName = driver.findElement(By.xpath("//input[@id='SaveProfileName']")).getAttribute("value");

		if(saveProfileName.trim().equalsIgnoreCase(savedProfile)){
			clickButtonV2("submit");
			//TODO how to verify totals billed and reimb
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifySaveProfileOptionWithDuplicateName(String agencyFromJSystem, String monthsAgo, String saveProfileNameAS) throws Exception {

		int failurecount = 0; String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		for(int i=0;i<agency.length;i++){
			selectByNameOrID("UserProvider", agency[i]);
		}
		typeEditBox("ClaimSubmittedLookbackMonths", monthsAgo);
		checkChkBox("SaveProfile");
		typeEditBox("SaveProfileName",saveProfileNameAS);
		clickButtonV2("submit");
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("Saved Profile is overwritten.");
		}
		boolean noRecords = waitForTextVisibility(ByLocator.xpath, "//b[text()='No records found matching the search criteria']", "No records found matching the search criteria");
		if(!noRecords)
			Thread.sleep(10000);	//Wait until results to display, or else the Record Count will always be "0"
		report.report("SEARCH RESULTS RECORD COUNT :"+getElementText(By.id("totalRows")));
		WebElement liveSearchIcon2 = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon2);
		clickLink("Advanced Search");
		verifyAlert("I am still processing your previous request, please try again shortly.");//needs to be modified
		waitForElementToBeClickable(ByLocator.xpath, "//input[@id='SaveProfile']", 30);
		checkChkBox("SaveProfile");
		typeEditBox("SaveProfileName",saveProfileNameAS);
		clickButtonV2("submit");
		if(verifyAlert("OK to overwrite profile "+ saveProfileNameAS+"?")){
			report.report("User received an appropriate message about existing profile name.");
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyAdvancedSearchForCustomDates(String agencyFromJSystem, String betweenDate, String andDate) throws Exception {

		int failurecount = 0;
		String[] agency = agencyFromJSystem.split(",");
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		for(int i=0;i<agency.length;i++){
			selectByNameOrID("UserProvider", agency[i]);
		}
		typeEditBox("ClaimSubmittedFrom", betweenDate);
		typeEditBox("ClaimSubmittedTo", andDate);
		driver.findElement(By.id("ClaimSubmittedLookbackMonths")).clear();

		WebElement monthsAgo = waitForElementToBeClickable(ByLocator.xpath, "//input[@id='ClaimSubmittedLookbackMonths']", 30);
		if(monthsAgo.getText().trim().equals("")){
			clickButtonV2("submit");
		}
		WebElement searchResults = waitForElementVisibility(By.xpath("//td[text()='SEARCH RESULTS']"));
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
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		for(int i=0;i<agency.length;i++){
			selectByNameOrID("UserProvider", agency[i]);
		}
		typeEditBox("ClaimSubmittedLookbackMonths", monthsAgo);
		clickButtonV2("submit");
		WebElement searchResults = waitForElementVisibility(By.xpath("//td[text()='SEARCH RESULTS']"));
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
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, "//a[@id='reportHICSearch']", 30);
		moveToElement(liveSearchIcon);
		clickLink("Advanced Search");
		for(int i=0;i<agency.length;i++){
			selectByNameOrID("UserProvider", agency[i]);
		}
		typeEditBox("ClaimSubmittedFrom", betweenDate);
		typeEditBox("ClaimSubmittedTo", andDate);
		typeEditBox("ClaimSubmittedLookbackMonths", monthsAgo);
		clickButtonV2("submit");
		WebElement searchResults = waitForElementVisibility(By.xpath("//td[text()='SEARCH RESULTS']"));
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
		WebElement element = waitForElementVisibility(By.id("reportHICSearch"));
		if(element==null){
			report.report("HIC Search ICON is not found in MyDDE Screen", Reporter.WARNING);
			return false;
		}
		moveToElement(element);
		typeEditBox("reportHICEntry", hic);
		clickButton("GO");

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