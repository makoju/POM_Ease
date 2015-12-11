package com.ability.ease.auto.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import jsystem.utils.FileUtils;

import com.aqua.filetransfer.ftp.FTPFileTransfer;
import com.aqua.sysobj.conn.CliCommand;
import com.aqua.sysobj.conn.CliConnection;
import com.aqua.sysobj.conn.CliFactory;




public class FileUtil {

	private final static String[] EMPTY_STRING_ARRAY = new String[] { "" };
	private static Object syncObj= new Object();
	
	/*Flag that defines a way of sending the command string to CLI ,
	if false - a whole command will be sent as one string ,
	if true - the command will be sent letter by letter.
	DelayTyping sometimes helps to send a command over "problematic" terminals*/	
	private static boolean delayTyping = false;
	
	// a flag that indicates if file was copied successfully.
	private static boolean wasFileCopied = false;


	private static HashSet<Class<?>> PRIMITIVE_TYPES;
	
	static {
		PRIMITIVE_TYPES = new HashSet<Class<?>>();    
		PRIMITIVE_TYPES.add(boolean.class);      
		PRIMITIVE_TYPES.add(char.class);  
		PRIMITIVE_TYPES.add(byte.class);  
		PRIMITIVE_TYPES.add(short.class);    
		PRIMITIVE_TYPES.add(int.class);   
		PRIMITIVE_TYPES.add(long.class);      
		PRIMITIVE_TYPES.add(float.class);     
		PRIMITIVE_TYPES.add(double.class); 
		PRIMITIVE_TYPES.add(boolean[].class);      
		PRIMITIVE_TYPES.add(char[].class);  
		PRIMITIVE_TYPES.add(byte[].class);  
		PRIMITIVE_TYPES.add(short[].class);    
		PRIMITIVE_TYPES.add(int[].class);   
		PRIMITIVE_TYPES.add(long[].class);      
		PRIMITIVE_TYPES.add(float[].class);     
		PRIMITIVE_TYPES.add(double[].class); 
	} 
	
	private static HashSet<Class<?>> WRAPPER_TYPES;
	
	static {
		WRAPPER_TYPES = new HashSet<Class<?>>();    
		WRAPPER_TYPES.add(Boolean.class);      
		WRAPPER_TYPES.add(Character.class);  
		WRAPPER_TYPES.add(Byte.class);  
		WRAPPER_TYPES.add(Short.class);    
		WRAPPER_TYPES.add(Integer.class);   
		WRAPPER_TYPES.add(Long.class);      
		WRAPPER_TYPES.add(Float.class);     
		WRAPPER_TYPES.add(Double.class); 
		WRAPPER_TYPES.add(Boolean[].class);      
		WRAPPER_TYPES.add(Character[].class);  
		WRAPPER_TYPES.add(Byte[].class);  
		WRAPPER_TYPES.add(Short[].class);    
		WRAPPER_TYPES.add(Integer[].class);   
		WRAPPER_TYPES.add(Long[].class);      
		WRAPPER_TYPES.add(Float[].class);     
		WRAPPER_TYPES.add(Double[].class); 
		WRAPPER_TYPES.add(String.class);
		WRAPPER_TYPES.add(String[].class);
	} 

	
	
	
	/**
	 * This method traverses a directory recursively on the local machine and copy its entire
	 * structure and content to a remote machine. The method assumes that the local machine is
	 * Windows. The remote machine can be either Windows or Linux.
	 * 
	 * @param file
	 *            - a File object that represents the source directory (the directory to copy to a
	 *            remote machine)
	 * @param dstDir
	 *            - the destination directory on the remote machine to copy the directory under it
	 * @param remoteFTPConnection
	 *            - FTP connection to the remote machine
	 * @throws Exception
	 */
	public static void copyDirectoryFromLocalToRemote(File file, String dstDir, FTPFileTransfer remoteFTPConnection) throws Exception {
		//use simple copy directory if remote=local
		if (remoteFTPConnection.cliConnection.getHost().equalsIgnoreCase(HostUtil.getLocalHostName())){
			if (file.isDirectory()) {
				if (!(new File(dstDir+file.getName()).exists())) {
					FileUtils.mkdirs(dstDir+file.getName());
				}
				FileUtils.copyDirectory(file, new File(dstDir+file.getName()));
			} else {
				throw new Exception ("the first parameter for 'copyDirectoryFromLocalToRemote' method must be a valid directory");
			}
		} else {
			copyDirectoryLocalToRemote(file, dstDir, remoteFTPConnection);
		}
	}

	private static void copyDirectoryLocalToRemote(File file, String dstDir, FTPFileTransfer remoteFTPConnection) throws Exception {

		if (!file.exists()) {
			throw new Exception("failed to copy " + file.getAbsolutePath() + " from local to " + remoteFTPConnection.cliConnection.getHost() + ". Directory or file doesn't exsist in local.");
		}

		// ignore hidden folders and files
		if (file.getName().startsWith(".svn") || file.isHidden()) {
			return;
		}

		if (file.isDirectory()) {
			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
				String currentSrcDir = file.getPath().split("\\\\")[file.getPath().split("\\\\").length - 1];
				String currentDestDir = dstDir.split("\\\\")[dstDir.split("\\\\").length - 1];
				if (!currentDestDir.equals(currentSrcDir)) { // new directory found
					dstDir = dstDir + "\\" + currentSrcDir;
				}
			}

			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				String currentSrcDir = file.getPath().split("\\\\")[file.getPath().split("\\\\").length - 1];
				String currentDestDir = dstDir.split("/")[dstDir.split("/").length - 1];
				dstDir = convertToLinux(dstDir);
				if (!currentDestDir.equals(currentSrcDir)) { // new directory found
					currentSrcDir = convertToLinux(currentSrcDir);
					dstDir = dstDir + "/" + currentSrcDir;
				}
			}
			Thread.sleep(2000);
			// for Linux use "mkdir -p"
			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				CliUtil.executeCLICommandOnRemote(remoteFTPConnection.cliConnection, "create new directory on remote machine " + remoteFTPConnection.cliConnection.getHost() + ": " + dstDir, "mkdir -p " + dstDir, false);
			}
			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
				String newDstDir = getNewFilePath(dstDir);
				CliUtil.executeCLICommandOnRemote(remoteFTPConnection.cliConnection, "create new directory on remote machine " + remoteFTPConnection.cliConnection.getHost() + ": " + newDstDir, "mkdir " + newDstDir, false);
			}

			// get the directory files (if there are any)
			String[] childrens = file.list();
			for (int i = 0; i < childrens.length; i++) {
				copyDirectoryLocalToRemote(new File(file, childrens[i]), dstDir, remoteFTPConnection);
			}
		} else {// copy file from local to remote
			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
				copyFileFromLocalToRemote(file.getCanonicalPath(), dstDir + "\\" + file.getName(), remoteFTPConnection);
			}
			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				copyFileFromLocalToRemote(file.getCanonicalPath(), dstDir + "/" + file.getName(), remoteFTPConnection);
			}
		}
	}

	/**
	 * This method traverses a directory recursively on a remote machine and copy its entire
	 * structure and content to the local machine. The method assumes that the local machine is
	 * Windows. The remote machine can be either Windows or Linux.
	 * 
	 * @param file
	 *            a File object that represents the source directory (the directory to copy from to
	 *            the remote machine)
	 * @param dstDir
	 *            the destination directory on the local machine to copy the directory under it
	 * @param remoteFTPConnection
	 *            FTP connection to the remote machine
	 * @throws Exception
	 */
	public static void copyDirectoryFromRemoteToLocal(File file, String dstDir, FTPFileTransfer remoteFTPConnection) throws Exception {
		copyDirectoryRemoteToLocal(file, dstDir, remoteFTPConnection);
		FtpUtil.exitFtpPrompt(remoteFTPConnection);
	}

	private static void copyDirectoryRemoteToLocal(File file, String dstDir, FTPFileTransfer remoteFTPConnection) throws Exception {
		if (file.isDirectory()) {
			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
				String currentSrcDir = file.getPath().split("\\\\")[file.getPath().split("\\\\").length - 1];
				String currentDestDir = dstDir.split("\\\\")[dstDir.split("\\\\").length - 1];
				if (!currentDestDir.equals(currentSrcDir)){ // new directory found
					dstDir = dstDir + "\\" + currentSrcDir;
				}
			}

			if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				String currentSrcDir = file.getPath().split("/")[file.getPath().split("/").length - 1];
				//String currentDestDir = dstDir.split("/")[dstDir.split("/").length - 1];
				String currentDestDir = dstDir.split("\\\\")[dstDir.split("\\\\").length - 1];
				if (!currentDestDir.equals(currentSrcDir)) { // new directory was  found
					//dstDir = dstDir + "/" + currentSrcDir;
					dstDir = dstDir + "\\" + currentSrcDir;
				}
			}

			// create new directory on the local machine
			FileUtils.mkdirs(dstDir);

			// get the directory files (if there are any)
			String[] childrens = file.list();
			for (int i = 0; i < childrens.length; i++) {
				copyDirectoryRemoteToLocal(new File(file, childrens[i]), dstDir, remoteFTPConnection);
			}
		} else { // copy file
			//use simple copy (not FTP) if remote=localhost
			if (remoteFTPConnection.cliConnection.getHost().equalsIgnoreCase(HostUtil.getLocalHostName())) {
				FileUtils.copyFile(file, new File(dstDir + "\\" + file.getName()));
			} else {//use FTP copy {
				CliUtil.validateCliConnection(remoteFTPConnection);
				if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
					copyFileFromRemoteToLocal(file, dstDir + "\\" + file.getName(),remoteFTPConnection);
				}
				if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
					//copyFileFromRemoteToLocal(file, dstDir + "/" + file.getName(),remoteFTPConnection);
					remoteFTPConnection.copyFileFromRemoteMachineToLocalMachine(file, new File(dstDir + "\\" + file.getName()));
					FtpUtil.exitFtpPrompt(remoteFTPConnection);
				}
			}
		}
	}

	/**
	 * This method copies one file from remote to local (JSystem) machine
	 * 
	 * @param file
	 *            - the file you want to copy from remote
	 * @param dstDir
	 *            - the directory on the JSystem machine in which the file will be copied to
	 * @param remoteFTPConnection
	 *            - the remote FTP connection
	 * @throws Exception
	 */
    public static void copyFileFromRemoteToLocal(File file, String dstDir, FTPFileTransfer remoteFTPConnection) throws Exception {
		// create the destination directory on JSystem machine if doesn't exist
		try {
			if (!(new File(dstDir)).isDirectory()) {
				jsystem.utils.FileUtils.mkdirs(dstDir);
			}
			
			String dstFileName = file.getName();
			//remove the asterisk(*) sign if exists from the remote file name
			int indexOfAsterisk = dstFileName.indexOf("*");
			
			if (indexOfAsterisk > 0) {
				dstFileName = dstFileName.substring(0,indexOfAsterisk)+dstFileName.substring(indexOfAsterisk+1);
			}
			//remove the " sign if exists, from the end of the remote file name
			int indexOfInvertedcommas = dstFileName.indexOf("\"");
			if (indexOfInvertedcommas > 0) {
				dstFileName = dstFileName.substring(0,indexOfInvertedcommas)+dstFileName.substring(indexOfInvertedcommas+1);
			}
			
			CliUtil.validateCliConnection(remoteFTPConnection);
			//use simple copy (not FTP) if remote=localhost
			if (remoteFTPConnection.cliConnection.getHost().equalsIgnoreCase(HostUtil.getLocalHostName())){
				FileUtils.copyFile(file, new File(dstDir, file.getName()));
			} else { //use FTP copy
				if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
					try {
						putFileFtp(file, dstDir, remoteFTPConnection, false);
					} catch (Exception ex) {
						remoteFTPConnection.init(); // this will also initialize the client
						remoteFTPConnection.cliConnection.init(); // [Ido] it seems that ftpFileConnection.init() doesn't initialize cliConnection when ftp is running.
						putFileFtp(file, dstDir, remoteFTPConnection, true);
					}
				} else if (remoteFTPConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
					remoteFTPConnection.copyFileFromRemoteMachineToLocalMachine(file, new File(dstDir+"\\"+dstFileName)); //dstDir + "/" + file.getName().replace("*","")));
				}
			}
		} finally {
			FtpUtil.exitFtpPrompt(remoteFTPConnection);
		}
	}

	/**
	 * This method deletes a directory on a remote machine. The remote machine can be either Windows
	 * or Linux.
	 * 
	 * @param directory
	 * @param ftpFileConnection
	 * @throws Exception
	 */
	@Deprecated
	public static void deleteDirectoryFromRemote(String directory, FTPFileTransfer ftpFileConnection) throws Exception {
		// delete a directory on remote machine
		String remoteMchine = ftpFileConnection.cliConnection.getHost();
		CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "feed new line: " + remoteMchine, ftpFileConnection.cliConnection.getEnterStr(), false);
		if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
			CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "delete a directory on remote machine: " + remoteMchine, "rmdir " + directory + " /S /Q", false);
		}
		if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
			CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "delete a directory on remote machine: " + remoteMchine, "rm -rf " + directory, false);
		}
	}
	
	/**
	 * This method deletes a directory from local machine. 
	 * Assuming  the local is Windows (where JSystem is running)
	 * 
	 * @param directory
	 * @throws Exception
	 */
	public static void deleteDirectoryFromLocal(String directory) throws Exception {
		Runtime.getRuntime().exec("rmdir " + directory + " /S /Q");
	}

	/**
	 * This method deletes a file on a remote machine. The remote machine can be either Windows or
	 * Linux.
	 * 
	 * @param file
	 * @param ftpFileConnection
	 * @throws Exception
	 */
	public static void deleteFileFromRemote(String file, FTPFileTransfer ftpFileConnection) {
		// delete a file on remote machine
		
		String remoteMchine = ftpFileConnection.cliConnection.getHost();
		
		try {
			CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "feed new line: " + remoteMchine, ftpFileConnection.cliConnection.getEnterStr(), false);
			if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
				CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "delete a file on remote machine: " + remoteMchine, "del " + file + " /Q", false);
			}
			if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "delete a file on remote machine: " + remoteMchine, "rm -f " + file, false);
			}
		} catch (Exception ex) {
			//ListenerstManager.getInstance().report("failed to delete remote file: " +file, Reporter.ReportAttribute.BOLD);
			//ignoring this error, only printing a message.
		}
	}

	/**
	 * This method copies all files under a local directory (the source directory) to a directory on
	 * a remote machine (the destination directory) The method assumes the local directory is on
	 * Windows.The remote machine can be either Windows or Linux.
	 * 
	 * @param srcDir
	 *            the source directory
	 * @param dstDir
	 *            the destination directory on the remote machine
	 * @param ftpFileConnection
	 *            the FTP connection object on the remote machine
	 * @throws Exception
	 */
	public static void copyFilesFromLocalDirectoryToRemoteDirectory(File srcDir, String dstDir, FTPFileTransfer ftpFileConnection) throws Exception {
		if (srcDir.isDirectory()) {
			if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				dstDir = convertToLinux(dstDir);
			}
			String remoteMchine = ftpFileConnection.cliConnection.getHost();
			// cd to the destination directory on the remote machine
			CliUtil.executeCLICommandOnRemote(ftpFileConnection.cliConnection, "cd to destination dir on remote machine: " + remoteMchine, "cd " + dstDir, false);
			// get all the files from the source directory
			String[] files = srcDir.list();
			String seperator = "\\"; // file seperator for Windows
			if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
				seperator = "/";
			}
			String dstFileName;
			String srcFile;
			for (int i = 0; i < files.length; i++) {
				dstFileName = files[i];
				srcFile = srcDir.getPath() + "\\" + files[i];
				//use simple copy (not FTP) if remote=localhost
				if (ftpFileConnection.cliConnection.getHost().equalsIgnoreCase(HostUtil.getLocalHostName())){
					FileUtils.copyFile(srcFile, dstDir + seperator + dstFileName);
				} else { //use FTP copy
					//if (HostUtil.isWindows2008(ftpFileConnection)) {
					if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)) {
						try {
							getFileFtp(srcFile, dstDir + seperator + dstFileName, ftpFileConnection, false);
						} catch (Exception ex) {
							ftpFileConnection.init(); // this will also initialize the client
							ftpFileConnection.cliConnection.init(); // [Ido] it seems that ftpFileConnection.init() doesn't initialize cliConnection when ftp is running.
							getFileFtp(srcFile, dstDir + seperator + dstFileName, ftpFileConnection, true);
						}
					} else if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
						ftpFileConnection.copyFileFromLocalMachineToRemoteMachine(new File(srcFile), new File(dstDir + seperator + dstFileName));
					}
					FtpUtil.exitFtpPrompt(ftpFileConnection);
				}
			}
		} else {
			throw new Exception("source directory must be a valid directory, aborting.");
		}
	}

	/**
	 * @param srcFile
	 *            source file full path
	 * @param dstFile
	 *            destination file full path
	 * @param ftpFileConnection
	 * @throws Exception
	 */
	public static void copyFileFromLocalToRemote(String srcFile, String dstFile, FTPFileTransfer ftpFileConnection) throws Exception {
		if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)) {
			dstFile = convertToLinux(dstFile);
		}
		
		//use simple copy (no FTP) if copy file should be done only in localhost
		if (ftpFileConnection.cliConnection.getHost().equalsIgnoreCase(HostUtil.getLocalHostName())){
			FileUtils.copyFile(srcFile, dstFile);
		} else {
			CliUtil.validateCliConnection(ftpFileConnection);
			
			delayTyping = false;
			wasFileCopied = false;
			
			com.ability.ease.auto.jsystem.fileTransfer.FTPFileTransfer modifiedFtpFileConnection = null;
			int tries = 1;
			while (tries <= 2 && !wasFileCopied) {
				try {
					//if (HostUtil.isWindows2008(ftpFileConnection) == true) {
					if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_WINDOWS)){
						getFileFtp(srcFile, dstFile, ftpFileConnection, delayTyping);
					} else if (ftpFileConnection.getOperatingSystem().equalsIgnoreCase(CliFactory.OPERATING_SYSTEM_LINUX)){
						modifiedFtpFileConnection = new com.ability.ease.auto.jsystem.fileTransfer.FTPFileTransfer(ftpFileConnection.cliConnection) ;
						//ftpFileConnection.copyFileFromLocalMachineToRemoteMachine(new File(srcFile), new File(dstFile));
						modifiedFtpFileConnection.copyFileFromLocalMachineToRemoteLinuxMachine(ftpFileConnection.getFtpUserName(), ftpFileConnection.getFtpPassword(), ftpFileConnection.getFtpServerHome(), new FileInputStream(srcFile), dstFile);
						//Gilad : replace next line with new method version to solve ftp between windows7 and Linux
						//ftpFileConnection.copyFileFromLocalMachineToRemoteMachine(new File(srcFile), new File(dstFile));

					}
					wasFileCopied = true; //to exit the while loop
				} catch (Exception e) { // fix Socket Closed issue
					if (modifiedFtpFileConnection == null) {
						ftpFileConnection.init(); // this will also initialize the client
						ftpFileConnection.cliConnection.init(); // [Ido] it seems that ftpFileConnection.init() doesn't initialize cliConnection when ftp is running.
					} else {
						modifiedFtpFileConnection.init(); // this will also initialize the client
						modifiedFtpFileConnection.cliConnection.init(); // [Ido] it seems that ftpFileConnection.init() doesn't initialize cliConnection when ftp is running.
					}
					Thread.sleep(60*1000*2); //wait 2 minutes for file to be released (in case it is being used by previous FTP process)
					delayTyping = true;
					tries++;
				}
			}
			FtpUtil.exitFtpPrompt(ftpFileConnection);
			delayTyping = false;
			if (!wasFileCopied) {
				throw new Exception ("failed in FTP file transfer: copy "+ srcFile +" to "+ dstFile + "\r\n");
			}
		}
	}
 
	public static void getFileFtp(String srcFile, String dstFile, FTPFileTransfer ftpFileConnection, boolean delayTyping) throws Exception {
		getFileFtp(srcFile, dstFile, ftpFileConnection, 600000, delayTyping);

	}

	public static void getFileFtp(String srcFile, String dstFile, FTPFileTransfer ftpFileConnection, long timeout, boolean delayTyping) throws Exception {

		String aquaFtpHomeDirectory = ftpFileConnection.getFtpServerHome();
		
		//if dstFile contains space(s) surround it with ""
		if (dstFile.contains(" ")) {
			if (!dstFile.startsWith("\"")) {
				dstFile = "\""+dstFile;
			} 
			if (!dstFile.endsWith("\"")) {
				dstFile = dstFile+"\"";
			}
		}
		
		//if srcFile contains space(s) surround it with ""
	/*	if (srcFile.contains(" ")) {
			if (!srcFile.startsWith("\"")) {
				srcFile = "\""+srcFile;
			} 
			if (!srcFile.endsWith("\"")) {
				srcFile = srcFile+"\"";
			}
		}*/
		
		File src = new File(srcFile);
		if (src.getParent().equalsIgnoreCase(aquaFtpHomeDirectory) == false) {
			jsystem.utils.FileUtils.copyFile(src.getCanonicalPath(), aquaFtpHomeDirectory + "\\" + src.getName());
		}
		
		CliConnection cli = ftpFileConnection.cliConnection;
		String localHostName = HostUtil.getLocalHostName();
		
		CliCommand command = new CliCommand("ftp " + localHostName.trim() + "\rftp\rftp\r");
		cli.handleCliCommand(command.getCommands()[0], command);

		command = new CliCommand("binary");
		cli.handleCliCommand(command.getCommands()[0], command);

		command = new CliCommand("prompt on");
		cli.handleCliCommand(command.getCommands()[0], command);
		
		
		
		
		File dst = new File(dstFile);
		String strCommand = "lcd " + dst.getParent();

		command = new CliCommand(strCommand);
		cli.handleCliCommand(command.getCommands()[0], command);

		
		/*int indexOfAsterisk = dstFile.indexOf("*");		
		if (indexOfAsterisk > 0) {
			dstFile = dstFile.substring(0,indexOfAsterisk)+dstFile.substring(indexOfAsterisk+1);
		}*/
	/*	String dstFilePath = dst.getParent();
		if (dstFilePath.endsWith("\\")) {
			dstFilePath = dstFilePath + "" + dst.getName();
		} else {
			dstFilePath = dstFilePath + "\\" + dst.getName();
		}*/
		//remove "" from dstFile
		
		//remove the " sign if exists, from the end of the file name		
		//dstFile.replaceAll("\"", "");
		
       // CliCommand command2 = new CliCommand("get \"" + src.getName() + "\" \"" + dstFile /*dstFilePath +*/+ "\"\r");
		CliCommand command2 = new CliCommand("get \"" + src.getName() + "\" " + dstFile /*dstFilePath +*/+ "\r");
		command2.setTimeout(timeout);
		Thread.sleep(1000);
		command2.setDelayTyping(delayTyping);
		cli.handleCliCommand(command2.getCommands()[0], command2);

		if (command2.getResult().indexOf("226") < 0) {
			throw new Exception("failed in FTP copy of file " + src.getName()+"\r\nError ="+command2.getResult());
		}
	}

	public static void putFileFtp(File srcFile, String dstDir, FTPFileTransfer ftpFileConnection, boolean delayTyping) throws Exception {
		
		String dstFileName = srcFile.getName();
		//remove the asterisk(*) sign if exists
		int indexOfAsterisk = dstFileName.indexOf("*");		
		if (indexOfAsterisk > 0) {
			dstFileName = dstFileName.substring(0,indexOfAsterisk)+dstFileName.substring(indexOfAsterisk+1);
		}
		//remove the " sign if exists, from the end of the file name
		int indexOfInvertedcommas = dstFileName.indexOf("\"");
		if (indexOfInvertedcommas > 0) {
			dstFileName = dstFileName.substring(0,indexOfInvertedcommas)+dstFileName.substring(indexOfInvertedcommas+1);
		}

		CliConnection cli = ftpFileConnection.cliConnection;
		String localHostName = HostUtil.getLocalHostName();
		
		CliCommand command = new CliCommand("ftp " + localHostName.trim() + "\rftp\rftp\r");
		command.setDelayTyping(delayTyping);
		cli.handleCliCommand(command.getCommands()[0], command);

		command = new CliCommand("binary");
		cli.handleCliCommand(command.getCommands()[0], command);

		command = new CliCommand("prompt on");
		cli.handleCliCommand(command.getCommands()[0], command);

		CliCommand command2;
		
		//create unique dstFileName to avoid "access denied" error
		//when a file with same name already exists in FTP home directory (c:\jsystem\runner\aqauaftp21)
		String uniqueDstFileName=System.currentTimeMillis()+dstFileName;
		
		//copy from remote to FTP home directory (c:\jsystem\runner\aquaftp21)
		if (!srcFile.getPath().startsWith("\"")) { //and assuming that also doesn't end with "
			command2 = new CliCommand("put "+ "\""+srcFile.getPath() + "\"  \""+uniqueDstFileName+"\"\r" );//+ srcFile.getCanonicalPath() + "\" \"" + fileName + "\"\r");
		} else {
			command2 = new CliCommand("put "+ srcFile.getPath() +  " \"" +uniqueDstFileName+"\"\r" );//+ srcFile.getCanonicalPath() + "\" \"" + fileName + "\"\r");
		}
		Thread.sleep(1000);
		command2.setDelayTyping(delayTyping);
		command2.setTimeout(30000); //600000); // otherwise if file is too big it will fail for timeout
		cli.handleCliCommand(command2.getCommands()[0], command2);

		if (command2.getResult().indexOf("226") < 0) {
			throw new Exception("failed in FTP copy of file " + srcFile.getPath() + "\r\ncommand result = "+command2.getResult());
		}

		//copy the file from C:\jsystem\runner\aquaftp21 to destination
		String aquaFtpHomeDirectory = ftpFileConnection.getFtpServerHome();
		File source = new File(aquaFtpHomeDirectory + "\\" + uniqueDstFileName);
		File destination = new File(dstDir + "\\" + dstFileName);
		jsystem.utils.FileUtils.copyFile(source, destination);

		//delete file from C:\jsystem\runner\aquaftp21 
		jsystem.utils.FileUtils.deleteFile(source.getCanonicalPath());

	}

	/**
	 * converts the sent path to a Linux style path and returns the new path
	 * 
	 * @param path
	 *            the converted path
	 * @return
	 */
	private static String convertToLinux(String path) {
		if (path.indexOf("\\") > -1) {
			path = path.replaceAll("\\\\", "/");
		}
		return path;
	}

	/**
	 * This method returns the last modified file from a list of files
	 * 
	 * @param files
	 * @return
	 */
	private static File findtLastModifiedFile(File[] files) {
		if (files.length != 0) {
			Arrays.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				}
			});
			return files[0];
		}
		return null;
	}

	/**
	 * This method returns the x most last modified files, for any x>0.
	 * 
	 * @param files
	 *            - array with the files
	 * @param x
	 *            - the required number of most last modified files to find. Should be > 0
	 * @return
	 */
	public static File[] getXmostLastModifedFiles(File[] files, int x) {
		if (files.length == 0 || x <= 0) {
			return null;
		}
		if (files.length < x) {
			x = files.length;
		}
		File[] files2 = files.clone();
		int[] markFiles = new int[files.length];
		File currrentLastModifiedFile;
		for (int i = 0; i < x; i++) {
			// find the last modified file from
			currrentLastModifiedFile = findtLastModifiedFile(files2);
			if (currrentLastModifiedFile != null) {
				// find the file position in files arrays
				int j = 0;
				for (j = 0; j < files.length; j++) {
					if (files[j] == currrentLastModifiedFile) {
						break;
					}
				}
				// mark the file in markFiles array
				markFiles[j] = 1;

				// now, that we found another file we can remove it from the
				// files2 array
				File[] tmp = new File[files2.length - 1];
				j = 0;
				for (int k = 0; k < files2.length; k++) {
					if (files2[k] != currrentLastModifiedFile) {
						tmp[j++] = files2[k];
					}
				}
				files2 = tmp;
			} else {
				return null;
			}
		}

		// building the array that will keep the X most last modified files
		File[] result = new File[x];
		int i = 0;
		for (int k = 0; k < markFiles.length; k++) {
			if (markFiles[k] == 1) {
				result[i++] = files[k];
			}
		}

		return result;
	}

	/**
	 * @param editFile
	 * @param oldText
	 * @param newText
	 * @throws Exception
	 */
	/*public static void replaceFileContent(File editFile, String oldText, String newText) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(editFile));
		String line = "";
		String oldContent = "";
		while ((line = reader.readLine()) != null) {
			oldContent += line + "\r\n";
		}

		reader.close();

		String newContent = oldContent.replaceAll(oldText, newText);
		FileWriter writer = new FileWriter(editFile);
		writer.write(newContent);

		writer.close();
	}
*/
	/**
	 * replaces all occurrences of oldText with the newText. keeps original encoding. Note: If the
	 * newText length is longer than oldText length the method throws run time exception.
	 * 
	 * @param editFile
	 * @param oldText
	 * @param newText
	 * @throws RuntimeException
	 * @throws Exception
	 */
	public static void replaceFileContent(File editFile, String oldText, String newText) throws Exception {
		String remoteFileContent = jsystem.utils.FileUtils.read(editFile);
    	remoteFileContent = remoteFileContent.toString().replaceAll(oldText, newText);
    	jsystem.utils.FileUtils.write(editFile,remoteFileContent, false );
		
		
		/*String line;
		FileInputStream fis = new FileInputStream(editFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		RandomAccessFile raf = new RandomAccessFile(editFile, "rw");
		int lineCounter = 0;
		while ((line = reader.readLine()) != null) {
			int before = line.length();
			if (line.contains("-Xms(.*)m -Xmx(.*)m -XX:MaxPermSize=(.*)m")){
				line = line.replaceAll("-Xms(.*)m -Xmx(.*)m -XX:MaxPermSize=(.*)m", newText);
				int after = line.length();
				if (after > before) {
					throw new RuntimeException("Attempt to replace text with a longer text is not supported");
				}
				raf.write((line + "\n").getBytes());
				lineCounter++;
			}
		}
		reader.close();
		raf.setLength(raf.getFilePointer());
		raf.close();*/
	}

	/**
	 * replaces all occurrences of oldText with the newText. text within comments WILL NOT be replaced
	 * 
	 * @param editFile
	 * @param oldText
	 * @param newText
	 * @throws Exception
	 */
	public static void replaceAllTextInFileIgnoreComments(File editFile, String oldText, String newText) throws Exception {
		String [] originalFile = getFileLinesAsStringArray(editFile, false);
		String [] updatedFile =new String [originalFile.length];
		boolean isCommentedOut = false;
		for ( int i =0 ; i < originalFile.length ; i ++){
			String line = originalFile[i];
			if (line.indexOf("<!--") >= 0 && ( line.indexOf("<!--") >= line.indexOf(oldText)) ){
				isCommentedOut = true ;
			}
			if (line.indexOf("-->") >= 0 && ( line.indexOf("-->") >= line.indexOf(oldText))){
				isCommentedOut = false ;
			}
			if (!isCommentedOut) {
				line = line.replaceAll(oldText, newText);
			}
			updatedFile[i] = line;
		}
		 FileOutputStream fos = new FileOutputStream(editFile); 
		 for ( int i =0 ; i < updatedFile.length ; i ++){
			 fos.write( (updatedFile[i]+ "\n").getBytes());
		 }		
		 fos.close(); 

	}

	/**
	 * return all directories under folderPath excluding the .svn directories
	 * 
	 * @param folderPath
	 * @return
	 */
	public static String[] listDirectories(String folderPath) {
		String[] dirs = jsystem.utils.FileUtils.listDirs(folderPath);
		if (dirs == null || dirs.length == 0) {
			return EMPTY_STRING_ARRAY;
		}
		ArrayList<String> directories = new ArrayList<String>();
		for (int i = 0; i < dirs.length; i++) {
			if (!dirs[i].startsWith(".")) {
				directories.add(dirs[i]);
			}
		}
		if (directories.size() == 0) {
			return EMPTY_STRING_ARRAY;
		}
		String[] dirsArray = directories.toArray(new String[directories.size()]);
		Arrays.sort(dirsArray);
		return dirsArray;
	}

	/**
	 * Returns the first line containing the string <code>textToFind</code>
	 * 
	 * @param fileName
	 * @param textToFind
	 * @return
	 * @throws IOException
	 */
	public static String getFirstLineWith(File fileName, String textToFind) throws IOException {
		String line = null;
		FileInputStream fis = new FileInputStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"UTF-16"));
		while ((line = reader.readLine()) != null) {
			if (line.contains(textToFind)) {
				break;
			}
		}
		reader.close();
		return line;
	}

	/**
	 * this method return file lines as an array of string
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String[] getFileLinesAsStringArray(File file, boolean ignoreEmptyLine) throws IOException {
		LineNumberReader lr = new LineNumberReader(new UnicodeFileReader(file.getAbsolutePath()));
		String line = null;
		ArrayList<String> al = new ArrayList<String>();

		// loop over the file lines
		while (true) {
			line = lr.readLine();
			if (line == null) {
				break;
			}

			if ((line.trim().equals("")) && (ignoreEmptyLine)) {
				continue;
			}

			al.add(line);
		}
		String[] lines = new String[al.size()];
		return al.toArray(lines);
	}

	public static String getNewFilePath(String filePath) {
		if (filePath.indexOf(" ") > -1 && !filePath.startsWith("\"")) {
			return "\"" + filePath + "\"";
		}
		return filePath;
	}
	
	
	
	public static String[] getFileLinesAsStringArrayIgnoreEmptyLines(File file, boolean ignoreEmptyLine) throws IOException {
		
		LineNumberReader lr = new LineNumberReader(new UnicodeFileReader(file.getAbsolutePath()));
		String line = null;
		ArrayList<String> al = new ArrayList<String>();

		// loop over the file lines
		while (true) {
			line = lr.readLine();
			if (line == null) {
				break;
			}

			if ((line.trim().equals("")) && (ignoreEmptyLine)) {
				continue;
			}
			
			if(!line.equals("\n")) {
				al.add(line);
			}
			
		}
		String[] lines = new String[al.size()];
		return al.toArray(lines);
	}

	
	/**
	 * this method loads file content to String
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String loadFileToString(File file) throws Exception {
		int len;
		char[] chr = new char[4096];
		final StringBuffer buffer = new StringBuffer();
		final FileReader reader = new FileReader(file);
		try {
			while ((len = reader.read(chr)) > 0) {
				buffer.append(chr, 0, len);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}
	/**
	 * this method loads property value from properties file
	 * @param file
	 * @param propertyName
	 * @return
	 * @throws IOException
	 */
	public static String loadPropertyFromFile (File file ,String propertyName) throws Exception {
		Properties prop = FileUtils.loadPropertiesFromFile(file.getAbsolutePath());
		return prop.getProperty(propertyName, "NA");
	}
	/**
	 * this method delete all files from folder according to pattern match
	 * 
	 * @param folderPath - folder where to delete all files
	 * @param pattern - String to match on file name
	 */
	public static void deleteFilesByName(String folderPath, String pattern) {
		File dir = new File(folderPath);
		if (!dir.exists()) {
			System.out.println(folderPath + " does not exist");
			return;
		}

		String[] info = dir.list();
		for (int i = 0; i < info.length; i++) {
			File n = new File(folderPath + File.separator + info[i]);
			if (!n.isFile()) { // skip ., .., other directories, etc.
				continue;
			}
			if (info[i].indexOf(pattern) == -1) { // name doesn't match
				continue;
			}
			System.out.println("removing " + n.getPath());
			if (!n.delete()) {
				System.err.println("Couldn't remove " + n.getPath());
			}
		}
	}
	
	public static void deleteDirectoryContent(String directory, boolean continueOnError) throws Exception {
		File dir = new File(directory);
		
		if (!dir.exists()) {
			throw new Exception(directory + " does not exist");
		}
		
		if (!dir.isDirectory()) {
			throw new Exception(directory+" is not a directory");
		}

		String[] dirContent = dir.list();
		for (int i = 0; i < dirContent.length; i++) {
			File f = new File(directory + File.separator + dirContent[i]);
			
			if (f.isFile()) {
				if (!f.delete()) {
					if (continueOnError) {
						continue;
					} else {
						throw new Exception ("failed to delete file: "+f.getCanonicalPath());
					}
				}
			}
			if (f.isDirectory()) {
				if (f.list().length < 3) {
					if (!f.delete()) {
						if (continueOnError) {
							continue;
						} else {
							throw new Exception ("failed to delete file: "+f.getCanonicalPath());
						}
					}
				}
				else {
					deleteDirectoryContent(f.getCanonicalPath(),continueOnError);
				}
				}
			}
		}
	
	public static String convertToNetworkShare (String remoteHostName, String remoteHostOS,  String file) throws Exception {
		if (remoteHostOS.toLowerCase().contains("windows")){
		return "\\\\" + remoteHostName +"\\" +  file.replace("/","\\").replace(":", "$");
		}
		else {
			return "\\\\" + remoteHostName +"\\" +  file.replaceFirst("/", "").replace("/","\\");
		}
	 }
	
	/**
	 * this method writes Strings to file
	 * 
	 * @param fileName - filename to write to
	 * @param dataLine - String to add to line
	 * @param isAppendMode - Should the new file be appended to existed file, or file should be re-written
	 * @param isNewLine - should the new string will appear as new line
	 */
	public static boolean writeToFile(String fileName, String dataLine,
			boolean isAppendMode, boolean isNewLine) {
		DataOutputStream  dos;
		if (isNewLine) {
			dataLine = "\n" + dataLine;
		}
		try {
			File outFile = new File(fileName);
			if (isAppendMode) {
				dos = new DataOutputStream(new FileOutputStream(fileName, true));
			} else {
				dos = new DataOutputStream(new FileOutputStream(outFile));
			}

			dos.writeBytes(dataLine);
			dos.close();
		} catch (FileNotFoundException ex) {
			return (false);
		} catch (IOException ex) {
			return (false);
		}
		return (true);
	}
	
	
	public static byte[] getBytesFromFile(File file) throws Exception {
		if (file == null){
			throw new Exception("file in null");
		}
		InputStream is = new FileInputStream(file);
		byte[] bytes = null;
		try {
	        // Get the size of the file
	        long length = 0;
	    	int nextByte = 0;
	        while (nextByte >= 0) {
	        	nextByte = is.read();
	        	length++;
	        	if (length > Integer.MAX_VALUE) {// File is too large
	        		throw new IOException("File is too large");
	        	}
	        }
	        is.close();
	        is = new FileInputStream(file);

	        // Create the byte array to hold the data
	        bytes = new byte[(int)length];

	        // Read in the bytes
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length
	               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }

	        // Ensure all the bytes have been read in
	        if (offset < bytes.length - 1) {
	            throw new IOException("Could not completely read resource " + file.getAbsolutePath());
	        }
		} finally {
			// Close the input stream and return bytes
			is.close();
		}
        return bytes;
    }
	
	public static Serializable loadObjectFromFile(String fileName){
		synchronized (syncObj) {
			FileInputStream f_in;
			try {
				f_in = new FileInputStream (fileName);
				ObjectInputStream obj_in = new ObjectInputStream (f_in);
				Object obj = obj_in.readObject ();
				if (obj instanceof Serializable)
				{
					f_in.close();
					return (Serializable) obj;
				}
				else return null;

			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;
			}
		}

	}
	
	public static <T extends Serializable> boolean saveObjectToFile(T objectToSave,String fileName){
		try {
			synchronized (syncObj) {
				FileOutputStream f_out = new FileOutputStream(fileName);
				ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
				obj_out.writeObject (objectToSave);
				obj_out.flush();
				obj_out.close();
				f_out.close();
				return true;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}

	}
	
	
	/**
	 * Returns list of files in a directory that are matching a given extension. 
	 * @param directory - the directory in which the files are located.
	 * @param extension - the file extension to be used as filter to compose the list.
	 * @return - list of files. List<File>
	 */
	public static List<File> getFilesNames(File directory, final String extension) {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(extension)) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] files = directory.listFiles(filter);

        return Arrays.asList(files);
	}
	
	/**
	 * Returns list of directories included in a given directory. 
	 * @param directory - the directory in which the directories are located.
	 * @return - list of directories. List<File>
	 */
	public static List<File> getDirectoriesNames(File directory) {
		FileFilter directoryFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		File[] files = directory.listFiles(directoryFilter);
        return Arrays.asList(files);
	}
}
