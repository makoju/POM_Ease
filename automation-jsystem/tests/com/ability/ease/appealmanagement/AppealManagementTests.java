package com.ability.ease.appealmanagement;

import java.util.HashMap;
import java.util.Map;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;
import jsystem.framework.scenario.UseProvider;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.SelectTimeframe;
import com.ability.ease.auto.enums.tests.Status;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IAppealManagement;
import com.ability.ease.testapi.IMyDDE;

public class AppealManagementTests extends BaseTest{

	private IAppealManagement appeal;
	private String monthsAgo;
	private String notes;

	

	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

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

	@ParameterProperties(description = "Provide the UI Screen Attributes to be set as a AttributeName,AttributeValue Pair")
	@UseProvider(provider = jsystem.extensions.paramproviders.ObjectArrayParameterProvider.class)
	public void setAttributeNameValueDialogProvider(
			AttributeNameValueDialogProvider[] attributeNameValueDialogProvider) {
		AttributeNameValueDialogProvider = attributeNameValueDialogProvider;
	}
}
