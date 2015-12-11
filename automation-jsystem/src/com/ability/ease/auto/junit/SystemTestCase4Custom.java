package com.ability.ease.auto.junit;



import jsystem.framework.ShutdownManager;
import jsystem.framework.analyzer.AnalyzerImpl;
import junit.framework.SystemTest;
import junit.framework.SystemTestCaseImpl;
import junit.framework.TestResult;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.ability.ease.auto.junit.JSystemJunit4ClassRunnerCustom;

@RunWith(JSystemJunit4ClassRunnerCustom.class)
public class SystemTestCase4Custom extends SystemTestCaseImpl implements SystemTest {
	
		
		public static AnalyzerImpl analyzer = null;
		private String name;
		static {
			ShutdownManager.init();
			analyzer = new AnalyzerImpl();
			analyzer.setTestAgainstObject("");
		}

		@Before
		public void defaultBefore() throws Throwable {
			jsystemTestPreExecution(this);
		}
		
		@After
		public void defaultAfter() throws Throwable {
			
		}
		
		public void run(TestResult result) {
			// not implemented for JUnit 4 test cases
		}

		//@Override
		public String getName() {
			return name;
		}
		
		//@Override
		public void setName(String name) {
			this.name = name;
		}

		//@Override
		public int countTestCases() {
			return 1;
		}

		//@Override
		public String getClassName() {
			return getClass().getName();
		}

		//@Override
		public String getMethodName() {
			return getName();
		}
	}
