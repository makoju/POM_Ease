package com.ability.ease.auditdoc;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.portal.selenium.ESMDSubmissionType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

public class AuditDocPageV2 extends AbstractPageObject{

	AuditDocHelper helper = new AuditDocHelper();
	boolean stepResult = false;
	int failCounter = 0;
	String esMDLink = elementprop.getProperty("ESMD_LINK");


	String agenycDropDownID = elementprop.getProperty("ESMD_AGENCY_DROPDOWN_ID");

	public boolean changeTimeFrame(ESMDSubmissionType esMDSubType)throws Exception{

		WebElement reportHeaderBefore = null;
		WebElement reportHeaderAfter = null;
		String reportHeaderText = null;
		String esMDStatusReportHdrXpath=helper.setESMDHeaderXPATH(esMDSubType);

		//navigateToPage();
		reportHeaderBefore = waitUntilElementVisibility(By.xpath(esMDStatusReportHdrXpath));
		if(  reportHeaderBefore != null){
			reportHeaderText = reportHeaderBefore.getText();
			if(waitForElementToBeClickable(ByLocator.linktext, "Timeframe", 30) != null){
				if(!reportHeaderText.contains("2011")){
					helper.changeTimeFrame();
					reportHeaderAfter = driver.findElement(By.xpath(esMDStatusReportHdrXpath));
					reportHeaderText = reportHeaderAfter.getText();
					if(reportHeaderText.contains("2011")){
						stepResult = true;
					}
				}else{
					stepResult = true;
				}
			}
		}
		return stepResult;
	}

	public boolean changeAgency(ESMDSubmissionType esMDSubType, String agency)throws Exception{

		String esMDStatusReportHdrXpath=helper.setESMDHeaderXPATH(esMDSubType);

		if(waitForElementToBeClickable(ByLocator.id, agenycDropDownID, 10) != null){
			moveToElement(elementprop.getProperty("AGENCY_LINK"));
			selectByNameOrID(agenycDropDownID, agency);
			clickButton(elementprop.getProperty("CHANGE_AGENCY_BUTTON"));
			moveToElement(driver.findElement(By.id("reportPrint")));
			WebElement we = waitForElementToBeClickable(ByLocator.xpath, esMDStatusReportHdrXpath, 30);
			if(we != null){
				if( we.getText().contains(agency) ){
					stepResult = true;
					report.report("Changed to agency to : " + agency);
				}else{
					report.report("Failed to change agency to : " + agency);
				}
			}
		}
		return stepResult;
	}

	public boolean verifyADRESMDStatusReportColumns(String HIC,String patientName,String daysDue,
			String thirtyDayDueDate,String code,String expectedADRStatusReportTableHeaders,String agency)throws Exception{

		List<WebElement> lsADRStatusReportTabelColumns = helper.getReportTableHeaders(elementprop.getProperty("REPORT_TABLE_ID"));
		lsADRStatusReportTabelColumns.remove(0);
		if ( Verify.compareTableHeaderNames(lsADRStatusReportTabelColumns, expectedADRStatusReportTableHeaders)){
			report.report("ADR status report column names have been validated successfully", ReportAttribute.BOLD);
		}else{
			failCounter++;
			report.report("ADR status report column names validation failed", ReportAttribute.BOLD);
		}

		//verifying the links available in ADR status report table
		if( !helper.verifyHICLink(HIC))
			failCounter++;

		if( !helper.verifyPatientNameLink(patientName))
			failCounter++;

		if( !helper.verifyDueDateLink(daysDue))
			failCounter++;

		if( !helper.verifyThirteeDayDueDateLink(thirtyDayDueDate))
			failCounter++;

		if(!helper.verifyCodeLink(code))
			failCounter++;

		if(!helper.verifyToolTips(agency))
			failCounter++;

		return (failCounter == 0) ? true : false;
	}

	public boolean selectFilesToUpload(ESMDSubmissionType esMDSubmissionType, ADRFileFomat fileType, 
			ADRFilesSize fileSize,String reviewContractorName)throws Exception{

		int failCounter = 0;

		String size = fileSize.toString();
		char relationalOperator = size.charAt(0);
		List<String> lsESMDFilePaths = helper.getADRFilePath(fileType, relationalOperator);
		List<String> lsESMDFileNames = helper.getADRFileNamesFromFilePaths(lsESMDFilePaths);

		String expectedAlertMessage = helper.setReviewContractorAlertMgs(esMDSubmissionType, reviewContractorName);

		if( !helper.uploadFilesAutoIT(lsESMDFilePaths)){
			failCounter++;
		}else{
			report.report("File(s) browsing is completed successfully", ReportAttribute.BOLD);
		}

		if ( helper.validateFilesBrowsed(lsESMDFileNames)){
			report.report("File(s) names has been valdiated !!!", ReportAttribute.BOLD);
			clickButtonV2("Send");
			//validate review contractor name on alert box appears before submitting the ADR to a review contractor
			if(verifyAlert(expectedAlertMessage)){		
				report.report("Review contractor name on alert validated successfully : " + reviewContractorName , ReportAttribute.BOLD);
				//validate successful submission of ADR
				if(verifyAlertV2(elementprop.getProperty("ALERT_MSG_AFTER_SUBMISSION"))){
					report.report("Successfully processed ADR response documents submission", ReportAttribute.BOLD);
					waitForElementToBeClickable(ByLocator.xpath, "//a[contains(text(),'Sent to CMS')]", 10);
				}else{
					failCounter++;
				}
			}else{
				failCounter++;
			}

		}
		return ( failCounter == 0 ) ? true : false;
	}

	public boolean navigateToESMDPage(ESMDSubmissionType esMDSubmissionType)throws Exception{
		return helper.navigateToESMDStatusPage(esMDSubmissionType);
	}

	public boolean fillDocumentUploadScreenValues(ESMDSubmissionType esMDSubmissionType, String reviewContractorName, 
			String claimIDorDCN, String caseID)throws Exception{

		boolean stepResult = false;
		String esMDInfoPageXpath = null;

		if(esMDSubmissionType.toString().equalsIgnoreCase("RAC")){
			esMDInfoPageXpath = MessageFormat.format(elementprop.getProperty("ESMD_INFO_PAGE_HDR_XPATH"), "'RAC AUDIT INFORMATION'");
		}else{
			esMDInfoPageXpath = MessageFormat.format(elementprop.getProperty("ESMD_INFO_PAGE_HDR_XPATH"), 
					"'" + esMDSubmissionType.toString().toUpperCase()+" INFORMATION'");
		}

		WebElement uploadScreen = waitForElementToBeClickable(ByLocator.xpath, esMDInfoPageXpath, 30);

		if( uploadScreen != null ){
			//enter review contractor name and other required details
			selectByNameOrID("reviewContractor", reviewContractorName);
			typeEditBox("CMSClaimIDParam", claimIDorDCN);
			typeEditBox("CaseIdParam", caseID);
			stepResult = true;
		}
		return stepResult;

	}

	public boolean selectClaimRecordToUploadDocuments(ESMDSubmissionType esMDSubType,String HIC)throws Exception{

		boolean stepResult = false;
		String uploadIconXpath = helper.setESMDDocUploadIconXPATH(esMDSubType, HIC);

		WebElement uploadDocIcon = waitForElementToBeClickable(ByLocator.xpath, uploadIconXpath, 30);
		if( uploadDocIcon != null){
			uploadDocIcon.click();
			if(waitForElementToBeClickable(ByLocator.id, elementprop.getProperty("ESMD_SEND_BTN_ID"), 30) != null){
				stepResult = true;
			}
		}else{
			report.report("Document upload icon is not present with given HIC , hence clicking on the first record doc icon");
			//need to be implemented
		}
		return stepResult;
	}

	public boolean clickCMSLink(ESMDSubmissionType esmdSubmissionType)throws Exception{
		
		boolean stepResult = false;
		String esMDCMSStatusReportHeaderXPATH = helper.setCMSStausTableHeaderXpath(esmdSubmissionType);
		
		WebElement cmsLink = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("CMS_LINK_XPATH"), 10);
		
		if( cmsLink != null){
			clickLink("Sent to CMS");
			if(waitForElementToBeClickable(ByLocator.xpath, esMDCMSStatusReportHeaderXPATH, 60) != null){
				report.report("Successfully navigated to CMS status report screen!!!!");
				stepResult = true;
			}
		}
		
		return stepResult;
		
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		WebElement adrLink;

		String adrStatusReportHdrXpath = elementprop.getProperty("ADR_STATUS_REPORT_HDR_XPATH");
		try{
			WebElement esMD = waitForElementToBeClickable(ByLocator.linktext, esMDLink, 80);
			String classAttr = esMD.getAttribute("class");
			if ( esMD != null) {
				if( classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected") && 
						waitForElementToBeClickable(ByLocator.xpath, adrStatusReportHdrXpath, 30) == null){
					safeJavaScriptClick(esMD);
					report.report("Clicked on esMD link");
					adrLink = waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("ADR_LINK"), 15);
					if ( adrLink != null ){
						safeJavaScriptClick(adrLink);
						waitForElementToBeClickable(ByLocator.xpath, adrStatusReportHdrXpath, 30);
						report.report("Clicked on ADR Submission Tab");
					}
				}else{
					safeJavaScriptClick(esMD);
					report.report("Clicked on esMD link");
					waitForElementToBeClickable(ByLocator.xpath, adrStatusReportHdrXpath, 30);
					adrLink = waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("ADR_LINK"), 15);
					if ( adrLink != null ){
						safeJavaScriptClick(adrLink);
						waitForElementToBeClickable(ByLocator.xpath, adrStatusReportHdrXpath, 30);
						report.report("Clicked on ADR Submission Tab");
					}
				}
			}
		}catch(Exception e){
			report.report("esMD link is not visible to click...!!!");
		}
	}

}
