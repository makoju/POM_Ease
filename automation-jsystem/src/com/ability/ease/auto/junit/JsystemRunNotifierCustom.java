package com.ability.ease.auto.junit;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

import com.ability.ease.auto.junit.IgnoreTest.IgnoreTestCaseException;

class JsystemRunNotifierCustom extends RunNotifier{
	

		private TestResult testResult;

		private Class<?> testClass;

		private String methodName;

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public Class<?> getTestClass() {
			return testClass;
		}

		public void setTestClass(Class<?> testClass) {
			this.testClass = testClass;
		}

		public TestResult getTestResult() {
			return testResult;
		}

		public void setTestResult(TestResult testResult) {
			this.testResult = testResult;
		}
		
		public static JUnit4ListenerAdapter getAddapterListener(Test test, TestResult result){
			return new JUnit4ListenerAdapter(test,result);
		}
		
		public void addAdapterListener(Test test, TestResult result){
			setTestResult(result);
			addListener(new JUnit4ListenerAdapter(test,result));
		}
}
	
class JUnit4ListenerAdapter extends RunListener {

		private TestResult result;
		private Test test;

		public JUnit4ListenerAdapter(Test test, TestResult result) {
			this.result = result;
			this.test = test;
		}
		
		
		@Override
		public void testFailure(Failure failure) throws Exception {
			if (failure.getException() instanceof IgnoreTestCaseException){
				//super.testIgnored(failure.getDescription());
			} else {
				if (AssertionError.class.isInstance(failure.getException())) {
					// hack to convert an AssertionError to AssertionFailedError which
					// so that a failure (and not an error) can be reported 
					AssertionFailedError assertionFailedError = 
						new AssertionFailedError(failure.getException().getMessage());
					result.addFailure(test, assertionFailedError);
				} else {
					result.addError(test, failure.getException());
				}
			}
		}

		@Override
		public void testFinished(Description description) throws Exception {
			try {
				result.endTest(test);
			} catch (Exception ex) {
				//ignore
			}
		}

		@Override
		public void testStarted(Description description) throws Exception {
			result.startTest(test);
		}
	}
