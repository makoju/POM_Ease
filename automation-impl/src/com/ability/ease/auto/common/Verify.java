package com.ability.ease.auto.common;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.mydde.reports.ReportsHelper;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

import jsystem.extensions.report.html.Report;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

public class Verify extends AbstractPageObject{

	public static boolean verifyArrayofStrings(String[] actual, String[] expected, boolean ignorecase){
		boolean isTrue=true;
		if(actual.length!=expected.length)
		{
			report.report("Actual and Expected arrays Lengths are not equal: Actual: "+ actual.length+ "  Expected: "+expected.length,Reporter.WARNING);
			return false;
		}
		else
		{
			for(int i=0;i<actual.length;i++)
			{
				if(ignorecase){
					if(actual[i].equalsIgnoreCase(expected[i])){
						report.report("Actual and Expected string are equal. Actual: "+ actual[i]+ "  Expected: "+expected[i], ReportAttribute.BOLD);
						continue;
					}
					else{
						isTrue=false;
						report.report("Actual and Expected string are not equal. Actual: "+ actual[i]+ "  Expected: "+expected[i], Reporter.WARNING);
					}
				}
				else
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
		/*Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(reportText);
		return m.matches();*/
		boolean matched = reportText.matches(regex);
		if(!matched)
			report.report("Actual String: "+reportText+" doesn't match with expected String: "+regex, Reporter.WARNING);

		return matched;
	}
	
	public static String getTableData(String tableidentifier, int row, int column){
		String text=null;
		WebElement we = getTable(tableidentifier);

		if(we != null){
			WebElement dataelement = waitForElement(By.xpath("//tbody/tr["+row+"]"+"/td["+column+"]"));
			if(dataelement!=null)
				text = dataelement.getText();
			else
				report.report("Specified Element not found by xpath: //tbody/tr["+row+"]"+"/td["+column+"]");
		}
		else
			report.report("No Table found with the given identifier"+tableidentifier);
		return text;
	}

	public static List<String> getEntireTableColumnData(String tableidentifier, int column){
		//using linkedhashset to preserve the order and to avoid duplicates
		Set<String> actual=new LinkedHashSet<String>();
		WebElement we = getTable(tableidentifier);
		if(we!=null){
			List<WebElement> dataelement = we.findElements(By.xpath("tbody//tr/td["+column+"][contains(@class,'datatablecell')]"));
			for(WebElement element:dataelement){
				if(element.getText().trim().matches("[a-zA-Z0-9,/\\s+]+"))
					actual.add(element.getText());
			}
		}
		return new ArrayList<String>(actual); //converting set into list
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


	public static boolean validateSortOrderForHighLevelPaymentSummary(String tablename, String columnname, int columnnindex) throws Exception{
		String xpath = "//table[@id='"+tablename+"']"+"//div[contains(text(),'"+columnname+"')]";
		String sortorder=null;
		waitForElement(By.xpath("//table[@id='"+tablename+"']"));
		WebElement changecolumn = driver.findElement(By.xpath(xpath));
		if(changecolumn!=null){
			WebElement changecolumnparent = driver.findElement(By.xpath("//table[@id='"+tablename+"']"+"//div[contains(text(),'"+columnname+"')]/.."));
			sortorder = changecolumnparent.getAttribute("aria-sort");

			//if the column was not sorted	then sort it
			if(sortorder==null){
				changecolumn.click();
				sortorder = changecolumnparent.getAttribute("aria-sort");
			}
			if(sortorder!=null){
				int count = 0;
				while(!sortorder.equalsIgnoreCase("ascending") && count++ <10){
					changecolumn.click(); //click on Change column to make it in ascending sort order
					changecolumnparent = driver.findElement(By.xpath("//table[@id='"+tablename+"']"+"//div[contains(text(),'"+columnname+"')]/.."));
					sortorder = changecolumnparent.getAttribute("aria-sort");
				}
				if(sortorder.equalsIgnoreCase("ascending")){
					List<String> lsactualcolumndata = getEntireTableColumnData(tablename, columnnindex);
					if(lsactualcolumndata.size()>0){
						List<String> lsexpectedcolumndata = new ArrayList<String>();
						lsexpectedcolumndata.addAll(lsactualcolumndata);
						Collections.sort(lsexpectedcolumndata); //which sorts elements as per natural order for strings alphabet ascending order
						report.report("Actual: "+lsactualcolumndata+" Expected: "+lsexpectedcolumndata);
						return Verify.listEquals(lsactualcolumndata, lsexpectedcolumndata);
					}
					else
					{
						report.report("Unable to fetch data for column: "+ columnname+"  It returns empty", Reporter.WARNING);
					}
				}
				else{
					report.report("Unable to sort the column in ascending order: "+ columnname, Reporter.WARNING);
				}

			}
			else{
				report.report("Unable to click on column"+columnname, Reporter.WARNING);
			}
		}
		return false;
	}

	public static boolean validateTableColumnSortOrder(String tablename, String columnname, int columnnindex) throws Exception{

		String xpath="//table[@id='"+tablename+"']"+"//td[contains(text(),'"+columnname+"')]";
		waitForElement(By.xpath(xpath));
		WebElement changecolumn = driver.findElement(By.xpath(xpath));
		if(changecolumn!=null){
			String sortorder = changecolumn.getAttribute("sortdir");
			if(sortorder==null){ //we reach this case when the column is in unsorted state 
				safeJavaScriptClick(changecolumn);
				sortorder = changecolumn.getAttribute("sortdir");
			}
			if(sortorder!=null){ //This is when the column is in s
				int count = 0;
				//This while loop is to make sure that column is in ascending sort. sortoder down means it's in ascending sort
				while (!sortorder.equalsIgnoreCase("down") && count++ <10){ //Try for 10 times before give it up
					safeJavaScriptClick(changecolumn); //click on Change column to make it in ascending sort order
					changecolumn = driver.findElement(By.xpath(xpath));
					sortorder = changecolumn.getAttribute("sortdir");
				}
				if(sortorder.equalsIgnoreCase("down")){
					List<String> lsactualcolumndata = getEntireTableColumnData(tablename, columnnindex);
					if(lsactualcolumndata.size()>0){
						List<String> lsexpectedcolumndata = new ArrayList<String>();
						lsexpectedcolumndata.addAll(lsactualcolumndata);
						Collections.sort(lsexpectedcolumndata); //which sorts elements as per natural order for strings alphabet ascending order
						report.report("Actual: "+lsactualcolumndata+" Expected: "+lsexpectedcolumndata);
						return Verify.listEquals(lsactualcolumndata, lsexpectedcolumndata);
					}
					else
					{
						report.report("Unable to fetch data for column: "+ columnname+"  It returns empty", Reporter.WARNING);
					}
				}
				else
				{
					report.report("Unable to sort the column in ascending order: "+ columnname, Reporter.WARNING);
				}

			}else{
				report.report("Unable to click on column"+columnname, Reporter.WARNING);
			}
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
			report.report("Waited for 30 secs for the element to visible but not found. So, giving up: "+by.toString());
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

	public static boolean listEquals(List<String> lsactualcolumndata,List<String> lsexpectedcolumndata) {
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
	public static boolean datewithinDateRange(String actualdate,String startdate,String enddate) throws ParseException{
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		if(actualdate!=null && startdate!=null && enddate!=null){
			Date actdate = format.parse(actualdate);
			Date stdate = format.parse(startdate);
			Date eddate = format.parse(enddate);
			return !(actdate.before(stdate) || actdate.after(eddate));
		}
		report.report("Either of actualdate,startdate and enddate are null: "+actualdate+","+startdate+","+enddate, Reporter.WARNING);
		return false;
	}

	public static boolean currencyValidator(String amountinusdollars){
		BigDecimalValidator validator = CurrencyValidator.getInstance();

		BigDecimal amount = validator.validate(amountinusdollars, Locale.US);
		/*     if(amount!=null){
     //remove the brackets and , since this is something unusual
     String in = amountinusdollars.replaceAll("\\(", "").replace(')', ' ').trim();
     in=in.replaceAll(",", "");

     amount = validator.validate(in, Locale.US);
     assertNotNull(amount);
     }*/
		return amount!=null?true:false;
	}

	public static boolean verifyTableColumnNames(String tableIdentifier, String[] expected){
		int i=0,faliureCount=0; 
		boolean isColFound = false; 

		//Waiting for 2 seconds before the page load finish
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if(isTextPresent("EASE found no items for this report") || isTextPresent("No changes have been detected for any patients")){
				report.report("EASE found no items for this report",ReportAttribute.BOLD);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<WebElement> allColumns =new ReportsHelper().getReportTableHeaders(tableIdentifier);
		if(allColumns == null){
			return false;
		}
//		String[] actualColNames = new String[allColumns.size()];
		ArrayList<String> actualColNames = new ArrayList<String>();
		for(WebElement column : allColumns){
			if(!column.getText().equals("")){
//				actualColNames[i++] = column.getText();
				actualColNames.add(column.getText());
			}
		}
		for(String s : expected){
			for(String actual : actualColNames){
				if(actual.trim().equalsIgnoreCase(s.trim())){
					report.report("Expected column is displayed : " + s,ReportAttribute.BOLD);
					isColFound = true;
					break;
				}
			}
			if(!isColFound){
				faliureCount++;
			}
			isColFound = false;
		}
		return faliureCount == 0? true:false;
	}

	/**
	 * nageswar.bodduri
	 * @param array1
	 * @param array2
	 * @return true if both arrays are identical; false otherwise
	 */
	public static boolean compareFloatArrays(float[] array1,float[] array2){
		boolean result = false;
		if( array1.length > 0 && array2.length > 0) {
			result =  Arrays.equals(array1, array2);
		}else{
			report.report("One or both arrays are empty, hence can not compare");
			result = false;
		}
		return result;
	}

	/**
	 * nageswar.bodduri
	 * @param lsTableHeaderElements : list of table header webelements
	 * @param expectedTableHeaderNames : string array of expected column names
	 * @return true if table column names are same , false other wise
	 */
	public static boolean compareTableHeaderNames(List<WebElement> lsTableHeaderElements, String expectedTableHeaderNames){

		boolean result = false;
		int passCounter = 0;
		int i = 0;
		String[] expectedTableHeaderNamesArray = expectedTableHeaderNames.split(",");

		report.report("Inside compare table header names method in Verify class");

		for(WebElement column:lsTableHeaderElements){
			if(column.getText().trim().equalsIgnoreCase(expectedTableHeaderNamesArray[i])){
				report.report("Expected column name : " + expectedTableHeaderNamesArray[i] + " is equal to Actual column name : " + column.getText().trim());
				passCounter++;
			}else{
				report.report("Expected column name : " + expectedTableHeaderNamesArray[i] + " is not equal to Actual column name : " + column.getText().trim());
			}
			i++;
		}
		if( passCounter == expectedTableHeaderNamesArray.length){
			result = true;
			report.report("Table column names verified succssfully");
		}else{
			result = false;
			report.report("Fail : Table column names are not identical");
		}
		return result;
	}


}
