package com.ability.ease.auto.enums.portal.selenium;

public enum ESMDSubmissionType {
	
	ADR("ADR"),
	RAC("RAC"),
	APPEAL("Appeal");

	private String value;

	private ESMDSubmissionType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
