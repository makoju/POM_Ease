package com.ability.ease.auto.enums.tests;

public enum NewUB04 {
	
	RelatedClaim("Related Claim"), UnrelatedClaim("Unrelated Claim");

	private String value;

	private NewUB04(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
