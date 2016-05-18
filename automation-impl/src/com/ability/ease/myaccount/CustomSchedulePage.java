package com.ability.ease.myaccount;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.TimeZoneConversionUtil;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;
import com.ability.ease.selenium.webdriver.WebDriverHelper;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

public class CustomSchedulePage extends AbstractPageObject {

	MyAccountHelper helper = new MyAccountHelper();

	public boolean submitCustomSchedule(Map<String, String> mapAttrValues, String expectedalertmessage) throws Exception {

		navigateToPage();
		clickLink("Change Schedule");

		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml/myaccount/ChangeSchedule.xml",
				mapAttrValues);

		UIActions changeSchedule = new UIActions();
		changeSchedule.fillScreenAttributes(lsAttributes);
		
		clickButtonV2("Save");
		//"Custom Schedule saved successfully!"
		if(!verifyAlert(expectedalertmessage)){
			report.report("Custom schedule setup was unsuccessful");
			return false;
		}
		return true;
	}
	
	public boolean deleteAllCustomSchedule(String agency) throws Exception {
		navigateToPage();
		clickLink("Change Schedule");
		selectByNameOrID("user_prov_id", agency);
		
		clickButton("Custom");
		if(waitForElementVisibility(By.xpath("//td[text()='ASSIGN CUSTOM SCHEDULE']"))==null){
			report.report("Unable to navigate to custom schedule page from change schedule", Reporter.WARNING);
			return false;
		}
		clickButtonV2("Delete All");
		if(!verifyAlert("Are you sure to remove all the entries?"))
		{
			report.report("unable to delete alerts from custom schedule page", Reporter.WARNING);
			return false;
		}
		
		return true;
	}
	
	public boolean verifyCustomSchedule(String agencyName, String sDay, String EndDay, String runtime, String credential,String timezone, int rownumber) throws ParseException, SQLException{
		
		int actStartDay=0;
		int actEndDay=0;
		String actCronSchedule=null;
		int failurecount=0;
		
		//Assuming it's verifying with test.customer hence harcoded customer id as '1'. Have to be modified to support for other customers too 
		String query1 = "Select ps.StartDay,ps.EndDay,ps.cronschedule,ps.OnOff,ps.Schedule from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='"
				+ agencyName + "' and ps.JobType='10' and p.customerid='1' and ps.StartDay!=''";
		
		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(query1);
		int count = 1;
		while(rs.next()){
			if(count == rownumber){
				actStartDay = rs.getInt(1);
				actEndDay = rs.getInt(2);
				actCronSchedule = rs.getString(3);
				break;
			}
			count++;
		}
		
		if(!(Integer.parseInt(sDay) == actStartDay)){
			report.report("Actual And Expected Start Days are not equal. Actual: "+actStartDay+" Expected: "+sDay,Reporter.WARNING);
			failurecount++;
		}
		
		if(!(Integer.parseInt(EndDay) == actEndDay)){
			report.report("Actual And Expected Start Days are not equal. Actual: "+actEndDay+" Expected: "+EndDay,Reporter.WARNING);
			failurecount++;
		}
		
		String expectedcronshedule = TimeZoneConversionUtil.convertToCronTimeCSTTimeZone(runtime, timezone);
		if(!Verify.StringEquals(actCronSchedule, expectedcronshedule)){
			report.report("Actual and expected cronschedulers do not match", Reporter.WARNING);
			failurecount++;
		}
		
		return failurecount==0?true:false;
	}
	
	public boolean verifyJobScheduleCurrentAction(String agencyName, String jobtype, String customerid){
		String currentime = new String(""+System.currentTimeMillis());
		String jobid = currentime.substring(currentime.length()-5, currentime.length());
		
		
		String query1 =  "DELETE From ddez.jobschedule where providerid in (select p.id from ddez.provider p where p.DisplayName='"+agencyName+"'  and p.customerid="+customerid+")";
		String query2 = "INSERT INTO ddez.jobschedule (CustomerID,ProviderID,JobType,JobID,ScheduleTime,SchedulePriority,Trace) select p.customerid,p.id,"+jobtype+","+jobid+",now(),0,0 from ddez.provider p where p.DisplayName='"+agencyName+"' and p.customerid="+customerid;
		
		//Delete the entry from job schedule table if it already exists
		MySQLDBUtil.getUpdateResultFromMySQLDB(query1);
		
		if(!(MySQLDBUtil.getInsertUpdateRowsCount(query2)>0)){
			report.report("Failed to insert record into Job Schedule Table", Reporter.WARNING);
			return false;
		}
		
		//Wait for 10 seconds to refresh in database
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//get the last row from the jobschedule table whose jobtype is 10
		String sQueryName = "SELECT * FROM ddez.jobschedule js where js.JobType="+jobtype+" and js.customerid ="+customerid+" order by jobid desc limit 1";
		ResultSet rs1 = MySQLDBUtil.getResultFromMySQLDB(sQueryName);
		String currentaction = MySQLDBUtil.getColumnValue(rs1, "CurrentAction");
		return (currentaction!=null && Verify.StringEquals(currentaction, "Initializing connection"))?true:false;
	}
		
	public boolean verifyInvalidDDEcredentials(String agency, String credential) throws Exception {
		navigateToPage();
		clickLink("Change Schedule");
		selectByNameOrID("user_prov_id", agency);
		clickButton("Custom");

		WebElement schedulecredential = waitForElementVisibility(By.name("schedule_credential"));
		Select selectcredential = new Select(schedulecredential);
		List<WebElement> listElements = selectcredential.getOptions();
		for (WebElement element : listElements) {
			if(element.getText().contains(credential)){
			  String actualColor = element.getAttribute("style");
		      if(actualColor.contains("color: red"))
		      {
		    	  report.report("Specified Credential"+ credential+" is colored red", ReportAttribute.BOLD);
		    	  return true;
		      }
			}
		}
		report.report("The Provided Credential"+credential+" either not found in the schedule credential dropdown or is not colored in red", Reporter.WARNING);
		return false;
	}
	
	public boolean updateDDEpasswordProblem(String username, int value) throws SQLException {

		int noOfrowsUpdated = 0;

		String query = "update ddez.userddecredential set DDEPasswordProblem='"+value+"', LoginErrorMessage='problem' where DDEPasswordHolder IN (Select UserID from ddez.user where username='"
				+ username + "')";
		try {
			noOfrowsUpdated = MySQLDBUtil.getUpdateResultFromMySQLDB(query);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
				MySQLDBUtil.closeAllDBConnections();
		}

		return noOfrowsUpdated>=1?true:false;

	}    
	
	public boolean verifyDeleteOptionNotEnableforFirstSchedule(String agency) throws Exception{
		navigateToPage();
		clickLink("Change Schedule");
		selectByNameOrID("user_prov_id", agency);
		clickButton("Custom");
		if(!isTextPresent("ASSIGN CUSTOM SCHEDULE"))
		{
			report.report("Unable to navigate to Custom Schedule page", Reporter.WARNING);
			return false;
		}
		
		WebElement deleteimage = waitForElementVisibility(By.xpath("//*[@id='schedulerTable']/tbody/tr[3]/td[5]/img"));
		if(deleteimage!=null && deleteimage.isEnabled())
		{
			report.report("Delete Image for first Schedule Row of custom schedule was enabled but it's expected to be disabled", Reporter.WARNING);
			return false;
		}
		return true;
	}

	public boolean deletecustomScheduleRows(String agency) throws Exception {
		int failurecount=0;
		navigateToPage();
		clickLink("Change Schedule");
		selectByNameOrID("user_prov_id", agency);
		clickButton("Custom");
		if(!isTextPresent("ASSIGN CUSTOM SCHEDULE"))
		{
			report.report("Unable to navigate to Custom Schedule page", Reporter.WARNING);
			return false;
		}

		List<String> scheduleIds = new ArrayList<String>();

		boolean flag = true;

		List<WebElement> deleteElements = driver.findElements(By.xpath("//img[@src='assets/images/delete.png']"));
		//First element refers to first row where delete image was disabled.So, Skipping that and taking a sublist after that
		deleteElements = deleteElements.subList(1, deleteElements.size());
		
		for (WebElement deleteimages : deleteElements) {
		  if(deleteimages.isEnabled()){
			String s = deleteimages.getAttribute("onclick");   // onclick=deleteRow(this,55591)
			scheduleIds.add(s.replace("deleteRow(this,", "").replace(")", ""));
			report.report("schedle ID"+scheduleIds);
			deleteimages.click();
			if (!verifyAlert("Are you sure you want to delete the row ?")) {
				report.report("unable to delete alerts from custom schedule page", Reporter.WARNING);
				failurecount++;
			}
		  }
		}
		clickButtonV2("Save");

		if (!verifyAlert("Custom Schedule saved successfully!")) {
			report.report("unable to save deleted alerts from custom schedule page", Reporter.WARNING);
			failurecount++;
		}
		//verify the deletion in DB
		for (String scheduleId : scheduleIds) {
			if (isRecordExist(scheduleId)){
				report.report("Deleted custom schedule not removed from DB", Reporter.WARNING);
				failurecount++;
			}
		}
		return failurecount==0?true:false;
	}

	public boolean isRecordExist(String providerSchedule) throws SQLException {
		int providerScheduleId = Integer.parseInt(providerSchedule);
		MySQLDBUtil.initializeDBConnection();

		String query = "SELECT providerschedule_ID FROM ddez.providerschedule where providerschedule_ID="
				+ providerScheduleId;

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(query);

		if (rs != null && rs.next()) {

			return (providerScheduleId == rs.getInt("providerSchedule_ID"));

		}
		return false;
	}
		
	private static Document convertStringToDocument(String xmlStr) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			// Document doc = builder.parse( new InputSource( new StringReader(
			// xmlStr ) ) );
			return builder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
		} catch (Exception e) {

		}
		return null;

	}
	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("Change Schedule") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
		}
	}
}
