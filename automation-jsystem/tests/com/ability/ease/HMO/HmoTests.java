package com.ability.ease.HMO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import jsystem.framework.report.Reporter;
import jsystem.framework.scenario.Parameter;

import org.junit.Before;
import org.junit.Test;

import com.ability.auto.common.AttrStringstoMapConvert;
import com.ability.ease.auto.common.annotations.SupportTestTypes;
import com.ability.ease.auto.common.test.resource.TestCommonResource;
import com.ability.ease.auto.dlgproviders.AttributeNameValueDialogProvider;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IHMO;

public class HmoTests extends BaseTest{
	private IHMO hmo;
	private String  sAgency;
	private String sHIC;
	private String sLastName;
	private String sFirstName;
	private String sDob;
	private String sSex;


	@Before
	public void setupTests()throws Exception{
		hmo = (IHMO)context.getBean("hmo");
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Add Patient To HMO", paramsInclude =  { "Agency,HIC,Lname,Fname,DOB,sex,testType" })
	public void addPatientToHMO()throws Exception{
		if(hmo.addToHMO(sAgency, sHIC, sLastName, sFirstName, sDob, sSex)){
			report.report("Successfully added patient to HMO", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to add patient to HMO", Reporter.FAIL);
		}
	}
	

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Add Existing Patient To HMO", paramsInclude =  { "Agency,HIC,Lname,Fname,DOB,sex,testType" })
	public void addToHMODuplicate()throws Exception{
		if(hmo.addToHMODuplicatePatient(sAgency, sHIC, sLastName, sFirstName, sDob, sSex)){
			report.report("patient is already being tracked by HMO,hence not added", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("patient is not tracked", Reporter.FAIL);
		}
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Extending the Days in HMO",paramsInclude = { "HIC,testType" })
	public void extendHMO()throws Exception{
		if(hmo.extendHMO(sHIC)){
			report.report("HMO Track days of a patient is extend", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("HMO Track days is not extend", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Add Patient to HMO from Patient info Page",paramsInclude = { "HIC,testType" })
	public void addtoHMOFromPatientInfo()throws Exception{
		if(hmo.addtoHMOFromPatientInfo(sHIC)){
			report.report("Successfully added patient to HMO from Patient Info Page", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Failed to add patient to HMO  from patient Info Page", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Remove Patient from HMO Catcher",paramsInclude = { "HIC,testType" })
	public void trashHMOPatient()throws Exception{
		if(hmo.trashHMOPatient(sHIC)){
			report.report("Patient is removed from HMO Catcher", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Patient is removed from HMO Catcher", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Add Existing Patient to HMO from Patient info Page",paramsInclude = { "HIC,testType" })
	public void addDuplicateToHMOFromPatientInfo()throws Exception{
		if(hmo.addDuplicatePatientToHMOFromPatientInfo(sHIC)){
			report.report("patient is already being tracked by HMO,hence not added", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("patient is not tracked", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Verifing LiveSearch and Print functionality ",paramsInclude = { "HIC,testType" })
	public void AdvanceSearchFromHMO()throws Exception{
		if(hmo.AdvanceSearchFromHMO(sHIC)){
			report.report("LiveSearch Functionlality from HMO is  working", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("LiveSearch  Functionlality from HMO is not working", Reporter.FAIL);
		}
	}
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Verifing  Print functionality ",paramsInclude = { "testType" })
	public void printFromHMO()throws Exception{
		if(hmo.printFromHMO()){
			report.report("Print Functionlality from HMO is  working", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Print  Functionlality from HMO is not working", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "Verifing Acknowledge Functionality ",paramsInclude = { "HIC,testType" })
	public void acknowledgeHMO()throws Exception{
		if(hmo.acknowledgeHMO(sHIC)){
			report.report("Patient is  acknowledged from HMO", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("Patient is not acknowledged from HMO", Reporter.FAIL);
		}
	}
	
	/*
	Getters and Setters methods
	*/
	public IHMO getHmo() {
		return hmo;
	}

	public void setHmo(IHMO hmo) {
		this.hmo = hmo;
	}

	public String getAgency() {
		return sAgency;
	}

	public void setAgency(String agency) {
		sAgency = agency;
	}

	public String getHIC() {
		return sHIC;
	}

	public void setHIC(String hIC) {
		sHIC = hIC;
	}

	public String getLname() {
		return sLastName;
	}

	public void setLname(String lname) {
		sLastName = lname;
	}

	public String getFname() {
		return sFirstName;
	}

	public void setFname(String fname) {
		sFirstName = fname;
	}

	public String getDOB() {
		return sDob;
	}

	public void setDOB(String dOB) {
		sDob = dOB;
	}

	public String getSex() {
		return sSex;
	}

	public void setSex(String sex) {
		this.sSex = sex;
	}





}
