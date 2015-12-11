package com.ability.ease.common;

public class AttributePair{
	
		String AttrName,AttrValue;
		
		public AttributePair(){
			
		}
		
		public AttributePair(String attrname,String attrvalue){
			 this.AttrName = attrname;
			 this.AttrValue = attrvalue;
		 }
		 public void setAttrName(String s){
			 AttrName=s;
		 }
		 public String getAttrName(){
			 return AttrName;
		 }
		 public void setAttrValue(String value){
			 AttrValue=value;
		 }
		 public String getAttrValue(){
			 return AttrValue;
		 }
	
}