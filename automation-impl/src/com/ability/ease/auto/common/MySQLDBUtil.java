package com.ability.ease.auto.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.ability.ease.auto.system.WorkingEnvironment;

/***
 * This class deals with my sql db actions,it contains methods to read connection arguments from globalparameter section of jsystem,
 * initialize db connection and some other reusable methods
 * @author nageswar.bodduri
 *
 */

public class MySQLDBUtil {

	//driver and db URI details
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static String DB_URL = null;

	//  Database credentials
	private static String hostName = null;
	private static String dbname = null;
	private static String port = null;
	private static String userName = null;
	private static String password = null;

	static Connection connection = null;
	static Statement statement = null;
	static ResultSet resultSet = null;

	//reading connection parameters from working environment - Jsystem sut file
	public static void readConnParamsFromWE(){
		hostName = WorkingEnvironment.getMySQLDBHostname();
		port = WorkingEnvironment.getMySQLDBPort();
		userName = WorkingEnvironment.getMySQLDBUser();
		password = WorkingEnvironment.getMySQLDBPassword();
		dbname = WorkingEnvironment.getMySQLDBName();
		DB_URL = "jdbc:mysql://" + hostName + ":" + port + "/" + dbname;
	}

	public static void initializeDBConnection(){
		try{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to mysql database...");
			connection = DriverManager.getConnection(DB_URL,userName,password);
			if( connection != null) {
				System.out.println("Connection successful to database !");
				statement = connection.createStatement(); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static ResultSet getResultFromMySQLDB(String sQueryName){
		readConnParamsFromWE();
		initializeDBConnection();
		try{
			resultSet = statement.executeQuery(sQueryName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultSet;
	}
	
	public static int getUpdateResultFromMySQLDB(String sQueryName){
		readConnParamsFromWE();
		initializeDBConnection();
		int noOfrowsUpdated = 0;
		try {
			noOfrowsUpdated = statement.executeUpdate(sQueryName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noOfrowsUpdated;
	}
	
	public static int getInsertUpdateRowsCount(String sQueryName){
		readConnParamsFromWE();
		initializeDBConnection();
		try{
			return statement.executeUpdate(sQueryName);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * Method to return a specific column value
	 */
	public static String getColumnValue(ResultSet oResultSet, String sColumnName){
		String sColumnValue = null;
		int iColumnValue = 0;

		ResultSetMetaData metaData=null;
		try {
			metaData = oResultSet.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			for (int i = 1; i <= metaData.getColumnCount(); i++) {

				int type = metaData.getColumnType(i);
				while( oResultSet.next()){
					if (type == Types.VARCHAR) {
						sColumnValue = oResultSet.getString(sColumnName);
						return sColumnValue;
					}else if(type == Types.INTEGER){
						iColumnValue = oResultSet.getInt(sColumnName);
						return String.valueOf(iColumnValue);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/* Added to fetch all column values from database*/
	public static List<String> getMultipleColumnValues(ResultSet oResultSet, String[] sColumnNames){
		String sColumnValue = null;
		int iColumnValue = 0;
		int j = 0;
		List<String> lsValues=new ArrayList<String>();
		ResultSetMetaData metaData=null;
		try {
			metaData = oResultSet.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while(oResultSet.next()){
				//check whether getColumncount and Number column names are equal or not
				for (int i = 1; i <= metaData.getColumnCount(); i++)  {
					j = i-1;
					//for(String sColumnName: sColumnNames){
					int type = metaData.getColumnType(i);
					if (type == Types.VARCHAR) {
						sColumnValue = oResultSet.getString(sColumnNames[j]);
						lsValues.add(sColumnValue);
					} else if (type == Types.INTEGER) {
						iColumnValue = oResultSet.getInt(sColumnNames[j]);
						lsValues.add(String.valueOf(iColumnValue));
					} else if (type == Types.TINYINT) {
						iColumnValue = oResultSet.getInt(sColumnNames[j]);
						lsValues.add(String.valueOf(iColumnValue));
					}
					else if (type == Types.DATE) {
						iColumnValue = oResultSet.getInt(sColumnNames[j]);
						lsValues.add(String.valueOf(iColumnValue));
					}
					else if (type == Types.BIT) {
						iColumnValue = oResultSet.getInt(sColumnNames[j]);
						lsValues.add(String.valueOf(iColumnValue));
					}					
					else if (type == Types.BIGINT) {
						iColumnValue = oResultSet.getInt(sColumnNames[j]);
						lsValues.add(String.valueOf(iColumnValue));
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lsValues;
	}


	public static void closeAllDBConnections(){
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
