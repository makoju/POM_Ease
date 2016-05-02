package com.ability.ease.myaccount;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.TimeZoneConversionUtil;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ChangeSchedulePage extends AbstractPageObject {

	MyAccountHelper helper = new MyAccountHelper();
	
	public boolean verifyChangeSchedule(Map<String,String> mapAttrValues)throws Exception {

		navigateToPage();
		clickLink("Change Schedule");

		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml/myaccount/ChangeSchedule.xml",mapAttrValues);

		UIActions changeSchedule = new UIActions();
		changeSchedule.fillScreenAttributes(lsAttributes);
		Thread.sleep(5000);

		//validations
		String sTimeZone = mapAttrValues.get("Timezone");
		report.report("Timezone is added");
		String sAgencyNumber = mapAttrValues.get("Agency");
		Map<Integer,String> xmlFileMap = helper.getScheduleXMLFilesFromDB(sAgencyNumber);

		//code to convert claim schedule time to CST
		String sClaimsScheduleTime = mapAttrValues.get("claimsTimeDropDown");
		report.report("Timezone clamis is added");
		String CSTValue_Claims = TimeZoneConversionUtil.convertToCSTTimeZone(sClaimsScheduleTime, sTimeZone);
		
		//code to convert Eligibility schedule time to CST
		String sEligibilityScheduleTime = mapAttrValues.get("EligibilityTimeDropDown");
		report.report("Timezone eligibility  is added");
		String CSTValue_Eligibility = TimeZoneConversionUtil.convertToCSTTimeZone(sEligibilityScheduleTime, sTimeZone);

		//code to convert EFT schedule time to CST
		String sEftScheduleTime = mapAttrValues.get("EFTTimeDropDown");
		report.report("Timezone EFT is added");
		String CSTValue_EFT = TimeZoneConversionUtil.convertToCSTTimeZone(sEftScheduleTime, sTimeZone);

		String sXpathExpression = "//schedule/@hour";
		Document claimsXMLDocument = convertStringToDocument(xmlFileMap.get(10));
		Document eligibilityXMLDocument = convertStringToDocument(xmlFileMap.get(11));
		Document eftXMLDocument = convertStringToDocument(xmlFileMap.get(12));

		return (
				isValueFoundinXML(claimsXMLDocument, sXpathExpression,  CSTValue_Claims) &
				isValueFoundinXML(eligibilityXMLDocument, sXpathExpression,  CSTValue_Eligibility) &
				isValueFoundinXML(eftXMLDocument, sXpathExpression,  CSTValue_EFT)
				);
	}

	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		try 
		{  
			builder = factory.newDocumentBuilder();  
			//Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
			return builder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
		} catch (Exception e) {  

		} 
		return null;
	}


	public boolean isValueFoundinXML(Document  document, String sXpathExpression, String value) throws XPathExpressionException{
		boolean flag =false;

		try{
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			XPathExpression expr1 = xpath.compile(sXpathExpression);
			String sValue = (String) expr1.evaluate(document, XPathConstants.STRING);
			report.report("Expected Value :"+ value);
			report.report("Actual Value :"+ sValue);
			flag = sValue.equals(value);
			
		}catch(Exception e){
			
		}
		return flag;
		//return (sValue.equals(value));

	}


	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
	}

}
