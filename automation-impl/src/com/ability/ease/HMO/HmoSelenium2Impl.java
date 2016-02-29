package com.ability.ease.HMO;

import java.util.Date;
import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.testapi.IHMO;

public class HmoSelenium2Impl implements IHMO{
	HmoPage hp = new HmoPage();
	@Override
	public boolean addToHMO(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex) throws Exception {	
			return hp.addToHMO(sAgency, sHIC, sLastName, sFirstName, sDob, sSex);
	}

	@Override
	public boolean addToHMODuplicatePatient(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex) throws Exception {
		return hp.addToHMODuplicatePatient(sAgency, sHIC, sLastName, sFirstName, sDob, sSex);
	}


	@Override
	public boolean extendHMO(String sHIC) throws Exception {
		// TODO Auto-generated method stub
		return hp.extendHMO(sHIC);
	}

	@Override
	public boolean addtoHMOFromPatientInfo(String sHIC) throws Exception {
		// TODO Auto-generated method stub
		return hp.addtoHMOFromPatientInfo(sHIC);
	}

	@Override
	public boolean addDuplicatePatientToHMOFromPatientInfo(String sHIC)
			throws Exception {
		// TODO Auto-generated method stub
		return hp.addDuplicatePatientToHMOFromPatientInfo(sHIC);
	}

	@Override
	public boolean trashHMOPatient(String sHIC) throws Exception {
		// TODO Auto-generated method stub
		return hp.trashHMOPatient(sHIC);
	}

	@Override
	public boolean AdvanceSearchFromHMO(String sHIC) throws Exception {
		// TODO Auto-generated method stub
		return hp.AdvanceSearchFromHMO(sHIC);
	}

	@Override
	public boolean acknowledgeHMO(String sHIC) throws Exception {
		// TODO Auto-generated method stub
		return hp.acknowledgeHMO(sHIC);
	}

	@Override
	public boolean printFromHMO() throws Exception {
		// TODO Auto-generated method stub
		return hp.printHMO();
	}

	







	}








	



