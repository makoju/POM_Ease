package com.ability.ease.auto.common;

import com.ability.ease.testapi.ICommon;

public class CommonSelenium2Impl implements ICommon{
	
	CommonPage cp = new CommonPage();
	
	@Override
	public boolean stopStartEaseServer(String status) throws Exception {
		return false;
	//	return cp.stopStartEaseServer(status);
	}

}
