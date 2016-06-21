package com.ability.ease.claims;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPageVersion2 extends AbstractPageObject{

	int failCounter = 0;
	ClaimsHelper helper = new ClaimsHelper();

	//get the required re-usable element locators from property file
	String ub04Link = elementprop.getProperty("UB04_FORM_LINK");
	String myDDELink = elementprop.getProperty("MY_DDE_LINK");
	String searchResPageHdr = elementprop.getProperty("ADVANCE_SEARCH_RESULTS_PAGE_HEADER");
	String ub04LockIcon = elementprop.getProperty("UB04_LOCK_ICON");
	String ub04LockResubmit = elementprop.getProperty("UB04_LOCK__RESUBMIT_LINK");


	//C-tor
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

		UIActions fillscreen = new UIActions();
		//navigate to MY DDE page
		navigateToPage();
		WebElement searchIcon = waitForElementToBeClickable(ByLocator.id, "reportHICSearch", 60);
		moveToElement(searchIcon);
		WebElement advancesearchlink = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_SEARCH_XPATH"), 30);
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

		boolean result = false;
		String sFieldValue = null;
		List<String> lsTitles = new ArrayList<String>();

		navigateToPage();
		clickLinkV2(ub04Link);
		List<WebElement> lsUB04Fields = driver.findElements(By.xpath("//input"));
		for( WebElement field : lsUB04Fields){
			sFieldValue = field.getAttribute("title");
			if(sFieldValue != null){
				lsTitles.add(sFieldValue);
			}
		}

		return result;
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
	public boolean checkContentInRelatedClaims() throws Exception{

		boolean stepResult = false;

		try{
			if(isElementPresent(By.id(elementprop.getProperty("NEW_UB04_ID")))){
				if(waitForElementToBeClickable(ByLocator.name, elementprop.getProperty("UB04_LOCK_ICON"), 30) != null){
					moveToElement(elementprop.getProperty("RELATED_CLAIMS_LINK_TEXT"));
					safeJavaScriptClick(elementprop.getProperty("PREVIUOS_EPSD_FINAL_LINK"));
					if(waitForElementToBeClickable(ByLocator.name, ub04LockIcon, 60) != null){
						stepResult = true;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return stepResult;

	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		try{
			WebElement myddelink = waitForElementToBeClickable(ByLocator.linktext, myDDELink, 60);
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