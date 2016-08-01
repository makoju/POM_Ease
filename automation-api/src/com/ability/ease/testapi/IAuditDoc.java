package com.ability.ease.testapi;


import java.util.List;

import com.ability.ease.auto.enums.portal.selenium.ESMDSubmissionType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;

public interface IAuditDoc {

	public boolean verifyADRESMDStatusReportColumns(String HIC,String patientName,String dueDate,
			String thirteeDayDueDate,String code,String expectedADRStatusReportTableHeaders,String agency) throws Exception;

	public boolean verifyADRDocumentUploadFileFormats(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;

	public boolean verifyCMSStatusScreenAfterADRSubmission(String expectedCMSStatusTableHeaders,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;

	public boolean verifyRecordsPresenUnderESMDreport(String sAgency)throws Exception; 

	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception; 

	public boolean isDocSplitted(String claimIDorDCN)throws Exception; 

	public long generateRandomInteger(int length)throws Exception;

	public boolean changeTimeFrame(ESMDSubmissionType esMDSubType)throws Exception;

	public boolean changeAgency(ESMDSubmissionType esMDSubType,String agency)throws Exception;

	public boolean selectFilesToUpload(ESMDSubmissionType esMDSubmissionType, ADRFileFomat fileType, ADRFilesSize fileSize,
			String reviewContractorName)throws Exception; 

	public boolean fillDocumentUploadScreenValues(ESMDSubmissionType esMDSubmissionType, String reviewContractorName, String claimIDorDCN, 
			String caseID)throws Exception;

	public boolean navigateToESMDPage(ESMDSubmissionType esMDSubmissionType)throws Exception;

	public boolean selectClaimRecordToUploadDocuments(ESMDSubmissionType esMDSubmissionType, String HIC)throws Exception;
	
	public boolean clickCMSLink(ESMDSubmissionType esMDSubType)throws Exception;

}
