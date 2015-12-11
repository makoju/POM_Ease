package com.ability.ease.selenium.webdriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * Used on {@link WebElement}s which must exist when the page is loaded<br/>
 * After constructing a new page object from a subclass of
 * {@link AbstractAjaxPageObjectImpl} an annotated element will be in the page.<br>
 * The {@link AbstractPageObject#assertInModule()} will try to find it (inside
 * {@link #value()}s) or a {@link TimeoutException} will be thrown
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ElementInFrameList {
	/**
	 * @return The frame-path of the element
	 */
	String[] value() default {};
}

