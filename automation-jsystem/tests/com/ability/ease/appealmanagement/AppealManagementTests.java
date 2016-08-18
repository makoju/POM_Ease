package com.ability.ease.appealmanagement;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IAppealManagement;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

public class AppealManagementTests extends BaseTest{

	private IAppealManagement appeal;
	private String monthsAgo;
	private String notes;
	private String agency;
	private String expectedColumns;
	private String fromDate;
	private String hic,claimIDorDCN,caseID,reviewContractorName;

	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String tagname;
	private String expectedalertmessage;

	@Before
	public void setupTests()throws Exception{
		appeal = (IAppealManagement)context.getBean("appeal");

	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);
//		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\AdvancedSearch.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the validations and functionality under the View Notes pop up Screen", paramsInclude = { "monthsAgo,notes,testType" })
	public void verifyValidationsUnderViewNotes()throws Exception{
		if(appeal.verifyValidationsUnderViewNotes(monthsAgo,notes)){
			report.report("Successfully verified the validations and functionality under the View Notes pop up Screen.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify the validations and functionality under the View Notes pop up Screen.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the UI fields present under the LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT for HHA Agency.", paramsInclude = { "agency,expectedColumns,fromDate,testType" })
	public void verifyUIFieldsUnderLEVEL1APPEALCLAIMSREPORTForHHAAgency()throws Exception{
		if(appeal.verifyUIFieldsUnderLEVEL1APPEALCLAIMSREPORTForHHAAgency(agency, expectedColumns, fromDate)){
			report.report("Successfully verified the UI fields present under the LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT for HHA Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify the UI fields present under the LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT for HHA Agency.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify whether the Tag drop down is displaying only the tags that are not added earlier.", paramsInclude = { "testType" })
	public void verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier()throws Exception{
		if(appeal.verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier()){
			report.report("Successfully verified whether the Tag drop down is displaying only the tags that are not added earlier.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify whether the Tag drop down is displaying only the tags that are not added earlier.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the TimeFrame Options under appeals report", paramsInclude = { "testType" })
	public void verifyTimeFrameOptionsFunctionalityUnderAppealsReport()throws Exception{
		if(appeal.verifyTimeFrameOptionsFunctionalityUnderAppealsReport()){
			report.report("Successfully verified Timeframe options under appeals claims submission report.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Timeframe options under appeals claims submission report.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the Menu Options under appeals report", paramsInclude = { "testType" })
	public void verifyMenuOptionsAvailableUnderAppealsReport()throws Exception{
		if(appeal.verifyMenuOptionsAvailableUnderAppealsReport()){
			report.report("Successfully verified Menu options under appeals claims submission report.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Menu options under appeals claims submission report.", Reporter.FAIL);
		}
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the HIC Search Options under appeals report", paramsInclude = { "testType" })
	public void verifyHICSearchOptionUnderAppealsReport()throws Exception{
		if(appeal.verifyHICSearchOptionUnderAppealsReport()){
			report.report("Successfully verified HIC Search options under appeals claims submission report.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify HIC Search options under appeals claims submission report.", Reporter.FAIL);
		}
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Export Options under appeals report", paramsInclude = { "testType" })
	public void verifyExportOptionsUnderAppealsReport()throws Exception{
		if(appeal.verifyExportOptionsUnderAppealsReport()){
			report.report("Successfully verified Export options under appeals claims submission report.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Export options under appeals claims submission report.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the Agency Select Option under appeals report", paramsInclude = { "testType" })
	public void verifyAgencyOptionUnderAppealsReport()throws Exception{
		if(appeal.verifyAgencyOptionUnderAppealsReport()){
			report.report("Successfully verified Agency Select option under appeals claims submission report.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Agency Select option under appeals claims submission report.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the Claim Tag MultiSelectListBox Option in Advacned Search", paramsInclude = { "testType" })
	public void verifyClaimTagMultiSelectListBoxinAdnacedSearch()throws Exception{
		if(appeal.verifyClaimTagMultiSelectListBoxinAdnacedSearch()){
			report.report("Successfully verified Claim Tag MultiSelectListBox Option in Advacned Search.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify Claim Tag MultiSelectListBox Option in Advacned Search.", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Add Tag", paramsInclude = { "testType, hic, tagname" })
	public void verifyAddTag()throws Exception{
		if(appeal.verifyAddTag(tagname,hic)){
			report.report("Successfully added tag and verified", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to add tag and verify", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify View Tag", paramsInclude = { "testType,hic,tagname" })
	public void verifyViewTag()throws Exception{
		if(appeal.verifyViewTag(tagname,hic)){
			report.report("Successfully Viewed the added tag", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to View the added tag", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Delete Tag", paramsInclude = { "testType, tagname, hic, expectedalertmessage" })
	public void verifyDeleteTag()throws Exception{
		if(appeal.verifyDeleteTag(tagname, hic,expectedalertmessage)){
			report.report("Successfully deleted the added tag", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to delete added tag", Reporter.FAIL);
		}
	}
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "sendDocumentToCMS", paramsInclude = { "testType,hic,claimIDorDCN,caseID,reviewContractorName,expectedalertmessage" })
	public void sendDocumentToCMS()throws Exception{
		if(appeal.sendDocumentToCMS(hic,claimIDorDCN,caseID,reviewContractorName)){
			report.report("Successfully deleted the added tag", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to delete added tag", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "VerifyClaimtagSearchinAdvanceSearchPage", paramsInclude = { "testType,tagname" })
	public void verifySearchCriteriawithclaimtagInAdvanceSearchPage()throws Exception{
		if(appeal.verifySearchCriteriawithclaimtagInAdvanceSearchPage(tagname)){
			report.report("Successfully verified SearchCriteria with claimtag In Advance Search", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify SearchCriteria with claimtag In Advance Search", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "verifyAddTagUnderCLAIMTAGGINGINFORMATIONScreen", paramsInclude = { "testType" })
	public void verifyAddTagUnderCLAIMTAGGINGINFORMATIONScreen()throws Exception{
		if(appeal.verifyAddTagUnderCLAIMTAGGINGINFORMATIONScreen()){
			report.report("Successfully verified AddTag functionality Under CLAIMTAGGINGINFORMATION Screen", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to verify AddTag functionality Under CLAIMTAGGINGINFORMATION Screen", Reporter.FAIL);
		}
	}
	

	public String getMonthsAgo() {
		return monthsAgo;
	}

	public void setMonthsAgo(String monthsAgo) {
		this.monthsAgo = monthsAgo;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	
	public String getExpectedalertmessage() {
		return expectedalertmessage;
	}

	public void setExpectedalertmessage(String expectedalertmessage) {
		this.expectedalertmessage = expectedalertmessage;
	}
	
	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getExpectedColumns() {
		return expectedColumns;
	}

	public void setExpectedColumns(String expectedColumns) {
		this.expectedColumns = expectedColumns;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	public String getHic() {
		return hic;
	}

	public void setHic(String hic) {
		this.hic = hic;
	}
	
	public String getClaimIDorDCN() {
		return claimIDorDCN;
	}

	public void setClaimIDorDCN(String claimIDorDCN) {
		this.claimIDorDCN = claimIDorDCN;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public String getReviewContractorName() {
		return reviewContractorName;
	}

	public void setReviewContractorName(String reviewContractorName) {
		this.reviewContractorName = reviewContractorName;
	}

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(
			AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}
}
