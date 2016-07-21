package com.ability.ease.testapi;


import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;

public interface IAuditDoc {
	
	public boolean verifyEsmdDeliveryStatusReportColumns(String timeframe, String Value, String agency, String agencyValue,String hic,String patient,
			String daysduedate,String duedate,String code) throws Exception;
	
	public boolean verifyADRDocumentUploadFileFormats(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;
	
	public boolean verifyCMSStatusScreenAfterADRSubmission(String expectedCMSStatusTableHeaders,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;
	
	public boolean verifyRecordsPresenUnderESMDreport(String sAgency)throws Exception; 

	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception; 
	
	public boolean isDocSplitted(String claimIDorDCN)throws Exception; 

	public long generateRandomInteger(int length)throws Exception;
	
}
