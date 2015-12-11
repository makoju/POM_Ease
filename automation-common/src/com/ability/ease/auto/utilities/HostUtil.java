package com.ability.ease.auto.utilities;

import java.net.InetAddress;

import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.utils.TimeStopper;

import com.aqua.filetransfer.ftp.FTPFileTransfer;
import com.aqua.sysobj.conn.CliCommand;
import com.aqua.sysobj.conn.CliConnection;
import com.aqua.sysobj.conn.CliFactory;
import com.aqua.sysobj.conn.WindowsDefaultCliConnection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class HostUtil {

	public static final int DEFAULT_WAIT_SECONDS = 120;
	public static final int DEFAULT_INTERVAL_MILLISECONDS = 10000;
	protected static Reporter report = ListenerstManager.getInstance();
	
	/**
	 * ping a host to check if the host is running. will ping the host in a loop until
	 * <code>DEFAULT_WAIT_SECONDS</code> is reached. waits
	 * <code>DEFAULT_INTERVAL_MILLISECONDS</code> between each iteration.
	 * 
	 * @param hostName
	 * @return
	 * @throws Exception
	 */
	public static boolean pingHostUntilAlive(String hostName) throws Exception {
		return pingHostUntilAlive(hostName, DEFAULT_WAIT_SECONDS, DEFAULT_INTERVAL_MILLISECONDS);
	}

	/**
	 * ping a host to check if the host is running. will ping the host in a loop until waitTime is
	 * reached. waits <code>DEFAULT_INTERVAL_MILLISECONDS</code> between each iteration.
	 * 
	 * @param hostName
	 * @param waitTime
	 * @return
	 * @throws Exception
	 */
	public static boolean pingHostUntilAlive(String hostName, int waitTime) throws Exception {
		return pingHostUntilAlive(hostName, waitTime, DEFAULT_INTERVAL_MILLISECONDS);
	}

	/**
	 * ping a host to check if the host is running. will ping the host in a loop until waitTime is
	 * reached. the interval is used to wait between each iteration.
	 * 
	 * @param hostName
	 *            the host name
	 * @param waitTime
	 *            total wait time in seconds
	 * @param interval
	 *            wait between each ping iteration
	 * @return
	 * @throws Exception
	 */
	public static boolean pingHostUntilAlive(String hostName, int waitTime, int interval) throws Exception {
		TimeStopper ts = new TimeStopper();
		ts.init();
		while (!jsystem.utils.MiscUtils.isPing(hostName) && ts.getTimeDiffInSec() <= waitTime) {
			Thread.sleep(interval);
		}
		if (!jsystem.utils.MiscUtils.isPing(hostName)) {
			return false;
		}
		return true;
	}

	/**
	 * Check if a windows service is started on remote machine
	 * 
	 * @param cli
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	/*public static boolean isWindowsServiceStarted(CliConnection cli, String serviceName) throws Exception {
		CliCommand command = new CliCommand("net start");
		cli.handleCliCommand("net start", command);
		int i = 0;
		while (command.getResult().indexOf(serviceName) < 0 && i < 18) {
			Thread.sleep(10000);
			cli.handleCliCommand("net start", command);
			i++;
		}
		return (command.getResult().indexOf(serviceName) > -1);
	}*/
	
	/**
	 * Check if a list of windows services are started on remote machine
	 * 
	 * @param hostName
	 * @param hostUser
	 * @param hostPassword
	 * @param serviceNames
	 * @return TRUE if all services are up
	 * @throws Exception
	 */
	public static boolean waitForWindowsServiceToStart(WindowsDefaultCliConnection cli, String [] serviceNames) throws Exception {
		
		CliCommand command =  null; 
		command = new CliCommand("uname");
		command.setIgnoreErrors(true);
		cli.handleCliCommand("checking operting system type", command);
		String res = command.getResult().toLowerCase();
		if (res.contains("linux")) {
			throw new Exception("Services status check is currently supported only for Windows OS.");
		} else if (res.contains("windows")) {
			//continue	
		} else {
			//continue, assuming windows
		}
		
		//loop over the services name to check if started
		long timeout = System.currentTimeMillis() + (10*60*1000); //setting timeout to 10 minutes
		for (String serviceName : serviceNames) {
			boolean isServiceStarted = false;
			while (!isServiceStarted && System.currentTimeMillis() < timeout) {
				command = new CliCommand("wmic /locale:ms_409 service where (name='"+serviceName+"') get state /value");
				cli.handleCliCommand("waiting for "+serviceName +" windows service to start. Sleep for "+DEFAULT_INTERVAL_MILLISECONDS/1000+"...", command);
				res = command.getResult();
				if (res.contains("State=Running")) {
					isServiceStarted = true;
					//ListenerstManager.getInstance().report("service: "+serviceName+" is now in running state!");
				} else {
					Thread.sleep(DEFAULT_INTERVAL_MILLISECONDS);
				}
			}
			if (!isServiceStarted) {
				throw new Exception ("Following windows service was not started: "+serviceName);
			}
		}
		cli.disconnect();
		return true;
	}

	public static boolean isWindows2008(FTPFileTransfer ftpFileConnection) throws Exception {
		if (ftpFileConnection.getOperatingSystem().equals(CliFactory.OPERATING_SYSTEM_WINDOWS) == false) {
			return false;
		}
		CliConnection cli = ftpFileConnection.cliConnection;
		CliCommand command = new CliCommand("ver");
		// command.setSilent(true);
		command.setIgnoreErrors(true);
		cli.handleCliCommand("Getting windows version", command);
		String res = command.getResult();
		if (res.indexOf("[Version 6") > -1) {
			return true;
		}
		return false;
	}

	/*public static String getThisHostName() throws Exception {
		Command cmd = new Command();
		cmd.setCmd(new String[] { "hostname" });
		Execute.execute(cmd, true);
		return cmd.getStdout().toString();
	}*/
	
	public static String getLocalHostName() throws Exception  {
	    return InetAddress.getLocalHost().getHostName();
	}
	
	/**
	 * This method checks if the url parameter returns 200 for HEAD request. Every 2 minutes and it gives
	 * up after 10 minutes. * The method checks if a URL is alive.
	 * @param url
	 * @param waitTime
	 * @return
	 * @throws Exception
	 */
	public static boolean isServerStartUpByURL(String url, long waitTime)throws Exception{
		int numberOfRetries = 0;
		final int NUMBER_OF_SERVER_RETRIES = 20;
		
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		do {
			try {
				report.report("trying to get response from url - " + url);
				ClientResponse response = webResource.head();
				if(response.getStatus() == 200){
					return true;
				}
			}
			catch (Exception e) {
				report.report("url not available - checking again in " + waitTime / 1000 + " seconds");
				Thread.sleep(waitTime);
			}
			numberOfRetries++;
		}while ((numberOfRetries < NUMBER_OF_SERVER_RETRIES) && (waitTime > 0) );	
		return false;
		
	}
	
	/* This method checks if the url parameter returns response code 200 for HEAD request every 5 seconds and it gives up after <waitTime> seconds. 
    * This method checks if a URL is alive.
    * @param url     - Server url to connect
    * @param waitTime - Seconds to wait until timeout
    * @param username - User to login
    * @param password - User password
    * @return
    * @throws Exception 
    */
	public static boolean isServerStartUpByURL(String url, long waitTime, String username, String password)throws Exception{
		long timeout = System.currentTimeMillis() + waitTime;
        final int SECONDS_TO_WAIT = 5;
        boolean result = false;
        while (System.currentTimeMillis() < timeout && result == false){
              report.report("trying to get response from url - " + url);
              Client client = Client.create();
              WebResource webResource = client.resource(url);
              String authentication = username + ":" + password;
              byte[] data = authentication.getBytes("UTF-8");
              String encoded = javax.xml.bind.DatatypeConverter.printBase64Binary(data);    
              ClientResponse response = webResource.header("Authorization", " Basic " +encoded).get(ClientResponse.class);
          if(response.getStatus() == 200){
              result = true;
          } 
          report.report("url not available - checking again in " + SECONDS_TO_WAIT + " seconds");
          Thread.sleep(SECONDS_TO_WAIT*1000);
        }
        return result;
	}
}
