package com.ability.ease.HMO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import com.ability.ease.auto.enums.tests.TestType;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.MyAccountSubMenu;
import com.ability.ease.common.AttributePair;
import com.ability.ease.common.BaseTest;
import com.ability.ease.testapi.IHMO;

public class HmoTests extends BaseTest {
	private IHMO hmo;
	private String agency;
	private String hic;
	private String lastName;
	private String firstName;
	private String dob;
	private String sex;

	public String ganerateRandom9DigitNumberfollowedByChar() {
		Random generator = new Random();
		StringBuilder stringBuilder = new StringBuilder();
		long lnum = Math.round(Math.random() * 1000000000);
		char cchar = (char) (generator.nextInt(26) + 'a');
		hic=String.valueOf(lnum)+String.valueOf(cchar);
		return hic;

	}

	@Before
	public void setupTests() throws Exception {
		hmo = (IHMO) context.getBean("hmo");
		
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Patient To HMO", paramsInclude = { "agency,lastName,firstName,dob,sex,testType" })
	public void addPatientToHMO() throws Exception {
		hic = ganerateRandom9DigitNumberfollowedByChar();
		if (hmo.addToHMO(agency, hic, lastName, firstName, dob, sex)) {
			report.report("Successfully added patient to HMO",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("Failed to add patient to HMO", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Existing Patient To HMO", paramsInclude = { "agency,hic,lastName,firstName,dob,sex,testType" })
	public void addToHMODuplicate() throws Exception {
		if (hmo.addToHMODuplicatePatient(agency, hic, lastName, firstName, dob,sex)) {
			report.report("patient is already being tracked by HMO,hence not added",Reporter.ReportAttribute.BOLD);
		} else {
			report.report("patient is not tracked", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Extending the Days in HMO", paramsInclude = { "hic,testType" })
	public void extendHMO() throws Exception {
		if (hmo.extendHMO(hic)) {
			report.report("HMO Track days of a patient is extend",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("HMO Track days is not extend", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Patient to HMO from Patient info Page", paramsInclude = { "testType" })
	public void addtoHMOFromPatientInfo() throws Exception {
		hic = ganerateRandom9DigitNumberfollowedByChar();
		if (hmo.addtoHMOFromPatientInfo(hic)) {
			report.report(
					"Successfully added patient to HMO from Patient Info Page",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report(
					"Failed to add patient to HMO  from patient Info Page",
					Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Remove Patient from HMO Catcher", paramsInclude = { "hic,testType" })
	public void trashHMOPatient() throws Exception {
		if (hmo.trashHMOPatient(hic)) {
			report.report("Patient is removed from HMO Catcher",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("Patient is not  removed from HMO Catcher", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Add Existing Patient to HMO from Patient info Page", paramsInclude = { "hic,testType" })
	public void addDuplicateToHMOFromPatientInfo() throws Exception {
		
		if (hmo.addDuplicatePatientToHMOFromPatientInfo(hic)) {
			report.report(
					"patient is already being tracked by HMO,hence not added",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("patient is not tracked", Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verifing LiveSearch and Print functionality ", paramsInclude = { "testType" })
	public void AdvanceSearchFromHMO() throws Exception {
		if (hmo.AdvanceSearchFromHMO(hic)) {
			hic = ganerateRandom9DigitNumberfollowedByChar();
			report.report("LiveSearch Functionlality from HMO is  working",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("LiveSearch  Functionlality from HMO is not working",
					Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verifing  Print functionality ", paramsInclude = { "testType" })
	public void printFromHMO() throws Exception {
		if (hmo.printFromHMO()) {
			report.report("Print Functionlality from HMO is  working",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("Print  Functionlality from HMO is not working",
					Reporter.FAIL);
		}
	}

	@Test
	@SupportTestTypes(testTypes = { TestType.Selenium2 })
	@TestProperties(name = "Verifing Acknowledge Functionality ", paramsInclude = { "hic,testType" })
	public void acknowledgeHMO() throws Exception {
		if (hmo.acknowledgeHMO(hic)) {
			report.report("Patient is  acknowledged from HMO",
					Reporter.ReportAttribute.BOLD);
		} else {
			report.report("Patient is not acknowledged from HMO", Reporter.FAIL);
		}
	}

	/*
	 * Getters and Setters methods
	 */

	public String getAgency() {
		return agency;
	}

	@ParameterProperties(description = "Enter Agency Name example: HHA1 | Test Agency 1")
	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getHic() {
		return hic;
	}

	public void setHic(String hic) {
		this.hic = hic;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
