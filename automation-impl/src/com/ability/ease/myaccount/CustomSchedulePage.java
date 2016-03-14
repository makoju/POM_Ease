package com.ability.ease.myaccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
	
	public boolean verifyCustomSchedule(String agencyName, String sDay, String EndDay, String runtime, String credential,String timezone, int rownumber) throws ParseException, SQLException{
		
		int actStartDay=0;
		int actEndDay=0;
		String actCronSchedule=null;
		int failurecount=0;
		
		
		String query1 = "Select ps.StartDay,ps.EndDay,ps.cronschedule,ps.OnOff,ps.Schedule from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='"
				+ agencyName + "' and ps.JobType=10";
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
		String query1 = "INSERT INTO ddez.jobschedule select p.customerid,p.id, 10, @rownum := @rownum+1, now(),0,null,null,null,null,null,null,null,-102,null,null from provider p where p.DisplayName='"+agencyName+"'";

		if(!(MySQLDBUtil.getInsertUpdateRowsCount(query1)>0)){
			report.report("Failed to insert record into Job Schedule Table", Reporter.WARNING);
			return false;
		}
		String sQueryName = "SELECT * FROM ddez.jobschedule js where js.JobType=10 limit 1"; 
		ResultSet rs1 = MySQLDBUtil.getResultFromMySQLDB(sQueryName);
		String currentaction = MySQLDBUtil.getColumnValue(rs1, "CurrentAction");
		return (currentaction!=null && currentaction.equalsIgnoreCase("Initializing connection"))?true:false;
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
