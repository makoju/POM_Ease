package com.ability.ease.common;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ability.auto.common.ShellExecUtil;

public class CommonUtils {

	static int failCounter = 0;
	
	static ArrayList<String> pDetails = new ArrayList<String>();

	public static boolean stopStartEaseServer(String request)throws Exception{

		boolean result = false;
		pDetails = getServerStatus();
		String sPID = pDetails.get(0);
		String serverStatus = pDetails.get(1);
		
		if(request.equalsIgnoreCase("Stop") && (serverStatus != null && serverStatus.equalsIgnoreCase("Running"))){
			result = stopServer(sPID);
		}else if(request.equalsIgnoreCase("Stop") && serverStatus.equalsIgnoreCase("Stopped")){
			System.out.println("EASE server is already stopped...!!!");
			return true;
		}else if(request.equalsIgnoreCase("Start") && serverStatus.equalsIgnoreCase("Stopped")){
			result = startServer(sPID);
		}else{
			System.out.println("EASE server is already running...!!!");
			return true;
		}
		return result;
	}

	private static boolean startServer(String pid) throws InterruptedException{
		boolean isServerStarted = false;
		int count = 0;
		StringBuilder shellCommand = new StringBuilder("sh /opt/abilitynetwork/ease/bin/easeServer.sh ");
		
		shellCommand.append("start");
		while(!isServerStarted && count++ < 3){
			runCommand(shellCommand.toString());
			System.out.println("Waiting for 10 seconds to EASE server to start !!!");
			Thread.sleep(10000);
			pDetails = getServerStatus();
			if( pDetails.get(1).equalsIgnoreCase("Stopped")){
				System.out.println("EASE server not started. So, retyring to start :: attempt " + count);
				continue;
			}else{
				System.out.println("EASE server started successfully");
				isServerStarted = true;
				break;
			}
		}
		return isServerStarted;
	}

	private static boolean stopServer(String pid) throws InterruptedException{
		boolean isServerStopped = false;
		int count = 0;
		StringBuilder shellCommand = new StringBuilder("sh /opt/abilitynetwork/ease/bin/easeServer.sh ");
		
		shellCommand.append("stop");
		while(!isServerStopped && count++ < 3){
			runCommand(shellCommand.toString());
			System.out.println("Waiting for 10 seconds to EASE server to stop!!!");
			Thread.sleep(10000);
			pDetails = getServerStatus();
			if( pDetails.get(1).equalsIgnoreCase("Running")){
				System.out.println("EASE server not stopped. So, retyring to stop :: attempt " + count);
				continue;
			}else{
				System.out.println("EASE server stopped successfully");
				isServerStopped = true;
				break;
			}
		}
		return isServerStopped;
	}

	private static ArrayList<String> getServerStatus(){
		
		String statusCommand = "sh /opt/abilitynetwork/ease/bin/easeServer.sh status";
		ArrayList<String> processDetails = new ArrayList<String>();
		String EASEServerCurrentStatus = ShellExecUtil.executeShellCmd(statusCommand);
		String sPID = getPID(EASEServerCurrentStatus);
		
		System.out.println("EASE Server Status : " + EASEServerCurrentStatus);
		String serverStatus = null;

		if( EASEServerCurrentStatus != null){
			if(! EASEServerCurrentStatus.contains("not")){
				serverStatus = "Running";
			}else{
				serverStatus = "Stopped";
			}
		}
		processDetails.add(sPID);
		processDetails.add(serverStatus);
		
		return processDetails;
	}

	private static String runCommand(String shellCommand){
		String output = ShellExecUtil.executeShellCmd(shellCommand.toString());
		System.out.println("Command : " + shellCommand + " -> output is :" + output);
		return output;
	}
	
	private static String getPID(String output){
		Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(output);
		String pid = null;
	     while(m.find()) {
	    	 pid=m.group(1);    
	     }
	    return pid;
	}
}
