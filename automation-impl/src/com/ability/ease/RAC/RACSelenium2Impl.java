package com.ability.ease.RAC;

import java.util.Date;
import java.util.Map;

import com.ability.ease.testapi.IRAC;

public class RACSelenium2Impl implements IRAC {
	RACPage racPage = new RACPage();

	@Override
	public boolean addtoRAC(Map<String, String> mapAttrVal) throws Exception {
		return racPage.addtoRAC(mapAttrVal);
	}

	@Override
	public boolean changeAgnecy(String sAgency) throws Exception {
		// TODO Auto-generated method stub
		return racPage.changeAgency(sAgency);
	}

	@Override
	public boolean VerifyClaimInRACSubmissioninDB(String sHIC, String sDCN, String sPatientCtrlNo, String sStartDate,
			String sThroughDate, String sRefIdParam, String sLetterDate, String sNotes) throws Exception {
		return racPage.VerifyClaimInRACSubmissioninDB(sHIC, sDCN, sPatientCtrlNo, sStartDate, sThroughDate, sRefIdParam,
				sLetterDate, sNotes);
	}

	@Override
	public boolean VerifyClaimInRACSubmissioninReport(String sAgency) throws Exception {
		// TODO Auto-generated method stub
		return racPage.VerifyClaimInRACSubmissioninReport(sAgency);
	}

}
