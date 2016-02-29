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
	abstract public boolean addToHMODuplicatePatient( String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex)throws Exception;
	abstract public boolean extendHMO(String sHIC) throws Exception;
	abstract public boolean addtoHMOFromPatientInfo(String sHIC) throws Exception;
	abstract public boolean addDuplicatePatientToHMOFromPatientInfo(String sHIC) throws Exception;
	abstract public boolean trashHMOPatient(String sHIC) throws Exception;
	abstract public boolean AdvanceSearchFromHMO(String sHIC) throws Exception;
	abstract public boolean acknowledgeHMO(String sHIC) throws Exception;
	abstract public boolean printFromHMO() throws Exception;
	
	


	


}
