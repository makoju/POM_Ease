package com.ability.ease.admin;

import java.util.Map;

import com.ability.ease.testapi.IAdministration;

public class AdminSelenium2Impl implements IAdministration{

	@Override
	public boolean addUser(Map<String, String> mapAttrVal) throws Exception {
	
		AdminPage adminpage = new AdminPage();
		return adminpage.addUser(mapAttrVal);
	}

	@Override
	public boolean editUser(Map<String, String> mapAttrVal) throws Exception {
		AdminPage adminpage = new AdminPage();
		return adminpage.editUser(mapAttrVal);
	}

}
