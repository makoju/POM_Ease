package com.ability.ease.auditdoc;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;

import org.junit.Before;
import org.junit.Test;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFilesSize;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRSubType;
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
	private String expectedCMSStatusTableHeaders;
	private int length;
	private String timeframe,Value,agencyValue,hic,patient,daysduedate,duedate,code;
	private ADRSubType adrSubmissionType;

	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		auditdoc = (IAuditDoc)context.getBean("auditdoc");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "verifyEsmdDeliveryStatusReportColumnsForHHA", paramsInclude = {"timeframe,Value,agency,agencyValue,hic,patient,daysduedate,duedate,code,testType"})
	public void verifyEsmdDeliveryStatusReportColumnsForHHA() throws Exception{	

		if(auditdoc.verifyEsmdDeliveryStatusReportColumnsForHHA(timeframe,Value,agency,agencyValue,hic,patient,daysduedate,duedate,code)){
			report.report("Expected Columns and data presented under eSMD Report", Reporter.PASS);
		}else{
			report.report("Expected Columns and data not presented under eSMD Report", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Response Document Upload Accepting PDF,TIFF / TIF Formats", 
	paramsInclude = { "agency, reviewContractorName, length, caseID, adrFileFormat, adrFileSize, testType" })
	public void verifyADRDocumentUploadFileFormats()throws Exception{
		claimIDDCN = String.valueOf(auditdoc.generateRandomInteger(length));
		keepAsGloablParameter("reviewContractorName", reviewContractorName);
		keepAsGloablParameter("claimIDDCN", claimIDDCN);
		keepAsGloablParameter("caseID", caseID);
		keepAsGloablParameter("adrFileFormat", adrFileFormat.toString());
		keepAsGloablParameter("adrFileSize", adrFileSize.toString());

		if(!auditdoc.verifyADRDocumentUploadFileFormats(agency, reviewContractorName, claimIDDCN, caseID, adrFileFormat, adrFileSize)){
			report.report("Failed to upload ADR response document of type " + adrFileFormat + " with size " + adrFileSize , Reporter.FAIL);
		}else{
			report.report("Successfully uploaded ADR response document of type " + adrFileFormat + " with size " + adrFileSize, Reporter.ReportAttribute.BOLD);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify CMS Status Screen Details", 
	paramsInclude = { "expectedCMSStatusTableHeaders, reviewContractorName, claimIDDCN, caseID, adrFileFormat, adrFileSize, testType" })
	public void verifyCMSStatusScreenAfterADRSubmission()throws Exception{

		if(!auditdoc.verifyCMSStatusScreenAfterADRSubmission(expectedCMSStatusTableHeaders, reviewContractorName, claimIDDCN, caseID, adrFileFormat, adrFileSize)){
			report.report("Failed to validate CMS screen details after submitting the ADR" , Reporter.FAIL);
		}else{
			report.report("Successfully validated CMS screen details", Reporter.ReportAttribute.BOLD);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Records Present Under eSMDreport", paramsInclude = { "testType" })
	public void verifyRecordsPresenUnderESMDreport()throws Exception{

		if(!auditdoc.verifyRecordsPresenUnderESMDreport()){
			report.report("Records present under ADR report and records present under eSMD status report are not equal" , Reporter.FAIL);
		}else{
			report.report("Records present under ADR report and records present under eSMD status report are equal", Reporter.ReportAttribute.BOLD);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify REJECT / RESEND submissions functionality", 
	paramsInclude = { "agency, reviewContractorName, length, caseID, adrFileFormat, adrFileSize, adrSubmissionType, testType" })
	public void verifyREJECTRESENDSubmissionsFunctionality()throws Exception{
		
		claimIDDCN = String.valueOf(auditdoc.generateRandomInteger(length))+adrSubmissionType.toString();
		report.report("Claim ID / DCN From Resend/Reject method" + claimIDDCN);
		if(!auditdoc.verifyREJECTRESENDSubmissionsFunctionality(agency, reviewContractorName, claimIDDCN, caseID, adrFileFormat, adrFileSize)){
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

	@ParameterProperties(description = "Provide agency name to select {HHA1,HHA2,SNF,HOSPICE etc.,}")
	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getExpectedCMSStatusTableHeaders() {
		return expectedCMSStatusTableHeaders;
	}

	@ParameterProperties(description = "Provide table headers expected on CMS status screen by separating with ,")
	public void setExpectedCMSStatusTableHeaders(
			String expectedCMSStatusTableHeaders) {
		this.expectedCMSStatusTableHeaders = expectedCMSStatusTableHeaders;
	}

	public int getLength() {
		return length;
	}

	@ParameterProperties(description = "Provide length of claimIDDCN you want to generate it should be 13,14,15")
	public void setLength(int length) {
		this.length = length;
	}

	public String getTimeframe() {
		return timeframe;
	}

	@ParameterProperties(description = "Test Data:Timeframe")
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}

	public String getValue() {
		return Value;
	}
	@ParameterProperties(description = "Test Data:All (up to 18 mos ago)")
	public void setValue(String value) {
		Value = value;
	}


	public String getAgencyValue() {
		return agencyValue;
	}
	@ParameterProperties(description = "Test Data:HHA1")
	public void setAgencyValue(String agencyValue) {
		this.agencyValue = agencyValue;
	}

	public String getHic() {
		return hic;
	}
	@ParameterProperties(description = "Test Data:000002778A")
	public void setHic(String hic) {
		this.hic = hic;
	}

	public String getPatient() {
		return patient;
	}
	@ParameterProperties(description = "Test Data:DOE, JOHN 2778")
	public void setPatient(String patient) {
		this.patient = patient;
	}

	public String getDaysduedate() {
		return daysduedate;
	}
	@ParameterProperties(description = "01/21/17")
	public void setDaysduedate(String daysduedate) {
		this.daysduedate = daysduedate;
	}

	public String getDuedate() {
		return duedate;
	}
	@ParameterProperties(description = "02/05/17")
	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public String getCode() {
		return code;
	}
	
	@ParameterProperties(description = "55555")
	public void setCode(String code) {
		this.code = code;
	}

	public ADRSubType getAdrSubmissionType() {
		return adrSubmissionType;
	}

	@ParameterProperties(description = "Provide submission type REJECT or RESEND")
	public void setAdrSubmissionType(ADRSubType adrSubmissionType) {
		this.adrSubmissionType = adrSubmissionType;
	}
	
	
}
