package com.ability.ease.jobschedule;

import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.home.HomePage.SubMenu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class jobSchedule extends AbstractPageObject{

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub
		
	}
	public void verifyEligibilityJob(Menu menuName, SubMenu subMenuName) throws Exception {
		if(menuName!=null && subMenuName!=null)
			report.startLevel("Navigating to " + menuName.name() + " > "+ subMenuName.name());
		/*clickLinkOnlyIfExists(menuName.toString());
		Thread.sleep(3000);
		clickLinkOnlyIfExists(subMenuName.toString());*/
	
	}

}
