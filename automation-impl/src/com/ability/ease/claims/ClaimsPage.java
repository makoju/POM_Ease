package com.ability.ease.claims;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UB04FormXMLParser;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.mydde.reports.MyDDEReportsPage;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPage extends AbstractPageObject{

	ClaimsHelper helper = new ClaimsHelper();
	UIActions uiactions = new UIActions();


	//C-tor
	public ClaimsPage() {
		super();
	}

	public String sQuery = null;

	/**
	 * This method is to verify all the fields in UB04 form
	 */
	public boolean verifyUB04FormFields(Map<String, String> mapAttrVal)throws Exception{
		String sXpathToAgencyName = "//span[contains(text(),'OVERNIGHT')]/..";
		String sAgencyName = null;
		int failCounter = 0;
		String sClaimRequestXML = null;
		String sClaimRequestID = null;

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrVal);

		//click on MY DDE link and capture Agency name
		helper.clickMYDDELink();
		//Before start filling new claim form get the agency name from OVER NIGHT report screen
		waitForElementVisibility(By.xpath(sXpathToAgencyName));
		sAgencyName = driver.findElement(By.xpath(sXpathToAgencyName)).getText();
		sAgencyName = sAgencyName.substring(sAgencyName.lastIndexOf(" ") + 1);

		//Get current time from ease Database
		String startTime = helper.getCurrentTimeFromEaseDB();
		//filling all screen attribute values in UB04 form
		uiactions.fillScreenAttributes(lsAttributes);
		//validation 1 :: verify field locator 1 values
		if(!helper.verifyFiledLocator1Values(lsAttributes)){
			failCounter++;
		}

		//validation 2 :: NPI Present under MY DDE Screen and UB04 should match
		String sNPILocator = "//input[@name='ub1_1']";
		String sNPIValueFromUB04 = driver.findElement(By.xpath(sNPILocator)).getAttribute("value");

		report.report("NPI Value from UB04 form :" + sNPIValueFromUB04, ReportAttribute.BOLD);
		if(sAgencyName.equalsIgnoreCase(sNPIValueFromUB04.trim())){
			report.report("Validation : NPI present under MY DDE screen and NPI in UB04 are same", ReportAttribute.BOLD);
		}else{
			report.report("NPI present under MY DDE screen and NPI in UB04 are not same");
			failCounter++;
		}

		//validation 3 :: verify federal tax number
		String sFedTaxNoLocator = "//input[@name='ub5']";
		String sActualFedTaxNoFromUB04 = driver.findElement(By.xpath(sFedTaxNoLocator)).getAttribute("value");
		String sExpectedFedTaxNum = getValueFromJSystem(lsAttributes, "Federal Tax No");
		if(! sActualFedTaxNoFromUB04.equalsIgnoreCase(sExpectedFedTaxNum)){
			report.report("Failed to verify federal tax number displayed in UB04 form");
			failCounter++;
		}else{
			report.report("Validation : Verification of federal tax number in UB04 form successfully",ReportAttribute.BOLD);
		}

		//validation 4 :: verify the value of field locator 50 by default it is set to Z
		String sFL50Xpath = "//input[@name='preub50a']";
		String sFL50Value = driver.findElement(By.xpath(sFL50Xpath)).getAttribute("value");
		if(sFL50Value != null && sFL50Value.equals("Z")){
			report.report("Validation : Field Locator 50 contains 'Z'", ReportAttribute.BOLD);
		}else{
			report.report("Field Locator 50 doesn't contain 'Z'");
			failCounter++;
		}
		//validation 5 :: verify Field Locators 65 - Employer Name
		String sFL65Xpath = "//input[@name='ub65a']";
		WebElement oFL65Element =  driver.findElement(By.xpath(sFL65Xpath));
		String sReadOnlyAttrValue = oFL65Element.getAttribute("readonly");
		if(sReadOnlyAttrValue != null){
			report.report("Validation : Employer name field is disabled", ReportAttribute.BOLD);
		}else{
			report.report("Employer name field is editabled", Reporter.FAIL);
			failCounter++;
		}

		clickLinkV2("claimSubmit");
		if(helper.validateConfirmationScreenSteps(lsAttributes)){
			clickButton("yesConfirmEditClaimButton");
			report.report("Claim request has been submitted successfully", ReportAttribute.BOLD);
		}
		else{
			report.report("There are some problems in form filling, please fill all mandatory values in UB04 form");
			failCounter++;
		}

		//get current time from ease DB
		Thread.sleep(5000);
		String endTime = helper.getCurrentTimeFromEaseDB();
		//validation 6 :: verify XML file
		String sRequestDetails[] = helper.getUB04XMLFromDatabase(startTime, endTime);
		sClaimRequestID = sRequestDetails[0];
		sClaimRequestXML = sRequestDetails[1];

		String sFileName = TestCommonResource.getTestResoucresDirPath()+"UB04XMLs\\Claim_"+sClaimRequestID+".xml";

		if( helper.validateXMLFileFields(lsAttributes, sClaimRequestXML, sFileName)){
			report.report("XML file field data validation is completed successfully");
		}else{
			failCounter++;
			report.report("FAIL : XML file field data validation is failed");
		}
		/* validation 7  :: 0001 should present the at the bottom of the claim line entries. Total Charges and Non Covered charges should be sum of 
		 * the individual charges of each claim line entry*/

		if(! helper.verifyTotals(lsAttributes, sFileName)){
			failCounter++;
		}
		MySQLDBUtil.closeAllDBConnections();
		report.report("Fail counter :" + failCounter);
		if(failCounter == 0){
			return true;
		}
		return false;
	}

	/**
	 * This method is to verify add or remove claim lines functionality in UB04 form
	 */
	@SuppressWarnings("static-access")
	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal,String claimLineNumberToDelete,
			String claimLineNumberToAdd, String newClaimLineEntry)throws Exception{

		//stopping ease server
		/*String sShellCommand = "cd /opt/abilitynetwork/easeweb/bin/; sudo sh easeWeb.sh status";
		String output = ShellExecUtil.executeShellCmd(sShellCommand);
		report.report("Outpubpeterst of shell command "+ sShellCommand +": "+ output);
		 */
		int failCounter = 0;
		float[] correctedClaimTotals = null;
		float prevTotalCoveredCharges = 0, prevTotalNonCoveredCharges = 0;
		String sClaimRequestXML = null;
		String sClaimRequestID = null;		

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrVal);

		helper.clickMYDDELink();

		//Get current time from ease Database
		String startTime = helper.getCurrentTimeFromEaseDB();
		//filling screen attribute values
		uiactions.fillScreenAttributes(lsAttributes);
		driver.findElement(By.xpath("//li[contains(text(),'0001')]")).click();

		//Before deleting the claim line entry get totals
		float[] totalsBeforeDelete = helper.getTotalsFromUB04Form();

		if( totalsBeforeDelete.length > 0) {
			report.report("Total covered charges before deletion of claim line : " + String.valueOf(totalsBeforeDelete[0]));
			report.report("Total non-covered charges before deletion of claim line : " + String.valueOf(totalsBeforeDelete[1]));
			prevTotalCoveredCharges = totalsBeforeDelete[0];
			prevTotalNonCoveredCharges = totalsBeforeDelete[1];
		}else{
			report.report("Fail to get total charges in UB04 form from UI, before removing claim entry", report.WARNING);
			failCounter++;
		}

		//get claim line record details which is going to be deleted
		String[] sClaimLines = getValueFromJSystem(lsAttributes, "ClaimLineEntries").split(",");
		String sClaimLineToBeDeleted = sClaimLines[Integer.valueOf(claimLineNumberToDelete)-1];
		float[] deletedTotals = helper.getTotalsFromClaimLine(sClaimLineToBeDeleted);
		correctedClaimTotals = new float[]{helper.round(prevTotalCoveredCharges - deletedTotals[0],2), 
				helper.round(prevTotalNonCoveredCharges - deletedTotals[1],2)};

		//Delete claim line entry
		String xPathToIdentifyWhichLineToDelete = "//*[@name='ub42_"+claimLineNumberToDelete+"']";
		helper.moveToEditIcon(xPathToIdentifyWhichLineToDelete);
		waitForElementVisibility(By.id("editmenutext"));
		if(isElementPresent(By.id("editmenutext"))){
			safeJavaScriptClick("Remove this claim line");
			report.report("Removed claim line : "+ claimLineNumberToDelete);
		}else{
			report.report("Failed moved mouse to edit icon");
			failCounter++;
		}

		//compare claim totals after deletion of claim line
		float[] totalsAfterDeleteClaimLine = helper.getTotalsFromUB04Form();

		if ( Verify.compareFloatArrays(totalsAfterDeleteClaimLine, correctedClaimTotals)){
			report.report("Total covered charges after deletion of claim line : " + String.valueOf(totalsAfterDeleteClaimLine[0]));
			report.report("Total non-covered charges after deletion of claim line : " + String.valueOf(totalsAfterDeleteClaimLine[1]));
			report.report("Claim totals after claim line deletion are corrected successfully");
		}else{
			report.report("Fail : Claim totals after claim line deletion are not corrected");
			failCounter++;
		}

		//Add claim line entry
		String[] temp1 = claimLineNumberToAdd.split(",");
		String sNewClaimLinePoisition = temp1[0];
		String beforeOrAfter = temp1[1];
		String xPathToIdentifyAtWhichLineToAdd = "//*[@name='ub42_"+sNewClaimLinePoisition+"']";
		float[] addedTotals = helper.getTotalsFromClaimLine(newClaimLineEntry);
		helper.moveToEditIcon(xPathToIdentifyAtWhichLineToAdd);
		waitForElementVisibility(By.id("editmenutext"));
		if(isElementPresent(By.id("editmenutext"))){
			if(beforeOrAfter.equalsIgnoreCase("before")){
				safeJavaScriptClick("Add claim line before");
				report.report("Added new claim line : "+ newClaimLineEntry + " : before line " + sNewClaimLinePoisition);
			}else if(beforeOrAfter.equalsIgnoreCase("after")){
				safeJavaScriptClick("Add claim line after");
				report.report("Added new claim line : "+ newClaimLineEntry + " : after line " + sNewClaimLinePoisition);
			}else{
				report.report("Please provide correct position either before or after in jsystem");
			}
		}else{
			report.report("Failed moved mouse to edit icon");
			failCounter++;
		}
		helper.addClaimLine(newClaimLineEntry, sNewClaimLinePoisition, beforeOrAfter);
		driver.findElement(By.xpath("//li[contains(text(),'0001')]")).click();

		//compare totals after adding new claim line
		float[] totalsAfterAddClaimLine = helper.getTotalsFromUB04Form();
		correctedClaimTotals = new float[]{helper.round(totalsAfterDeleteClaimLine[0] + addedTotals[0],2), 
				helper.round(totalsAfterDeleteClaimLine[1] + addedTotals[1],2)};

		if ( Verify.compareFloatArrays(totalsAfterAddClaimLine, correctedClaimTotals)){
			report.report("Total covered charges after addition of new claim line : " + String.valueOf(totalsAfterAddClaimLine[0]));
			report.report("Total non-covered charges after addition of new claim line : " + String.valueOf(totalsAfterAddClaimLine[1]));
			report.report("Claim totals after new claim line addition are corrected successfully");
		}else{
			report.report("Fail : Claim totals after new claim line addition are not corrected");
			failCounter++;
		}

		clickLinkV2("claimSubmit");

		if(helper.acceptUB04SubmitWarning()){
			if(helper.validateConfirmationScreenSteps(lsAttributes)){
				clickButton("yesConfirmEditClaimButton");
				report.report("Claim request has been submitted successfully", ReportAttribute.BOLD);
			}
			else{
				report.report("There are some problems in form filling, please fill all mandatory values in UB04 form");
				failCounter++;
			}
		}else{
			report.report("Fail : Unable to handle UB04 Form submission dailog box");
		}

		String endTime = helper.getCurrentTimeFromEaseDB();
		//waiting to record to be pushed unto the database
		Thread.sleep(5000);
		//get the xml file from database
		String sRequestDetails[] = helper.getUB04XMLFromDatabase(startTime, endTime);
		sClaimRequestID = sRequestDetails[0];
		sClaimRequestXML = sRequestDetails[1];

		String sFileName = TestCommonResource.getTestResoucresDirPath()+"UB04XMLs\\Claim_"+sClaimRequestID+".xml";

		if( helper.validateXMLFileFields(lsAttributes, sClaimRequestXML, sFileName)){
			report.report("XML file field data validation is completed successfully");
		}else{
			failCounter++;
			report.report("FAIL : XML file field data validation is failed");
		}

		//check whether XMl file contains an entry for removed claim line
		String sXpathExpression = "//claimline[@action='remove']/@line";
		String sRemovedClaimLineNumber = UB04FormXMLParser.getInstance().getValueOfXpathExpression(sFileName, sXpathExpression);

		if(sRemovedClaimLineNumber.equalsIgnoreCase(claimLineNumberToDelete)){
			report.report("Request XMl file contains an entry for removed claim line : " + claimLineNumberToDelete);
		}else{
			report.report("Fail : Request XML file doesn't contain entry for removed claim line :" + claimLineNumberToDelete);
			failCounter++;
		}

		//verify totals in XML file and UB04 form in UI
		float[] totalsFromXMLFile = UB04FormXMLParser.getInstance().getClaimTotals(sFileName);
		if(Verify.compareFloatArrays(totalsAfterAddClaimLine, totalsFromXMLFile)){
			report.report("Claim totals in XML file and UB04 form are same and validated successfully");
		}else{
			failCounter++;
			report.report("Fail: Claim totals in XMl file and UB04 are not same");
		}

		MySQLDBUtil.closeAllDBConnections();
		report.report("fail counter:" + String.valueOf(failCounter));
		return (failCounter == 0) ? true : false;
	}

	/*
	 * Use this method to search a claim request based on status from Advance Search page  
	 */
	public boolean selectStatusLocationInAdvanceSearchPageAndSearch(String statusLocationToSelect) throws Exception {
		boolean result = false;
		int count = 0;

		String searchResultXpath = "//td[contains(text(),'SEARCH RESULTS')]";
		WebElement searchResult = null;

		WebElement searchIcon = driver.findElement(By.id("reportHICSearch"));
		if(searchIcon != null){
			moveToElement(searchIcon);
			waitForElementVisibility(By.id("reportAdvanceSearch"));
			WebElement advanceSearch = driver.findElement(By.xpath("//*[@id='reportAdvanceSearch']"));
			safeJavaScriptClick(advanceSearch);
			selectByNameOrID("Status", statusLocationToSelect);
			clickButtonV2("Search");
			do{
				searchResult = driver.findElement(By.xpath(searchResultXpath));
				count++;
				if(searchResult != null){
					break;
				}
			}while( searchResult == null || count < 5);

			if(searchResult.getText().equalsIgnoreCase("SEARCH RESULTS")){
				report.report("Search results page got opened succssfully" + ReportAttribute.BOLD);
				result = true;
			}
		}else{
			report.report("Fail : Report HIC Search icon is not present on page");
			result = false;
		}	
		return result;
	}

	/*
	 * Use this method to search a claim request based on status from Advance Search page  
	 */
	public boolean selectClaimRecordFromSearchResults(String patientControlNumber) throws Exception {
		boolean result = false;
		String xPathPCN = "//*[@name='ub3a']";
		WebElement pcn = null;
		String sXpathOfRenchIcon = "//a[contains(text(),'"+patientControlNumber+"')]/../preceding-sibling::td[3]/span/a/img";

		waitForElementVisibility(By.xpath(sXpathOfRenchIcon));
		WebElement renchIcon = driver.findElement(By.xpath(sXpathOfRenchIcon));
		if( renchIcon != null){
			renchIcon.click();
			report.report("Clicked rench icon");
			waitForElementVisibility(By.xpath(xPathPCN));
			pcn = helper.retryUntilElementIsVisible(xPathPCN, 10);
			String pcnValue = pcn.getAttribute("value");
			WebElement lockIcon = driver.findElement(By.name("ub04lock"));
			moveToElement(lockIcon);
			safeJavaScriptClick("Resubmit");
			if(pcnValue.trim().equalsIgnoreCase(patientControlNumber)){
				report.report("Claim record has been opened in edit mode successfully", ReportAttribute.BOLD);
				result = true;
			}else{
				report.report("Fail : Unable to open claim record");
				result = false;
			}
		}
		return result;
	}


	public boolean verifyDataInEditClaimLinePopUpWindow(Map<String, String> mapAttrValues, String claimLineNumberToEdit)throws Exception{
		int failCounter = 0;
		String sEditClaimLineHeaderTextAcual = null;
		String sEditClaimLineHeaderTextExpected = "Edit Claim Line - "+ claimLineNumberToEdit;

		List<String> editClaimLineModelDailogBoxDataExpected = new ArrayList<String>();
		helper.clickMYDDELink();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrValues);
		uiactions.fillScreenAttributes(lsAttributes);

		//Edit claim line entry
		String xPathToIdentifyWhichLineToDelete = "//*[@name='ub42_"+claimLineNumberToEdit+"']";
		helper.moveToEditIcon(xPathToIdentifyWhichLineToDelete);
		if(isElementPresent(By.id("editmenutext"))){
			safeJavaScriptClick("Edit claim line");
			report.report("Clicked on edit claim line at line number : "+ claimLineNumberToEdit);
		}else{
			report.report("Failed moved mouse to edit icon");
			failCounter++;
		}
		//Get edited claim line details from JSYSTEM
		String sAllClaimLineEntries = getValueFromJSystem(lsAttributes, "ClaimLineEntries");
		String temp = sAllClaimLineEntries.split(",")[Integer.valueOf(claimLineNumberToEdit) - 1];
		String[] sEditedClaimLine = temp.split(":");
		for(String s:sEditedClaimLine){
			editClaimLineModelDailogBoxDataExpected.add(s);
		}
		//capture elements data on edit claim line dailog box
		driver.switchTo().activeElement();
		List<String> editClaimLineModelDailogBoxDataActual = helper.getFiledValuesFromEditClaimLineModelDailogWindow();

		//Get header of edit claim line model window
		WebElement editClaimLineHeader = driver.findElement(By.xpath("//div[contains(text(),'Edit Claim Line')]"));
		if(editClaimLineHeader != null){
			sEditClaimLineHeaderTextAcual = editClaimLineHeader.getText();
			if(sEditClaimLineHeaderTextAcual.equalsIgnoreCase(sEditClaimLineHeaderTextExpected)){
				report.report("Edit claim line model dailog window header validated succssfully");
			}else{
				failCounter++;
				report.report("Fail : Fail to validate edit claim line model dailog window header");
			}
		}
		if(Verify.ListEquals(editClaimLineModelDailogBoxDataExpected, editClaimLineModelDailogBoxDataActual)){
			report.report("Data in edit claim line model dailog box is validated successfully", ReportAttribute.BOLD);
		}else{
			report.report("Fail : there is a mismatch in data on edit claim line model dailog window");
			failCounter++;
		}

		report.report("Fail counter is : " + failCounter);
		return  (failCounter == 0) ? true : false;
	}
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}
}
