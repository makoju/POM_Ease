package com.ability.ease.appealmanagement;

import java.util.ArrayList;
import java.util.List;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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