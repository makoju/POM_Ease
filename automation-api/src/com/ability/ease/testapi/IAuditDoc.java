package com.ability.ease.testapi;


import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;

public interface IAuditDoc {
	
	public boolean verifyADRESMDStatusReportColumns(String HIC,String patientName,String dueDate,
			String thirteeDayDueDate,String code,String expectedADRStatusReportTableHeaders) throws Exception;
	
	public boolean verifyADRDocumentUploadFileFormats(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;
	
	public boolean verifyCMSStatusScreenAfterADRSubmission(String expectedCMSStatusTableHeaders,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;
	
	public boolean verifyRecordsPresenUnderESMDreport(String sAgency)throws Exception; 

	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception; 
	
	public boolean isDocSplitted(String claimIDorDCN)throws Exception; 

	public long generateRandomInteger(int length)throws Exception;
	
	public boolean changeTimeFrame()throws Exception;
	
	public boolean changeAgency(String agency)throws Exception;
	
}
