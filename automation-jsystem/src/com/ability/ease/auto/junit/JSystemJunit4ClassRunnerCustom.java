package com.ability.ease.auto.junit;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import jsystem.framework.RunnerStatePersistencyManager;
import jsystem.framework.report.ExtendTestListener;
import jsystem.framework.report.ListenerstManager;
import jsystem.framework.scenario.Scenario;
import jsystem.framework.scenario.JTestContainer;
import jsystem.framework.scenario.RunningProperties;
import jsystem.framework.scenario.ScenarioHelpers;
import jsystem.framework.scenario.ScenariosManager;
import jsystem.framework.scenario.flow_control.AntForLoop;
import junit.framework.AssertionFailedError;
import junit.framework.NamedTest;
import junit.framework.SystemTest;
import junit.framework.SystemTestCaseImpl;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestResult;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.junit.IgnoreTest.IgnoreTestCaseException;
import com.ability.ease.auto.utilities.ScenarioHelper;


/**
 * 
 * This is a special runner for JUnit 4 test written for JSystem. It delegates test running
 * events to the ListenersManager by using several quite ugly hacks to translate the events
 * sent by JUnit 4's RunListener and convert them into JUnit 3's TestListener events which
 * ListenersManager expects.
 *
 */
public class JSystemJunit4ClassRunnerCustom extends JUnit4ClassRunner {

	private Logger log = Logger.getLogger(JSystemJunit4ClassRunnerCustom.class.getName());

	/**
	 * 
	 * This Annotation-derived hack of a class allows to add information to a Description
	 * object.
	 * 
	 * The events passed while the test is running (testStarter, testFinished, etc.) carry only
	 * a Description object. Since the Description constructor is private, the only way way
	 * to add information to it (that I can see) is to add an Annotation object to it.
	 *
	 */
	private class TestInfoAnnotation implements Annotation {
		private final String className;
		private final String methodName;

		public TestInfoAnnotation(String className, String methodName) {
			this.className = className;
			this.methodName = methodName;
		}

		public Class<? extends Annotation> annotationType() {
			return TestInfoAnnotation.class;
		}

		public String getClassName() {
			return className;
		}

		public String getMethodName() {
			return methodName;
		}
	}

	/**
	 * 
	 * JUnit 3's TestListener expects a Test object to be passed with every event. This class
	 * is a dummy Test object that holds information about the running test - the class name,
	 * the method name and the uuid.
	 *
	 */
	public class TestInfo implements NamedTest {
		private String className;
		private String methodName;

		public TestInfo(Description description) {
			// Get our special annotation object with the info we need
			TestInfoAnnotation testInfo = (TestInfoAnnotation)description.getAnnotation(TestInfoAnnotation.class);
			if (testInfo != null){
				className = testInfo.getClassName();
				methodName = testInfo.getMethodName();
				test.setName(methodName);
			}
		}

		//@Override
		public String getClassName() {

			return className;
		}

		//@Override
		public String getMethodName() {
			return methodName;
		}

		//@Override
		public String getFullUUID() {
			String fullUUID;
			// check if value was already initiated
			// get parent scenario full unique id first
			String parentFullUUID = System.getProperty(RunningProperties.UUID_PARENT_TAG);
			if (parentFullUUID == null){
				return null;
			}
			while (parentFullUUID.startsWith(".")){
				parentFullUUID = parentFullUUID.substring(1);
			}
			// get test unique id
			String uuid = System.getProperty(RunningProperties.UUID_TAG);
			fullUUID = parentFullUUID.equals("") ? uuid : parentFullUUID + "." + uuid;
			return fullUUID;
		}

		//@Override
		public int countTestCases() {
			throw new RuntimeException("TestInfo.countTestCases should never be called.");
		}

		//@Override
		public void run(TestResult arg0) {
			//throw new RuntimeException("TestInfo.run should never be called.");
		}

		public SystemTest getSystemTest() {
			return test;
		}
	}

	/**
	 * 
	 * This class converts events from JUnit 4's RunListener to JUnit 3's TestListener.
	 *
	 */
	private class TestListenerAdapter extends RunListener implements ExtendTestListener{
		private TestListener testListener;

		TestListenerAdapter(TestListener testListener) {
			this.testListener = testListener; 
		}

		@Override
		public void testFailure(Failure failure) throws Exception {

			if (failure.getException() instanceof IgnoreTestCaseException){
				notifier.fireTestIgnored(failure.getDescription());	
			} else {	
				super.testFailure(failure);
				if (failure.getException() instanceof AssertionFailedError){
					testListener.addFailure(new TestInfo(failure.getDescription()),(AssertionFailedError)failure.getException());
				}else{
					testListener.addError(new TestInfo(failure.getDescription()), failure.getException());
				}
			}
		}

		@Override
		public void testFinished(Description description) throws Exception {
			super.testFinished(description);
			testListener.endTest(new TestInfo(description));
			jsystemEndTest();
		}

		@Override
		public void testStarted(Description description) throws Exception {
			super.testStarted(description);
			TestInfo info = new TestInfo(description);
			methodName = info.getMethodName();
			testListener.startTest(info);
		}

		@Override
		public void testIgnored(Description description) throws Exception {
			//mark current test as "should be ignored" 
			String testFulUUID = ScenariosManager.getInstance().getCurrentScenario().getTest(RunnerStatePersistencyManager.getInstance().getActiveTestIndex()).getFullUUID();
			String scenarioName = System.getProperty(RunningProperties.CURRENT_SCENARIO_NAME);
			String propertyName = RunningProperties.ABORT_CURRENT_SCENARIO_EXECUTION;
			String propertyValue = "true";
			ScenarioHelpers.setTestProperty(testFulUUID,scenarioName, propertyName, propertyValue, false);
		}

		public void addError(Test test, Throwable t) {
			// TODO Auto-generated method stub

		}

		public void addFailure(Test test, AssertionFailedError t) {
			// TODO Auto-generated method stub

		}

		public void endTest(Test test) {
			// TODO Auto-generated method stub

		}

		public void startTest(Test test) {
			// TODO Auto-generated method stub

		}

		public void addWarning(Test test) {
			// TODO Auto-generated method stub

		}

		public void startTest(jsystem.framework.report.TestInfo testInfo) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
		}

		public void endContainer(JTestContainer container) {
			// TODO Auto-generated method stub
		}
	}

	private Class<?> testClass;
	private SystemTest test;
	private String methodName;
	private RunNotifier notifier;

	private void jsystemEndTest(){

		TestResult result = ((SystemTestCaseImpl) test).getTestResult();
		if (result.wasSuccessful() && ListenerstManager.getInstance().getLastTestFailed()){ // add report error to errors
			addFailure(notifier, new AssertionFailedError("Fail report was submitted"));
		}
		test.jsystemTestPostExecution(test);
		writeScenarioResultToTextFile(test);

	}

	/**
	 *	This part of code captures the module name , scenario name and scenario result into a text file 
	 */

	public void writeScenarioResultToTextFile(SystemTest test){

		List<Scenario> lsScenario = new ArrayList<Scenario>();
		String testModuleName = null;
		String scenarioName = null;
		String fileLine = null;
		String bbResult = null;
		boolean bScenarioResult = false;
		BufferedWriter bufferWritter=null;

		lsScenario = ScenarioHelper.getScenarioAncestors(ScenarioHelper.getCurrentRunningTest());
		for(Scenario sc:lsScenario){
			String temp = sc.toString();
			if(temp.contains("_Module")){
				testModuleName = temp;
				break;
			}
		}

		scenarioName = ScenarioHelper.getTestScenario(ScenarioHelper.getCurrentRunningTest()).toString();
		bScenarioResult = test.isPass();
		if( bScenarioResult )
			bbResult = "pass";
		else
			bbResult = "fail";
		fileLine = testModuleName.split("/")[1] +"," + scenarioName.split("/")[1] + "," + bbResult;
		try{
			String filePath= TestCommonResource.getTestResoucresDirPath()+"testresults\\ScenarioResult.txt";
			File file =new File(filePath);
			//if file doesn't exists, then create it
			if(!file.exists()){
				file.createNewFile();
			}
			bufferWritter = new BufferedWriter(new FileWriter(file,true));
			bufferWritter.write(fileLine+"\n");
			bufferWritter.flush();
		}catch(IOException e){
			System.out.println("Exception occured while opening the file writer objects ERROR :: ");
			e.printStackTrace();
		}finally{
			try{
				bufferWritter.close();
			}catch(Exception e){
				System.out.println("Exception occured while closing the file writer objects ERROR :: ");
				e.printStackTrace();
			}
		}
	}

	/**
	 * added in order to get the test instance
	 */
	protected Object createTest() throws Exception {
		test = (SystemTest) getTestClass().getConstructor().newInstance();
		TestResult result = null;
		if (notifier instanceof JsystemRunNotifierCustom){ // run from JRunner
			result = ((JsystemRunNotifierCustom)notifier).getTestResult();
			((SystemTestCaseImpl) test).setTestResult(result);
		}else{ // run from eclipse
			result = new TestResult();
			notifier.addListener(JsystemRunNotifierCustom.getAddapterListener(test, result));
		}
		((SystemTestCaseImpl) test).setTestResult(result);
		return test;
	}

	/**
	 * Set up listener adapter and run the test.
	 */
	@Override
	public void run(RunNotifier notifier) {
		testClass = getTestClass().getJavaClass();

		notifier.addListener(new TestListenerAdapter(ListenerstManager.getInstance()));
		this.notifier = notifier;
		super.run(notifier);
	}

	private void addFailure(RunNotifier notifier, Throwable t){
		notifier.fireTestFailure(new Failure(Description.createTestDescription(testClass, methodName),t));
	}

	/**
	 * This terrible hack uses a custom Annotation object to add information to the otherwise
	 * sealed Description object.
	 */
	@Override
	protected Description methodDescription(Method method) {
		Annotation [] annotations = testAnnotations(method);
		Annotation [] annotations_extend = new Annotation[annotations.length + 1];	
		for (int i=0; i < annotations.length; i++) {
			annotations_extend[i] = annotations[i];
		}
		annotations_extend[annotations.length - 1] = new TestInfoAnnotation(method.getDeclaringClass().getName(), method.getName());
		return Description.createTestDescription(getTestClass().getJavaClass(), testName(method), annotations_extend);



	}

	public JSystemJunit4ClassRunnerCustom(Class<?> klass) throws InitializationError {
		super(klass);
	}
}
