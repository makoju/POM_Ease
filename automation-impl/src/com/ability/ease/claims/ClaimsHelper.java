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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.ProviderTable;
import com.ability.ease.auto.common.UB04FormXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ClaimsHelper extends AbstractPageObject{


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
		WebElement mydde = waitForElementVisibility(By.linkText("MY DDE"));
		String classAttr = mydde.getAttribute("class");
		if ( mydde != null) {
			if( !classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				clickLink("MY DDE");
				return;
			}else{
				//nothing to do
			}
		}
		report.report("MY DDE Link element is not avaible on page");
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

		String sExpectedConfirmationOne = "Step 1: Submit claim "+ sBillType +" claim from "+sStmtFromWhichPeroid.insert(5, '/')+
				" through "+ sStmtToWhichPeroid.insert(5, '/') + " (brand new).";
		String sExpectedConfirmationTwo = "Step 2: Wait for the new claim to get paid.";

		String sConfirmationOne = driver.findElement(By.xpath("//ol[contains(text(), 'Submit')]")).getText();
		String sConfirmationTwo = driver.findElement(By.xpath("//ol[contains(text(), 'Wait')]")).getText();

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
			report.report("Can not read totals from JSYSTEM", report.WARNING);
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
			report.report("Can not read totals from UB04 form", report.WARNING);
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
		int numberBefore0001RevCode = Integer.valueOf(totalClaimLinesDisplayed);	
		if(numberBefore0001RevCode != 0 ){
			numberBefore0001RevCode = numberBefore0001RevCode - 1;
			suffix = String.valueOf(numberBefore0001RevCode);
			for(int i=0;i < sValues.length; i++){
				typeEditBox(sLocators[i]+"_"+suffix, sValues[i]);
			}
		}else{
			report.report("Unable to get the total claim number before 0001 rev code");
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
		int failCounter = 0;

		if(sClaimsXMLFile != null){
			stringToDom(sClaimsXMLFile, sFileName);
		}else{
			report.report("Can not submit a claim rquest with same TOB (Type of bill), please provide a different TOB and try again");
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
		WebElement we = driver.switchTo().activeElement();
		if( we != null){
			report.report("Text present on warning dailoge : " + we.getText());
			clickButtonV2("ub04-warning-submit-button");
			return true;
		}else{
			report.report("UB04 From Submit Warning : Model dailog box reference is null");
			return false;
		}
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
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}
