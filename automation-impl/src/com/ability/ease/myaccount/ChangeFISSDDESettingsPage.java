package com.ability.ease.myaccount;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.FailedLoginException;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class ChangeFISSDDESettingsPage extends AbstractPageObject {
	
	public boolean verifyOptionsUnderAddFISSDDESetup() throws Exception {
		int failurecount=0;
		
		navigateToPage();
		clickLink("Add");
		WebElement we = waitForElementVisibility(By.id("lblgroupname"));
		if(we==null){
			report.report("Group Name not found in Add FISS/DDE Setup screen", Reporter.WARNING);
			failurecount++;
		}

		WebElement weSelect = waitForElementVisibility(By.id("UserProvID"));
		if(weSelect==null){
			report.report("Agency List Box not found in Add FISS/DDE Setup screen", Reporter.WARNING);
			failurecount++;
		}
		
		WebElement weSubmit = waitForElementToBeClickable(ByLocator.xpath, "//input[@value='Submit']", 10);
		if(weSubmit == null){
			report.report("Submit button not found in Add FISS/DDE Setup screen", Reporter.WARNING);
			failurecount++;
		}
		
		return failurecount==0?true:false;
	}
	
	public boolean addFISSDDESetup(String username, String groupname, String agencies, boolean isnewcustomer) throws Exception {

		if(!isTextPresent("ADD FISS/DDE SETUP")){
			navigateToPage();
			if(!isnewcustomer)
				clickLink("Add");	
		}
		typeEditBox("name", groupname);
		String[] agencieslist = agencies.split(",");
		for(String agency:agencieslist)
			selectByNameOrID("UserProvID", agency);
		clickButtonV2("Submit");
		
		if(!verifyAlert("Information saved!")){
			report.report("Failed to add FISS/DDE Settings", Reporter.WARNING);
			return false;
		}
		else
			report.report("Successfully setup FISS/DDE Settings", ReportAttribute.BOLD);
		
		//verify GroupName in DB
		boolean isgroupfound=false;
		int failurecount=0;
		String query1 = "select cred.GroupName from ddez.userddecredential cred where cred.DDEPasswordHolder=(select u.userid from ddez.user u where u.userName='"+username+"')";
		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(query1);
		while(rs.next()){
			if(rs.getString(1).trim().equalsIgnoreCase(groupname)){
				report.report("Added GroupName "+groupname+ " Was found in DB", ReportAttribute.BOLD);
				isgroupfound=true;
				break;
			}
		}
		if(!isgroupfound){
			report.report("Added Group was not found in DB",Reporter.WARNING);
			failurecount++;
		}
		
	/*	String query2="select provider.ProviderID from ddez.userddecredentialprovider provider where provider.DDEPasswordHolder=(select u.userid from ddez.user u where u.userName='"+username+"') and provider.GroupID="+
"(Select cred.GroupID from ddez.userddecredential cred where cred.DDEPasswordHolder=(select userid from ddez.user where userName='"+username+"') and cred.GroupName='"+groupname+"')";
		ResultSet rs1 = MySQLDBUtil.getResultFromMySQLDB(query2);
		String[] expected=new String[agencieslist.length];
		int i=0;
		
		while(rs1.next()){
			expected[i++] = rs1.getString(0).trim();
		}*/
		
		//Verify Agencies under groupName in UI
		String[] actual = new String[agencieslist.length];
		int i=0;
		
		clickLink("Edit");
		WebElement selectagencies = waitForElementVisibility(By.id("UserProvID"));
		Select select = new Select(selectagencies);
		List<WebElement> lsselected = select.getAllSelectedOptions();
		for(WebElement we:lsselected){
			actual[i++] = we.getText().trim();
		}
		
		Arrays.sort(actual);
		Arrays.sort(agencieslist);
		if(!Verify.verifyArrayofStrings(actual, agencieslist, true))
		{
			report.report("Actual and expected agencies selected mismatch. Actual:"+ actual+" Expected: "+agencies);
			failurecount++;
		} 
		
		return failurecount==0?true:false;
	}
	
	public boolean setupDDECredential(String groupname, String ddeuserid, String ddepassword) throws Exception{
		navigateToManageDDESettingsPage(groupname);
		WebElement we = waitForElementVisibility(By.id("ddeuser"));
		if(we!=null){
			typeEditBox("ddeuser", ddeuserid);
			typeEditBox("ddepassword", ddeuserid);
			typeEditBox("Verify", ddeuserid);
			clickButtonV2("Submit");
			WebDriverWait wait = new WebDriverWait(driver, 85);
			wait.until(ExpectedConditions.alertIsPresent());
			
			if(!verifyAlert("DDE information changed")){
				report.report("DDE Information Submitted was not acknowledged",Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Unable to navigate to DDE Setup page", Reporter.WARNING);
			return false;
		}

		return true;
	}
	public boolean editFISSDDESetup(String groupname, String agencies) throws Exception{
		navigateToPage();
		WebElement we = waitForElementVisibility(By.linkText("Password Protection"));
		if(we == null){
			WebElement select = waitForElementVisibility(By.id("group"));
			if(select!=null)
				selectByNameOrID("group", groupname);
			we = waitForElementVisibility(By.id("ddeuser"));
		}
		if(we!=null){
			clickLink("Edit");

			if(!agencies.isEmpty())
			{
				WebElement selectagencies = waitForElementVisibility(By.id("UserProvID"));
				Select select = new Select(selectagencies);
				select.deselectAll();

				String[] agencieslist = agencies.split(",");
				for(String agency:agencieslist)
					selectByNameOrID("UserProvID", agency);
			}
			clickButtonV2("Submit");
			if(!verifyAlert("Information saved!")){
				report.report("Failed to edit FISS/DDE Settings of group"+groupname, Reporter.WARNING);
				return false;
			}
			else
				report.report("Successfully edited FISS/DDE Settings of a group"+groupname, ReportAttribute.BOLD);
		}
		
		else
		{
			report.report("Unable to navigate to DDE Setup page", Reporter.WARNING);
			return false;
		}
		return true;
	 }
	
	public boolean editDDECredential(String groupname, String ddeuserid, String ddepassword) throws Exception{
		navigateToPage();
		WebElement we = waitForElementVisibility(By.linkText("Password Protection"));
		if(we == null){
			report.report("Multiple groups visible under FISS settings...selecting one");
			WebElement select = waitForElementVisibility(By.id("group"));
			if(select!=null)
				selectByNameOrID("group", groupname);
		}
		we = waitForElementVisibility(By.id("ddeuser"));
		if(we!=null){
			typeEditBox("ddeuser", ddeuserid);
			typeEditBox("ddepassword", ddeuserid);
			typeEditBox("Verify", ddeuserid);
			clickButtonV2("Submit");
			WebDriverWait wait = new WebDriverWait(driver, 85);
			wait.until(ExpectedConditions.alertIsPresent());
			
			if(!verifyAlert("DDE information changed")){
				report.report("DDE Information Submitted was not acknowledged",Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Unable to navigate to DDE Setup page", Reporter.WARNING);
			return false;
		}

		return true;
	}
	
	
	public boolean removeFISSDDESetup(String groupname) throws Exception{
		navigateToPage();
		WebElement we = waitForElementVisibility(By.linkText("Password Protection"));
		if(we == null){
			WebElement select = waitForElementVisibility(By.id("group"));
			if(select!=null)
				selectByNameOrID("group", groupname);
		}
		we = waitForElementVisibility(By.id("ddeuser"));
		if(we!=null){
			clickLink("Remove");
			verifyAlert("Are you sure you wish to delete the group?");
			if(!verifyAlert("Information removed!")){
				report.report("Failed to remove FISSDDE settings of group"+groupname,Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Unable to navigate to DDE Setup page", Reporter.WARNING);
			return false;
		}

		return true;
	}
	
	public void navigateToManageDDESettingsPage(String groupname) throws Exception{
		navigateToPage();
		WebElement we = waitForElementVisibility(By.linkText("Password Protection"));
		if(we == null){
			WebElement select = waitForElementVisibility(By.id("group"));
			if(select!=null)
				selectByNameOrID("group", groupname);
		}
	}
	//There are jobs scheduled in this time window. Please change the start time. Job Scheduled time : 09:00 
	
	public boolean configureBlackoutTime(String groupname, String starttime,
			String endtime, String expectedalertmessage) throws Exception {
		navigateToManageDDESettingsPage(groupname);
		WebElement we = waitForElementVisibility(By.id("restrict_usage"));
		if(we!=null){
			typeEditBox("ddepassword", "test");
			typeEditBox("Verify", "test");
			uncheckChkBox("restrict_usage");
			selectByNameOrID("credential_start_time", starttime);
			selectByNameOrID("credential_end_time", endtime);
			clickButtonV2("Submit");
			
			WebDriverWait wait = new WebDriverWait(driver, 85);
			wait.until(ExpectedConditions.alertIsPresent());
			
			if(!verifyAlert(expectedalertmessage)){
				report.report("Expected alert: "+expectedalertmessage+"was not present",Reporter.WARNING);
				return false;
			}
			return true;
		}
		else{
			report.report("Unable to navigate or No Usage time Restriction checkbox is not available in Manage FISS/DDE Settings page", Reporter.WARNING);
			return false;
		}
		
	}

	
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("MANAGE FISS/DDE SETTINGS") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
		}
		if(waitForElementToBeClickable(ByLocator.linktext, "Change FISS/DDE Settings", 5)==null)
			HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
		clickLink("Change FISS/DDE Settings");
	}
}
