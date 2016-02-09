package com.ability.ease.auto.enums.portal.selenium;

import java.util.EnumSet;


public enum WebDriverType {
	FIREFOX_DRIVER("firefox"), 
	INTERNET_EXPLORER_DRIVER("iexplore"), 
	CHROME_DRIVER("chrome"); 

	private String webDriverType;

	WebDriverType(String type) {
		webDriverType = type;
	}

	public String getBorwserType() {
		return webDriverType;
	}

	public static WebDriverType getBrowserTypeFromString(String type) {
		if (type != null) {
			for (WebDriverType webDriverTypeItem : WebDriverType.values()) {
				if (webDriverTypeItem.webDriverType.toLowerCase().equals(type.toLowerCase())) {
					return webDriverTypeItem;
				}
			}
		}

		return null;
	}

	public static String getAllSupportedWebDriverTypesAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("The Supported WebDriverType:");
		sb.append("\n");
		int index = 1;
		for (WebDriverType type : EnumSet.allOf(WebDriverType.class) ) {
			sb.append(index + ". " + type.name() + "\n");
			index++;
		}
		return sb.toString();
	}

}
