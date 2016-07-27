package com.ability.ease.testapi;

import java.util.Date;
import java.util.Map;

public interface IRAC {
	public boolean addtoRAC(Map<String, String> mapAttrVal) throws Exception;
	public boolean VerifyClaimInRACSubmissioninDB(String sHIC, String sDCN, String sPatientCtrlNo, String sStartDate,
			String sThroughDate, String sRefIdParam, String sLetterDate, String sNotes) throws Exception;
	public boolean changeAgnecy(String sAgency) throws Exception;
	public boolean VerifyClaimInRACSubmissioninReport(String sAgency) throws Exception;

}
