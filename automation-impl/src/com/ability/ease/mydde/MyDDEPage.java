package com.ability.ease.mydde;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class MyDDEPage extends AbstractPageObject{

	ReportsHelper reportshelper = new ReportsHelper();

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYDDE, null);
	}

	public boolean verifyPageViewAndOptions() {

		int failurecount = 0;
		WebElement verifyAdvanced = waitForElementVisibility(By.id("reportComplexity"));
		if ( verifyAdvanced.getText().contains("Advanced")) {
			report.report("Displayed page content is in BASIC view");
		}else{
			report.report("Displayed page content is in Advanced view");
			failurecount++;
		}

		String[] options = {"reportHICSearch","reportHome","reportList","reportTimeframe","reportAgency","reportPieChart","reportPrint","reportExport",
				"reportNewUB04","reportComplexity"}; //skipped Add HICs

		for(String optionsId : options){

			WebElement verifyOptions = waitForElementVisibility(By.id(optionsId));
			if ( verifyOptions != null) {
				String optionsText = verifyOptions.getText().toUpperCase();
				if(!optionsText.equals("")){
					report.report("Successfully verified "+optionsText+" under Basic view.");
				}
				else{
					report.report("Successfully verified "+optionsId+" under Basic view.");
				}
			}else{
				report.report("Failed to verify the options under Basic view.");
				failurecount++;
			}
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderReports(Map<String, String> mapAttrValues) throws Exception {

		int failurecount = 0;
		String[] expected = {"ADR","RAPs At Risk","Stuck In Suspense","Eligibility Issues","T Status Report"};

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		
		/*//Hover on reports and get the links by clicking on each sub link
		boolean compare = reportshelper.verifySubMenuLinksAndClick(expected, "Reports", "reportListMenu");
		if(compare){
			report.report("Actual and expected values are equal.");
		}
		else{
			failurecount++;
		}

		//Read timeframe
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);

		//For Sorting in Overnight Summary Report
		if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 5))
			failurecount++;
		
		//Loop through ADR,RAP's at Risk,Stuck In Suspense & Eligibility Issues
		for(int i=0;i<4;i++){
			String headerText = getElementText(By.xpath("//td[@class='headerblue']"));
			((JavascriptExecutor) driver).executeScript("$('#Report_"+i+"').click();");
			//Column Sorting
			if (!Verify.validateTableColumnSortOrder("datatable", "HIC", 1))
				failurecount++;
		}*/
		
		//Click on T-Status Report
		((JavascriptExecutor) driver).executeScript("$('#Report_4').click();");
		
		String expectedText = "ADVANCED SEARCH";
		String advancedSearchText = getElementText(By.xpath("//td[@class='headerblue'"));
		if(advancedSearchText.equals(expectedText)){
			report.report("User is in ADVANCED SEARCH page.");
		}else{
			failurecount++;
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderTimeframe(Map<String, String> mapAttrValues) throws Exception {
		
		int failurecount = 0;
		String[] expected = {"Overnight","Weekly"};
		
		boolean compare = reportshelper.verifySubMenuLinksAndClick(expected, "Timeframe", "timeframeListMenu");
		if(compare){
			report.report("Actual and expected values are equal.");
		}
		else{
			failurecount++;
		}
		//verify From and To
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+ "uiattributesxml\\MyDDE\\MYDDE.xml", mapAttrValues);
		UIActions mydde = new UIActions();
		mydde.fillScreenAttributes(lsAttributes);
		//To-Do
		
		return failurecount == 0 ? true : false;
	}

	public boolean verifyOptionsUnderAgency(Map<String, String> mapAttrValues) throws Exception {
		
		
		
		
		return false;
	}

	public boolean verifySaveProfileOption() throws Exception {
		
		int failurecount = 0;
//		clickLink("reportHICSearch");
		((JavascriptExecutor) driver).executeScript("$('#reportHICSearch').click();");
		Thread.sleep(5000);
		return false;
	}
}
