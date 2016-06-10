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

	public boolean verifyEditClaimLineOptions(String HIC, String claimLineToEdit)throws Exception{

		String renchIconXpath = MessageFormat.format(elementprop.getProperty("RENCH_ICON_XPATH"), "'"+HIC+"'");
		String editIconXpath = MessageFormat.format(elementprop.getProperty("EDIT_CLAIM_LINE_XPATH"), "'ub42_"+claimLineToEdit+"'");

		List<WebElement> editMenuList = new ArrayList<WebElement>();

		helper.openRejectedClaimRecordFromAdvanceSearchPage(HIC);
		if (waitForElementToBeClickable(ByLocator.xpath, searchResPageHdr, 15) != null){
			WebElement renchIcon = waitForElementToBeClickable(ByLocator.xpath, renchIconXpath, 5);
			if( renchIcon != null){
				renchIcon.click();
				WebElement lockIcon = waitForElementToBeClickable(ByLocator.name, ub04LockIcon, 15);
				moveToElement(lockIcon);
				safeJavaScriptClick(ub04LockResubmit);
				helper.moveToEditIcon(editIconXpath);
				editMenuList = driver.findElements(By.xpath(elementprop.getProperty("EDIT_MENU_OPTIONS_XPATH")));

				report.report("Options from edit icon menu");
				for( WebElement we: editMenuList){
					report.report(we.getText());
				}
			}

		}


		return failCounter == 0 ? true : false;
	}


	public boolean openClaimRecordFromAdvanceSearchPage(Map<String, String> mapAttrVal, String patientControlNumber)
			throws Exception{

		boolean stepResult = false;
		String claimRenchIconXpath = MessageFormat.format(elementprop.getProperty("CLAIM_RENCH_ICON_ADV_SEARCH_PAGE"), "'"+patientControlNumber+"'");

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\AdvancedSearch.xml", mapAttrVal);

		UIActions fillscreen = new UIActions();
		//navigate to MY DDE page
		navigateToPage();
		WebElement searchIcon = waitForElementToBeClickable(ByLocator.id, "reportHICSearch", 30);
		moveToElement(searchIcon);
		WebElement advancesearchlink = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADV_SEARCH_XPATH"), 10);
		safeJavaScriptClick(advancesearchlink);
		report.report("Clicked on Advanced Search link");
		//fill advance search page filters and click search
		fillscreen.fillScreenAttributes(lsAttributes);
		clickButton(elementprop.getProperty("SEARCH_BUTTON"));

		//wait for search result to be displayed
		if(waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty(searchResPageHdr), 60) != null){
			WebElement renchIcon = waitForElementToBeClickable(ByLocator.xpath, claimRenchIconXpath, 30);
			renchIcon.click();
			if (waitForElementToBeClickable(ByLocator.name, elementprop.getProperty("UB04_LOCK_ICON"), 30) != null){
				stepResult = true;
			}
		}

		return stepResult;
	}




	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {

		WebElement myddelink = waitForElementToBeClickable(ByLocator.linktext, myDDELink, 30);
		String classAttr = myddelink.getAttribute("class");

		if ( myddelink != null) {
			if( !classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				safeJavaScriptClick(myDDELink);
				report.report("Clicked on MY DDE link");
			}
		} 
	}


}