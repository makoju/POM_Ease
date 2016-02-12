package com.ability.ease.auto.common;

import java.io.File;

import jsystem.framework.JSystemProperties;

public class TestCommonResource {
	private static final String SEPERATOR = File.separator;
	
	private static final String TEST_RESOURCES_DIR = JSystemProperties.getCurrentTestsPath()  + SEPERATOR + "resources" + SEPERATOR;
	
	public static String getTestResoucresDirPath() {
		return TEST_RESOURCES_DIR;
	}
	
}
