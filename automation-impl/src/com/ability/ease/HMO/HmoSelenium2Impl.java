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
	public boolean addToHMODuplicate(String  sAgency,String sHIC,String sLastName, String sFirstName,String sDob,String sSex) throws Exception {
		return hp.addToHMODuplicate(sAgency, sHIC, sLastName, sFirstName, sDob, sSex);
	}

	@Override
	public boolean extendHMO() throws Exception {
		// TODO Auto-generated method stub
		return hp.extendHMO();
	}

	







	}








	



