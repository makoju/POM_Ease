package com.ability.ease.testapi;

public interface IAppealManagement {
	
	boolean verifyValidationsUnderViewNotes(String monthsAgo,String notes) throws Exception;
	boolean verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier() throws Exception;
}
