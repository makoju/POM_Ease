package com.ability.ease.auto.enums.common;

public enum DateFormat {
	EUROPE("dd-MM-yyyy"),
	LINUX("MM/dd/yyyy"),
	US("MM-dd-yyyy");
	
	private String value;

	private DateFormat(String value) {
		this.value = value;
	}
	public String getValue() {
        return value;
    }

}
