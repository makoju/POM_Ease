package com.ability.ease.auditdoc;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

import jsystem.framework.report.Reporter.ReportAttribute;

public class AuditDocPageV2 extends AbstractPageObject{

	AuditDocHelper helper = new AuditDocHelper();
	boolean stepResult = false;
	int failCounter = 0;
	String esMDLink = elementprop.getProperty("ESMD_LINK");
	String adrStatusReportHdrXpath=elementprop.getProperty("ADR_STATUS_REPORT_HDR_XPATH");
	String agenycDropDownID = elementprop.getProperty("AGENCY_DROPDOWN_ID");

	public boolean changeTimeFrame()throws Exception{

		WebElement reportHeaderBefore = null;
		WebElement reportHeaderAfter = null;
		String reportHeaderText = null;
		navigateToPage();
		reportHeaderBefore = waitForElementToBeClickable(ByLocator.xpath, adrStatusReportHdrXpath, 30);
		if(  reportHeaderBefore != null){
			reportHeaderText = reportHeaderBefore.getText();
			if(waitForElementToBeClickable(ByLocator.linktext, "Timeframe", 30) != null){
				if(!reportHeaderText.contains("2011")){
					helper.changeTimeFrame();
					reportHeaderAfter = driver.findElement(By.xpath(adrStatusReportHdrXpath));
					reportHeaderText = reportHeaderAfter.getText();
					if(reportHeaderText.contains("2011")){
						stepResult = true;
					}
				}
			}
		}
		return stepResult;
	}

	public boolean changeAgency(String agency)throws Exception{

		if(waitForElementToBeClickable(ByLocator.id, agenycDropDownID, 10) != null){
			moveToElement(elementprop.getProperty("AGENCY_LINK"));
			selectByNameOrID(agenycDropDownID, agency);
			clickButton(elementprop.getProperty("CHANGE_AGENCY_BUTTON"));
			WebElement we = waitForElementToBeClickable(ByLocator.xpath, adrStatusReportHdrXpath, 30);
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
			String thirtyDayDueDate,String code,String expectedADRStatusReportTableHeaders)throws Exception{
		
		List<WebElement> lsADRStatusReportTabelColumns = helper.getReportTableHeaders(elementprop.getProperty("REPORT_TABLE_ID"));
		lsADRStatusReportTabelColumns.remove(0);
		if ( Verify.compareTableHeaderNames(lsADRStatusReportTabelColumns, expectedADRStatusReportTableHeaders)){
			report.report("ADR status report column names have been validated successfully", ReportAttribute.BOLD);
		}else{
			failCounter++;
			report.report("ADR status report column names validation failed", ReportAttribute.BOLD);
		}
		
		return (failCounter == 0) ? true : false;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		WebElement adrLink;
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
