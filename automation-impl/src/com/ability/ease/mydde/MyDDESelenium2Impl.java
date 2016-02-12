package com.ability.ease.mydde;

import com.ability.ease.auto.enums.tests.Status;
import com.ability.ease.testapi.IMyDDE;

public class MyDDESelenium2Impl implements IMyDDE{
	
	private MyDDEPage mydde = new MyDDEPage();

	@Override
	public boolean verifyPageViewAndOptions() throws Exception {
		return mydde.verifyPageViewAndOptions();
	}

	@Override
	public boolean verifyOptionsUnderReportsForHHA() throws Exception {
		return mydde.verifyOptionsUnderReportsForHHA();
	}

	@Override
	public boolean verifyOptionsUnderTimeframe() throws Exception {
		return mydde.verifyOptionsUnderTimeframe();
	}

	@Override
	public boolean verifyOptionsUnderAgency(String agency) throws Exception {
		return mydde.verifyOptionsUnderAgency(agency);
	}

	@Override
	public boolean verifySaveProfileOption() throws Exception {
		return mydde.verifySaveProfileOption();
	}

	@Override
	public boolean verifyOptionsUnderReportsForNonHHA() throws Exception {
		return mydde.verifyOptionsUnderReportsForNonHHA();
	}

	@Override
	public boolean verifyTimeFrameOptionsUnderAdvanced() throws Exception {
		return mydde.verifyTimeFrameOptionsUnderAdvanced();
	}

	@Override
	public boolean verifyAdvanceSearchForMAXFields(String hic, String monthsAgo,Status status,String location) throws Exception {
		return mydde.verifyAdvanceSearchForMAXFields(hic,monthsAgo,status,location);
	}

	@Override
	public boolean verifyAdvancedSearchForStatusLocation() throws Exception {
		return mydde.verifyAdvancedSearchForStatusLocation();
	}

}
