package com.ability.ease.auto.dlgproviders;

import jsystem.framework.TestBeanClass;


@TestBeanClass(include = {"AttributeName", "AttributeValue","Description" }, model = AttributeNameValueDialogProviderModel.class)
public class AttributeNameValueDialogProvider {
	
	private String AttributeName;
	private String AttributeValue;
	private String Description;
	
	public String getAttributeName() {
		return AttributeName;
	}
	public void setAttributeName(String attributeName) {
		AttributeName = attributeName;
	}
	public String getAttributeValue() {
		return AttributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		AttributeValue = attributeValue;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		this.Description = description;
	}	
}
