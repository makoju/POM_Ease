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
	private AttributePair[] attrpair;
	private AttributeNameValueDialogProvider[] AttributeNameValueDialogProvider;

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
	@TestProperties(name = "Add Patient To HMO Duplicate", paramsInclude =  { "Agency,HIC,Lname,Fname,DOB,sex,testType" })
	public void addToHMODuplicate()throws Exception{
		if(hmo.addToHMODuplicate(sAgency, sHIC, sLastName, sFirstName, sDob, sSex)){
			report.report("patient is already being tracked by HMO,hence not added", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("patient is not tracked", Reporter.FAIL);
		}
	}
	
	
	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2} )
	@TestProperties(name = "ExtendHMO",paramsInclude = { "testType" })
	public void ExtendHMO()throws Exception{
		if(hmo.extendHMO()){
			report.report("HMO is extend by some days", Reporter.ReportAttribute.BOLD);
		}else{
			report.report("HMO is not extend by some days", Reporter.FAIL);
		}
	}
	
	
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
