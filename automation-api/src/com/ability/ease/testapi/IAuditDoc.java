package com.ability.ease.testapi;


import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;

public interface IAuditDoc {
	
	boolean verifyEsmdDeliveryStatusReportColumnsForHHA(String timeframe, String Value, String agency, String agencyValue,String hic,String patient,
			String daysduedate,String duedate,String code) throws Exception;
	
	/*
	 * method to verify whether ADR Response Document Upload  accepting the following file formats
	 */

	public boolean verifyADRDocumentUploadFileFormats(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;
	
	/*
	 * method to verify CMS status screen 
	 */

	public boolean verifyCMSStatusScreenAfterADRSubmission(String expectedCMSStatusTableHeaders,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception;
	
	
	/*
	 * method to verify the esmd report recods count
	 */
	public boolean verifyRecordsPresenUnderESMDreport()throws Exception; 

	/*
	 * method to verify REJECT and RESEND Submission functionality
	 */
	public boolean verifyREJECTRESENDSubmissionsFunctionality(String agency,String reviewContractorName, String claimIDorDCN, 
			String caseID, ADRFileFomat adrFileType, ADRFilesSize adrFileSize)throws Exception; 

	
	/*
	 * method to verify document split submission
	 */
	public boolean isDocSplitted(String claimIDorDCN)throws Exception; 

	
	/*
	 * Use this BB to generate a random number which is of length your wish
	 */
	public long generateRandomInteger(int length)throws Exception;
	
}
