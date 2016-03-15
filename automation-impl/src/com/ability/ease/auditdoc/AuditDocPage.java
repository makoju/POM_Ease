package com.ability.ease.auditdoc;

import java.util.List;
import java.util.Map;
import java.util.Random;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.claims.ClaimsHelper;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AuditDocPage extends AbstractPageObject{

	AuditDocHelper helper = new AuditDocHelper();
	int failCounter = 0;
	String xpathToADRSubPage = "//td[contains(text(),'ADR Response Document Submission Report')]";

	String tableheadersxpath = "//table[@id='datatable']//tr[@class='tableheaderblue']/td";
	ReportsHelper reportshelper = new ReportsHelper();

	//Expected column header help text
	String[] expectedheaders = { "Upload ADR Documents",
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

	/**
	 * 
	 * @param Timeframe
	 * @param Value
	 * @param agency
	 * @param agencyValue
	 * @param hic
	 * @param patient
	 * @param daysduedate
	 * @param duedate
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public boolean verifyEsmdDeliveryStatusReportColumnsForHHA(String Timeframe, String Value, String agency, String agencyValue, 
			String hic,String patient,String daysduedate,String duedate,String code) throws Exception {
		//	waitForElementVisibility(By.linkText("MY DDE"));
		navigateToPage();
		//	waitForElementVisibility(By.linkText("Advanced"));
		clickLink("Advanced");
		//	waitForElementVisibility(By.linkText("esMD Delivery & Status"));
		clickLink("esMD Delivery & Status");
		String icon=".//*[@id='scrollContent']/tr/td[1]//img";
		String sloc=".//*[contains(text(),'S/Loc')]";
		String  cmsStatus=".//*[contains(text(),'CMS')]";
		helper.clickTimeFrame(Timeframe,Value);
		helper.clickAgency(agency, agencyValue);
		boolean isicon=helper.verifyColumn(icon);
		boolean isSLoc=helper.verifyColumn(sloc);
		boolean isCMSStatus=helper.verifyColumn(cmsStatus);
		if(isCMSStatus==true & isicon==true & isSLoc==true){
			report.report("Expected Columns and data presented under eSMD Report", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Expected Columns and data not presented under eSMD Report", Reporter.FAIL);
			failCounter++;
		}
		//Click HIC under esMD Report
		clickLink(hic);
		if(helper.isHIcExist(hic)){
			helper.navigateBack();
			report.report("HIC ID presented on the patient information page", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("HIC ID not presented on the patient information page", Reporter.FAIL);
			failCounter++;
		}

		//Click Patient Link under esMD Report
		clickLink(patient);
		if(helper.isPatientExist(patient)){
			helper.navigateBack();
			report.report("Patient Name presented on the patient information page", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Patient Name is not presented on the patient information page", Reporter.FAIL);
			failCounter++;
		}
		//Click 30-days due date link
		clickLink(daysduedate);
		if(helper.isADRExist(daysduedate)){
			helper.navigateBack();
			report.report("ADR Page is displayed", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("ADR Page isnot displayed", Reporter.FAIL);
			failCounter++;
		}
		//Click due date under the esMD report	
		clickLink(duedate);
		if(helper.isADRExist(duedate)){
			helper.navigateBack();
			report.report("ADR Page is displayed", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("ADR Page isnot displayed", Reporter.FAIL);
			failCounter++;
		}
		//Click code link under esMD Report
		clickLink(code);
		if(helper.isADRExist(code)){
			helper.navigateBack();
			report.report("ADR Page is displayed", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("ADR Page isnot displayed", Reporter.FAIL);
			failCounter++;
		}
		//Comparing 
		String[] actualheadertooltips = reportshelper
				.getTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true)){
			failCounter++;
			report.report("Total number of failures is: " + failCounter,ReportAttribute.BOLD);
		}
		return (failCounter == 0) ? true : false;
	}


	/**
	 * Use this method to upload a PDF / TIFF document in AuditDoc 
	 * @return true or false
	 * @throws Exception
	 */
	public boolean verifyADRDocumentUploadFileFormats(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID,ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception{

		String sExpectedAlertMessageBeforeADRSubmission = "Proceed with ADR Document Submission to Review Contractor : "+ reviewContractorName +"?";
		String sExpectedAlertMessageAfterSuccessfulADRSubmission = "Successfully processed ADR response documents Submission";
		String sXpathofEsmdReport = "//td[contains(text(),'ESMD DELIVERY & STATUS REPORT')]";
		String adrPageXpath = "//td[contains(text(),'ADR INFORMATION')]";


		String fileSize = adrFileSize.toString();
		char relationalOperator = fileSize.charAt(0);
		List<String> lsADRFilePaths = helper.getADRFilePath(adrFileType, relationalOperator);
		List<String> lsADRFileNames = helper.getADRFileNamesFromFilePaths(lsADRFilePaths);

		helper.navigateToESMDStatusPage(agency);
		waitForElementToBeClickable(ByLocator.xpath,sXpathofEsmdReport, 30);
		WebElement uploadDocIcon = retryUntilElementIsVisible("//td/center/a/img[1]", 7); 
		uploadDocIcon.click();
		waitForElementVisibility(By.xpath(adrPageXpath));
		WebElement adrPage = retryUntilElementIsVisible(adrPageXpath, 5);
		if( adrPage != null ){
			//enter review contractor name and other required details
			selectByNameOrID("reviewContractor", reviewContractorName);
			typeEditBox("ClaimId", claimIDorDCN);
			typeEditBox("CaseId", caseID);
			//upload ADR based on input provided in JSYSTEM
			if( helper.uploadFilesAutoIT(lsADRFilePaths)) {
				//validate whether all files browsed successfully before clicking on send
				if( helper.validateFilesBrowsed(lsADRFileNames)){
					report.report("ADR file(s) browsing is completed successfully", ReportAttribute.BOLD);
					clickButtonV2("Send");
					//validate review contractor name on alert box appears before submitting the ADR to a review contractor
					if(verifyAlert(sExpectedAlertMessageBeforeADRSubmission)){		
						report.report("Review contractor name on alert validated successfully : " + reviewContractorName , ReportAttribute.BOLD);
						//validate successful submission of ADR
						if(verifyAlertV2(sExpectedAlertMessageAfterSuccessfulADRSubmission)){
							report.report("Successfully processed ADR response documents submission", ReportAttribute.BOLD);
							waitForElementToBeClickable(ByLocator.xpath, "//a[contains(text(),'Sent to CMS')]", 10);
							if( isElementPresent(By.xpath("//a[contains(text(),'Sent to CMS')]"))){
								clickLink("Sent to CMS");
								if(waitForElementToBeClickable(ByLocator.xpath, xpathToADRSubPage, 10) != null){
									report.report("Successfully sent ADR reponse documents to CMS", ReportAttribute.BOLD);
								}else{
									failCounter++;
									report.report("Fail: Error occured while sending ADR to CMS");
								}
							}else{
								failCounter++;
								report.report("Fail : Can not find Sent to CMS link");
							}		
						}else{
							failCounter++;
							report.report("Fail : " + sExpectedAlertMessageAfterSuccessfulADRSubmission +" alert is not present");
						}
					}else{
						failCounter++;
						report.report("Fail : " + sExpectedAlertMessageBeforeADRSubmission + " alert is not present" );
					}
				}else{
					failCounter++;
					report.report("Fail : File name validation failed after browsing the docs on ADR page");
				}
			}else{
				failCounter++;
				report.report("Fail : ADR response documents browsing failed");
			}
		}else{
			failCounter++;
			report.report("Can't find ADR page after clicking on document upload icon");
		}
		report.report("Fail counter value from verifyADRDocumentUploadFileFormat() method is : "+ failCounter);
		return (failCounter==0)? true : false;
	}

	/**
	 * Use this method to verify CMS status screen after uploading ADR response documents
	 * @return true or false
	 * @throws Exception
	 */
	public boolean verifyCMSStatusScreenAfterADRSubmission(String expectedCMSStatusTableHeaders,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception{

		//remove below if condition after testing this building block
		/*helper.navigateToESMDStatusPage("HHA1");
		if( isElementPresent(By.xpath("//a[contains(text(),'Sent to CMS')]"))){
			clickLink("Sent to CMS");
			Thread.sleep(5000);
		}*/
		//Before validating CMS screen details or ddez.cmsstatusupdates and ddez.adrdocsubmissioninfo wait for three minutes to get the mock time reponses from Audti doc server
		String fileSize = adrFileSize.toString();
		char relationalOperator = fileSize.charAt(0);
		List<String> lsADRFilePaths = helper.getADRFilePath(adrFileType, relationalOperator);
		List<String> lsADRFileNames = helper.getADRFileNamesFromFilePaths(lsADRFilePaths);

		String receivedByCMSESTXpath = "//td[contains(text(),'" + claimIDorDCN + "')]/following-sibling::td[3]";
		String receivedByReviewerESTXpath = "//td[contains(text(),'" + claimIDorDCN + "')]/following-sibling::td[4]";
		String reviewerAckTimeEST = null,  CMSAckTimeInEST = null;
		String CMSAckTimeInCST = null, reviewerAckTimeCST = null;
		long timeDiffInHoursOfCMSAckTime = 0L, timeDiffInHoursOfReviewerAckTime = 0L; 

		report.report("Verifying CMS status screen details");
		report.report("Waiting for ~ 4 minutes to get the mock reposne from Audit Doc server...");
		Thread.sleep(240000);

		//validations
		List<WebElement> lsADRSubmissionTableHeaders = helper.getReportTableHeaders("datatable");
		if ( Verify.compareTableHeaderNames(lsADRSubmissionTableHeaders, expectedCMSStatusTableHeaders)){
			report.report("Validation 1 : CMS status update screen fileds have been validated successfully", ReportAttribute.BOLD);
		}else{
			failCounter++;
			report.report("Validation 1 : CMS status update screen fileds validation failed", ReportAttribute.BOLD);
		}

		helper.navigateBack();
		helper.navigateForward();

		if( helper.waitForADRResponsePageToBeVisible(receivedByCMSESTXpath) ){

			String sQueryCMSStatusUpdate = "SELECT * FROM ddez.cmsstatusupdates where CMSClaimID = "+ claimIDorDCN ;
			Map<String, String> CMSStatusUpdateTableData = helper.getDataFromCMSStatusUpdateTable(sQueryCMSStatusUpdate);

			//verify the review contractor name
			if( CMSStatusUpdateTableData.get("ReviewerName").equalsIgnoreCase(reviewContractorName)){
				report.report("Review contractor name -> expected : " + CMSStatusUpdateTableData.get("ReviewerName") + " and actual : " + 
						reviewContractorName + " are same", ReportAttribute.BOLD);
			}else{
				failCounter++;
				report.report("Fail :Review contractor name -> expected : " + CMSStatusUpdateTableData.get("ReviewerName") + " and actual : " 
						+ reviewContractorName + " are not equal");
			}
			//verify CMS claim ID or DCN name
			if( CMSStatusUpdateTableData.get("CMSClaimID").equalsIgnoreCase(claimIDorDCN)){
				report.report("CMS Claim ID or DCN -> expected : " + CMSStatusUpdateTableData.get("CMSClaimID") + " and actual : " + 
						claimIDorDCN + " are same", ReportAttribute.BOLD);
			}else{
				failCounter++;
				report.report("Fail :CMS Claim ID or DCN -> expected : " + CMSStatusUpdateTableData.get("CMSClaimID") + " and actual : " + claimIDorDCN + " are not equal");
			}
			//verify CMS case ID
			if( CMSStatusUpdateTableData.get("CMSCaseID").equalsIgnoreCase(caseID)){
				report.report("CMS Case ID -> expected : " + CMSStatusUpdateTableData.get("CMSCaseID") + " and actual : " + 
						caseID + " are same", ReportAttribute.BOLD);
			}else{
				failCounter++;
				report.report("Fail :CMS Case ID -> expected : " + CMSStatusUpdateTableData.get("CMSCaseID") + " and actual : " + caseID + " are not equal");
			}

			//Verify Received By CMS and Received by Reviewer time stamps
			CMSAckTimeInCST = CMSStatusUpdateTableData.get("CMSACKTime").replace('-', '/');
			reviewerAckTimeCST = CMSStatusUpdateTableData.get("ReviewerACKTime").replace('-', '/');

			report.report("CMS ack time from DB :" + CMSAckTimeInCST);
			report.report("Reviewer ack time from DB :" + reviewerAckTimeCST);

			WebElement CMSReceivedTime = waitForElementToBeClickable(ByLocator.xpath, receivedByCMSESTXpath, 60);
			CMSAckTimeInEST = CMSReceivedTime.getText().trim();

			WebElement reviewerReceivedTime = waitForElementToBeClickable(ByLocator.xpath, receivedByReviewerESTXpath, 60);
			reviewerAckTimeEST = reviewerReceivedTime.getText().trim();
			timeDiffInHoursOfCMSAckTime = helper.timeDiffInHours(CMSAckTimeInCST, CMSAckTimeInEST);
			timeDiffInHoursOfReviewerAckTime = helper.timeDiffInHours(reviewerAckTimeCST, reviewerAckTimeEST);
			report.report("Received By CMS time stamp in EST : " + CMSAckTimeInEST);
			report.report("Received By CMS time stamp in CST : " + CMSAckTimeInCST);
			report.report("Received By Reviewer time stamp in EST : " + reviewerAckTimeEST);
			report.report("Received By Reviewer time stamp in CST : " + reviewerAckTimeCST);

			if(timeDiffInHoursOfCMSAckTime == 1 && timeDiffInHoursOfReviewerAckTime == 1){	
				report.report("Reviewer acknowledgement time and CMS acknowledge times are displayed correctly in EST time zone", ReportAttribute.BOLD);
			}else{
				failCounter++;
				report.report("Fail : Reviewer acknowledgement time and CMS acknowledge times are not valid !");
			}

			helper.validateADRResponseTableData(CMSStatusUpdateTableData, "CMSTransactionID");
			List<String> lsFileNamesFromToolTip = helper.getToolTipOfView(claimIDorDCN);
			if( Verify.listEquals(lsFileNamesFromToolTip, lsADRFileNames) ) {
				report.report("File names visible on view tool tip are same as uploaded ones", ReportAttribute.BOLD);
			}else{
				failCounter++;
				report.report("Fail : File names visible on view tool tip are different from uploaded ones");
			}

		}else{
			report.report("Fail : ADR Resonse Document submission page data table not populated with received timestamp values, please check!");
			failCounter++;
		}

		report.report("Fail counter value from verifyCMSStatusScreenAfterADRSubmission method is : " + failCounter);
		return (failCounter == 0) ? true : false;
	}

	public boolean verifyRecordsPresenUnderESMDreport()throws Exception{

		String sXpathOfOVERNIGHT = "//span[contains(text(),'OVERNIGHT')]";
		String ADRPageXpath = "//td[contains(text(),'ADR REPORT FOR')]";
		String eSMDPageXpath = "//td[contains(text(),'ESMD DELIVERY & STATUS')]";
		String sADRDataTableXpath = "//table[@id='datatable']/tbody/tr";
		String sEaseFoundNoItemsXpath = "//span[contains(text(),'EASE found no items')]";
		List<WebElement> lsADRRecords = null;
		List<WebElement> lseSMDAndStatusRecords = null;
		int recordCountFromADRReport = 0;
		int recordCountFromeSMDStatusReport = 0;


		int failCounter = 0;

		helper.clickMyDDELink();
		clickLink("Advanced");
		//get the record count from ADR Report
		if(waitForElementToBeClickable(ByLocator.xpath,sXpathOfOVERNIGHT, 30) != null){
			clickLink("ADR");
			if(waitForElementToBeClickable(ByLocator.xpath, ADRPageXpath, 10) != null){
				lsADRRecords = driver.findElements(By.xpath(sADRDataTableXpath));
				recordCountFromADRReport = lsADRRecords.size();
			}	
			clickLink("esMD Delivery & Status");
			if(waitForElementToBeClickable(ByLocator.xpath, eSMDPageXpath, 10) != null){
				if( waitForElementToBeClickable(ByLocator.xpath, sEaseFoundNoItemsXpath, 10) != null){
					helper.changeTimeFrame();
					if (waitForElementToBeClickable(ByLocator.xpath, eSMDPageXpath, 10) != null ){
						lsADRRecords = driver.findElements(By.xpath(sADRDataTableXpath));
						recordCountFromADRReport = lsADRRecords.size();
					}else{
						failCounter++;
						report.report("ESMD DELIVERY & STATUS...text is not present on page after clicking eSMD Delivery & Status Report");
					}
				}else{
					failCounter++;
					report.report("EASE found no items...text is not present on page after clicking eSMD Delivery & Status Report");
				}
				lseSMDAndStatusRecords = driver.findElements(By.xpath(sADRDataTableXpath));
				recordCountFromeSMDStatusReport = lseSMDAndStatusRecords.size();
			}
		}else{
			failCounter++;
			report.report("Fail : Unable to find ADR link on page");
		}

		if( failCounter == 0 && recordCountFromADRReport == recordCountFromeSMDStatusReport){
			report.report("Records count under ADR Report are : " + recordCountFromADRReport, ReportAttribute.BOLD);
			report.report("Records count under eSMD Delivery & Status Report are : " + recordCountFromADRReport, ReportAttribute.BOLD);
			return true;
		}else{
			return false;
		}
	}

	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception{
		boolean result = verifyADRDocumentUploadFileFormats(agency, reviewContractorName, claimIDorDCN, caseID, adrFileType, adrFileSize);
		return result; 
	}

	public long generateRandomInteger(int length)throws Exception{

		Random random = new Random();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
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
