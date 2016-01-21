package com.ability.ease.auto.common;

import java.util.HashMap;
import java.util.Map;

/**This class contains a static map which creates a map object with filed name as key and UI Attribute xml display name as value
 * the key values are picked from XML file created in database table userdderequest after submitting the claim 
 * @author : nageswar.bodduri
 */
public class UB04FormXMLMap {
	
	private static final Map<String, String> ub04FormXMLMap;
	
	static {
		ub04FormXMLMap = new HashMap<String, String>();
		ub04FormXMLMap.put("patient_cntr", "Patient Control Number");
		ub04FormXMLMap.put("medical_record_nbr", "Medical Record Number");
		ub04FormXMLMap.put("tob", "TYPE OF BILL");
		ub04FormXMLMap.put("facility_zip", "Billing Provider City ZIP");
		ub04FormXMLMap.put("patient_tax_num", "Federal Tax Nor");
		ub04FormXMLMap.put("from", "Statement from which period");
		ub04FormXMLMap.put("through", "Statement to which period");
		ub04FormXMLMap.put("patient_last_name", "PATIENT NAME");
		//gaps need to fill
		ub04FormXMLMap.put("patient_zip", "Patient ZIP");
		ub04FormXMLMap.put("patient_dob", "Patient DOB");
		ub04FormXMLMap.put("patient_sex", "Patient SEX");
		ub04FormXMLMap.put("admission_date", "Patient Admission Date");
		ub04FormXMLMap.put("admit_hour", "Patient Admission Hour");
		ub04FormXMLMap.put("admit_type", "Patient Admission Type");
		ub04FormXMLMap.put("admit_src", "Patient Admission Source");
		ub04FormXMLMap.put("discharge_hour", "Discharge Hour");
		ub04FormXMLMap.put("discharge_status", "Discharge Status");
		ub04FormXMLMap.put("condition_codes", "Condition Codes");
		ub04FormXMLMap.put("adjustment_reason_code", "ADJ-Reason Code");
		ub04FormXMLMap.put("cd_codes", "OCCURRENCE Codes and Dates");
		ub04FormXMLMap.put("span_codes", "OCCURRENCE Span Codes");
		ub04FormXMLMap.put("value_codes", "Value Codes");
		//claim lines are not included here as we had a separate validation for claims totals
		ub04FormXMLMap.put("payer_cd", "Payer Codes");
		ub04FormXMLMap.put("payer", "Payer Names");
		ub04FormXMLMap.put("payer_ri", "Release of Information");
		ub04FormXMLMap.put("payer_assignment_benefits", "Payer Assignment of Benefits");
		ub04FormXMLMap.put("payer_est_amt", "Payer Estimated Ammount");
		//Payer oscar value need to be fill
		ub04FormXMLMap.put("insurance_insured_last_name", "Insurance Insureds Name");
		ub04FormXMLMap.put("insurance_insured_rel", "Patient's Relation to Insured");
		ub04FormXMLMap.put("insurance_hic", "Insureds Unique ID Primary");
		ub04FormXMLMap.put("hic", "Insureds Unique ID Primary");
		ub04FormXMLMap.put("insruance_group", "Insurance Group Name Primary");
		ub04FormXMLMap.put("insurance_group_num", "Insurance Group No Primary");
		ub04FormXMLMap.put("match_key", "Treatment Authorization Number");
		ub04FormXMLMap.put("diagnosis_codes", "Principal Diagnosis Code on Admission");
		ub04FormXMLMap.put("admit_diagnosis", "Admitting Diagnosis Code");
		ub04FormXMLMap.put("reason_for_visit_code", "Patient's Reason for Visit");
		ub04FormXMLMap.put("e_code", "Patient's Reason for Visit");
		ub04FormXMLMap.put("att_phys_npi", "Attending Provider NPI");
		ub04FormXMLMap.put("procedure_codes_dates", "Principal Procedure Codes and Dates");
		ub04FormXMLMap.put("att_phys_ln", "Attending Provider Last Name");
		ub04FormXMLMap.put("att_phys_fn", "Attending Provider First Name");
		ub04FormXMLMap.put("oper_phys_npi", "Operating Provider NPI");
		ub04FormXMLMap.put("oper_phys_ln", "Operating Provider Last Name");
		ub04FormXMLMap.put("oper_phys_fn", "Operating Provider First Name");
		ub04FormXMLMap.put("othr_phys_npi", "Other Provider NPI");
		ub04FormXMLMap.put("othr_phys_ln", "Other Provider Last Name");
		ub04FormXMLMap.put("othr_phys_fn", "Other Provider First Name");
		ub04FormXMLMap.put("patient_addr", "Patient Adress");
	}

}
