package com.ability.ease.common;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ability.auto.common.ShellExecUtil;
import com.ability.ease.auto.enums.portal.selenium.WebDriverType;
import com.ability.ease.auto.system.WorkingEnvironment;
import com.ability.ease.auto.systemobjects.DefaultWorkingEnvironment;
import com.ability.ease.auto.systemobjects.WebDriverSystemObject;

public class CommonUtils{

	static int failCounter = 0;

	static ArrayList<String> pDetails = new ArrayList<String>();
	static String gridserver = WorkingEnvironment.getEaseGridServer1();

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
		}
		else if(request.equalsIgnoreCase("ReStart")){
			result = restartServer(sPID);
		}
		else{
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
			runCommand(shellCommand.toString(), gridserver);
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
			runCommand(shellCommand.toString(), gridserver);
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

	private static boolean restartServer(String pid) throws Exception{
		boolean isServerStopped = false;
		int count = 0;
		String killPPIDCommand = "kill -9 `ps -wwfC java |awk -F \" \" 'FNR == 2 {print $3}'`";
		String killPIDCommand = "kill -9 `ps -wwfC java |awk -F \" \" 'FNR == 2 {print $2}'`";

		runCommand(killPPIDCommand,gridserver);
		runCommand(killPIDCommand,gridserver);

		while(!isServerStopped && count++ < 3){
			pDetails = getServerStatus();
			if( pDetails.get(1).equalsIgnoreCase("Running")){
				System.out.println("EASE server not stopped. So, retyring to stop :: attempt " + count);
				runCommand(killPPIDCommand,gridserver);
				runCommand(killPIDCommand,gridserver);
				continue;
			}else{
				System.out.println("EASE server stopped successfully");
				isServerStopped = true;
				break;
			}
		}

		return stopStartEaseServer("Start");


		//StringBuilder shellCommand = new StringBuilder("sh /opt/abilitynetwork/ease/bin/easeServer.sh ");
		/*StringBuilder shellCommand = new StringBuilder(statusCommand); //force kill by PPID of EaseServer Process

		//shellCommand.append("restart");
		while(!isServerStarted && count++ < 3){
			runCommand(shellCommand.toString(), gridserver);
			System.out.println("Waiting for 3 seconds to EASE server to stop !!!");
			Thread.sleep(3000);

			String ppid = ShellExecUtil.executeShellCmd(statusCommand,gridserver);
			if(ppid==null || ppid.isEmpty()) //if no process lists then 
				isServerStarted=true;

		 */

		/*pDetails = getServerStatus();
			if( pDetails.get(1).equalsIgnoreCase("Stopped")){
				System.out.println("EASE server not started. So, retyring to start :: attempt " + count);
				continue;
			}else{
				System.out.println("EASE server started successfully");
				isServerStarted = true;
				break;
			}*/
	}

	private static ArrayList<String> getServerStatus(){

		String statusCommand = "sh /opt/abilitynetwork/ease/bin/easeServer.sh status";
		ArrayList<String> processDetails = new ArrayList<String>();
		String EASEServerCurrentStatus = ShellExecUtil.executeShellCmd(statusCommand,gridserver);
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

	public static String runCommand(String shellCommand, String hostnameorIP){
		String output = ShellExecUtil.executeShellCmd(shellCommand.toString(),hostnameorIP);
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

	public static boolean sendEmailNotification()throws Exception{

		String OSName = null, browserName = null;
		//String mailserver = WorkingEnvironment.getMailServerHost();
		DefaultWorkingEnvironment workingEnvironment = new DefaultWorkingEnvironment();
		String mailserver = workingEnvironment.getMailServerHost();
		StringBuilder command = new StringBuilder("sh /easeauto/notify_results.sh");
		String sBuildInfo = WorkingEnvironment.getEasebuildId();
		int i = sBuildInfo.lastIndexOf(".");
		OSName = OSDetector();
		browserName = getBrowserName();

		command.append(" " + sBuildInfo.substring(0, i) + " " + sBuildInfo + " " + OSName + " " + browserName);
		String output = runCommand(command.toString(), mailserver);
		System.out.println("Command : " + command);
		System.out.println("Output : " + output);

		if(output.contains("e-mail has been sent successfully")){
			return true;
		}else{
			return false;
		}
	}

	public static String OSDetector()throws Exception{
		String OSName = null;

		try{
			OSName = System.getProperty("os.name");
		}catch(Exception e){
			System.out.println("Exception occured while getting the Operating System name");
		}
		return OSName.replaceAll(" ", "-");

	}

	public static String getBrowserName(){
		String browserName = null;
		WebDriverType browserType = WorkingEnvironment.getWebdriverType();

		switch (browserType) {
		case INTERNET_EXPLORER_DRIVER:
			browserName = "InrternetExplorer";
			break;
		case FIREFOX_DRIVER:
			browserName = "Firefox";
			break;
		case CHROME_DRIVER:
			browserName = "Chrome";
			break;
		default:
			System.out.println("Please set the right webdriver type in SUT file in JSYSTEM!!!");
			break;
		}
		return browserName;
	}
}
