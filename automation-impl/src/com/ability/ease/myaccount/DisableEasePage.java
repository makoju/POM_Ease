package com.ability.ease.myaccount;

import org.openqa.selenium.WebElement;

import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class DisableEasePage extends AbstractPageObject {
	
	public boolean disableEase(String grouporagencycheckboxname, String grouporagencyname, String disableuntil) throws Exception {
		String selectlocator;
		if(!grouporagencycheckboxname.contains("Agency"))
			selectlocator="groupid";
		else
			selectlocator="ProvID";
		
		navigateToPage();
		WebElement radiobutton = waitForElementToBeClickable(ByLocator.xpath, "//span[text()='"+grouporagencycheckboxname+"']/preceding-sibling::span[@class='formkey']/input", 60);
		radiobutton.click();
		selectByNameOrID(selectlocator, grouporagencyname);
		selectByNameOrID("stoptype", disableuntil);
		clickButtonV2("Submit");
		return verifyAlert("FISS/DDE will stop executing immediately!");
	}
	
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("Change FISS/DDE Settings") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
		}
		clickLink("Disable Ease");
	}
}
