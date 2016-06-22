package com.ability.ease.admin;

import java.util.List;
import java.util.Map;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.home.HomePage.SubMenu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class AdminPage extends AbstractPageObject{

	public boolean addUser(Map<String, String> mapAttrVal)throws Exception{
		//String sExpected = null;
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\AddUser.xml", mapAttrVal);
		UIActions admin = new UIActions();

		//sExpected = mapAttrVal.get("User Name");
		
		admin.fillScreenAttributes(lsAttributes);
		clickButtonV2("Submit");
		
		Alert alert = waitForAlert(driver);
		if(alert!=null)
		{
			report.report(alert.getText());
			alert.accept();
			return true;
		}
		else
		{
			report.report("Failed to Add User", Reporter.WARNING);
			return false;
		}
		
	}

	public boolean editUser(Map<String, String> mapAttrVal)throws Exception{

		return false;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.Admin, SubMenu.AddUser);
	}
}
