package com.ability.ease.employee;

import com.ability.ease.admin.AddCustomerPage;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.misc.MiscPage;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

/**
 * 
 * @author vahini.eruva
 *
 */
public class EmployeePage extends AbstractPageObject{
	
	public boolean addEmployee(Map<String, String> mapAttrVal)throws Exception
	{
		String username=null;
		String newpassword="test1234";
		int failCounter = 0;
		WebElement link = waitForElementVisibility(By.linkText("Add Employee"));
		if ( link != null) 			
			safeJavaScriptClick("Add Employee");
		else{
			report.report("Add Employee link not found hence returning false");
			return false;
		}
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\Admin.xml", mapAttrVal);
		UIActions admin = new UIActions();
		admin.fillScreenAttributes(lsAttributes);
		
		username = mapAttrVal.get("Username");

		List<WebElement> checkBoxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
		for(int i=0;i<checkBoxes.size();i++){
			if(!checkBoxes.get(i).isSelected())
			{
				checkBoxes.get(i).click();
			}
		}
		System.out.println(checkBoxes.size()+"size");


		clickButtonV2("Submit");

		String sExpected= "User "+ username + " added!";
		if(verifyAlert(sExpected)){
			report.report( username + " employee added succssfully !!", ReportAttribute.BOLD);
		}else{
			report.report( "Failed to add employee " + username);
			failCounter++;
		}
		clickOK();
		AddCustomerPage custpage=new AddCustomerPage();
		custpage.updatePassword(username,newpassword);		
		if(new MiscPage().validLogin(username,newpassword))
		{
			report.report("Logged in with the new employee successfully",ReportAttribute.BOLD);
		}
		else
		{
			report.report("Logged in with the new employee unsuccessfully",Reporter.WARNING);
			failCounter++;
		}
		
		return (failCounter == 0) ? true : false;

	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigateToPage() throws Exception {
		if(!isTextPresent("Current Status"))
			HomePage.getInstance().navigateTo(Menu.Admin, null);
		
	}

}
