package com.ability.auto.common;

import com.ability.ease.auto.system.WorkingEnvironment;
import com.jcraft.jsch.*;

import java.io.*;

/**
 * 
 * @author nageswar.bodduri
 *
 */
public class ShellExecUtil {

	public static String executeShellCmd(String shellCommand){
		
		Channel channel = null;
		Session session = null;
		String outputFromShell = null;
		String sHostName = WorkingEnvironment.getEaseGridServer1();
		String sUserName = "empadmin";
		try{
			System.out.println("Creating JSch object...");
			JSch jsch = new JSch();
			System.out.println("Creating session and channel objects...");
			session = jsch.getSession(sUserName, sHostName, 22); 

			UserInfo ui=new MyUserInfo();
			session.setUserInfo(ui);
			session.setTimeout(3000);
			session.connect();
			System.out.println("SSH Session got created succssfully to " + sHostName);

			channel = session.openChannel("exec");
			((ChannelExec)channel).setPty(true);
			((ChannelExec)channel).setCommand("sudo -S -p '' " + shellCommand);
			InputStream in=channel.getInputStream();
			OutputStream out=channel.getOutputStream();
			((ChannelExec)channel).setErrStream(System.err);
			channel.connect();
			out.write(("Totally#1"+"\n").getBytes());
			out.flush();
			Thread.sleep(5000);
			byte[] temp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(temp, 0, 1024);
					if (i < 0) {
						break;
					}
					outputFromShell = new String(temp, 0, i);//It is printing the response to console
				}
				channel.disconnect();
				if(channel.isClosed()){
					break;
				}
			}
		}catch(Exception e){
			System.out.println("Exception occured during creation of session and/or channel: ERROR" + e.getMessage());
			e.printStackTrace();
		}finally{
			System.out.println("Closing channel with " + sHostName);
			channel.disconnect();
			System.out.println("Closing session with " + sHostName);
			session.disconnect();
		}
		return outputFromShell;
	}

	public static class MyUserInfo implements UserInfo {

		String passwd;

		public String getPassword(){
			return passwd;
		}
		public boolean promptYesNo(String str){
			str = "Yes";
			return true;
		}

		public String getPassphrase(){
			return null;
		}
		public boolean promptPassphrase(String message){
			return true;
		}
		public boolean promptPassword(String message){
			passwd="Totally#1";
			return true;
		}
		@Override
		public void showMessage(String arg0) {
			// TODO Auto-generated method stub
		}
	}
}
