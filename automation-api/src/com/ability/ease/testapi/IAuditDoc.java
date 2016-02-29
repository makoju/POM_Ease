package com.ability.ease.testapi;

import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;

public interface IAuditDoc {
	
	public boolean verifyEsmdDeliveryStatusReportColumns(Map<String, String> mapAttrValues) throws Exception;
	
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
	 * Use this BB to generate a random number which is of length your wish
	 */
	public long generateRandomInteger(int length)throws Exception;
	
}
