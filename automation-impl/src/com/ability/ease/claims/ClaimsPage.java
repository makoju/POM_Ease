package com.ability.ease.claims;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.ProviderTable;
import com.ability.ease.auto.common.ShellExecUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
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

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrVal);
		UIActions admin = new UIActions();

		//click on MY DDE link and capture Agency name
		WebElement link = waitForElementVisibility(By.linkText("MY DDE"));
		if ( link != null) {
			report.report("Clicking MY DDE link");
			clickLink("MY DDE");
		}else{
			report.report("MY DDE link is not available on page !");
			return false;
		}
		//Before clicking on UB04 form get the agency name from OVER NIGHT report screen
		waitForElementVisibility(By.xpath(sXpathToAgencyName));
		sAgencyName = driver.findElement(By.xpath(sXpathToAgencyName)).getText();
		sAgencyName = sAgencyName.substring(sAgencyName.lastIndexOf(" ") + 1);

		//filling all screen attribute values in UB04 form
		admin.fillScreenAttributes(lsAttributes);

		//***validations****//
		//validation 1 :: verify field locator 1 values
		if(!verifyFiledLocator1Values()){
			falseCounter++;
		}

		//validation 2 :: NPI Present under MY DDE Screen and UB04 should match
		String sNPILocator = "//input[@name='ub1_1']";
		String sNPIValueFromUB04 = driver.findElement(By.xpath(sNPILocator)).getAttribute("value");

		report.report("NPI Value from UB04 form :" + sNPIValueFromUB04);
		if(sAgencyName.equalsIgnoreCase(sNPIValueFromUB04.trim())){
			report.report("NPI present under MY DDE screen and NPI in UB04 are same");
		}else{
			report.report("NPI present under MY DDE screen and NPI in UB04 are not same");
			falseCounter++;
		}

		//validation 3 :: verify federal tax number



		/* validation 4  :: 0001 should present the at the bottom of the claim line entries. Total Charges and Non Covered charges should be sum of 
		 * the individual charges of each claim line entry*/

		if(! verifyTotals(lsAttributes)){
			falseCounter++;
		}

		//validation 5 :: verify the value of field locator 50 by default it is set to Z
		String sFL50Xpath = "//input[@name='preub50a']";
		String sFL50Value = driver.findElement(By.xpath(sFL50Xpath)).getAttribute("value");
		if(sFL50Value != null && sFL50Value.equals("Z")){
			report.report("File Locator contains letter 'Z'");
		}else{
			report.report("File Locator doesn't contain letter 'Z'");
			falseCounter++;
		}
		//validation 6 :: verify Field Locators 65 - Employer Name
		String sFL65Xpath = "//input[@name='ub65a']";
		WebElement oFL65Element =  driver.findElement(By.xpath(sFL65Xpath));
		List<String> elementAttrs = getAllAttributes(oFL65Element);
		if(elementAttrs.contains("readonly")){
			report.report("Filed Locator 65 (Employer Name) is disabled state");
		}else{
			report.report("Can not verify Filed Locator 65 state!!");
			falseCounter++;
		}
		
		clickLink("Submit");

		//validation 7 :: verify XML file

		if(falseCounter == 0){
			return true;
		}
		return false;
	}

	/**
	 * This method is to verify add or remove claim lines in UB04 form
	 */
	public boolean verifyAddRemoveClaimLines(Map<String, String> mapAttrVal)throws Exception{
		String sShellCommand = "cd /opt/abilitynetwork/easeweb/bin/; sudo sh easeWeb.sh status";
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Claims\\UB04.xml", mapAttrVal);
		UIActions admin = new UIActions();

		//stopping ease server
		String output = ShellExecUtil.executeShellCmd(sShellCommand);
		report.report("Output of shell command "+ sShellCommand +": "+ output);
		//click on MY DDE link and capture Agency name
		clickMYDDELink();
		//filling screen attribute values
		admin.fillScreenAttributes(lsAttributes);

		//validations
		return true;
	}



	public ProviderTable getFiledLocator1Values(String sQuery)throws Exception{
		ProviderTable providerTable = new ProviderTable();
		int rowCount = 0;
		ResultSet oResultSet = MySQLDBUtil.getResultFromMySQLDB(sQuery);
		/*if(oResultSet.last()){
			rowCount = oResultSet.getRow();
		}*/
		ResultSetMetaData rsmd = oResultSet.getMetaData();
		rowCount = rsmd.getColumnCount();
		for(int i=1; i <= rowCount ;i++){
			if(rsmd.getColumnName(i).equalsIgnoreCase("Name")){
				providerTable.setsName(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("Intermediary")){
				providerTable.setsIntermediary(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("ProviderID")){
				providerTable.setsProviderID(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("Address")){
				providerTable.setsAddress(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("City")){
				providerTable.setsCity(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("State")){
				providerTable.setsState(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("Zip")){
				providerTable.setsZip(oResultSet.getString(i));
			}
			if(rsmd.getColumnName(i).equalsIgnoreCase("Phone")){
				providerTable.setsPhone(oResultSet.getString(i));
			}
		}
		return providerTable;
	}

	//Clicks on MY DDE Link
	public void clickMYDDELink() throws Exception{
		WebElement link = waitForElementVisibility(By.linkText("MY DDE"));
		if ( link != null) {
			clickLink("MY DDE");
		}else{
			report.report("MY DDE link is not visible");
		}
	}

	//
	public boolean verifyTotals(List<Attribute> lsAttributes)throws Exception{
		float totalCharges = 0;
		float nonCoveredCharges = 0;
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
			return true;
		}else{
			return false;
		}
	}

	//Method to verify filed locator 1 values
	public boolean verifyFiledLocator1Values()throws Exception{
		ProviderTable pTable = new ProviderTable();
		String sAddressLocator = "//input[@name='ub1_2']";
		String sAddressValueFromUB04 = driver.findElement(By.xpath(sAddressLocator)).getAttribute("value");
		String sCityLocator = "//input[@name='ub1_3']";
		String sCityValueFromUB04 = driver.findElement(By.xpath(sCityLocator)).getAttribute("value");
		String sZipLocator = "//input[@name='ub1_4']";
		String sZipValueFromUB04 = driver.findElement(By.xpath(sZipLocator)).getAttribute("value");

		String sQuery = "SELECT ProviderID,Address,City,Zip from ddez.providerinfo where ProviderID = '000001'";

		pTable = getFiledLocator1Values(sQuery);
		if(pTable.getsName().equalsIgnoreCase(sAddressValueFromUB04) && pTable.getsCity().equalsIgnoreCase(sCityValueFromUB04)
				&& pTable.getsZip().equalsIgnoreCase(sZipValueFromUB04)) {
			report.report("Field Locator1 NPI, Address and Zip values are successfully compared with database values");
			return true;
		}else{
			report.report("Failed to compare field locator 1 values with database values");
			return false;
		}
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
