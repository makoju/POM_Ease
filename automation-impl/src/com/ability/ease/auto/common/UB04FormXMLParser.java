package com.ability.ease.auto.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jsystem.framework.report.Reporter.ReportAttribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.commons.lang3.StringUtils;

import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.selenium.webdriver.AbstractPageObject;



public class UB04FormXMLParser extends AbstractPageObject{

	public static UB04FormXMLParser getInstance(){
		return new UB04FormXMLParser();
	}

	public boolean validateUB04XML(List<Attribute> lsAttributes, String sFile){

		String sFieldName = null, sActualFieldValue = null, sDisplayName = null; 
		int iFalseCounter = 0;
		boolean isXMLValid = false;
		List<String> claimFiled = new ArrayList<String>();
		claimFiled.add("rev");claimFiled.add("ndcNumPrefix");claimFiled.add("HCPC");claimFiled.add("service_date");claimFiled.add("total_units");
		claimFiled.add("cov_units");claimFiled.add("tot_charge");claimFiled.add("ncov_charge");

		Document doc = createDOMDocument(sFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("field");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				sFieldName = eElement.getAttribute("name");
				sActualFieldValue = eElement.getAttribute("new");

				if(claimFiled.contains(sFieldName)){
					continue;
				}

				sDisplayName = UB04FormXMLMap.ub04FormXMLMap.get(sFieldName);
				StringBuilder sExpectedFiledValue = new StringBuilder(getValueFromJSystem(lsAttributes, sDisplayName));
				if(sFieldName.equalsIgnoreCase("from") || sFieldName.equalsIgnoreCase("through") || sFieldName.equalsIgnoreCase("admission_date")){
					sExpectedFiledValue = formatString(sExpectedFiledValue);
				}

				if(sFieldName.equalsIgnoreCase("cd_codes") || sFieldName.equalsIgnoreCase("span_codes") || sFieldName.equalsIgnoreCase("procedure_codes_dates")){
					String temp = sExpectedFiledValue.toString();
					StringBuilder temp2 = new StringBuilder(formatStringV2(temp));
					sExpectedFiledValue = temp2;
				}

				if( sActualFieldValue != null && sExpectedFiledValue != null){
					if(sActualFieldValue.equalsIgnoreCase(sExpectedFiledValue.toString())){
						report.report("value from xml "+sActualFieldValue + " is equivalent to expected value "+ sExpectedFiledValue );
					}else{
						report.report("value from xml "+sActualFieldValue + " is not equivalent to expected value "+ sExpectedFiledValue );
						iFalseCounter++;
					}
				}else{
					report.report("XML validation: Expected or Actual value is null", ReportAttribute.BOLD);
					iFalseCounter++;
				}
			}
		}
		if(iFalseCounter == 0){
			isXMLValid = true;;
		}else {
			isXMLValid = false;
		}
		return isXMLValid;

	}

	//this method will format date from ddmmyyyy to ddmmyy 
	public StringBuilder formatString(StringBuilder strbld){
		strbld.deleteCharAt(5);
		strbld.deleteCharAt(4);
		return strbld;
	}

	//this method will format date from code date [xx ddmmyyyy] to [xx ddmmyy] 
	public String formatStringV2(String sStringToFormat){

		String[] afterFormat;
		String s = null;
		String sArray[] = sStringToFormat.split("~~~");
		List<String> list = new ArrayList<String>();

		for(String str:sArray){
			String sArray2[] = str.split(" ");
			String code = sArray2[0];
			if(sArray2.length > 2){
				StringBuilder tempdate1 = new StringBuilder(sArray2[1]);
				StringBuilder tempdate2 = new StringBuilder(sArray2[2]);
				String date1 = formatString(tempdate1).toString();
				String date2 = formatString(tempdate2).toString();
				list.add(code+ " " + date1 + " " + date2);
			}
			else{
				StringBuilder tempdate3 = new StringBuilder(sArray2[1]);
				String date3 = formatString(tempdate3).toString();
				list.add(code+ " "+ date3);
			}
		}
		afterFormat = new String[list.size()];
		afterFormat = list.toArray(afterFormat);

		s = StringUtils.join(afterFormat, "~~~");
		return s;
	}

	public Document createDOMDocument(String sFile){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(sFile);
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	public float[] getClaimTotals(String sFile){

		float totalCharges = 0,nonCoveredCharges = 0,tempTotalCharges = 0,tempNonCoveredCharges=0;

		String sFieldName = null, sActualFieldValue = null;
		Document doc = createDOMDocument(sFile);
		NodeList nList = doc.getElementsByTagName("field");
		List<String> claimFiled = new ArrayList<String>();
		claimFiled.add("rev");claimFiled.add("ndcNumPrefix");claimFiled.add("HCPC");claimFiled.add("service_date");claimFiled.add("total_units");
		claimFiled.add("tot_charge");claimFiled.add("ncov_charge");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				sFieldName = eElement.getAttribute("name");
				sActualFieldValue = eElement.getAttribute("new");
				if(claimFiled.contains(sFieldName)){
					if(sFieldName.equalsIgnoreCase("tot_charge")){
						tempTotalCharges = Float.valueOf(sActualFieldValue);
						totalCharges = totalCharges + tempTotalCharges;
					}else if(sFieldName.equalsIgnoreCase("ncov_charge")){
						tempNonCoveredCharges = Float.valueOf(sActualFieldValue);
						nonCoveredCharges = nonCoveredCharges + tempNonCoveredCharges;
					}
				}else{
					continue;
				}
			}
		}
		totalCharges = totalCharges - tempTotalCharges;
		nonCoveredCharges = nonCoveredCharges - tempNonCoveredCharges;
		report.report("Actual total charges are:" + String.valueOf(round(totalCharges,2)));
		report.report("Actual total non covered charges are:" + String.valueOf(round(nonCoveredCharges,2)));
		float claimTotals[] = new float[]{round(totalCharges,2), round(nonCoveredCharges,2)};
		return claimTotals;
	}

	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}
