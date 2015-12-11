package com.ability.ease.selenium.webdriver;

import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

/**
 * This class if for Event Handler by the Dispatcher of the
 * WebDriverEventListener Class. the
 * 
 */
public class WebDriverReportEventHandler implements WebDriverEventListener {

	private final Reporter reporter;
	private final String prefix = "[";
	private final String suffix = "]";

	public WebDriverReportEventHandler() {
		reporter = ListenerstManager.getInstance();
		reporter.report("Init the WebDriverReportEventHandler");
	}

	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		//reporter.report("Changed value: " + arg0);
	}

	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		reporter.report("Clicked " + arg0.toString().substring(arg0.toString().indexOf("->")+2, arg0.toString().length()-1));
	}

	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		//report("FindBy", "FindBy= " + arg0);
	}

	public void afterNavigateBack(WebDriver arg0) {
		report("Navigate back to: " + arg0.getCurrentUrl());
	}

	public void afterNavigateForward(WebDriver arg0) {
		report("Navigate forward to: " + arg0.getCurrentUrl());
	}

	public void afterNavigateTo(String arg0, WebDriver arg1) {
		report("Navigate to. Page loaded= " + arg0);
	}

	public void afterScript(String arg0, WebDriver arg1) {
		//reporter.report("after Script");
	}

	/**
	 * Called before {@link WebElement#clear WebElement.clear()},
	 * {@link WebElement#sendKeys WebElement.sendKeys(...)}, or
	 * {@link WebElement#toggle WebElement.toggle()}.
	 */
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		//report("Before Change Value Of");
	}

	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		//report("Before Click On");
	}

	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		//report("Before FindBy", "FindBy= " + arg0);
	}

	public void beforeNavigateBack(WebDriver arg0) {
		//report("Before Navigate Back", "Navigate back from= " + arg0.getCurrentUrl());
	}

	public void beforeNavigateForward(WebDriver arg0) {
		//report("Before Navigate Forward", "Navigate forward from= " + arg0.getCurrentUrl());
	}

	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		//report("Before Navigation", "Navigate from= " + arg1.getCurrentUrl() + ", Navigate to= " + arg0);
	}

	public void beforeScript(String arg0, WebDriver arg1) {
		//reporter.report("beforeScript");
	}
	
	private void report(String title) {
		reporter.report(prefix + title );
	}

	private void report(String title, String text) {
		reporter.report(prefix + title + suffix + ": " + text);
	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}

}


