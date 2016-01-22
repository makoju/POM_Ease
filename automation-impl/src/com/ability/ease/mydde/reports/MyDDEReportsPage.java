package com.ability.ease.mydde.reports;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

    public boolean verifySummaryReportHeaderandHelpText(Map<String, String> mapAttrValues) throws Exception{
    	boolean istimeframedaterange=false;
    	int failurecount=0;
    	String fromdate,todate;
    	fromdate=todate=null;
    	
    	//navigation part
    	navigateToPage();
    	UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		
		//Verification part
		//Verify the agency in Summary Report Header
		String[] actual=null;
		String[] expected={"Critical","Errors","Normal"};
		String[] expectedheaders = {"The episode status.","The number of episodes in this category.","The amount Medicare has already paid.",
											"The total amount at risk (already paid or prospective).", "The remaining amount of money expected from Medicare."};	
		int i=0;
		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'EASE SUMMARY REPORT')]"));
		Attribute agencyattr = reportshelper.getAttribute(lsAttributes, "agency");
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes, "Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")){
			istimeframedaterange = true;
			String[] dates=timeframe.split(":");
			fromdate=dates[0];
			todate=dates[1];
			
			fromdate = fromdate.substring(fromdate.indexOf("(")+1,fromdate.indexOf(")"));
			todate =  todate.substring(todate.indexOf("(")+1,todate.indexOf(")")); 
		}
		
		report.report("comparing summary report header value Actual:  "+reportText);
		if (agencyattr!=null){
		   if (istimeframedaterange && !Verify.StringEquals(reportText, "EASE SUMMARY REPORT FROM "+fromdate+" TO "+todate+", FOR AGENCY "+agencyattr.getValue()))
			   failurecount++;
		   else
			 if(!Verify.StringMatches(reportText, "EASE SUMMARY REPORT FROM * TO *, FOR AGENCY "+agencyattr.getValue()))
				failurecount++;
		}
		//verify the link sections in report
		List<WebElement> lsel = driver.findElements(By.cssSelector(".formgridheader"));
		actual=new String[lsel.size()];
		for(WebElement we:lsel){
			actual[i++] = new String(getElementText(we));
		}
		
		//Verify the Report links of each section
		if(!Verify.verifyArrayofStrings(actual,expected,true))
			failurecount++;
		//Verify Critical reports section
		if(!reportshelper.validateReportLinkSectionswithEpisodes("Critical"))
			failurecount++;
		//Verify Errors reports section
		if(!reportshelper.validateReportLinkSectionswithEpisodes("Errors"))
			failurecount++;
		//Verify Normal reports section
		if(!reportshelper.validateReportLinkSectionswithEpisodes("Normal"))
			failurecount++;
		
		//verify the table header tool tips
		String[] actualheaderscritical = reportshelper.getReportLinkSectionsTableHeaderToolTips("Critical");
		if(!Verify.verifyArrayofStrings(actualheaderscritical, expectedheaders, true))
			failurecount++;

		String[] actualheadersErros = reportshelper.getReportLinkSectionsTableHeaderToolTips("Errors");
		if(!Verify.verifyArrayofStrings(actualheadersErros, expectedheaders, true))
		  failurecount++;
		
		String[] actualheadersNormal = reportshelper.getReportLinkSectionsTableHeaderToolTips("Normal");
		if(!Verify.verifyArrayofStrings(actualheadersNormal, expectedheaders, true))
		  failurecount++;
		
		report.report("Total number of failures is: "+failurecount, ReportAttribute.BOLD);
		
		return failurecount==0?true:false;
	}
    
	public boolean verifySummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0;
		//navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		
		//verification part
		String[] expected = {"Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF","Save complete report to Excel"}, actual;
		int i=0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for(WebElement we:lsexportlinks){
			actual[i++] = we.getText(); 
		}
		
		if(!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		
		for(i=0;i<actual.length;i++){
			reportshelper.navigateExportlink(actual[i]);
			//To DO - Need to validate whether respective link is opened or not 
		}
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyChangesReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange=false;
		int failurecount=0;
		String fromdate,todate;
		fromdate=todate=null;
		String[] expectedheaders = {"Allows you to edit the claim.", "The Change column shows the change to a claim status or an eligibility status which caused it to be included in the report.",
				"The ELG column shows if there is a problem with patient eligibility. A Green Check mark means that there were no problems found with eligibility."," A Red X means that there was an eligibility problem or that EASE was not able to find the eligibility for the patient. IMPORTANT: Hover over any Red X to see the detailed information regarding the eligibility problem.",
				"The claim Status and Location.", "The Patient HIC number.", "The name of the patient.", "Type of claim.", "Type of Bill.", "The claim reason code.", "The claim start date.", "The claim through date.", "The dollar value of the claim.", "The last time the claim was updated in Ease from DDE."};
		
		navigateToPage();
    	UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		
	    safeJavaScriptClick("Changes");
		
		//Verification part
		//Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Change",2))
		 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "S/Loc",4))
			 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC",5))
			 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "Patient",6))
			 failurecount++;
		
		Attribute agencyattr = reportshelper.getAttribute(lsAttributes, "agency");
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes, "Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")){
			istimeframedaterange = true;
			String[] dates=timeframe.split(":");
			fromdate=dates[0];
			todate=dates[1];
			
			fromdate = fromdate.substring(fromdate.indexOf("(")+1,fromdate.indexOf(")"));
			todate =  todate.substring(todate.indexOf("(")+1,todate.indexOf(")")); 
		}
		
		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'CHANGES REPORT')]"));
		
		report.report("comparing summary report header value Actual:  "+reportText);
		if (agencyattr!=null){
		   if (istimeframedaterange && !Verify.StringEquals(reportText, "CHANGES REPORT FROM "+fromdate+" TO "+todate+", FOR AGENCY "+agencyattr.getValue()))
			   failurecount++;
		   else
			 if(!Verify.StringMatches(reportText, "CHANGES REPORT FROM * TO *, FOR AGENCY "+agencyattr.getValue()))
				failurecount++;
		}
		
		//verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if(!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders, true))
			failurecount++;
		
		report.report("Total number of failures is: "+failurecount, ReportAttribute.BOLD);
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyChangesReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0;
		//navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		
		safeJavaScriptClick("Changes");
		
		//verification part
		String[] expected = {"Save Changes report to PDF","Save Changes report to Excel","Save complete report to PDF","Save complete report to Excel"}, actual;
		int i=0;
		clickLink("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for(WebElement we:lsexportlinks){
			actual[i++] = we.getText(); 
		}
		
		if(!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		
		for(i=0;i<actual.length;i++){
			reportshelper.navigateExportlink(actual[i]);
			//To DO - Need to validate whether respective link is opened or not 
		}
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyChangesReportLastUpdateColumn(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange=false;
    	String lastupdatedate,fromdate,todate;
    	lastupdatedate=fromdate=todate=null;
    	int failurecount=0;
    	
		//navigation part
		navigateToPage();
    	UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		
		safeJavaScriptClick("Changes");
			
		lastupdatedate = Verify.getTableData("datatable", 1, 13);
		
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes, "Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")){
			istimeframedaterange = true;
			String[] dates=timeframe.split(":");
			fromdate=dates[0];
			todate=dates[1];
			
			fromdate = fromdate.substring(fromdate.indexOf("(")+1,fromdate.indexOf(")"));
			todate =  todate.substring(todate.indexOf("(")+1,todate.indexOf(")")); 
		}
		if(istimeframedaterange){
		  if(!Verify.datewithinDateRange(lastupdatedate, fromdate, todate))
			failurecount++;
		}
		
		else
			return false; //To Do - Need to implement the validation for Overnight and weekly reports
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyHighLevelPaymentSummaryReportSortHeader(Map<String, String> mapAttrValues) throws Exception{
		int failurecount=0;
		// TODO Auto-generated method stub
		//navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("High Lvl Payment Summary");
		
		//Verification part
		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'HIGH LEVEL PAYMENT')]"));
		if(!Verify.StringMatches(reportText.trim(), "HIGH LEVEL PAYMENT SUMMARY REPORT FROM *"))
			failurecount++;
		
		//Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Provider",1))
		 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "Day",3))
			 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "Check #",4))
			 failurecount++;
						
		return failurecount==0?true:false;
	}
	
	public boolean verifyHighLevelPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0,i=0;
		String[] expected = {"Save High Lvl Payment Summary report to PDF","Save High Lvl Payment Summary report to Excel"}, actual;
		//navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("High Lvl Payment Summary");
		
		//verification part
		safeJavaScriptClick("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
			for(WebElement we:lsexportlinks){
				actual[i++] = we.getText(); 
			 }
			if(!Verify.verifyArrayofStrings(actual, expected, true))
				failurecount++;
		
			for(i=0;i<actual.length;i++){
				reportshelper.navigateExportlink(actual[i]);
				//To DO - Need to validate whether respective link is opened or not 
			}
			
		 return failurecount==0?true:false;
	}
	

	public boolean verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(Map<String, String> mapAttrValues) throws Exception{
		int failurecount=0;
		//navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("High Lvl Payment Summary");
		//verification part
		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'HIGH LEVEL PAYMENT')]"));
		if(!Verify.StringMatches(reportText.trim(), "HIGH LEVEL PAYMENT SUMMARY REPORT FROM *"))
			failurecount++;
		
		String totalcheckamount = getElementText(By.id("totalCheckAmount"));
		String totalprojectedamount = getElementText(By.id("totalProjectedAmount"));
		if(!Verify.currencyValidator(totalcheckamount))
			failurecount++;
		if(!Verify.currencyValidator(totalprojectedamount))
			failurecount++;
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyHighLevelPaymentSummaryReportMultiAgencySelectAndExportOptions(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0,i=0;
		String[] actual;
		//navigation part
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		
		safeJavaScriptClick("High Lvl Payment Summary");
		//verification part
		UIAttribute multiselectagency = reportshelper.getAttribute(lsAttributes, "MultiSelectAgency");
		String multiselectagencystr = multiselectagency.getValue();
		int actualcheckedagencies = findElements(By.xpath("//input[@name='multiselect_example' and @checked='checked']")).size();
		
		if(multiselectagencystr.toLowerCase().startsWith("check all")){
			int expectedAgencies = findElements(By.xpath("//input[@name='multiselect_example']")).size();
			if(expectedAgencies!=actualcheckedagencies){
				report.report("There should be "+expectedAgencies+" Agencies Selected. But, Only "+actualcheckedagencies+" were Selected");
				failurecount++;
			}
		}
		else if(multiselectagencystr.toLowerCase().startsWith("uncheck all")){
			
			if(actualcheckedagencies!=0){
				report.report("There should be No Agencies Selected. But, "+actualcheckedagencies+" were Selected");
				failurecount++;
			}
		}
		else{
			int expectedAgencies = multiselectagencystr.split(",").length;
			if(actualcheckedagencies!=expectedAgencies){
				report.report("There should be "+expectedAgencies+" Agencies Selected. But, "+actualcheckedagencies+" were Selected");
				failurecount++;
			}
		}
		
		safeJavaScriptClick("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
	
			for(i=0;i<actual.length;i++){
				reportshelper.navigateExportlink(actual[i]);
				//To DO - Need to validate whether respective link is opened or not 
			}
			
		return failurecount==0?true:false;
	}

	public boolean verifyPaymentSummaryReportSortHeaderHelp(Map<String, String> mapAttrValues) throws Exception {
		boolean istimeframedaterange=false;
		int failurecount=0;
		String fromdate,todate;
		fromdate=todate=null;
		String[] expectedheaders = {"The date of payment.", "The day of week for the payment.", "The check number from Medicare (if available).", 
				"The number of claims paid on this date.", "This is the amount Medicare scheduled to pay.", "This is the amount actually paid on the check.", 
				"This is the amount that is projected to be paid.", "The last time the claim was updated in Ease from DDE."};
		
		navigateToPage();
    	UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		
	    safeJavaScriptClick("Payment Summary");
		
		//Verification part
		//Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Day",2))
		 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "Check #",3))
			 failurecount++;
		
		Attribute agencyattr = reportshelper.getAttribute(lsAttributes, "agency");
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = reportshelper.getAttribute(lsAttributes, "Timeframe");
		String timeframe = timeframeattr.getValue().toLowerCase();
		if (timeframe.contains("fromdate")){
			istimeframedaterange = true;
			String[] dates=timeframe.split(":");
			fromdate=dates[0];
			todate=dates[1];
			
			fromdate = fromdate.substring(fromdate.indexOf("(")+1,fromdate.indexOf(")"));
			todate =  todate.substring(todate.indexOf("(")+1,todate.indexOf(")")); 
		}
		
		String reportText = getElementText(By.xpath("//div[@id='reportarea']//td[contains(text(),'PAYMENT SUMMARY')]"));
		
		report.report("comparing summary report header value Actual:  "+reportText);
		if (agencyattr!=null){
		   if (istimeframedaterange && !Verify.StringEquals(reportText, "PAYMENT SUMMARY REPORT FROM "+fromdate+" TO "+todate+", FOR AGENCY "+agencyattr.getValue()))
			   failurecount++;
		   else
			 if(!Verify.StringMatches(reportText, "PAYMENT SUMMARY REPORT FROM * TO *, FOR AGENCY "+agencyattr.getValue()))
				failurecount++;
		}
		
		//verify the table header tool tips
		String[] actualheadertooltips = reportshelper.getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if(!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders, true))
			failurecount++;
		
		report.report("Total number of failures is: "+failurecount, ReportAttribute.BOLD);
				
		return failurecount==0?true:false;
	}
	
	public boolean verifyPaymentSummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0,i=0;
		String[] expected = {"Save Payment Summary report to PDF","Save Payment Summary report to Excel", "Save complete report to PDF", "Save complete report to Excel"}, actual;
		//navigation part
		navigateToPage();
		reportshelper.fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		safeJavaScriptClick("Payment Summary");
		
		//verification part
		safeJavaScriptClick("Export");
		List<WebElement> lsexportlinks = reportshelper.getAllExportLinks();
		actual = new String[lsexportlinks.size()];
			for(WebElement we:lsexportlinks){
				actual[i++] = we.getText(); 
			 }
			if(!Verify.verifyArrayofStrings(actual, expected, true))
				failurecount++;
		
			for(i=0;i<actual.length;i++){
				reportshelper.navigateExportlink(actual[i]);
				//To DO - Need to validate whether respective link is opened or not 
			}
			
		 return failurecount==0?true:false;
	}
	
	@Override
	public void assertInPage() {
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYDDE, null);
	}

}
