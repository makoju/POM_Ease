package com.ability.ease.auto.system;

import com.ability.ease.auto.enums.portal.selenium.WebDriverType;
import com.ability.ease.auto.enums.tests.TestType;

public final class WorkingEnvironment{

	//require for runtime
	private static String host;
	private static String port;
	private static TestType testType;
	private static WebDriverType webdriverType;
	private static String webdriverIEServerEXEPath;
	private static String webdriverChromeServerEXEPath;
	private static String jsessionid;
	private static com.ability.ease.auto.enums.common.DateFormat systemDateFormat;
	private static String easeURL;
	private static String mySQLDBHostname;
	private static String mySQLDBName;
	private static String mySQLDBPort;
	private static String mySQLDBUser;
	private static String mySQLDBPassword;
	private static String mailServerHost;

	public static String getHost() {
		return host;
	}
	public static void setHost(String host) {
		WorkingEnvironment.host = host;
	}
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		WorkingEnvironment.port = port;
	}

	public static TestType getTestType() {
		return testType;
	}
	public static void setTestType(TestType testType) {
		WorkingEnvironment.testType = testType;
	}
	public static WebDriverType getWebdriverType() {
		return webdriverType;
	}
	public static void setWebdriverType(WebDriverType webdriverType) {
		WorkingEnvironment.webdriverType = webdriverType;
	}
	public static String getWebdriverIEServerEXEPath() {
		return webdriverIEServerEXEPath;
	}
	public static void setWebdriverIEServerEXEPath(String webdriverIEServerEXEPath) {
		WorkingEnvironment.webdriverIEServerEXEPath = webdriverIEServerEXEPath;
	}
	public static String getWebdriverChromeServerEXEPath() {
		return webdriverChromeServerEXEPath;
	}
	public static void setWebdriverChromeServerEXEPath(
			String webdriverChromeServerEXEPath) {
		WorkingEnvironment.webdriverChromeServerEXEPath = webdriverChromeServerEXEPath;
	}
	public static String getJsessionid() {
		return jsessionid;
	}
	public static void setJsessionid(String jsessionid) {
		WorkingEnvironment.jsessionid = jsessionid;
	}

	public static com.ability.ease.auto.enums.common.DateFormat getSystemDateFormat() {
		return systemDateFormat;
	}
	public static void setSystemDateFormat(
			com.ability.ease.auto.enums.common.DateFormat systemDateFormat) {
		WorkingEnvironment.systemDateFormat = systemDateFormat;
	}
	public static String getMailServerHost() {
		return mailServerHost;
	}
	public static void setMailServerHost(String mailServerHost) {
		WorkingEnvironment.mailServerHost = mailServerHost;
	}
	public static String getEaseURL() {
		return easeURL;
	}
	public static void setEaseURL(String easeURL) {
		WorkingEnvironment.easeURL = easeURL;
	}
	public static String getMySQLDBHostname() {
		return mySQLDBHostname;
	}
	public static void setMySQLDBHostname(String mySQLDBHostname) {
		WorkingEnvironment.mySQLDBHostname = mySQLDBHostname;
	}
	public static String getMySQLDBPort() {
		return mySQLDBPort;
	}
	public static void setMySQLDBPort(String mySQLDBPort) {
		WorkingEnvironment.mySQLDBPort = mySQLDBPort;
	}
	public static String getMySQLDBUser() {
		return mySQLDBUser;
	}
	public static void setMySQLDBUser(String mySQLDBUser) {
		WorkingEnvironment.mySQLDBUser = mySQLDBUser;
	}
	public static String getMySQLDBPassword() {
		return mySQLDBPassword;
	}
	public static void setMySQLDBPassword(String mySQLDBPassword) {
		WorkingEnvironment.mySQLDBPassword = mySQLDBPassword;
	}
	public static String getMySQLDBName() {
		return mySQLDBName;
	}
	public static void setMySQLDBName(String mySQLDBName) {
		WorkingEnvironment.mySQLDBName = mySQLDBName;
	}
	
	
}
