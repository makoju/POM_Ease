package com.ability.ease.auditdoc;


import java.util.List;

import com.ability.ease.auto.enums.portal.selenium.ESMDSubmissionType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.testapi.IAuditDoc;

public class AuditDocSelenium2Impl implements IAuditDoc{

	AuditDocPage auditdocpage = new AuditDocPage();
	AuditDocPageV2 audit = new AuditDocPageV2();

	@Override
	public boolean verifyADRESMDStatusReportColumns(String HIC,String patientName,String dueDate,String thirteeDayDueDate,
			String code,String expectedADRStatusReportTableHeaders,String agency) throws Exception {
		return audit.verifyADRESMDStatusReportColumns(HIC,patientName,dueDate,thirteeDayDueDate,code,expectedADRStatusReportTableHeaders,agency);
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
	public boolean changeTimeFrame(ESMDSubmissionType esMDSubType) throws Exception {
		return audit.changeTimeFrame(esMDSubType);
	}

	@Override
	public boolean changeAgency(ESMDSubmissionType esMDSubType,String agency) throws Exception {
		return audit.changeAgency(esMDSubType,agency);
	}

	@Override
	public boolean selectFilesToUpload(ESMDSubmissionType esMDSubmissionType, ADRFileFomat fileType, 
			ADRFilesSize fileSize,String reviewContractorName)throws Exception {
		return audit.selectFilesToUpload(esMDSubmissionType,fileType, fileSize,reviewContractorName);
	}

	@Override
	public boolean fillDocumentUploadScreenValues(ESMDSubmissionType esMDSubmissionType,String reviewContractorName, 
			String claimIDorDCN, String caseID) throws Exception {
		return audit.fillDocumentUploadScreenValues(esMDSubmissionType, reviewContractorName, claimIDorDCN, caseID);
	}

	@Override
	public boolean navigateToESMDPage(ESMDSubmissionType esMDSubmissionType) throws Exception {
		return audit.navigateToESMDPage(esMDSubmissionType);
	}

	@Override
	public boolean selectClaimRecordToUploadDocuments(ESMDSubmissionType esMDSubmissionType,String HIC) throws Exception {
		return audit.selectClaimRecordToUploadDocuments(esMDSubmissionType,HIC);
	}

	@Override
	public boolean clickCMSLink(ESMDSubmissionType esMDSubType) throws Exception {
		return audit.clickCMSLink(esMDSubType);
	}
}
