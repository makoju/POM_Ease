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
import com.ability.ease.auto.enums.tests.SelectTimeframe;
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
	private String expectedCMSStatusTableHeaders;
	private String agency;
	private int length;
	private String timeFrame,customDate,agencyName,HIC,patientName,thirtyDayDueDate,dueDate,code;
	private ADRSubType adrSubmissionType;
	private SelectTimeframe timeframe;
	private String fromDate,toDate;
	private String adrStatusReportColumnHeaders;

	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		auditdoc = (IAuditDoc)context.getBean("auditdoc");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify ADR Status Report For : ${agency}", 
	paramsInclude = {"agency,HIC,patientName,dueDate,thirtyDayDueDate,code,adrStatusReportColumnHeaders,testType"})
	public void verifyADRESMDStatusReport() throws Exception{	

		if(!auditdoc.verifyADRESMDStatusReportColumns(HIC,patientName,dueDate,thirtyDayDueDate,code,adrStatusReportColumnHeaders,agency)){
			report.report("Failed to validate ADR status report columns and links!!!", Reporter.FAIL);
		}else{
			report.report("Successfully validated ADR status report columns and links!!!",Reporter.ReportAttribute.BOLD);
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
	@TestProperties(name = "Verify Records Present Under eSMDreport", paramsInclude = { "agency, testType" })
	public void verifyRecordsPresenUnderESMDreport()throws Exception{

		if(!auditdoc.verifyRecordsPresenUnderESMDreport(agency)){
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

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Document Split Functionality",paramsInclude = { "claimIDDCN, testType" })
	public void isDocSplitted()throws Exception{

		if(!auditdoc.isDocSplitted(claimIDDCN)){
			report.report("Document submitted not splitted into multiple chunks", Reporter.FAIL);
		}else{
			report.report("Documet submitted is splitted successfully" , Reporter.ReportAttribute.BOLD);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Change Time Frame",paramsInclude = { "testType" })
	public void changeTimeFrame()throws Exception{

		if(!auditdoc.changeTimeFrame()){
			report.report("Failed to change time frame !!!" , Reporter.FAIL);
		}else{
			report.report("Successfully changed time frame !!!" , Reporter.ReportAttribute.BOLD);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Change Agency To ${agency}",paramsInclude = { "agency, testType" })
	public void changeAgency()throws Exception{

		if(!auditdoc.changeAgency(agency)){
			report.report("Failed to change agency to -> " + agency, Reporter.FAIL);
		}else{
			report.report("Successfully changed to agency -> " + agency , Reporter.ReportAttribute.BOLD);
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


	public String getCode() {
		return code;
	}

	@ParameterProperties(description = "The code associated with ADR ex., 55555")
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

	public String getTimeFrame() {
		return timeFrame;
	}

	@ParameterProperties(description = "Time Frame {Overnight,Weekly,All (up to 18 mos ago)}")
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}

	public String getCustomDate() {
		return customDate;
	}

	@ParameterProperties(description = "Custom Time Frame Value : {From:7/21/2011,To:7/21/2016}")
	public void setCustomDate(String customDate) {
		this.customDate = customDate;
	}

	public String getAgency() {
		return agency;
	}

	@ParameterProperties(description = "Provide Agency Name {HHA1,HHA2,HHA3,HOSPITAL,HOSPICE,SNF}")
	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getHIC() {
		return HIC;
	}

	@ParameterProperties(description = "Patient HIC ID ex., 000002778A")
	public void setHIC(String hIC) {
		HIC = hIC;
	}

	public String getPatientName() {
		return patientName;
	}

	@ParameterProperties(description = "Patient Name ex., DOE, JOHN")
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getThirtyDayDueDate() {
		return thirtyDayDueDate;
	}

	@ParameterProperties(description = "30 Day Due Date ex ., 01/21/17")
	public void setThirtyDayDueDate(String thirtyDayDueDate) {
		this.thirtyDayDueDate = thirtyDayDueDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	@ParameterProperties(description = "Due Date ex., 02/05/17")
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public SelectTimeframe getTimeframe() {
		return timeframe;
	}

	@ParameterProperties(description = "Select Timeframe" )
	public void setTimeframe(SelectTimeframe timeframe) {
		this.timeframe = timeframe;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getAdrStatusReportColumnHeaders() {
		return adrStatusReportColumnHeaders;
	}

	@ParameterProperties(description = "Provide comma seperated column headrs to be validated { HIC,Patient Name,S/Loc etc.,}" )
	public void setAdrStatusReportColumnHeaders(String adrStatusReportColumnHeaders) {
		this.adrStatusReportColumnHeaders = adrStatusReportColumnHeaders;
	}
	
}
