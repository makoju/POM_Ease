package com.ability.ease.selenium.webdriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ability.ease.auto.enums.portal.selenium.WebDriverType;
import com.ability.ease.auto.system.WorkingEnvironment;

public class WebDriverFactory {
	
	
	private static String chromeProfile = null;
	private static String chromeExtension = null;
	private static String firefoxExtension = null;
	private static String firefoxProfile = null;
	private static boolean ignoreCertificateErrors = false;
	private static String firefoxBrowserPath;// browser path ,e.g.// c:/...../Firefox.exe
	private static boolean windowMaximize = true;
	
	public static WebDriverWrapper getWebDriver(WebDriverType wdType) throws Exception {
		return webDriverFactory(wdType);
	}
	
	
	private static WebDriverWrapper webDriverFactory(WebDriverType type) throws Exception {

		WebDriverWrapper driver = null;
		WebDriver webDriver = null;

		try {

			if (type == null) {
				throw new IllegalArgumentException(
						"the input browser type is null(change the value of the 'webDriver' Type in the SUT");
			}

			switch (type) {
			case CHROME_DRIVER:
				webDriver = getChromeDriver();
			break;

			case FIREFOX_DRIVER:
				webDriver = getFirefoxWebDriver();
			break;

			case INTERNET_EXPLORER_DRIVER:
				webDriver = getInternetExplorerWebDriver();
			break;
			default:
				throw new IllegalArgumentException("WebDriver type is unsupported");

			}
		} catch (IllegalArgumentException e) {			
			report(e.getMessage() + WebDriverType.getAllSupportedWebDriverTypesAsString(), false);
			e.printStackTrace();
			throw e;
		}

		if (webDriver == null) {
			report("Failed to init the web driver", false);
			throw new IllegalArgumentException("Failed to init the web driver-" + type);
		}
		else {
			driver = new WebDriverWrapper(webDriver);
		}
		return driver;
	}
	
	
	protected static WebDriver getInternetExplorerWebDriver() {
		WebDriver webDriver = null;
		try {			
			
			System.setProperty("webdriver.ie.driver",WorkingEnvironment.getWebdriverIEServerEXEPath());

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability("enablePersistentHover", false);
			/*ieCapabilities.setCapability("nativeEvents", true);    
			ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
			ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
			ieCapabilities.setCapability("disable-popup-blocking", true);
			ieCapabilitiesc.setCapability("enablePersistentHover", true);
			ieCapabilities.setCapability("ie.ensureCleanSession", true);
			ieCapabilities.setCapability("ie.enableElementCacheCleanup", true);
			ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);*/
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			if (ignoreCertificateErrors) {
				ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			}
			webDriver = new InternetExplorerDriver(ieCapabilities);
		} catch (Exception e) {
			ListenerstManager.getInstance().report(e.getMessage(),false);
		}
		return webDriver;
	}

	private static FirefoxProfile ffProfile = null;
	private static FirefoxBinary ffBinary = null;

	private static WebDriver getFirefoxWebDriver() throws Exception {
		WebDriver webDriver = null;
		try {
			if (ffProfile == null) {
				ffProfile = getFirefoxConfigurationProfile();
			}

			if (firefoxBrowserPath == null || firefoxBrowserPath.isEmpty()) {
				webDriver = new FirefoxDriver(ffProfile);
			}
			else {
				if (ffBinary == null) {
					System.setProperty("webdriver.firefox.bin", firefoxBrowserPath);
					// Build  the Default path of Firefox driver in runtime
					report("set Firefox bin Property :" + firefoxBrowserPath);
					File pathToFirefoxBinary = new File(firefoxBrowserPath);
					ffBinary = new FirefoxBinary(pathToFirefoxBinary);
					report("the Property was set to :" + System.getProperty("webdriver.firefox.bin"));
				}
				webDriver = new FirefoxDriver(ffBinary, ffProfile);
			}
		} catch (Exception e) {
			report("failed to set browser path =" + firefoxBrowserPath + ". " + e.getMessage(), e);
		}
		//System.setProperty("webdriver.firefox.bin","C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		//webDriver = new FirefoxDriver();
		return webDriver;
	}

	private static WebDriver getChromeDriver() {

		WebDriver driver = null;
		String chromeDriverPath;
		ChromeOptions options = new ChromeOptions();

		try {
			//chromeDriverPath = getChromeDriverExePath();

			report("the Chrome Driver path is =" + WorkingEnvironment.getWebdriverChromeServerEXEPath());
			System.setProperty("webdriver.chrome.driver", WorkingEnvironment.getWebdriverChromeServerEXEPath());

			ArrayList<String> switches = new ArrayList<String>();
			if (chromeProfile != null) {
				report("open webDriver chrome with the profile(" + chromeProfile + ").");
				switches.add("--user-data-dir=" + chromeProfile);
			}

			if (chromeExtension != null) {
				report("open webDriver chrome with the chromeExtension(" + chromeExtension + ").");
				switches.add("--load-extension=" + chromeExtension);
			}

			if (windowMaximize == true) {
				report("open webDriver chrome with the flag of Maximized.");
				switches.add("--start-maximized");
			}

			if (ignoreCertificateErrors == true) {
				report("open webDriver chrome with the flag of ignoreCertificateErrors.");
				switches.add("--ignore-certificate-errors");
			}

			if (switches.size() > 0) {
				options.addArguments(switches);
				driver = new ChromeDriver(options);
			}
			else {
				driver = new ChromeDriver();
			}
		} catch (Throwable e) {

			ListenerstManager.getInstance().report("Error in init the chorme driver", e);
			e.printStackTrace();
		}
		return driver;
	}
	
	protected static FirefoxProfile getFirefoxConfigurationProfile() {
		File extention = null;
		FirefoxProfile profile = null;

		if (firefoxProfile != null) {
			//report.report("open webDriver firefox with the profile(" + firefoxProfile + ").");
			File profileLib = new File(firefoxProfile);
			if (profileLib != null && !profileLib.exists() || profileLib == null) {
				throw new IllegalArgumentException("Failed to find firefox profile. is the path : " + firefoxProfile
						+ " contains the profile?");
			}
			profile = new FirefoxProfile(profileLib);
		}

		if (firefoxExtension != null) {
			report("open webDriver firefox with Extension(" + firefoxExtension + ").");
			if (null == profile) {
				profile = new FirefoxProfile();
			}
			extention = new File(firefoxExtension);
			if (!extention.exists()) {
				throw new IllegalArgumentException("Failed to find firefox Extension. is the path : "
						+ firefoxExtension + " contains the .xpi extantion?");
			}
			try {

				profile.addExtension(extention);
			} catch (IOException e) {
				report("faild to add Extension to the firefox profile", false);
				throw new IllegalArgumentException("Failed to add firefox c to profile. is the path : "
						+ firefoxExtension + " contains the firefoxExtension?");
			}
		}
		if (ignoreCertificateErrors) {
			if (null == profile) {
				profile = new FirefoxProfile();
			}
			report("set accept Untrusted Certificates");
			profile.setAcceptUntrustedCertificates(ignoreCertificateErrors);
		}

		try {
			if (profile != null) {
				profile.setEnableNativeEvents(true);
			}
		} catch (Exception e) {
			report("faild to enable native event on the firefox profile", Reporter.WARNING);
		}
		return profile;
	}
	
	private static void report(String message, int reporterType) {
		ListenerstManager.getInstance().report(message, reporterType);
	}
	
	private static void report(String message) {
		ListenerstManager.getInstance().report(message);
	}
	
	private static void report(String message, boolean pass) {
		ListenerstManager.getInstance().report(message, pass);
	}
	
	private static void report(String message, Exception ex) {
		ListenerstManager.getInstance().report(message, ex);
	}
	
	

}
