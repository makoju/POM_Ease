package com.ability.ease.selenium.webdriver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jsystem.framework.JSystemProperties;
import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;
import jsystem.runner.loader.ClassPathBuilder;
import jsystem.runner.loader.ExtendsTestCaseClassLoader;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.BrowserType;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.portal.selenium.WebDriverType;
import com.ability.ease.auto.events.ui.UIEvents;
import com.ability.ease.auto.system.WorkingEnvironment;

/**
 * The page object design pattern makes it easier to access HTML elements in a
 * page.<br>
 * Each {@link WebElement} field (i.e. {@code private WebElement element}) will
 * be automatically attached to the one in the {@link WebDriver}<br>
 * The default locating mechanism is {@link ByIdOrName}, that means that in the
 * previous example, {@code element} will be attached to a
 * {@code <div name='element'>...</div>}<br>
 * In order to use other mechanism, or use other name or id (such as in id that
 * contains colon etc,) use {@link FindBy} or {@link FindBys} e.g: <br>
 * {@code public class SamplePage extends AbstractPageObject <br>
 * 
 * &nbsp;&nbsp;{@literal @}FindBys (value={
 * </br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@literal @}FindBy(id="container"),
 * </br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@literal @}
 * FindBy(linkText="start")</br>
 * &nbsp;&nbsp;}&nbsp;&nbsp;)<br>
 * 
 * &nbsp;&nbsp;private {@link WebElement} element;<br>
 * <br>
 * &nbsp;&nbsp;{@literal @}FindBy(css="div.containers")<br>
 * &nbsp;&nbsp;private {@link List}<{@link WebElement}> allDivContainers;<br>
 * <br>
 * &nbsp;&nbsp;private {@link WebElement} element; //this element is the same as
 * with {@literal @}FindBy(id="element")<br>
 * <br>
 * }<br>
 *
 * 
 */
public abstract class AbstractPageObject implements HasWebDriver, Observer  {

	protected static WebDriver driver;
	private boolean clearCookiesBeforeOpen = false;
	private String seleniumTimeOut = "30000";
	protected String webDriverEventListenerClasses = "com.ability.ease.selenium.webdriver.WebDriverScreenshotEventHandler;" +
			"com.ability.ease.selenium.webdriver.WebDriverReportEventHandler";
	protected static Reporter report = ListenerstManager.getInstance();
	protected static boolean isLoggedIn = false;
	protected static boolean isBrowserOpen = false;
	protected static String currentLoggedInUser = "";
	protected enum SimplePopUpAction{ClickOK, ClickCancel, ClickYes, ClickNO }
	protected static final long DefaultTimeOutInSeconds = 30;  
	protected static final long shortTimeOutInSeconds = 1;
	protected static final long MaxTimeOutInSeconds = 300; //5 minutes


	// declaration of commonly used web elements

	//@FindBy(css="button:contains('OK')")
	@FindBy(xpath="//span[contains(./text(),'OK')]")
	private WebElement btnOK;

	@FindBy(css="button:contains('Cancel')")
	private WebElement btnCancel;


	private static boolean wasAddedAsWebdriverObserver = false;

	//C'tor
	public AbstractPageObject() {
		initElements();		
		assertMustExistElements();
		assertInPage();
		if (!wasAddedAsWebdriverObserver) {
			UIEvents.getInstance().addObserver(this);
			wasAddedAsWebdriverObserver = true;
		}
	}

	/**
	 * change all {@link WebElement} and {@link List}<{@link WebElement}> fields
	 * of this object to be proxys to the HTML elements in the page
	 */
	void initElements() {
		if (driver==null) {
			openBrowser();
		}

		PageFactory.initElements(driver, this);

	}


	/**
	 * Asserts that the web is actually opened in the page we expect it to be.<br>
	 * this method will run after all of the elements
	 * e.g:
	 * 
	 * <pre>
	 * {@code 
	 * &#64;Override void assertInModule(){
	 *  WebDriverWait wait = new WebDriverWait(driver, 10);
	 * 	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("container")));
	 * 	wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("start")));
	 * 	}
	 * }
	 * </pre>
	 */
	/**
	 * Asserts that all {@link ElementMustExist}s fields are visible in this
	 * page object.
	 */
	public abstract void assertInPage();	

	public enum SortType{
		ASCENDING("ascending"), DESCENDING("descending");
		String value;
		private SortType(String s){
			this.value=s;
		}
		String getValue(){
			return this.value;
		}
	}

	/**
	 * Navigates to page's main screen
	 */
	public abstract void navigateToPage() throws Exception;

	private int pollingMillis = 500;
	public void assertMustExistElements() {		
		if (driver!=null) {
			driver.manage().timeouts().implicitlyWait(pollingMillis, TimeUnit.MILLISECONDS);
			Field[] fields = this.getClass().getDeclaredFields();
			for (final Field currField : fields) {
				currField.setAccessible(true);
				if (currField.isAnnotationPresent(ElementMustExist.class)) {
					By by = new Annotations(currField).buildBy();
					report.report("Waiting for Element (@ElementMustExist): " + by);
					final WebDriverWait wait = new WebDriverWait(driver, DefaultTimeOutInSeconds);
					wait.ignoring(WebDriverException.class);
					wait.pollingEvery(100, TimeUnit.MILLISECONDS);
					wait.until(ExpectedConditions.visibilityOfElementLocated(by));
				}
			}
		}
	}

	//protected abstract void load(String url);


	/**
	 * Use this method to verify if an error message is being currently shown on page.
	 * Assuming all errors message in IM share the same format.
	 * @return true if error mesaage found on page, or false otherwise.
	 */
	public boolean assertErrorMessageOnPage(String text) {			
		if (driver.findElements(By.cssSelector(WebDriverHelper.constructMessage(".im-error-msg:contains('{0}')", text))).size() > 0  ) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * Opens the required browser if closed and navigate to the application main URL (which will likely to be the login page) 
	 * @throws Exception 
	 */
	protected void openBrowser()  {
		if (!isBrowserOpen) {
			try {
				WebDriverWrapper driverWrapper = getBrowserInstance();
				isBrowserOpen = true;
				initWebDriverEventListener(driverWrapper);

				if (seleniumTimeOut != null && WorkingEnvironment.getWebdriverType() != null) {
					setImplicitlyWait(driverWrapper, seleniumTimeOut);
				}

				driver = driverWrapper;

				driver.manage().window().maximize();

				if (isClearCookiesBeforeOpen()) {
					driver.manage().deleteAllCookies();
				}
			} catch (Exception ex) {
				report.report("Exception while opening a open browser: " + ex.getMessage());
				report.report("Retrying...");
			}
		}
	}

	private WebDriverWrapper getBrowserInstance() throws Exception {
		WebDriverWrapper webDriverInstance = null;

		if (WorkingEnvironment.getWebdriverType() != null) {
			webDriverInstance = WebDriverFactory.getWebDriver(WorkingEnvironment.getWebdriverType());
		}
		else {
			String error = "Browser instance init Exception, the webDriver type(e.g. CHROME_DRIVER) must not be null";
			error += "\n" + WebDriverType.getAllSupportedWebDriverTypesAsString();

			throw new RuntimeException(error);
		}
		return webDriverInstance;
	}

	private void setImplicitlyWait(WebDriverWrapper webDriverWrapper, String seleniumTimeOut) {
		try {
			long miliSecs = Long.valueOf(seleniumTimeOut);
			report.report("Set WebDriver implicitlyWait seleniumTimeOut to " + seleniumTimeOut + "(Milliseconds)");
			webDriverWrapper.getDriver().manage().timeouts().implicitlyWait(miliSecs, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			report.report("unable to set WebDriver implicitlyWait seleniumTimeOut,please check the SUT seleniumTimeOut parameter(Milliseconds  as String)");
		}

	}

	private void initWebDriverEventListener(WebDriverWrapper driverWrapper) {
		if (webDriverEventListenerClasses != null && !webDriverEventListenerClasses.isEmpty()) {
			String[] listeners = webDriverEventListenerClasses.split(";");

			ClassLoader loader;
			if (JSystemProperties.getInstance().isJsystemRunner()) {
				loader = new ExtendsTestCaseClassLoader(ClassPathBuilder.getClassPath(), getClass().getClassLoader());
			}
			else {
				loader = this.getClass().getClassLoader();
			}

			for (String eventListenerClass: listeners) {
				try {

					if(eventListenerClass !=null && !eventListenerClass.isEmpty()){
						// Trim the class string
						eventListenerClass = eventListenerClass.trim();

						// load the class by the class loader
						Class<?> loadClass = loader.loadClass(eventListenerClass);

						// create new instance of the class
						WebDriverEventListener webDriverEventListener = (WebDriverEventListener) loadClass.newInstance();

						// register the class as a web driver event handler.
						driverWrapper.register(webDriverEventListener);
					}


				} catch (Throwable e) {
					report.report(eventListenerClass + " cannot be loaded. " + e.getStackTrace(),Reporter.PASS);
				}
			}
		}

	}



	//*************************************************************************************************************
	//*                                                                                                           *
	//###################    ----------- commonly used web actions methods -----------		########################
	//*                                                                                                           *
	//*************************************************************************************************************


	/**
	 * Returns an information string about the active browser
	 * @return
	 */
	public static String getBrowserInfo(BrowserType browserType) {
		String result = "N/A";
		try {
			String browserInfo =  (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
			switch (browserType) {
			case Firefox:
				result = browserInfo.substring(browserInfo.indexOf("Firefox"), browserInfo.length());
				break;
			case InternetExplorer:
				String substr = browserInfo.substring(browserInfo.indexOf("MSIE"), browserInfo.length()-1);
				result = substr.substring(0, substr.indexOf(";"));
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			//ignore
		}

		return result;
	}

	/**
	 * Checks check box. 
	 * @param chkBoxName - check box unique identifier
	 */
	public void checkChkBox(String chkBoxName) {
		checkCheckbox(chkBoxName, true);
	}

	/**
	 * Checks the checkbox in a certain Position (positionToCheckForCheckBox) before 
	 * a specific tag with a certain text (textToSearch)
	 * @param textToSearch - text to be search in a specific tag
	 * @param positionToCheckForCheckBox - check a check box at the position passed here...
	 * @param check - pass true to check and false to uncheck
	 */
	private boolean checkObjectWithText(String textToSearch, String positionToCheckForCheckBox, boolean check){
		String tagWithText = "//.[text()='"+textToSearch+"']";
		String checkBoxXPath = tagWithText + positionToCheckForCheckBox; 

		if (check && !driver.findElement(By.xpath(checkBoxXPath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXPath)).click();
		}
		if (!check && driver.findElement(By.xpath(checkBoxXPath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXPath)).click();
		}

		return driver.findElement(By.xpath(checkBoxXPath)).isSelected();

	}

	/**
	 * Checks the checkbox in a certain Position (positionToCheckForCheckBox) before 
	 * a specific tag with a certain text (textToSearch)
	 * @param textToSearch
	 * @param positionToCheckForCheckBox
	 */
	public boolean checkObjectWithText(String textToSearch, String positionToCheckForCheckBox){
		return checkObjectWithText(textToSearch, positionToCheckForCheckBox, true);
	}

	/**
	 * Checks the checkbox in a certain Position (positionToCheckForCheckBox) before 
	 * a specific tag with a certain text (textToSearch)
	 * @param textToSearch
	 * @param positionToCheckForCheckBox
	 */
	public boolean UncheckObjectWithText(String textToSearch, String positionToCheckForCheckBox){
		return checkObjectWithText(textToSearch, positionToCheckForCheckBox, false);
	}


	/**
	 * Un-checks check box. 
	 * @param chkBoxName - check box unique identifier
	 */
	public void uncheckChkBox(String chkBoxName) {
		checkCheckbox(chkBoxName, false);
	}

	/**
	 * A private method to do check/uncheck 
	 * @return
	 */
	protected void checkCheckbox(String checkboxName, boolean enable) {
		String checkBoxXpath = "//input[@type='checkbox' and (@name='" + 
				checkboxName + "' or @id='" + checkboxName + 
				"' or @title='" + checkboxName +  
				"' or @value='" + checkboxName +"' or @title='Select \"" + checkboxName + "\"' or @title='Select " + checkboxName+"' or contains(@title,'"+checkboxName+"'))]" +
				" | //span[.='" + checkboxName + "']/preceding-sibling::input"+
				" | //td[text()='" + checkboxName + "']/preceding-sibling::td/input";

		try {
			WebDriverWait wait = new WebDriverWait(driver, 8); 
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkBoxXpath)));
		} catch (org.openqa.selenium.TimeoutException ex) {
		}
		if ( enable && !driver.findElement(By.xpath(checkBoxXpath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXpath)).click();
		}
		if ( !enable && driver.findElement(By.xpath(checkBoxXpath)).isSelected() ) {
			driver.findElement(By.xpath(checkBoxXpath)).click();
		}
	}


	public  void checkCheckboxes(List<String> checkboxName) {
		for (String item : checkboxName) {
			checkChkBox(item);
		}
	}

	/**
	 * Check box is checked?. 
	 * @param chkBoxName - check box unique identifier
	 */
	public boolean isChecked(String checkboxName) {
		String checkBoxCpath = "//input[@type='checkbox' and (@name='" + 
				checkboxName + "' or @id='" + checkboxName + 
				"' or @title='" + checkboxName +  
				"' or @value='" + checkboxName +"' or @title='Select \"" + checkboxName + "\"')]";
		return driver.findElement(By.xpath(checkBoxCpath)).isSelected();

	}

	/**
	 * Use this method to click on the OK button
	 * @return
	 * @throws Exception 
	 */
	public void clickOK() throws Exception {

		if (WorkingEnvironment.getWebdriverType() == WebDriverType.INTERNET_EXPLORER_DRIVER) {
			sendEnterOnWebElement(btnOK);
			return;
		}
		clickButton("OK");	
	}


	/**
	 * Use this method to click on the OK button
	 * @return
	 * @throws Exception 
	 */
	public void clickCancel() throws Exception {
		if (WorkingEnvironment.getWebdriverType() == WebDriverType.INTERNET_EXPLORER_DRIVER) {
			sendEnterOnWebElement(btnCancel);
			return;
		}
		clickButton("Cancel");	
	}

	/**
	 * Use this method to know whether the current page is the last page or not	
	 * @return true if it's a last page; false otherwise
	 */
	public boolean isLastPage(){
		return waitForElementVisibility(By.xpath("//input[@value='last page']"),20)==null?true:false;
	}

	/**
	 * Use this method to know whether the next page does exist or not.	
	 * @return true if next page does exist;false otherwise
	 */
	public boolean isNextPageExists(){
		return waitForElementVisibility(By.xpath("//input[@value='next page']"),20)!=null?true:false;
	}

	/**
	 * Use this method to know whether the current page is the first page or not	
	 * @return true if it's a first page; false otherwise
	 */
	public boolean isFirstPage(){
		return waitForElementVisibility(By.xpath("//input[@value='first page']"),20)==null?true:false;
	}

	/**
	 * Use this method to navigate to the next page	
	 */
	public void navigateToNextPageInTheTable(){
		if(isNextPageExists())
			clickImage("next page");
		else
			report.report("Next Page doesn't exist in this table", Reporter.FAIL);
	}

	/**
	 * Use this method to navigate to the first page	
	 */
	public void navigateToFirstPageInTheTable(){
		if(isFirstPage()){
			report.report("already in the first page of the table", ReportAttribute.BOLD);
			return;
		}
		else{
			try {
				if(isElementPresent(By.xpath("//input[@value='first page']")))
					clickImage("first page");
			} catch (Exception e) {
				report.report("Failed to navigate to first Page in the table", Reporter.FAIL);
			}
		}
	}

	/**
	 * Use this method to navigate to the last page	
	 */
	public void navigateToLastPageInTheTable(){
		if(isFirstPage()){
			report.report("already in the last page of the table", ReportAttribute.BOLD);
			return;
		}
		else{
			try {
				if(isElementPresent(By.xpath("//input[@value='last page']")))
					clickImage("last page");
			} catch (Exception e) {
				report.report("Failed to navigate to last Page in the table", Reporter.FAIL);
			}
		}
	}

	/**
	 * Clicks a button after verified that it is visible and that you can click it.
	 * @param btnName -unique representer of the button. E.g, unique name or id. 
	 * @throws Exception 
	 */	
	public static void clickButton(String btnName) throws Exception {
		clickButton(btnName, 8);
	}

	/**
	 * Clicks a button after verified that it is visible and that you can click it.This method is to avoid a situation where if we use .sendKeys on button -
	 * the immediate alert is disappearing from screen , hence this click button uses .click() instead of .sendKeys
	 * @param btnName -unique representer of the button. E.g, unique name or id. 
	 * @throws Exception 
	 */	
	public static void clickButtonV2(String btnName) throws Exception {
		String elementLocator = "//button[@value='" + btnName+ "' or text()='" + btnName + "'] | " +
				"//button[contains(text(),'" + btnName + "')] | "+
				"//button[@id='"+ btnName +"'] |" +
				"//input[@type='" + btnName +"'] | "+
				"//input[@value='" + btnName + "'] | //input[contains(@value,'" + btnName+ "')] | //input[(@title='" + btnName + "')] | //input[(@name='" + btnName + "')] | " +
				"//input[@id='" + btnName + "'] | //span[@id='" + btnName + "'] | //span[contains(text(),'" + btnName + "')]/following-sibling::span[@role='img'] | "+ 				
				"//span[contains(text(),'" + btnName + "')][@class='x-btn-inner x-btn-inner-center']";

		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementLocator)));
		} catch (org.openqa.selenium.TimeoutException ex) {
			// ignore exception, return null instead
		}		
		if (element!=null) {
			WebDriverHelper.highlightElement(driver, element);
			if (WorkingEnvironment.getWebdriverType() == WebDriverType.INTERNET_EXPLORER_DRIVER) {
				element.click();
				//sendEnterOnWebElement(element);
				return;
			}
			//element.click();
		} else { 
			throw new Exception ("Web element not found: " + elementLocator); 
		}
	}

	public void clickButtonInGrid(String itemGridName) throws Exception {
		String btnGridXpath = "//a[.='" + itemGridName + "']/../preceding-sibling::td/span//button";
		clickOnElement(ByLocator.xpath, btnGridXpath, 5);
	}

	/**
	 * Clicks a span element with a given text on page
	 * @param text
	 * @throws Exception 
	 */
	public void clickSpanElement(String text) throws Exception {
		String spanXpath = "//span[.='" + text + "']";
		clickOnElement(ByLocator.xpath, spanXpath, 5);
	}

	/**
	 * Clicks a button after verified that it is visible and that you can click it.
	 * @param btnName - unique representer of the button. E.g, unique name or id. 
	 * @param timeoutInSeconds - maxx time in seconds to wait for the button to be visible and clickable.
	 * @throws Exception
	 */
	public static void clickButton(String btnName, long timeoutInSeconds) throws Exception {
		String btnXpath = "//button[@value='" + btnName+ "' or text()='" + btnName + "'] | " +
				"//button[contains(text(),'" + btnName+"') or contains(@class,'"+btnName+"') or starts-with(@class,'"+btnName+"')] | "+
				"//input[@type='" + btnName +"'] | "+
				"//input[@value='" + btnName + "'] | //input[contains(@value,'" + btnName+ "')] | //input[(@title='" + btnName + "')] | //input[(@name='" + btnName + "')] | " +
				"//input[@id='" + btnName + "'] | //span[@id='" + btnName + "'] | //span[contains(text(),'" + btnName + "')]/following-sibling::span[@role='img'] | "+ 				
				"//span[contains(text(),'" + btnName + "')][@class='x-btn-inner x-btn-inner-center']";

		clickOnElement(ByLocator.xpath, btnXpath, timeoutInSeconds);
	}

	/**
	 * Clicks a button which may have different names/id/title on different browsers. i.e, IE, FF.
	 * @param possibleButtnNames - array of possible buttons names
	 * @param timeoutInSeconds - time out in seconds to wait for the button to show  before giving up.
	 * @throws Exception
	 */
	public static void clickButton(String[] possibleButtnNames, long timeoutInSeconds) throws Exception {
		boolean buttonFound = false;
		int loop = possibleButtnNames.length - 1;
		while (loop >= 0 && buttonFound == false){
			try {
				clickButton(possibleButtnNames[loop--],timeoutInSeconds );
				buttonFound = true;
			} catch (Exception ex) {
				// ignore
			}
		}
		if (buttonFound == false) {
			throw new Exception ("Button not found: " + possibleButtnNames.toString()); 
		}
	}

	/**
	 * Clicks a button which may have different names/id/title on different browsers. i.e, IE, FF.
	 * @param possibleButtnNames - array of possible buttons names
	 * @param timeoutInSeconds - time out in seconds to wait for the button to show  before giving up.
	 * @throws Exception
	 */
	public static void clickButton(String[] possibleButtnNames) throws Exception {
		clickButton(possibleButtnNames, DefaultTimeOutInSeconds);
	}

	/**
	 * Wait for button to be visible and clickable
	 * @param btnName - unique representer of the button. E.g, unique name or id. 
	 * @param timeoutInSeconds - max time in seconds to wait for the button to be visible and clickable.
	 * @throws Exception
	 */
	public static void waitForButtonToBeClickable(String btnName, long timeoutInSeconds) throws Exception {
		String btnXpath = "//button[@value='" + btnName+ "' or text()='" + btnName + "'] | " +
				"//input[@value='" + btnName + "'] | //input[(@name='" + btnName + "')] | " +
				"//input[@id='" + btnName + "'] | //span[@id='" + btnName + "'] | //span[contains(text(),'" + btnName + "')]/following-sibling::span[@role='img'] | "+ 				
				"//span[contains(text(),'" + btnName + "')][@class='x-btn-inner x-btn-inner-center']";

		waitForElementToBeClickable(ByLocator.xpath, btnXpath, timeoutInSeconds);
	}

	private static void waitForElementToBeClickable(ByLocator by, String elementLocator,long timeoutInSeconds)throws Exception{
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		switch (by) {
		case xpath:	
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		default:
			break;
		}		
		if (element == null) {
			throw new Exception ("Web element not found: " + elementLocator); 
		}
	}

	public static void clickOnElement(ByLocator by, String elementLocator,long timeoutInSeconds) throws Exception {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		switch (by) {
		case xpath:			
			try {
				element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementLocator)));
			} catch (org.openqa.selenium.TimeoutException ex) {
				// ignore exception, return null instead
			}
			break;
		default:
			break;
		}		
		if (element!=null) {
			WebDriverHelper.highlightElement(driver, element);

			if (WorkingEnvironment.getWebdriverType() == WebDriverType.INTERNET_EXPLORER_DRIVER) {
				//element.click();
				sendEnterOnWebElement(element);
				return;
			}
			element.click();
		} else { 
			throw new Exception ("Web element not found: " + elementLocator); 
		}
	}


	/**
	 * Use this method to click on an image by title or alt attribute
	 * @param imageIdentifier - make sure it represents unique identifier of the image on the page. 
	 */	
	public void clickImage(String imageIdentifier) {
		String imgXpath = "//input[@type='image'][@alt='" + imageIdentifier + "' or @title='" + imageIdentifier + "'] | " +
				" //input[@type='image'][contains(@alt,'" + imageIdentifier + "')]";
		WebDriverWait wait = new WebDriverWait(driver, 3); // for links that become enable by client-side js
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(imgXpath)));
		WebElement button = driver.findElement(By.xpath(imgXpath));
		WebDriverHelper.highlightElement(driver, button);

		if(WorkingEnvironment.getWebdriverType() == WebDriverType.INTERNET_EXPLORER_DRIVER){
			sendEnterOnWebElement(button);
			return;
		}
		button.click();
	}
	/**
	 * Use this method for special case for IE9, where button is enable BUT click yield nothing
	 * @param element to be clicked on
	 */
	private static void sendEnterOnWebElement(WebElement element){
		try {
			int count = 0 ;
			while (element.isEnabled() && count < 5){//Special case for IE9 
				report.report("Sending Enter to WebElement. Attempt Number: "+count);
				element.sendKeys(Keys.ENTER);
				count ++ ;
				Thread.sleep(2000);
				try{
				Alert alert = driver.switchTo().alert();
				if(alert.getText().contains("I am still processing"))
					alert.accept();
				}
				catch(Exception e)
				{
					//ignoring exception 
				}
			}
		} catch (Exception e) {
		}	
	}

	/**
	 * Clicks link by link text, looking for the whole text.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void clickLink(String linkText) throws Exception {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(linkText);
		linkText = (lnkText!=null)?lnkText:linkText;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link;
		if (lnkText == null) {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
		} else {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.="+linkText+"]")));
		}

		if (link == null ) {
			report.report("debug:: link wasn't found. try to locate with CSS locator ....");
			link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains(^"+linkText+"$)")));
		}

		if (link!=null){
			WebDriverHelper.highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + linkText);
		}
	}

	/**
	 * Clicks link by link text, looking for the whole text.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void SearchAllPagesAndclickLink(String linkText) throws Exception {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(linkText);
		linkText = (lnkText!=null)?lnkText:linkText;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link=null;
		boolean nextpage=true;
		do{		
			if (lnkText == null) {
				try{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			else {
				try{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.=concat(" + linkText + ")]")));
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (link == null ) {
				report.report("debug:: link wasn't found. try to locate with CSS locator ....");
				try{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains(^"+linkText+"$)")));
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(link!=null)
				break;
			else if(isElementPresent(By.xpath("//input[@title='next page']"))){
				driver.findElement(By.xpath("//input[@title='next page']")).click();
			}
			else{
				nextpage=false;
				break;
			}

		}while(nextpage);

		if (link!=null){
			WebDriverHelper.highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + linkText);
		}
	}

	/**
	 * Clicks link by link text, looking for the whole text.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void SearchAllPagesAndSelectRoleCheckBox(String adminRole) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link=null;
		boolean nextpage=true;
		do{		
			try{
				link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='checkbox'][@title='Select \""+adminRole+"\"']")));
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			if(link!=null)
				break;
			else if(isElementPresent(By.xpath("//input[@title='next page']"))){
				driver.findElement(By.xpath("//input[@title='next page']")).click();
			}
			else{
				nextpage=false;
				break;
			}

		}while(nextpage);

		if (link!=null){
			WebDriverHelper.highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + adminRole);
		}
	}

	/**
	 * Clicks link by link text, looking for the text to be contained.
	 * @param linkText
	 * @throws Exception 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	public void clickLinkPartialText(String partialLinkText) throws Exception {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(partialLinkText);
		partialLinkText = (lnkText!=null)?lnkText:partialLinkText;

		WebDriverWait wait = new WebDriverWait(driver, 10); // for links that become enable by client-side js
		WebElement link;
		if (lnkText == null) {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(partialLinkText)));
		} else {
			link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.,concat(" + partialLinkText + ")]")));
		}
		if (link == null ) {
			report.report("debug:: partially link  text wasn't found. try to locate with CSS locator ....");
			link = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a:contains("+partialLinkText+")")));
		}

		if (link!=null){
			WebDriverHelper.highlightElement(driver, link);
			link.click();
		} else {
			throw new  Exception("Failed to find link text: " + partialLinkText);
		}
	}


	/**
	 * Clicks link by link text ONLY if it is exist ,otherwise do nothing
	 * @param linkText
	 */
	public boolean clickLinkOnlyIfExists(String linkText) {
		String lnkText = reconstructStringWithSingleQuoteForXpathConcatFunction(linkText);
		linkText = (lnkText!=null && !linkText.startsWith("/"))?lnkText:linkText;

		boolean found = true ;
		WebElement link = null;
		WebDriverWait wait = new WebDriverWait(driver, shortTimeOutInSeconds);
		try {
			if (lnkText == null) {
				link = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
			} else {
				if (linkText.startsWith("/")) // link text is actually an XPATH
				{
					link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(linkText)));
				} else {
					link = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.=concat(" + linkText + ")]")));
				}				
			}
		} catch (org.openqa.selenium.TimeoutException ex) {
			found = false;
		} catch (org.openqa.selenium.NoSuchElementException ex2) {
			//ignore
		}
		if (link!=null) {
			WebDriverHelper.highlightElement(driver, link);
			link.click();
		}
		return found;

	}

	/**
	 * private method to be used when text contains signle quote signs.
	 * The methods returns the string in a different form to be used with the Xpath concat function 
	 * @param str - the string
	 * @return string in a form to be used with the Xpath concat function 
	 */
	private String reconstructStringWithSingleQuoteForXpathConcatFunction(String str) {
		StringBuilder sb = null;
		if (str.contains("'")) {
			String[] arr = str.split("'");
			sb = new StringBuilder();
			for (int i =0; i < arr.length-1; i++) {
				sb.append("'" + arr[i] + "'");
				sb.append(",");
				sb.append("\"'\"");
				sb.append(",");
			}
			sb.append("'" + arr[arr.length-1] + "'");
		}

		return (sb == null)?null:sb.toString();
	}

	/**
	 * Use this method to type a text in edit box
	 * @param editBoxName
	 * @param textToType
	 */
	public void typeEditBox(String editBoxName, String textToType) throws Exception{
		String xpath = "//input[@name='" + editBoxName + "' or " +
				"@title='" + editBoxName + "' or " +
				"@id='"+ editBoxName + "' or " +
				"@type='" + editBoxName + "' ] | //textarea[@name='" + editBoxName + "' or "+" @id='"+ editBoxName + "'] | //td[contains(text(),'"+ editBoxName + "')]/input[@type='text'] |  //table[@id = '"+ editBoxName + "']//input[@type='text'] ";
		int count = 0 ;
		waitForElementVisibility(By.xpath(xpath));
		enterTextToField (xpath, textToType); 
		if (!driver.findElement(By.xpath(xpath)).getAttribute("value").contains(textToType) & count < 3){
			enterTextToField (xpath, textToType); 
		}
	}
	/**
	 * Use this method to type a text in edit box
	 * @param WebElement
	 * @param textToType
	 */
	public void typeEditBoxByWebElement(WebElement editBox, String textToType) throws Exception{
		int count = 0 ;
		editBox.clear();
		editBox.sendKeys(textToType);
		if (!editBox.getAttribute("value").contains(textToType) & count < 3){
			editBox.clear();
			editBox.sendKeys(textToType); 
		}
	}
	/**
	 * Use this method to upload a file
	 * @param editBoxName
	 * @param textToType
	 */
	public void uploadFile(String fileInputElementName,  String filePath)throws Exception{
		String xpath = "//input[@name='" + fileInputElementName + "' or " +
				"@title='" + fileInputElementName + "' or " +
				"@id='"+ fileInputElementName + "']";
		int count = 0 ;
		WebElement editBox = driver.findElement(By.xpath(xpath));
		editBox.sendKeys(filePath);
		if (!driver.findElement(By.xpath(xpath)).getAttribute("value").contains(filePath) & count < 3){
			editBox.sendKeys(filePath); 
		}
	}

	/**
	 * Use this method to upload a file
	 * @param editBoxName
	 * @param textToType
	 */
	public void uploadImageFile(String fileInputElementName,  String filePath)throws Exception{
		String xpath = "//input[@name='" + fileInputElementName + "' or " +
				"@title='" + fileInputElementName + "' or " +
				"@id='"+ fileInputElementName + "']";
		int count = 0 ;
		WebElement editBox = driver.findElement(By.xpath(xpath));
		editBox.sendKeys(filePath);		
	}

	/**
	 * Used to type something inside a text edit box 
	 * @param xpath
	 * @param textToType
	 */
	private void enterTextToField(String xpath , String textToType) {
		WebElement editBox = driver.findElement(By.xpath(xpath));
		WebDriverHelper.highlightElement(driver, editBox);
		editBox.clear();
		editBox.sendKeys(textToType);
	}

	protected void clearInputByClickingDelete(WebElement webElement) {	    
		while (!StringUtils.isEmpty(webElement.getAttribute("value"))) {
			// "\u0008" - is backspace char
			webElement.sendKeys("\u0008");
		}
	}
	/**
	 * Use this method to select from drop-down list
	 * @param selectNameOrID - set the name or id of the desired select tag
	 * @param valueToSelect - the value to select from the drop-down list
	 */
	public void selectByNameOrID(String selectNameOrID, String valueToSelect) {
		/* WebElement we = waitForElementVisibility(By.xpath("//select[contains(@name,'" + selectNameOrID + "') or " +
				 										  "contains(@id,'"+ selectNameOrID + "') or " + "contains(@title,'" + selectNameOrID + "')]"));
		 */ 
		//added by bodna02 to handle a select element if it is inside a span tag when a name / id couldn't able to identify
		WebElement we = waitForElementVisibility(By.xpath("//select[contains(@name,'" + selectNameOrID + "') or " + 
				"contains(@id,'"+ selectNameOrID + "') or " + "contains(@title,'" + selectNameOrID + "')] | " + 
				"//span[@id='"+ selectNameOrID +"']/select | " + "//td[span[contains(@title,"+"'"+ selectNameOrID +"'"+")]]/following-sibling::td/select | " + "//td[contains(text(),"+"'"+ selectNameOrID +"'"+")]/select"));

		WebDriverHelper.highlightElement(driver, we);
		setSelectedField(we, valueToSelect);
		// sendEnterOnWebElement(we);

		try {

			if (we.getAttribute("onchange")!= null){
				report.report("firing 'onchange' event");
				((JavascriptExecutor) driver).executeScript(we.getAttribute("onchange")); 
				// Thread.sleep(2000);
			}

			String handler = we.getAttribute("handler");
			if (handler!=null) {
				report.report("firing 'onchange' event");
				handler = handler.substring(handler.indexOf('{')+1, handler.lastIndexOf('}')-1);
				((JavascriptExecutor) driver).executeScript(handler); 

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Use this method to select from drop-down list using WebElement parameter
	 * @param WebElement of the select tag
	 * @param optionToSelect
	 */
	public void selectByWebElement(WebElement selectTag, String optionToSelect) {
		WebDriverHelper.highlightElement(driver, selectTag);
		setSelectedField(selectTag, optionToSelect);

		try {
			if (selectTag.getAttribute("onchange")!= null){
				((JavascriptExecutor) driver).executeScript(selectTag.getAttribute("onchange")); 
			}
			String handler = selectTag.getAttribute("handler");
			if (handler!=null) {
				report.report("firing 'onchange' event");
				handler = handler.substring(handler.indexOf('{')+1, handler.lastIndexOf('}')-1);
				((JavascriptExecutor) driver).executeScript(handler); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Use this method to select from radio buttons options
	 * @param radioNameOrID - set the name or id of the desired select tag
	 * @param valueToSelect - If value is dynamic set to NA
	 */
	public void selectRadioButton(String radioNameOrID, String valueToSelect) {
		String xpath = "";
		if (valueToSelect.contains("NA")){
			xpath = "//input[@type='radio'][(@name='" + radioNameOrID + "' or " + "@id='"+ radioNameOrID + "' or "+ "@value='" + radioNameOrID + "' or " +
					"@title[contains(.,'"+ radioNameOrID + "')]" + " or "+ "@title='" + "Select " + "\"" + radioNameOrID + "\"" + "')]";
		}else {
			xpath = "//input[@type='radio'][(@name='" + radioNameOrID + "' or " + "@id='"+ radioNameOrID + "' or "+ 
					"@title[contains(.,'"+ radioNameOrID + "')]" + " or " + "@title='" + "Select " + "\"" + radioNameOrID + "\"" + "')  and @value='" + valueToSelect + "']";
		}

		waitForElementVisibility(By.xpath(xpath));
		driver.findElement(By.xpath(xpath)).click();
	}
	/**
	 * Use this method to wait explicitly for element
	 * @param locator
	 * @param timeout in seconds
	 *//*
    public void waitFor(By locator, int timeout){
    	WebDriverWait wait = new WebDriverWait(driver, timeout); 
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
	  */
	/**
	 * Use this method move to generic IM tab
	 * @param tabName
	 */
	public void switchToTab(String tabName)throws Exception {
		waitForElementVisibility(By.xpath("//input[@value='" + tabName + "']"));
		WebElement input = driver.findElement(By.xpath("//input[@value='" + tabName + "']"));
		WebDriverHelper.highlightElement(driver, input);
		input.click();
	}
	private void setSelectedField(WebElement selectWebElement, String value) {
		Select dropDown = new Select(selectWebElement);
		try {
			dropDown.selectByVisibleText(value);
		} catch (Exception ex) {

			dropDown.selectByValue(value);
		} 
	}


	/**
	 * Verifies text exists in table
	 * @param text - text to verify
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isTextExistInTable(String text) throws Exception{
		return isTextExistInTable(text, DefaultTimeOutInSeconds);
	}


	/**
	 * Verifies text exists in table
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isTextExistInTable(String text, long waitTimeInSeconds) throws Exception {
		String xpath = "//td[text() ='"+text+"'] | //td/span[text() ='"+text+"']" ;
		WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds); // for links that become enable by client-side js
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		}
		catch(Exception e){
			return false;
		}
		return (driver.findElements(By.xpath(xpath)).size() > 0);
	}

	/**
	 * Takes the column name as input and returns all the values in that column as string array.
	 * @param ColName
	 * @return
	 * @throws Exception
	 */
	public List<String> getColumnValuesFromTable(String ColName) throws Exception {
		String XpathCol="//th[(text())] | //th/input";

		//get the column number with the help of column name
		List<WebElement> colnamelist=driver.findElements(By.xpath(XpathCol));
		int colnum=1;
		for (WebElement el:colnamelist)
		{
			if((el.getText()!=null) && (el.getText().trim().equalsIgnoreCase(ColName)))
			{
				report.report("The column numver is "+ colnum);
				break;
			}
			else if ((el.getAttribute("value")!=null) && (el.getAttribute("value").trim().equalsIgnoreCase(ColName)))
			{
				report.report("The column number is "+ colnum);
				break;
			}
			else
			{
				colnum++;
				report.report("The column number is " + colnum);
			}
		}
		//after getting the column number get the list of element and return the strin array
		String xpath="//td["+colnum+"]";
		List<WebElement> colvaluelist=driver.findElements(By.xpath(xpath));
		List<String> colvalues=new ArrayList<String>();
		for (WebElement el:colvaluelist)
		{
			colvalues.add(el.getText().trim());
		}
		return colvalues;
	}

	/**
	 * Verifies text exists in table
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isChckboxNameExistInTable(String text, long waitTimeInSeconds) throws Exception {
		String xpath = 	"//input[@title='Select \""+ text +"\"']";
		WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds); // for links that become enable by client-side js
		try{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		}
		catch(Exception e){
			return false;
		}
		return (driver.findElements(By.xpath(xpath)).size() > 0);
	}

	/**
	 * Verifies if any option is available in Select Box
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public boolean isOptionExistInSelect(String selectID) throws Exception {
		String xpath = "//select[@id='"+selectID+"']/option";
		//int num = driver.findElements(By.xpath(xpath)).size();
		if(driver.findElements(By.xpath(xpath)).size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Verifies if any option is available in Select Box
	 * @param text - text to verify
	 * @param waitTimeInSeconds - seconds to wait for text to show before giving up.
	 * @return true if found, false otherwise
	 * @throws Exception
	 */
	public void selectFirstOptionInSelect(String selectID) throws Exception {
		String xpath = "//select[@id='"+selectID+"']/option";
		try{
			driver.findElement(By.xpath(xpath)).click();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Use this method to verify if a text is being displayed somewhere on the active page.
	 * @param text
	 * @return
	 * @throws Exception 
	 */
	public static boolean isTextPresent(String text) throws Exception{
		return driver.getPageSource().contains(text);
		//return (driver.findElements(By.xpath("//.[.='"+ text +"']")).size() > 0);
	}

	/**
	 * Wait for text to present within a given element
	 * @param text
	 * @return true if text was found and false otherwise.
	 * @throws Exception 
	 */
	public boolean waitForTextVisibility(ByLocator by, String elementLocator, String text) throws Exception {
		return waitForTextVisibility(by, elementLocator, text, DefaultTimeOutInSeconds);
	}

	public boolean waitForTextVisibility(ByLocator by, String elementLocator, String text, long timeoutInSeconds) throws Exception {
		WebElement webElement = null;
		switch (by) {
		case xpath:
			webElement = waitForElementVisibility(By.xpath(elementLocator + "[contains(text(),\""+ text +"\")]"), timeoutInSeconds);
			break;
		case classname:
		case css:
		case linktext:
		case id:
		case name:
		case title:
			report.report("Currently waitForTextVisibility method doesn't support this locator type: " + by.name(), Reporter.FAIL);
		}
		return (webElement !=null)?true:false;
	}

	public void switchUrl(String url) {
		initElements();
		if (!driver.getCurrentUrl().toLowerCase().contains(url.toLowerCase())){
			driver.get(url);
		}
	}

	/**
	 * Looks for an element on the active web page
	 * @param elementIdentifier - provide name, id ,XPath or CSS
	 * @param args - provide arguments for xpath or css if needed
	 * @return true if the element was found, or false otherwise.
	 * @throws Exception
	 */
	public static boolean isElementPresent(By by)throws Exception{
		return driver.findElements(by).size()>0?true:false;
	}

	/**
	 * wait until element found is displayed on screen, or <code>MaxTimeOutInSeconds</code> exceeded.
	 * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
	 * @param by - WebDriver By object. For example: By.cssLoccator("..")
	 * @return WebElement object if found, and null if was not found
	 */
	public WebElement waitForElementVisibility(By by) {
		return waitForElementVisibility(by, DefaultTimeOutInSeconds);
	}


	/**
	 * wait until element found on screen , or <code>MaxTimeOutInSeconds</code> exceeded.
	 * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
	 * @throws Exception
	 * @return WebElement object if found, and null if was not found
	 */
	public WebElement waitForElementVisibility(By by, long timeOutInSeconds){
		WebElement webElement = null;
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		try {
			webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (org.openqa.selenium.TimeoutException ex) {
			// ignore exception, return null instead
		}
		return webElement;
	}

	/**
	 * Use this method to type a text in edit box
	 * @param editBoxName
	 * @param textToType
	 */
	public String getEditBoxValue(String editBoxName) throws Exception{
		String xpath = "//input[@name='" + editBoxName + "' or " +
				"@title='" + editBoxName + "' or " +
				"@id='"+ editBoxName + "'] | //textarea[@name='" + editBoxName + "' or @id='"+ editBoxName + "']";

		waitForElementVisibility(By.xpath(xpath));

		return driver.findElement(By.xpath(xpath)).getAttribute("value"); 
	}

	/**
	 * Use this method to get the text in an edit box
	 * @param editBoxName
	 * @param textToType
	 */
	public String getEditBoxText(String editBoxName) throws Exception{
		String xpath = "//input[@name='" + editBoxName + "' or " +
				"@title='" + editBoxName + "' or " +
				"@id='"+ editBoxName + "'] | //textarea[@name='" + editBoxName + "' or @id='"+ editBoxName + "']";

		waitForElementVisibility(By.xpath(xpath));

		return driver.findElement(By.xpath(xpath)).getText().trim(); 
	}
	/**
	 * Use this method to get the current state of a check box
	 * @param checkboxName
	 * @return
	 */
	public boolean getCheckBoxState(String checkboxName){

		String checkBoxXpath = "//input[@type='checkbox' and (@name='" + 
				checkboxName + "' or @id='" + checkboxName + 
				"' or @title='" + checkboxName +  
				"' or @value='" + checkboxName +"' or @title='Select \"" + checkboxName + "\"')]" +
				" | //span[.='" + checkboxName + "']/preceding-sibling::input";

		try  {
			WebDriverWait wait = new WebDriverWait(driver, 8); 
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkBoxXpath)));
		} 
		catch (org.openqa.selenium.TimeoutException ex) {
		}
		return driver.findElement(By.xpath(checkBoxXpath)).isSelected();
	}


	/**
	 * This method lets you test for the existence of an element.
	 * 
	 * @param css = css selector text
	 * @return TRUE if css element isDisplayed, false with no error if not
	 */
	public boolean tryByCss(String css) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(css)));
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)));
		return true;
	}

	/**
	 * This method lets you test for the existence of an element.
	 * 
	 * @param xpath = xpath selector text
	 * @return TRUE if xpath element isDisplayed, false with no error if not
	 */
	public boolean tryByXpath(String xpath) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		return true;
	}

	/**
	 * This method lets you test for the existence of an element.
	 * 
	 * @param text = link text selector text
	 * @return TRUE if link text element isDisplayed, false with no error if not
	 */
	public boolean tryByLinkText(String text) {
		try {
			return driver.findElement(By.linkText(text)).isDisplayed();
		} catch (NoSuchElementException e) {
			report.report("Xpath element not found: " + text);
			return false;
		}
	}

	public boolean isThrobberDisplayed() {
		List<WebElement> throbbers = driver.findElements(By.cssSelector(".x-mask-msg-inner"));
		int throbberSize = throbbers.size();
		// there can be several on a page, so we have to check them all
		if (throbbers.isEmpty()) {
			return false;
		} else {
			for (int i=0; i < throbberSize; i++) {
				if (throbbers.get(i).isDisplayed()) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * This method used WebDriver User Actions builder to move the
	 * mouse pointer to an element and then click.  This can be a
	 * useful alternative in cases where WebDriver complains that an
	 * element is not accessible.
	 * 
	 * @param element A WebElement
	 */
	public void moveToElement(WebElement element) {
		Actions builder = new Actions(driver);
		Action moveAndClick = builder.moveToElement(element).build();
		moveAndClick.perform();	
	}
	public void moveToElement(String sElementText){
		WebElement we = waitForElementVisibility(By.linkText(sElementText));
		moveToElement(we);
		//moveToElementAndClick(we);
	}
	public void moveByOffset(String sElementText){
		WebElement we = waitForElementVisibility(By.linkText(sElementText), 60);
		Actions builder = new Actions(driver);
		Action moveAndClick = builder.moveToElement(we, -10, 0).build();
		moveAndClick.perform();
	}
	
	public void moveToElementAndClick(WebElement element) {
		Actions builder = new Actions(driver);
		Action moveAndClick = builder.moveToElement(element)
				.click()
				.build();
		moveAndClick.perform();	
	}

	public void moveToElementAndClickCss(String cssString) {
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssString)));
		new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssString)));
		Actions builder = new Actions(driver);
		Action moveAndClick = builder.moveToElement(driver.findElement(By.cssSelector(cssString)))
				.click()
				.click()
				.click()
				.build();
		moveAndClick.perform();	
	}

	public void moveToElementAndClickXpath(String xpathString) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathString)));
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath(xpathString)));
		Actions builder = new Actions(driver);
		Action moveAndClick = builder.moveToElement(driver.findElement(By.xpath(xpathString)))
				.click()
				.build();
		moveAndClick.perform();	
	}


	public void searchIcon(){
		String xpath = "//./[@class(contains(text,'x-btn icons-magnify-small x-unselectable x-box-item x-btn-default-small'))]";
	}

	public void setFilter(String filterAttribute,
			String filterOper, String filterValue) throws Exception{
		selectByNameOrID("Filter.0.Field", filterAttribute);
		selectByNameOrID("Filter.0.Op", filterOper);
		typeEditBox("Filter.0.Value", filterValue);
	}

	public String getCurrentTab(){
		WebElement currenttab;
		try {
			if(isElementPresent(By.cssSelector(".tab-open"))){
				currenttab=driver.findElement(By.xpath("//li[@class='tab-open']//span/input"));
				if(currenttab!=null)
					return currenttab.getAttribute("value");
			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Handle alerts :: this method reads an alert text from test method and compares with the actuals
	 * Nageswar.Bodduri
	 */
	public boolean verifyAlert(String sExpected){

		String sActual=null;
		try{
			//Wait 10 seconds till alert is present
			report.report("Inside verify alert method....");
			WebDriverWait wait = new WebDriverWait(driver, 5);
			//Alert alert = driver.switchTo().alert();
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			sActual = alert.getText().toString();
			report.report("Expected text on alert box is :" +sActual);
			if( sActual.equalsIgnoreCase(sExpected)){
				report.report("Actual text on alert box is : "+ sExpected);
				alert.accept();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * get main window handle
	 * Nageswar.Bodduri
	 */
	public String returnMainWindowHandle(){
		return driver.getWindowHandle();
	}

	/**
	 * get current active window handle
	 * Nageswar.Bodduri
	 */
	public void returnCurrentWindowHandle(String mainWindowHandle){
		Set windowHandles=driver.getWindowHandles();
		Iterator ite=windowHandles.iterator();

		while(ite.hasNext())
		{
			String popupHandle=ite.next().toString();
			if(!popupHandle.contains(mainWindowHandle))
			{
				driver.switchTo().window(popupHandle);
			}
		}

	}

	public String getElementText(By by){
		WebElement element = waitForElementVisibility(by);
		return getElementText(element);
	}

	public String getElementText(WebElement e){
		return e.getText();
	}

	public void safeJavaScriptClick(String linktext) throws Exception {
		WebElement element = waitForElementVisibility(By.linkText(linktext));
		safeJavaScriptClick(element);
	}

	/*Click the element with java script*/
	public static void safeJavaScriptClick(WebElement element) throws Exception {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				System.out.println("Clicking on element using java script click");

				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else {
				System.out.println("Unable to click on element");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document "+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element was not found in DOM "+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to click on element "+ e.getStackTrace());
		}
	}

	/**
	 * This method is to return the xpath for alert option under My account > setup alerts task
	 * Nageswar.Bodduri
	 */
	public String getAlertOptionXpath(String alertOption){

		StringBuilder sLabelXpath = new StringBuilder("//span[contains(text(),'");
		String[] alert = alertOption.split(" ");
		int j = 0;
		while( j < alert.length){
			if( j == alert.length - 1){
				sLabelXpath.append(alert[j] +"')]");
			}else{
				sLabelXpath.append(alert[j] +"') and contains(text(), '");
			}
			j++;
		}
		return sLabelXpath.toString();
	}

	/**
	 * Clicks a Link : when you can not locate any anchor element by its text.Use this method to locate an anchor tag with its id or name
	 * You can add other possible ways to identify an anchor tag without link text to existing xpath
	 * @author nageswar.bodduri
	 */
	public void clickLinkV2(String linkLocator)throws Exception{

		String elementLocator = "//a[@id='" + linkLocator+ "'] | " + " //a[@name='" + linkLocator +"']";
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 10);

		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementLocator)));
		} catch (org.openqa.selenium.TimeoutException ex) {
			// ignore exception, return null instead
		}		
		if (element!=null) {
			WebDriverHelper.highlightElement(driver, element);
			if (WorkingEnvironment.getWebdriverType() == WebDriverType.INTERNET_EXPLORER_DRIVER) {
				//element.click();
				sendEnterOnWebElement(element);
				return;
			}else{
				element.click();
			}

		} else { 
			throw new Exception ("Web element not found: " + elementLocator); 
		}

	}

	/**
	 * Get all attributes of a HTML element using java script
	 * @author nageswar.bodduri
	 */

	@SuppressWarnings("unchecked")
	public String[] getAllAttributes(WebElement element)throws Exception{

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String attrs[] = null ;
		List<Object> response = null;


		StringBuilder sJScript = new StringBuilder();
		sJScript.append("var x = document.getElementsByClassName(\"ub65 defaultText upper ub04changed\");\n");
		sJScript.append("var i = 0; \n");
		sJScript.append("var arr = []; \n");
		sJScript.append("var attrs = x[0].attributes;\n");
		sJScript.append("for (i = 0; i < attrs.length; i++) {\n");
		sJScript.append(" arr.push(attrs[i].name);\n");
		sJScript.append("}\n");
		sJScript.append("return arr;\n");

		if (driver instanceof JavascriptExecutor) {

			if( response == null)
				while(response != null) {
					response = (List<Object>) jsExecutor.executeScript(sJScript.toString());
					report.report("Executing javascript....");
				}
		}
		report.report("Inside getAllAttributes Method..." + response.toString());
		return attrs;
	}

	public List<WebElement> findElements(By by){
		if (waitForElementVisibility(by)!=null)
			return driver.findElements(by);
		return null;
	}

	//This method will return corresponding value of the UI Attribute displayName passed from JSYSTEM
	public static String getValueFromJSystem(List<Attribute> lsAttributes, String displayName){
		String sValueFromJsystem = null;
		for(Attribute scrAttr:lsAttributes){
			if(scrAttr.getDisplayName().equalsIgnoreCase(displayName)){
				sValueFromJsystem = scrAttr.getValue();
				break;
			}
		}
		return sValueFromJsystem;
	}

	//#########################################   Getters & Setters ###################################################
	/**
	 * 
	 * @return the driver that initialized this object
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * set driver object
	 * @param driver
	 */
	public  void setDriver(WebDriver _driver) {
		driver = _driver;
	}

	public boolean isClearCookiesBeforeOpen() {
		return clearCookiesBeforeOpen;
	}

	public void setClearCookiesBeforeOpen(boolean clearCookiesBeforeOpen) {
		this.clearCookiesBeforeOpen = clearCookiesBeforeOpen;
	}

	@Override
	public void update(Observable o, Object arg) {
		driver.quit();
	}


}

