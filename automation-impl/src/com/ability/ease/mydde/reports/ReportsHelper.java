package com.ability.ease.mydde.reports;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ReportsHelper extends AbstractPageObject{


	//ReportsHelper reportshelper = new ReportsHelper();

	//Begin: Helper Methods 
	public void navigateExportlink(String linkText) throws Exception {
		WebElement we = waitForElementVisibility(By.partialLinkText(linkText));
		if(!we.isEnabled() && !we.isDisplayed()){
			clickLink("Export");
			we = waitForElementVisibility(By.partialLinkText(linkText));
		}
		we.click();
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
			if(tooltiprawtext !=null && tooltiprawtext.trim()!=""){
				headertooltips[i++] = tooltiprawtext.substring(tooltiprawtext.indexOf("\"")+1, tooltiprawtext.lastIndexOf("\""));
			}
		}
		return headertooltips;
	}

	public Attribute getAttribute(List<Attribute> lsAttributes, String attrdisplayname){
		for(Attribute attr:lsAttributes){
			if (attr.getDisplayName().equalsIgnoreCase(attrdisplayname))
				return attr;
		}
		return null;
	}

	public void fillScreen(String filename, Map<String,String> mapAttrValues) throws Exception{
		//navigation part
		if(mapAttrValues.size()>0){
			UIAttributeXMLParser parser = new UIAttributeXMLParser();
			List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(filename, mapAttrValues);
			UIActions mydde = new UIActions();
			mydde.fillScreenAttributes(lsAttributes);
		}
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

	public List<WebElement> getAllSubMenuLinks(String mainMenuID){
		String exportlinksxpath = "//ul[@id='"+mainMenuID+"']//li/a";
		return findElements(By.xpath(exportlinksxpath));
	}

	public List<WebElement> getReportTableHeaders(String tableidentifier){
		WebElement table = waitForElementVisibility(By.xpath("//table[@id='"+tableidentifier+"']"));
		if(table != null){
			String tableheaderxpath = "//table[@id='"+tableidentifier+"']/thead/tr/td";
			return findElements(By.xpath(tableheaderxpath));
		}
		else{
			report.report("There are no records under this.",Reporter.WARNING);
			return null;
		}
	}
	
	public String[] verifySubMenuLinks(String[] expected, String mainMenuLinkText, String mainMenuID) throws Exception{
		String[] actual;
		int i = 0;
		Thread.sleep(3000);
		moveToElement(mainMenuLinkText);
		List<WebElement> lsexportlinks = getAllSubMenuLinks(mainMenuID);
		actual = new String[lsexportlinks.size()];
		for (WebElement we : lsexportlinks) {
			actual[i++] = we.getText();
		}
		return actual;
	}

	public void clickSubMenuLinks(String[] actual, String mainMenuLinkText) throws InterruptedException{
		for(String option : actual){
			moveToElement(mainMenuLinkText);
			WebElement we = waitForElementVisibility(By.linkText(option));
			we.click(); //To click on each link
			Thread.sleep(3000);
			waitForElementVisibility(By.xpath("//td[@class='headerblue' or @class='headergreen']"));
			String headerText = driver.findElement(By.xpath("//td[@class='headerblue' or @class='headergreen']")).getText();
			report.report(headerText);
		}
	}

	public String[] regExpToGetDates(String headerText){
		
		Pattern p = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
		Matcher m = p.matcher(headerText);
		String[] dates=new String[2];
		int i=0;
		while(m.find()){
			dates[i++] = m.group();
		}
		return dates;
	} 
	
	public int monthsDifferenceBetween(Date startDate, Date endDate){
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		report.report("reports helper month diff between method : "+diffYear);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		return diffMonth;
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