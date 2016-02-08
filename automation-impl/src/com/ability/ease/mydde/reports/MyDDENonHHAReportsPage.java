package com.ability.ease.mydde.reports;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class MyDDENonHHAReportsPage extends AbstractPageObject {

	String tableheadersxpath = "//table[@id='datatable']//tr[@class='tableheaderblue']/td";
	ReportsHelper reportshelper = new ReportsHelper();

	public boolean verifyNonHHASummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Change column shows the change to a claim status or an eligibility status which caused it to be included in the report.",
				"The ELG column shows if there is a problem with patient eligibility. A Green Check mark means that there were no problems found with eligibility. "
						+ "A Red X means that there was an eligibility problem or that EASE was not able to find the eligibility for the patient. "
						+ "IMPORTANT: Hover over any Red X to see the detailed information regarding the eligibility problem.",
						"The claim Status and Location.","The Patient HIC number.","The name of the patient.","Type of Bill.","The claim reason code.",
						"The date the patient was originally admitted.","The claim start date.","The claim through date.","The reimbursement amount posted by Medicare on the claim.",
		"The last time the claim was updated in Ease from DDE."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Change", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'SUMMARY REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "SUMMARY REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"SUMMARY REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;

	}

	public boolean verifyNonHHASummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);

		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}
	public boolean verifyNonHHASummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		// navigation part
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		lastupdatedate = Verify.getTableData("datatable", 1, 13);

		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}
		if (istimeframedaterange) {
			if (!Verify.datewithinDateRange(lastupdatedate, fromdate, todate))
				failurecount++;
		}

		else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaymentSummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The date of payment.","The day of week for the payment.","The check number from Medicare (if available).",
				"The number of claims paid on this date.","This is the amount Medicare scheduled to pay.","This is the amount actually paid on the check.",
				"This is the amount that is projected to be paid.","The last time the claim was updated in Ease from DDE."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Payment Summary");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Day", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'PAYMENT SUMMARY REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "PAYMENT SUMMARY REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"PAYMENT SUMMARY REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Payment Summary");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaymentSummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Payment Summary");

		lastupdatedate = Verify.getTableData("datatable", 1, 8);

		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}
		if (istimeframedaterange) {
			if (!Verify.datewithinDateRange(lastupdatedate, fromdate, todate))
				failurecount++;
		}

		else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaymentReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The date of payment.","The Patient HIC number.","The name of the patient.","Type of Bill.",
				"The date the claim was submitted to Medicare.","The date the patient was originally admitted.","The claim start date.",
				"The claim through date.","The reimbursement amount posted by Medicare on the claim.","The last time the claim was updated in Ease from DDE."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Payment");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'PAYMENT REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "PAYMENT REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"PAYMENT REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaymentReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Payment");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaymentReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Payment");

		lastupdatedate = Verify.getTableData("datatable", 1, 10);

		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}
		if (istimeframedaterange) {
			if (!Verify.datewithinDateRange(lastupdatedate, fromdate, todate))
				failurecount++;
		}

		else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHASubmittedClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The month of submission.","The number of episodes in this month.","The number of new patients admissions.",
				"The number of discharged patients.","The number of claims that have been paid.","The number of claims that have been cancelled.",
				"The number of claims that have errors.","The total number of claims that have not been paid."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Submitted Claims");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Claims", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'SUBMITTED CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "SUBMITTED CLAIMS REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"SUBMITTED CLAIMS REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHASubmittedClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Submitted Claims");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAUnPaidClaimsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The status of the claim.","The claim Status and Location.",
				"The claim reason code.","The Patient HIC number.","The name of the patient.","Your agency’s internal ID for this patient.",
				"Type of Bill.","The date the patient was originally admitted.","The claim start date.","The claim through date.",
				"The date the claim was submitted to Medicare.","The number of days since the claim was submitted to Medicare.",
				"The amount charged for this claim.","The reimbursement amount posted by Medicare on the claim."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Unpaid Claims");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Status", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "UNPAID CLAIMS REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"UNPAID CLAIMS REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAUnPaidClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Unpaid Claims");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAStuckInSuspenseSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"","","","","","","","","","",""};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Stuck In Suspense");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Status", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'STUCK IN SUSPENSE REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "STUCK IN SUSPENSE REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"STUCK IN SUSPENSE REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAStuckInSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Stuck In Suspense");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAOnHoldSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"","","","","","","","","","",""};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("On Hold");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Status", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'Z-ON HOLD REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "Z-ON HOLD REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"Z-ON HOLD REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAOnHoldExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("On Hold");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAADRSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"The claim start date.","The claim through date.","The reimbursement amount posted by Medicare on the claim.",
				"The number of days left to respond to the ADR.","The date an action is required by the FI in order to resolve the ADR.",
				"Ensure that ADR documentation is mailed by this date to avoid unnecessary auto-denying of your claim.",
				"The code associated with the ADR.","Total Amount Billed for Claim","The last time the claim was updated in Ease from DDE."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("ADR");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'ADR REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "ADR REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"ADR REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAADRExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("ADR");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAADRLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("ADR");

		lastupdatedate = Verify.getTableData("datatable", 1, 12);

		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}
		if (istimeframedaterange) {
			if (!Verify.datewithinDateRange(lastupdatedate, fromdate, todate))
				failurecount++;
		}

		else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAEligibilityIssuesSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The patient's Date of Birth.","The patient sex code (M or F).",
				"Whether or not the patient is under your care.","Whether or not the patient is eligible for Medicare Part A.",
				"Whether or not the patient is eligible for Medicare Part B.","Whether or not the patient exhausted Part A benefits.",
				"Whether or not the patient currently covered by an HMO plan.","Whether or not the patient has an MSP or liability period.",
				"Whether or not an Home Health Agency is/was treating the patient for the same time period.","Whether or not the patient has been treated by a hospice agency during the episode."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Eligibility Issues");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'ELIGIBILITY ISSUES REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "ELIGIBILITY ISSUES REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"ELIGIBILITY ISSUES REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAEligibilityIssuesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Eligibility Issues");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The patient's Date of Birth.","The patient sex code (M or F).",
				"Whether or not the patient is under your care.","Whether or not the patient has an eligibility issue.","Whether or not the patient is eligible for Medicare Part A.",
				"Whether or not the patient is eligible for Medicare Part B.","Whether or not the patient exhausted Part A benefits.",
				"Whether or not the patient currently covered by an HMO plan.","Whether or not the patient has an MSP or liability period.",
				"Whether or not another is/was agency treating the patient for the same time period.","Whether or not the patient has been treated by a hospice agency during the episode.",
				"The remaining Part A deductable to be met.","The remaining Part B deductable to be met."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Patients");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'PATIENTS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "PATIENTS REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"PATIENTS REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Patients");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAEligibilityErrorsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
		"The Eligibility problem description (if available)."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Eligibility Errors");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'ELIGIBILITY ERRORS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "ELIGIBILITY ERRORS REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"ELIGIBILITY ERRORS REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAEligibilityErrorsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Eligibility Errors");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAMSPPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The MSP Insurer name.","The MSP Code option.",
				"The date the patient was originally admitted.","The claim start date.","The reimbursement amount posted by Medicare on the claim."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("MSP Patients");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'MSP REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "MSP REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"MSP REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAMSPPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("MSP Patients");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAHHAPatientsSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {""};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("HHA Patients");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'HHA OVERLAP REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "HHA OVERLAP REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"HHA OVERLAP REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAHHAPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("HHA Patients");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAOverlappingHospiceSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"The reimbursement amount posted by Medicare on the claim.","The other Hospice provider ID.","The other Hospice start of care date.",
		"The other hospice end of care date."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Overlapping Hospice");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'OVERLAPPING HOSPICE REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "OVERLAPPING HOSPICE REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"OVERLAPPING HOSPICE REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAOverlappingHospiceExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Overlapping Hospice");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHASuspenseSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","Type of Bill.","The date the claim was submitted to Medicare.",
				"The date the patient was originally admitted.","The claim start date.","The claim through date.","The claim Status and Location.",
		"The reimbursement amount posted by Medicare on the claim."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Suspense");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'SUSPENSE REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "SUSPENSE REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"SUSPENSE REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHASuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Suspense");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAErrorSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"The claim start date.","The claim through date.","Type of Bill.","The Status and Location of the last claim in the episode.",
				"The claim reason code.","The reimbursement amount posted by Medicare on the claim."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Error");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'ERROR REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "ERROR REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"ERROR REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Error");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHACancelledSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"The claim start date.","The claim through date.","Type of Bill.","The claim Status and Location.","The date the claim was cancelled.",
				"The claim reason code.","The reimbursement amount posted by Medicare on the claim."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Cancelled");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'CANCELLED REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "CANCELLED REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"CANCELLED REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHACancelledExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Cancelled");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaidSortHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"The claim start date.","The claim through date.","Type of Bill.","The claim Status and Location.","The date the claim was paid (or processed).",
				"The reimbursement amount posted by Medicare on the claim.","The claim billed amount."};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Cancelled");
		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 2))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'PAID REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "PAID REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"PAID REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyNonHHAPaidExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Paid");
		String[] expected = { "Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}

		return failurecount == 0 ? true : false;
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
