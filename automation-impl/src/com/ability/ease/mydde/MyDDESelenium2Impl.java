package com.ability.ease.mydde;

import java.util.Map;

import com.ability.ease.testapi.IMyDDE;

public class MyDDESelenium2Impl implements IMyDDE{
	
	private MyDDEPage mydde = new MyDDEPage();

	@Override
	public boolean verifyPageViewAndOptions() throws Exception {
		return mydde.verifyPageViewAndOptions();
	}

	@Override
	public boolean verifyOptionsUnderReports(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOptionsUnderReports(mapAttrValues);
	}

	@Override
	public boolean verifyOptionsUnderTimeframe(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOptionsUnderTimeframe(mapAttrValues);
	}

	@Override
	public boolean verifyOptionsUnderAgency(Map<String, String> mapAttrValues) throws Exception {
		return mydde.verifyOptionsUnderAgency(mapAttrValues);
	}

	@Override
	public boolean verifySaveProfileOption() throws Exception {
		return mydde.verifySaveProfileOption();
	}

}
