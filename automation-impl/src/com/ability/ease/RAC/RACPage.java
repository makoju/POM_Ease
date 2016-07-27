package com.ability.ease.RAC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemWithParam;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class RACPage extends AbstractPageObject {

	RACHelper RAChelper = new RACHelper();

	/*
	 * test.customer Add Claim to RAC Submissionstest.customer
	 */
	public boolean addtoRAC(Map<String, String> mapAttrVal) throws Exception {
		RAChelper.navigateToRACSubmission();
		RAChelper.removeReadOnlyAttr();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml\\RAC\\AddRAC.xml", mapAttrVal);
		UIActions addRAC = new UIActions();
		addRAC.fillScreenAttributes(lsAttributes);
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE);
		driver.findElement(By.xpath(elementprop.getProperty("ADD_RAC_CLAIM_SUBMIT_XPATH"))).click();
		if (verifyAlert("Successfully processed RAC Claim Info Submission")) {
			report.report("Claim test.customeris added to RAC Successfully!");
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Method to modify the Change Agency
	 */

	public boolean changeAgency(String sAgency) throws Exception {
		moveToElement(driver.findElement(By.id(elementprop.getProperty("AGENCY_MENU_ID"))));
		selectByNameOrID(elementprop.getProperty("AGENCY_DROPDOWN_ID"), sAgency);
		driver.findElement(By.id(elementprop.getProperty("CHANGE_AGENCY_BUTTON_ID"))).click();
		String agencyName = driver.findElement(By.xpath(elementprop.getProperty("REPORT_HEADER_XPATH"))).getText();
		boolean isAgencySelected = agencyName.contains(agencyName) ? true : false;
		return isAgencySelected;

	}

	/*
	 * Verify RAC Claim Record in DataBase Base
	 */

	boolean VerifyClaimInRACSubmissioninDB(String sHIC, String sDCN, String sPatientCtrlNo, String sStartDate,
			String sThroughDate, String sRefIdParam, String sLetterDate, String sNotes) throws Exception {
		String dbHic = null;
		String dbDCN = null;
		String dbPatientCtrlNo = null;
		String dbStartDate = null;
		String dbThroughDate = null;
		String dbLetterreferenceId = null;
		String dbLetterDate = null;
		String dbNotes = null;
		boolean verifyRAC = false;
		sStartDate = sStartDate.substring(6, 10) + "-" + sStartDate.substring(0, 2) + "-" + sStartDate.substring(3, 5);
		sThroughDate = sThroughDate.substring(6, 10) + "-" + sThroughDate.substring(0, 2) + "-"
				+ sThroughDate.substring(3, 5);
		sLetterDate = sLetterDate.substring(6, 10) + "-" + sLetterDate.substring(0, 2) + "-"
				+ sLetterDate.substring(3, 5);
		String query = "SELECT * FROM ddez.racauditclaim where DCN=" + sDCN + " and HIC='" + sHIC + "'";
		ResultSet racClaimDetails = MySQLDBUtil.getResultFromMySQLDB(query);
		while (racClaimDetails.next()) {
			dbHic = racClaimDetails.getString("HIC");
			dbDCN = racClaimDetails.getString("DCN");
			dbPatientCtrlNo = racClaimDetails.getString("PatientCtrlNo");
			dbStartDate = racClaimDetails.getString("StartDateOfService");
			dbThroughDate = racClaimDetails.getString("EndDateOfService");
			dbLetterreferenceId = racClaimDetails.getString("RefID");
			dbLetterDate = racClaimDetails.getString("LetterDate");
			dbNotes = racClaimDetails.getString("Notes");

		}

		if (dbHic.equals(sHIC) && dbDCN.equals(sDCN) && dbPatientCtrlNo.equals(sPatientCtrlNo)
				&& dbStartDate.equals(sStartDate) && dbThroughDate.equals(sThroughDate)
				&& dbLetterDate.equals(dbLetterDate) && dbNotes.equalsIgnoreCase(sNotes))
		// && dbLetterreferenceId.equals(sRefIdParam))
		{
			verifyRAC = true;
		}
		return verifyRAC;

	}

	/*
	 * Verify RAC Claim Record in the RAC Report
	 */
	boolean VerifyClaimInRACSubmissioninReport(String sHIC) throws SQLException {
		boolean hicInReport = false;
		int iDaysleftFromDB = 0;
		WebElement racReporttable = driver
				.findElement(By.xpath(elementprop.getProperty("RAC_SUBMISSION_REPORT_TABLE_XPATH")));
		List<WebElement> racReporttableRows = racReporttable.findElements(By.tagName("tr"));
		int iDaysleftFromApp = Integer
				.parseInt(driver.findElement(By.xpath(elementprop.getProperty("RAC_DUEDATE_XPATH"))).getText()) + 1;
		String queryToGetDaysLeft = "select DATEDIFF(DueDate, LetterDate) as DaysLeft FROM ddez.racauditclaim where HIC='"
				+ sHIC + "'";
		ResultSet dayLeftResult = MySQLDBUtil.getResultFromMySQLDB(queryToGetDaysLeft);
		while (dayLeftResult.next()) {
			iDaysleftFromDB = Integer.parseInt(dayLeftResult.getString("DaysLeft"));
		}
		for (int i = 0; i < racReporttableRows.size(); i++) {
			List<WebElement> racReporttableColumns = racReporttableRows.get(i).findElements(By.tagName("td"));
			for (int j = 0; j < racReporttableColumns.size(); j++) {
				{
					if (racReporttableColumns.get(j).getText().equals(sHIC)) {
						hicInReport = true;
						break;
					}
				}

			}

		}

		if (hicInReport = true && iDaysleftFromDB == iDaysleftFromApp) {
			return true;
		} else {
			return false;
		}
	};

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}
}
