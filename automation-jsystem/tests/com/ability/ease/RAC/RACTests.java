package com.ability.ease.RAC;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.SelectTimeframe;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IRAC;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

public class RACTests extends BaseTest {
	private IRAC racSubmissions;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;
	private String hic, agency, DCN, PatientCtrlNo, RefIdParam, Notes, StartDate, ThroughDate, LetterDate;;

	@Before
	public void setupTests() throws Exception {
		racSubmissions = (IRAC) context.getBean("RACSubmission");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName) throws Exception {
		super.handleUIEvent(map, methodName);
		UIAttributesXMLFileName.setUIAttributesxmlfileName(
				TestCommonResource.getTestResoucresDirPath() + "uiattributesxml\\RAC\\AddRAC.xml");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Patient To RAC", paramsInclude = { "AttributeNameValueDialogProvider, testType" })
	public void addtoRAC() throws Exception {
		Map<String, String> mapAttrValues = AttrStringstoMapConvert
				.convertAttrStringstoMapV2(AttributeNameValueDialogProvider);
		keepAsGloablParameter("hic", mapAttrValues.get("Patient HIC"));
		keepAsGloablParameter("dcn", mapAttrValues.get("DCN"));
		keepAsGloablParameter("patientctrlno", mapAttrValues.get("Patient Ctrl No"));
		keepAsGloablParameter("startdate", mapAttrValues.get("Start Date"));
		keepAsGloablParameter("throughdate", mapAttrValues.get("Through Date"));
		keepAsGloablParameter("letterdate", mapAttrValues.get("Letter Date"));
		keepAsGloablParameter("refidparam", mapAttrValues.get("RefIdParam"));
		keepAsGloablParameter("notes", mapAttrValues.get("Notes"));
		if (racSubmissions.addtoRAC(mapAttrValues)) {
			report.report("Claim is added to RAC Successfully !", Reporter.ReportAttribute.BOLD);
		} else {
			report.report("Claim is not added to RAC Successfull", Reporter.FAIL);
		}
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getHic() {
		return hic;
	}

	public void setHic(String hic) {
		this.hic = hic;
	}

	public AttributeNameValueDialogProvider[] getAttributeNameValueDialogProvider() {
		return AttributeNameValueDialogProvider;
	}

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(
			AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify the added Claim in RAC report", paramsInclude = { "hic,testType" })
	public void VerifyClaimInRACSubmissioninReport() throws Exception {
		if (racSubmissions.VerifyClaimInRACSubmissioninReport(hic)) {
			report.report("HIC is present in the RAC Report..!", Reporter.ReportAttribute.BOLD);
		} else {
			report.report("HIC is not present in the RAC Report..!", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verify the RAC Claims in DB", paramsInclude = {
			"hic,DCN,PatientCtrlNo,StartDate,ThroughDate,RefIdParam,LetterDate,Notes,testType" })
	public void VerifyClaimInRACSubmissioninDB() throws Exception {
		if (racSubmissions.VerifyClaimInRACSubmissioninDB(hic, DCN, PatientCtrlNo, StartDate, ThroughDate, RefIdParam,
				LetterDate, Notes)) {
			report.report("claim is present in the RAC Submission Reportest.customert..!",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("claim is not present in the RAC Submission Report..!", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Change Agency", paramsInclude = { "agency,testType" })
	public void changeAgency() throws Exception {
		if (racSubmissions.changeAgnecy(agency)) {
			report.report("Agency changed successfully..!", Reporter.ReportAttribute.BOLD);
		} else {
			report.report("Agency not changee successfully..!", Reporter.FAIL);
		}
	}

	public IRAC getRacSubmissions() {
		return racSubmissions;
	}

	public void setRacSubmissions(IRAC racSubmissions) {
		this.racSubmissions = racSubmissions;
	}

	public String getDCN() {
		return DCN;
	}

	public void setDCN(String dCN) {
		DCN = dCN;
	}

	public String getPatientCtrlNo() {
		return PatientCtrlNo;
	}

	public void setPatientCtrlNo(String patientCtrlNo) {
		PatientCtrlNo = patientCtrlNo;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getThroughDate() {
		return ThroughDate;
	}

	public void setThroughDate(String throughDate) {
		ThroughDate = throughDate;
	}

	public String getLetterDate() {
		return LetterDate;
	}

	public void setLetterDate(String letterDate) {
		LetterDate = letterDate;
	}

	public String getRefIdParam() {
		return RefIdParam;
	}

	public void setRefIdParam(String refIdParam) {
		RefIdParam = refIdParam;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

}
