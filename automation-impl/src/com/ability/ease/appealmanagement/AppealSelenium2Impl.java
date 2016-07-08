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

	
}
