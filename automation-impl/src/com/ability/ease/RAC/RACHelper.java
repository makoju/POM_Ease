package com.ability.ease.RAC;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class RACHelper extends AbstractPageObject {
	/*
	 * Method to navigate to the RAC Submission Page
	 */
	public void navigateToRACSubmission() throws Exception {
		waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("esMD_TOPMENU_LINKTEXT"), 60);
		safeJavaScriptClick(elementprop.getProperty("esMD_TOPMENU_LINKTEXT"));
		report.report("Navigate to esMD Top Ribbon!");
		waitForElementToBeClickable(ByLocator.linktext, "RAC Submission", 60);
		safeJavaScriptClick("RAC Submission");
		report.report("Navigate to RAC Submissoin Page!");
	}

	/*
	 * Method to enable Date field in the ADD RAC Page
	 */
	public void removeReadOnlyAttr() throws Exception{
		waitForElementToBeClickable(ByLocator.linktext,elementprop.getProperty("ADD_RAC_LINKTEXT") , 60);
		safeJavaScriptClick(elementprop.getProperty("RAC_SUBMISSION_LINKTEXT"));
		waitForElementToBeClickable(ByLocator.name,elementprop.getProperty("PATIENT_HIC_NAME") , 60);
		((JavascriptExecutor) driver)
				.executeScript("document.getElementById('StartDOSParam').removeAttribute('readonly');");
		driver.findElement(By.id(elementprop.getProperty("START_DATE_NAME"))).clear();
		((JavascriptExecutor) driver)
				.executeScript("document.getElementById('EndDOSParam').removeAttribute('readonly');");
		driver.findElement(By.id(elementprop.getProperty("THROUGH_DATE_NAME"))).clear();
		((JavascriptExecutor) driver)
				.executeScript("document.getElementById('LetterDateParam').removeAttribute('readonly');");
		driver.findElement(By.id(elementprop.getProperty("LETTER_DATE_NAME"))).clear();
		
		
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
