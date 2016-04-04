package com.ability.ease.common;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;




import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.system.WorkingEnvironment;

public class TestResultParser {

	static FileInputStream fis = null;
	static BufferedReader reader = null;
	static final String sFilePath = TestCommonResource.getTestResoucresDirPath()+"testresults\\ScenarioResult.txt";

	//DB specific variables
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static String DB_URL = null;
	static Connection sConnection = null;
	static Statement sStatement = null;
	static ResultSet oResultSet = null;

	//
	public static void parseTestResultAndPushDataToARTDB(){

		//List<String> testStatus = new ArrayList<String>();
		Map<String, List<Result>> resultmap = new HashMap<String, List<Result>>();
		String sValues[] = null;

		try {
			fis = new FileInputStream(sFilePath);
			reader = new BufferedReader(new InputStreamReader(fis));

			String sLine;
			String sPrevScenarioname=null;
			String sPrevModuleName=null;
			boolean isScenarioFailure=false;
			Result r=null;
			List<Result> lsScenarioResult = new ArrayList<TestResultParser.Result>();

			while((sLine = reader.readLine()) != null){
				sValues = sLine.split(",");

				String sModuleName = sValues[0];
				String sScenarioName = sValues[1];
				String sStatus = sValues[2];
				//if prev.modulename and prev.scenario name are same with the current module and scenario names then check for the scenario status
				if(sModuleName.equalsIgnoreCase(sPrevModuleName) && sScenarioName.equalsIgnoreCase(sPrevScenarioname))
				{
					if(!isScenarioFailure){
						if(sStatus.equalsIgnoreCase("fail"))
							isScenarioFailure = true;					
					}
					continue;
				}
				else if(sPrevScenarioname!=null && sPrevModuleName!=null)
				{
					if(sModuleName.equalsIgnoreCase(sPrevModuleName))
					{	
						r = new TestResultParser().new Result();
						r.setScenarioName(sPrevScenarioname);

						if(isScenarioFailure)
							r.setStatus("fail");
						else
							r.setStatus("pass");
						lsScenarioResult.add(r);
					}
					else{
						//Save the Last Result of Previous Module
						r = new TestResultParser().new Result();
						r.setScenarioName(sPrevScenarioname);

						if(isScenarioFailure)
							r.setStatus("fail");
						else
							r.setStatus("pass");
						lsScenarioResult.add(r);  
						resultmap.put(sPrevModuleName, lsScenarioResult);
						lsScenarioResult = new ArrayList<TestResultParser.Result>();
					}
				}

				//Setting flags to defaults for next scenario
				isScenarioFailure = false;
				r=null;
				sPrevModuleName = sModuleName;
				sPrevScenarioname = sScenarioName;
				if(sStatus.equalsIgnoreCase("fail"))
					isScenarioFailure = true;
			}//while close

			//Save the Last Line from the Result
			r = new TestResultParser().new Result();
			r.setScenarioName(sPrevScenarioname);

			if(isScenarioFailure)
				r.setStatus("fail");
			else
				r.setStatus("pass");
			lsScenarioResult.add(r);  
			resultmap.put(sPrevModuleName, lsScenarioResult);
			Set<String> moduleNames= resultmap.keySet();
			int moduleID = 0;
			int scenarioCount = 0, scenarioPassed = 0, scenarioFailed = 0;

			//Getting connection to ART database
			initializeDBConnection();

			//truncate art.scenario table before inserting new set of results
			/*String sTruncateQuery = "Truncate Table art.Scenario";
			executeQuery(sTruncateQuery);*/

			deleteDataFromScenarioTable();

			for(String module:moduleNames)
			{
				//Getting module ID with module name from art.module table
				oResultSet = getResultSet("Select moduleid from art.module where modulename='"+ module.split("_")[0] +"'");
				try {
					while(oResultSet.next()){
						moduleID = oResultSet.getInt("ModuleID");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				List<Result> lsScenarioResult1 = resultmap.get(module);
				for(Result r1:lsScenarioResult1){
					//Inserting scenario , status and module ID to art.scenario table
					String sInsertQuery = "Insert into art.scenario(scenarioname, scenariostatus, moduleid) values ('" + r1.getScenarioName() + "','" + r1.getStatus() + "'," + moduleID + ")";
					executeQuery(sInsertQuery);
				}
			}

			//Push Build Details into art.BuildDetails Table
			insertBuildDetails();
			//Now, push art.scenario table results to art.testresults table to display in ART dash board
			String sQuery1 = "Select count(scenarioname) from art.scenario";
			String sQuery2 = "Select count(scenariostatus) from art.scenario where scenarioStatus = 'pass'";
			String sQuery3 = "Select count(scenariostatus) from art.scenario where scenarioStatus = 'fail'";

			oResultSet = getResultSet(sQuery1);
			scenarioCount = getCoulmnValue(oResultSet, "count(scenarioname)");

			oResultSet = getResultSet(sQuery2);
			scenarioPassed = getCoulmnValue(oResultSet, "count(scenariostatus)");

			oResultSet = getResultSet(sQuery3);
			scenarioFailed = getCoulmnValue(oResultSet, "count(scenariostatus)");

			//Here, build id value should be parameterized once 
			int buildId = Integer.parseInt(WorkingEnvironment.getEasebuildId());
			String sInsertQuery = "Insert into testresults(totaltests,passed,failed,buildid) values ("+scenarioCount +"," + scenarioPassed + "," + scenarioFailed + "," + buildId +")";
			executeQuery(sInsertQuery);


		} catch (FileNotFoundException fnfe) {
			System.out.println("File :" + sFilePath + " is not present under specified path");
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("Exception while reading the "+ sFilePath + "ERROR:");
			ioe.printStackTrace();
		} 
		finally {
			try {
				reader.close();
				fis.close();
				try {
					System.out.println("statement object destroyed!");
					sStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					System.out.println("connection closed to ART database!");
					sConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException ex) {

			}
		}
		System.out.println("Test results parsing is completed !");
	}

	public static Properties readARTDBProperties(){
		Properties prop = new Properties();
		try {
			prop.load(TestResultParser.class.getResourceAsStream("/artdb.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}

	public static void initializeDBConnection(){

		Properties prop = readARTDBProperties();

		String hostName = prop.getProperty("artdbhostname");
		String port = prop.getProperty("artdbport");
		String userName = prop.getProperty("artdbusername");
		String password = prop.getProperty("artdbpassword");
		String dbname = prop.getProperty("dbname");

		DB_URL = "jdbc:mysql://" + hostName + ":" + port + "/" + dbname;
		System.out.println("DB URL : " + DB_URL);
		try{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to ART database!");
			sConnection = DriverManager.getConnection(DB_URL,userName,password);
			if( sConnection != null) {
				System.out.println("Connection successful to ART database!!!");
				//sStatement = sConnection.createStatement();
				sStatement = sConnection.createStatement
						(ResultSet.TYPE_SCROLL_SENSITIVE,
								ResultSet.CONCUR_UPDATABLE);
			}
		}catch(Exception e){
			System.out.println("Exception occured while establishing connection to MySQL database ! ERROR::");
			e.printStackTrace();
		}
	}

	public static ResultSet getResultSet(String sQuery){

		try {
			oResultSet = sStatement.executeQuery(sQuery);
		} catch (SQLException e) {
			System.out.println("Exception while executing getResultSet method...Exception is: "+ e.getMessage());
			e.printStackTrace();
		}
		return oResultSet;
	}

	public static void executeQuery(String sQuery){
		try {
			sStatement.executeUpdate(sQuery);
		} catch (SQLException e) {
			System.out.println("Exception while executing insert query on ART database ! Exeception" + e.getMessage());
			e.printStackTrace();
		}
	}

	public static int getCoulmnValue(ResultSet oResultSet, String sColumnName){

		int rowValue = 0;
		try{
			while(oResultSet.next()){
				rowValue = oResultSet.getInt(sColumnName);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rowValue;
	}

	public static void insertBuildDetails(){
		
		int buildId = 0;
		String sBuildID = WorkingEnvironment.getEasebuildId();
		String buildName = WorkingEnvironment.getEasebuildName();
		String buildDate = WorkingEnvironment.getEasebuildDate();
		buildId = Integer.valueOf(sBuildID);
		System.out.println("Ease Build ID : " + buildId);
		System.out.println("Ease Build Name : " + buildName);
		System.out.println("Ease Build Generated Date : " + buildDate);
		String insertQueryBuildDetails = "Insert into art.builddetails(BuildID, BuildName, BuildDate) values ("+ buildId +", '"+ buildName +
				"', STR_TO_DATE('" + buildDate + "', '%d-%m-%Y'));";
		System.out.println("Insert Build Details Query : " + insertQueryBuildDetails);
		executeQuery(insertQueryBuildDetails);
		System.out.println("Insertion successful !!");
	}


	public static boolean deleteDataFromScenarioTable(){

		String sDeleteQuery = "DELETE FROM art.scenario";
		String sAlterQuery = "ALTER TABLE art.scenario AUTO_INCREMENT = 1";
		int[] rowCount = null;
		try {
			System.out.println("Executing Batch Query Update To Delete Rows in Scenario Table...");
			sStatement.addBatch(sDeleteQuery);
			sStatement.addBatch(sAlterQuery);
			rowCount = sStatement.executeBatch();
		} catch (SQLException e) {
			System.out.println("Exception occured while : Executing Batch Query Update To Delete Rows in Scenario Table...");
			System.out.println("ERROR :" + e.getMessage());
			e.printStackTrace();
		}
		if( rowCount.length > 0){
			System.out.println("Executing Batch Query Update To Delete Rows In Scenario Table Is Completed Succesfully");
			return true;
		}else{
			return false;
		}
	}



	class Result
	{
		String scenarioName;
		String status;

		public String getScenarioName() {
			return scenarioName;
		}
		public void setScenarioName(String scenarioName) {
			this.scenarioName = scenarioName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

	}

}
