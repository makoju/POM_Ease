package com.ability.ease.HMO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class HMOHelper extends AbstractPageObject {

	/*
	 * Entering Patient details into HMO Catcher Page
	 */
	public void fillHmo(String sAgency, String sHIC, String sLastName, String sFirstName, String sDob, String sSex)
			throws Exception {
		waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("ELIG_LINK"), 30);
		safeJavaScriptClick(elementprop.getProperty("ELIG_LINK"));
		if (waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("ELIG_CHECK_HEADER_MESSAGE"),
				40) != null) {
			clickLinkV2(elementprop.getProperty("HMO_CATCHER_ADD_LINK"));
			if (waitForElementToBeClickable(ByLocator.id, elementprop.getProperty("HMO_CATCHER_AGENCY_DROPDOWN_ID"),
					40) != null) {
				selectByNameOrID(elementprop.getProperty("HMO_CATCHER_AGENCY_DROPDOWN_ID"), sAgency);
				typeEditBox(elementprop.getProperty("HIC_TEXTBOX_ID"), sHIC);
				typeEditBox(elementprop.getProperty("LASTNAME_TEXTBOX_ID"), sLastName);
				typeEditBox(elementprop.getProperty("FIRSTNAME_TEXTBOX_ID"), sFirstName);
				typeEditBox(elementprop.getProperty("DOB_TEXTBOX_ID"), sDob);
				selectByNameOrID(elementprop.getProperty("SEX_DROPDOWN_ID"), sSex);
				clickButtonV2("submit");
			} else {
				report.report("Fail : Failed to locate Provider ID dropdwon in Eligibility page!!!");
			}
		} else {
			report.report("Fail: Failed to locate Add to HMO/Adv. Catcher link under Elig. tab!!!");
		}
	}

	/*
	 * Connecting to DB to get Extended Days using HIC and NPI
	 */
	public String HMODBConnection(String sAgency, String sHIC) throws SQLException {
		String sQueryToGetDays = "SELECT DATEDIFF(Termination,createdate) as Days from ddeztest.customer.hmocatcherpatient where providerid="
				+ sAgency + " and HIC='" + sHIC + "'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		String daysVal = "";
		while (results.next()) {
			daysVal = results.getString("Days");
		}
		return daysVal;
	}

	/*
	 * Connecting to DB to get Extended Days using HIC
	 */
	public int getExtendedDaysFromDB(String sHIC) throws SQLException {
		String sQueryToGetDays = "SELECT datediff(Termination,createdate) as Days from ddez.hmocatcherpatient where  HIC='"
				+ sHIC + "'";
		ResultSet results = MySQLDBUtil.getResultFromMySQLDB(sQueryToGetDays);
		int iextendedDays = 0;
		while (results.next()) {
			iextendedDays = results.getInt("Days");
		}
		return iextendedDays;
	}

	/*
	 * Navigate to Patient info from Eligibility-report link
	 */
	public void navigateToPatientInfoPage(String sHIC) throws Exception {
		WebElement greenBox = waitForElementToBeClickable(ByLocator.xpath,
				elementprop.getProperty("ELIG_GREEN_ACTIVITYBOX_XPATH"), 20);
		greenBox.click();
	}

	/*
	 * Navigate to HMO/Adv Catcher Patients
	 */
	public void navigateToHMOCatcherExtendPage() throws Exception {

		if ((waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("ELIG_LINK"), 20)) != null) {
			safeJavaScriptClick(elementprop.getProperty("ELIG_LINK"));
			report.report("Clicked ELIG. link");
			if (waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("HMO_CATCHER_PATIENT_LINK"),
					60) != null) {
				safeJavaScriptClick(elementprop.getProperty("HMO_CATCHER_PATIENT_LINK"));
				report.report("Clicked HMO/Adv Catcher Patients Tab...");
			} else {
				report.report("Failed to navigate to HMO/Adv Catcher Patients Tab");
			}
		} else {
			report.report("Failed to navigate to ELIG. page");
		}
	}

	/**
	 * @author nageswar.bodduri This method just updates the termination time
	 *         stamp for one of the existing records to perform the extend
	 *         operation on HMO catcher
	 */

	public void updateTerminationTime(String sHIC) {
		String updateQuery = "UPDATE ddez.hmocatcherpatient SET CreateDate=now(),Termination=Date_ADD(CreateDate, INTERVAL 5 DAY) WHERE HIC = '"
				+ sHIC + "'";
		MySQLDBUtil.getUpdateResultFromMySQLDB(updateQuery);
		report.report("Termination date updation successful !!!");
	}

	/**
	 * To Insert a HMO record into ddez.hmocatcherpatient to verify duplicate
	 * HMO records are not inserted
	 */

	public void InsertRecordIntoHMOCatcher(String sHIC) {
		String insertQuery = "Insert into hmocatcherpatient values (1,'" + sHIC + "'"
				+ ",now() - INTERVAL 20 DAY,NOW() + INTERVAL 20 DAY,null,null,0,1453)";
		MySQLDBUtil.getInsertUpdateRowsCount(insertQuery);
		report.report("Inserted Recored into hmocatcherpatient !!!");
	}

	/**
	 * To update ddez.hmocatcherpatient table to validate HMOCatcher acknowledge
	 */
	public void updateLastChangedInHMO(String sHIC) {
		String insertQuery = "UPDATE ddez.hmocatcherpatient SET HasChanged=1 WHERE ProviderId=1 and HIC = '" + sHIC
				+ "'";
		MySQLDBUtil.getInsertUpdateRowsCount(insertQuery);
		report.report("update hmocatcherpatient table with Has changed column !!!");
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
