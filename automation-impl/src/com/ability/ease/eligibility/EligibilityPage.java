package com.ability.ease.eligibility;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.framework.report.ReporterHelper;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class EligibilityPage extends AbstractPageObject{


	public boolean verifyEligibility(Map<String, String> mapAttrVal)throws Exception{
		int failurecount=0;
		navigateToPage();
		clickLink("Eligiblity Check");
		
		//Fill Screen
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute>	lsAttributes = parser.getUIAttributesFromXMLV2
				(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml", mapAttrVal);
		UIActions eligibility = new UIActions();
		eligibility.fillScreenAttributes(lsAttributes);
		clickButton("Submit");

		if(!verifyAlert("Eligibility check accepted"))
			failurecount++;
		
		//validation
		ReportsHelper reportshelper = new ReportsHelper();
		Attribute attrlastname = reportshelper.getAttribute(lsAttributes, "Last Name");
		String lastname = attrlastname!=null?attrlastname.getValue():null;
		
		//clickOnElement(ByLocator.id, "tdPendingActivity", 10);
		WebElement tblpendingactivity = waitForElementVisibility(By.id("tdPendingActivity"));
		moveToElement(tblpendingactivity);
				
		String firstlastname = Verify.getTableData("pendingActivity", 1, 5);
		if (firstlastname!=null && firstlastname.contains(lastname))
		  report.report("Submitted Patient Eligibility was found in pending table(Orange).", ReportAttribute.BOLD);		
		else{
		  report.report("Submitted Patient Eligibility was not found in pending table(Orange). Trying in Good Activity (Green) table!!!!", Reporter.WARNING);
		  waitForElementVisibility(By.id("tdGoodActivity"));
		  firstlastname = Verify.getTableData("goodActivity", 1, 5);
		  if (firstlastname!=null && firstlastname.contains(lastname))
			  report.report("Submitted Patient Eligibility was found in Good Activity table(Green).", ReportAttribute.BOLD);
		  else{
			  report.report("Submitted Patient Eligibility was not found in neither Pending Activity Table nor Good Activity table(Green).", Reporter.WARNING);
			  return false;
		  }
		}
		//Todo - Need to click on reports link in good activity table and verify the report
					
		return failurecount==0?true:false;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.ELIGIBILITY, null);
	}

}
