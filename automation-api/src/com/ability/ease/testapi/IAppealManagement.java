package com.ability.ease.testapi;

public interface IAppealManagement {
	boolean verifyValidationsUnderViewNotes(String monthsAgo,String notes) throws Exception;
	boolean verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier() throws Exception;
	boolean verifyUIFieldsUnderLEVEL1APPEALCLAIMSREPORTForHHAAgency(String agency, String columns, String fromDate) throws Exception;
	
	
	
	boolean verifyTimeFrameOptionsFunctionalityUnderAppealsReport() throws Exception;
	boolean verifyMenuOptionsAvailableUnderAppealsReport() throws Exception;
	boolean verifyHICSearchOptionUnderAppealsReport() throws Exception;
	boolean verifyAgencyOptionUnderAppealsReport() throws Exception;
	boolean verifyExportOptionsUnderAppealsReport() throws Exception;
	boolean verifyClaimTagMultiSelectListBoxinAdnacedSearch() throws Exception;
	boolean verifyAddTag(String tagname, String hic) throws Exception;
	boolean verifyViewTag(String tagname, String hic) throws Exception;
	boolean verifyDeleteTag(String tagname, String hic, String expectedalertmessage) throws Exception;
	boolean sendDocumentToCMS(String hic, String claimIDorDCN, String caseID, String reviewContractorName) throws Exception;
}
