package com.ability.ease.testapi;

import java.util.Map;

public interface IAdministration {
	
	abstract public boolean addUser(Map<String, String> mapAttrVal)throws Exception;
	
	abstract public boolean editUser(Map<String, String> mapAttrVal)throws Exception;

}
