package com.ability.ease.auto.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.utils.FileUtils;

import com.aqua.filetransfer.ftp.FTPFileTransfer;
import com.aqua.sysobj.conn.CliCommand;
import com.aqua.sysobj.conn.CliConnection;
import com.aqua.sysobj.conn.LinuxDefaultCliConnection;
import com.aqua.sysobj.conn.WindowsDefaultCliConnection;
import com.ability.ease.auto.common.exceptions.ErrorLevelException;

/**
 * This class includes CLI (Command Line Interface) related methods
 * 
 */
public class CliUtil {

	// This array contains possible error strings returned in response to a CLI command
	private static String[] cliCommonErrors = { "error", "failed", "fail", "refused", "cannot find", "could not", "exception", "nullpointer", "terminating abnormally", "IllegalStateException", "No such directory", "No such file", "**** LEFT ****",
			"not exist", "invalid", "failure" };

	private static String[] cliCommonSuccessStrings = { "successfully" };
	private static final String REPORT_ATTACHEMENT_TEMP_DIR = "c:\\temp\\jsystem_report\\attachments";
	private static final long MAX_CLI_COMMAND_TIMEOUT = 60 * 1000 * 15; // 15
	
	private static CliConnection cliConnection;

	// minutes

	/**
	 * executes a CLI command with timeout of 5 minutes
	 * 
	 * @param cliConnection
	 * @param logOutput
	 *            the output for the LOG when executing a command
	 * @param command
	 *            the command to execute
	 * @param checkErrorLevel
	 * @throws Exception
	 */
	public static void executeCLICommandOnRemote(CliConnection cliConnection, String logOutput, String command, boolean checkErrorLevel) throws Exception {
		executeCLICommandOnRemote(cliConnection, logOutput, command, checkErrorLevel, MAX_CLI_COMMAND_TIMEOUT);
	}

	/**
	 * this method executes & validates CLI command on remote machine
	 * 
	 * @param cliConnection
	 *            - CLI remote connection on which the command should be executed
	 * @param logOutput
	 *            - the message that identifies the command best that will be added to the report.
	 * @param command
	 *            - the command the execute
	 * @param checkErrorLevel
	 *            - use "true" only if the command affect the %ERRORLEVLEL% variable, else use
	 *            "false"
	 * @param timeout
	 *            - the number of ms to wait for the command to finish
	 * @throws Exception
	 */
	private static String executeCLICommandOnRemote(CliConnection cliConnection, String logOutput, String command, boolean checkErrorLevel, long timeout) throws Exception {

		cliConnection.setKeyTypingDelay(100);
		validateCliConnection(cliConnection);
		CliCommand cliCommand = null;
		try {
			cliCommand = new CliCommand(command);
			cliCommand.setTimeout(timeout);
			cliConnection.cleanCliBuffer();
			cliConnection.handleCliCommand(logOutput, cliCommand);
		} catch (Exception ex) {
			cliConnection.reconnect();
			try {
				cliConnection.handleCliCommand(logOutput, cliCommand);
			} catch (Exception ex2) {
				throw new Exception("command failed due to this error: " + ex2.getMessage());
			}
		}

		Thread.sleep(1000);
		// validate the CLI command
		try {
			validateCliCommand(cliConnection, cliCommand, checkErrorLevel);
		} finally {
			// keep current CLI output command in a temp file
			// delete and create new REPORT_ATTACHEMENT_TEMP_DIR directory
			try {
				jsystem.utils.FileUtils.deltree(REPORT_ATTACHEMENT_TEMP_DIR);
				jsystem.utils.FileUtils.mkdirs(REPORT_ATTACHEMENT_TEMP_DIR);
				Thread.sleep(1000);
				// write the file on disk: save the CLI command output to
				// c:\temp\jsystem_report\attachments
				String cliOutput = cliCommand.getResult();
				FileUtils.write(REPORT_ATTACHEMENT_TEMP_DIR + "\\LastCliOutput.log", cliOutput);
				ListenerstManager.getInstance().report(cliCommand.getResult());
			} catch (Exception ex) {
				// ignore this exception
			}
		}
		return cliCommand.getResult(); 
	}

	/**
	 * This method checks if the CLI command succeeded or failed.
	 * 
	 * @param cliRemoteConnection
	 *            - the CLI command remote connection on which the command should be executed
	 * @param cliCommand
	 *            - the CLI command to execute
	 * @param chkErrorLevel
	 *            - use "true" only if the command affect the %ERRORLEVLEL% variable, else use
	 *            "false"
	 * @throws Exception
	 */
	public static void validateCliCommand(CliConnection cliRemoteConnection, CliCommand cliCommand, boolean chkErrorLevel) throws Exception {
		// check the ERRORLEVEL environment variable
		if (chkErrorLevel) {
			if (!chkErrorLevel(cliRemoteConnection)) {
				throw new ErrorLevelException("%ErrorLevel% check failed for command: " + cliCommand.getCommands()[0]);
			}
		}

		Thread.sleep(1000);

		// check the command output for errors string, but only if the success strings were not
		// found
		//IDO: put below check in comment on (March/26-2014) - make troubles.
		/*if (!findStringsInCliCommand(cliCommand, cliCommonSuccessStrings)) {
			if (findStringsInCliCommand(cliCommand, cliCommonErrors)) {
				throw new Exception("Cli command failed: " + cliCommand.getResult());
			}
		}*/
	}

	/**
	 * This method returns true in case at least one string was found within the output of the CLI
	 * command
	 * 
	 * @param cliCommand
	 *            - the CLI command
	 * @return "true" in found at least one string , and "false" otherwise
	 * @throws Exception
	 */
	private static boolean findStringsInCliCommand(CliCommand cliCommand, String[] stringsToFind) throws Exception {
		// searching for errors strings in the returned command text
		String commandResult = cliCommand.getResult();
		for (int i = 0; i < stringsToFind.length; i++) {
			if (commandResult.toUpperCase().contains(stringsToFind[i].toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks if the CLI command succeeded or failed by checking only the exit code
	 * returned by the %ERRORLEVEL% variable
	 * 
	 * @param cliRemoteConnection
	 *            - the CLI command remote connection
	 * @return true in case of success (exit code=0) or false (exit code !=0)otherwise
	 * @throws Exception
	 */
	private static boolean chkErrorLevel(CliConnection cliRemoteConnection) throws Exception {
		String commandResult = null;

		// check command error level if it contains 0 then process execution was a success
		// get the result number from the returned string of 'echo %ERRORLEVEL%'
		cliRemoteConnection.cleanCliBuffer();
		CliCommand cliCmd = new CliCommand("echo %ERRORLEVEL%");
		cliRemoteConnection.handleCliCommand("checking error-level", cliCmd);
		commandResult = cliCmd.getResult();
		// int exitCode = 0;
		// try {
		if (commandResult.contains("\r\n0\r\n") || (commandResult.contains("\r0\r\n"))){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * run a dummy command to make sure socket is not closed. should help handle
	 * <code>SocketException</code> failures. if a <code>SocketException</code> is caught it will
	 * try to reconnect the CLI.
	 * 
	 * @param ftpSession
	 * @throws Exception
	 *             in case the cli command throws Exception which is not SocketException
	 */
	public static void validateCliConnection(FTPFileTransfer ftpSession) throws Exception {
		validateCliConnection(ftpSession.cliConnection);
	}

	/**
	 * run a dummy command to make sure socket is not closed. should help handle
	 * <code>SocketException</code> failures. if a <code>SocketException</code> is caught it will
	 * try to reconnect the CLI.
	 * 
	 * @param cliConnection
	 * @throws Exception
	 *             in case the cli command throws Exception which is not SocketException
	 */
	public static void validateCliConnection(CliConnection cliConnection) throws Exception {
		try {

			
			//FtpUtil.exitFtpPrompt(cliConnection); // in case we are in FTP mode
			cliConnection.cleanCliBuffer();
			CliCommand cmd = new CliCommand("echo %ERRORLEVEL%");
			cmd.setIgnoreErrors(true);
			cliConnection.handleCliCommand("testing CLI connection", cmd);
			Thread.sleep(1000);
			if (cmd.getResult() == null || cmd.getResult().length() == 0) {
				cliConnection.reconnect();
			} else {
				if (cmd.getResult().indexOf("Server has closed the connection") > -1 
						|| cmd.getResult().indexOf("unknown user name") > -1
						|| cmd.getResult().indexOf("The handle is invalid") > -1) {
					cliConnection.init();
					cliConnection.connect();
				}
				cliConnection.cleanCliBuffer();
			}
		} catch (Exception ex) {
			cliConnection.init();
			cliConnection.connect();
		}
	}
	
	/**
	 * Run CLI command on Windows remote machine
	 * @return
	 * @throws Exception
	 */
	 public static String runCliCommandOnWindows(String host, String hostUser, String hostPassword,
									  String command,  boolean checkErrorLevel, long timeout) throws Exception {
			boolean isConnected;
			String result;
			try {

				
				//establish CLI connection
				if (cliConnection!=null) {
					if(!cliConnection.getHost().equalsIgnoreCase(host)){
						cliConnection = new WindowsDefaultCliConnection(host, hostUser, hostPassword);			
						cliConnection.init();
					} else{
						isConnected = cliConnection.isConnected();
						if(!isConnected){
							cliConnection.connect();
						}
					}
				}	
				else{
					cliConnection = new WindowsDefaultCliConnection(host, hostUser, hostPassword);			
					cliConnection.init();
					isConnected = cliConnection.isConnected();
				}
				cliConnection.setKeyTypingDelay(500);				 
				//CliUtil.validateCliConnection(cliConnection);
				//SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
				//df.toPattern();
				//run the command
				result = executeCLICommandOnRemote(cliConnection, "Windows CLI Command", command, checkErrorLevel, timeout);
			} catch (Exception ex) {
				throw ex;
			}
			return result;
		}
	 
	 
	 /**
		 * Run CLI command on Linux remote machine
		 * @return
		 * @throws Exception
		 */
		 public static String runCliCommandOnLinux(String host, String hostUser, String hostPassword, String commandContent,
										  boolean checkErrorLevel, long timeout) throws Exception {
				boolean isConnected;
				String result;
				try {

					
					//establish CLI connection
					if (cliConnection!=null) {
						if(!cliConnection.getHost().equalsIgnoreCase(host)){
							cliConnection = new LinuxDefaultCliConnection(host, hostUser, hostPassword);			
							cliConnection.init();
						} else{
							isConnected = cliConnection.isConnected();
							if(!isConnected){
								cliConnection.connect();
							}
						}
					}	
					else{
						cliConnection = new LinuxDefaultCliConnection(host, hostUser, hostPassword);			
						cliConnection.init();
						isConnected = cliConnection.isConnected();
					}
									 
					CliUtil.validateCliConnection(cliConnection);
					
					//run the command
					result = executeCLICommandOnRemote(cliConnection, "", commandContent, checkErrorLevel, timeout);
				} catch (Exception ex) {
					throw ex;
				}
				return result;
			}
		
		
		/**
		 * Runs CLI command on WIndows OS
		 * @param host
		 * @param hostUser
		 * @param hostPassword
		 * @param commandContent
		 * @param commandName
		 * @param checkErrorLevel
		 * @return
		 * @throws Exception
		 */
		public static String runCliCommandOnWindows(String host, String hostUser, String hostPassword,
				  String commandContent, String commandName, 
				  boolean checkErrorLevel) throws Exception {
			
			return runCliCommandOnWindows(host,  hostUser, hostPassword,
					  commandContent,  checkErrorLevel, MAX_CLI_COMMAND_TIMEOUT);
		}
		
		
		/**
		 * Executes the specified command and arguments in a separate process. 
		 * @param command 
		 * @throws IOException 
		 */
		public static void executeCmdInSeparateProcess(String[] command) throws IOException {
				ListenerstManager.getInstance().report("Executing the command '" + Arrays.toString(command).replace(",", "") + "':", Reporter.ReportAttribute.BOLD);			
				Process p = Runtime.getRuntime().exec(command);
				monitorProcessExecution(p);
		}
		
		/**
		 * Uses Java RunTime.exec which executes the specified string command in a separate process.  
		 * @param command
		 * @throws IOException 
		 */
		public static void executeCmdInSeparateProcess(String command) throws IOException {
				Process p = Runtime.getRuntime().exec(command);
				monitorProcessExecution(p);
		}
		
		/**
		 * monitors the execution of a process
		 * @param p
		 */
		private static void monitorProcessExecution(Process p) {
			
			String s = null;
			BufferedReader stdInput = null;
			BufferedReader stdError =null;
			try { 
			   stdInput = new BufferedReader(new 
		       InputStreamReader(p.getInputStream()));

			    stdError = new BufferedReader(new 
			    InputStreamReader(p.getErrorStream()));
		
			    // read the output from the command
			    while ((s = stdInput.readLine()) != null) {
			         ListenerstManager.getInstance().report(s, Reporter.PASS);
			    }
			            
		        // read any errors from the attempted command
		        while ((s = stdError.readLine()) != null) {
		          	ListenerstManager.getInstance().report(s, Reporter.PASS);
		        }
		   }
		   catch (IOException e) {
		       	ListenerstManager.getInstance().report("exception happened - here's what I know: "+ e.getMessage(), Reporter.WARNING);
		   } finally {
			   try {
					stdInput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 try {
					stdError.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   
		   }
		}

}
