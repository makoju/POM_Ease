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
}
