package com.ability.ease.selenium.webdriver;

import org.openqa.selenium.WebDriver;

public interface WebDriverContainer extends HasWebDriver {
	public WebDriver getDriver();
	public void setDriver(WebDriver driver);
}

