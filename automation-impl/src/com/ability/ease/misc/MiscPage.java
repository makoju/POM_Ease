package com.ability.ease.misc;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import jsystem.framework.report.Reporter;

import org.jdom.Parent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.selenium.webdriver.AbstractPageObject;
import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.TimeZoneConversionUtil;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.UserActionType;
import com.ability.ease.auto.system.WorkingEnvironment;
import com.ability.ease.auto.utilities.HttpClientUtil;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.home.HomePage.SubMenu;

public class MiscPage extends AbstractPageObject {

	public static final int ON = 1;
	public static final int OFF = 0;
	public static String mainWindowHanlde;
	String advanced_LINK = elementprop.getProperty("ADVANCED_LINK");
	String addHIC_LINK = elementprop.getProperty("ADDHIC_LINK");
	String agencyDROPDOWN_ID = elementprop.getProperty("AGENCYDROPDOWN_ID");
	String addHic_Basic_id = elementprop.getProperty("ADDHIC_BASIC_ID");
	String basic_LINK = elementprop.getProperty("BASIC_LINK");

	// C-tor
	public MiscPage() {
		super();
	}

	/*
	 * verify login functionality in ease
	 * 
	 * @param sUserName - username
	 * 
	 * @param sPassword - password
	 * 
	 * @throws exception
	 */
	public boolean validLogin(String sUserName, String sPassword) throws Exception {
		// If Browser is not open or current browser URL not matches with the
		// EASE URL then Open URL
		if (isBrowserOpen == false || !driver.getCurrentUrl().contains(WorkingEnvironment.getEaseURL())) {
			try {
				driver.get(WorkingEnvironment.getEaseURL());
				// driver.manage().window().maximize();
				mainWindowHanlde = driver.getWindowHandle();
				isBrowserOpen = true;
			} catch (Exception e) {
				report.report("Exception occured during driver.get() method execution");
				report.report(e.getMessage());
			}
		}

		// If some user already logs in and if the new user doesn't matches with
		// the already logged in user, Then do logout and login with the new
		// user
		if (isLoggedIn && !currentLoggedInUser.equalsIgnoreCase(sUserName)) {
			if (waitForElementToBeClickable(ByLocator.linktext, "LOGOUT", 10) != null) {
				report.report("Logging out user: " + currentLoggedInUser);
				try {
					safeJavaScriptClick("LOGOUT");
				} catch (Exception e) {
					report.report("QUIT method called due to exception in user: " + currentLoggedInUser
							+ " log out.So, Closing all browser instances!!!");
					quitAndRelaunchBrowser();
				}
				isLoggedIn = false;
				// Once logged out from the current user session, Lets switch to
				// mainwindow
				driver.switchTo().window(mainWindowHanlde);

				/*
				 * try { report.report(
				 * "QUIT method called after logging out user "+
				 * currentLoggedInUser +"...Closing all browser instances!!!");
				 * quitAndRelaunchBrowser(); } catch (Exception e) {
				 * report.report("Exception in launching browser: "+
				 * e.getMessage()); if(e instanceof NoSuchWindowException){ //TO
				 * DO } } finally{ isLoggedIn=false; }
				 */
			} else {
				/*
				 * isLoggedIn = false; validLogin(sUserName, sPassword);
				 */
				quitAndRelaunchBrowser();
			}
		}
		if (!isLoggedIn) {
			int countTry = 0;
			do {
				try {
					mainWindowHanlde = returnMainWindowHandle(); // save the
																	// current
																	// window
																	// for later
																	// use
					Thread.sleep(6000);
					driver.switchTo().frame(0);
					report.report("Login to Ease as:" + sUserName);
					typeEditBox("txtUser", sUserName);
					typeEditBox("txtPassword", sPassword);
					Thread.sleep(3000);
					clickButtonV2("loginbutton");
					Thread.sleep(5000);
					switchToChildWindow(mainWindowHanlde);

					if (waitForElementToBeClickable(ByLocator.linktext, "LOGOUT", 10) != null) {
						isLoggedIn = true;
						currentLoggedInUser = sUserName;
						// countTry = 3;
					} else {
						report.report(
								"QUIT method called because LOGOUT text is not present on the page...Closing all browser instances!!!");
						quitAndRelaunchBrowser();
					}
				} catch (UnhandledAlertException uae) {
					report.report(
							"QUIT method called from unhandled alert exception...Closing all browser instances!!!");
					quitAndRelaunchBrowser();
				} catch (Exception e) {
					e.printStackTrace();
					report.report("The following exception occured during login attempt:" + e.toString());
				} finally {
					report.report("Inside Finally Block attempt: ..." + countTry);
					countTry++;
				}
				if (!isLoggedIn) {
					report.report("Retrying Login....Attempt#" + countTry);
					driver.switchTo().defaultContent(); // if login failed come
														// out of frame(0) to
														// mainwindow
				}
			} while (countTry < 3 && !isLoggedIn);
		}
		// isLoggedIn = true;
		// currentLoggedInUser = userName;
		if (isLoggedIn) {
			// This code is to handle intermediate Manage FISS/DDE Settings page
			if (isTextPresent("MANAGE FISS/DDE SETTINGS")) {
				typeEditBox("ddeuser", "tset");
				typeEditBox("ddepassword", "test1234");
				typeEditBox("Verify", "test1234");
				clickButtonV2("Submit");
				WebDriverWait wait = new WebDriverWait(driver, 85);
				wait.until(ExpectedConditions.alertIsPresent());

				if (!verifyAlert("DDE information changed")) {
					report.report("DDE Information Submitted was not acknowledged", Reporter.WARNING);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/*
	 * verify invalid login functionality in ease
	 * 
	 * @param sUserName - incorrect username
	 * 
	 * @param sPassword - incorrect password
	 * 
	 * @throws exception
	 */
	public boolean invalidLogin(String sUserName, String sPassword) throws Exception {
		boolean result = false;
		if (isBrowserOpen == false || !driver.getCurrentUrl().contains(WorkingEnvironment.getEaseURL())) {
			driver.get(WorkingEnvironment.getEaseURL());
			isBrowserOpen = true;
		}
		if (isLoggedIn) {
			if (!isTextPresent("LOG IN")) {
				if (currentLoggedInUser.equalsIgnoreCase(sUserName)) {
					// check that current user is the requested userName by
					// checking status property currentLoggedInUser AND by
					// validating that page source includes the logged in user
					// name
					report.report(sUserName + " is already logged-in to ease... nothing to do here.",
							Reporter.ReportAttribute.BOLD);
				} else {
					try {
						report.report("Logging out user: " + currentLoggedInUser);
						HomePage.getInstance().signOut();
						driver.get(WorkingEnvironment.getEaseURL());
						isLoggedIn = false;
					} catch (Exception e) {
					}
				}
			} else {
				isLoggedIn = false;
				validLogin(sUserName, sPassword);
			}
		}
		if (!isLoggedIn) {
			try {
				driver.switchTo().frame(0);
				if (isElementPresent(By.id("loginbutton"))) {
					report.report("Login to Ease as:" + sUserName);
					typeEditBox("txtUser", sUserName);
					typeEditBox("txtPassword", sPassword);
					clickButtonV2("loginbutton");
					result = verifyAlert("Access denied!");
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
				report.report("The following exception occured during login attempt:" + e.toString());
			}
		}
		return result;
	}

	/*
	 * verify help link on ease page
	 * 
	 * @throws exception
	 */
	public boolean verifyHelp() throws Exception {
		if (isTextPresent("HELP")) {
			if (driver.findElement(By.linkText("HELP")).getAttribute("href").contains("EASE-User-Guide.pdf")) {
				return true;
			}
			report.report("No valid hyper reference is present in the HELP link");
		}
		return false;
	}

	/*
	 * verify support link on ease page
	 * 
	 * @throws exception
	 */
	public boolean verifySupport() throws Exception {
		if (isTextPresent("SUPPORT")) {
			if (driver.findElement(By.linkText("SUPPORT")).getAttribute("href")
					.contains("portal.support@abilitynetwork.com")) {
				int response = HttpClientUtil
						.sendPOST("mailto:portal.support@abilitynetwork.com?subject=EASE Support Request");
				System.out.println(response);
				if (response == 200) {
					return true;
				} else {
					report.report("Inavlid response code...please check the reference URI.");
				}
			}
			report.report("No valid mail reference is present in the suport link.");
		}
		return false;
	}

	/*
	 * verify refresh page
	 * 
	 * @throws exception
	 */
	public boolean verifyPageRefresh() throws Exception {

		String sXpathRefresh = "//a[@id='refreshPage']";

		if (isElementPresent(By.id("refreshPage"))) {
			driver.findElement(By.xpath(sXpathRefresh)).click();
			Thread.sleep(3000);
			if (isElementPresent(By.id("refreshPage"))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * verify forward and backward buttons functionality
	 * 
	 * @throws exception
	 */
	public boolean verifyFWDBACK() throws Exception {

		String sXpathBack = "//a[@id='backNav']";
		String sXpathFwd = "//a[@id='forwardNav']";
		boolean flag1, flag2 = false;

		safeJavaScriptClick("MY DDE");
		Thread.sleep(5000);
		safeJavaScriptClick("ELIG.");
		Thread.sleep(5000);
		isElementPresent(By.xpath(sXpathBack));
		driver.findElement(By.xpath(sXpathBack)).click();
		flag1 = isLinkSelected("MY DDE");
		isElementPresent(By.xpath(sXpathFwd));
		driver.findElement(By.xpath(sXpathFwd)).click();
		flag2 = isLinkSelected("ELIG.");
		if (flag1 && flag2) {
			return true;
		}
		return false;
	}

	/*
	 * verify change personal page in My account tab
	 * 
	 * @throws exception
	 */
	public boolean verifyPersonalInfo(Map<String, String> mapAttrVal, UserActionType userAction) throws Exception {

		String sName = null, sPhone = null, sEmail = null, sBtn = null, sXpathBtn = null;
		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml\\Misc\\Misc.xml", mapAttrVal);
		UIActions admin = new UIActions();

		clickMYACCOUNTLink();
		if (userAction.toString().equalsIgnoreCase("Read")) {
			for (Attribute scrAttr : lsAttributes) {
				if (scrAttr.getDisplayName().equalsIgnoreCase("Name")) {
					sName = scrAttr.getLocator();
				}
				if (scrAttr.getDisplayName().equalsIgnoreCase("Phone")) {
					sPhone = scrAttr.getLocator();
				}
				if (scrAttr.getDisplayName().equalsIgnoreCase("Email")) {
					sEmail = scrAttr.getLocator();
				}
				if (scrAttr.getDisplayName().equalsIgnoreCase("Submit")) {
					sBtn = scrAttr.getLocator();
					sXpathBtn = "//input[@type='" + sBtn + "']";
				}
			}
			if (isElementPresent(By.name(sName)) && isElementPresent(By.name(sPhone))
					&& isElementPresent(By.name(sEmail)) && isElementPresent(By.xpath(sXpathBtn))) {
				return true;
			}
		}
		Thread.sleep(5000);
		if (userAction.toString().equalsIgnoreCase("Modify")) {
			admin.fillScreenAttributes(lsAttributes);
			return verifyAlert("Personal information changed successfully!");
		}
		return false;
	}

	/*
	 * Verify left menu items under My Account Page
	 */
	public boolean verifyLeftMenuInMyAccountTab(MyAccountSubMenu subMenuItem, String sExpectedOutput) throws Exception {

		String sXpathContainer = "//div[@id='sbarcontainer']/ul/li";
		String sXpathTabHead = "//*[@id='mainPageArea']/div/table/tbody/tr/td";
		List<WebElement> menuItem = new ArrayList<WebElement>();
		String sElementText = null;

		clickMYACCOUNTLink();
		waitForElementVisibility(By.xpath(sXpathContainer), 60);
		// get all menu item into one list object
		menuItem = driver.findElements(By.xpath(sXpathContainer));
		int i = 1;
		for (WebElement we : menuItem) {
			WebElement subItem = driver.findElement(By.xpath(sXpathContainer + "[" + i + "]"));
			if (subItem != null) {
				String sActual = subItem.getText().trim();
				String sExpected = subMenuItem.toString().trim();
				if (sActual.equals(sExpected)) {
					if (isElementPresent(By.xpath(sXpathContainer + "[" + i + "]")))
						sElementText = subItem.getText();
					report.report("Clicking on " + sElementText + " option");
					safeJavaScriptClick(sElementText);
					Thread.sleep(5000);
					if (driver.findElement(By.xpath(sXpathTabHead)).getText().equalsIgnoreCase(sExpectedOutput)) {
						report.report(
								"verified left menu option '" + subMenuItem.toString() + "' under My Account tab");
						return true;
					}
				}
			} else {
				report.report("Subitem in left menu items is null");
			}
			i++;
		}
		return false;
	}

	/*
	 * Verify setup alerts options
	 */
	public boolean verifyAlertOptions(Map<String, String> mapAttrVal, String userName) throws Exception {

		ResultSet results = null;
		int userID = 0;
		String[] sValues = null;
		Map<Integer, Integer> userAlertConfig = new HashMap<Integer, Integer>();
		int actualAlertOptionValue = 0, actualONOFF = 0;
		int expectedAlertOptionValue, expectedONOFF = 0;
		int counter = 0;

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml\\Misc\\Misc.xml", mapAttrVal);

		UIActions admin = new UIActions();

		clickMYACCOUNTLink();
		Thread.sleep(3000);
		safeJavaScriptClick("Setup Alerts");
		if (waitForElementToBeClickable(ByLocator.xpath, "//td[contains(text(),'SETUP ALERTS')]", 60) != null) {
			admin.fillScreenAttributes(lsAttributes);
			verifyAlert("User alerts updated!");
		}

		// verify whether data got changed in database
		// getting user id from ddez.user table

		results = MySQLDBUtil.getResultFromMySQLDB("SELECT UserID FROM ddez.user where UserName='" + userName + "'");
		String sUserID = MySQLDBUtil.getColumnValue(results, "UserID");
		userID = Integer.parseInt(sUserID);
		// verify alert options setup in ddez.useralertconfig table for specific
		// user
		results = MySQLDBUtil.getResultFromMySQLDB("SELECT * FROM ddez.useralertconfig where UserID ='" + userID + "'");

		while (results.next()) {
			int alertOption = results.getInt("AlertType");
			int onOff = results.getInt("OnOff");
			if (userAlertConfig.containsKey(alertOption)) {
				// to do
			} else {
				userAlertConfig.put(alertOption, onOff);
			}
		}

		for (Attribute scrAttr : lsAttributes) {
			if (scrAttr.getDisplayName().equalsIgnoreCase("Setup Alerts Options")) {
				sValues = scrAttr.getValue().split(",");
				for (String str : sValues) {
					String[] sOptions = str.split(":");
					String alertOptionName = sOptions[0];
					String onOrOff = sOptions[1];
					expectedONOFF = onOrOff.equalsIgnoreCase("ON") ? ON : OFF;
					String alertOptionXpath = getAlertOptionXpath(alertOptionName);
					String alertOptionValue = driver.findElement(By.xpath(alertOptionXpath + "/../input"))
							.getAttribute("value");
					expectedAlertOptionValue = Integer.parseInt(alertOptionValue);
					Iterator<Map.Entry<Integer, Integer>> entries = userAlertConfig.entrySet().iterator();

					while (entries.hasNext()) {
						Map.Entry<Integer, Integer> entry = entries.next();
						if (entry.getKey() == expectedAlertOptionValue) {
							actualAlertOptionValue = entry.getKey();
							actualONOFF = entry.getValue();
						}
					}
					if (actualONOFF == expectedONOFF && actualAlertOptionValue == expectedAlertOptionValue) {
						counter++;
					}
				}
			}
		}
		if (counter == sValues.length) {
			report.report("Verified the alert option values in ddez.useralertconfig table");
			return true;
		}
		return false;
	}

	/*
	 * verify refresh page
	 * 
	 * @throws exception
	 */
	public boolean verifyAddHICs(Map<String, String> mapAttrVal) throws Exception {

		UIAttributeXMLParser parser = new UIAttributeXMLParser();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml\\Misc\\MyDDE.xml", mapAttrVal);
		UIActions admin = new UIActions();
		admin.fillScreenAttributes(lsAttributes);
		return false;
	}

	/*
	 * This method is used as part of forward and backward buttons
	 * functionalities Used by verifyFWDBACK()
	 */
	public boolean isLinkSelected(String linkName) {
		List<WebElement> topnavlinks = new ArrayList<WebElement>();
		topnavlinks = driver.findElements(By.className("topNavAnchor"));
		for (WebElement we : topnavlinks) {
			String className = we.getAttribute("class");
			if (className.contains("topNavAnchorSelected")) {
				return true;
			}
		}
		return false;
	}

	public void clickMYACCOUNTLink() throws Exception {
		WebElement mydde = waitForElementVisibility(By.linkText("MY ACCOUNT"));
		String classAttr = mydde.getAttribute("class");
		if (mydde != null) {
			if (!classAttr.equalsIgnoreCase("topNavAnchor topNavAnchorSelected")) {
				clickLink("MY ACCOUNT");
				return;
			} else {
				// nothing to do
			}
		}
		report.report("MY ACCOUNT Link element is not avaible on page");
	}

	/*
	 * This method is used to get the alert option value from UI and used as
	 * part of verifyAlertOption() method
	 */
	public int getAlertOptionValue(String alertOptionName) {

		String alertOptionXpath = getAlertOptionXpath(alertOptionName);
		return Integer.parseInt(driver.findElement(By.xpath(alertOptionXpath)).getAttribute("value"));
	}

	public boolean isLogedIn() {
		return isLoggedIn;
	}

	public void quitAndRelaunchBrowser() throws InterruptedException {
		isLoggedIn = false;
		driver.quit();
		Thread.sleep(10000);
		isBrowserOpen = false;
		openBrowser();
		driver.get(WorkingEnvironment.getEaseURL());
		mainWindowHanlde = driver.getWindowHandle();
		// driver.manage().window().maximize();
	}

	public boolean verifyAddSingleHIC(String agency, String hic, String sExpectedMessage) throws Exception {
		navigateToPage();
		//Thread.sleep(1000);
		
		clickLink(basic_LINK);
		Thread.sleep(10000);
		clickLink(advanced_LINK);
		Thread.sleep(10000);
		clickLink(addHIC_LINK);
		selectByNameOrID(agencyDROPDOWN_ID, "HHA1");
		WebElement enterHIC = waitForElementVisibility(By.id(elementprop.getProperty("INPUT_SINGLE_HIC_ID")));
		typeEditBoxByWebElement(enterHIC, hic);

		((JavascriptExecutor) driver).executeScript("addSingleHIC();");
		clickButtonV2(elementprop.getProperty("CLICK_SUBMIT_VALUE"));

		if (!verifyAlert(sExpectedMessage))
			return false;
		Thread.sleep(10000);
		clickLinkPartialText("See a log of");

	
		WebElement timestamp = waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr[1]/td[1]/a"));
        if(timestamp!=null)
              timestamp.click();
else{
  	      report.report("Unable to find timestamp in log table", Reporter.WARNING);
  	     
  return false;
}

		Thread.sleep(10000);
		//return (hic.equals(waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr/td[1]")).getText()));
		WebElement  hicfromlogtable = waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr/td[1]")); 
		if(hicfromlogtable!=null)
		  return Verify.StringEquals(hic, hicfromlogtable.getText());

		else
		{
		    report.report("Unable to find HIC from log table");
		    return false;
		}


	}

	public boolean verifyAddSingleHIC_BasicView(String agency, String hic, String sExpectedMessage) throws Exception {

		navigateToPage();
		clickLink(addHIC_LINK);
		selectByNameOrID(agencyDROPDOWN_ID, "HHA");
		WebElement enterHIC = waitForElementVisibility(By.id(elementprop.getProperty("INPUT_SINGLE_HIC_ID")));
		typeEditBoxByWebElement(enterHIC, hic);

		((JavascriptExecutor) driver).executeScript("addSingleHIC();");
		clickButtonV2(elementprop.getProperty("CLICK_SUBMIT_VALUE"));

		if (!verifyAlert(sExpectedMessage))
			return false;
		Thread.sleep(10000);
		clickLinkPartialText("See a log of");

		/*WebElement timestamp = waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr[1]/td[1]/a"));
		timestamp.click();
		Thread.sleep(10000);
		return (hic.equals(waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr/td[1]")).getText()));*/
		WebElement timestamp = waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr[1]/td[1]/a"));
        if(timestamp!=null)
              timestamp.click();
else{
  	      report.report("Unable to find timestamp in log table", Reporter.WARNING);
  	     
  return false;
}

		Thread.sleep(10000);
		//return (hic.equals(waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr/td[1]")).getText()));
		WebElement  hicfromlogtable = waitForElementVisibility(By.xpath(".//*[@id='scrollContent']/tr/td[1]")); 
		if(hicfromlogtable!=null)
		  return Verify.StringEquals(hic, hicfromlogtable.getText());

		else
		{
		    report.report("Unable to find HIC from log table");
		    return false;
		}
		
		

	}

	// verifyAddMultpleHICs_BasicView

	public boolean verifyAddMultpleHICs_BasicView(String agency, String hics, String sExpectedMessage)
			throws Exception {
		navigateToPage();

		clickLink(addHIC_LINK);

		selectByNameOrID(agencyDROPDOWN_ID, "HHA");

		String stringMic = hics;
		String[] shics = {};

		if (stringMic.contains(";")) {
			shics = stringMic.split(";");
			WebElement multihicstextarea = waitForElementVisibility(By.id("MultiHICs"), 60);
			multihicstextarea.clear();

			for (String hic : shics) {
				multihicstextarea.sendKeys(hic);
				multihicstextarea.sendKeys(Keys.ENTER);
			}
			// stringMic = stringMic.replaceAll(";", "\n");
			// WebElement enterMul_HIC =
			// waitForElementVisibility(By.id(elementprop.getProperty("INPUT_MULTIHICS_ID")));
			// report.report("Hics after adding new line: "+stringMic);
			// typeEditBox(elementprop.getProperty("INPUT_MULTIHICS_ID"),
			// stringMic);

			((JavascriptExecutor) driver).executeScript("addMultipleHICs();");

		}

		clickButtonV2(elementprop.getProperty("CLICK_SUBMIT_VALUE"));
		
		Thread.sleep(10000);
		if (!verifyAlert(shics.length + " " + sExpectedMessage))

			return false;

		//Thread.sleep(3000);

		WebElement seealog = waitForElementVisibility(By.partialLinkText("See a log of"));
		seealog.click();

		Thread.sleep(10000);

		WebElement timestamp = waitForElementVisibility(By.xpath(elementprop.getProperty("TIMESTAMP_XPATH")));
		timestamp.click();

		//Thread.sleep(10000);
		// inputHICs
		// String[] stringMics = stringMic.split("\n");

		WebElement hicTemp = null;

		int failurecount = 0;

		for (int i = 0; i < shics.length; i++) {

			System.out.println("Entered into hics iteration" + shics[i]);
			hicTemp = waitForElementVisibility(
					By.xpath("//*[@id='scrollContent']/tr/td[1][contains(text(),'" + shics[i] + "')]"));

			if (hicTemp == null) {
				report.report("Expected HIC " + shics[i] + "  not found in Added HICs log table");
				failurecount++;
				continue;
			}

			else
				report.report("Expected HIC " + shics[i] + " found in Added HICs log table");

		}

		return failurecount == 0 ? true : false;
	}

	public boolean verifyAddMultpleHICs(String agency, String hics, String sExpectedMessage) throws Exception {
		clickLink(advanced_LINK);
		Thread.sleep(10000);
		clickLink(addHIC_LINK);

		selectByNameOrID(agencyDROPDOWN_ID, agency);

		String stringMic = hics;
		String[] shics = {};

		if (stringMic.contains(";")) {
			shics = stringMic.split(";");
			WebElement multihicstextarea = waitForElementVisibility(By.id("MultiHICs"), 60);
			multihicstextarea.clear();

			for (String hic : shics) {
				multihicstextarea.sendKeys(hic);
				multihicstextarea.sendKeys(Keys.ENTER);
			}

			((JavascriptExecutor) driver).executeScript("addMultipleHICs();");

		}

		clickButtonV2(elementprop.getProperty("CLICK_SUBMIT_VALUE"));

		if (!verifyAlert(shics.length + " " + sExpectedMessage)) {
			report.report("Expected Alert was not found", Reporter.WARNING);
			return false;
		}

		Thread.sleep(3000);

		WebElement seealog = waitForElementVisibility(By.partialLinkText("See a log of"));
		seealog.click();

		//Thread.sleep(10000);

		WebElement timestamp = waitForElementVisibility(By.xpath(elementprop.getProperty("TIMESTAMP_XPATH")));
		timestamp.click();

		Thread.sleep(10000);
		// inputHICs
		// String[] stringMics = stringMic.split("\n");

		WebElement hicTemp = null;

		int failurecount = 0;

		for (int i = 0; i < shics.length; i++) {

			System.out.println("Entered into hics iteration" + shics[i]);
			hicTemp = waitForElementVisibility(
					By.xpath("//*[@id='scrollContent']/tr/td[1][contains(text(),'" + shics[i] + "')]"));

			if (hicTemp == null) {
				report.report("Expected HIC " + shics[i] + "  not found in Added HICs log table");
				failurecount++;
				continue;
			}

			else
				report.report("Expected HIC " + shics[i] + " found in Added HICs log table");

		}

		return failurecount == 0 ? true : false;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void navigateToPage() throws Exception {
		// HomePage.getInstance().navigateTo(Menu.Admin, SubMenu.AddUser);
		HomePage.getInstance().navigateTo(Menu.MYDDE, null);
	}

}
