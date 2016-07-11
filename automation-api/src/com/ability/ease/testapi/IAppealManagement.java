package com.ability.ease.testapi;

public interface IAppealManagement {
	
	boolean verifyValidationsUnderViewNotes(String monthsAgo,String notes) throws Exception;
	boolean verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier() throws Exception;
	boolean verifyTimeFrameOptionsFunctionalityUnderAppealsReport() throws Exception;
	boolean verifyMenuOptionsAvailableUnderAppealsReport() throws Exception;
	boolean verifyHICSearchOptionUnderAppealsReport() throws Exception;
	boolean verifyAgencyOptionUnderAppealsReport() throws Exception;
	boolean verifyExportOptionsUnderAppealsReport() throws Exception;
	boolean verifyClaimTagMultiSelectListBoxinAdnacedSearch() throws Exception;
}
