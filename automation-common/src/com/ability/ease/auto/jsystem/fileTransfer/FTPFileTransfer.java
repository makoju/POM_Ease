package com.ability.ease.auto.jsystem.fileTransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;

import com.aqua.filetransfer.ftp.FTPRemoteClient;
import com.aqua.sysobj.conn.CliConnection;

public class FTPFileTransfer extends com.aqua.filetransfer.ftp.FTPFileTransfer {

	public FTPFileTransfer(CliConnection cliConnection) throws Exception {
		 this.cliConnection = cliConnection;  
	}
	public  CliConnection   cliConnection;
	private static FTPFileTransfer instance = null;
	
	public void copyFileFromLocalMachineToRemoteLinuxMachine(String ftpUser, String ftpPassword, String ftpServerHome, InputStream source,String destination) throws Exception {
		File f = new File(ftpServerHome,""+System.currentTimeMillis());
		f.deleteOnExit();
		FileOutputStream out = new FileOutputStream(f);
		org.apache.commons.io.IOUtils.copy(source, out);
		org.apache.commons.io.IOUtils.closeQuietly(out);
		FTPRemoteClient remoteClient = new FTPRemoteClient(cliConnection, InetAddress.getLocalHost().getHostName());
		remoteClient.init();
		remoteClient.cliConnection = this.cliConnection;
		remoteClient.setFtpUserName(ftpUser);
		remoteClient.setFtpPassword(ftpPassword);
		remoteClient.setOperatingSystem("Linux");
		remoteClient.setAscii(false);
		remoteClient.setPromptOn(false);
		remoteClient.copyFileFromLocalMachineToRemoteClient(f.getName(), destination);		
	}

	 public static FTPFileTransfer getInstance(CliConnection cliConnection) throws Exception {
	      if(instance == null) {
	         instance = new FTPFileTransfer(cliConnection);
	      }
	      return instance;
	   }
}