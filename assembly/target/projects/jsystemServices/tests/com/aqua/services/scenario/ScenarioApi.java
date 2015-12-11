/*
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package com.aqua.services.scenario;

import java.io.File;

import jsystem.framework.JSystemProperties;
import jsystem.framework.scenario.RunnerTest;
import jsystem.framework.scenario.Scenario;
import jsystem.framework.scenario.ScenariosManager;

import org.junit.Test;

/**
 * This class demonstrates how to work with Scenario API
 * to programatically create/query/modify scenarios
 *  
 * @author goland
 */
public class ScenarioApi {

	
	/**
	 * Creates a scenario adds to the scenario two instances of this test. 
	 */
	@Test public void createAScenarioWithTest() throws Exception {
		//getting project's classes path to create the project under it.
		String classes = JSystemProperties.getCurrentTestsPath();
		Scenario s = new Scenario(new File(classes),"scenarios/myScenario.xml");
		
		//adding tests to scenario. Note if you want to add the same test twice, create two instances of the test
		RunnerTest test = new RunnerTest(ScenarioApi.class.getName(),"createAScenarioWithTest");
		s.addTest(test);
		test = new RunnerTest(ScenarioApi.class.getName(),"createAScenarioWithTest");
		s.addTest(test);
		// saving the scenario
		s.update(true);
		
		//verifying that the scenario was created.
		ScenariosManager.getInstance().isScenarioExists("scenarios/myScenario");
		
	}
}
