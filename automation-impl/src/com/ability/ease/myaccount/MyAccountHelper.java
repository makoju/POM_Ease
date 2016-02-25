package com.ability.ease.myaccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

public class MyAccountHelper extends AbstractPageObject{



	/**
	 * This method pulls the job schedule XML files from provider schedule table for a customer  
	 * @return
	 */
	/*	public List<String> getScheduleXMLFilesFromDB(String sAgencyNumber){		

		List<String> listOfXMLs = new ArrayList<String>();
		int agencyNumber = Integer.valueOf(sAgencyNumber);

		String  sSQLQueryToGetTheXMLFile ="Select ps.JobType schedule from ddez.provider p join providerschedule ps ON p.id = ps.providerid where npi = "+ agencyNumber+ ";"; 
		ResultSet rs =MySQLDBUtil.getResultFromMySQLDB(sSQLQueryToGetTheXMLFile);
		try {
			while(rs.next()){
				listOfXMLs.add(rs.getString("schedule"));				
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		MySQLDBUtil.closeAllDBConnections();
		return listOfXMLs;
	}
	 */
	
	
	

	public Map<Integer, String> getScheduleXMLFilesFromDB(String sAgencyNumber){		
		Map<Integer, String> listOfXMLs = new HashMap<Integer, String>();
		String agencyNumber = String.valueOf(sAgencyNumber);

		String  sSQLQueryToGetTheXMLFile ="Select ps.JobType, ps.Schedule from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='"+sAgencyNumber+"'"; 
		ResultSet rs =MySQLDBUtil.getResultFromMySQLDB(sSQLQueryToGetTheXMLFile);
		if(rs==null){
			report.report("Result set was null there are no records matching the select query", Reporter.WARNING);
			return null;
		}
		try {
			while(rs.next()){
				//listOfXMLs.put(rs.getString("schedule"), sSQLQueryToGetTheXMLFile);				
				listOfXMLs.put(rs.getInt("JobType"), rs.getString("schedule"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		MySQLDBUtil.closeAllDBConnections();
		return listOfXMLs;
	}
	
	/*public ArrayList<Dbcolumnsvalidatation> getCronScheduleXMLFilesFromDB(String sAgencyName){	
		
		

		ArrayList<Dbcolumnsvalidatation> listofDbvalues = new  ArrayList<Dbcolumnsvalidatation>();

			String agencyName = String.valueOf(sAgencyName);

			//String  sSQLQueryToGetTheXMLFile ="Select ps.StartDay,ps.EndDay,ps.CronSchedule, ps.JobType, ps.Schedule,ps.onoff from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='"+sAgencyName+"'";
			String  sSQLQueryToGetTheXMLFile ="Select ps.onoff, ps.StartDay,ps.EndDay,ps.CronSchedule, ps.JobType, ps.Schedule from ddez.provider p join ddez.providerschedule ps ON p.id = ps.providerid where p.DisplayName='HHA2'"+sAgencyName+"'";

			ResultSet rs =MySQLDBUtil.getResultFromMySQLDB(sSQLQueryToGetTheXMLFile);
			if(rs==null){
				report.report("Result set was null there are no records matching the select query", Reporter.WARNING);
				return null;
			}
			try {
				while(rs.next()){
					
					try{
					 Dbcolumnsvalidatation  dbcolumnsvalidatation = new  Dbcolumnsvalidatation();
					 dbcolumnsvalidatation.setAgency( rs.getString("agency"));
					
					 dbcolumnsvalidatation.setJobtype( rs.getInt("JobType"));
						dbcolumnsvalidatation.setSchedule( rs.getString("Schedule"));
						
						dbcolumnsvalidatation.setStartday( rs.getInt("Startday"));

						dbcolumnsvalidatation.setEndday( rs.getInt("endday"));
						dbcolumnsvalidatation.setCronSchedule( rs.getInt("CronSchedule"));
						listofDbvalues.add(dbcolumnsvalidatation);
						
						
						
						 listofDbvalues.add(dbcolumnsvalidatation);
					}catch(Exception e){
						System.out.println("Db "+e);
					} finally{
						
					}
				}
	
				}
			catch(Exception e)
			{
				
			}
			return null;
	}*/

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}
