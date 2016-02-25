package com.ability.ease.testapi;

import java.util.Date;
import java.util.Map;

import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;

public interface IHMO {
	/*
	 * ADD PATIENT TO HMO ADVANTAGE MOVE CATCHER 
	 */

	abstract public boolean addToHMO( String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception;
	abstract public boolean addToHMODuplicate( String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception;
	abstract public boolean extendHMO() throws Exception;


	


}
