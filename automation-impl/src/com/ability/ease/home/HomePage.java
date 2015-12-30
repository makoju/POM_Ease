package com.ability.ease.home;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.selenium.webdriver.AbstractPageObject;
import com.ability.ease.selenium.webdriver.WebDriverHelper;

public class HomePage extends AbstractPageObject{

	private static HomePage instance = null;

	// C'tor
	private HomePage() {
		super();
	}

	public static enum Menu {
		MYDDE("MY DDE"),
		Admin("ADMINISTRATION"),
		NA("NA");
		private String value;

		private Menu(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	};   

	public static enum SubMenu {
		AddUser("Add User"),
		NA("NA");
		private String value;

		private SubMenu(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	};   

	public static HomePage getInstance() {
		if (instance == null) {
			instance = new HomePage();
		}
		return instance;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	} 

	/**
	 * use this method to sign out from Ease
	 * @throws Exception 
	 */
	public void signOut() throws Exception {
		clickLink("LOGOUT");
		isBrowserOpen = false;
	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

	public void navigateTo(Menu menuName, SubMenu subMenuName) throws Exception {
		report.startLevel("Navigating to " + menuName.name() + " > "+ subMenuName.name());
		/*clickLinkOnlyIfExists(menuName.toString());
		Thread.sleep(3000);
		clickLinkOnlyIfExists(subMenuName.toString());*/
		clickMenuLinkOnlyIfExists(menuName.toString());
		clickMenuLinkOnlyIfExists(subMenuName.toString());
	}

	
	/**
	 * Clicks ONLY on MENU link by link text ONLY if it is exists ,otherwise do nothing
	 * @param linkText
	 * @throws Exception 
	 * @return true for success, false for error
	 */
	public boolean clickMenuLinkOnlyIfExists(String linkText) throws Exception{
		boolean isfound = true ;
		//String menuLinkXpath = "//a[./text()='" + linkText + "']";
		WebElement  link = null;
		WebDriverWait wait = new WebDriverWait(driver, shortTimeOutInSeconds);
		link  = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
		if (link!=null) {
			WebDriverHelper.highlightElement(driver, link);
			link.click();				
			try { 
				clickButton("Yes",1); 
			} catch (Exception ex) {
				//ignore
			}
		}else {
				isfound = false ;
		}

		return isfound;
	}


}
