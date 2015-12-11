package com.ability.auto.common;

import java.util.HashMap;
import java.util.Map;

import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.common.AttributePair;

public class AttrStringstoMapConvert {

	public static Map<String,String> convertAttrStringstoMap(AttributePair[] pair){
		Map<String,String> attrValuesMap=new HashMap<String, String>();
		for(int i=0;i<pair.length;i++)
			attrValuesMap.put(pair[i].getAttrName(),pair[i].getAttrValue());

		return attrValuesMap;
	}
	public static Map<String,String> convertAttrStringstoMapV2(AttributeNameValueDialogProvider[] pair){
		System.out.println("Inside attrstringtomapconvert method");
		Map<String,String> attrValuesMap=new HashMap<String, String>();
		for(int i=0;i<pair.length;i++)
			attrValuesMap.put(pair[i].getAttributeName(),pair[i].getAttributeValue());

		return attrValuesMap;
	}
}
