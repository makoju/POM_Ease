package com.ability.ease.auto.systemobjects;


import jsystem.framework.IgnoreMethod;
import jsystem.framework.system.SystemObjectImpl;

import com.ability.ease.auto.enums.portal.selenium.WebDriverType;
import com.ability.ease.auto.events.ui.UIEvents;


public class WebDriverSystemObject  extends SystemObjectImpl {


	/**
	 * the type of the browser to be open. e.g. IE/Chrome//FF
	 */
	protected WebDriverType webDriverType;
	/**
	 * in case of true will not open the browser when init the system object.
	 */
	protected boolean lazyInit = false;

	protected String firefoxBrowserPath;// browser path ,e.g.// c:/...../Firefox.exe
	protected String chromeFlags = null;
	protected boolean windowMaximize = true;
	protected String chromeProfile = null;
	protected String chromeExtension = null;
	protected String firefoxExtension = null;
	protected String firefoxProfile = null;
	protected String screenShotPath;
	protected String screenShotFolderName = "Screenshots";
	protected String seleniumTimeOut = "30000";
	protected boolean ignoreCertificateErrors = false;
	
	@Override
	public void close() {
		UIEvents.getInstance().closeBrowser();
		
	/*	report.report("Closing WebDriver System Object");
		try {
			closeBrowserInstance();
		} catch (Exception e) {
			report.report("error in closing WebDriver System Object,Error=" + e.getMessage());
		} finally {
			pageKeeper = null;
		}*/
	}


	/**
	 * User and system web driver event handler classes Init from the SUT -
	 * classes that will be loaded by Reflection API (class loader) this String
	 * should contains the full class name of the class implementation if there
	 * is more than one class the string should be this ':' char as separator
	 * (like reporter in jsystem.propreties)
	 * 
	 * 
	 * @see WebDriverScreenshotEventHandler
	 * @see WebDriverReportEventHandler
	 * @see WebDriverEventListener
	 */

	private boolean clearCookiesBeforeOpen = false;

	public void init() throws Exception {
		super.init();
	}


	public String getScreenShotPath() {
		return screenShotPath;
	}

	@IgnoreMethod
	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}

	public String getSeleniumTimeOut() {
		return seleniumTimeOut;
	}

	@IgnoreMethod
	public void setSeleniumTimeOut(String timeout) {
		this.seleniumTimeOut = timeout;
	}

	public String getScreenShotFolderName() {
		return screenShotFolderName;
	}

	@IgnoreMethod
	public void setScreenShotFolderName(String screenShotFolderName) {
		this.screenShotFolderName = screenShotFolderName;
	}

	public String getFirefoxBrowserPath() {
		return firefoxBrowserPath;
	}

	@IgnoreMethod
	public void setFirefoxBrowserPath(String browserPath) {
		this.firefoxBrowserPath = browserPath;
	}

	public String getChromeFlags() {
		return chromeFlags;
	}

	@IgnoreMethod
	public void setChromeFlags(String chromeFlags) {
		this.chromeFlags = chromeFlags;
	}

	public boolean isWindowMaximize() {
		return windowMaximize;
	}

	@IgnoreMethod
	public void setWindowMaximize(boolean windowMaximize) {
		this.windowMaximize = windowMaximize;
	}

	public String getChromeProfile() {
		return chromeProfile;
	}

	@IgnoreMethod
	public void setChromeProfile(String chromeProfile) {
		this.chromeProfile = chromeProfile;
	}

	public String getChromeExtension() {
		return chromeExtension;
	}

	@IgnoreMethod
	public void setChromeExtension(String chromeExtension) {
		this.chromeExtension = chromeExtension;
	}

	public String getFirefoxExtension() {
		return firefoxExtension;
	}

	@IgnoreMethod
	public void setFirefoxExtension(String firefoxExtension) {
		this.firefoxExtension = firefoxExtension;
	}

	public String getFirefoxProfile() {
		return firefoxProfile;
	}

	@IgnoreMethod
	public void setFirefoxProfile(String firefoxProfile) {
		this.firefoxProfile = firefoxProfile;
	}

	public boolean isIgnoreCertificateErrors() {
		return ignoreCertificateErrors;
	}

	@IgnoreMethod
	public void setIgnoreCertificateErrors(boolean ignoreCertificateErrors) {
		this.ignoreCertificateErrors = ignoreCertificateErrors;
	}

	public WebDriverType getWebDriverType() {
		return webDriverType;
	}

	public void setWebDriverType(WebDriverType webDriverType) {
		this.webDriverType = webDriverType;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	@IgnoreMethod
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	/**
	 * @return True if and only if the driver set to clear cookies inside
	 *         {@link #openBrowser()} method
	 */
	public boolean isClearCookiesBeforeOpen() {
		return clearCookiesBeforeOpen;
	}

	/**
	 * The WebDriverSystemObject can delete all cookies before navigating to the
	 * domain (in {@link #openBrowser()} and {@link #init()} methods)
	 * 
	 * @param clearCookiesBeforeOpen
	 * 
	 */
	@IgnoreMethod
	public void setClearCookiesBeforeOpen(boolean clearCookiesBeforeOpen) {
		this.clearCookiesBeforeOpen = clearCookiesBeforeOpen;
	}
}

