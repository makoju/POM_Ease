package com.ability.ease.auto.junit;

public class IgnoreTest {

	static class IgnoreTestCaseException extends Error {      
		private static final long serialVersionUID = 1L;
		
		IgnoreTestCaseException() {             
			super("If you see this, then you have forgot to run the test-case "    
					+ "with the IgnoreableRunner JUnit runner.");          
		}      }      

	public static void ignoreIf(final boolean shouldIgnore) { 
		if (shouldIgnore) {   
			throw new IgnoreTestCaseException();
		}
	}       

}
