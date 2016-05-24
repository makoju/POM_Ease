package com.ability.ease.myaccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class SetUpAlertsPage extends AbstractPageObject {

	public boolean setUpAlerts(String username) throws Exception {
		int failurecount=0;
		
		navigateToPage();
		clickLink("Setup Alerts");

		List<Integer> prev = getUserAlerts(username);

		// clickImage("alertonoff0");
		List<WebElement> deleteElements1 = driver.findElements(By.xpath("//img[@src='assets/images/red_checkbox.png']"));
		List<WebElement> deleteElements2 = driver.findElements(By.xpath("//img[@src='assets/images/green_checkbox.png']"));
		for (WebElement checkbox : deleteElements1) {
			checkbox.click();
		}
		 
		for (WebElement checkbox : deleteElements2) {
			checkbox.click();
		}

		clickButtonV2("Submit");

		if (!verifyAlert("User alerts updated!")) {
			report.report("Failed to Setup alert for user"+username, Reporter.WARNING);
			return false;
		}
		List<Integer> next = getUserAlerts(username);
		
		for (int i=0;i < prev.size(); i++) {
			if(prev.get(i) == next.get(i)) {
				report.report("Previous and Next Alerts were supposed to be different but are same: "+prev.get(i), Reporter.WARNING);
				failurecount++;
			}
		}
		return failurecount==0?true:false;
	}

	public List<Integer> getUserAlerts(String username) throws SQLException {

		MySQLDBUtil.initializeDBConnection();

		String query = "SELECT OnOff FROM ddez.useralertconfig where AlertType = 1 and userID=(Select UserID from ddez.user where username='"
				+ username + "')";

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB(query);

		List<Integer> lst = new ArrayList<Integer>(10);
		while(rs.next()) {
			lst.add(rs.getInt(1));
		}
		return lst;
	}

	@Override
	public void assertInPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void navigateToPage() throws Exception {
		int count = 0;
		while (!isTextPresent("Setup Alerts") && count++ < 3) {
			HomePage.getInstance().navigateTo(Menu.MYACCOUNT, null);
		}

	}
}
