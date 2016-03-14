package com.ability.ease.auto.enums.tests;

public enum Status {

	DENIED("Denied"), REJECTED("Rejected"), RTP("RTP"), SUSPENSE("Suspense"), PAID("Paid"), NOT_PAID("Not_Paid");
	
	String status;

	private Status(String status) {
		this.status = status;
	}
	
	public String getStatus(){
		return status;
	}
	
	
}
