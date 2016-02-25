package com.ability.ease.auditdoc;

import java.util.Map;

import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.testapi.IAuditDoc;

public class AuditDocSelenium2Impl implements IAuditDoc{

	AuditDocPage auditdocpage = new AuditDocPage();
	
	@Override
	public boolean verifyEsmdDeliveryStatusReportColumns(
			Map<String, String> mapAttrValues) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean verifyADRDocumentUploadFileFormats(String agency, String reviewContractorName, String claimIDorDCN, 
			String caseID,ADRFileFomat adrFileType, ADRFilesSize adrFileSize) throws Exception {
		return auditdocpage.verifyADRDocumentUploadFileFormats(agency, reviewContractorName, claimIDorDCN, caseID, adrFileType, adrFileSize);
	}

}
