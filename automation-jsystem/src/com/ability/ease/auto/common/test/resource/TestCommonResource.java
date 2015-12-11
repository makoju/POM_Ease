package com.ability.ease.auto.common.test.resource;

import java.io.File;

import jsystem.framework.JSystemProperties;

/**
 * Utility class to get test resources information
 *
 */
public class TestCommonResource {

	
	private static final String SEPERATOR = File.separator;
	private static final String TEST_RESOURCES_DIR = JSystemProperties.getCurrentTestsPath()  + SEPERATOR + "resources" + SEPERATOR;	
	private static final String CHROME_DRIVER_EXE_NAME = "chromedriver.exe";
	private static final String IE_DRIVER_EXE_PATH = "IEDriverServer.exe";

	
	/**
	 * Returns tests 'resources' directory path
	 * @return
	 */
	public static String getTestResoucresDirPath() {
		return TEST_RESOURCES_DIR;
	}	
	

	/**
	 * Returns IEdriverserver.exe executable path
	 * @return
	 */
	public static String getIEDriverExePath() {
		return TEST_RESOURCES_DIR + "webdriver" + SEPERATOR + IE_DRIVER_EXE_PATH;
	}
	
	/**
	 * Returns ChromeDriver executable path
	 * @return
	 */
	public static String getChromeDriverExePath() {
		return TEST_RESOURCES_DIR+ "webdriver" + SEPERATOR + CHROME_DRIVER_EXE_NAME;
	}
	

	/**
	 * returns the Jsystem Classes folder path
	 */
	public static String getJsystemClassesDir() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR;
	}

	
	/**
	 * returns the configurations folder path
	 * @return
	 */
	public static String getConfigurationsDirectory() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR + "configurations";
	}

	/**
	 * returns the cfg folder path
	 * @return
	 */
	public static String getCfgDirectory() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR + "configurations" + SEPERATOR + "cfg";
	}

	/**
	 * returns the cfg folder path
	 * @return
	 */
	public static String getBprDirectory() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR + "configurations" + SEPERATOR + "bpr";
	}

	/**
	 * returns the cfg folder path
	 * @return
	 */
	public static String getAuditDirectory() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR + "configurations" + SEPERATOR + "audit";
	}

	/**
	 * returns the sql folder path
	 * @return
	 */
	public static String getSqlDirectory() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR + "configurations" + SEPERATOR + "sql";
	}

	/**
	 * returns test data folder.
	 * this folder contains files which contain test input data such as excel files with Users information.
	 * @return
	 */
	public static String getTestDataDirectory() {
		return JSystemProperties.getCurrentTestsPath() + SEPERATOR + "configurations" + SEPERATOR + "testdata";
	}

	/**
	 * get the resources directory
	 * @return
	 */
	public static String getResourcesDirectory() {
		return JSystemProperties.getCurrentTestsPath()  + SEPERATOR + "resources";
	}

	
	/**
	 * Returns the IM sample directory
	 * @return
	 */
	public static String getImSampleDirectory() {
		return getResourcesDirectory()  + SEPERATOR + "im-samples";
	}
	
	
	/**
	 * Returns the base directory of IM store spring XMLs.
	 * @return
	 */
	public static String getImStoresConfBaseDir() {
		return getResourcesDirectory() + SEPERATOR + "spring" + SEPERATOR + "imstore";
	}

	/**
	 * Returned the directory location of images
	 */
	public static String getImagesDirectory() {
		return getResourcesDirectory() + SEPERATOR + "images";
	}
	
	/**
	 * Returned the directory location of environment setting images
	 */
	public static String getEnvironmentImagesDirectory() {
		return getImagesDirectory() + SEPERATOR + "environment";
	}
	
	/**
	 * Returned the directory location of environment setting images
	 */
	public static String getFileSeperator() {
		return SEPERATOR;
	}
	
}