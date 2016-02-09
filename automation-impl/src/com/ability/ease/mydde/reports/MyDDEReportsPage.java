package com.ability.ease.mydde.reports;

//import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.dataStructure.common.easeScreens.UIAttribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class MyDDEReportsPage extends AbstractPageObject {

	String tableheadersxpath = "//table[@id='datatable']//tr[@class='tableheaderblue']/td";
	ReportsHelper reportshelper = new ReportsHelper();

	public boolean verifySummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;

		// navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		// Verification part
		// Verify the agency in Summary Report Header
		String[] actual = null;
		String[] expected = { "Critical", "Errors", "Normal" };
		String[] expectedheaders = { "The episode status.",
				"The number of episodes in this category.",
				"The amount Medicare has already paid.",
				"The total amount at risk (already paid or prospective).",
		"The remaining amount of money expected from Medicare." };
		int i = 0;
		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'EASE SUMMARY REPORT')]"));
		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		report.report("comparing summary report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(
							reportText,
							"EASE SUMMARY REPORT FROM " + fromdate + " TO "
									+ todate + ", FOR AGENCY "
									+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"EASE SUMMARY REPORT FROM.*TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}
		// verify the link sections in report
		List<WebElement> lsel = driver.findElements(By
				.cssSelector(".formgridheader"));
		actual = new String[lsel.size()];
		for (WebElement we : lsel) {
			actual[i++] = new String(getElementText(we));
		}

		// Verify the Report links of each section
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		// Verify Critical reports section
		if (!reportshelper.validateReportLinkSectionswithEpisodes("Critical"))
			failurecount++;
		// Verify Errors reports section
		if (!reportshelper.validateReportLinkSectionswithEpisodes("Errors"))
			failurecount++;
		// Verify Normal reports section
		if (!reportshelper.validateReportLinkSectionswithEpisodes("Normal"))
			failurecount++;

		// verify the table header tool tips
		String[] actualheaderscritical = reportshelper
				.getReportLinkSectionsTableHeaderToolTips("Critical");
		if (!Verify.verifyArrayofStrings(actualheaderscritical,
				expectedheaders, true))
			failurecount++;

		String[] actualheadersErros = reportshelper
				.getReportLinkSectionsTableHeaderToolTips("Errors");
		if (!Verify.verifyArrayofStrings(actualheadersErros, expectedheaders,
				true))
			failurecount++;

		String[] actualheadersNormal = reportshelper
				.getReportLinkSectionsTableHeaderToolTips("Normal");
		if (!Verify.verifyArrayofStrings(actualheadersNormal, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifySummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		
		int failurecount = 0;
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);

		// verification part
		String[] expected = { "Save Summary report to PDF",
				"Save Full Summary report to Excel",
				"Save complete report to PDF", "Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		// To DO - Need to validate whether respective link is opened or not
		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyChangesReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {
				"Allows you to edit the claim.",
				"The Change column shows the change to a claim status or an eligibility status which caused it to be included in the report.",
				"The ELG column shows if there is a problem with patient eligibility. A Green Check mark means that there were no problems found with eligibility. A Red X means that there was an eligibility problem or that EASE was not able to find the eligibility for the patient. IMPORTANT: Hover over any Red X to see the detailed information regarding the eligibility problem.",
				"The claim Status and Location.", "The Patient HIC number.",
				"The name of the patient.", "Type of claim.", "Type of Bill.",
				"The claim reason code.", "The claim start date.",
				"The claim through date.", "The dollar value of the claim.",
		"The last time the claim was updated in Ease from DDE." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Changes");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Change", 2))
			failurecount++;

		if (!Verify.validateTableColumnSortOrder("datatable", "Loc", 4))
			failurecount++;

		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 5))
			failurecount++;

		if (!Verify.validateTableColumnSortOrder("datatable", "Patient", 6))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'CHANGES REPORT')]"));

		report.report("comparing changes report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "CHANGES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"CHANGES REPORT FROM.*TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyChangesReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		
		
		clickLink("Changes");

		// verification part
		String[] expected = { "Save Changes report to PDF","Save Changes report to Excel", "Save complete report to PDF","Save complete report to Excel" }, actual;
		int i = 0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}

		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		// To DO - Need to validate whether respective link is opened or not
		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyChangesReportLastUpdateColumn(
			Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		// navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Changes");

		lastupdatedate = Verify.getTableData("datatable", 1, 13);

		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
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

	public boolean verifyHighLevelPaymentSummaryReportSortHeader(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		// TODO Auto-generated method stub
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("High Lvl Payment Summary");

		// Verification part
		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'HIGH LEVEL PAYMENT')]"));
		if (!Verify.StringMatches(reportText.trim(),
				"HIGH LEVEL PAYMENT SUMMARY REPORT FROM.*"))
			failurecount++;

		// Natural Ascending Sort Verification
		if (!Verify.validateSortOrderForHighLevelPaymentSummary("datatable",
				"Provider", 1))
			failurecount++;

		if (!Verify.validateSortOrderForHighLevelPaymentSummary("datatable",
				"Day", 3))
			failurecount++;

		if (!Verify.validateSortOrderForHighLevelPaymentSummary("datatable",
				"Check", 4))
			failurecount++;

		return failurecount == 0 ? true : false;
	}

	public boolean verifyHighLevelPaymentSummaryReportExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save High Lvl Payment Summary report to PDF",
		"Save High Lvl Payment Summary report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("High Lvl Payment Summary");

		// verification part
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		// To DO - Need to validate whether respective link is opened or not
		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);

		// verification part
		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'HIGH LEVEL PAYMENT')]"));
		if (!Verify.StringMatches(reportText.trim(),
				"HIGH LEVEL PAYMENT SUMMARY REPORT FROM.*"))
			failurecount++;

		String totalcheckamount = getElementText(By.id("totalCheckAmount"));
		String totalprojectedamount = getElementText(By
				.id("totalProjectedAmount"));
		if (!Verify.currencyValidator(totalcheckamount))
			failurecount++;
		if (!Verify.currencyValidator(totalprojectedamount))
			failurecount++;

		return failurecount == 0 ? true : false;
	}

	public boolean verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0,actualcheckedagencies=0;
		String[] actual;
		// navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("High Lvl Payment Summary");
		//This is a click on multi-agency select dropdown. Which should be opened for subsequent calls to succeed
		clickButton("ui-multiselect"); 
		// verification part
		UIAttribute multiselectagency = reportshelper.getAttribute(
				lsAttributes, "MultiSelectAgency");
		String multiselectagencystr = multiselectagency.getValue();
		List<WebElement> lscheckedAgencies = findElements(
				By.xpath("//div[contains(@style, 'block')]//input[@name='multiselect_example' and @checked='checked']"));
		if(lscheckedAgencies!=null)
			actualcheckedagencies = lscheckedAgencies.size();
		
		else if(!multiselectagencystr.toLowerCase().startsWith("uncheck all")){
			report.report("actual selected agencies are null", Reporter.WARNING);
			failurecount++;
		}
		
		if (multiselectagencystr.toLowerCase().startsWith("check all")) {
			int expectedAgencies = 0;
			List<WebElement> lsexpectedagencies = findElements(
					By.xpath("//div[contains(@style, 'block')]//input[@name='multiselect_example']"));
			if(lsexpectedagencies!=null)
				expectedAgencies = lsexpectedagencies.size();
			else{
				report.report("expected agencies are null", Reporter.WARNING);
				failurecount++;
			}
			
			if (expectedAgencies != actualcheckedagencies) {
				report.report("There should be " + expectedAgencies
						+ " Agencies Selected. But, Only "
						+ actualcheckedagencies + " were Selected");
				failurecount++;
			}
		} else if (multiselectagencystr.toLowerCase().startsWith("uncheck all")) {

			if (actualcheckedagencies != 0) {
				report.report("There should be No Agencies Selected. But, "
						+ actualcheckedagencies + " were Selected");
				failurecount++;
			}
		} else {
			int expectedAgencies = multiselectagencystr.split(",").length;
			if (actualcheckedagencies != expectedAgencies) {
				report.report("There should be " + expectedAgencies
						+ " Agencies Selected. But, " + actualcheckedagencies
						+ " were Selected");
				failurecount++;
			}
		}

		clickLink("Export");
		
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		// To DO - Need to validate whether respective link is opened or not
		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
		}
		 */
		return failurecount == 0 ? true : false;
	}

	public boolean verifyPaymentSummaryReportSortHeaderHelp(
			Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = { "The date of payment.",
				"The day of week for the payment.",
				"The check number from Medicare (if available).",
				"The number of claims paid on this date.",
				"This is the amount Medicare scheduled to pay.",
				"This is the amount actually paid on the check.",
				"This is the amount that is projected to be paid.",
		"The last time the claim was updated in Ease from DDE." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Payment Summary");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Day", 2))
			failurecount++;

		if (!Verify.validateTableColumnSortOrder("datatable", "Check", 3))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'PAYMENT SUMMARY')]"));

		report.report("comparing payment summary report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(
							reportText,
							"PAYMENT SUMMARY REPORT FROM " + fromdate + " TO "
									+ todate + ", FOR AGENCY "
									+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,
					"PAYMENT SUMMARY REPORT FROM.*TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF",
				"Save Payment Summary report to Excel",
				"Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("Payment Summary");

		// verification part
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		// To DO - Need to validate whether respective link is opened or not
		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyPaymentSummaryReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		// navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Payment Summary");

		lastupdatedate = Verify.getTableData("datatable", 1, 8);
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}
		if (istimeframedaterange) {
			if (lastupdatedate != " ") {
				if (!Verify.datewithinDateRange(lastupdatedate, fromdate,
						todate))
					failurecount++;
			}
		} else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifyPaymentReportSortHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = { "The date of payment.",
				"The Patient HIC number.", "The name of the patient.",
				"Type of claim.", "Type of Bill.",
				"The date the claim was submitted to Medicare.",
				"The claim start date.", "The claim through date.",
				"The dollar value of the claim.",
		"The last time the claim was updated in Ease from DDE." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Payment");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 2))
			failurecount++;

		if (!Verify.validateTableColumnSortOrder("datatable", "Type", 4))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'PAYMENT')]"));

		report.report("comparing payment report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "PAYMENT REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"PAYMENT REPORT FROM.*TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyPaymentReportExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception { // still
		// pending...not
		// able to
		// execute
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment report to PDF",
				"Save Payment report to Excel", "Save complete report to PDF",
		"Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("Payment");

		// verification part
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		if (lsexportlinks == null) {
			((JavascriptExecutor) driver)
			.executeScript("$('#reportExport').click();");
			Thread.sleep(5000);
			lsexportlinks = reportshelper.getAllExportLinks();
		}
		actual = new String[lsexportlinks.size()];

		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		/*for (i = 0; i < actual.length; i++) {
			//reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyPaymentReportLastUpdateColumn(
			Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		// navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Payment");

		lastupdatedate = Verify.getTableData("datatable", 1, 10);
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}
		if (istimeframedaterange) {
			if (lastupdatedate != " ") {
				if (!Verify.datewithinDateRange(lastupdatedate, fromdate,
						todate))
					failurecount++;
			}
		} else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifySubmittedClaimsHeaderandHelpText(
			Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {
				"The month of certification.",
				"The number of episodes in this month.",
				"The number of new patients admissions.",
				"The number of existing patients’ recertifications.",
				"The number of episodes that have been cancelled. An episode is considered cancelled if it was not fully paid and there has been no billing activity for more than 90 days.",
				"The number of episodes where a RAP was submitted.",
				"The number of episodes where a RAP has been paid.",
				"The number of episodes where a RAP has not been paid.",
				"The number of episodes where a RAP has been cancelled and not resubmitted.",
				"The average number of days for the first RAP to be submitted after episodes had begun.",
				"The number of episodes where a Final was submitted.",
				"The number of episodes where a Final has been paid.",
				"The number of episodes where a Final has not been paid.",
				"The number of episodes where a Final has been cancelled and not resubmitted.",
		"The average number of days for the first Final to be submitted after the episodes has ended." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Submitted Claims");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Admits", 3))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'SUBMITTED CLAIMS')]"));

		report.report("comparing submitted claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(
							reportText,
							"SUBMITTED CLAIMS REPORT FROM " + fromdate + " TO "
									+ todate + ", FOR AGENCY "
									+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,
					"SUBMITTED CLAIMS REPORT FROM.*.TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifySubmittedClaimsExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF",
				"Save Payment Summary report to Excel",
				"Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("Submitted Claims");

		// verification part
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		WebElement element = (WebElement) ((JavascriptExecutor) driver).executeScript("$('ul#reportExportMenu li').each(function(){$(this).text();});");
		if (lsexportlinks == null) {
			Thread.sleep(5000);
			WebElement we = driver
					.findElement(By
							.xpath("html/body/table[1]/tbody/tr[3]/td[2]/div[1]/ul/li[8]/a"));
			Actions action = new Actions(driver);
			action.moveToElement(we).click().build().perform();
			lsexportlinks = reportshelper.getAllExportLinks();
		}
		
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}*/
		return failurecount == 0 ? true : false;
	}

	public boolean verifyUnpaidClaimsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {
				"Allows you to edit the claim.",
				"The status of the claim.",
				"The claim Status and Location.",
				"The claim reason code.",
				"The Patient HIC number.",
				"The name of the patient.",
				"Your agency’s internal ID for this patient.",
				"Type of Bill.",
				"Type of claim.",
				"The date the patient was originally admitted.",
				"The claim start date.",
				"The claim through date.",
				"The date the claim was submitted to Medicare.",
				"The number of days since the claim was submitted to Medicare.",
				"The amount charged for this claim.",
		"The reimbursement amount posted by Medicare on the claim." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Unpaid Claims");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Reason", 4))
			failurecount++;
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 5))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(
							reportText,
							"UNPAID CLAIMS REPORT FROM " + fromdate + " TO "
									+ todate + ", FOR AGENCY "
									+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,
					"UNPAID CLAIMS REPORT FROM.*TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyUnpaidClaimsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF",
				"Save Payment Summary report to Excel",
				"Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("Unpaid Claims");

		// verification part
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		/*for (i = 0; i < actual.length; i++) {
			reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyActiveEpisodesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception { // no data,
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = { "The Patient HIC number.", "The name of the patient.", "The date the claim was submitted to Medicare.",
				"The date the patient was originally admitted.", "The claim start date.", "The claim through date.", "The episode Seq. number.",
				"Your agencyâ€™s internal ID for this patient.", "Type of Bill.", "Type of claim.", "The claim Status and Location.", "The patient discharge status.",
				"The date the claim was paid (or processed).", "The date the claim was cancelled.", "The claim reason code.", "The amount charged for this claim.",
				"The reimbursement amount posted by Medicare on the claim."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Active Episodes");

		// Verification part
		// Natural Ascending Sort Verification
		
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "Episode", 7))
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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'ACTIVE EPISODES REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText,"ACTIVE EPISODES REPORT FROM " + fromdate + " TO "+ todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"ACTIVE EPISODES REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyActiveEpisodesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Active Episodes");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyEpisodesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = { "Allows you to edit the claim.",
				"The Patient HIC number.", "The name of the patient.",
				"The date the patient was originally admitted.",
				"Start of Episode date.",
				"End of Episode date, the date the episode ended.",
				"The Status and Location of the last claim in the episode.",
				"The reimbursement amount posted by Medicare on the claim.",
				"The total episode value.",
		"The remaining amount of money expected from Medicare." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Episodes");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;
		if (!Verify.validateTableColumnSortOrder("datatable", "Loc", 7))
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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "EPISODES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;

			else if (!Verify.StringMatches(reportText,
					"ACTIVE EPISODES REPORT FROM.*.TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyEpisodesExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF",
				"Save Payment Summary report to Excel",
				"Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("Active Episodes");

		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		/*for (i = 0; i < actual.length; i++) {
			//reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyClaimsReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {
				"Allows you to edit the claim.",
				"The Patient HIC number.",
				"The name of the patient.",
				"The date the claim was submitted to Medicare.",
				"The date the patient was originally admitted.",
				"The episode sequence number in the current patient care begins with the first certification, regardless of which agency the certification started with.",
				"Type of Bill.", "The claim start date.",
				"The claim through date.", "The claim Status and Location.",
				"The date the claim was paid (or processed).",
				"The date the claim was cancelled.",
				"The reimbursement amount posted by Medicare on the claim.",
		"The claim reason code." };

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		clickLink("Episodes");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 2))
			failurecount++;
		if (!Verify.validateTableColumnSortOrder("datatable", "Loc", 10))
			failurecount++;

		Attribute agencyattr = reportshelper.getAttribute(lsAttributes,
				"agency");
		// To Do - Need to get the From and Todate from Timeframe attributes
		// value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes,
				"Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")) {
			istimeframedaterange = true;
			String[] dates = timeframe.split(":");
			fromdate = dates[0];
			todate = dates[1];

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "EPISODES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"EPISODES REPORT FROM.*TO.*FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,
				ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyClaimsReportExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF",
				"Save Payment Summary report to Excel",
				"Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		clickLink("Active Episodes");

		// verification part
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		if (!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;

		/*for (i = 0; i < actual.length; i++) {
			//reportshelper.navigateExportlink(actual[i]);
			// To DO - Need to validate whether respective link is opened or not
		}*/

		return failurecount == 0 ? true : false;
	}

	public boolean verifyStuckInSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"The claim start date.","The claim through date.","The date the claim was submitted to Medicare.","The number of days the claim is stuck in suspense.",
				"The claim Status and Location.","Type of Bill.","The claim reason code.","The total episode value."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Stuck In Suspense");

		// Verification part
		// Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
			failurecount++;
		if (!Verify.validateTableColumnSortOrder("datatable", "Loc", 8))
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
			if (istimeframedaterange && !Verify.StringEquals(reportText, "EPISODES REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"EPISODES REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyStuckInSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Stuck In Suspense");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyRAPSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the claim was submitted to Medicare.",
				"The date the patient was originally admitted.","Start of Episode date.","The total episode value."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("RAP Suspense");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "EPISODES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"EPISODES REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyRAPSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("RAP Suspense");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyRAPPaidHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The date the claim was paid (or processed).","The reimbursement amount posted by Medicare on the claim.",
				"The total episode value.","The remaining amount of money expected from Medicare."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("RAP Paid");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "EPISODES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"EPISODES REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyRAPPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("RAP Paid");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyRAPErrorHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The claim Status and Location.","The claim reason code.","The total episode value."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath()
				+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("RAP Error");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "EPISODES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"EPISODES REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyRAPErrorExportPDFExcel(
			Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("RAP Error");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyRAPCancelledHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The claim Status and Location.","The claim reason code.","The total episode value."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("RAP Error");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'UNPAID CLAIMS REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "EPISODES REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"EPISODES REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyRAPCancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("RAP Suspense");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyFinalSuspenseHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the claim was submitted to Medicare.",
				"The date the patient was originally admitted.","Start of Episode date.","The reimbursement amount posted by Medicare on the claim.","The total episode value.",
		"The remaining amount of money expected from Medicare."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Final Suspense");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'FINAL SUSPENSE REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "FINAL SUSPENSE REPORT FROM"
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"FINAL SUSPENSE REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyFinalSuspenseExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Final Suspense");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyFinalPaidHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The date the claim was paid (or processed).","The reimbursement amount posted by Medicare on the claim."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Final Paid");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'FINAL PAID REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "FINAL PAID REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"FINAL PAID REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyFinalPaidExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Final Paid");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyFinalErrorHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The Status and Location of the last claim in the episode.","The claim reason code.","The reimbursement amount posted by Medicare on the claim.",
				"The total episode value.","The remaining amount of money expected from Medicare."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Final Error");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'FINAL ERROR REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "FINAL ERROR REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"FINAL ERROR REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyFinalErrorExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Final Error");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyFinalCancelledHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"Allows you to edit the claim.","The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The date the claim was cancelled.","The claim reason code.","The reimbursement amount posted by Medicare on the claim.",
				"The total episode value.","The remaining amount of money expected from Medicare."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Final Cancelled");

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

			fromdate = fromdate.substring(fromdate.indexOf("(") + 1,
					fromdate.indexOf(")"));
			todate = todate.substring(todate.indexOf("(") + 1,
					todate.indexOf(")"));
		}

		String reportText = getElementText(By
				.xpath("//div[@id='reportarea']//td[contains(text(),'FINAL CANCELLED REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange
					&& !Verify.StringEquals(reportText, "FINAL CANCELLED REPORT FROM "
							+ fromdate + " TO " + todate + ", FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(
					reportText,
					"FINAL CANCELLED REPORT FROM * TO *, FOR AGENCY "
							+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper
				.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,
				true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyFinalCancelledExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Final Cancelled");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyFinalDueHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The date the RAP claim was submitted to Medicare.","The date a final claim should be submitted to avoid from RAP been cancelled.",
				"The number of days left to submit the Final claim to avoid the RAP being auto-cancelled.","The reimbursement amount posted by Medicare on the claim.",
				"The total episode value.","The remaining amount of money expected from Medicare."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Final Due");

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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'FINAL DUE REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "
				+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "FINAL DUE REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"FINAL DUE REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyFinalDueExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Final Due");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyZ_OnHoldHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.","Start of Episode date.",
				"The date the claim was processed.","The Non Payment Code for the claim.","The reimbursement amount posted by Medicare on the claim."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Z-On Hold");

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

	public boolean verifyZ_OnHoldExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Z-On Hold");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyRAPsAtRiskHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.",
				"Start of Episode date.","The date the RAP claim was submitted to Medicare.","The date the claim was paid (or processed).",
				"The date a final claim should be submitted to avoid from RAP been cancelled.","The number of days left to submit the Final claim to avoid the RAP being auto-cancelled.",
				"The reimbursement amount posted by Medicare on the claim.",
				"The total episode value.","The remaining amount of money expected from Medicare."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("RAPs At Risk");

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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'RAPS AT RISK REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "RAPS AT RISK REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"RAPS AT RISK REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyRAPsAtRiskExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("RAPs At Risk");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyADRHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.","Start of Episode date.",
				"The reimbursement amount posted by Medicare on the claim.","The total episode value.","The number of days left to respond to the ADR.",
				"The date an action is required by the FI in order to resolve the ADR.","Ensure that ADR documentation is mailed by this date to avoid unnecessary auto-denying of your claim.",
				"The code associated with the ADR.","Total Amount Billed for Claim","The last time the claim was updated in Ease from DDE."};

		navigateToPage();
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

	public boolean verifyADRExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("ADR");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyADRLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		String lastupdatedate, fromdate, todate;
		lastupdatedate = fromdate = todate = null;
		int failurecount = 0;

		// navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("ADR");

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
			if (lastupdatedate != " ") {
				if (!Verify.datewithinDateRange(lastupdatedate, fromdate, todate))
					failurecount++;
			}
		} else
			return false; // To Do - Need to implement the validation for
		// Overnight and weekly reports

		return failurecount == 0 ? true : false;
	}

	public boolean verifyEligibilityIssuesHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The patient's Date of Birth.","The patient sex code (M or F).",
				"Whether or not the patient is under your care.","Whether or not the patient is eligible for Medicare Part A.","Whether or not the patient is eligible for Medicare Part B.",
				"Whether or not the patient currently covered by an HMO plan.","Whether or not the patient has an MSP or liability period.","Whether or not another agency is/was treating the patient for the same time period.",
		"Whether or not the patient has been treated by a hospice agency during the episode."};

		navigateToPage();
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

	public boolean verifyEligibilityIssuesExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Eligibility Issues");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyPatientsReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The patient's Date of Birth.","The patient sex code (M or F).",
				"Whether or not the patient is under your care.","Whether or not the patient has an eligibility issue.","Whether or not the patient is eligible for Medicare Part A.",
				"Whether or not the patient is eligible for Medicare Part B.","Whether or not the patient currently covered by an HMO plan.","Whether or not the patient has an MSP or liability period.",
				"Whether or not another is/was agency treating the patient for the same time period.","Whether or not the patient has been treated by a hospice agency during the episode."};

		navigateToPage();
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

	public boolean verifyPatientsReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Patients");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyEligibilityErrorsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.","Start of Episode date.",
				"The eligibility problem description (if available)."};

		navigateToPage();
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

	public boolean verifyEligibilityErrorsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Eligibility Errors");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyHMOPatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The HMO Plan ID.","The HMO Plan option.","The date the patient was originally admitted.",
				"Start of Episode date.","The reimbursement amount posted by Medicare on the claim."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("HMO Patients");

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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'HMO REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "HMO REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"HMO REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyHMOPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("HMO Patients");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyMSPPatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The MSP Insurer name.","The MSP Code option.","The date the patient was originally admitted.",
				"Start of Episode date.","The reimbursement amount posted by Medicare on the claim."};

		navigateToPage();
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

	public boolean verifyMSPPatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("MSP Patients");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyOtherHHA1stHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.","Start of Episode date.",
				"The reimbursement amount posted by Medicare on the claim.","The other agency’s intermediary code.","The other agency’s provider ID.",
				"The other agency’s Start of Episode. If this is date is earlier than your agency’s start of episode, your episode is at risk.","The other agency’s End of Episode.",
				"The other agency last billable action date. This can help you determine if the other agency should have adjusted their End of Episode to an earlier date when the patient switched to your care."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Other HHA 1st");

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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'OTHER HHA 1ST REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "OTHER HHA 1ST REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"OTHER HHA 1ST REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOtherHHA1stExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Other HHA 1st");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyOtherHHA2ndHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.","Start of Episode date.",
				"The reimbursement amount posted by Medicare on the claim.","The other agency’s intermediary code.","The other agency’s provider ID.",
				"The other agency’s Start of Episode. If this is date is earlier than your agency’s start of episode, your episode is at risk.","The other agency’s End of Episode.",
				"The other agency last billable action date. This can help you determine if the other agency should have adjusted their End of Episode to an earlier date when the patient switched to your care."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Other HHA 2nd");

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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'OTHER HHA 2ND REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "OTHER HHA 2ND REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"OTHER HHA 2ND REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyOtherHHA2ndExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Other HHA 2nd");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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

	public boolean verifyHospicePatientsHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange = false;
		int failurecount = 0;
		String fromdate, todate;
		fromdate = todate = null;
		String[] expectedheaders = {"The Patient HIC number.","The name of the patient.","The date the patient was originally admitted.","Start of Episode date.",
				"The reimbursement amount posted by Medicare on the claim.","The Hospice provider ID.","The Hospice start of care date.","The hospice end of care date."};

		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		safeJavaScriptClick("Hospice Patients");

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

		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'HOSPICE REPORT')]"));

		report.report("comparing unpaid claims report header value Actual:  "+ reportText);
		if (agencyattr != null) {
			if (istimeframedaterange && !Verify.StringEquals(reportText, "HOSPICE REPORT FROM "+ fromdate + " TO " + todate + ", FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
			else if (!Verify.StringMatches(reportText,"HOSPICE REPORT FROM * TO *, FOR AGENCY "+ agencyattr.getValue()))
				failurecount++;
		}

		// verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if (!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders,true))
			failurecount++;

		report.report("Total number of failures is: " + failurecount,ReportAttribute.BOLD);

		return failurecount == 0 ? true : false;
	}

	public boolean verifyHospicePatientsExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount = 0, i = 0;
		String[] expected = { "Save Payment Summary report to PDF","Save Payment Summary report to Excel","Save complete report to PDF", "Save complete report to Excel" }, actual;
		// navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Hospice Patients");

		// verification part
		// safeJavaScriptClick("Export");
		clickOnElement(ByLocator.xpath, "//a[@id='reportExport']", 5);

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
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYDDE, null);
	}
}
