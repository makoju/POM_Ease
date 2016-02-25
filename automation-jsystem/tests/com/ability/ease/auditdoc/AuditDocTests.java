package com.ability.ease.auditdoc;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;

import org.junit.Before;
import org.junit.Test;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IAuditDoc;


public class AuditDocTests extends BaseTest{

	private IAuditDoc auditdoc;
	private ADRFileFomat adrFileFormat;
	private ADRFilesSize adrFileSize;
	private String reviewContractorName;
	private String claimIDDCN;
	private String caseID;
	private String agency;
	
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		auditdoc = (IAuditDoc)context.getBean("auditdoc");
	}

	/*
	 * Implemented using XML approach 
	 * @param - AttributeNameValueDialogProvider :: to provide a dialog provider with parsed attributes from xml
	 */
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Response Document Upload Accepting PDF,TIFF / TIF Formats", 
	paramsInclude = { "reviewContractorName, claimIDDCN, caseID, adrFileFormat, adrFileSize, testType" })
	public void verifyADRDocumentUploadFileFormats()throws Exception{

		if(!auditdoc.verifyADRDocumentUploadFileFormats(agency, reviewContractorName, claimIDDCN, caseID, adrFileFormat, adrFileSize)){
			report.report("Failed to upload ADR response document of type " + adrFileFormat + " with size " + adrFileSize , Reporter.FAIL);
		}else{
			report.report("Successfully uploaded ADR response document of type " + adrFileFormat + " with size " + adrFileSize, Reporter.ReportAttribute.BOLD);
		}
	}


	/*###
	# Getters and Setters
	###*/

	public AttributeNameValueDialogProvider[] getAttributeNameValueDialogProvider() {
		return AttributeNameValueDialogProvider;
	}


	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	public void setAttributeNameValueDialogProvider(
			AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}

	public ADRFileFomat getAdrFileFormat() {
		return adrFileFormat;
	}

	@ParameterProperties(description = "Select ADR file format from dropdown to upload")
	public void setAdrFileFormat(ADRFileFomat adrFileFormat) {
		this.adrFileFormat = adrFileFormat;
	}

	public ADRFilesSize getAdrFileSize() {
		return adrFileSize;
	}

	@ParameterProperties(description = "Select ADR file size dropdown to upload")
	public void setAdrFileSize(ADRFilesSize adrFileSize) {
		this.adrFileSize = adrFileSize;
	}

	public String getReviewContractorName() {
		return reviewContractorName;
	}

	@ParameterProperties(description = "Provide review contractor name to select during ADR doc upload")
	public void setReviewContractorName(String reviewContractorName) {
		this.reviewContractorName = reviewContractorName;
	}

	public String getClaimIDDCN() {
		return claimIDDCN;
	}

	@ParameterProperties(description = "Provide claim ID or DCN to type must be 13,14,15 digit number or 17-23 char value")
	public void setClaimIDDCN(String claimIDDCN) {
		this.claimIDDCN = claimIDDCN;
	}

	public String getCaseID() {
		return caseID;
	}

	@ParameterProperties(description = "Provide case id to type during ADR doc upload")
	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public String getAgency() {
		return agency;
	}

	@ParameterProperties(description = "Provide agency name to select {HHA1,HHA2,SNF,HOSPICE etc.,")
	public void setAgency(String agency) {
		this.agency = agency;
	}
	
	
	
}
