package com.ability.ease.claims;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.dataStructure.common.easeScreens.UIAttribute;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPage extends AbstractPageObject{


	//C-tor
	public ClaimsPage() {
		super();
	}


	/**
	 * This method is to verify all the fields in UB04 form
	 */
	public boolean verifyUB04FormFields(Map<String, String> mapAttrVal)throws Exception{
		String sXpathToAgencyName = "//span[contains(text(),'OVERNIGHT')]/..";
		String sAgencyName = null;
		int falseCounter = 0;
		float totalCharges = 0;
		float nonCoveredCharges = 0;

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrVal);
		UIActions admin = new UIActions();

		//click on MY DDE link and capture Agency name
		WebElement link = waitForElementVisibility(By.linkText("MY DDE"));
		if ( link != null) {
			clickLink("MY DDE");
		}else{
			report.report("MY DDE link is available on page !");
			falseCounter++;
		}
		sAgencyName = driver.findElement(By.xpath(sXpathToAgencyName)).getText();
		sAgencyName = sAgencyName.substring(sAgencyName.lastIndexOf(" ") + 1);

		//filling all screen attributes in UB04 form
		admin.fillScreenAttributes(lsAttributes);

		//verify field locator 1 values

		/*
		 * NPI Present under MY DDE Screen and UB04 should match
		 */
		String sNPILocator = "//input[@name='ub1_1']";
		String sNPIValueFromUB04 = driver.findElement(By.xpath(sNPILocator)).getAttribute("value");
		report.report("NPI Value from UB04 form :" + sNPIValueFromUB04);
		if(sAgencyName.equalsIgnoreCase(sNPIValueFromUB04.trim())){
			report.report("NPI present under MY DDE screen and NPI in UB04 are same");
		}else{
			report.report("NPI present under MY DDE screen and NPI in UB04 are not same");
			//falseCounter++;
		}


		//verify federal tax number


		/*
		 * 0001 should present the at the bottom of the claim line entries. Total Charges and Non Covered charges should be sum of 
		 * the individual charges of each claim line entry
		 */
		String sTotalChargesLocator = "//*[contains(text(),'TOTALS')]/following-sibling::li[1]";
		String actualTotalCharge = driver.findElement(By.xpath(sTotalChargesLocator)).getAttribute("value");
		String sTotalNonCoveredChargesLocator = "//*[contains(text(),'TOTALS')]/following-sibling::li[2]";
		String actualNonCoveredTotalCharge = driver.findElement(By.xpath(sTotalNonCoveredChargesLocator)).getAttribute("value");
		for(Attribute scrAttr:lsAttributes){
			if(scrAttr.getDisplayName().equalsIgnoreCase("ClaimLineEntries")){
				String[] sLocatorsClaim = scrAttr.getLocator().split(",");
				String[] sValue = scrAttr.getValue().split(",");
				int j=1;

				for(String str:sValue){
					String[] sValues = str.split(":");
					for(int i=0;i < sValues.length; i++){
						if(sLocatorsClaim[i].contains("ub47")){
							String tempTotalCharge = sValues[i];
							if( tempTotalCharge != null){
								totalCharges = totalCharges + Float.valueOf(tempTotalCharge);
							}
						}
						if(sLocatorsClaim[i].contains("ub48")){
							String tempNonCoveredCharges = sValues[i];
							if( tempNonCoveredCharges != null){
								nonCoveredCharges = nonCoveredCharges + Float.valueOf(tempNonCoveredCharges);
							}
						}
						j++;
					}
				}
			}
		}
		if(Float.valueOf(actualTotalCharge) == totalCharges && Float.valueOf(actualNonCoveredTotalCharge) == nonCoveredCharges){
			report.report("Total charges and non covered charges is equivalent to individual charges of each claim line entry");
		}else{
			falseCounter++;
		}
		//verify the value of field locator 50 by default it is set to Z

		//Verify  Field Locators 65 - Employer Name
		
		
		clickLink("Submit");

		if(falseCounter == 0){
			return true;
		}
		return false;
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
