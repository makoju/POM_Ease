package com.ability.ease.auto.systemobjects;


import jsystem.framework.system.SystemObjectImpl;

import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.system.WorkingEnvironment;


/**
 * Represents the SUT object
 *
 */
public class MySut extends SystemObjectImpl{


	// Declaration of JSystem System Objects
	public DefaultWorkingEnvironment workingEnvironment;
	public WebDriverSystemObject webdriver;	
	private static boolean isInitialized = false;
	private com.ability.ease.auto.enums.common.DateFormat systemDateFormat = com.ability.ease.auto.enums.common.DateFormat.US;


	/**
	 * This method is being called when JSystem initializes its system objects
	 * @throws Exception
	 */
	@Override
	public final void init() throws Exception {

		if (!isInitialized) {
			isInitialized = true; // this line should be placed BEFORE the 'super.init()' line
			super.init();
			String easeURL ="";

			String host = workingEnvironment.getHost();
			String port = workingEnvironment.getPort();

			//constructing ease applications URL ..
			easeURL = "http://" + host + ":" + port + "/";

			//Saving working environment data from SUT
			WorkingEnvironment.setHost(workingEnvironment.getHost());
			WorkingEnvironment.setPort(workingEnvironment.getPort());
			WorkingEnvironment.setEaseURL(easeURL);
			WorkingEnvironment.setWebdriverIEServerEXEPath(TestCommonResource.getIEDriverExePath());
			WorkingEnvironment.setWebdriverChromeServerEXEPath(TestCommonResource.getChromeDriverExePath());
			WorkingEnvironment.setWebdriverType(webdriver.getWebDriverType());
			WorkingEnvironment.setMySQLDBHostname(workingEnvironment.getMySQLDBHostName());
			WorkingEnvironment.setMySQLDBPort(workingEnvironment.getMySQLDBPort());
			WorkingEnvironment.setMySQLDBUser(workingEnvironment.getMySQLDBUserName());
			WorkingEnvironment.setMySQLDBPassword(workingEnvironment.getMySQLDBPassword());
			WorkingEnvironment.setMySQLDBName(workingEnvironment.getMySQLDBName());
		}
	}



}
