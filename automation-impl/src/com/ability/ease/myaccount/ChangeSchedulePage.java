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

import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.w3c.dom.Document;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.TimeZoneConversionUtil;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ChangeSchedulePage extends AbstractPageObject {

	MyAccountHelper helper = new MyAccountHelper();
	private int failurecount=0;

	public boolean verifyChangeSchedule(Map<String,String> mapAttrValues)throws Exception {
		String sXpathExpression = "//schedule/@hour";
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
		report.report("Timezone: "+sTimeZone);
		String sAgencyNumber = mapAttrValues.get("Agency");
		report.report("Agency: "+sAgencyNumber);
		Map<Integer,String> xmlFileMap = helper.getScheduleXMLFilesFromDB(sAgencyNumber);

		//code to convert claim schedule time to CST	
		if(mapAttrValues.containsKey("claimsTimeDropDown")){
			String sClaimsScheduleTime = mapAttrValues.get("claimsTimeDropDown");
			String CSTValue_Claims = TimeZoneConversionUtil.convertToCSTTimeZone(sClaimsScheduleTime, sTimeZone);
			report.report("Claim Schedule Time in CST: "+CSTValue_Claims);
			Document claimsXMLDocument = convertStringToDocument(xmlFileMap.get(10));
			if(!isValueFoundinXML(claimsXMLDocument, sXpathExpression,  CSTValue_Claims)){
				report.report("Unable to find value in XML for JobType: 10", Reporter.WARNING);
				failurecount++;
			}
		}

		//code to convert Eligibility schedule time to CST
		if(mapAttrValues.containsKey("EligibilityTimeDropDown")){
			String sEligibilityScheduleTime = mapAttrValues.get("EligibilityTimeDropDown");
			String CSTValue_Eligibility = TimeZoneConversionUtil.convertToCSTTimeZone(sEligibilityScheduleTime, sTimeZone);
			report.report("Eligibility Schedule Time in CST: "+CSTValue_Eligibility);
			Document eligibilityXMLDocument = convertStringToDocument(xmlFileMap.get(11));
			if(!isValueFoundinXML(eligibilityXMLDocument, sXpathExpression,  CSTValue_Eligibility) ){
				report.report("Unable to find value in XML for JobType: 11", Reporter.WARNING);
				failurecount++;
			}
		}

		//code to convert EFT schedule time to CST
		if(mapAttrValues.containsKey("EFTTimeDropDown")){
			String sEftScheduleTime = mapAttrValues.get("EFTTimeDropDown");
			String CSTValue_EFT = TimeZoneConversionUtil.convertToCSTTimeZone(sEftScheduleTime, sTimeZone);
			report.report("EFT Schedule Time in CST: "+CSTValue_EFT);
			Document eftXMLDocument = convertStringToDocument(xmlFileMap.get(12));
			if(!isValueFoundinXML(eftXMLDocument, sXpathExpression,  CSTValue_EFT)){
				report.report("Unable to find value in XML for JobType: 12", Reporter.WARNING);
				failurecount++;
			}
		}

		return failurecount>0?true:false; 
	}

	public boolean verifyBlackoutTimeHelpTextinChangeandCustomScheduleWindow(String agency, String starttime, String endtime) throws Exception {
		int failurecount=0;

		String expectedblackouthelptext = "Credential Time Window : "+starttime+" - "+endtime;
		navigateToPage();
		//Verify the helptext in Changeschedule page
		clickLink("Change Schedule");
		selectByNameOrID("user_prov_id", agency.trim());
		String actualblackouttimehelptext = getElementText(By.xpath("//*[contains(text(),'Credential Time Window')]"));

		if(!Verify.StringEquals(expectedblackouthelptext, actualblackouttimehelptext)){
			failurecount++;
			report.report("Expected and Actual Blackout time helptext doesn't match", Reporter.WARNING);
		}
		//verify the helptext in Custom Schedule page
		clickButton("Custom");
		actualblackouttimehelptext = getElementText(By.xpath("//span[contains(text(),'Credential Time Window')]"));
		if(!Verify.StringEquals(expectedblackouthelptext, actualblackouttimehelptext)){
			failurecount++;
			report.report("Expected and Actual Blackout time helptext doesn't match", Reporter.WARNING);
		}
		return failurecount==0?true:false;
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
