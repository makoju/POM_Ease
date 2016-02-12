package com.ability.ease.testapi;

import com.ability.ease.auto.enums.tests.Status;


public interface IMyDDE {
	
	boolean verifyPageViewAndOptions() throws Exception;
	boolean verifyOptionsUnderReportsForHHA() throws Exception;
	boolean verifyOptionsUnderTimeframe() throws Exception;
	boolean verifyOptionsUnderAgency(String agency) throws Exception;
	boolean verifySaveProfileOption() throws Exception;
	boolean verifyOptionsUnderReportsForNonHHA() throws Exception;
	boolean verifyTimeFrameOptionsUnderAdvanced() throws Exception;
	boolean verifyAdvanceSearchForMAXFields(String hic, String monthsAgo, Status status, String location) throws Exception;
	boolean verifyAdvancedSearchForStatusLocation() throws Exception;
	
}
