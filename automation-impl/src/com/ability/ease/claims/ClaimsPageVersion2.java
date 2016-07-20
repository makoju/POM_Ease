package com.ability.ease.claims;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPageVersion2 extends AbstractPageObject {

	int failCounter = 0;
	boolean stepResult = false;
	ClaimsHelper helper = new ClaimsHelper();
	UIActions fillscreen = new UIActions();

	//get the required re-usable element locators from property file
	String ub04Link = elementprop.getProperty("UB04_FORM_LINK");
	String myDDELink = elementprop.getProperty("MY_DDE_LINK");
	String searchResPageHdr = elementprop.getProperty("ADVANCED_SEARCH_RESULTS_PAGE_HEADER");
	String ub04LockIcon = elementprop.getProperty("UB04_LOCK_ICON");
	String ub04LockResubmit = elementprop.getProperty("UB04_LOCK__RESUBMIT_LINK");
	String patientInfoPageHeaderXpath = elementprop.getProperty("PATIENT_INFO_TD_HEADR_XPATH");
	String reportHICSearch = elementprop.getProperty("MAGINFIER_ICON_ID");

	// C-tor
	public ClaimsPageVersion2() {
		super();
	}

	/**
	 * This method perform edit claim line,provided that there should be a rejected claim already opened 
	 * @param HIC
	 * @param claimLineToEdit
	 * @return
	 * @throws Exception
	 */
	public boolean verifyEditClaimLineOptionAvailability(String claimLineToEdit, String expectedOutput)throws Exception{

		boolean stepResult = false;
		String editIconXpath = MessageFormat.format(elementprop.getProperty("EDIT_CLAIM_LINE_XPATH"), "'ub42_"+claimLineToEdit+"'");
		List<WebElement> editMenuList = new ArrayList<WebElement>();

		WebElement lockIcon = waitForElementToBeClickable(ByLocator.name, ub04LockIcon, 60);

		if (lockIcon != null){
			moveToElement(lockIcon);
			safeJavaScriptClick(ub04LockResubmit);
			helper.moveToEditIcon(editIconXpath);
			editMenuList = driver.findElements(By.xpath(elementprop.getProperty("EDIT_MENU_OPTIONS_XPATH")));
			for( WebElement we: editMenuList){
				if( we.getText().equalsIgnoreCase(expectedOutput)){	
					report.report( we.getText() + " is availble for given claim line");
					stepResult = true;
					break;
				}
			}
		}

		return stepResult;
	}


	/**
	 * This method just open a claim record using the filters provided in advanced search page
	 * @param mapAttrVal
	 * @param patientControlNumber
	 * @return
	 * @throws Exception
	 */
	public boolean openClaimRecordFromAdvanceSearchPage(Map<String, String> mapAttrVal, String patientControlNumber)
			throws Exception{

		boolean stepResult = false;
		String claimRenchIconXpath = MessageFormat.format(elementprop.getProperty("CLAIM_RENCH_ICON_ADV_SEARCH_PAGE"), "'"+patientControlNumber+"'");

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\AdvancedSearch.xml", mapAttrVal);

		//navigate to MY DDE page
		navigateToPage();
		WebElement searchIcon = waitForElementToBeClickable(ByLocator.id, reportHICSearch, 60);
		//WebElement searchIcon = waitUntilElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH_ICON")));
		moveToElement(searchIcon);
		WebElement advancesearchlink = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_SEARCH_XPATH"), 60);
		//WebElement advancesearchlink  = waitUntilElementVisibility(By.xpath(elementprop.getProperty("ADV_SEARCH_XPATH")));
		safeJavaScriptClick(advancesearchlink);
		report.report("Clicked on Advanced Search link");
		//fill advance search page filters and click search
		fillscreen.fillScreenAttributes(lsAttributes);
		clickButtonV2(elementprop.getProperty("SEARCH_BUTTON"));

		//wait for search result to be displayed
		if(waitForElementToBeClickable(ByLocator.xpath, searchResPageHdr, 60) != null){
			WebElement renchIcon = waitForElementToBeClickable(ByLocator.xpath, claimRenchIconXpath, 30);
			renchIcon.click();
			if (waitForElementToBeClickable(ByLocator.name, ub04LockIcon, 30) != null){
				report.report("Claim has been opened in edit mode");
				stepResult = true;
			}
		}

		return stepResult;
	}


	public boolean verifyHelpTextInUB04Form() throws Exception{

		String sHelpTextFieldLocators = elementprop.getProperty("HELP_TEXT_LOCATOR_NAMES");
		String locators[] = sHelpTextFieldLocators.split(",");
		String titleAttrVal = null;
		int passCounter = 0;

		try{
			if(waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("TOB_XPATH"), 10) != null){
				for(String locator:locators){	
					WebElement ub04Field = driver.findElement(By.xpath("//input[@name='" + locator +"']"));
					titleAttrVal = ub04Field.getAttribute("title");
					if(titleAttrVal != " " && !titleAttrVal.isEmpty()){
						report.report("Help text of locator '" + locator + "' is : " + titleAttrVal);
						passCounter++;
					}else{
						titleAttrVal = driver.findElement(By.xpath("//input[@name='" + locator +"']")).getAttribute("oldtitle");
						if(titleAttrVal != "" && !titleAttrVal.isEmpty()){
							report.report("Help text of locator '" + locator + "' is : " + titleAttrVal);
							passCounter++;
						}
					}
				}
			}
		}catch(Exception e){
			report.report("Exception occured while getting the help text from UB04 form fields");
		}
		return (passCounter == locators.length) ? true : false;
	}

	/**
	 * This method is used to check the individual claim charges with total claim charges for bot covered and non covered
	 * @param coveredOrNonCovered - COVERED or NOT-COVERED
	 * @return
	 * @throws Exception
	 */
	public boolean verifyTotChargesWithIndividualCharges(String coveredOrNonCovered)throws Exception{
		boolean stepResult = false;
		float expectedTotalCharges = 0;
		float actualTotalCharges = 0;

		String totalChargesCellXpath = null;

		totalChargesCellXpath = helper.setTotalChargesCellXPATH(coveredOrNonCovered);

		try{
			expectedTotalCharges = Float.valueOf(helper.getValueOfValueAttr(totalChargesCellXpath));
			actualTotalCharges = helper.validateTotalChargeClaimLineCount(coveredOrNonCovered);

			if(expectedTotalCharges > 0 && actualTotalCharges  > 0){
				if(expectedTotalCharges == actualTotalCharges){
					stepResult = true;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return stepResult;
	}

	/**
	 * This method is used to verify the claim content between related claims
	 * @return
	 * @throws Exception
	 */
	public boolean checkContentInRelatedClaims(String coveredOrNonCovered) throws Exception{

		boolean stepResult = false;
		float totalsFromPreviousClaim = 0;
		float totalsFromRelatedClaims = 0;

		try{
			if(isElementPresent(By.id(elementprop.getProperty("NEW_UB04_ID")))){
				if(waitForElementToBeClickable(ByLocator.name, elementprop.getProperty("UB04_LOCK_ICON"), 30) != null){
					totalsFromPreviousClaim = helper.validateTotalChargeClaimLineCount(coveredOrNonCovered);
					driver.findElement(By.name(elementprop.getProperty("CLAIM_FORM_CLOSE_NAME"))).click();
					verifyAlert("Are you sure you want to discard this claim?");
					moveToElement(elementprop.getProperty("RELATED_CLAIMS_LINK_TEXT"));
					clickLink(elementprop.getProperty("PREVIUOS_EPSD_FINAL_LINK"));
					Thread.sleep(5000);
					if(waitUntilElementVisibility(By.name(ub04LockIcon)) != null){
						totalsFromRelatedClaims = helper.validateTotalChargeClaimLineCount(coveredOrNonCovered);
						if ( totalsFromPreviousClaim > 0 && totalsFromRelatedClaims > 0){
							report.report("Claim totals from previous claim - " + totalsFromPreviousClaim);
							report.report("Claim totals from related claim ( previous episode final ) - " + totalsFromRelatedClaims);
							if(totalsFromPreviousClaim != totalsFromRelatedClaims){
								report.report("Claims total are not swapped in related claims");
								stepResult = true;
							}
						}

					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stepResult;

	}

	public float getTotalClaimCharges(String coveredOrNonCovered) throws Exception{
		float totalCharges = 0;
		totalCharges = helper.validateTotalChargeClaimLineCount(coveredOrNonCovered);
		return totalCharges;
	}

	public boolean unlockClaim(String unlockOption) throws Exception{

		boolean stepResult = false;
		WebElement lockIcon = null;
		String beforeUnlockReadOnllyAttrVal = null;
		String afterUnlockReadOnlyAttrVal = null;

		try{
			lockIcon = waitForElementToBeClickable(ByLocator.name, ub04LockIcon, 30);
			moveToElement(lockIcon);
			WebElement firstClaimLine = waitUntilElementVisibility(By.name(elementprop.getProperty("FIRST_CLAIM_LINE_NAME")));
			beforeUnlockReadOnllyAttrVal = firstClaimLine.getAttribute("readonly");
			safeJavaScriptClick(unlockOption);
			firstClaimLine = waitUntilElementVisibility(By.name(elementprop.getProperty("FIRST_CLAIM_LINE_NAME")));
			afterUnlockReadOnlyAttrVal = firstClaimLine.getAttribute("readonly");
			if(beforeUnlockReadOnllyAttrVal.equalsIgnoreCase("true") && afterUnlockReadOnlyAttrVal == null){
				report.report("Unlocked claim with " + unlockOption + " !!!");
				stepResult = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stepResult;
	}


	public void addClaimNewLine(String claimLineDetails, String claimLinePosition) throws Exception{

		//boolean stepResult = false;
		String temp[] = claimLinePosition.split(",");
		String sLineNumber = temp[0];
		String sPosition = temp[1];		
		helper.moveAndClick(sLineNumber, sPosition);
		helper.addClaimLine(claimLineDetails, sLineNumber, sPosition);
	}

	public boolean resubmitAdjustedClaim(){

		boolean stepResult = false;


		return stepResult;

	}

	public boolean tallyClaimChargesAfterAdjustments(String coveredOrNonCovered,float previousClaimTotals, 
			String claimLineDetails) throws Exception{

		float updatedClaimTotalCharges = 0;
		String[] newClaimLineDetails = claimLineDetails.split(":");
		float correctedClaimCharge = 0;
		WebElement revCode = null;

		try {

			if( coveredOrNonCovered.equalsIgnoreCase("COVERED")){
				correctedClaimCharge = Float.valueOf(newClaimLineDetails[newClaimLineDetails.length - 2]);
			}else if( coveredOrNonCovered.equalsIgnoreCase("NON-COVERED")){
				correctedClaimCharge = Float.valueOf(newClaimLineDetails[newClaimLineDetails.length - 1]);
			}

			revCode = waitUntilElementVisibility(By.xpath(elementprop.getProperty("REV_CODE_001_XPATH")));
			if(revCode != null ){
				revCode.click();
				updatedClaimTotalCharges = getTotalClaimCharges(coveredOrNonCovered);

				report.report("Previous claim totals for " + coveredOrNonCovered + " are : " + previousClaimTotals);
				report.report("Corrected claim " + coveredOrNonCovered + " charge : " + correctedClaimCharge);
				report.report("Total claim charges of " + coveredOrNonCovered + " after correction are : " + updatedClaimTotalCharges);

				if( updatedClaimTotalCharges > 0 && (previousClaimTotals > 0 && correctedClaimCharge > 0 ))
					if ( updatedClaimTotalCharges == (previousClaimTotals + correctedClaimCharge) ){
						report.report("Claim totals are tallied successfully after correction");
						stepResult = true;
						//helper.comeBackToHomePage();
					}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stepResult;
	}

	public boolean undoChanges(String undoOption) throws Exception{

		String undoOptions[] = {"Undo all changes" , "Reset claim to original"};
		List<WebElement> lsUndoOptions = null;
		List<String> lsOptionValues = new ArrayList<String>();
		String temp = null;

		WebElement undoLink = waitUntilElementVisibility(By.id(elementprop.getProperty("UNDO_CHANGES_ID")));
		if( undoLink != null){
			moveToElement(undoLink);
			lsUndoOptions = driver.findElements(By.xpath(elementprop.getProperty("UNDO_CHANGES_OPTIONS_XPATH")));

			for( WebElement option : lsUndoOptions){
				temp = option.getText();
				if(temp != null){
					lsOptionValues.add(temp);
				}
			}

			if ( ! Verify.listEquals(lsOptionValues, Arrays.asList(undoOptions))){
				failCounter++;
			}

			moveToElement(undoOption);
			clickLink(undoOption);
			//safeJavaScriptClick(undoOption);
			if ( !verifyAlert("Are you sure?") ){
				failCounter++;
			}

		}
		return (failCounter == 0 ) ? true : false;
	}

	public boolean verifyPatientInformation(String patientControlNumber)throws Exception{

		String temp[], expectedHICValue=null;
		StringBuilder expectedPCNNumber = new StringBuilder("PCN");
		String actualHICValue = null,actualPCNNumber=null;

		if( patientControlNumber != null){
			temp = patientControlNumber.split("-");
			expectedHICValue = temp[1];
			expectedPCNNumber.append(temp[1].substring(4, 9));
		}

		WebElement patientInfoLink = waitUntilElementVisibility(By.linkText(elementprop.getProperty("PATIENT_PAGE_LINK")));

		if(patientInfoLink != null){
			patientInfoLink.click();
		}else{
			report.report("Patient page link is not visible on claims page!!!");
		}

		if( waitUntilElementVisibility(By.xpath(patientInfoPageHeaderXpath)) != null){
			actualHICValue = driver.findElement(By.xpath(elementprop.getProperty("HIC_TEXT_XPATH"))).getText();
			actualPCNNumber = driver.findElement(By.xpath(elementprop.getProperty("PATIENT_CNTRL_TEXT_XPATH"))).getText();
		}

		report.report("Expected HIC value is : " + expectedHICValue + " and actual HIC value is : " + actualHICValue);
		report.report("Expected PCN # is : " + expectedPCNNumber + " and actual PCN # is : " + actualPCNNumber);

		return (actualHICValue.equalsIgnoreCase(expectedHICValue)
				&& actualPCNNumber.equalsIgnoreCase(expectedPCNNumber.toString())) ? true : false;
	}


	public boolean verifyValidationFor0001RevCode(Map<String, String> mapAttrValues)throws Exception{


		String expectedValidationMsg = "* 0001 code cannot be used here.";
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrValues);

		fillscreen.fillScreenAttributes(lsAttributes);
		clickLinkV2("claimSubmit");
		WebElement we = waitUntilElementVisibility(By.xpath(elementprop.getProperty("0001_REV_CODE_ERR_XPATH")));

		if( we != null){
			report.report("Expected 0001 rev code validation message : " + expectedValidationMsg);
			report.report("Actual 0001 rev code validation message : " + we.getText());
			//helper.comeBackToHomePage();
			stepResult = true;
		}

		return stepResult;
	}

	public boolean openNewUB04Form()throws Exception{

		navigateToPage();
		WebElement ub04 = waitForElementToBeClickable(ByLocator.id, ub04Link, 10);
		if( ub04 != null){
			clickLinkV2(ub04Link);
			if( waitUntilElementVisibility(By.id(elementprop.getProperty("NEW_UB04_ID"))) != null){
				report.report("New claim form has been opened successfully!!!");
				stepResult = true;
			}else{
				report.report("Failed to open new claim form");
			}
		}

		
		return stepResult;
	}

	public boolean fillUB04FormAndValidateXMLFile(Map<String, String> mapAttrValues)throws Exception{

		String sRequestDetails[];
		String sClaimRequestID = null, sClaimRequestXML = null, sFileName=null;
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+
				"uiattributesxml\\Claims\\UB04.xml", mapAttrValues);

		String startTime = ClaimsHelper.getCurrentTimeFromEaseDB();
		fillscreen.fillScreenAttributes(lsAttributes);
		clickLinkV2("claimSubmit");

		if (helper.handleSubmitWarningAlert(lsAttributes) ){
			
			Thread.sleep(3000);
			String endTime = ClaimsHelper.getCurrentTimeFromEaseDB();
			sRequestDetails = helper.getUB04XMLFromDatabase(startTime, endTime);
			sClaimRequestID = sRequestDetails[0];
			sClaimRequestXML = sRequestDetails[1];
			sFileName = TestCommonResource.getTestResoucresDirPath()+"UB04XMLs\\Claim_"+sClaimRequestID+".xml";

			if( helper.validateXMLFileFields(lsAttributes, sClaimRequestXML, sFileName)){
				report.report("XML file field data validation is completed successfully");
			}else{
				failCounter++;
				report.report("FAIL : XML file field data validation is failed");
			}
			report.report("Successfully filled values in UB04 form");
			stepResult = true;
		}else{
			failCounter++;
			report.report("Fail : Error while filling values in UB04 form");
		}
		return (failCounter == 0) ? true : false;
	}

	public boolean openUB04FormWithPatientDetails(Map<String, String> mapAttrValues)throws Exception{

		WebElement ub04Icon = waitForElementToBeClickable(ByLocator.id, ub04Link, 30);

		if(ub04Icon != null){
			ub04Icon.click();
			stepResult = helper.validatePatientDetailsInNewUb04Form(mapAttrValues);
			if( !stepResult){
				report.report("Patient details were incorrect in the new UB04 for opened from patient info page!!!");
			}
		}
		//helper.comeBackToHomePage();
		return stepResult;
	}

	public boolean navigateToPatientInfoPageFromAdvancedSearchPage(Map<String, String> mapAttrValues, 
			String patientControlNumber) throws Exception{

		String claimHICLinkXpath = MessageFormat.format(elementprop.getProperty("HIC_XPATH"), "'"+patientControlNumber+"'");

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+
				"uiattributesxml\\MyDDE\\AdvancedSearch.xml", mapAttrValues);

		navigateToPage();
		WebElement searchIcon = waitForElementToBeClickable(ByLocator.id, reportHICSearch, 60);
		moveToElement(searchIcon);
		WebElement advancesearchlink = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_SEARCH_XPATH"), 60);
		safeJavaScriptClick(advancesearchlink);
		report.report("Clicked on Advanced Search link");
		fillscreen.fillScreenAttributes(lsAttributes);
		clickButtonV2(elementprop.getProperty("SEARCH_BUTTON"));
		//wait for search result to be displayed
		if(waitForElementToBeClickable(ByLocator.xpath, searchResPageHdr, 60) != null){
			WebElement hicLink = waitForElementToBeClickable(ByLocator.xpath, claimHICLinkXpath, 30);
			hicLink.click();
			if (waitForElementToBeClickable(ByLocator.xpath, patientInfoPageHeaderXpath, 30) != null){
				report.report("Patient information page has been opened successfully from advanced search result page!!!");
				stepResult = true;
			}
		}

		return stepResult;
	}

	public boolean comeBackToHomePage()throws Exception{
		return helper.comeBackToHomePage();
	}

	public String getClaimReqStartTimeFromEASEDB()throws Exception{
		return ClaimsHelper.getCurrentTimeFromEaseDB();
	}

	public String getClaimReqEndTimeFromEASEDB()throws Exception{
		return ClaimsHelper.getCurrentTimeFromEaseDB();
	}


	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		try{
			WebElement myddelink = waitForElementToBeClickable(ByLocator.linktext, myDDELink, 80);
			//WebElement myddelink = waitUntilElementVisibility(By.linkText(myDDELink));
			String classAttr = myddelink.getAttribute("class");
			if ( myddelink != null) {
				if( classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected") && 
						waitForElementToBeClickable(ByLocator.id, ub04Link, 30) == null){
					safeJavaScriptClick(myDDELink);
					report.report("Clicked on MY DDE link");
				}else{
					safeJavaScriptClick(myddelink);
					waitForElementToBeClickable(ByLocator.id, ub04Link, 30);
				}
			}
		}catch(Exception e){
			report.report("MY DDE Link is not visible to click...!!!");
		}
	}
}