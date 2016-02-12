package com.ability.ease.auto.enums.tests;

public enum SelectTimeframe {
	
	Overnight("Overnight"), Weekly("Weekly"), All ("All (up to 18 mos ago)"), Date("date");

	String value;
	private SelectTimeframe(String s) {
		this.value=s;
	}

}
