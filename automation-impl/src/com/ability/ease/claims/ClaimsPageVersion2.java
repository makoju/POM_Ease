package com.ability.ease.claims;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPageVersion2 extends AbstractPageObject{

	int failCounter = 0;
	ClaimsHelper helper = new ClaimsHelper();

	//get the required element locators from property file
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

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		WebElement mydde = waitForElementToBeClickable(ByLocator.linktext, myDDELink, 30);
		String classAttr = mydde.getAttribute("class");
		if ( mydde != null) {
			if( !classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				safeJavaScriptClick(myDDELink);
				WebElement ub04 = waitForElementToBeClickable(ByLocator.id, ub04Link, 30); 
				if(ub04  != null){
					safeJavaScriptClick(ub04);
				}
			}else{
				//nothing to do
			}
		}
		report.report("MY DDE Link element is not avaible on page");
	}


}