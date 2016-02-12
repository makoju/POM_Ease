package com.ability.ease.mydde;

import java.util.HashMap;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

import org.junit.Before;
import org.junit.Test;

import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.SelectTimeframe;
import com.ability.ease.auto.enums.tests.Status;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IMyDDE;

public class MyDDETests extends BaseTest{

	private IMyDDE mydde;
	private SelectTimeframe timeframe;
	private String agency;
	private String hic;
	private String monthsAgo;
	private Status status;
	private String location;
	private String fromDate,toDate;

	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

	@Before
	public void setupTests()throws Exception{
		mydde = (IMyDDE)context.getBean("mydde");
	}

	public void handleUIEvent(HashMap<String, Parameter> map, String methodName)throws Exception{
		super.handleUIEvent(map, methodName);

		UIAttributesXMLFileName.setUIAttributesxmlfileName(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\MyDDE\\MyDDE.xml");
		map.get("FromDate").setEditable(false);
		map.get("ToDate").setEditable(false);
		if(map.get("Timeframe").getStringValue().equalsIgnoreCase(SelectTimeframe.Date.toString()))
		{
			map.get("FromDate").setEditable(true);
			map.get("ToDate").setEditable(true);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify the page view", paramsInclude = { "timeframe,fromDate,toDate,agency,testType" })
	public void verifyPageViewAndOptions()throws Exception{
		if(mydde.verifyPageViewAndOptions()){
			report.report("Successfully Verified Page view and Options under it.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Page view and Options under it.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Reports for HHA Agencies", paramsInclude = { "testType" })
	public void verifyOptionsUnderReportsForHHA()throws Exception{
		if(mydde.verifyOptionsUnderReportsForHHA()){
			report.report("Successfully Verified Options under Reports for HHA Agencies.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Reports for HHA Agencies.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Timeframe ", paramsInclude = { "testType" })
	public void verifyOptionsUnderTimeframe()throws Exception{
		if(mydde.verifyOptionsUnderTimeframe()){
			report.report("Successfully Verified Options under Timeframe.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Timeframe.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Agency ", paramsInclude = { "agency,testType" })
	public void verifyOptionsUnderAgency()throws Exception{
		if(mydde.verifyOptionsUnderAgency(agency)){
			report.report("Successfully Verified Options under Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Agency.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verifying Save Profile option under Advance Search", paramsInclude = { "testType" })
	public void verifySaveProfileOption()throws Exception{
		if(mydde.verifySaveProfileOption()){
			report.report("Successfully Verified Options under Agency.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Agency.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify options under Reports for Non-HHA Agencies", paramsInclude = { "testType" })
	public void verifyOptionsUnderReportsForNonHHA()throws Exception{
		if(mydde.verifyOptionsUnderReportsForNonHHA()){
			report.report("Successfully Verified Options under Reports for Non-HHA Agencies.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify Options under Reports for Non-HHA Agencies.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify TimeFrame Options Under Advanced", paramsInclude = { "testType" })
	public void verifyTimeFrameOptionsUnderAdvanced()throws Exception{
		if(mydde.verifyTimeFrameOptionsUnderAdvanced()){
			report.report("Successfully Verified TimeFrame Options Under Advanced.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verify TimeFrame Options Under Advanced.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advanced Search For MAX Fields", paramsInclude = { "hic,monthsAgo,status,location,testType" })
	public void verifyAdvanceSearchForMAXFields()throws Exception{
		if(mydde.verifyAdvanceSearchForMAXFields(hic,monthsAgo,status,location)){
			report.report("Successfully Verified Advanced Search For MAX Fields.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verified Advanced Search For MAX Fields.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 } )
	@TestProperties(name = "Verify Advance Search report for Status:Suspense & Location:B6001", paramsInclude = { "testType" })
	public void verifyAdvancedSearchForStatusLocation()throws Exception{
		if(mydde.verifyAdvancedSearchForStatusLocation()){
			report.report("Successfully Verified Advanced Search For Status Location.", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to Verified Advanced Search For Status Location.Please see the JSystem report log for more details", Reporter.FAIL);
		}
	}


	/*####
	##Getters and Setters methods
	######*/	

	public SelectTimeframe getTimeframe() {
		return timeframe;
	}

	@ParameterProperties(description = "Select Timeframe" )
	public void setTimeframe(SelectTimeframe timeframe) {
		this.timeframe = timeframe;
	}

	public String getAgency() {
		return agency;
	}

	@ParameterProperties(description = "Provide an Agency/Agencies using comma separate" )
	public void setAgency(String agency) {
		this.agency = agency;
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
	
	public String getHic() {
		return hic;
	}

	@ParameterProperties(description = "Provide HIC")
	public void setHic(String hic) {
		this.hic = hic;
	}
	
	public String getMonthsAgo() {
		return monthsAgo;
	}

	@ParameterProperties(description = "Months Ago field in Advanced Search")
	public void setMonthsAgo(String monthsAgo) {
		this.monthsAgo = monthsAgo;
	}

	public String getLocation() {
		return location;
	}
	
	@ParameterProperties(description = "Locations should be B9000,B9099,B9997 etc.,")
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Status getStatus() {
		return status;
	}
	
	@ParameterProperties(description = "Select Status")
	public void setStatus(Status status) {
		this.status = status;
	}

	public AttributeNameValueDialogProvider[] getAttributeNameValueDialogProvider() {
		return AttributeNameValueDialogProvider;
	}

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}
}
