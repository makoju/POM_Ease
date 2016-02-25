package com.ability.ease.auditdoc;

import java.io.File;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AuditDocPage extends AbstractPageObject{

	AuditDocHelper helper = new AuditDocHelper();

	public boolean verifyADRDocumentUploadFileFormats(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID,ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception{

		int failCounter = 0;
		String sADRFilePath = null, sADRFileName = null;
		String sExpectedAlertMessageWithReviewContractorName = "Proceed with ADR Document Submission to Review Contractor : "+ reviewContractorName +"?";
		String sExpectedAlertMessageBeforeProceedingWithSubmission = "Successfully processed ADR response documents Submission";
		String adrPageXpath = "//td[contains(text(),'ADR INFORMATION')]";
		String fileSize = adrFileSize.toString();
		char relationalOperator = fileSize.charAt(0);
		sADRFilePath = helper.getADRFilePath(adrFileType, relationalOperator);
		File f = new File(sADRFilePath);
		sADRFileName = f.getName();
		String xpathAfterFileUpload = "//span[contains(text(),'" + sADRFileName + "')]";
		
		helper.navigateToESMDStatusPage();
		moveToElement("Agency");
		selectByNameOrID("reportAgencySelect", agency);
		clickButtonV2("changeAgencyButton");
		moveToElement("Timeframe");
		typeEditBox("reportCustomDateFrom", "2/23/2011");
		clickButtonV2("reportTimeframeButton");
		//Thread.sleep(5000);test.customer
		WebElement uploadDocIcon = retryUntilElementIsVisible("//td/center/a/img[1]", 5); 
		uploadDocIcon.click();
		waitForElementVisibility(By.xpath(adrPageXpath));
		WebElement adrPage = retryUntilElementIsVisible(adrPageXpath, 5);
		if( adrPage != null ){
			report.report("ADR Document filepath is : " + sADRFilePath);
			selectByNameOrID("reviewContractor", reviewContractorName);
			typeEditBox("ClaimId", claimIDorDCN);
			typeEditBox("CaseId", caseID);
			clickButtonV2("fileUploadId");
			uploadFileAutoIT(sADRFilePath);
			Thread.sleep(5000);
			WebElement afterFileUpload = waitForElementVisibility(By.xpath(xpathAfterFileUpload), 60);
			if( afterFileUpload != null){
				report.report("File browsing completed successfully", ReportAttribute.BOLD);
				clickButtonV2("Send");
				if(verifyAlert(sExpectedAlertMessageWithReviewContractorName)){
					report.report("Review contractor alert handeled successfully" + reviewContractorName );

					if(verifyAlert(sExpectedAlertMessageBeforeProceedingWithSubmission)){
						report.report("Successfully handeled ADR sumission alert");
					}

				}else{
					failCounter++;
					report.report("Fail : ADR Document submission failed after clicking send button");
				}
			}else{
				failCounter++;
				report.report("Fail : File browsing failed");
			}

		}else{
			failCounter++;
			report.report("Can't find ADR page after clicking on document upload icon");
		}



		return (failCounter==0)? true : false;
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
