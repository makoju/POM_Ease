package com.ability.ease.auditdoc;

import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.testapi.IAuditDoc;

public class AuditDocSelenium2Impl implements IAuditDoc{

	AuditDocPage auditdocpage = new AuditDocPage();
	
	@Override
	public boolean verifyEsmdDeliveryStatusReportColumnsForHHA(String timeframe, String Value, String agency, String agencyValue, String hic,String patient,String daysduedate,String duedate,String code) throws Exception {
		return auditdocpage.verifyEsmdDeliveryStatusReportColumnsForHHA(timeframe,Value,agency,agencyValue,hic,patient,daysduedate,duedate,code);
	}
	
	@Override
	public boolean verifyADRDocumentUploadFileFormats(String agency, String reviewContractorName, String claimIDorDCN, 
			String caseID,ADRFileFomat adrFileType, ADRFilesSize adrFileSize) throws Exception {
		return auditdocpage.verifyADRDocumentUploadFileFormats(agency, reviewContractorName, claimIDorDCN, caseID, adrFileType, adrFileSize);
	}

	@Override
	public boolean verifyCMSStatusScreenAfterADRSubmission(String expectedCMSStatusTableHeaders,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType,ADRFilesSize adrFileSize) throws Exception {
		return auditdocpage.verifyCMSStatusScreenAfterADRSubmission(expectedCMSStatusTableHeaders,reviewContractorName,claimIDorDCN,caseID,adrFileType,adrFileSize);
	}

	@Override
	public long generateRandomInteger(int length) throws Exception {
		return auditdocpage.generateRandomInteger(length);
	}

	@Override
	public boolean verifyRecordsPresenUnderESMDreport() throws Exception {
		return auditdocpage.verifyRecordsPresenUnderESMDreport();
	}

	@Override
	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception {
		return auditdocpage.verifyREJECTRESENDSubmissionsFunctionality(agency, reviewContractorName, claimIDorDCN, caseID, adrFileType, adrFileSize);
	}
	
	

}
