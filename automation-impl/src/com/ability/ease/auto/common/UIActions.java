package com.ability.ease.auto.common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import jsystem.framework.report.Reporter.ReportAttribute;

import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
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
					String[] sLocator42To49 = scrAttr.getLocator().split(",");
					String[] sValue = scrAttr.getValue().split(",");
					int j=1;
					
					for(String str:sValue){
						String[] sValues42To49 = str.split(":");
						for(int i=0;i < sLocator42To49.length; i++){
							typeEditBox(sLocator42To49[i]+"_"+j, sValues42To49[i]);
						}
						j++;
						if( j >= 10){
							
						}
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
						typeEditBox("reportCustomDateFrom", fromdate);
						typeEditBox("reportCustomDateTo", todate);
						clickButton("reportTimeframeButton");
					}
					else 
						clickLink(timeframe.trim());
					break;
					
				case Agency:
					clickLink(scrAttr.getLocator());
					selectByNameOrID("reportAgencySelect", scrAttr.getValue().trim());
					clickButton("Change Agency");
					break;
					
				default:
					report.report("The attribute Style is not implemented.",
							ReportAttribute.BOLD);
					break;
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
