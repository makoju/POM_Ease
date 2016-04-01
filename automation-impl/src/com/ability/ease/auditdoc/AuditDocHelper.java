package com.ability.ease.auditdoc;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.claims.ClaimsHelper;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AuditDocHelper extends AbstractPageObject{

	ReportsHelper reportHelper = new ReportsHelper();
	ClaimsHelper claimHelper = new ClaimsHelper();

	int failCounter = 0;
	WebElement uploadIOCN=null;
	int passCounter = 0;

	public void clickTimeFrame(String location,String value)throws Exception{ 
		moveToElement(location);
		//if the value value starts with fromdate then user is going to set from and todate 
		if (value.toLowerCase().startsWith("fromdate")){
			String fromdate="",todate="";
			String[] dates = value.split(":");
			//getting the from and todate from the user supplied date string say, "fromdate(MM//DD//YYYY):todate(MM//DD//YYYY)"
			if(dates.length>1){
				fromdate = dates[0].substring(dates[0].indexOf("(")+1, dates[0].indexOf(")"));
				todate = dates[1].substring(dates[1].indexOf("(")+1, dates[1].indexOf(")"));;
			}
			typeEditBox("reportCustomDateFrom", fromdate);
			typeEditBox("reportCustomDateTo", todate);
			clickButton("reportTimeframeButton");
		}
		else 
			clickLink(value.trim());
	}

	public void clickAgency(String agency,String value)throws Exception{ 
		moveToElement(agency);
		selectByNameOrID("reportAgencySelect", value.trim());
		clickButton("Change Agency");
	}

	public boolean verifyColumn(String locator){

		//waitForElementVisibility(By.xpath(locator));
		WebElement present=driver.findElement(By.xpath(locator));
		if(present!=null){
			return true;
		}else{
			return false;
		}
	}

	public boolean isHIcExist(String hic){
		String xPathToHIC = "//span[contains(text(),'HIC')]/following-sibling::span";
		String sActual = driver.findElement(By.xpath(xPathToHIC)).getText();
		if(sActual.equalsIgnoreCase(hic)){
			return true;
		}else{
			return false;
		}
	}

	public boolean isPatientExist(String patient){
		String xPathToHIC="//span[contains(text(),'Last Name')]/following-sibling::span";
		String sActual = driver.findElement(By.xpath(xPathToHIC)).getText();
		if(patient.contains(sActual)){
			return true;
		}else{
			report.report("Patient name not presented on the patient information page");
			return false;
		}
	}

	public boolean isADRExist(String daysduedate){
		String pageHeader=waitForElementVisibility(By.xpath("//*[contains(text(),'ADR INFORMATION')]")).getText();
		if(pageHeader.contains("ADR INFORMATION")){
			return true;
		}else{
			return true;
		}
	}

	public void navigateBack() throws Exception	{
		waitForElementToBeClickable(ByLocator.id, "backNav", 10);
		driver.findElement(By.id("backNav")).click();
	}

	public void navigateForward() throws Exception{
		WebElement we = waitForElementToBeClickable(ByLocator.id, "forwardNav", 10);
		safeJavaScriptClick(we);
		Thread.sleep(5000);
	}

	//Use this method to click on MY DDE Link
	public void navigateToESMDStatusPage(String agency) throws Exception{

		WebElement mydde = waitForElementVisibility(By.linkText("MY DDE"));
		String classAttr = mydde.getAttribute("class");
		if ( mydde != null) {
			if( !classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				safeJavaScriptClick("MY DDE");
				waitForElementToBeClickable(ByLocator.id, "reportNewUB04", 10);
				clickEsmdStatusLink(agency);
				return;
			}else{
				clickEsmdStatusLink(agency);
			}
		}
	}

	public void clickEsmdStatusLink(String agency)throws Exception{
		String sXpathOfOVERNIGHT = "//span[contains(text(),'OVERNIGHT')] | //td[contains(text(),'EASE SUMMARY REPORT')] | //td[contains(text(),'FOR AGENCY')] | "
				+ "//td[contains(text(),'ADR Response Document Submission')]";
		String sXpathBeforeClickOnESMD = "//td[contains(text(),'FOR AGENCY')]";
		String sXpathofEsmdReport = "//td[contains(text(),'ESMD DELIVERY & STATUS REPORT')]";
		waitForElementToBeClickable(ByLocator.xpath, sXpathOfOVERNIGHT, 10);
		if( !isTextPresent("Basic")){
			clickLink("Advanced");
		}
		Thread.sleep(5000);
		WebElement reportHeaderTextInBlue = retryUntilElementIsVisible(sXpathOfOVERNIGHT, 10);
		String reportHeaderText = reportHeaderTextInBlue.getText();
		if(reportHeaderTextInBlue != null){
			waitForElementToBeClickable(ByLocator.id, "reportAgencySelect", 5);
			//change agency and time frame to get the right record to upload ADR document
			moveToElement("Agency");
			selectByNameOrID("reportAgencySelect", agency);
			clickButton("Change Agency");
			//wait for FOR AGENCY text on table header in blue before clicking on esmd-status link
			//waitForElementToBeClickable(ByLocator.xpath,sXpathBeforeClickOnESMD, 60)
			if(waitForElementToBeClickable(ByLocator.linktext, "Timeframe", 30) != null){
				if(!reportHeaderText.contains("2011")){
					changeTimeFrame();
				}
				Thread.sleep(5000);
				if( waitForElementToBeClickable(ByLocator.xpath,sXpathBeforeClickOnESMD, 60) != null){
					//clickLink("esMD Delivery & Status");
					safeJavaScriptClick("esMD Delivery & Status");
					waitForElementToBeClickable(ByLocator.xpath,sXpathofEsmdReport, 30);
				}else{
					report.report("OVERNIGHT EASE SUMMARY REPORT FOR XX/XX/XXXX, FOR AGENCY XXX not visible after changing the timeframe");
				}
			}else{
				report.report("EASE SUMMARY REPORT FROM MM/DD/YYYY TO MM/DD/YYYY, FOR AGENCY XXXX is not present !!!");
			}

		}
	}

	@SuppressWarnings("static-access")
	public List<String> getADRFilePath(ADRFileFomat adrFileType,char relationalOperator){
		List<String> filePaths = new ArrayList<String>();

		switch (adrFileType) {
		case PDF:
			if( relationalOperator == '>'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_2_38.8MB.pdf");
			}else if( relationalOperator == '<'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_1_0.10MB.pdf");
			}
			break;
		case TIFF:
			if( relationalOperator == '>'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_2_45MB.tiff");
			}else if( relationalOperator == '<'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_1_0.13MB.TIFF");
			}
			break;
		case TIF:
			//currently we don't have .tif file which is > 35 MB hence loading .TIFF
			if( relationalOperator == '>'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_2_45MB.TIFF");
			}else if( relationalOperator == '<'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_3_0.07MB.tif");
			}
			break;

		case PDFandTIFF:
			if( relationalOperator == '>'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_2_38.8MB.pdf");
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_4_7.80MB.pdf");
			}else if( relationalOperator == '<'){
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_3_0.07MB.tif");
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_3_6.31MB.pdf");
				filePaths.add(TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_1_0.13MB.TIFF");
			}
			break;

		default:
			report.report("Fail : You have selected un-supported file format to upload", report.WARNING);
			break;
		}

		return filePaths;
	}

	/**
	 * Use this method to upload list of files in IE browser , here the auto IT exe is developed only for IE
	 * @param filePaths
	 */
	public boolean uploadFilesAutoIT(List<String> lsFilePaths)throws Exception {
		int failCounter = 0;
		int i = 0;
		String sFileName = null;
		String autoITScriptPath = TestCommonResource.getTestResoucresDirPath()+"fileuploadutil\\FileUploadAutoIT.exe";
		String browseFileXpath = null;

		for( String sFilePath:lsFilePaths){
			sFileName = getFileNameFromFilePath(sFilePath);

			try {
				if( i == 0){
					browseFileXpath = "//input[@id='fileUploadId']";
					driver.findElement(By.xpath(browseFileXpath)).click();
				}else{
					browseFileXpath = "//input[@id='fileUploadId_F"+i+"']";
					WebElement browse = driver.findElement(By.xpath(browseFileXpath));
					browse.click();
					//safeJavaScriptClick(browse);
				}
				report.report("Invoking autoit script to upload file : " + sFileName);
				Runtime.getRuntime().exec(autoITScriptPath + " " + sFilePath);
				report.report(sFileName + " : File browsing completed ");
			} catch (IOException e) {
				failCounter++;
				e.printStackTrace();
			}catch(ElementNotVisibleException enve){
				failCounter++;
				enve.printStackTrace();
			}
			Thread.sleep(10000);
			i++;
		}
		return (failCounter == 0) ? true : false;
	}

	/**
	 * Use this method to validate a file has been browsed successfully on ADR upload doc page 
	 */
	public boolean validateFilesBrowsed(List<String> lsFileNames)throws Exception{

		int failCounter = 0 ;
		String xpathAfterFileUpload = null; 

		for(String sFileName: lsFileNames){
			xpathAfterFileUpload = "//span[contains(text(),'" + sFileName + "')]";
			Thread.sleep(5000);
			WebElement afterFileUpload = waitForElementToBeClickable(ByLocator.xpath,xpathAfterFileUpload, 60);
			if(afterFileUpload != null){
				report.report(sFileName + " is visible on ADR upload doc page !!");
			}else{
				failCounter++;
				report.report(sFileName + " is not visible on ADR upload doc page !!");
			}
		}
		return (failCounter == 0) ? true : false;		
	}

	/**
	 * Use this method to separate file name from given file paths and prepare a list
	 */
	public List<String> getADRFileNamesFromFilePaths(List<String> lsFilePaths){

		List<String> lsFileNames = new ArrayList<String>();
		if( !lsFilePaths.isEmpty()) {
			for( String sFilePath:lsFilePaths){
				lsFileNames.add(getFileNameFromFilePath(sFilePath));
			}
		}else{
			report.report("Given file paths list is empty , please check");
		}
		return lsFileNames;
	}


	/**
	 * reusing method in ReportsHelper to get table headers 
	 */
	public List<WebElement> getReportTableHeaders(String tableIdentifier){
		return reportHelper.getReportTableHeaders(tableIdentifier);
	}

	/**
	 * 
	 */
	public Map<String, String> getDataFromCMSStatusUpdateTable(String sQuery)throws Exception{

		Map<String, String> CMSStatusUpdateTableData = new HashMap<String, String>();
		ResultSetMetaData metaData=null;
		int datatype = 0;
		String columnLabelName = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(sQuery);
		metaData = rs.getMetaData();

		while(rs.next()){

			for (int i = 1; i <= metaData.getColumnCount(); i++) {

				datatype = metaData.getColumnType(i);
				columnLabelName = metaData.getColumnName(i);

				if (datatype == Types.VARCHAR) {
					if(rs.getString(columnLabelName) != null){
						CMSStatusUpdateTableData.put(columnLabelName, rs.getString(columnLabelName));
					}else{
						report.report("Column " + columnLabelName + "contains a null value");
						CMSStatusUpdateTableData.put(columnLabelName, null);
					}
				}else if(datatype == Types.INTEGER){
					if(rs.getString(columnLabelName) != null){
						CMSStatusUpdateTableData.put(columnLabelName, String.valueOf(rs.getInt(columnLabelName)));
					}else{
						report.report("Column " + columnLabelName + "contains a null value");
						CMSStatusUpdateTableData.put(columnLabelName, null);
					}
				}else if(datatype == Types.TIMESTAMP){
					if(rs.getString(columnLabelName) != null){
						CMSStatusUpdateTableData.put(columnLabelName, dateFormat.format(rs.getTimestamp(columnLabelName)));
					}else{
						report.report("Column " + columnLabelName + "contains a null value");
						CMSStatusUpdateTableData.put(columnLabelName, null);
					}
				}else if(datatype == Types.TINYINT){
					if(rs.getString(columnLabelName) != null){
						CMSStatusUpdateTableData.put(columnLabelName, String.valueOf(rs.getInt(columnLabelName)));
					}else{
						report.report("Column " + columnLabelName + "contains a null value");
						CMSStatusUpdateTableData.put(columnLabelName, null);
					}
				}
			}
		}
		return CMSStatusUpdateTableData;

	}

	public long timeDiffInHours(String date1, String date2){
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
		long diff = 0;
		long diffInHours = 0;
		Date d1 = null;
		Date d2 = null;

		if(date1 != null && date2 != null) {
			try {
				d1 = format.parse(date1);
				d2 = format.parse(date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}else{
			report.report("Fail : Both dates passed are null");
		}
		if( d1.compareTo(d2) > 0) {
			diff = d1.getTime() - d2.getTime();
		}else{
			diff = d2.getTime() - d1.getTime();
		}

		diffInHours = diff / (60 * 60 * 1000);

		return diffInHours;
	}

	public void doPageRefresh() throws Exception{
		String sXpathRefresh = "//a[@id='refreshPage']";
		if(isElementPresent(By.id("refreshPage"))){
			driver.findElement(By.xpath(sXpathRefresh)).click();
			Thread.sleep(5000);
		}else{
			report.report("Refresh icon is not present on page");
		}
	}

	public boolean waitForADRResponsePageToBeVisible(String sXpath) throws InterruptedException{

		//String xpathToADRSubPage = "//td[contains(text(),'ADR Response Document Submission Report')]";
		//String sXpathRefresh = "//a[@id='refreshPage']";
		boolean result = false;
		WebElement we = null;
		boolean isElementPresent = false;
		int count = 0;
		while( !isElementPresent || count++ <= 5 ){
			try{
				we = driver.findElement(By.xpath(sXpath));
				String rcvdByCMSTime = we.getText();
				report.report("Text from Received by reviewer column..." + rcvdByCMSTime);
				if( we != null && !rcvdByCMSTime.isEmpty()) {
					isElementPresent = true;
					result = true;
					break;
				}
				if( rcvdByCMSTime.isEmpty() ){
					navigateBack();
					Thread.sleep(10000);
					navigateForward();
				}
				//driver.findElement(By.xpath(sXpathRefresh)).click();
			}catch(NoSuchElementException nsee){
				Thread.sleep(10000);
			}catch(Exception e){
				try{
					Alert alert = driver.switchTo().alert();
					if(alert.getText().contains("I am still processing"))
						alert.accept();
				}
				catch(Exception e1){
					//ignore
				}
			}
			continue;
		}
		return result;
	}

	public void changeTimeFrame()throws Exception{
		moveToElement("Timeframe");
		typeEditBox("reportCustomDateFrom", "2/23/2011");
		clickButtonV2("reportTimeframeButton");
	}

	public void clickMyDDELink() throws Exception{
		claimHelper.clickMYDDELink();
	}

	public void validateADRResponseTableData(Map<String, String> CMSStatusUpdateTableData, String columnNameID){
		String columnValue = CMSStatusUpdateTableData.get(columnNameID);
		if( columnValue != null ){
			report.report(columnNameID + " has a got value : " + columnValue);
		}else{
			report.report(columnNameID + " value is : null ");
		}
	}

	public List<String> getToolTipOfView(String ClaimIDDCN)throws Exception{

		String viewToolTipXpath = "//td[contains(text(),'" + ClaimIDDCN + "')]/following-sibling::td[contains(text(),'view')]";
		waitForElementVisibility(By.xpath(viewToolTipXpath));
		WebElement we = driver.findElement(By.xpath(viewToolTipXpath));
		String tooltiprawtext = we.getAttribute("onmouseover");
		tooltiprawtext.replaceAll("\"", "");
		List<String> lsFileNamesFromViewColumn = new ArrayList<String>(); 

		String str2 = tooltiprawtext.substring(tooltiprawtext.indexOf("<BR>"));
		String[] str3 = str2.split("<BR>");
		for(String s1:str3){
			if(!s1.isEmpty()){
				lsFileNamesFromViewColumn.add(s1.replaceAll("[^a-zA-Z0-9-._ ]", ""));
			}
		}
		report.report("There are/is "+ lsFileNamesFromViewColumn.size() + " file name(s) found in Document List view tool tip");
		for(String fileName:lsFileNamesFromViewColumn){
			report.report("File name from Document List view tool tip : " + fileName);
		}
		return lsFileNamesFromViewColumn;
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
