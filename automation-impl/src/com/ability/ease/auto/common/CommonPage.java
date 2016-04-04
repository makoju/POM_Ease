package com.ability.ease.auto.common;

import jsystem.framework.report.Reporter.ReportAttribute;

import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class CommonPage extends AbstractPageObject{

	int failCounter = 0;
	public boolean stopStartEaseServer(String status)throws Exception{


		String statusCommand = "sh /opt/abilitynetwork/ease/bin/easeServer.sh status";
		StringBuilder shellCommand = new StringBuilder("sh /opt/abilitynetwork/ease/bin/easeServer.sh ");

		String easeServerCurrentStatus = ShellExecUtil.executeShellCmd(statusCommand);
		report.report("Ease Server Status : " + easeServerCurrentStatus);
		String serverStatus = null;

		if( easeServerCurrentStatus != null){
			if(! easeServerCurrentStatus.contains("not")){
				serverStatus = "Server Running";
			}else{
				serverStatus = "Server Stopped";
			}
		}

		if(status.equalsIgnoreCase("Stop") && serverStatus.equalsIgnoreCase("Server Running")){
			shellCommand.append("stop");
		}
		if(status.equalsIgnoreCase("Start") && serverStatus.equalsIgnoreCase("Server Stopped")){
			shellCommand.append("start");
		}
		if(status.equalsIgnoreCase("Stop") && serverStatus.equalsIgnoreCase("Server Stopped")){
			report.report("Ease server is in stopped state...no need to stop again :) :) !!!");
			return true;
		}
		if(status.equalsIgnoreCase("Start") && serverStatus.equalsIgnoreCase("Server Running")){
			report.report("Ease server is already running...no need to start again :) :) !!!");
			return true;
		}

		String output = ShellExecUtil.executeShellCmd(shellCommand.toString());
		report.report("Command : " + shellCommand + " -> output is :" + output);
		if( output != null){
			if(status.equalsIgnoreCase("Start")){
				report.report("Request : Start Ease");
				if(output.trim().equalsIgnoreCase("Starting EASE Server...")){
					report.report("Ease server started successfully", ReportAttribute.BOLD);
				}else{
					failCounter++;
					report.report("Fail : Failed to start ease server !!!");
				}
			}else if(status.equalsIgnoreCase("Stop")){
				report.report("Request : Stop Ease");
				if(output.trim().equalsIgnoreCase("Stopping EASE Server...")){
					report.report("Ease server stopped successfully", ReportAttribute.BOLD);
				}else{
					failCounter++;
					report.report("Fail : Failed to stop ease server !!!");
				}
			}
		}
		report.report("Waiting for sometime to Ease server to get stopped...");
		Thread.sleep(60000);
		return (failCounter == 0 ) ? true : false;
	}




	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}
