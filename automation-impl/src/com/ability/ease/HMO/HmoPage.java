package com.ability.ease.HMO;

import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.home.HomePage.SubMenu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;
import com.ability.ease.testapi.IHMO;

public class HmoPage extends AbstractPageObject {

	HMOHelper hhp = new HMOHelper();
	boolean alertverify;
	int idaysVal;

	/*
	 * Adding a Patient to HMO Catcher
	 */
	public boolean addToHMO(String sAgency, String sHIC, String sLastName, String sFirstName, String sDob, String sSex)
			throws Exception {
		hhp.fillHmo(sAgency, sHIC, sLastName, sFirstName, sDob, sSex);
		alertverify = verifyAlert("Patient was successfully added to HMO Advantage Move Catcher!");
		int idaysVal = hhp.getExtendedDaysFromDB(sHIC);
		if (alertverify && idaysVal == 75) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Adding a Patient to HMO Catcher, which already being tracked
	 */
	public boolean addToHMODuplicatePatient(String sAgency, String sHIC, String sLastName, String sFirstName,
			String sDob, String sSex) throws Exception {
		String sExpectedMessageOnAlertBox = "Your request to add this patient to the HMO Advantage Move Catcher was not accepted because this patient is "
				+ "already being tracked by HMO Advantage Move Catcher!";
		hhp.fillHmo(sAgency, sHIC, sLastName, sFirstName, sDob, sSex);
		if (verifyAlert(sExpectedMessageOnAlertBox)) {
			return true;
		} else {
			report.report("Alert messages are not matching !!!");
			return false;
		}
	}

	/*
	 * Extending the HMO Catcher by 75 days,need to have data with less than 10
	 * days in the application
	 */
	public boolean extendHMO(String sHIC) throws Exception {
		boolean isExtended = false;
		int idays;
		String xpathToCheckBox = MessageFormat.format(elementprop.getProperty("HMOCATCHER_RECORD_SEL_CHECKBOX_XPATH"),
				"'" + sHIC + "'");
		String xpathDaysLeft = MessageFormat.format(elementprop.getProperty("DAYLEFT_XPATH"), "'" + sHIC + "'");
		hhp.updateTerminationTime(sHIC);
		hhp.navigateToHMOCatcherExtendPage();
		idays = Integer.parseInt(driver.findElement(By.xpath(xpathDaysLeft)).getText()) + 75;
		driver.findElement(By.xpath(xpathToCheckBox)).click();
		clickLink("Extend");
		isExtended = verifyAlert("Patient(s) successfully changed on HMO Advantage Catcher!");
		if (idays > 75 && isExtended) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Adding a Patient to HMO Catcher from patient info page
	 */
	public boolean addtoHMOFromPatientInfo(String sHIC) throws Exception {
		MySQLDBUtil.insertEligibilityCheckSP(1453, "09/09/2009", "Fname", sHIC, "Lname", "M");
		hhp.navigateToPatientInfoPage(sHIC);
		String datatable = "datatable";
		String report = "Repoort";
		String xpathReportLinkCompletedActivityLog = MessageFormat
				.format(elementprop.getProperty("COMPLETED_ACTIVITY_LOG_REPORT_HIC_XPATH"), "'" + sHIC + "'");
		driver.findElement(By.xpath(xpathReportLinkCompletedActivityLog)).click();
		clickLink(elementprop.getProperty("ADD_PATIENT_HMO_CATCHER_LIST_PATIENTINFO_LINK"));
		alertverify = verifyAlert("Patient was successfully added to HMO Advantage Move Catcher!");
		safeJavaScriptClick(elementprop.getProperty("HMO_CATCHER_PATIENT_LINK"));
		int ipatientdays = hhp.getExtendedDaysFromDB(sHIC);
		if (alertverify && ipatientdays == 75) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Adding a Patient to HMO Catcher from patient info page,which is already
	 * being tracked
	 */
	public boolean addDuplicatePatientToHMOFromPatientInfo(String sHIC) throws Exception {
		hhp.InsertRecordIntoHMOCatcher(sHIC);
		String xpathReportLinkCompletedActivityLog = MessageFormat
				.format(elementprop.getProperty("COMPLETED_ACTIVITY_LOG_REPORT_HIC_XPATH"), "'" + sHIC + "'");
		MySQLDBUtil.insertEligibilityCheckSP(1453, "09/09/2009", "Fname", sHIC, "Lname", "M");
		hhp.navigateToPatientInfoPage(sHIC);
		driver.findElement(By.xpath(xpathReportLinkCompletedActivityLog)).click();
		clickLink(elementprop.getProperty("ADD_PATIENT_HMO_CATCHER_LIST_PATIENTINFO_LINK"));
		return verifyAlert(
				"Your request to add this patient to the HMO Advantage Move Catcher was not accepted because this patient is already being tracked by HMO Advantage Move Catcher!");

	}

	/*
	 * Removing a patient from HMO Catcher
	 */
	public boolean trashHMOPatient(String sHIC) throws Exception {

		boolean result = false;
		String xpathToCheckBox = MessageFormat.format(elementprop.getProperty("HMOCATCHER_RECORD_SEL_CHECKBOX_XPATH"),
				"'" + sHIC + "'");

		hhp.navigateToHMOCatcherExtendPage();
		if (isTextExistInTable(sHIC, 5)) {
			driver.findElement(By.xpath(xpathToCheckBox)).click();
			clickLinkV2("reportDelete");
			alertverify = verifyAlert("Patient(s) successfully changed on HMO Advantage Catcher!");
			Thread.sleep(5000);
			if (alertverify && !isTextExistInTable(sHIC, 60)) {
				result = true;
			} else {
				result = false;
			}
		}
		return result;
	}

	/*
	 * Acknowledge Patient from HMO Catcher
	 */
	public boolean acknowledgeHMO(String sHIC) throws Exception {
		safeJavaScriptClick("ELIG.");
		String xpathToAcknowledgeHmo = MessageFormat.format(elementprop.getProperty("ACKNOWLEDGE_HMO_CATHER_XPATH"),
				"'" + sHIC + "'");
		hhp.updateLastChangedInHMO(sHIC);
		if ((driver.findElement(By.linkText(elementprop.getProperty("HMO_CATCHER_REPOERT_LINK"))) != null)) {
			waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("HMO_CATCHER_REPOERT_LINK"), 10);
			safeJavaScriptClick(elementprop.getProperty("HMO_CATCHER_REPOERT_LINK"));
			driver.findElement(By.xpath(xpathToAcknowledgeHmo)).click();
		}

		Thread.sleep(5000);
		if (!isTextExistInTable(sHIC, 60)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean AdvanceSearchFromHMO(String sHIC) throws Exception {
		int ifailCount = 0;
		List<String> al = new ArrayList();
		int iflag = 1;
		String patientInfoXpath = "//td[contains(text(),'PATIENT INFORMATION')]";

		hhp.navigateToHMOCatcherExtendPage();
		WebElement searchIcon = driver.findElement(By.id("reportHICSearch"));
		moveToElement(searchIcon);
		typeEditBox("reportHICEntry", sHIC);
		clickButton("reportHICButton");
		boolean bliveSearcheligcheck = driver.getPageSource().contains("ELIGIBILITY CHECK AND CLAIMS SCRAPE");
		waitForElementToBeClickable(ByLocator.xpath, patientInfoXpath, 10);
		boolean bliveSearchPatientInfo = driver.getPageSource().contains("PATIENT INFORMATION ");
		if (bliveSearcheligcheck == true || bliveSearchPatientInfo == true) {
			al.add("Pass");
			report.report("Live Search from HMO is working fine", Reporter.ReportAttribute.BOLD);
		} else {
			al.add("Fail");
			report.report("Live Search from HMO is not working fine", Reporter.FAIL);
		}

		hhp.navigateToHMOCatcherExtendPage();
		moveToElement(searchIcon);
		WebElement advanceSearch = driver.findElement(By.xpath("//*[@id='reportAdvanceSearch']"));
		safeJavaScriptClick(advanceSearch);
		boolean bliveSearchAdvance = driver.getPageSource().contains("ADVANCED SEARCH");

		if (bliveSearchAdvance == true) {
			al.add("Pass");
			report.report("Advance Search Navigation from HMO is working fine", Reporter.ReportAttribute.BOLD);
		} else {
			al.add("Fail");
			report.report("Advance Search Navigation from HMO is not working fine", Reporter.FAIL);
		}

		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).equals("Fail")) {
				iflag = 0;
				break;
			}
		}
		if (iflag == 1) {
			return true;
		} else {
			return false;
		}

	}

	public boolean printHMO() throws Exception {
		hhp.navigateToHMOCatcherExtendPage();
		driver.findElement(By.xpath(".//*[@id='reportPrint']")).click();
		waitForElementToBeClickable(ByLocator.xpath, ".//*[@id='reportPrint']", 8);
		Runtime.getRuntime().exec("C:\\Users\\srinivas.bandari\\Desktop\\Automation\\print.exe");
		return true;
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
