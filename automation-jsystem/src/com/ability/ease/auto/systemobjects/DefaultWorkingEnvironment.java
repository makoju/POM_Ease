package com.ability.ease.auto.systemobjects;

import jsystem.framework.system.SystemObjectImpl;

/**
 * A system object that holds the minimum information 
 * require to run tests against a specified environment
 * 
 */
public class DefaultWorkingEnvironment extends SystemObjectImpl{

	// variables require for runtime
		private String host;
		private String port;
		private String mySQLDBHostName;
		private String mySQLDBName;
		private String mySQLDBUserName;
		private String mySQLDBPassword;
		private String mySQLDBPort;
		private String mailServerHost = "abc.senecaglobal.com";
		private String mailServerUser = "administrator";
		private String mailServerPassword = "adminpassword";		
	
	
		// Getters & Setters
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}

		public String getMySQLDBHostName() {
			return mySQLDBHostName;
		}
		public void setMySQLDBHostName(String mySQLDBHostName) {
			this.mySQLDBHostName = mySQLDBHostName;
		}
		public String getMySQLDBName() {
			return mySQLDBName;
		}
		public void setMySQLDBName(String mySQLDBName) {
			this.mySQLDBName = mySQLDBName;
		}
		public String getMySQLDBUserName() {
			return mySQLDBUserName;
		}
		public void setMySQLDBUserName(String mySQLDBUserName) {
			this.mySQLDBUserName = mySQLDBUserName;
		}
		public String getMySQLDBPassword() {
			return mySQLDBPassword;
		}
		public void setMySQLDBPassword(String mySQLDBPassword) {
			this.mySQLDBPassword = mySQLDBPassword;
		}
		public String getMySQLDBPort() {
			return mySQLDBPort;
		}
		public void setMySQLDBPort(String mySQLDBPort) {
			this.mySQLDBPort = mySQLDBPort;
		}
		public String getMailServerHost() {
			return mailServerHost;
		}
		/**
		 * Mail server host
		 */
		public void setMailServerHost(String mailServerHost) {
			this.mailServerHost = mailServerHost;
		}
		public String getMailServerUser() {
			return mailServerUser;
		}
		// Mail server user to login
		public void setMailServerUser(String mailServerUser) {
			this.mailServerUser = mailServerUser;
		}
		public String getMailServerPassword() {
			return mailServerPassword;
		}
		// Mail server user password
		public void setMailServerPassword(String mailServerPassword) {
			this.mailServerPassword = mailServerPassword;
		}
}
