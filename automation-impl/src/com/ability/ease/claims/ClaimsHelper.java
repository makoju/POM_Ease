package com.ability.ease.claims;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.ProviderTable;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UB04FormXMLParser;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsHelper extends AbstractPageObject{

	private String searchResultXpath = elementprop.getProperty("ADVANCE_SEARCH_RESULTS_PAGE_HEADER");
	private String rptSearchIcon = elementprop.getProperty("REPORT_HIC_SEARCH_ICON");
	private String advSearchLink = elementprop.getProperty("ADV_SEARCH_LINK");
	private String hic = elementprop.getProperty("HIC_TEXT");
	private String lookBackMonths = elementprop.getProperty("LOOK_BACK_MONTHS_TEXT");
	private String statusLoc = elementprop.getProperty("SLOC_STATUS_DROP_DOWN");
	private String searchBtn = elementprop.getProperty("SEARCH_BUTTON");
	private String myDDELink = elementprop.getProperty("MY_DDE_LINK");
	private int failCounter = 0;


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

	//this method clicks on MY DDE Link
	public void clickMYDDELink() throws Exception{

		//WebElement mydde = waitForElementVisibility(By.linkText("MY DDE"));
		Thread.sleep(5000);
		WebElement mydde = waitForElementToBeClickable(ByLocator.linktext, "MY DDE", 60);
		String classAttr = mydde.getAttribute("class");
		if ( mydde != null) {
			if( !classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				safeJavaScriptClick("MY DDE");
				waitForElementToBeClickable(ByLocator.id, "reportNewUB04", 60);
				return;
			}else{

			}
		}
	}

	//this method compared totals provided in JSYSTEM and totals in xml file in data base
	public boolean verifyTotals(List<Attribute> lsAttributes, String sFile)throws Exception{

		float[] claimsTotalsFromXML = UB04FormXMLParser.getInstance().getClaimTotals(sFile);
		float[] claimsTotalsFromJSYSTEM = getTotalsFromJSYSTEM(lsAttributes);

		report.report("Expected total charges are : " + claimsTotalsFromJSYSTEM[0]);
		report.report("Expected total non covered charges are : " + claimsTotalsFromJSYSTEM[1]);

		report.report("Actual total charges are : " + claimsTotalsFromXML[0]);
		report.report("Actual total non covered charges are : " + claimsTotalsFromXML[1]);

		if( Arrays.equals(claimsTotalsFromXML, claimsTotalsFromJSYSTEM)){
			report.report("Total charges and non covered charges is equivalent to individual charges of each claim line entry", ReportAttribute.BOLD);
			return true;
		}else{
			report.report("Total charges and non covered charges are not equal");
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



	public static String getCurrentTimeFromEaseDB(){
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


	public boolean validateConfirmationScreenSteps(List<Attribute> lsAttributes) throws Exception{
		String xpathToSubmit = "//ol[contains(text(), 'Submit')]";
		String xpathToWait = "//ol[contains(text(), 'Wait')]";
		String sConfirmationOne = null,sConfirmationTwo = null;

		String sBillType = getValueFromJSystem(lsAttributes, "TYPE OF BILL");
		StringBuilder sStmtFromWhichPeroid = new StringBuilder(getValueFromJSystem(lsAttributes, "Statement from which period"));
		StringBuilder sStmtToWhichPeroid = new StringBuilder(getValueFromJSystem(lsAttributes, "Statement to which period"));
		sStmtFromWhichPeroid.insert(2, '/');
		sStmtToWhichPeroid.insert(2, '/');

		String sExpectedConfirmationOne = "Step 1: Submit claim "+ sBillType +" claim from "+sStmtFromWhichPeroid.insert(5, '/')+
				" through "+ sStmtToWhichPeroid.insert(5, '/') + " (brand new).";
		String sExpectedConfirmationTwo = "Step 2: Wait for the new claim to get paid.";

		if(waitForElementToBeClickable(ByLocator.xpath, xpathToSubmit, 10) != null){
			sConfirmationOne = driver.findElement(By.xpath(xpathToSubmit)).getText();
			sConfirmationTwo = driver.findElement(By.xpath(xpathToWait)).getText();
		}
		if( sExpectedConfirmationOne.equalsIgnoreCase(sConfirmationOne.trim()) && sExpectedConfirmationTwo.equalsIgnoreCase(sConfirmationTwo.trim()) )
			return true;
		else
			return false;
	}

	//below method convert a XML file from String to proper file format 
	public void stringToDom(String xmlSource,String sFileName) 
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

	public float[] getTotalsFromJSYSTEM(List<Attribute> lsAttributes)throws Exception{
		float[] totals = null;
		float totalCoveredCharges = 0;
		float totalNonCoveredCharges = 0;
		//this code will parse the user input from JSYSTEM and define the total covered and non-covered charges
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
								totalCoveredCharges = totalCoveredCharges + Float.valueOf(tempTotalCharge);
							}
						}
						if(sLocatorsClaim[i].contains("ub48")){
							String tempNonCoveredCharges = sValues[i];
							if( tempNonCoveredCharges != null){
								totalNonCoveredCharges = totalNonCoveredCharges + Float.valueOf(tempNonCoveredCharges);
							}
						}
						j++;
					}
				}
			}
		}
		if( totalCoveredCharges != 0 && totalNonCoveredCharges != 0){
			totals = new float[]{round(totalCoveredCharges,2), round(totalNonCoveredCharges,2)};
		}else{
			report.report("Can not read totals from JSYSTEM", Reporter.WARNING);
			totals = new float[]{0,0};
		}
		return totals;

	}
	//this method is used to get the totals from the UB04 form directly
	public float[] getTotalsFromUB04Form()throws Exception{

		float[] totals;
		String sTotalChargesLocator = "//*[contains(text(),'TOTALS')]/following-sibling::li[1]/input";	
		String sTotalNonCoveredChargesLocator = "//*[contains(text(),'TOTALS')]/following-sibling::li[2]/input";
		String sActualTotals = null, sActualNonCoveredTotals = null;
		sActualTotals = getValueOfValueAttr(sTotalChargesLocator);
		sActualNonCoveredTotals = getValueOfValueAttr(sTotalNonCoveredChargesLocator);

		if( sActualTotals != null && sActualNonCoveredTotals != null){
			totals = new float[]{Float.valueOf(sActualTotals), Float.valueOf(sActualNonCoveredTotals)};
		}else{
			report.report("Can not read totals from UB04 form", Reporter.WARNING);
			totals = new float[]{0, 0};
		}
		return totals;
	}

	public String getValueOfValueAttr(String sLocator) throws Exception{

		int count = 0;
		WebElement we = null;
		String sValue = null;
		do{
			waitForElementVisibility(By.xpath(sLocator), 60);
			if(isElementPresent(By.xpath(sLocator))) {
				we = driver.findElement(By.xpath(sLocator));
				sValue = we.getAttribute("value");
			}
			if( sValue != null){
				//break;
			}
			count ++;
		}while(sValue == null || count <= 5);
		return sValue;
	}

	public float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	public void moveToEditIcon(String xpath)throws Exception{
		WebElement we = driver.findElement(By.xpath(xpath));
		moveByOffset(we,-20,0);

	}

	public void addClaimLine(String sClaimLine,String sLineNumber, String sPosition) throws Exception{
		String[] sLocators = new String[]{"ub42","ub44","ub45","ub46","ub47","ub48"}; 
		String[] sValues = sClaimLine.split(":");
		String suffix = null;

		//get the number before rev code 0001
		String totalClaimLinesDisplayed = driver.findElement(By.xpath("//li[contains(text(),'0001')]/preceding-sibling::li")).getText();
		int numberBefore0001RevCode = Integer.valueOf(totalClaimLinesDisplayed.trim());	
		if(numberBefore0001RevCode != 0 ){
			report.report("Row number at rev code 0001 " + numberBefore0001RevCode);
			if( numberBefore0001RevCode == 11 ){
				numberBefore0001RevCode = 11;
			}else{
				numberBefore0001RevCode = numberBefore0001RevCode - 1;
			}
			suffix = String.valueOf(numberBefore0001RevCode);
			for(int i=0;i < sValues.length; i++){
				typeEditBox(sLocators[i]+"_"+suffix, sValues[i]);
			}
		}else{
			report.report("Unable to get the total claim number before 0001 rev code");
		}
	}

	public void moveAndClick(String claimLineNumberToAdd, String position) throws Exception{

		String xPathToIdentifyAtWhichLineToAdd = "//*[@name='ub42_"+claimLineNumberToAdd+"']";
		moveToEditIcon(xPathToIdentifyAtWhichLineToAdd);
		waitForElementVisibility(By.id("editmenutext"));
		if(isElementPresent(By.id("editmenutext"))){
			if(position.equalsIgnoreCase("before")){
				safeJavaScriptClick("Add claim line before");
				report.report("Clicked on before at line nuber : "+ claimLineNumberToAdd);
			}else if(position.equalsIgnoreCase("after")){
				safeJavaScriptClick("Add claim line after");
				report.report("Clicked on after at line nuber : "+ claimLineNumberToAdd);
			}else{
				report.report("Please provide correct position either before or after in jsystem");
			}
		}
	}

	public float[] getTotalsFromClaimLine(String sClaimLine){
		float[] totals = null;
		String[] sClaimDetails = sClaimLine.split(":");
		int temp = sClaimDetails.length;
		totals = new float[]{Float.valueOf(sClaimDetails[temp-2]), Float.valueOf(sClaimDetails[temp-1])};
		return totals;
	}

	public String[] getUB04XMLFromDatabase(String startTime, String endTime) throws SQLException{

		report.report("Inside get Ub04 form from EASE DB method");
		String requestDetails[] = null;
		int iClaimRequestID = 0;
		String sClaimRequestXML = null;
		String sSQLQueryToGetTheXMLFile = "Select RequestID, Request from ddez.userdderequest where SubmitTime BETWEEN '"+ 
				startTime.substring(0, startTime.length()-2) +"' AND "+ "'"+ endTime.substring(0, endTime.length()-2) + "'";

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(sSQLQueryToGetTheXMLFile);
		while(rs.next()){
			iClaimRequestID = rs.getInt("RequestID");
			sClaimRequestXML = rs.getString("Request");
		}
		requestDetails = new String[]{String.valueOf(iClaimRequestID), sClaimRequestXML};
		return requestDetails;
	}

	public boolean validateXMLFileFields(List<Attribute> lsAttributes, String sClaimsXMLFile, String sFileName) throws SAXException, ParserConfigurationException, IOException{

		report.report("Inside validate userdderequest xml file validation...");
		int failCounter = 0;

		if(sClaimsXMLFile != null){
			stringToDom(sClaimsXMLFile, sFileName);
		}else{
			failCounter++;
		}

		if( UB04FormXMLParser.getInstance().validateUB04XML(lsAttributes, sFileName)){
			report.report("XML file validation is completed successfully", ReportAttribute.BOLD);
		}else{
			report.report("XML file validation failed!");
			failCounter++;
		}
		return (failCounter == 0) ? true : false;
	}

	public boolean acceptUB04SubmitWarning() throws Exception{

		/*String sExpected = "Warning! There are potential errors with this Claim. Click 'Review' to change the values or Click 'Submit'";
		String sActual = null;
		 */
		boolean result = false;
		WebElement we = driver.switchTo().activeElement();
		if( we != null){
			report.report("Text present on warning dailoge : " + we.getText());
			clickButtonV2("ub04-warning-submit-button");
			if (waitForElementToBeClickable(ByLocator.id, "yesConfirmEditClaimButton", 30) != null )
				result = true;
		}else{
			report.report("UB04 From Submit Warning : Model dailog box reference is null");
			result = false;
		}
		return result;
	}

	public List<String> getFiledValuesFromEditClaimLineModelDailogWindow()throws Exception{
		String sRevCode = null;
		String sHIPPSCode = null;
		String sServiceDate = null;
		String sTotalUnits = null;
		String sCoveredUnits = null;
		String sTotalCharges = null;
		String sNonCoveredCharges = null;
		List<String> lsEditClaimLineWindow = new ArrayList<String>();
		int trueCounter = 0;


		//Get Rev code label and text
		WebElement revCodeLabel = driver.findElement(By.xpath("//*[@id='revCodeID']/../preceding-sibling::td/label"));
		if(revCodeLabel != null && revCodeLabel.getText().equalsIgnoreCase("42 REV CO:")){
			sRevCode = driver.findElement(By.xpath("//input[@id='revCodeID']")).getAttribute("value");
			lsEditClaimLineWindow.add(sRevCode);
		}

		//get HCPCS / HIPPS label and text
		WebElement hippsCodeLabel =  driver.findElement(By.xpath("//*[@id='hcpcsValID']/../preceding-sibling::td/label"));
		if(hippsCodeLabel != null && hippsCodeLabel.getText().equalsIgnoreCase("HCPCS / HIPPS:")){
			sHIPPSCode = driver.findElement(By.xpath("//input[@id='hcpcsValID']")).getAttribute("value");
			lsEditClaimLineWindow.add(sHIPPSCode);
		}

		//get service date lable and text
		WebElement serviceDateLabel =  driver.findElement(By.xpath("//*[@id='svcDtID']/../preceding-sibling::td/label"));
		if(serviceDateLabel != null && serviceDateLabel.getText().trim().equalsIgnoreCase("45 SERV DATE:")){
			sServiceDate = driver.findElement(By.xpath("//input[@id='svcDtID']")).getAttribute("value");
			lsEditClaimLineWindow.add(sServiceDate);
		}

		//get Total units label and text
		WebElement sTotalUnitLabel =  driver.findElement(By.xpath("//*[@id='servUnitsID']/../preceding-sibling::td/label"));
		if(sTotalUnitLabel != null && sTotalUnitLabel.getText().equalsIgnoreCase("TOT UNIT:")){
			sTotalUnits = driver.findElement(By.xpath("//input[@id='servUnitsID']")).getAttribute("value");
			lsEditClaimLineWindow.add(sTotalUnits);
		}

		//get covered units label and text
		WebElement sCoveredUnitLabel =  driver.findElement(By.xpath("//*[@id='daysCovID']/../preceding-sibling::td/label"));
		if(sCoveredUnitLabel != null && sCoveredUnitLabel.getText().equalsIgnoreCase("COV UNIT:")){
			sCoveredUnits = driver.findElement(By.xpath("//input[@id='daysCovID']")).getAttribute("value");
			trueCounter ++;
			//lsEditClaimLineWindow.add(sCoveredUnits);
		}

		//get total charges label and text
		WebElement sTotalChargesLabel =  driver.findElement(By.xpath("//*[@id='totChrgID']/../preceding-sibling::td/label"));
		if(sTotalChargesLabel != null && sTotalChargesLabel.getText().trim().equalsIgnoreCase("47 TOTAL CHARGES:")){
			sTotalCharges = driver.findElement(By.xpath("//input[@id='totChrgID']")).getAttribute("value");
			lsEditClaimLineWindow.add(sTotalCharges);
		}

		//get non voered charges label and text
		WebElement sNonCoveredChargesLabel =  driver.findElement(By.xpath("//*[@id='nonCovChrgID']/../preceding-sibling::td/label"));
		if(sNonCoveredChargesLabel != null && sNonCoveredChargesLabel.getText().trim().equalsIgnoreCase("48 NON-COVERED CHARGES:")){
			sNonCoveredCharges = driver.findElement(By.xpath("//input[@id='nonCovChrgID']")).getAttribute("value");
			lsEditClaimLineWindow.add(sNonCoveredCharges);
		}

		return lsEditClaimLineWindow;
	}

	public boolean isSubmitWarningPresent(){
		WebElement we = null;
		boolean flag = false;
		//Alert alert = null;
		try { 
			we = driver.switchTo().activeElement();
			String str = we.getText();
			if(str != null && str.trim().contains("Review")){
				flag = true;
			}
			return flag;
		}catch (NoAlertPresentException Ex){ 
			return flag;
		}  
	}

	public int getActivitycount(String activitytablename){
		return Integer.parseInt(getElementText(By.xpath("//table[@id='activityTable']//td[@id='"+activitytablename+"']")));
	}

	public boolean handleSubmitWarningAlert(List<Attribute> lsAttributes)throws Exception{
		int failCounter = 0;
		boolean isSubmitWarn = isSubmitWarningPresent();

		if( !isSubmitWarn ) {
			report.report("Submit warning alert not present on the screen...");
			if(validateConfirmationScreenSteps(lsAttributes)){
				clickButtonV2("yesConfirmEditClaimButton");
				if( verifyAlert("Changes scheduled!")){
					report.report("Claim request has been submitted successfully", ReportAttribute.BOLD);
				}else{
					failCounter++;
					report.report("Fail : Changes scheduled alert is not present");
				}
			}else{
				failCounter++;
				report.report("Fail : Failed to validate submission confirmation screen content");
			}
		}else {
			report.report("Submit warning alert is present on the screen...");
			acceptUB04SubmitWarning();
			if(validateConfirmationScreenSteps(lsAttributes)){
				clickButtonV2("yesConfirmEditClaimButton");
				if( verifyAlert("Changes scheduled!")){
					report.report("Claim request has been submitted successfully", ReportAttribute.BOLD);
				}else{
					failCounter++;
					report.report("Fail : Changes scheduled alert is not present");
				}
			}else{
				failCounter++;
				report.report("Fail : Failed to validate submission confirmation screen content");
			}
		}
		return (failCounter == 0) ? true : false;
	}

	public void openRejectedClaimRecordFromAdvanceSearchPage(String HIC)throws Exception{

		WebElement searchResult = null;
		int count = 0;

		clickMYDDELink();
		WebElement searchIcon = waitForElementToBeClickable(ByLocator.id, rptSearchIcon, 10);
		if(searchIcon != null){
			moveToElement(searchIcon);
			safeJavaScriptClick(driver.findElement(By.id(advSearchLink)));
			typeEditBox(hic, HIC);
			typeEditBox(lookBackMonths, "30");
			selectByNameOrID(statusLoc, "Rejected");
			clickButtonV2(searchBtn);
			do{
				searchResult = waitForElementToBeClickable(ByLocator.xpath, searchResultXpath, 30);
				if(searchResult != null){
					break;
				}
			}while( searchResult == null || count++ < 5);
		}
	}


	/**
	 * This method capture the all rows of total charge column in UB04 form and calculates
	 * individual charges with final total charges
	 * @return
	 * @throws Exception
	 */
	public float validateTotalChargeClaimLineCount(String coveredOrNonCovered) throws Exception{

		String temp = null;
		List<WebElement> lsTotChargesCols = new ArrayList<WebElement>();
		float actualTotalCharges = 0;
		int i = 0,size = 0;
		String coveredOrNonCoveredChargesColumnXPATH = null;

		coveredOrNonCoveredChargesColumnXPATH = setCoveredOrNonCoveredChargesColumnXPATH(coveredOrNonCovered);

		try{
			if(isElementPresent(By.id(elementprop.getProperty("NEW_UB04_ID")))){
				lsTotChargesCols = driver.findElements(By.xpath(coveredOrNonCoveredChargesColumnXPATH));
				size = lsTotChargesCols.size();
				if(lsTotChargesCols.size() > 0 ){
					for(WebElement totChargeLine:lsTotChargesCols){
						temp = totChargeLine.getAttribute("value");
						if((!temp.isEmpty() || !temp.equalsIgnoreCase("")) &&  (i < size-1)){
							actualTotalCharges = round(actualTotalCharges,2) +  round(Float.valueOf(temp),2);

						}
						i++;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return actualTotalCharges;
	}

	String setTotalChargesCellXPATH(String coveredOrNonCovered){
		String totalChargesCellXpath = null;
		if(coveredOrNonCovered.equalsIgnoreCase("COVERED")){
			totalChargesCellXpath = elementprop.getProperty("FINAL_TOTAL_CHARGES_XPATH");
		}else if(coveredOrNonCovered.equalsIgnoreCase("NON-COVERED")){
			totalChargesCellXpath = elementprop.getProperty("FINAL_TOTAL_NON_COVERED_CHARGES_XPATH");
		}else{
			report.report("Please provide either Covered or Non-Covered");
		}
		return totalChargesCellXpath;
	}

	String setCoveredOrNonCoveredChargesColumnXPATH(String coveredOrNonCovered){
		String coveredOrNonCoveredChargesColumnXPATH = null;
		if(coveredOrNonCovered.equalsIgnoreCase("COVERED")){
			coveredOrNonCoveredChargesColumnXPATH = elementprop.getProperty("TOTAL_COVERED_CHARGES_COLUMN_XPATH");
		}else if(coveredOrNonCovered.equalsIgnoreCase("NON-COVERED")){
			coveredOrNonCoveredChargesColumnXPATH = elementprop.getProperty("TOTAL_NON_COVERED_CHARGES_COLUMN_XPATH");
		}else{
			report.report("Please provide either Covered or Non-Covered");
		}
		return coveredOrNonCoveredChargesColumnXPATH;
	}

	public boolean comeBackToHomePage() throws Exception{

		boolean result = false;
		String expectedAlertText = "If you leave this page you will lose any changes you have made. Are you sure you wish to continue?";
		clickLinkV2(elementprop.getProperty("CLAIM_HOME_ID"));

		try{
			if( !isAlertPresent()){
				if( waitForElementToBeClickable(ByLocator.id, elementprop.getProperty("REPORT_HOME_ID"), 20) != null){
					result = true;
				}
			}else{
				result = verifyAlert(expectedAlertText);
			}
		}catch(Exception e){
			report.report("Exception occured while coming back to EASE home page"  + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public boolean validatePatientDetailsInNewUb04Form(Map<String, String> mapAttrValues)throws Exception{

		String temp = null;
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+
				"uiattributesxml\\Claims\\PatientInfoPage.xml", mapAttrValues);

		report.report("Inside validate patient details in new UB04 form method");
		report.report("employee details found in UB04 form are");

		for(Attribute scrAtr:lsAttributes){
			temp = driver.findElement(By.name(scrAtr.getLocator())).getAttribute("value");
			report.report(scrAtr.getDisplayName() + " = " + temp);
			if( !temp.isEmpty() || temp != null){
				if(!scrAtr.getValue().equalsIgnoreCase(temp)){
					failCounter++;
				}
			}
		}

		return (failCounter == 0) ? true : false;

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