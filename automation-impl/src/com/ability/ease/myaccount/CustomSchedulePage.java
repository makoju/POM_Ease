package com.ability.ease.myaccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

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

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

public class CustomSchedulePage extends AbstractPageObject {

	MyAccountHelper helper = new MyAccountHelper();

	public boolean submitCustomSchedule(Map<String, String> mapAttrValues) throws Exception {

		navigateToPage();
		clickLink("Change Schedule");

		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml/myaccount/ChangeSchedule.xml",
				mapAttrValues);

		UIActions changeSchedule = new UIActions();
		changeSchedule.fillScreenAttributes(lsAttributes);
		
		clickButtonV2("Save");
		if(!verifyAlert("Custom Schedule saved successfully!")){
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
	
	public boolean verifyJobScheduleCurrentAction(String agencyName){
		String query1 =  "DELETE From ddez.jobschedule where providerid=(select p.id from ddez.provider p where p.DisplayName='"+agencyName+"'  and customerid='1')";
		String query2 = "INSERT INTO ddez.jobschedule (CustomerID,ProviderID,JobType,ScheduleTime,SchedulePriority,Trace) select p.customerid,p.id, 10, now(),0,-102 from ddez.provider p where p.DisplayName='"+agencyName+"' and customerid='1'";
		
		//Delete the entry from job schedule table if it already exists
		MySQLDBUtil.getUpdateResultFromMySQLDB(query1);
		
		if(!(MySQLDBUtil.getInsertUpdateRowsCount(query2)>0)){
			report.report("Failed to insert record into Job Schedule Table", Reporter.WARNING);
			return false;
		}
		//Wait for 5 seconds to refresh in database
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get the last row from the jobschedule table whose jobtype is 10
		String sQueryName = "SELECT * FROM ddez.jobschedule js where js.JobType=10 order by jobid desc limit 1"; 
		ResultSet rs1 = MySQLDBUtil.getResultFromMySQLDB(sQueryName);
		String currentaction = MySQLDBUtil.getColumnValue(rs1, "CurrentAction");
		return (currentaction!=null && Verify.StringEquals(currentaction, "Initializing connection"))?true:false;
	}
		
/*
	// Start day and endday validation from DB
	public boolean verifyStartDayEndDay(Map<String, String> mapAttrValues,String startDayEnddayConfValue, String agencyName) throws Exception {
		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute> lsAttributes = parser.getUIAttributesFromXMLV2(TestCommonResource.getTestResoucresDirPath() + "uiattributesxml/myaccount/ChangeSchedule.xml",mapAttrValues);

		UIActions changeSchedule = new UIActions();
		changeSchedule.fillScreenAttributes(lsAttributes);
	
		boolean flag = false;
		String customschedulervalue =mapAttrValues.get("custom");
		
		StringBuilder DBtmpData = new StringBuilder();
		String query1 = "Select ps.StartDay,ps.EndDay from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='"
				+ agencyName + "'";

		// Select ps.StartDay,ps.EndDay from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='HHA2'

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(query1);
		try {
			while (rs.next()) {
				DBtmpData.append(rs.getInt("startday"));
				DBtmpData.append(",");
				DBtmpData.append(rs.getInt("enday"));
				DBtmpData.append(";");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return startDayEnddayConfValue.equals(DBtmpData.toString());
	}

	// validation for Cronformat to CST
	public boolean verifyRuntimeCronscheduleToCST(int hours ,String agency ){
					String customschedule = scrAttr.getLocator();
					String[] customschedulevalue = scrAttr.getValue().split(";");
					
					for(int i=1;i<=count;i++){
					
					ArrayList cronList = new ArrayList();
					StringBuilder tmpData = new StringBuilder();
					//tmpData.append(count);
					//tmpData.append(";");
					for(int i=1;i<=count;i++){
										
						String[] schedule = customschedulevalue[i].split(",");
						tmpData.append(schedule[0].trim()).append(",").append(schedule[1].trim()).append(";");//10,0;20,11;30,21;
						cronList.add(schedule[2].trim());
					
					
					String xmlruntimeSchedule ="Select cronschedule from providerSchedule where agencyName ='"+agency+"'";
					ResultSet rs =MySQLDBUtil.getResultFromMySQLDB(xmlruntimeSchedule);
					try {
						while(rs.next()){
						if (hours == rs.getInt(hours))
							return true;
						else
							return false;
						
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//tmpData.deleteCharAt(tmpData.length());
					boolean isStartDayEndDayValueValid = verifyStartDayEndDay(tmpData.toString(), agency);
					boolean isCronTimeCorrect = true;
					for(String cron : cronList){
						int tempConTime = convertToCronTimeCSTTimeZone(cron, sTimeZone);
						isCronTimeCorrect = verifyRuntimeCronscheduleToCST(tempConTime, agency);
						if(isCronTimeCorrect == false)
							break;
					}
					
					
					if(isCronTimeCorrect == false)
						return false;
					}
					
					if(verifyOnoff(11, booleanToInt(isChecked("Schdule_onoff_0"))) == false || verifyOnoff(12, booleanToInt(isChecked("Schdule_onoff_1"))))
						return false;
					
					
					}
			}

	public int booleanToInt(boolean input) {
		if (input)
			return 1;
		else
			return 0;

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

	}*/

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
