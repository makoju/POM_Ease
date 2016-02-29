package com.ability.ease.auditdoc;

import java.util.List;
import java.util.Map;
import java.util.Random;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.claims.ClaimsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AuditDocPage extends AbstractPageObject{

	AuditDocHelper helper = new AuditDocHelper();
	int failCounter = 0;
	String xpathToADRSubPage = "//td[contains(text(),'ADR Response Document Submission Report')]";

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
		WebElement uploadDocIcon = retryUntilElementIsVisible("//td/center/a/img[1]", 5); 
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
						report.report("Review contractor name on alert validated successfully" + reviewContractorName , ReportAttribute.BOLD);
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
		String receivedByCMSESTXpath = "//td[contains(text(),'" + claimIDorDCN + "')]/following-sibling::td[3]";
		String receivedByReviewerESTXpath = "//td[contains(text(),'" + claimIDorDCN + "')]/following-sibling::td[4]";
		String reviewerAckTimeEST = null,  CMSAckTimeInEST = null;
		String CMSAckTimeInCST = null, reviewerAckTimeCST = null;
		long timeDiffInHoursOfCMSAckTime = 0L, timeDiffInHoursOfReviewerAckTime = 0L; 

		report.report("Waiting for ~ 3.1 minutes to get the mock reposne from Audit Doc server...");
		Thread.sleep(200000);

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
		}else{
			report.report("Fail : ADR Resonse Document submission page data table not populated with received timestamp values, please check!");
			failCounter++;
		}
		
		report.report("Fail counter value from verifyCMSStatusScreenAfterADRSubmission method is : " + failCounter);
		return (failCounter == 0) ? true : false;
	}

	public boolean verifyRecordsPresenUnderESMDreport()throws Exception{

		int failCounter = 0;


		return ( failCounter == 0 ) ? true : false;
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
