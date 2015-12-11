package com.ability.ease.selenium.webdriver;


import java.lang.reflect.Field;
import java.text.MessageFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WebDriverHelper {
	
	
	private static boolean _debugMode_ = false; 
	
	/**
	 * Use this method for xpath/css/ .. expressions with arguments set in the @FindBy annotation over field in page object class..
	 * For example: say you have this string:   .class:contains'{0}' > a:contains('{1}') as a generic CSS locator expression displayed by @FindBy annotation.
	 * the method will return you the message after replacing the missing arguments ({0},{1}) with the args parameter you have provided.
	 * @param fieldName
	 * @param args
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static String constructFindByMessage(Class<?> clazz, String fieldName, Object... args) throws SecurityException, NoSuchFieldException {
		return  MessageFormat.format(getFieldFindByValue(clazz, fieldName), args);
	}
	
	/**
	 * Use this method to format a message with arguments.
	 * For example: say you have this generic string:   .class:contains{'0'} > a:contains('{1}) as a CSS locator expression.
	 * the method will return you the message after replacing the missing arguments ({0},{1}) with the args parameter you have provided.
	 * @param message
	 * @param args
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static String constructMessage(String message, Object... args)  {
		return  MessageFormat.format(message, args);
	}
	
	
	/**
	 * Use this method to highlight selected elements during run time for debug
	 * @param driver
	 * @param element
	 */
	public static void highlightElement(WebDriver driver, WebElement element) {
		if (_debugMode_) {
			 if (driver instanceof JavascriptExecutor) {
			        ((JavascriptExecutor)driver).executeScript("arguments[0].style.backgroundColor = '#FF0000'", element);
			    }
		}
	}

	
	
	//############################# private methods ######################################
	
	private static String getFieldFindByValue(Class<?> clazz, String fieldName) throws SecurityException, NoSuchFieldException {
		Field field = clazz.getDeclaredField(fieldName);
		if (field!=null) { 
			field.setAccessible(true);
			if (field.isAnnotationPresent(FindBy.class)) {
				By by = new Annotations(field).buildBy();
				return by.toString().substring(by.toString().indexOf(':')+1, by.toString().length()).trim();				
			}
		}
		return null;
	}
}
