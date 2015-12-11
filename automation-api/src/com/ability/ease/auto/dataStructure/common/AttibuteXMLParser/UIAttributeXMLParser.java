package com.ability.ease.auto.dataStructure.common.AttibuteXMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.uienum.CommonEaseUIEnum.UIAttributeStyle;

public class UIAttributeXMLParser {
	
	   public static UIAttributeXMLParser getInstance(){
		   return new UIAttributeXMLParser();
	   }
		
	   List<Attribute> lsattrs=new ArrayList<Attribute>();
	 
	   public List<Attribute> getUIAttributesFromXML(String fileName) {
		   return getUIAttributesFromXMLV2(fileName,null);
	    }	
		public List<Attribute> getUIAttributesFromXMLV2(String fileName, final Map<String,String> mapattVal){
			
			try {
				 
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
			 
				DefaultHandler handler = new DefaultHandler() {
			 
				boolean bskipPopulatingScreen=false;
				//boolean bUIAttributeEnd=false;
				
				private String sCurrentElement;
				
				StringBuilder locator;
				StringBuilder Style;
				StringBuilder displayName;
				UIAttributeStyle attrStyle;
				StringBuilder tewsTab;
				//StringBuilder description;
				boolean skipPopulatingScreen; 
			 
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
			 
					 sCurrentElement=qName;
							
					if (qName.equalsIgnoreCase("locator")) {
						locator=new StringBuilder();
					}
			 
					if (qName.equalsIgnoreCase("style")) {
						Style=new StringBuilder();
					}
			 
					if (qName.equalsIgnoreCase("displayName")) {
						displayName=new StringBuilder();
					}
					if (qName.equalsIgnoreCase("skipPopulatingScreen")) {
						bskipPopulatingScreen = true;
					}
					
					if (qName.equalsIgnoreCase("tewsTab")) {
						tewsTab=new StringBuilder();
					}
				}
				
				private void createAttributeObject(){
					Attribute attr=new Attribute();
					if(displayName!=null)
					  attr.setDisplayName(displayName.toString());
					if(locator!=null)
					  attr.setLocator(locator.toString());
					
					attr.setSkipPopulatingScreen(skipPopulatingScreen);
					if(displayName!=null)
					  attr.setValue(mapattVal.get(displayName.toString()));
					//set style here each time you add a new style
					if(Style.toString().equalsIgnoreCase("Text")){
						attrStyle= UIAttributeStyle.Text;
					}
					if(Style.toString().equalsIgnoreCase("Table")){
						attrStyle= UIAttributeStyle.Table;
					}
					if(Style.toString().equalsIgnoreCase("CheckBox")){
						attrStyle= UIAttributeStyle.CheckBox;
					}
					if(Style.toString().equalsIgnoreCase("Submit")){
						attrStyle= UIAttributeStyle.Submit;
					}
					if(Style.toString().equalsIgnoreCase("Link")){
						attrStyle= UIAttributeStyle.Link;
					}
					if(Style.toString().equalsIgnoreCase("SetupAlert")){
						attrStyle= UIAttributeStyle.SetupAlert;
					}
					if(Style.toString().equalsIgnoreCase("DropDown")){
						attrStyle= UIAttributeStyle.DropDown;
					}
					if(Style.toString().equalsIgnoreCase("DropDownList")){
						attrStyle= UIAttributeStyle.DropDown;
					}
					if(Style.toString().equalsIgnoreCase("Image")){
						attrStyle= UIAttributeStyle.Image;
					}
					
					attr.setStyle(attrStyle);
					lsattrs.add(attr);
					locator=displayName=Style=null;
				}
			 
				public void endElement(String uri, String localName,
					String qName) throws SAXException {
					if(qName.equalsIgnoreCase("UIAttribute")){
					if(mapattVal==null){
						createAttributeObject();
					}
					//Creating the Objects only for those attributes that are specified in the JSYSTEM UI 	
					else if(mapattVal!=null && mapattVal.containsKey(displayName.toString())){
						createAttributeObject();
					}
					}
					if(qName.equalsIgnoreCase("skipPopulatingScreen"))
						bskipPopulatingScreen=false;
					//System.out.println("End Element :" + qName);
			 	}

	 
		public void characters(char ch[], int start, int length) throws SAXException {
	 
			if (sCurrentElement.equalsIgnoreCase("locator") && locator!=null) {
				  locator.append(new String(ch, start, length).trim());
			}
	 
			if (sCurrentElement.equalsIgnoreCase("style") && Style!=null) {
				 Style.append(new String(ch, start, length).trim());
			}
	 
			if (sCurrentElement.equalsIgnoreCase("displayName") && displayName!=null) {
				 displayName.append(new String(ch, start, length).trim());
			}
			if (sCurrentElement.equalsIgnoreCase("skipPopulatingScreen") && bskipPopulatingScreen) {
				skipPopulatingScreen=Boolean.parseBoolean(new String(ch, start, length).trim());
			}
			if (sCurrentElement.equalsIgnoreCase("tewsTab") && tewsTab!=null) {
				 tewsTab.append(new String(ch, start, length).trim());
			}
		}
	 
	     };
	 
	       saxParser.parse(fileName, handler);
	 
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	     
	     System.out.println("UI Attribute"+lsattrs);
	     return lsattrs;
	 
	   }
}
	 
