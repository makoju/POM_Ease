package com.ability.ease.auto.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.selenium.webdriver.AbstractPageObject;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

public class Verify extends AbstractPageObject{
	
	public static boolean verifyArrayofStrings(String[] actual, String[] expected, boolean ignorecase){
		boolean isTrue=true;
		if(actual.length!=expected.length)
		{
		   report.report("Actual and Expected arrays Lengths are not equal: Actual: "+ actual.length+ "  Expected: "+expected.length);
		   return false;
		}
		else
		{
			for(int i=0;i<actual.length;i++)
			{
				if(actual[i].equals(expected[i])){
					report.report("Actual and Expected string are equal. Actual: "+ actual[i]+ "  Expected: "+expected[i], ReportAttribute.BOLD);
					continue;
				}
				else{
				  isTrue=false;
				  report.report("Actual and Expected string are not equal. Actual: "+ actual[i]+ "  Expected: "+expected[i], Reporter.WARNING);
				}
			}
		}
		return isTrue;
	}
	
	public static boolean StringEquals(String s1, String s2){
		report.report("Verifying String Equality Actual: "+s1+ " Expected: "+s2);
		if (!s1.equalsIgnoreCase(s2)){
			report.report("Actual and Expected are not equal", Reporter.WARNING);
			return false;
		}
		
		return true;
	}
	
	public static boolean StringMatches(String reportText, String regex) {
		report.report("Matching Actual Report text with expected Actual: "+reportText+" Expected: "+regex, ReportAttribute.BOLD);
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(reportText);
		return m.matches();
	}
	
	public static String getTableData(String tableidentifier, int row, int column){
		String text="";
		WebElement we = getTable(tableidentifier);
		if(we!=null)
		{
			WebElement dataelement = we.findElement(By.xpath("tbody/tr["+row+"]"+"/td["+column+"]"));
			text = dataelement.getAttribute("innerText");
			/*((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", dataelement);
			text = dataelement.getText();*/
		}
		else
			report.report("No Table found with the given identifier"+tableidentifier);
		
		return text;
	}
	
	public static List<String> getEntireTableColumnData(String tableidentifier, int column){
		List<String> actual=new ArrayList<String>();
		WebElement we = getTable(tableidentifier);
		if(we!=null){
			List<WebElement> dataelement = we.findElements(By.xpath("tbody/tr/td["+column+"]"));
			for(WebElement element:dataelement){
				actual.add(element.getText());
			}
		}
		return actual;
	}
	
	public static int getTotalTableRows(String tableidentifier){
		WebElement we = getTable(tableidentifier);
		if(we!=null){
			return we.findElements(By.xpath("tbody/tr")).size(); //excluding header row from the count
		}
		else
			report.report("No Table found with the given identifier"+tableidentifier);
		
		return -1;
	}
	
	public static boolean validateTableColumnSortOrder(String tablename, String columnname, int columnnindex){
	   waitForElement(By.xpath("//table[@id='"+tablename+"']"));
	   WebElement changecolumn = driver.findElement(By.xpath("//table[@id='"+tablename+"']"+"//td[contains(text(),'"+columnname+"')]"));
	   if(changecolumn!=null){
		String sortorder = changecolumn.getAttribute("sortdir");
		if(!sortorder.equalsIgnoreCase("down")) 
			changecolumn.click(); //click on Change column to make it in ascending sort order
		
		List<String> lsactualcolumndata = getEntireTableColumnData(tablename, columnnindex);
		List<String> lsexpectedcolumndata = lsactualcolumndata;
		Collections.sort(lsexpectedcolumndata); //which sorts elements as per natural order for strings alphabet ascending order

		 return Verify.ListEquals(lsactualcolumndata, lsexpectedcolumndata);
	   }
	   return false;
    }
	
	public static WebElement getTable(String tableidentifier) 
	{
	    String xpath="//table[@id='"+tableidentifier+"']| //span[text()='"+tableidentifier+"']/following-sibling::table";
		return waitForElement(By.xpath(xpath));
    }

	public static WebElement waitForElement(By by){
		WebElement webElement = null;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		try {
			webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (org.openqa.selenium.TimeoutException ex) {
			// ignore exception, return null instead
		}
		return webElement;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public static boolean ListEquals(List<String> lsactualcolumndata,
			List<String> lsexpectedcolumndata) {
		int failurecount=0;
		if(lsactualcolumndata!=null && lsexpectedcolumndata!=null && lsactualcolumndata.size()!=lsexpectedcolumndata.size()){
			report.report("Both the lists are of different size, Actual: "+lsactualcolumndata.size()+" Expected: "+lsexpectedcolumndata.size());
			return false;
		}
		if(lsactualcolumndata!=null && lsexpectedcolumndata!=null){
			for(int i=0;i<lsactualcolumndata.size();i++){
				if(!StringEquals(lsactualcolumndata.get(i), lsexpectedcolumndata.get(i)))
					failurecount++;
			}
		}
		else
			return false;
		
		return failurecount>0?false:true;
	}
}
