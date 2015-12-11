package com.ability.ease.auto.jsystem;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import jsystem.framework.DBProperties;
import jsystem.framework.FrameworkOptions;
import jsystem.framework.JSystemProperties;
import jsystem.framework.RunProperties;
import jsystem.framework.RunnerStatePersistencyManager;
import jsystem.framework.common.CommonResources;
import jsystem.framework.report.ExecutionListener;
import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.framework.report.Summary;
import jsystem.framework.report.TestInfo;
import jsystem.framework.scenario.JTest;
import jsystem.framework.scenario.JTestContainer;
import jsystem.framework.scenario.RunnerTest;
import jsystem.framework.scenario.RunningProperties;
import jsystem.framework.scenario.Scenario;
import jsystem.framework.scenario.ScenarioHelpers;
import jsystem.framework.scenario.ScenariosManager;
import jsystem.framework.scenario.flow_control.AntForLoop;
import jsystem.runner.ErrorLevel;
import jsystem.runner.agent.publisher.TestPublisher;
import jsystem.runner.agent.tests.PublishTest;
import jsystem.runner.remote.Message;
import jsystem.runner.remote.RemoteTestRunner;
import jsystem.runner.remote.RemoteTestRunner.RemoteMessage;
import jsystem.utils.FileUtils;
import jsystem.utils.StringUtils;
import junit.framework.AssertionFailedError;

import com.ability.ease.common.BaseTest;
import com.ability.ease.auto.system.WorkingEnvironment;
import com.ability.ease.auto.utilities.EmailUtil;
import com.ability.ease.auto.utilities.ScenarioHelper;


/**
 * this class extends JSystem PublishTest in order to customize the default published email format.
 */
public class PublishTestsResults extends PublishTest implements ExecutionListener {

	private static final String REPORT_PACKAGE_PATH = "\\com\\ability\\ease\\auto\\jsystem\\";

	private boolean addSummaryReport = false;
	private String emailCc;
	private static Map<String, String> failedScenarios = new HashMap<String, String>();
	private static Map<String, String> responsibles = new HashMap<String, String>();

	private String browser;
	private String runMode;
	private String branch;
	private StringBuilder fullReportUrl;
	private final static String DEFAULT_REPONSIBLE = "Nageswar Bodduri";
	private static int numberOftestsIgnored = 0;
	private static final String TEST_SEPARATOR = " | ";

	private static final String scenario_to_monitor = "ODataRest-Tests";
	private static int oDataRest_tests_fail = 0;
	private static int oDataRest_tests_pass = 0;
	/**
	 * this hash map contains all the parameters that we want to send via the
	 * email
	 */
	protected HashMap<MsgType, String> msgHash = null;

	/**
	 * all the parameters that may be needed for the publish/email sending
	 */
	protected enum MsgType {
		SCENARIO,
		SUT,
		VERSION,
		BUILD,
		USER,
		START_TIME,
		NUM_TEST,
		NUM_TEST_FAIL,
		NUM_TEST_PASS,
		NUM_TEST_WARNING,
		STATION,
		RUN_INDEX,
		DESCRIPTION,
		HTML
	}

	//static Logger log = Logger.getLogger(PublisherForRcm.class);

	/**
	 * Publishes (email and/or save results on JSystem report server)
	 * @param mapInlineImages - optional:: images to be placed in the email encapsulated in map. Put null if not required.
	 * @throws Exception
	 */
	public void publish(Map<String,String> mapInlineImages) throws Exception {

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put(DBProperties.ACTION_TYPE, getActionType());
		hashMap.put(DBProperties.DESCRIPTION, this.getRunMode());
		hashMap.put(DBProperties.VERSION, version);
		hashMap.put(DBProperties.BUILD, build /*getValue(getBuild(), buildString)*/);
		hashMap.put(DBProperties.INIT_REPORT, isInitReporter());
		hashMap.put(DBProperties.SCENARIO_NAME, getScenarioName());
		hashMap.put(DBProperties.STATION, InetAddress.getLocalHost().getHostName());
		hashMap.put(DBProperties.SETUP, sut());
		hashMap.put(DBProperties.STATION, InetAddress.getLocalHost().getHostName()+" ("+InetAddress.getLocalHost().getHostAddress()+")");
		// In case there is a runner - try to send to it
		jsystem.runner.remote.Message publishMessage = null;
		if (RemoteTestRunner.getInstance() != null) {
			report.report("RemoteTestRunner is not null");
			publishMessage = createAPublishMessage(hashMap);
			try {
				publishMessage = RemoteTestRunner.getInstance().sendPublishMessage(publishMessage, this);
			} catch (Exception e) {
				log.log(Level.SEVERE, "problem Publishing to DB!\n\n" + StringUtils.getStackTrace(e));
				report.report("problem Publishing to DB!", StringUtils.getStackTrace(e), false);
			}
		} else {
			report.report("RemoteTestRunner is null");
			try {
				TestPublisher tb = new TestPublisher();
				tb.setWithGUI(false);
				publishMessage = tb.publish(hashMap);
			} catch (Throwable t) {
				log.fine("Failed publishing");
			}
		}

		generateMsgHashMap(publishMessage);
		updateRunPropertiesWithPublishURL();

		// send email only in publish_and_email or in email Action type
		if (actionType == ActionType.email || actionType == ActionType.publish_and_email) {
			String error = wereMailParametersDefined();
			if (error != null) {
				report.report("Can't email results, since "	+ error	+ " is not configured in JSystem properties", false);
				return; //changed from just return;
			}
			String htmlBody = buildEmailBody(publishMessage);
			String fromAddress = JSystemProperties.getInstance().getPreference(FrameworkOptions.MAIL_FROM_ACCOUNT_NAME);
			String userName = JSystemProperties.getInstance().getPreference(FrameworkOptions.MAIL_FROM_USER_NAME);
			String password = JSystemProperties.getInstance().getPreference(FrameworkOptions.MAIL_FROM_PASSWORD);
			String host = JSystemProperties.getInstance().getPreference(FrameworkOptions.MAIL_HOST);
			boolean ssl = Boolean.parseBoolean(JSystemProperties.getInstance().getPreference(FrameworkOptions.MAIL_SSL));
			String port = JSystemProperties.getInstance().getPreference(FrameworkOptions.MAIL_SMTP_PORT);
			String toAddress = getSendTo();
			String ccAddress = getEmailCc();

			int testsPass= Integer.parseInt(msgHash.get(MsgType.NUM_TEST_PASS))-1; 
			int testsFail=Integer.parseInt(msgHash.get(MsgType.NUM_TEST_FAIL));
			int testTotal = Integer.parseInt(msgHash.get(MsgType.NUM_TEST))-1;
			int testsWarn = Integer.parseInt(msgHash.get(MsgType.NUM_TEST_WARNING));

			String successRate = " (" + ((testsFail==0)?"100%)":((new DecimalFormat( "#.0" ).format((1.0-(double)(testsFail)/(double)(testTotal-numberOftestsIgnored))*100)))+"%)");
			setMailSubject(getMailSubject()+successRate);
			EmailUtil.send(host, port, userName, password, toAddress, ccAddress, fromAddress, getMailSubject(), htmlBody, mapInlineImages);
		}
	}


	//@Override
	public Message createAPublishMessage(HashMap<String, Object> theMap /*Test test*/) {
		Message message = new Message();
		//	message.setType(RemoteMessage.M_PUBLISH);
		message.addField(this.getFullUUID());
		message.addField(createStringForPublishMessage(theMap,	DBProperties.ACTION_TYPE));
		message.addField(createStringForPublishMessage(theMap,	DBProperties.DESCRIPTION));
		message.addField(createStringForPublishMessage(theMap,	DBProperties.INIT_REPORT));
		message.addField(createStringForPublishMessage(theMap,	DBProperties.VERSION));
		message.addField(createStringForPublishMessage(theMap, DBProperties.BUILD));
		message.addField(createStringForPublishMessage(theMap,	DBProperties.SCENARIO_NAME));
		message.addField(createStringForPublishMessage(theMap,	DBProperties.STATION));
		message.addField(createStringForPublishMessage(theMap, DBProperties.SETUP));
		return message;
	}

	/**
	 * changed: added this method 
	 * @param p
	 *            properties that contains the description, setup name, version
	 *            and build (field name and values)
	 * @param fieldName
	 *            TestPublisher.DESCRIPTION,TestPublisher.SETUP_NAME,TestPublisher
	 *            .VERSION or TestPublisher.BUILD must be const
	 * @return string that contains field name , delimiter and it's value
	 */
	private String createStringForPublishMessage(HashMap<String, Object> p, String fieldName) {
		Object fieldValue = p.get(fieldName);
		if (fieldValue == null ) {
			fieldValue = "";
		}
		else if (StringUtils.isEmpty(fieldName)){
			fieldValue = "";
		}else { 
			fieldValue = p.get(fieldName).toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(fieldName);
		sb.append(delimiter);
		sb.append(fieldValue);
		return sb.toString();
	}

	/**
	 * generates message that will be send as email message if the user
	 * published the data to the webServer it will add the relevant links
	 * 
	 * @param msg
	 *            message that was get from the RemoteTestRunner class
	 * @return message that will be send via the email
	 * @throws Exception
	 */
	protected String buildEmailBody(jsystem.runner.remote.Message msg) {
		boolean publishSuccessfull = (msgHash.get(MsgType.RUN_INDEX) != null);
		if (publishSuccessfull) {
			report.report("Results where published to Reports server");
		} else if (actionType != ActionType.email) {
			report.report("Results where not published to Reports server", false);
		}

		File emailTemplate = new File(JSystemProperties.getCurrentTestsPath() + REPORT_PACKAGE_PATH + "AutoEmailTemplate.html");
		try {
			StringBuilder agentInfoString = new StringBuilder(FileUtils.read(emailTemplate));
			// collect run info
			HashMap<String,String> runInfo = new HashMap<String,String>();

			//Pair<String,String> linkToInstallKit = getLinkToInstallFilesKit();

			String url = WorkingEnvironment.getEaseURL();
			String runningTime = calculateRunningTime();			
			runInfo.put("replace_css_style", FileUtils.read(JSystemProperties.getCurrentTestsPath() + REPORT_PACKAGE_PATH + "EmailTemplate.css"));
			runInfo.put("replace_title", getDescription());
			runInfo.put("replace_im-build", getBuild());
			runInfo.put("replace_stream", getBranch());
			runInfo.put("replace_total_tests",String.valueOf(Integer.parseInt(msgHash.get(MsgType.NUM_TEST))-1));			
			runInfo.put("replace_fail_tests",msgHash.get(MsgType.NUM_TEST_FAIL));
			runInfo.put("replace_warning_tests",msgHash.get(MsgType.NUM_TEST_WARNING));
			runInfo.put("replace_run_time", runningTime);
			runInfo.put("replace_web_browser",this.getBrowser());
			runInfo.put("replace_jsystem_station", InetAddress.getLocalHost().getHostName()); //Summary.getInstance().getProperty("Station").toString());
			runInfo.put("replace_app_url", url);
			runInfo.put("replace_full_report_url", fullReportUrl.toString());

			//calculate success rate of main scenarios
			String OData_Rest_success_rate =  ((new DecimalFormat( "#.0" ).format((1.0-(double)(oDataRest_tests_fail)/(double)(oDataRest_tests_fail + oDataRest_tests_pass))*100)))+"%";
			runInfo.put("replace_main_success_rate", "Success rate for " +scenario_to_monitor +": " +  OData_Rest_success_rate + " ("+ oDataRest_tests_pass + "/" + (oDataRest_tests_fail + oDataRest_tests_pass) +")" );

			replacePlaceHolders(agentInfoString, runInfo);


			// add responsible table
			StringBuilder resposibilitiesStringBuilder = new StringBuilder();
			if (Integer.parseInt(msgHash.get(MsgType.NUM_TEST_FAIL))>0) {
				// generating the responsible list
				resposibilitiesStringBuilder.append("<br>").append( generateResponsibleTable());
			} else {
				resposibilitiesStringBuilder = resposibilitiesStringBuilder.append("All tests passed successfully.");
			}
			String replaceResponsibleTableString = "replace_responsibleTable";
			int replaceRespTableInd = agentInfoString.indexOf(replaceResponsibleTableString);
			agentInfoString.replace(replaceRespTableInd, replaceRespTableInd + replaceResponsibleTableString.length() , resposibilitiesStringBuilder.toString());

			try {
				return agentInfoString.toString();
			} catch (Exception e2) {
				report.report("Fail to write ResposibilitiesStructureToFile for parallel automation ! ", Reporter.WARNING);
				e2.printStackTrace();
			}
			return null;
		} catch (IOException e) {
			report.report("Failed to read/write email template file." + emailTemplate.getAbsolutePath(), Reporter.FAIL);
			e.printStackTrace();
		}
		return null;
	}


	/**********************  private methods *********************************/


	private void replacePlaceHolders(StringBuilder agentInfoString, HashMap<String,String> runInfo) {
		Set<String> keys = runInfo.keySet();
		for (String key : keys){
			int startOldText = agentInfoString.indexOf(key);
			if (startOldText != -1){
				int endOldText = startOldText + key.length();
				agentInfoString.replace(startOldText, endOldText, runInfo.get(key));
			}
		}
	}

	private StringBuilder generateResponsibleTable() {
		StringBuilder resposibilitiesStringBuilder = new StringBuilder(); // make a separate resposibilities String Builder 

		resposibilitiesStringBuilder.append("<div id=\"responsibleTable\">");
		resposibilitiesStringBuilder.append("<table>");
		resposibilitiesStringBuilder.append("<thead>");
		resposibilitiesStringBuilder.append("<th> Scenario </th>");
		resposibilitiesStringBuilder.append("<th> Tests </th>");
		resposibilitiesStringBuilder.append("<th> Responsible </th>");
		resposibilitiesStringBuilder.append("</tr>");
		resposibilitiesStringBuilder.append("</thead>");
		Iterator<?> it = failedScenarios.entrySet().iterator();
		resposibilitiesStringBuilder.append("<tbody>");
		int j = 1;
		while  (it.hasNext()) {
			resposibilitiesStringBuilder.append("<tr>");
			Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
			String[] scenariosTree = pairs.getKey().split("->");
			String scenario = "";
			if (scenariosTree.length > 1) {
				for (int i=1; i<scenariosTree.length-1; i++) {
					scenario += scenariosTree[i]+">";
				}
			} else {
				scenario += scenariosTree[0];
			}
			if (scenariosTree.length > 1) {
				resposibilitiesStringBuilder.append("<td style=\"word-break:break-all width=\"20%\" font-size:10.0pt\">" /*+scenario*/ + scenariosTree[scenariosTree.length-1]);
			} else {
				resposibilitiesStringBuilder.append("<td>"+scenario);
			}
			int numberOfFailedTests = pairs.getValue().split("\\"+TEST_SEPARATOR.trim()).length; 
			if (numberOfFailedTests  >= 1) {//Add number of fail test only when count is successful
				resposibilitiesStringBuilder.append("<font color=\"red\">  (" + numberOfFailedTests + " fail)</font></td>");
			}else {
				resposibilitiesStringBuilder.append("</td>");
			}				

			resposibilitiesStringBuilder.append("<td>"+pairs.getValue()+"</td>");
			resposibilitiesStringBuilder.append("<td>"+responsibles.get(pairs.getKey())+"</td>");
			resposibilitiesStringBuilder.append("</tr>");			
			j++;
		}
		resposibilitiesStringBuilder.append("</tbody></table></div>");
		return resposibilitiesStringBuilder;
	}


	private String calculateRunningTime() {
		//calculating the running time
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date startTimne = null;
		Date endTime = null;
		try {
			startTimne = df.parse((String) Summary.getInstance().getProperties().getProperty("Start"));
			endTime = df.parse(df.format(new Date()));
		} catch (ParseException e) {
			startTimne = new Date();
			endTime = new Date();
		}
		long runTimeSecons = (endTime.getTime()-startTimne.getTime()) / (1000) % 60;
		long runTimeMins = (endTime.getTime()-startTimne.getTime()) / (1000 * 60) % 60;
		long runTimeHours = (endTime.getTime()-startTimne.getTime()) / (1000 * 60 * 60) % 60;

		return runTimeHours+":"+runTimeMins+":"+runTimeSecons;
	}

	private String wereMailParametersDefined() {
		FrameworkOptions[] optionsToCheck = new FrameworkOptions[] {
				FrameworkOptions.MAIL_FROM_USER_NAME,
				FrameworkOptions.MAIL_HOST, FrameworkOptions.MAIL_SMTP_PORT,
				FrameworkOptions.MAIL_FROM_ACCOUNT_NAME };
		for (FrameworkOptions option : optionsToCheck) {
			if (JSystemProperties.getInstance().getPreference(option) == null) {
				return option.toString();
			}
		}
		return null;
	}

	/**
	 * this method updates run properties with publish URL
	 */
	private void updateRunPropertiesWithPublishURL() {
		try {
			// base URL
			StringBuilder baseUrl = new StringBuilder("");
			baseUrl.append("http://").append(
					DBProperties.getInstance().getProperty("serverIP")).append(
							":").append(DBProperties.getInstance().getProperty("browser.port"));
			// detail report URL
			StringBuilder runDetailurl = new StringBuilder("");
			runDetailurl.append(baseUrl.toString()).append("/reports/runDetails.jsp?runIndex=").append(msgHash.get(MsgType.RUN_INDEX));

			// full report URL
			fullReportUrl = new StringBuilder("");
			fullReportUrl.append(baseUrl.toString()).append("/").append(msgHash.get(MsgType.HTML));

			// update run properties with publish URL for run detail report
			RunProperties.getInstance().setRunProperty(LAST_PUBLISH_DETAIL_URL,	runDetailurl.toString());
			// update run properties with publish URL for full report
			RunProperties.getInstance().setRunProperty(LAST_PUBLISH_FULL_REPORT_URL, fullReportUrl.toString());

			// update run properties of finish run
			RunProperties.getInstance().setRunProperty("automation.run.status","finish");
		} catch (Exception e) {
			report.report("Failed writing publish url to run props  "
					+ e.getMessage(), StringUtils.getStackTrace(e), true);
			log.log(Level.WARNING, "Failed writing publish url to run props");
		}
	}

	private String addSummaryFileToAttachments() {
		if (!addSummaryReport) {
			return getAttachments();
		}
		String dir = JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER)
				+ File.separator + "current" + File.separator + "summary.html";
		if (dir.startsWith(File.separator)) {
			dir = dir.substring(1);
		}
		return getAttachments() == null ? dir : dir + CommonResources.DELIMITER + getAttachments();
	}


	/**
	 * if the current value is not empty , return it, otherwise check the
	 * summary
	 * 
	 * @param currentValue
	 *            the current value
	 * @param summarykey
	 *            the summary key to search
	 * @return
	 */
	private Object getValue(String currentValue, String summarykey) {
		if (!StringUtils.isEmpty(currentValue)) {
			return currentValue;
		}
		/*Object summaryValue = summary.getProperty(summarykey);
		if (!StringUtils.isEmpty((String) summaryValue)) {
			return summaryValue;
		}*/
		return "";
	}



	public String getEmailCc() {
		return emailCc;
	}



	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}



	public void addWarning(junit.framework.Test test) {
		// TODO Auto-generated method stub

	}



	public void startTest(TestInfo testInfo) {
		report.report("Start TEST");

	}



	public void endRun() {
		// TODO Auto-generated method stub

	}



	public void startLoop(AntForLoop loop, int count) {
		// TODO Auto-generated method stub

	}



	public void endLoop(AntForLoop loop, int count) {
		// TODO Auto-generated method stub

	}



	public void startContainer(JTestContainer container) {
		report.report("New container is about to start ... ");
		ScenariosManager.getInstance().getCurrentScenario();

	}



	public void endContainer(JTestContainer container) {
		report.report("end container");

	}



	public void addError(junit.framework.Test test, Throwable t) {
		//report.report("add error");

	}



	public void addFailure(junit.framework.Test test, AssertionFailedError t) {
		//	report.report("add failure");

	}


	/**
	 * this method is performed for each time a test execution has reached to its end
	 */
	public void endTest(junit.framework.Test test) {
		if (BaseTest.wasTestIgnored == true) {
			++numberOftestsIgnored;
		}
		RunnerTest currentTest = ScenariosManager.getInstance().getCurrentScenario().getTest(RunnerStatePersistencyManager.getInstance().getActiveTestIndex());
		//int number_of_enable_tests_in_current_test_scenario = ScenarioHelper.getTestScenario(currentTest).getEnabledTests().size();
		String scenarioHierarchyName = ScenarioHelper.getTestScenarioFullHierarchy(currentTest);
		scenarioHierarchyName = scenarioHierarchyName.substring(0, scenarioHierarchyName.lastIndexOf("-->")); //removing test from the scenario hierarchy name	
		String[] scenarios = scenarioHierarchyName.split("-->");

		if (scenarioHierarchyName.contains(scenario_to_monitor)) {
			if (!BaseTest.wasTestIgnored) {
				if (ListenerstManager.getInstance().getLastTestFailed() == true){
					oDataRest_tests_fail++;
				}else {
					oDataRest_tests_pass++;
				}
			}
		}


		boolean testFail = ListenerstManager.getInstance().getLastTestFailed();
		if ((testFail == true || (testFail == false && currentTest.isMarkedAsNegativeTest()) )  && !BaseTest.wasTestIgnored) {	
			if (testFail == true && currentTest.isMarkedAsNegativeTest()) {
				return;
			}
			//if (currentTest.isMarkedAsNegativeTest() == false) {			
			String testName = currentTest.getMeaningfulName();
			if (currentTest.isMarkedAsKnownIssue() == true) {
				testName=testName+(" (known issue)");
			}

			scenarioHierarchyName="";
			for (int i=0; i<scenarios.length; i++) {
				scenarioHierarchyName = scenarioHierarchyName +scenarios[i]+"->";
			}
			scenarioHierarchyName = scenarioHierarchyName.substring(0,scenarioHierarchyName.lastIndexOf("->"));

			if (failedScenarios.containsKey(scenarioHierarchyName)) {
				if (!failedScenarios.get(scenarioHierarchyName).contains(testName)) {
					testName = failedScenarios.get(scenarioHierarchyName) + TEST_SEPARATOR + testName;
				} else {
					testName = failedScenarios.get(scenarioHierarchyName);
				}
			} 


			Scenario scenario = ScenarioHelper.getTestScenario(ScenariosManager.getInstance().getCurrentScenario().getTest(RunnerStatePersistencyManager.getInstance().getActiveTestIndex()));
			String scenarioComment = scenario.getComment();
			if (!StringUtils.isEmpty(scenarioComment)) { 
				scenarioHierarchyName = scenarioHierarchyName.replace(scenarioComment,"").replace("-", "").trim();
			}
			failedScenarios.put(scenarioHierarchyName, testName);

			// Looking for responsible(s) in test's and ancestors scenarios (new method)
			JTest jtest = ScenarioHelper.getCurrentRunningTest();		
			List<Scenario> parentScenarios = ScenarioHelper.getScenarioAncestors(jtest);
			responsibles.put(scenarioHierarchyName, DEFAULT_REPONSIBLE); 
			if (parentScenarios != null) {
				String responsible = null;
				for (Scenario s : parentScenarios) {	
					//responsible = s.getResponsible(); //ScenarioHelper.getTestScenario(ScenariosManager.getInstance().getScenario(parentScenarios.get(scenarioNumber).getName())).getIgnoreRule();
					try {
						//responsible = ScenarioHelpers.getScenarioProperties(s.getName(), true).get(RunningProperties.SCENARIO_RESPONSIBLE_TAG).toString();
					} catch (NullPointerException ex) {
						responsible = null;
					} catch (Exception ex) {
						report.report("failed to retrieve reponsible: " + ex.getStackTrace(), Reporter.WARNING);
						responsible = null;
					}
					if (!com.ability.ease.auto.utilities.StringUtils.isEmptyAfterTrimming(responsible)){
						responsibles.put(scenarioHierarchyName, responsible);
						break;
					}
				}
			}
		}
		//}
	}

	public void startTest(junit.framework.Test test) {
	}

	public void remoteExit() {
		// TODO Auto-generated method stub

	}

	public void remotePause() {
		// TODO Auto-generated method stub

	}

	public void executionEnded(String scenarioName) {
		// TODO Auto-generated method stub

	}

	public void errorOccured(String title, String message, ErrorLevel level) {
		// TODO Auto-generated method stub

	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getRunMode() {
		return runMode;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

}

