package com.ability.ease.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsystem.extensions.paramproviders.GenericObjectParameterProvider;
import jsystem.extensions.paramproviders.GolbalParametersBean;
//import jsystem.extensions.paramproviders.GolbalParametersBean;
import jsystem.extensions.paramproviders.ObjectArrayParameterProvider;
import jsystem.framework.JSystemProperties;
import jsystem.framework.ParameterProperties;
import jsystem.framework.RunProperties;
import jsystem.framework.RunnerStatePersistencyManager;
import jsystem.framework.report.ListenerstManager;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.JTest;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.Parameter.ParameterType;
import jsystem.framework.scenario.ParameterProvider;
import jsystem.framework.scenario.RunningProperties;
import jsystem.framework.scenario.Scenario;
import jsystem.framework.scenario.ScenarioHelpers;
import jsystem.framework.scenario.ScenariosManager;
import jsystem.framework.scenario.UseProvider;
import jsystem.utils.TimeStopper;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ability.ease.auto.junit.IgnoreTest;
import com.ability.ease.auto.junit.SystemTestCase4Custom;
import com.ability.ease.auto.common.annotations.IgnoreIf;
import com.ability.ease.auto.common.annotations.SharedParameter;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.common.test.resource.TestResource;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.auto.jsystem.PublishTestsResults;
import com.ability.ease.auto.system.WorkingEnvironment;
import com.ability.ease.auto.systemobjects.MySut;
import com.ability.ease.auto.systemobjects.WebDriverSystemObject;
import com.ability.ease.auto.utilities.ReflectUtil;
import com.ability.ease.auto.utilities.ScenarioHelper;
import com.ability.ease.auto.utilities.StringUtils;

/**
 *  the super class that each test class should extend
 *
 */
public class BaseTest extends  SystemTestCase4Custom { /*SystemTestCase4*/ 	

	protected final static int TEST_RECOMMENDED_TIME_LIMIT = 20;    // warn if a test ran more than this value (in minutes)
	protected final static long TEST_TIMEOUT = 1000*60*60; //  Stop a run of a test if ran more than this value (in milliseconds)
	protected final static long REST_TEST_TIMEOUT = 1000*60*5; //  Stop a run of a test if ran more than this value (in milliseconds)
	protected final static long JMETER_TEST_TIMEOUT = 10800000; //3 HOURS
	private String methodName;
	protected TestResource [] testResources;
	protected static TimeStopper timeStopper = null;
	protected TestType testType;
	protected static MySut mySut = null;
	protected WebDriverSystemObject seleniumSystemObject;
	protected ApplicationContext context;
	private static int otherCount;
	private static int selenium2Count;
	private static int restfullCount;
	private static int tewsCount;
	private final static String IGNORE_RULE_REGEXP="@ignoreif\\s*\\(\\s*((and|or|not)\\s*\\()?(\\s*(site)\\s*(=|!=)\\s*\\w+\\s*(,)?\\s*)+\\)\\s*(\\))?\\s*";
	public static boolean wasTestIgnored;
	private final static String GLOBAL_PARAMATER_REGEXP="#{1}\\{{1}(\\-?\\w+\\-?\\w?)+\\}{1}";
	private static Pattern globalParameterPattern = Pattern.compile(GLOBAL_PARAMATER_REGEXP, Pattern.CASE_INSENSITIVE);	
	protected static Map<String, String> globalParamMap = null;
	private static String testFirstScenarioName = StringUtils.EMPTY_STRING;
	private static boolean WereListeneresAdded = false;
	public static String imBuild = "N/A";
	public static String imfwBuild = "N/A";
	private static Writer sceanrioTimeFile = null;
	private static String storedScenarioName = StringUtils.EMPTY_STRING;
	private static final String SCENARIOS_TIME_FILE_NAME = "scenarios.time";



	/**
	 * This method is being performed at the beginning of each test method.
	 * Every sub-class should call it in the overridden method.
	 * @throws Exception
	 */
	@Before	
	public final void _setup() throws Exception {

		if (!WereListeneresAdded) {			
			JSystemProperties.getInstance().setIsReporterVm(true);
			PublishTestsResults publishTests = new PublishTestsResults();
			JSystemProperties.getInstance().setIsReporterVm(false);
			ListenerstManager.getInstance().addListener(publishTests);	
			WereListeneresAdded=true;
		}


		WorkingEnvironment.setTestType(testType);
		if (testType == null){
			throw new Exception ("Please set the test type !!!!" );
		}
		
		if (mySut == null) {
			mySut = (MySut)system.getSystemObject("mySut");
		}

		switch (testType) {
		case Selenium2: 		   
			context = new ClassPathXmlApplicationContext("resources/spring/beancontext.xml");
			RunProperties.getInstance().setRunProperty(testType.name() + "_count", Integer.toString(++selenium2Count));
			break;
			/*case RESTful:
				RunProperties.getInstance().setRunProperty(testType.name() + "_count", Integer.toString(++restfullCount));
				break;*/
		default: 
			RunProperties.getInstance().setRunProperty(testType.name() + "_count", Integer.toString(++otherCount));
		}

		// handle ignore rules	
		IgnoreTest.ignoreIf(shouldIgnoreTest()); 

		// handle global parameters	
		handleGlobalParameters();

		// copy test resources if needed
		if (testResources != null && testResources.length > 0) {
			copyAllTestResource();
		}
	}


	/**
	 * This method set network use for sharing drive c:\ 
	 * @param host	- machine host name 
	 * @param user	- user to login
	 * @param password	- user password
	 * @throws IOException
	 */
	private void setNetworkUse(String host, String user, String password) throws IOException {
		// delete connection if exists to avoid 'Multiple connections' error
		String netUseCmd = "net use \\\\" + host + "\\c$ /DELETE";
		// start new connection
		netUseCmd = "net use \\\\" + host + "\\c$  /USER:" + user + " " + password + " /PERSISTENT:yes";
		report.report("Running : " + netUseCmd);
		Runtime.getRuntime().exec(netUseCmd);
	}


	/*protected void takeScreenshot(String title){
		List<WebDriverEventListener> allRegisteredWebDriverEventListeners = seleniumSystemObject.getAllRegisteredWebDriverEventListeners();
		for (WebDriverEventListener webDriverEventListener : allRegisteredWebDriverEventListeners) {
			if (webDriverEventListener instanceof WebDriverScreenshotEventHandler ){
				WebDriverScreenshotEventHandler screenshot =(WebDriverScreenshotEventHandler)webDriverEventListener; // safe cast
				screenshot.takeScreenshot(driver, title);
				break;
			}
		}
	}*/


	/**
	 * called by JRunner framework.
	 * Adds all {@link SharedParameter} of the class as properties to the {@link RunProperties} test member.
	 * each sub class should call this if implementing handleUIEvent() method.
	 * @param map map of name of the parameter (String) and its values (Parameter).
	 * 		  if the parameter is user_defined its value is an Object of this user_defined class
	 * @param methodName the test method name i.e. checkAddUserToResource
	 * @throws Exception
	 */
	public void handleUIEvent(HashMap<String, Parameter> map, String methodName) throws Exception {
		if (map == null || methodName == null) {
			return;
		}

		Parameter testType = map.get("TestType");
		if (testType!=null) {
			if (testType.isVisible()) {
				this.methodName = methodName;
				testType.setOptions(getTestTypeOptions());
			}
		}
	}

	protected void copyAllTestResource() throws Exception {
		if (  testResources == null || testResources.length == 0){
			report.report("-------------->>> No files/dir were entered as parameters over TestResource !!!", Reporter.WARNING);
			return;
		}
		else {
			report.report("Copying all test resources in status shouldCopy=TRUE...");
		}
		for (TestResource resource : testResources) {
			if (resource.isShouldCopy()){
				boolean isFile = new File (JSystemProperties.getCurrentTestsPath() + resource.getSource().getPath().replaceAll("classes", "")).isFile();
				String srcDir =  JSystemProperties.getCurrentTestsPath() + resource.getSource().getPath().replaceAll("classes", "");
				report.report("Copying to Entity="+resource.getDestinationEntity().toString());
				if (isFile) {
					report.report("Source is File : " + srcDir);
					String originalDestinationFile = resource.getConstructedPath() ;
					String destinationFile = originalDestinationFile;
					if (!destinationFile.contains(".")){ // source is File and destination is Folder -> need to add file name
						destinationFile = destinationFile +  new File(srcDir).getName();
					}


					originalDestinationFile = originalDestinationFile.substring(0, originalDestinationFile.lastIndexOf("\\"));
					originalDestinationFile = originalDestinationFile.replace("/", "\\");
					FileUtils.copyFile(new File(srcDir), new File(destinationFile), true);
					report.report("Copy : " + srcDir + " to:" + destinationFile);

					report.report("-----------Finish copy test resource----------------");
				}
			}	
		}
	}

	public TestResource[] getTestResources() {
		return testResources;
	}

	@ParameterProperties(description = "Optional: source and destination of test resources")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setTestResources(TestResource[] testResources) {
		this.testResources = testResources;
	}

	/**
	 * returns an array of the supported test types attached to the current test method.
	 * @return
	 */
	public String[] getTestTypeOptions() {
		TestType[] testTypes = ReflectUtil.getSupportedTestTypes(this.getClass(), methodName); 
		if (testTypes != null) {
			String[] strTestTypes = new String[testTypes.length];
			for (int i = 0; i < testTypes.length; i++) {
				strTestTypes[i] = testTypes[i].name();
			} 
			return strTestTypes;
		} else {
			return  new String[] { "" };
		}
	}
	public TestType getTestType() {
		return testType;
	}

	@ParameterProperties(description = "Mandatory: please select the test type")
	public void setTestType(TestType testType) {
		this.testType = testType;
	}

	/**
	 * Returns true if test should be ignored, and false otherwise.
	 * Valid ignore rules:
	 * 1) @ignoreIf(or(os!=linux,db!=sqlServer))
	 * 2) @ignoreIf(db!=sqlServer)
	 * 3) @ignoreIf(and(os=windows,db!=Oracle))
	 * 4) @ignoreIf(and(os=windows,db=sqlServer))
	 * 5) @ignoreIf(today!=weekend)
	 * @return
	 * @throws Exception 
	 */
	protected boolean shouldIgnoreTest() throws Exception {

		wasTestIgnored = false;

		//delete test's should.ignore.test property from scenario properties file and from cache
		ScenarioHelpers.resetCache();
		String testFulUUID = ScenariosManager.getInstance().getCurrentScenario().getTest(RunnerStatePersistencyManager.getInstance().getActiveTestIndex()).getFullUUID();
		String rootScenarioName = System.getProperty(RunningProperties.CURRENT_SCENARIO_NAME);
		ScenarioHelpers.setTestProperty(testFulUUID, rootScenarioName, RunningProperties.SCENARIO_SHOULD_IGNORE, "false", false);
		// checking if test's  @IgnoreIf annotation was declared by the test 
		String[] rules;
		StringBuilder sb = new StringBuilder();
		Method method = this.getClass().getMethod(this.getMethodName());
		Annotation[] annotations = method.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if(annotation instanceof IgnoreIf) {
				sb.append("@IgnoreIf(");
				sb.append(((IgnoreIf) annotation).rule()+")");
				sb.append("\n");				
			}
		}		
		if (sb!=null && sb.length()>0) {
			rules = sb.toString().toLowerCase().split("\n");	
			if (isIgnoreRuleMatch(rules, null)) {
				return true;
			}
		}

		// current test doesn't have ignore rule annotation => continue looking for ignore rule(s) in test's scenario and ancestors
		JTest test = ScenarioHelper.getCurrentRunningTest();		
		List<Scenario> parentScenarios = ScenarioHelper.getScenarioAncestors(test);
		if (parentScenarios != null) {
			String ignoreRule = null;
			for (Scenario scenario : parentScenarios) {
				try {				
					ignoreRule = ScenarioHelpers.getScenarioProperties(scenario.getName(), true).get(RunningProperties.SCENARIO_IGNORE_RULE_TAG).toString();
				} catch (NullPointerException ex) {
					ignoreRule = null;
				} catch (Exception ex) {
					throw ex;
				}
				if (!StringUtils.isEmptyAfterTrimming(ignoreRule)){
					//cycle over all exiting @ignoreIf lines
					ignoreRule = ignoreRule.toLowerCase();
					rules = ignoreRule.split("\n");
					return (isIgnoreRuleMatch(rules, scenario.getName())); //parentScenarios.get(scenarioNumber).getName()));
				}
			}
		}
		report.report("Dynamic ignore rue handler: no ignore rules were found for current test.");
		return false;
	}

	/**
	 * private method that returns true if at least one ignore rule was met, or false otherwise.
	 * @param ignoreRules
	 * @param scenarioName
	 * @return
	 * @throws Exception
	 */
	private boolean isIgnoreRuleMatch(String[] ignoreRules, String scenarioName) throws Exception {
		report.report(String.format("Looking for ignore rules... %s ",(ignoreRules.length == 0)?
				"no ignore rules found attached to the parent scenario.":
					"found " + ignoreRules.length + " ignore rules attached to parent scenario." ));
		for (String rule : ignoreRules) {
			if (rule.trim().matches(IGNORE_RULE_REGEXP))	 {
				int leftBrackIndex = rule.lastIndexOf("(");
				int rightBrackIndex = rule.indexOf(")");
				String ruleContent = rule.substring(leftBrackIndex+1, rightBrackIndex).trim();
				//split operands
				String operand = null;
				String[] expressions = null;
				boolean shouldIgnore = true;
				if (rule.contains("or(")) {
					expressions = ruleContent.split(",");
					operand = "or";
					shouldIgnore = false;
				} else if (rule.contains("and(")) {
					expressions = ruleContent.split(",");
					operand = "and";
					shouldIgnore = true;
				} else if (rule.contains("not(")) {
					if (ruleContent.contains(",")) {
						report.report("The following ignore Rule is NOT valid. When using the 'not' operand, only one expression is supported: "+rule, Reporter.WARNING );
						break; //exits if
					}
					expressions = new String[]{ruleContent};
					operand = "not";
					shouldIgnore = true;
					/* }else if (rule.contains("today")) {
			    	expressions = new String[]{ruleContent};*/
				}  else {
					expressions = new String[]{ruleContent};
				}
				boolean isCurrentExpressionEqual = false;
				for (int i = 0 ; i < expressions.length; i++) {
					//get left and right hands of current expression
					String expressionEqualSign = null;
					if (expressions[i].contains("!=")) {
						expressionEqualSign="!=";
					} else if (expressions[i].contains("=")) {
						expressionEqualSign="="; 
					}
					String[] hands = expressions[i].split(expressionEqualSign);
					String leftHand = hands[0].trim();
					String rightHand = hands[1].trim();
					if (leftHand.equalsIgnoreCase("site")) {
						//isCurrentExpressionEqual = mySut.getSite().name().equalsIgnoreCase(rightHand.trim());
						/*} else if (leftHand.equalsIgnoreCase("db")) {
			    		isCurrentExpressionEqual = rcm.environment.database.getVendor().trim().equalsIgnoreCase(rightHand.trim());
			    	} else if (leftHand.equalsIgnoreCase("server")) {
			    		isCurrentExpressionEqual = rcm.environment.applicationServer.getVendor().trim().equalsIgnoreCase(rightHand.trim());
			    	} else if (leftHand.equalsIgnoreCase("today")) {
			    		if ("weekend".equals(rightHand)) {	
			    			isCurrentExpressionEqual = isWeekend();*/
					} else {
						report.report("Unrecognized right hand expression in ignore rule:"+expressions[i], Reporter.FAIL);
					}
					if ("=".equals(expressionEqualSign))  {
						if (operand == null) {
							shouldIgnore = isCurrentExpressionEqual;
						}else if (operand.equalsIgnoreCase("or")) {
							shouldIgnore = shouldIgnore | isCurrentExpressionEqual;						    			  
						} else if (operand.equalsIgnoreCase("and")) {
							shouldIgnore = shouldIgnore & isCurrentExpressionEqual;	
						}else if (operand.equalsIgnoreCase("not")) {
							shouldIgnore = !isCurrentExpressionEqual;	
						}
					} else if ("!=".equals(expressionEqualSign))  {
						if (operand == null) {
							shouldIgnore = !(isCurrentExpressionEqual);
						} else if (operand.equalsIgnoreCase("or")) {
							shouldIgnore = shouldIgnore | !(isCurrentExpressionEqual);						    			  
						} else if (operand.equalsIgnoreCase("and")) {
							shouldIgnore = shouldIgnore & !isCurrentExpressionEqual;	
						}else if (operand.equalsIgnoreCase("not")) {
							shouldIgnore = isCurrentExpressionEqual;
						}
					}						   
				}
				if (shouldIgnore) {
					report.report("The following ignore rule was met: " + rule + " in "+((scenarioName!=null)?"scenario: "+scenarioName:" test: "+this.getName()), Reporter.ReportAttribute.BOLD);
					wasTestIgnored = true;
					return true;
				}
			} else {			
				report.report("The ignore rule '"+rule+ "'in"+ ((scenarioName != null)?" scenario '"+scenarioName:" test '"+this.getName())+"' is NOT valid!", Reporter.WARNING );				
				report.reportHtml("See valid ignore rules examples here",
						"1. @ignoreIf(site!=ITC) <br>"+
								"2. @ignoreIf(not(site=ITC))<br> " +
								"3. @ignoreIf(server=jboss)<br> " +
								"<b>Note1:</b> only 'site' is currently supported.<br> " +
								"<b>Note2:</b> following operands are supported: or, and, not.<br> "+
								"<b>Note3:</b> when using the 'not' operand, only one expression is supported", true);
			}		
		}
		return false;
	}

	/**
	 * Handles test's parameters global parameters
	 * @throws Exception 
	 */
	private void handleGlobalParameters() throws Exception {	
		// initializing global parameters map once for each scenario +  start new timer for a new scenario
		String currentScenarioName = ScenarioHelper.getTestScenario(ScenarioHelper.getCurrentRunningTest()).toString();
		if (!currentScenarioName.equals(storedScenarioName)) { //If a new scenario is about to start ...
			if (timeStopper == null) {
				timeStopper = new TimeStopper();

			} else {
				if (sceanrioTimeFile == null) {
					sceanrioTimeFile = new BufferedWriter(new FileWriter(getScenarioTimeFilePath(), true));
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					sceanrioTimeFile.append(dateFormat.format(cal.getTime()) + " :: scenarios time by minutes\n"); 
				}
				// keep previous scenario runtime in file
				sceanrioTimeFile.append(storedScenarioName + "=" + timeStopper.getTimeDiffInMin() + "\n");
				sceanrioTimeFile.flush();
			}
			timeStopper.init();
			storedScenarioName = currentScenarioName;
			globalParamMap = initGlobalParametersMap();
		}

		report.report(String.format("Global parameters handler: %s ",	(globalParamMap.isEmpty())?
				"no global parameteres found for current test.":
					"found " + globalParamMap.size() + " global parameters for current test." ));
		if (globalParamMap.isEmpty()) {
			return;
		}

		// replacing test's global parameters with global parameters values
		Parameter[] testParams = ScenarioHelper.getCurrentRunningTest().getVisibleParamters(); //.getParameters();
		for (Parameter param : testParams) {
			if (param.getValue() == null || 
					StringUtils.isEmpty(param.getValue().toString()) || 
					!param.isEditable() ||  //this doesn't seem to work
					!param.isVisible())  {
				continue;
			}
			Method method = null;
			switch (param.getType())  {
			case JSYSTEM_INTERNAL_FLAG:
				break;
			case SHORT:
			case INT:
			case FLOAT:				
			case LONG:
			case DOUBLE:
			case STRING:					
				if (param.isGlobalParam()) {  	
					String pNewValue = getNewGlobalParamValue(param.getValue().toString());					    					    	
					if (pNewValue!=null) {						
						try { // try to set param with new value		 
							method = param.getSetMethod();
							method.setAccessible(true);
							if (param.getType() == ParameterType.STRING) {
								method.invoke(this, pNewValue);
							} else if (param.getType() == ParameterType.SHORT) {
								method.invoke(this, Short.parseShort(pNewValue));
							}  else if (param.getType() == ParameterType.INT) {
								method.invoke(this, Integer.parseInt(pNewValue));
							}  else if (param.getType() == ParameterType.FLOAT) {
								method.invoke(this, Float.parseFloat(pNewValue));
							}  else if (param.getType() == ParameterType.LONG) {
								method.invoke(this, Long.parseLong(pNewValue));
							}  else if (param.getType() == ParameterType.DOUBLE) {
								method.invoke(this, Double.parseDouble(pNewValue));
							} 
							report.report("Setting parameter '"+param.getName()+"' to: "+pNewValue, Reporter.ReportAttribute.BOLD);
						} catch (Exception ex) {
							report.report ("Failed to replace parameter '"+param.getName()+"' with global parameter value\n"+ex.getStackTrace(), Reporter.FAIL);
						}
					}
				}
				break;	
			case STRING_ARRAY:
			case ENUM:
			case REFERENCE:
			case DATE:
			case FILE:
			case BOOLEAN:
				if (param.isGlobalParam()) {
					report.report("WARNING!! '" + param.getParamTypeString() +"' type is not supproted by global parameters handler. Please send a request to automation team");
				}
				break;
			case USER_DEFINED: 
				ParameterProvider paramProvider = param.getProvider();
				if (paramProvider!=null) {
					if (paramProvider instanceof GenericObjectParameterProvider) {
						// provider = generic object means an object. 
						// the idea here is to search the object members for for global fields (e.g, thos that look like  #{??} ) and replace them with real values.
						method = param.getGetMethod();						
						Object object =  method.invoke(this);
						if (object!=null) {
							Field[] fields = getAllFields(object.getClass());

							// cycle all object's single-value fields and replace global parameters if required
							boolean wasObjectUpdated = false;
							String fValue = null;
							for (Field f : fields) {
								try {
									method = object.getClass().getMethod("get"+f.getName().substring(0,1).toUpperCase()+f.getName().substring(1,f.getName().length()));
									if (method.getReturnType().equals(String.class) ||
											method.getReturnType().equals(Integer.class) ||
											method.getReturnType().equals(Long.class) || 
											method.getReturnType().equals(Short.class) ||
											method.getReturnType().equals(Double.class) ||
											method.getReturnType().equals(Float.class)) {
										Object o = method.invoke(object); // get field value
										if (o != null) {
											fValue = o.toString();			
											// looking for global property in .run.properties
											if (globalParameterPattern.matcher(fValue).matches()) {
												String globalParamValueFromRunPropertiesFile = RunProperties.getInstance().getRunProperty(fValue);
												if (globalParamValueFromRunPropertiesFile != null) {
													String propertyName = fValue.replace("#","").replace("{","").replace("}","");
													globalParamMap.put(propertyName, globalParamValueFromRunPropertiesFile+"~"+GolbalParametersBean.GLOBAL_PARAM_TYPE.DYNAMICALLY.name());
												}
											}
											String fNewValue = getNewGlobalParamValue(fValue);					
											if (fNewValue != null) {
												method = object.getClass().getMethod("set"+f.getName().substring(0,1).toUpperCase()+f.getName().substring(1,f.getName().length()),String.class);
												method.setAccessible(true);
												if (f.getType().equals(String.class)) {
													method.invoke(object, fNewValue);
												} else if (f.getType().equals(Short.class)) {
													method.invoke(object, Short.parseShort(fNewValue));
												} else if (f.getType().equals(Integer.class)) {
													method.invoke(object, Integer.parseInt(fNewValue));
												} else if (f.getType().equals(Float.class)) {
													method.invoke(object, Float.parseFloat(fNewValue));
												} else if (f.getType().equals(Long.class)) {
													method.invoke(object, Long.parseLong(fNewValue));
												} else if (f.getType().equals(Double.class)) {
													method.invoke(object, Double.parseDouble(fNewValue));
												}
												wasObjectUpdated = true;
												report.report("Parameter '"+f.getName()+"' in object '"+object.getClass()+"' was set to: "+fNewValue, Reporter.ReportAttribute.BOLD);
											}
										}
									}  else {
										Object o = method.invoke(object);
										if (o != null) {
											if ( globalParameterPattern.matcher(method.invoke(object).toString()).matches()) {
												report.report("This error message is refering to test's parameter: '"+f.getName() +" in object "+ object.getClass()+"'." +
														" Currently, global parameters are not supported for parameter type "+f.getType(), Reporter.FAIL);
												wasObjectUpdated = false;
												break; //loop
											}
										}
									}
								} catch (NoSuchMethodException ex) {
									continue;
								} catch (Exception ex) {
									throw ex;
								}
							}
							// update object with global parameters values
							if (wasObjectUpdated) {
								Method pSetMethod = param.getSetMethod();
								pSetMethod.setAccessible(true);									
								pSetMethod.invoke(this, object);
							}	
						}
					} else if (paramProvider instanceof ObjectArrayParameterProvider) {		
						// provider = array object means array of objects (of same type)
						// the idea here is to search the objects members for for global fields (e.g, those that look like  #{??} ) and replace them with real values.
						method = param.getGetMethod();						
						Object[] objects = (Object[]) method.invoke(this);
						boolean wasObjectUpdated = false;
						if (objects != null) {
							for (int i=0; i<objects.length; i++) {
								Field[] fields = objects[i].getClass().getDeclaredFields();
								//wasObjectUpdated = false;
								String fValue = null;
								for (Field f : fields) {
									try {
										method = objects[i].getClass().getMethod("get"+f.getName().substring(0,1).toUpperCase()+f.getName().substring(1,f.getName().length()));
										if (method.getReturnType().equals(String.class) ||
												method.getReturnType().equals(Integer.class) ||
												method.getReturnType().equals(Long.class) || 
												method.getReturnType().equals(Short.class) ||
												method.getReturnType().equals(Double.class) ||
												method.getReturnType().equals(Float.class)) {
											fValue = method.invoke(objects[i]).toString().toLowerCase();
											String fNewValue = getNewGlobalParamValue(fValue);	

											if (fNewValue != null) {
												method = objects[i].getClass().getMethod("set"+f.getName().substring(0,1).toUpperCase()+f.getName().substring(1,f.getName().length()),String.class);
												method.setAccessible(true);
												if (f.getType().equals(String.class)) {
													method.invoke(objects[i], fNewValue);
												} else if (f.getType().equals(Short.class)) {
													method.invoke(objects[i], Short.parseShort(fNewValue));
												} else if (f.getType().equals(Integer.class)) {
													method.invoke(objects[i], Integer.parseInt(fNewValue));
												} else if (f.getType().equals(Float.class)) {
													method.invoke(objects[i], Float.parseFloat(fNewValue));
												} else if (f.getType().equals(Long.class)) {
													method.invoke(objects[i], Long.parseLong(fNewValue));
												} else if (f.getType().equals(Double.class)) {
													method.invoke(objects[i], Double.parseDouble(fNewValue));
												}
												wasObjectUpdated = true;
												report.report("Parameter '"+f.getName()+"' in object '"+objects[i].getClass()+"' was set to: "+fNewValue);
											}
										} else {
											Object o = method.invoke(objects[i]);
											if (o != null) {
												if ( globalParameterPattern.matcher(o.toString()).matches() ) {
													report.report("This error message is refering to test's parameter: '"+f.getName()+"' in array object: " + objects[i].getClass() +
															" Currently, global parameters are not supported for parameter type "+f.getType(), Reporter.FAIL);
													wasObjectUpdated = false;
													break; //loop
												}
											}												
										}
									} catch (NoSuchMethodException ex) {
										continue;
									} catch (NullPointerException ex) {
										continue;
									} catch (Exception ex) {
										throw ex;
									}
								}
							}
						}
						if (wasObjectUpdated) {
							// update current test's parameter with global parameters values
							Method pSetMethod = param.getSetMethod();
							pSetMethod.setAccessible(true);
							Object[] args = {objects};
							pSetMethod.invoke(this, args);
						}
					}
				}
				break;
			default:  
				if (param.isGlobalParam()) {
					report.report("This error message is refering to test's parameter: '"+param.getName()+"'. Currently, global parameters are not supported for parameter type "+param.getType().toString(), Reporter.FAIL);
				}
				break;
			}
		}
		report.report("--------- End Of Global Parameters Handler -------------");
	}
	/**
	 * Returns all class fields include all inherited fields
	 * @param object
	 * @return
	 */
	private static Field[] getAllFields(Class object) {
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(object.getDeclaredFields()));
		if (object.getSuperclass() != null) {
			fields.addAll(Arrays.asList(getAllFields(object.getSuperclass())));
		}
		return fields.toArray(new Field[] {});
	}




	/**
	 * Initializes the global parameters map.
	 * Searches for global parameters also in parents scenarios.
	 * Current scenario has the precedence over same global parameters names in parents scenarios. 
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> initGlobalParametersMap() throws Exception {
		JTest test = ScenarioHelper.getCurrentRunningTest();		
		List<Scenario> parentScenarios = ScenarioHelper.getScenarioAncestors(test);
		if (parentScenarios != null) {
			String globalParamsString= null;
			ParameterProvider provider = new ObjectArrayParameterProvider(); // global parameters are kept on Object array container
			Object[] obj = null;
			globalParamMap = new HashMap<String, String>();
			//while (scenarioNumber < parentScenarios.size()) {
			for (Scenario scenario : parentScenarios) {
				//globalParamsString = scenario.getGlobalParameters(); 
				//This is a VERY expensive operation ==> ScenarioHelper.getTestScenario(ScenariosManager.getInstance().getScenario(parentScenarios.get(scenarioNumber).getName())).getGlobalParameters();
				try {
					globalParamsString = ScenarioHelpers.getScenarioProperties(scenario.getName(), true).get(RunningProperties.SCENARIO_GLOBAL_PARAMETERS_TAG).toString();
					scenario.setGlobalParameters(globalParamsString);  // => update scenario global parameters in cache
				} catch (NullPointerException ex) {
					globalParamsString = null;
				} catch (Exception ex) {
					throw ex;
				}
				if (!StringUtils.isEmpty(globalParamsString)) {
					obj = (GolbalParametersBean[]) provider.getFromString(globalParamsString);
					for (Object o : obj) {
						if (((GolbalParametersBean)o).getGlobalName() == null || ((GolbalParametersBean)o).getGlobalValue() == null) {
							report.report("ignored global parameters with null values: " + ((GolbalParametersBean)o).getGlobalName());
							continue;
						}
						if (!globalParamMap.containsKey(((GolbalParametersBean)o).getGlobalName().toLowerCase())) {
							globalParamMap.put(((GolbalParametersBean)o).getGlobalName().toLowerCase(), 
									((GolbalParametersBean)o).getGlobalValue()+"~"+((GolbalParametersBean)o).getAdded()+"~"+((GolbalParametersBean)o).getBy());
						}
					}
				}
			}
		}
		return globalParamMap;
	}
	protected String replcaeGlobalParamValue(String pValue) {
		return getNewGlobalParamValue(pValue);
	}
	/**
	 * Returns new parameter value for global parameter request, or null if global parameter name was not found.	 * 
	 * @param pValue - a string that contains one or more expressions of global parameters.
	 * A global parameter expression starts with "#{" and ends with "}"
	 * @return - a string with new global parameters values.
	 */
	private String getNewGlobalParamValue(String pValue) {
		Matcher matcher =  null;
		matcher = globalParameterPattern.matcher(pValue);
		String pNewValue = null;
		String globalParamValue = null;
		while (matcher.find()) {
			//get rid of the 'envelope'
			try {
				globalParamValue = globalParamMap.get(matcher.group().replaceAll("#","").replaceAll("\\{","").replaceAll("\\}","").toLowerCase()).split("~")[0];
			} catch (NullPointerException ex) {
				globalParamValue = globalParamMap.get(matcher.group().replaceAll("#","").replaceAll("\\{","").replaceAll("\\}","").toLowerCase());
			}
			if (globalParamValue!= null) {
				pValue = pValue.replaceFirst(GLOBAL_PARAMATER_REGEXP, globalParamValue);
				pNewValue = pValue;
			} else {
				report.report("ERROR!!!   failed to set new global parameter value to '"+matcher.group()+"', " +
						" because it is missing from global parameters table.", Reporter.FAIL);
				pNewValue = null;
				break;
			}
		}
		return pNewValue;
	}

	/**
	 * keeps parameter as global parameter in cache (global parameters map) and in file (scenario property file)
	 * @param globalParamName - the global parameter name to be kept in the global parameters list. This name will be used in order to retrieve the object from the list. 
	 * @param value - The string value to keep as global parameter. Should be instance of String.
	 * @throws Exception
	 */
	public static void  keepAsGloablParameter(String globalParamName, String value) throws Exception {
		addDynamicGlobalParameter(globalParamName.toLowerCase(), value);
	}
	/**
	 * Updates cache with new dynamic global parameter and updates the scenario property of global parameters
	 * Existing global parameter name will be overridden
	 * @param key - the global parameter name 
	 * @param value - the global parameter value
	 * @throws Exception
	 */
	private static void addDynamicGlobalParameter(String key, String value) throws Exception {
		// adding global parameter to .run.properties file
		// think point: is it really necessary to save in .run.properties because no global param handler reads the .run.properties
		// RunProperties.getInstance().setRunProperty("#{"+key+"}", value);

		// adding dynamic global parameter to global parameters map

		String currentTestMethodName = ScenarioHelper.getCurrentRunningTestMethodName();

		globalParamMap.put(key, value+"~"+GolbalParametersBean.GLOBAL_PARAM_TYPE.DYNAMICALLY.name()+"~"+"Test: "+currentTestMethodName);
		JTest test = ScenarioHelper.getCurrentRunningTest();		
		//List<Scenario> parentScenarios = ScenarioHelper.getScenarioAncestors(test);
		String globalParamsString = ScenarioHelper.getTestScenario(test /*ScenariosManager.getInstance().getScenario(parentScenarios.get(0).getName())*/).getGlobalParameters();
		if (StringUtils.isEmpty(globalParamsString)) {
			globalParamsString = GolbalParametersBean.class.getName()+";#"+(new Date()).toString()+"\r\n";
		}

		// ugly code to override existing global name in scenario's global parameter property string

		boolean wasOverridden= false;
		String[] tmp = null;
		if (globalParamsString == null) {
			globalParamsString = StringUtils.EMPTY_STRING;
		}
		tmp = globalParamsString.split("\r\n");
		for (int i=0; i<tmp.length; i++) {
			tmp[i] = tmp[i] +"\r\n";
			if (tmp[i].contains(".GlobalName=")) {
				if (tmp[i].toLowerCase().contains(key.toLowerCase())) {
					//String pNum = org.apache.commons.lang.StringUtils.substringBefore(tmp[i], ".");
					String pNum = tmp[i].substring(0, tmp[i].indexOf("."));
					for (int j=0; j<tmp.length; j++) {
						if (tmp[j].contains(pNum+".GlobalValue=")) {
							tmp[j] = pNum+".GlobalValue="+value+"\r\n";
							wasOverridden = true;
						} else if (tmp[j].contains(pNum+".Added=")) {
							tmp[j] = pNum+".Added="+GolbalParametersBean.GLOBAL_PARAM_TYPE.DYNAMICALLY.name()+"\r\n";;
							wasOverridden = true;
						} else if (tmp[j].contains(pNum+".By=")) {
							tmp[j] = pNum+".By="+"Test: "+currentTestMethodName+"\r\n";;
							wasOverridden = true;
						} 
					}
				}
			}
		}
		if (wasOverridden) {
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<tmp.length; i++) {
				sb.append(tmp[i]);
			}
			globalParamsString = (sb.toString()).replaceFirst("\\{", "").replaceAll("\\}$","");
		} else {
			globalParamsString = globalParamsString + ((globalParamsString.split("\r\n").length-1)/4)+".Added="+GolbalParametersBean.GLOBAL_PARAM_TYPE.DYNAMICALLY.name()+"\r\n" +
					((globalParamsString.split("\r\n").length-1)/4)+".GlobalValue="+value+"\r\n"+
					((globalParamsString.split("\r\n").length-1)/4)+".GlobalName="+key+"\r\n" +
					((globalParamsString.split("\r\n").length-1)/4)+".By="+"Test: "+currentTestMethodName+"\r\n";
		}

		// update cache with global parameter property and also save to file (scenario.properties file)
		ScenarioHelper.getTestScenario(test /*ScenariosManager.getInstance().getScenario(parentScenarios.get(0).getName())*/).setGlobalParameters(globalParamsString);
		try {
			Properties p = ScenarioHelpers.getScenarioProperties(ScenarioHelper.getTestScenario(test /*ScenariosManager.getInstance().getScenario(parentScenarios.get(0).getName())*/).getName());
			p.setProperty(RunningProperties.SCENARIO_GLOBAL_PARAMETERS_TAG, globalParamsString);
			ScenarioHelpers.saveScenarioPropertiesToSrcAndClass(p, ScenarioHelper.getTestScenario(test /*ScenariosManager.getInstance().getScenario(parentScenarios.get(0).getName())*/).getName(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method returns the absolute name of the scenario Times file 
	 */
	protected static String getScenarioTimeFilePath() {
		return TestCommonResource.getResourcesDirectory() + File.separator + SCENARIOS_TIME_FILE_NAME;
	}

	public boolean isGlobalParam(){
		return false;
	}
}
