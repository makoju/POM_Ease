package com.ability.ease.auto.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jsystem.framework.RunnerStatePersistencyManager;
import jsystem.framework.scenario.JTest;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.RunnerTest;
import jsystem.framework.scenario.Scenario;
import jsystem.framework.scenario.ScenarioHelpers;
import jsystem.framework.scenario.ScenariosManager;

import com.ability.ease.auto.common.annotations.SharedParameter;

public class ScenarioHelper {

	// private static Map<String, Object> sharedParameters = new HashMap<String, Object>();

	/**
	 * Returns all scenario parameters as a map.
	 * The map key is the parameter name.
	 * @return
	 */
	public static Map<String, Object> getScenarioParameters(Scenario scenario) {
		Parameter[] parameters = scenario.getParameters();
		Map<String, Object> pMap = new HashMap<String, Object>();
		for (Parameter p : parameters) {
			pMap.put(p.getName(), p.getValue());
		}
		return pMap;
	}

	/**
	 * This method returns the test's scenario it runs under
	 * @return
	 */
	public static Scenario getTestScenario(JTest test) {
		return ScenarioHelpers.getFirstScenarioAncestor(test);
	}

	/**
	 * This method returns the full test's scenarios hierarchy it runs under
	 * @param test
	 * @return
	 */
	public static String getTestScenarioFullHierarchy(JTest test) {
		return ScenarioHelpers.getTestHierarchyInPresentableFormat(test);
	}

	/**
	 * This method returns the current running test
	 */
	public static JTest getCurrentRunningTest() {
		return ScenariosManager.getInstance().getCurrentScenario().getTest(RunnerStatePersistencyManager.getInstance().getActiveTestIndex());
	}
	
	/**
	 * This method returns the current running test's method name
	 */
	public static String getCurrentRunningTestMethodName() {
		return ScenariosManager.getInstance().getCurrentScenario().getTest(RunnerStatePersistencyManager.getInstance().getActiveTestIndex()).getMethodName();
	}
	

	/**
	 * This method retrieves the @GlobalParametersTest annotated test in the specified
	 * class and populates all the test parameters and parameters values into a map
	 * while the key is the global parameter name, which is the class field name.
	 * Note that if the current running test is not a 'globalParameterTest' the method returns null 
	 * to indicate that there is no need to update runProperties.
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getGlobalTestSharedParameters(Class<?> cls) throws Exception {

		String globalMethodName = ReflectUtil.getClassGlobalParametersTest(cls);
		if (globalMethodName == null) {
			return null;
		}

		Map<String, Object> sharedParameters = new HashMap<String, Object>();

		// get current running test
		JTest currentRunningTest = getCurrentRunningTest();

		// get the scenario of the current running test
		Scenario scenario = getTestScenario(currentRunningTest);

		// get all tests in the current scenario
		Vector<JTest> scenarioTests = scenario.getRootTests(); // .getTests();

		// loop over all scenario tests until the globalTestData test is found
		for (JTest test : scenarioTests) {
			if (test instanceof RunnerTest) {
				RunnerTest rt = (RunnerTest) test;
				if (!rt.isDisable() && rt.getMethodName().equals(globalMethodName)) { // is this test enable and global?
					Parameter[] runnerTestParameters = rt.getParameters();
					Map<String, Parameter> paramsMap = getArrayAsMap("getName", runnerTestParameters);
					Field[] fields = cls.getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						// field name in JSystem starts with upper case
						String fieldName = fields[i].getName().substring(0, 1).toUpperCase() + "" + fields[i].getName().substring(1);
						if (fields[i].getAnnotation(SharedParameter.class) != null && paramsMap.get(fieldName) != null) {
							sharedParameters.put(fields[i].getName(), paramsMap.get(fieldName).getValue());
						}
					}
					break; // the globaTest was found for the current scenario
				}
			}
		}
		return (sharedParameters.size() == 0) ? null : sharedParameters;
	}

	public static <T> Map<String, T> getArrayAsMap(String method, T[] clsArray) throws Exception {
		Map<String, T> mapa = new HashMap<String, T>();
		for (T cls : clsArray) {
			Method m = cls.getClass().getDeclaredMethod(method);
			m.setAccessible(true);
			Object obj = m.invoke(cls);
			mapa.put((String) obj, cls);
		}
		return mapa;
	}

	/**
	 * Returns a list of all ancestors scenarios of a given test.
	 * If test is a scenario and doesn't have a parent returns null.
	 */
	public static List<Scenario> getScenarioAncestors(JTest test){
		List<Scenario> parents = null; 
		JTest parent = test.getParent();
		if (parent !=null) {
			parents = new ArrayList<Scenario>();
		}
		while (parent != null){
			if (parent instanceof Scenario && !ScenarioHelpers.isScenarioAsTestAndNotRoot(parent)) {
				parents.add((Scenario) parent);
			}
			parent = parent.getParent();
		}
		return parents;
	}
}
