package com.ability.ease.mydde;

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
		Attribute agencyattr = getAttribute(lsAttributes, "agency");
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = getAttribute(lsAttributes, "Timeframe");
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
		if(!validateReportLinkSectionswithEpisodes("Critical"))
			failurecount++;
		//Verify Errors reports section
		if(!validateReportLinkSectionswithEpisodes("Errors"))
			failurecount++;
		//Verify Normal reports section
		if(!validateReportLinkSectionswithEpisodes("Normal"))
			failurecount++;
		
		//verify the table header tool tips
		String[] actualheaderscritical = getReportLinkSectionsTableHeaderToolTips("Critical");
		if(!Verify.verifyArrayofStrings(actualheaderscritical, expectedheaders, true))
			failurecount++;

		String[] actualheadersErros = getReportLinkSectionsTableHeaderToolTips("Errors");
		if(!Verify.verifyArrayofStrings(actualheadersErros, expectedheaders, true))
		  failurecount++;
		
		String[] actualheadersNormal = getReportLinkSectionsTableHeaderToolTips("Normal");
		if(!Verify.verifyArrayofStrings(actualheadersNormal, expectedheaders, true))
		  failurecount++;
		
		report.report("Total number of failures is: "+failurecount, ReportAttribute.BOLD);
		
		return failurecount==0?true:false;
	}
    
	public boolean verifySummaryReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0;
		//navigation part
		navigateToPage();
		fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		
		//verification part
		String[] expected = {"Save Summary report to PDF","Save Full Summary report to Excel","Save complete report to PDF","Save complete report to Excel"}, actual;
		int i=0;
		clickLink("Export");
		List<WebElement> lsexportlinks = getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for(WebElement we:lsexportlinks){
			actual[i++] = we.getText(); 
		}
		
		if(!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		
		for(i=0;i<actual.length;i++){
			navigateExportlink(actual[i]);
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
		
	    WebElement changeselement = waitForElementVisibility(By.linkText("Changes"));
		safeJavaScriptClick(changeselement);
		
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
		
		Attribute agencyattr = getAttribute(lsAttributes, "agency");
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = getAttribute(lsAttributes, "Timeframe");
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
		String[] actualheadertooltips = getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if(!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders, true))
			failurecount++;
		
		report.report("Total number of failures is: "+failurecount, ReportAttribute.BOLD);
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyChangesReportExportPDFExcel(Map<String, String> mapAttrValues) throws Exception {
		int failurecount=0;
		//navigation part
		navigateToPage();
		fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		
	    WebElement changeselement = waitForElementVisibility(By.linkText("Changes"));
		safeJavaScriptClick(changeselement);
		
		//verification part
		String[] expected = {"Save Changes report to PDF","Save Changes report to Excel","Save complete report to PDF","Save complete report to Excel"}, actual;
		int i=0;
		clickLink("Export");
		List<WebElement> lsexportlinks = getAllExportLinks();
		actual = new String[lsexportlinks.size()];
		for(WebElement we:lsexportlinks){
			actual[i++] = we.getText(); 
		}
		
		if(!Verify.verifyArrayofStrings(actual, expected, true))
			failurecount++;
		
		for(i=0;i<actual.length;i++){
			navigateExportlink(actual[i]);
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
		
	    WebElement changeselement = waitForElementVisibility(By.linkText("Changes"));
		safeJavaScriptClick(changeselement);
			
		lastupdatedate = Verify.getTableData("datatable", 1, 13);
		
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = getAttribute(lsAttributes, "Timeframe");
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
		fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		WebElement HighLvlPaymentSummary = waitForElementVisibility(By.linkText("High Lvl Payment Summary"));
		safeJavaScriptClick(HighLvlPaymentSummary);
		
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
		fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		WebElement HighLvlPaymentSummary = waitForElementVisibility(By.linkText("High Lvl Payment Summary"));
		safeJavaScriptClick(HighLvlPaymentSummary);
		
		//verification part
		WebElement export = waitForElementVisibility(By.linkText("Export"));
		safeJavaScriptClick(export);
		List<WebElement> lsexportlinks = getAllExportLinks();
		actual = new String[lsexportlinks.size()];
			for(WebElement we:lsexportlinks){
				actual[i++] = we.getText(); 
			 }
			if(!Verify.verifyArrayofStrings(actual, expected, true))
				failurecount++;
		
			for(i=0;i<actual.length;i++){
				navigateExportlink(actual[i]);
				//To DO - Need to validate whether respective link is opened or not 
			}
			
		 return failurecount==0?true:false;
	}
	

	public boolean verifyHighLevelPaymentSummaryReportCheckAndProjectedAmount(Map<String, String> mapAttrValues) throws Exception{
		int failurecount=0;
		//navigation part
		navigateToPage();
		fillScreen(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		WebElement HighLvlPaymentSummary = waitForElementVisibility(By.linkText("High Lvl Payment Summary"));
		safeJavaScriptClick(HighLvlPaymentSummary);
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
		
		WebElement HighLvlPaymentSummary = waitForElementVisibility(By.linkText("High Lvl Payment Summary"));
		safeJavaScriptClick(HighLvlPaymentSummary);
		//verification part
		UIAttribute multiselectagency = getAttribute(lsAttributes, "MultiSelectAgency");
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
		
		WebElement export = waitForElementVisibility(By.linkText("Export"));
		safeJavaScriptClick(export);
		List<WebElement> lsexportlinks = getAllExportLinks();
		actual = new String[lsexportlinks.size()];
	
			for(i=0;i<actual.length;i++){
				navigateExportlink(actual[i]);
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
		
	    WebElement paymentsummaryelement = waitForElementVisibility(By.linkText("Payment Summary"));
		safeJavaScriptClick(paymentsummaryelement);
		
		//Verification part
		//Natural Ascending Sort Verification
		if (!Verify.validateTableColumnSortOrder("datatable", "Day",2))
		 failurecount++;
		
		if (!Verify.validateTableColumnSortOrder("datatable", "Check #",3))
			 failurecount++;
		
		Attribute agencyattr = getAttribute(lsAttributes, "agency");
		//To Do - Need to get the From and Todate from Timeframe attributes value
		Attribute timeframeattr = getAttribute(lsAttributes, "Timeframe");
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
		String[] actualheadertooltips = getReportLinkSectionsTableHeaderToolTips(tableheadersxpath);
		if(!Verify.verifyArrayofStrings(actualheadertooltips, expectedheaders, true))
			failurecount++;
		
		report.report("Total number of failures is: "+failurecount, ReportAttribute.BOLD);
				
		return failurecount==0?true:false;
	}
	
   //Begin: Helper Methods 
   private void navigateExportlink(String linkText) throws Exception {
	   WebElement we = waitForElementVisibility(By.partialLinkText(linkText));
	   if(!we.isEnabled() && !we.isDisplayed()){
		   clickLink("Export");
		   we = waitForElementVisibility(By.partialLinkText(linkText));
	   }   
	}
   
 
   public boolean validateReportLinkSectionswithEpisodes(String reportlinksection) throws Exception{
    		//Verify Critical reports section
    		List<WebElement> Allreportlinks = getAllReportLinksofSection(reportlinksection);
    		String[] reportlinks = new String[Allreportlinks.size()];
    		int row=1,i=0;
    		boolean isepisodesmatch = true;
    		/* Store the link text of each element to use later*/
    		for(WebElement we:Allreportlinks){
    				reportlinks[i++] = we.getText();
    		}
    		/* Iterate over each link to navigate and check the data*/
    		for(int j=0;j<Allreportlinks.size();j++)
    		{
    				WebElement reportlinkelement = waitForElementVisibility(By.linkText(reportlinks[j]));
    		  		int episodes = Integer.parseInt(Verify.getTableData(reportlinksection, ++row, 2));
    		  	  if(episodes>0){    		  		
    				String reportlink = reportlinkelement.getText();
    				safeJavaScriptClick(reportlinkelement);

    				  int rowcount = Verify.getTotalTableRows("datatable");
    				  report.report("Validating episodes count of Report link"+reportlink);
    				  report.report("Expected:" +episodes+ " Actual:"+rowcount);
    				   if(episodes != rowcount){
    					 isepisodesmatch = false;
    					 report.report("episodes count doens't match with the episodes displayed in report table: "+reportlink+" Expected:" +episodes+ " Actual:"+rowcount, Reporter.WARNING);
    				   }

    				WebElement summaryelement = waitForElementVisibility(By.linkText("Summary"));
    				safeJavaScriptClick(summaryelement);
     			  }
    		 }
    		return isepisodesmatch;
    }
   
    public void clickTableHeaderElement(int columnnumber){
    	List<WebElement> lstableheaders = getReportTableHeaders("datatable");
		WebElement columnelement = lstableheaders.get(columnnumber);
		if(columnelement!=null)
			columnelement.click();
		else{
			report.report("No element found with the given Column Number: "+columnnumber, Reporter.WARNING);
			return;
		}
	 }
   
   //Get the ToolTips of Tables Display under Critical, Error, Normal Sections of Summary Report 
   public String[] getReportLinkSectionsTableHeaderToolTips(String reportlinksection) throws Exception{
	   String ReportlinksXpath = "//span[text()='"+reportlinksection+"']/following-sibling::table//tr[@class='tableheaderblue']/td";
	   return getTableHeaderToolTips(ReportlinksXpath);
   }
   
   public String[] getTableHeaderToolTips(String tableheadersxpath)
   {
	   int i=0;
	   waitForElementVisibility(By.xpath(tableheadersxpath));
	   List<WebElement> lsHeaders = driver.findElements(By.xpath(tableheadersxpath));
	   String[] headertooltips = new String[lsHeaders.size()];
	   for(WebElement we: lsHeaders){
	     String tooltiprawtext = we.getAttribute("onmouseover");
	       if(tooltiprawtext !=null || tooltiprawtext.trim()!=""){
		      headertooltips[i++] = tooltiprawtext.substring(tooltiprawtext.indexOf("\"")+1, tooltiprawtext.lastIndexOf("\""));
	       }
	   }
	   return headertooltips;
   }

	private Attribute getAttribute(List<Attribute> lsAttributes, String attrdisplayname){
		for(Attribute attr:lsAttributes){
			if (attr.getDisplayName().equalsIgnoreCase(attrdisplayname))
				return attr;
		}
		return null;
    }
    
	public void fillScreen(String filename, Map<String,String> mapAttrValues) throws Exception{
		//navigation part
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(filename, mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
	}
	
	//Use this method to get the report links displayed under each report section like, Critical, Errors and Normal  
	public List<WebElement> getAllReportLinksofSection(String reportsection) throws Exception{
		String Reportlinksxpath = "//span[text()='"+reportsection+"']/following-sibling::table//td/a";
		waitForTextVisibility(ByLocator.xpath,"//span[text()='"+reportsection+"']" , reportsection);
		return driver.findElements(By.xpath(Reportlinksxpath));
	}
	
	public List<WebElement> getAllExportLinks(){
		String exportlinksxpath = "//ul[@id='reportExportMenu']//li/a";
		return findElements(By.xpath(exportlinksxpath));
	}
	
	public List<WebElement> getReportTableHeaders(String tableidentifier){
		String tableheaderxpath = "//table[@id='"+tableidentifier+"']/thead/tr/td";
		return findElements(By.xpath(tableheaderxpath));
	}
	
	@Override
	public void assertInPage() {
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYDDE, null);
	}

}
