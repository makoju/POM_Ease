package com.ability.ease.appealmanagement;

import com.ability.ease.testapi.IAppealManagement;

public class AppealSelenium2Impl implements IAppealManagement {
	
	private AppealManagementPage appeal = new AppealManagementPage();
	
	@Override
	public boolean verifyValidationsUnderViewNotes(String monthsAgo, String notes) throws Exception {
		return appeal.verifyValidationsUnderViewNotes(monthsAgo, notes);
	}

	@Override
	public boolean verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier() throws Exception {
		return appeal.verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier();
	}

	@Override
	public boolean verifyTimeFrameOptionsFunctionalityUnderAppealsReport()
			throws Exception {
		return appeal.verifyTimeFrameOptionsFunctionalityUnderAppealsReport();
	}

	@Override
	public boolean verifyMenuOptionsAvailableUnderAppealsReport()
			throws Exception {
		return appeal.verifyMenuOptionsAvailableUnderAppealsReport();
	}

	@Override
	public boolean verifyHICSearchOptionUnderAppealsReport() throws Exception {
		return appeal.verifyHICSearchOptionUnderAppealsReport();
	}

	@Override
	public boolean verifyAgencyOptionUnderAppealsReport() throws Exception {
		return appeal.verifyAgencyOptionUnderAppealsReport();
	}

	@Override
	public boolean verifyExportOptionsUnderAppealsReport() throws Exception {
		return appeal.verifyExportOptionsUnderAppealsReport();
	}

	@Override
	public boolean verifyClaimTagMultiSelectListBoxinAdnacedSearch()
			throws Exception {
		return appeal.verifyClaimTagMultiSelectListBoxinAdnacedSearch();
	}

	@Override
	public boolean verifyAddTag(String tagname, String hic) throws Exception {
		return appeal.verifyAddTag(tagname, hic);
	}

	@Override
	public boolean verifyViewTag(String tagname, String hic) throws Exception {
		return appeal.verifyViewTag(tagname, hic);
	}

	@Override
	public boolean verifyDeleteTag(String tagname, String hic, String expectedalertmessage) throws Exception {
		return appeal.verifyDeleteTag(tagname, hic, expectedalertmessage);
	}

	@Override
	public boolean sendDocumentToCMS(String hic, String claimIDorDCN, String caseID, String reviewContractorName)
			throws Exception {
		return appeal.sendDocumentToCMS(hic,claimIDorDCN,caseID,reviewContractorName);
	}

}
