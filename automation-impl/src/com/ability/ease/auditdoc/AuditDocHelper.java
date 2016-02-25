package com.ability.ease.auditdoc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AuditDocHelper extends AbstractPageObject{

	//this method clicks on MY DDE Link
	public void navigateToESMDStatusPage() throws Exception{

		WebElement mydde = waitForElementVisibility(By.linkText("MY DDE"));
		String classAttr = mydde.getAttribute("class");
		if ( mydde != null) {
			if( !classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")){
				clickLink("MY DDE");
				clickEsmdStatusLink();
				return;
			}else{
				clickEsmdStatusLink();
			}
		}
	}

	public void clickEsmdStatusLink()throws Exception{
		String sXpathOfOVERNIGHT = "//span[contains(text(),'OVERNIGHT')]";
		String sXpathofEsmdReport = "//td[contains(text(),'ESMD DELIVERY & STATUS REPORT')]";
		clickLink("Advanced");
		Thread.sleep(5000);
		WebElement overNightText = retryUntilElementIsVisible(sXpathOfOVERNIGHT, 10);
		if(overNightText != null){
			clickLink("esMD Delivery & Status");
			WebElement esmdReportText = retryUntilElementIsVisible(sXpathofEsmdReport, 60);
		}
	}
	
	public String getADRFilePath(ADRFileFomat adrFileType,char relationalOperator){
		String sADRFilePath = null;
		
		switch (adrFileType) {
		case PDF:
			if( relationalOperator == '>'){
				sADRFilePath = TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_46.2MB.pdf";
			}else if( relationalOperator == '<'){
				sADRFilePath = TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_PDF_0.10MB.pdf";
			}
			break;
		case TIFF:
			if( relationalOperator == '>'){
				sADRFilePath = TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_45MB.tiff";
			}else if( relationalOperator == '<'){
				sADRFilePath = TestCommonResource.getTestResoucresDirPath()+"auditdocfiles\\AuditDoc_ADR_TIFF_0.13MB.TIFF";
			}
			break;
		case TIF:
			
			break;
			
		case PDFandTIFF:
			
			break;

		default:
			report.report("Fail : You have selected un-supported file format to upload", report.WARNING);
			break;
		}
		
		return sADRFilePath;
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
