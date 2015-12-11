package com.ability.ease.auto.dataStructure.common.easeScreens;

import com.ability.ease.auto.uienum.CommonEaseUIEnum.UIAttributeStyle;

public class UIAttribute {

	protected String locator; //Distinguished name that Selenium can use to identify the UI Element
	protected UIAttributeStyle style; // Attribute Style example: Text, Table, Link etc.,
	protected String value; //Attribute value, provide a ',' seperated list if it is multi-valued attribute
	protected String displayName; //Screen display name
	protected String description;
	protected boolean skipPopulatingScreen; // //This is to handle exception cases such as attribute behavior based on quirkiness of user stores

	//Constructor
	public UIAttribute(){
		locator = null;
		style = null;
		value = null;
		displayName = null;
		description = null;
		skipPopulatingScreen = false;
	}

	//C'tor to define Screen definition in the Object Data Structure.
	public UIAttribute(String attrName, UIAttributeStyle attrStyle, String attrDisplayName, boolean flagSkipPopulatingScreen){
		locator = attrName;
		style = attrStyle;
		displayName = attrDisplayName;
		skipPopulatingScreen = flagSkipPopulatingScreen; 
	}

	//Getters and Setters
	public String getLocator() {
		return locator;
	}
	public void setLocator(String locator) {
		this.locator = locator;
	}
	public UIAttributeStyle getStyle() {
		return style;
	}

	public void setStyle(UIAttributeStyle style) {
		this.style = style;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isSkipPopulatingScreen() {
		return skipPopulatingScreen;
	}

	public void setSkipPopulatingScreen(boolean skipPopulatingScreen) {
		this.skipPopulatingScreen = skipPopulatingScreen;
	}
}
