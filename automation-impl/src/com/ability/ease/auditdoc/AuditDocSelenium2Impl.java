package com.ability.ease.auditdoc;


import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.testapi.IAuditDoc;

public class AuditDocSelenium2Impl implements IAuditDoc{

	AuditDocPage auditdocpage = new AuditDocPage();
	AuditDocPageV2 audit = new AuditDocPageV2();

	@Override
	public boolean verifyADRESMDStatusReportColumns(String HIC,String patientName,String dueDate,String thirteeDayDueDate,
			String code,String expectedADRStatusReportTableHeaders) throws Exception {
		return audit.verifyADRESMDStatusReportColumns(HIC,patientName,dueDate,thirteeDayDueDate,code,expectedADRStatusReportTableHeaders);
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
	public boolean verifyRecordsPresenUnderESMDreport(String sAgency) throws Exception {
		return auditdocpage.verifyRecordsPresenUnderESMDreport(sAgency);
	}

	@Override
	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception {
		return auditdocpage.verifyREJECTRESENDSubmissionsFunctionality(agency, reviewContractorName, claimIDorDCN, caseID, adrFileType, adrFileSize);
	}

	@Override
	public boolean isDocSplitted(String claimIDorDCN) throws Exception {
		return auditdocpage.isDocSplitted(claimIDorDCN);
	}

	@Override
	public boolean changeTimeFrame() throws Exception {
		return audit.changeTimeFrame();
	}

	@Override
	public boolean changeAgency(String agency) throws Exception {
		return audit.changeAgency(agency);
	}

}
