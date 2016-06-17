package com.ability.ease.admin;

import java.util.List;
import java.util.Map;

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
		String sActual = null;
		navigateToPage();
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Administration\\AddUser.xml", mapAttrVal);
		UIActions admin = new UIActions();

		for ( Attribute scrAttr:lsAttributes){
			if( scrAttr.getDisplayName().equalsIgnoreCase("User Name")){
				sActual = scrAttr.getValue().toString();
			}
		}
		admin.fillScreenAttributes(lsAttributes);
		clickButtonV2("Submit");
		report.report("User "+ sActual + " added!");
		return verifyAlert("User "+ sActual + " added!");
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
