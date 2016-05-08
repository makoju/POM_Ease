package com.ability.ease.auto.common;

import com.ability.ease.auto.utilities.HttpClientUtil;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class CommonPage extends AbstractPageObject{

	public boolean launchARTDashboardURL(String sURL)throws Exception{
		
		boolean flag;
		int response = HttpClientUtil.sendPOST(sURL);
		if( response == 200){
			report.report("Launching ART Dashboard URL to copy images to reporint server...");
			driver.get("http://" + sURL);
			flag = true;
		}else{
			report.report("Launching ART Dashboard URL failed with reponse code HTTP:" + response);
			flag = false;
		}
		
		return flag;
	}


	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		// TODO Auto-generated method stub

	}

}
