package com.ability.ease.auditdoc;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
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
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.portal.selenium.ESMDSubmissionType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.claims.ClaimsHelper;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AuditDocHelper extends AbstractPageObject{

	ReportsHelper reportHelper = new ReportsHelper();
	ClaimsHelper claimHelper = new ClaimsHelper();
	boolean stepResult = false;

	int failCounter = 0;
	WebElement uploadIOCN=null;
	int passCounter = 0;

	//Expected column header help text for HHA
	String[] expectedheaders_HHA = { "Upload ADR Documents",
			"The Patient HIC number.",
			"The name of the patient.",
			"The claim Status and Location.",
			"The date the patient was originally admitted.",
			"Start of Episode date.",
			"The reimbursement amount posted by Medicare on the claim.",
			"The total episode value.",
			"The number of days left to respond to the ADR.",
			"The date an action is required by the FI in order to resolve the ADR.",
			"Ensure that ADR documentation is mailed by this date to avoid unnecessary auto-denying of your claim.",
			"The code associated with the ADR.",
			"Total Amount Billed for Claim",
			"The last time the claim was updated in Ease from DDE.",
	"ADR Response Document Submission status."};
	//Expected column header help text for Generic
	String[] expectedheaders_Generic = { "Upload ADR Documents",
			"The Patient HIC number.",
			"The name of the patient.",
			"The claim Status and Location.",
			"The date the patient was originally admitted.",
			"The claim start date.",
			"The claim through date.",
			"The reimbursement amount posted by Medicare on the claim.",
			"The number of days left to respond to the ADR.",
			"The date an action is required by the FI in order to resolve the ADR.",
			"Ensure that ADR documentation is mailed by this date to avoid unnecessary auto-denying of your claim.",
			"The code associated with the ADR.",
			"Total Amount Billed for Claim",
			"The last time the claim was updated in Ease from DDE.",
	"ADR Response Document Submission status."};



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
		WebElement we = waitForElementToBeClickable(ByLocator.id, "backNav", 10);
		if ( we != null){
			we.click();
			waitForElementToBeClickable(ByLocator.xpath,elementprop.getProperty("ADR_STATUS_REPORT_HDR_XPATH"),60);
		}
	}

	public void navigateForward() throws Exception{
		WebElement we = waitForElementToBeClickable(ByLocator.id, "forwardNav", 10);
		safeJavaScriptClick(we);
		Thread.sleep(5000);
	}

	/**
	 * Use this method to click on esMD Link
	 * Updated below code from EASE 1.5 to work accordingly on EASE 1.6 code
	 * nageswar.bodduri
	 */
	public void navigateToESMDStatusPage(String agency) throws Exception{

		String adrReportHeaderXpath = "//td[contains(text(),'ADR ESMD STATUS REPORT')]";
		WebElement esMD = waitForElementToBeClickable(ByLocator.linktext, "esMD", 60);
		if ( esMD != null) {
			safeJavaScriptClick(esMD);
			WebElement adrLink = waitForElementToBeClickable(ByLocator.linktext, "ADR Submission", 15);
			if ( adrLink != null ){
				safeJavaScriptClick(adrLink);
				WebElement reportHeader = waitForElementToBeClickable(ByLocator.xpath, adrReportHeaderXpath, 15);
				if( reportHeader != null ){
					String reportHeaderText = reportHeader.getText();
					waitForElementToBeClickable(ByLocator.id, "reportAgencySelect", 5);
					moveToElement("Agency");
					selectByNameOrID("reportAgencySelect", agency);
					clickButton("Change Agency");
					if(waitForElementToBeClickable(ByLocator.linktext, "Timeframe", 30) != null){
						if(!reportHeaderText.contains("2011")){
							changeTimeFrame();
						}
					}else{
						report.report("FAIL : Timeframe option not visible on the page");
					}
				}else{
					report.report("FAIL : Report header element is not visible on the page");
				}
			}else{
				report.report("FAIL : ADR Submission option not visible on the page");
			}
		}else{
			report.report("FAIL : esMD link is not available in EASE landing page");
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

		report.report("Validating filenames which are ready to uploaded");
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
		while( !isElementPresent && count++ <= 10 ){
			try{
				we = driver.findElement(By.xpath(sXpath));
				String rcvdByCMSTime = we.getText();
				report.report("Text from Received by reviewer column is : " + rcvdByCMSTime + " in " + count + " try");
				if( we != null && !rcvdByCMSTime.isEmpty()) {
					isElementPresent = true;
					result = true;
					break;
				}
				if( rcvdByCMSTime.isEmpty() ){
					navigateBack();
					Thread.sleep(30000);
					navigateForward();
				}
				//driver.findElement(By.xpath(sXpathRefresh)).click();
			}catch(NoSuchElementException nsee){
				Thread.sleep(30000);
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
		report.report("Changed from date field to 2/23/2011");
		clickButtonV2("reportTimeframeButton");
		Thread.sleep(5000);
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

	public int getRecordCountFromADRReport(String sAgency) throws Exception{

		List<WebElement> lsADRRecords = null;
		int recordCountFromADRReport = 0;
		String ADRPageXpath = "//td[contains(text(),'ADR REPORT, FOR')]";
		String sADRDataTableXpath = "//table[@id='datatable']/tbody/tr";

		if( waitForElementToBeClickable(ByLocator.linktext, "MY DDE", 30) != null){
			safeJavaScriptClick("MY DDE");
			if( waitForElementToBeClickable(ByLocator.linktext, "Advanced", 30) != null){
				safeJavaScriptClick("Advanced");
				WebElement adrLink = waitForElementToBeClickable(ByLocator.linktext, "ADR", 15);
				if( adrLink != null){
					adrLink.click();
					if(waitForElementToBeClickable(ByLocator.xpath, ADRPageXpath, 60) != null){
						lsADRRecords = driver.findElements(By.xpath(sADRDataTableXpath));
						recordCountFromADRReport = lsADRRecords.size();
						report.report("Record(s) present under ADR Report for agency " + sAgency + " is : " + recordCountFromADRReport);
					}
				}
			}
		}
		return recordCountFromADRReport;
	}	

	public int getRecountFromESMDADRSubmission(String sAgency)throws Exception{

		List<WebElement> lsADRRecords = null;
		int recordCountFromADRReport = 0;
		String sADRDataTableXpath = "//table[@id='datatable']/tbody/tr";
		String sXpathToADResMDStatusReport = "//td[contains(text(),'ADR ESMD STATUS REPORT')]";

		navigateToESMDStatusPage(sAgency);
		if ( waitForElementToBeClickable(ByLocator.xpath, sXpathToADResMDStatusReport, 20) != null){
			lsADRRecords = driver.findElements(By.xpath(sADRDataTableXpath));
			recordCountFromADRReport = lsADRRecords.size();
			report.report("Record(s) present under ADR ESMD STATUS REPORT for agency " + sAgency + " is : " +  recordCountFromADRReport);
		}
		return recordCountFromADRReport;
	}

	public boolean verifyHICLink(String HIC)throws Exception{

		String actualHIC = null;

		clickLink(HIC);
		if ( waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("PATIENT_INFO_TD_HEADR_XPATH"), 60) != null){
			actualHIC = driver.findElement(By.xpath(elementprop.getProperty("HIC_TEXT_XPATH"))).getText();
			if( actualHIC.equalsIgnoreCase(HIC)){
				stepResult = true;
				report.report("Successfully navigated to patient info page from HIC link!!!");
			}
		}
		navigateBack();
		return stepResult;
	}

	public boolean verifyPatientNameLink(String patientName)throws Exception{

		clickLink(patientName);
		WebElement we = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("PATIENT_MIDDLE_NAME_XPATH"), 30);

		if( we!=null){
			if(patientName.contains(we.getText())){
				stepResult = true;
				report.report("Navigated to patient information page successfully");
			}
		}
		navigateBack();
		return stepResult;
	}

	public boolean verifyDueDateLink(String dueDate)throws Exception{


		clickLink(dueDate);
		WebElement we = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ADR_INFO_PAGE_HDR_XPATH"), 30);
		if( we != null){
			report.report("Navigated to ADR INFORMATION page");
			stepResult = true;
		}
		navigateBack();
		return stepResult;
	}

	public boolean verifyThirteeDayDueDateLink(String thirteeDayDueDate)throws Exception{
		return verifyDueDateLink(thirteeDayDueDate);
	}

	public boolean verifyCodeLink(String code)throws Exception{
		return verifyDueDateLink(code);
	}

	public boolean verifyToolTips(String agency)throws Exception{

		String tableHeadersXpath = elementprop.getProperty("TABLE_HEADERS_XPATH");
		boolean result = false;

		if(agency.contains("HHA")) {
			String[] actualheadertooltips = reportHelper.getTableHeaderToolTips(tableHeadersXpath);
			if (Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders_HHA,true)){
				result = true;
				report.report("Tool tips of ADR status report table columns have been verified for HHA provider !!!");
			}
		}else{
			String[] actualheadertooltips = reportHelper.getTableHeaderToolTips(tableHeadersXpath);
			if (Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders_Generic,true)){
				result = true;
				report.report("Tool tips of ADR status report table columns have been verified for Generic provider !!!");
			}
		}
		return result;
	}

	/**
	 * Use this method to click on esMD Link and navigate to ADR, RAC,Appeal Submission Page
	 * Updated below code from EASE 1.5 to work accordingly on EASE 1.6 code
	 * nageswar.bodduri
	 */
	public boolean navigateToESMDStatusPage(ESMDSubmissionType esMDType) throws Exception{

		boolean stepResult = false;
		String adrReportHeaderXpath = MessageFormat.format(elementprop.getProperty("ESMD_REPORT_HDR_XPATH"), esMDType.toString());

		WebElement esMD = waitForElementToBeClickable(ByLocator.linktext, "esMD", 60);
		String classAttr = esMD.getAttribute("class");
		if ( esMD != null) {
			if( classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				WebElement adrLink = waitForElementToBeClickable(ByLocator.linktext, esMDType.toString()+" Submission", 60);
				if ( adrLink != null ){
					safeJavaScriptClick(adrLink);
					WebElement reportHeader = waitForElementToBeClickable(ByLocator.xpath, adrReportHeaderXpath, 60);
					if( reportHeader != null ){
						stepResult = true;
					}
				}
			}else{
				safeJavaScriptClick(esMD);
				WebElement adrLink = waitForElementToBeClickable(ByLocator.linktext, esMDType.toString()+" Submission", 60);
				if ( adrLink != null ){
					safeJavaScriptClick(adrLink);
					WebElement reportHeader = waitForElementToBeClickable(ByLocator.xpath, adrReportHeaderXpath, 60);
					if( reportHeader != null ){
						stepResult = true;
					}else{
						report.report("Failed to navigate to " + esMDType.toString() + " submission page");
					}
				}
			}
		}
		return stepResult;
	}

	public String setESMDHeaderXPATH(ESMDSubmissionType esMDSubType)throws Exception{

		String esMDType = esMDSubType.toString();
		String esMDXpath = null;

		if( esMDType.equalsIgnoreCase("ADR")){
			esMDXpath = MessageFormat.format(elementprop.getProperty("ESMD_STATUS_REPORT_HDR_XPATH"), "'" + "ADR ESMD STATUS REPORT" + "'");
		}else if(esMDType.equalsIgnoreCase("RAC")){
			esMDXpath = MessageFormat.format(elementprop.getProperty("ESMD_STATUS_REPORT_HDR_XPATH"), "'" + "RAC CLAIMS ESMD STATUS REPORT" + "'");
		}else{
			esMDXpath = MessageFormat.format(elementprop.getProperty("ESMD_STATUS_REPORT_HDR_XPATH"), "'" + "APPEAL CLAIMS ESMD STATUS REPORT" + "'");
		}
		return esMDXpath;
	}

	public String setESMDDocUploadIconXPATH(ESMDSubmissionType esMDSubType, String HIC)throws Exception{

		String esMDType = esMDSubType.toString();
		String uploadIconXpath = null;

		if( esMDType.equalsIgnoreCase("ADR") || esMDType.equalsIgnoreCase("APPEAL")){
			uploadIconXpath = MessageFormat.format(elementprop.getProperty("ESMD_DOC_UPLOAD_ICON"), "'" + HIC + "'");
		}else{
			uploadIconXpath = MessageFormat.format(elementprop.getProperty("RAC_DOC_UPLOAD_ICON"), "'" + HIC + "'");
		}
		return uploadIconXpath;
	}


	public String setReviewContractorAlertMgs(ESMDSubmissionType esMDSubType, String reviewContractorName)throws Exception{
		String esMDType = esMDSubType.toString();
		String reviewContractorAlertMsg = null;

		if( esMDType.equalsIgnoreCase("ADR")){
			reviewContractorAlertMsg = MessageFormat.format(elementprop.getProperty("REVIEW_CONTRACOTR_ALERT_MSG"), "ADR Document" , reviewContractorName);
		}else if(esMDType.equalsIgnoreCase("APPEAL")){
			reviewContractorAlertMsg = MessageFormat.format(elementprop.getProperty("REVIEW_CONTRACOTR_ALERT_MSG"), "Appeal" , reviewContractorName);
		}else{
			reviewContractorAlertMsg = MessageFormat.format(elementprop.getProperty("REVIEW_CONTRACOTR_ALERT_MSG"), "RAC Audit" , reviewContractorName);
		}
		return reviewContractorAlertMsg;

	}

	public String setCMSStausTableHeaderXpath(ESMDSubmissionType esmdSubmissionType)throws Exception{

		String esMDType = esmdSubmissionType.toString();
		String CMSStatusTableHeaderXPATH = null;

		if( esMDType.equalsIgnoreCase("ADR")){
			CMSStatusTableHeaderXPATH = elementprop.getProperty("ADR_CMS_STATUS_HEADER_XPATH");
		}else{
			CMSStatusTableHeaderXPATH = MessageFormat.format(elementprop.getProperty("ESMD_CMS_STATUS_HEADER_XPATH"), esMDType +" esMD Delivery Response Report");
		}
		return CMSStatusTableHeaderXPATH;
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
