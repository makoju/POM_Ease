package com.ability.ease.auto.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.jcraft.jsch.*;

import com.aqua.filetransfer.ftp.FTPFileTransfer;
import com.aqua.sysobj.conn.CliCommand;
import com.aqua.sysobj.conn.CliConnection;

public class FtpUtil {

	/**
	 * Use this method to download file using secure SFTP 
	 * @param host - machine name
	 * @param port 
	 * @param user
	 * @param password
	 * @param ftpRemoteDirectory - folder where desire file reside over remote host
	 * @param remoteFileName - name of file to download
	 * @param localFile - local file to be created
	 * @throws Exception
	 */
	public  static void downloadFileFromSFTP(String host, int port, String user, String password ,
			String ftpRemoteDirectory , String remoteFileName, File localFile) throws Exception{
		JSch jsch=new JSch();
	    Session session=jsch.getSession(user, host, port);
	    session.setPassword(password);
	   
	    session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        System.out.println("Connected to " + host + ".");
        
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp channelSftp = (ChannelSftp) channel;

        boolean success = false;
        OutputStream output = new FileOutputStream(localFile.getAbsolutePath());
        channelSftp.cd(ftpRemoteDirectory);
        channelSftp.get(remoteFileName,output);
        success = true;
        if (success)
        	System.out.println("DOWNLOAD_SUCCESS_MSG" + remoteFileName);
        output.close();
	}
	
	
	/**
	 * Use this method to download file using FTP protocol ( default port is 21 )
	 * @param host - machine name
	 * @param user
	 * @param password
	 * @param ftpRemoteDirectory - folder where desire file reside over remote host
	 * @param remoteFileName - name of file to download
	 * @param localFile - local file to be created
	 * @throws Exception
	 */
	/*public  static void downloadFile(String host, String user, String password ,
			String ftpRemoteDirectory , String remoteFileName, File localFile) throws Exception{
		FTPClient client = new FTPClient();
		client.connect(host);
		client.login(user, password);
		client.changeDirectory(ftpRemoteDirectory);
		client.download(client.list(remoteFileName)[0].getName(), localFile);
	}*/
	
	/**
	 * Use this method to upload file using FTP protocol ( default port is 21 )
	 * @param host - remote machine name
	 * @param user - remote ftp user
	 * @param password - remote ftp password
	 * @param localFile - local file to be created
	 * @throws Exception
	 */
	/*public  static void uploadFile(String host, String user, String password , File localFile) throws Exception{
		FTPClient client = new FTPClient();
		client.connect(host);
		client.login(user, password);
		client.upload(localFile);
	}*/
	/**
	 * retrieves JSystem FTP home directory (e.g c:/jsystem/runner/aquaftp21) and deletes all files in this folder
	 * 
	 * @param ftpSession
	 *            some FTPFileTransfer object that is already initialized
	 * @return true for success, false for error.
	 */
	public static boolean deleteJSystemFtpServerHomeDirectory(FTPFileTransfer jSystemFtpSession) {
		String jsystemFtpHomeDirectory = jSystemFtpSession.getFtpServerHome();
		if (jsystemFtpHomeDirectory != null) {
			// delete the folder
			jsystem.utils.FileUtils.deltree(jsystemFtpHomeDirectory);
			// re-create the folder
			File f = new File(jsystemFtpHomeDirectory);
			f.mkdir();
			return true;
		}
		return false;
	}

	/**
	 * exit ftp prompt which is when the cli connection prompt is "ftp>". it is required in order to
	 * be able to run cli commands which are not related to ftp. call this method after completing
	 * the use of the <code>FTPFileTransfer</code> object operations such as copy to remote machine
	 * or copy from remote machine to local machine.
	 * 
	 * @param ftpSession
	 * @throws Exception
	 */
	public static void exitFtpPrompt(FTPFileTransfer ftpSession) throws Exception {
		if (ftpSession != null) {
			exitFtpPrompt(ftpSession.cliConnection);
		}
	}

	/**
	 * exit ftp prompt which is when the cli connection prompt is "ftp>". it is required in order to
	 * be able to run cli commands which are not related to ftp. call this method after completing
	 * the use of the <code>FTPFileTransfer</code> object operations such as copy to remote machine
	 * or copy from remote machine to local machine.
	 * 
	 * @param ftpSession
	 * @throws Exception
	 */
	public static void exitFtpPrompt(CliConnection cliConnection) throws Exception {
		if (cliConnection.getResultPrompt() != null) {
			if (cliConnection.getResultPrompt().getPrompt().indexOf(">") > -1) {
				try {
					CliCommand cmd = new CliCommand("bye");
					cmd.setIgnoreErrors(true);
					cliConnection.handleCliCommand("exit ftp prompt", cmd);
				} catch (Exception ex) {
					// ignore exception
				}
			}
		}
	}
}
