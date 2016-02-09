package com.ability.ease.auto.enums.common;

public class CommonEnums {

	public static enum OperatingSystem{
		Windows, Linux
	}

	public static enum TpRetryIntervalUnit{
		mins, hours, days
	}

	public enum ConditionOperator {
		Equals("EQUALS"),
		Not_equals("NOT_EQUALS"),
		Start_with("STARTS_WITH"),
		//Not_start_with("Not start with"),
		Contains("CONTAINS"),
		Not_contains("Not contains"),
		Ends_with("ENDS_WITH");
		//Not_ends_with("Not ends with"),
		//TO DO: Add more operators

		private String operator = null;

		private ConditionOperator(String operator){
			this.operator = operator;
		}

		public String toString()
		{
			return operator;
		}
	};
}

