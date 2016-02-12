package com.ability.ease.auto.common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import jsystem.framework.report.Reporter.ReportAttribute;

import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.selenium.webdriver.AbstractPageObject;


/**
 * Class to perform generic UI actions like filling profile form based pages
 * 
 * @author nageswar.bodduri
 *
 */
public class UIActions extends AbstractPageObject {

	public String sValues[];

	/**
	 * Converts a normal string based JSystem bean into custom data structure bean
	 * used in the Selenium generic UI actions
	 */

	public void fillScreenAttributes(List<Attribute> lsAttributes)throws Exception{

		for ( Attribute scrAttr:lsAttributes){

			if( scrAttr.getValue() != null && !scrAttr.isSkipPopulatingScreen()){
				switch(scrAttr.getStyle()){
				case Text:
					typeEditBox(scrAttr.getLocator(), scrAttr.getValue());
					break;
				case DropDown:
					if(!scrAttr.getValue().contains("~")){
						selectByNameOrID(scrAttr.getLocator(), scrAttr.getValue());
					}else{
						String[] locators = scrAttr.getLocator().split(",");
						String dropDownNameOrID = locators[0].trim();
						String addButtonOrID = locators[1].trim(); 
						String[] values = scrAttr.getValue().split("~");
						for(int i=0;i<values.length;i++){
							selectByNameOrID(dropDownNameOrID, values[i].trim());
							clickButton(addButtonOrID);
						}
					}
					break;
				case SetupAlert:
					sValues = scrAttr.getValue().split(",");
					String onOffFlag = null;
					String alertOptionXpath = null;

					Thread.sleep(3000);
					for(String str:sValues){
						String[] sOptions = str.split(":");
						String alertOptionName = sOptions[0];
						onOffFlag = sOptions[1];
						alertOptionXpath = getAlertOptionXpath(alertOptionName);
						WebElement label = driver.findElement(By.xpath(alertOptionXpath+"/../following-sibling::td/label"));
						String labelText = label.getText();
						if( !labelText.equalsIgnoreCase(onOffFlag)){
							driver.findElement(By.xpath(alertOptionXpath+"/../following-sibling::td/img")).click();
						}
					}
					break;
				case Button:
					clickButton(scrAttr.getLocator());
					break;
				case Submit:
					clickButtonV2(scrAttr.getLocator());
					break;
				case Link:
					clickLink(scrAttr.getLocator());
					break;
				case Image:
					waitForElementVisibility(By.xpath(scrAttr.getLocator()), 30);
					WebElement imageRef = driver.findElement(By.xpath(scrAttr.getLocator()));
					if( imageRef != null){
						imageRef.click();
					}
					break;
				case DropDownList:
					break;
				case TextArea:
					sValues = scrAttr.getValue().split(",");
					WebElement element = driver.findElement(By.id(scrAttr.getLocator()));
					element.clear();
					for(String value: sValues){
						element.sendKeys(value);
						element.sendKeys(Keys.ENTER);
					}
					break;
				case LinkWithNoText:
					clickLinkV2(scrAttr.getLocator());
					break;
				case ClaimLineUB04:
					String[] sLocators42To49 = scrAttr.getLocator().split(",");
					String[] sValue42To49 = scrAttr.getValue().split(",");
					int j=1;

					report.report("Filling "+ scrAttr.getDisplayName() + " Values");
					for(String str:sValue42To49){
						String[] sValues42To49 = str.split(":");
						for(int i=0;i < sLocators42To49.length; i++){
							typeEditBox(sLocators42To49[i]+"_"+j, sValues42To49[i]);
						}
						j++;
						if( j >= 10){

						}
					}

					break;
				case ConditionOccurruenceCodes:

					String[] sLocators18To28 = scrAttr.getLocator().split(",");
					String[] sValues18To28 = scrAttr.getValue().split("~~~");

					report.report("Filling "+ scrAttr.getDisplayName() + " Values");
					for(int i=0;i < sValues18To28.length; i++){
						typeEditBox(sLocators18To28[i], sValues18To28[i]);
					}
					break;
				case SpanCodes:
					String[] sSpanCodesDates = null;
					String[] sLocatorsSpanCodes = scrAttr.getLocator().split(",");
					int k = 0;

					report.report("Filling "+ scrAttr.getDisplayName() + " Values");
					if(scrAttr.getValue().contains(",")){
						sSpanCodesDates = scrAttr.getValue().split(",");
						fillSpanCodeDates(sLocatorsSpanCodes, sSpanCodesDates);
					}else{
						String values[] = scrAttr.getValue().split("~~~");
						for(String str:values){
							String[] fillValues = str.split(" ");
							for(int i=0;i < fillValues.length;i++){
								typeEditBox(sLocatorsSpanCodes[k], fillValues[i]);
								k++;
							}				
						}
					}
					break;

				case FillLocatorValues:
					//This style fills values which are separated by ~~~ and if locators prefix is unique 
					String sLocatorID = scrAttr.getLocator();
					String[] sLocatorValues = scrAttr.getValue().split("~~~");

					report.report("Filling "+ scrAttr.getDisplayName() + " Values");
					if(!sLocatorID.equalsIgnoreCase("ub67")){
						char ch = 'a';
						for(int i=0; i< sLocatorValues.length; i++){
							typeEditBox(sLocatorID+ch, sLocatorValues[i]); // for each iteration ch is replaced with alphabetic chars starting from 'a'
							ch++;
						}
					}else{
						typeEditBox(sLocatorID, sLocatorValues[0]);
						char c = 'a';
						for(int i=1; i< sLocatorValues.length; i++){
							typeEditBox(sLocatorID+c, sLocatorValues[i]); // for each iteration ch is replaced with alphabetic chars starting from 'a'
							c++;
						}
					}
					break;


				case FillValuesUB04:
					//This style fills values and locators values which are separated by comma ','
					String sLocators[] = scrAttr.getLocator().split(",");
					String sValues[] = scrAttr.getValue().split(",");		
					report.report("Filling "+ scrAttr.getDisplayName() + " Values");
					for(int i=0; i<sValues.length; i++){
						typeEditBox(sLocators[i], sValues[i]);
					}
					break;


				case Timeframe:
					clickLink(scrAttr.getLocator());
					String timeframe = scrAttr.getValue().trim();
					//if the timeframe value starts with fromdate then user is going to set from and todate 
					if (timeframe.toLowerCase().startsWith("fromdate")){
						String fromdate="",todate="";
						String[] dates = timeframe.split(":");
						//getting the from and todate from the user supplied date string say, "fromdate(MM//DD//YYYY):todate(MM//DD//YYYY)"
						if(dates.length>1){
							fromdate = dates[0].substring(dates[0].indexOf("(")+1, dates[0].indexOf(")"));
							todate = dates[1].substring(dates[1].indexOf("(")+1, dates[1].indexOf(")"));;
						}
						/*typeEditBox("reportCustomDateFrom", fromdate);
						typeEditBox("reportCustomDateTo", todate);
						clickButton("reportTimeframeButton");*/
						((JavascriptExecutor) driver).executeScript("$('#reportCustomDateFrom').val('"+fromdate+"');");
						((JavascriptExecutor) driver).executeScript("$('#reportCustomDateTo').val('"+todate+"');");
						((JavascriptExecutor) driver).executeScript("$('#reportTimeframeButton').click();");
						
					}
					else 
						clickLink(timeframe.trim());
					break;

				case Agency:
					clickLink(scrAttr.getLocator());
					selectByNameOrID("reportAgencySelect", scrAttr.getValue().trim());
					clickButton("Change Agency");
					break;

				case MultiSelectAgency:
					String[] multiAgencies = scrAttr.getValue().split(",");
					clickButton(scrAttr.getLocator());
					waitForTextVisibility(ByLocator.xpath, "//span", "Check all");
					for(String agency:multiAgencies){
						if(agency.startsWith("Check all"))
							clickOnElement(ByLocator.xpath, "//a[@class='ui-multiselect-all']", 5);
						else if(agency.startsWith("Uncheck all"))
							clickOnElement(ByLocator.xpath, "//a[@class='ui-multiselect-none']", 5);
						else
							checkChkBox(agency.trim());
					}
					break;					


				default:
					report.report("The attribute Style is not implemented.",
							ReportAttribute.BOLD);
					break;
				}

			}
		}
	}

	public void fillCodeValues(String[] locators, String[] values) throws Exception{
		for(String str:values){
			String[] sValues = str.split(" ");
			for(int i=0;i < sValues.length; i++){
				typeEditBox(locators[i], sValues[i]);
			}
		}
	}

	public void fillSpanCodeDates(String[] locators, String[] values)throws Exception{
		String tempValues[] = null;
		String fillValues[] = null;
		//char ch = 'a';

		for(String str:values){
			tempValues = str.split("~~~");
			for(int i=0; i< tempValues.length ;i++){
				fillValues = tempValues[i].split(" ");
				for(int j=0;j< fillValues.length;j++){
					typeEditBox(locators[i], fillValues[j]);
				}				
			}
		}
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
