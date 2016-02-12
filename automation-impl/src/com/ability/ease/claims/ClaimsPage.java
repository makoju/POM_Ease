package com.ability.ease.claims;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.ProviderTable;
import com.ability.ease.auto.common.ShellExecUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UB04FormXMLParser;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.mydde.reports.MyDDEReportsPage;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsPage extends AbstractPageObject{


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
		int falseCounter = 0;
		String sClaimRequestXML = null;
		int iClaimRequestID = 0;

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
		//Before start filling new claim form get the agency name from OVER NIGHT report screen
		waitForElementVisibility(By.xpath(sXpathToAgencyName));
		sAgencyName = driver.findElement(By.xpath(sXpathToAgencyName)).getText();
		sAgencyName = sAgencyName.substring(sAgencyName.lastIndexOf(" ") + 1);

		//Get current time from ease Database
		String startTime = getCurrentTimeFromEaseDB();
		//filling all screen attribute values in UB04 form
		admin.fillScreenAttributes(lsAttributes);

		//***validations****//
		//validation 1 :: verify field locator 1 values
		if(!verifyFiledLocator1Values(lsAttributes)){
			falseCounter++;
		}

		//validation 2 :: NPI Present under MY DDE Screen and UB04 should match
		String sNPILocator = "//input[@name='ub1_1']";
		String sNPIValueFromUB04 = driver.findElement(By.xpath(sNPILocator)).getAttribute("value");

		report.report("NPI Value from UB04 form :" + sNPIValueFromUB04, ReportAttribute.BOLD);
		if(sAgencyName.equalsIgnoreCase(sNPIValueFromUB04.trim())){
			report.report("Validation : NPI present under MY DDE screen and NPI in UB04 are same", ReportAttribute.BOLD);
		}else{
			report.report("NPI present under MY DDE screen and NPI in UB04 are not same");
			falseCounter++;
		}

		//validation 3 :: verify federal tax number
		String sFedTaxNoLocator = "//input[@name='ub5']";
		String sActualFedTaxNoFromUB04 = driver.findElement(By.xpath(sFedTaxNoLocator)).getAttribute("value");
		String sExpectedFedTaxNum = getValueFromJSystem(lsAttributes, "Federal Tax No");
		if(! sActualFedTaxNoFromUB04.equalsIgnoreCase(sExpectedFedTaxNum)){
			report.report("Failed to verify federal tax number displayed in UB04 form");
			falseCounter++;
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
			falseCounter++;
		}
		//validation 5 :: verify Field Locators 65 - Employer Name
		String sFL65Xpath = "//input[@name='ub65a']";
		WebElement oFL65Element =  driver.findElement(By.xpath(sFL65Xpath));
		String sReadOnlyAttrValue = oFL65Element.getAttribute("readonly");
		if(sReadOnlyAttrValue != null){
			report.report("Validation : Employer name field is disabled", ReportAttribute.BOLD);
		}else{
			report.report("Employer name field is editabled", Reporter.FAIL);
			falseCounter++;
		}

		clickLinkV2("claimSubmit");
		if(validateConfirmationScreenSteps(lsAttributes)){
			clickButton("yesConfirmEditClaimButton");
			report.report("Claim request has been submitted successfully", ReportAttribute.BOLD);
		}
		else{
			report.report("There are some problems in form filling, please fill all mandatory values in UB04 form");
			falseCounter++;
		}

		//get current time from ease DB
		Thread.sleep(5000);
		String endTime = getCurrentTimeFromEaseDB();
		//validation 6 :: verify XML file

		String sSQLQueryToGetTheXMLFile = "Select RequestID, Request from ddez.userdderequest where SubmitTime BETWEEN '"+ 
				startTime.substring(0, startTime.length()-2) +"' AND "+ "'"+ endTime.substring(0, endTime.length()-2) + "'";

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(sSQLQueryToGetTheXMLFile);
		while(rs.next()){
			iClaimRequestID = rs.getInt("RequestID");
			sClaimRequestXML = rs.getString("Request");
		}

		String sFileName = TestCommonResource.getTestResoucresDirPath()+"claimsxmlforms\\Claim_"+iClaimRequestID+".xml";
		if(sClaimRequestXML != null){
			stringToDom(sClaimRequestXML, sFileName);
		}else{
			report.report("Can not submit a claim rquest with same TOB (Type of bill), please provide a different TOB and try again");
			falseCounter++;
		}

		if( UB04FormXMLParser.getInstance().validateUB04XML(lsAttributes, sFileName)){
			report.report("XML file validation is completed successfully", ReportAttribute.BOLD);
		}else{
			falseCounter++;
			report.report("XML file validation failed!");
		}

		/* validation 7  :: 0001 should present the at the bottom of the claim line entries. Total Charges and Non Covered charges should be sum of 
		 * the individual charges of each claim line entry*/

		if(! verifyTotals(lsAttributes, sFileName)){
			falseCounter++;
		}
		MySQLDBUtil.closeAllDBConnections();
		report.report("Fail counter :" + falseCounter);
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
		ResultSet oResultSet = MySQLDBUtil.getResultFromMySQLDB(sQuery);
		ResultSetMetaData rsmd = oResultSet.getMetaData();
		int rowCount = rsmd.getColumnCount();

		while(oResultSet.next()){
			for(int i=1; i <= rowCount; i++){
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
		}

		oResultSet.close();
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
	public boolean verifyTotals(List<Attribute> lsAttributes, String sFile)throws Exception{
		float totalCharges = 0;
		float nonCoveredCharges = 0;

		/*String sTotalChargesLocator = "//*[contains(text(),'TOTALS')]/following-sibling::li[1]";
		WebElement actualTotalCharge = driver.findElement(By.xpath(sTotalChargesLocator));
		String sActualTotlaCharges = getElementValueJS(actualTotalCharge, "prevval");
		String sTotalNonCoveredChargesLocator = "//*[contains(text(),'TOTALS')]/following-sibling::li[2]";
		WebElement actualNonCoveredTotalCharge = driver.findElement(By.xpath(sTotalNonCoveredChargesLocator));
		String sActualNonCoveredCharges = getElementValueJS(actualNonCoveredTotalCharge, "prevval");
		 */
		float[] claimTotals = UB04FormXMLParser.getInstance().getClaimTotals(sFile);

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

		report.report("Expected total charges are : " + String.valueOf(totalCharges));
		report.report("Expected total non covered charges are : " + String.valueOf(nonCoveredCharges));

		if(claimTotals[0] == UB04FormXMLParser.round(totalCharges,2) && claimTotals[1] == UB04FormXMLParser.round(nonCoveredCharges,2)){
			report.report("Total charges and non covered charges is equivalent to individual charges of each claim line entry", ReportAttribute.BOLD);
			return true;
		}else{
			report.report("Totla charges and non covered charges are not equal");
			return false;
		}
	}

	//Method to verify filed locator 1 values
	public boolean verifyFiledLocator1Values(List<Attribute> lsAttributes)throws Exception{

		String sNPILocator = "//input[@name='ub1_1']";
		String sActualNPIValueFromUB04 = driver.findElement(By.xpath(sNPILocator)).getAttribute("value");
		String sAddressLocator = "//input[@name='ub1_2']";
		String sActualAddressValueFromUB04 = driver.findElement(By.xpath(sAddressLocator)).getAttribute("value");
		String sCityLocator = "//input[@name='ub1_3']";
		String sActualCityValueFromUB04 = driver.findElement(By.xpath(sCityLocator)).getAttribute("value");
		String sZipLocator = "//input[@name='ub1_4']";
		String sActualZipValueFromUB04 = driver.findElement(By.xpath(sZipLocator)).getAttribute("value");

		//getting user input from JSystem to compare the form values		
		String sExpectedNPIValue = getValueFromJSystem(lsAttributes, "Billing Provider Name");
		String sExpectedAddressValue = getValueFromJSystem(lsAttributes, "Billing Provider Street Address");
		String sExpectedCityValue = getValueFromJSystem(lsAttributes, "Billing Provider City");
		String sExpectedZIPValue = getValueFromJSystem(lsAttributes, "Billing Provider City ZIP");


		if(sExpectedNPIValue.equalsIgnoreCase(sActualNPIValueFromUB04) && sExpectedAddressValue.equalsIgnoreCase(sActualAddressValueFromUB04)
				&& sExpectedCityValue.equalsIgnoreCase(sActualCityValueFromUB04) && sExpectedZIPValue.equalsIgnoreCase(sActualZipValueFromUB04)) {
			report.report("Validation : Field Locator 1 NPI, Address and Zip values are successfully verified", ReportAttribute.BOLD);
			return true;
		}else{
			report.report("Failed to compare field locator 1 values with database values");
			return false;
		}
	}



	public String getCurrentTimeFromEaseDB(){
		String currentTimeStamp = null;
		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB("Select now()");
		try {
			while(rs.next()){
				currentTimeStamp = rs.getTimestamp("now()").toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return currentTimeStamp;
	}


	public boolean validateConfirmationScreenSteps(List<Attribute> lsAttributes){
		String sBillType = getValueFromJSystem(lsAttributes, "TYPE OF BILL");
		StringBuilder sStmtFromWhichPeroid = new StringBuilder(getValueFromJSystem(lsAttributes, "Statement from which period"));
		StringBuilder sStmtToWhichPeroid = new StringBuilder(getValueFromJSystem(lsAttributes, "Statement to which period"));
		sStmtFromWhichPeroid.insert(2, '/');
		sStmtToWhichPeroid.insert(2, '/');

		String sExpectedConfirmationOne = "Step 1: Submit claim "+ sBillType +" claim from "+sStmtFromWhichPeroid.insert(5, '/')+ " through "+ sStmtToWhichPeroid.insert(5, '/') + " (brand new).";
		String sExpectedConfirmationTwo = "Step 2: Wait for the new claim to get paid.";

		String sConfirmationOne = driver.findElement(By.xpath("//ol[contains(text(), 'Submit')]")).getText();
		String sConfirmationTwo = driver.findElement(By.xpath("//ol[contains(text(), 'Wait')]")).getText();

		if( sExpectedConfirmationOne.equalsIgnoreCase(sConfirmationOne.trim()) && sExpectedConfirmationTwo.equalsIgnoreCase(sConfirmationTwo.trim()) )
			return true;
		else
			return false;
	}

	//below method convert a XML file from String to a file format 
	public static void stringToDom(String xmlSource,String sFileName) 
			throws SAXException, ParserConfigurationException, IOException {
		// Parse the given input
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

		// Write the parsed document to an xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(doc);

		File file = new File(sFileName);

		//Create the file
		if (file.createNewFile()){
			report.report(sFileName + "is created!");
		}else{
			report.report(sFileName + " is already exists.");
		}
		StreamResult result =  new StreamResult(new File(sFileName));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/*	public String getElementValueJS(WebElement element, String attributeName) throws Exception {
		String sValue = null;
		if (element.isEnabled() && element.isDisplayed()) {
			sValue = (String) ((JavascriptExecutor) driver).executeScript("arguments[0].getAttribute(argument[1]);", element, attributeName);
		} else {
			report.report("");
		}
		return sValue;
	}*/
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}
