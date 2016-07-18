package com.ability.ease.appealmanagement;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.ReportAttribute;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ability.ease.auditdoc.AuditDocHelper;
import com.ability.ease.auto.common.MySQLDBUtil;
import com.ability.ease.auto.common.Verify;
import com.ability.ease.auto.enums.portal.selenium.ByLocator;
import com.ability.ease.auto.enums.tests.EaseSubMenuItems.ADRFileFomat;
import com.ability.ease.home.HomePage;
import com.ability.ease.home.HomePage.Menu;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

/**
 * 
 * @author abhilash.chavva
 *
 */

public class AppealManagementPage extends AbstractPageObject{

	String expectedtagname, tagAgencytext;
	static int firstaddtagrownumber = -1;
	static String hic;

	public boolean verifyValidationsUnderViewNotes(String monthsAgo, String notes) throws Exception {
		int failurecount = 0;
		WebElement tagLabel = null, notesLabel = null, submitButton = null;

		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), monthsAgo);
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		Thread.sleep(5000);
		boolean isPresent = driver.findElements(By.linkText(elementprop.getProperty("SEARCH_RESULTS_VIEW_TAGS_LINKTEXT"))).size() > 0;
		if(!isPresent){
			report.report("View tags link identified.....",ReportAttribute.BOLD);
			driver.findElement(By.xpath(elementprop.getProperty("SEARCH_RESULTS_ADD_TAG_IMAGE_XPATH"))).click();
			WebElement notesTextArea = waitForElementToBeClickable(ByLocator.xpath, "//div[@id='add-claim-tag']/descendant::textarea[@id='ClaimTagNoteTextId']", 30); 
			typeEditBoxByWebElement(notesTextArea, notes);
			clickOnElement(ByLocator.xpath, "//div[@id='add-claim-tag']/descendant::button[text()='Submit']", 30);
		}
		driver.findElement(By.linkText(elementprop.getProperty("SEARCH_RESULTS_VIEW_TAGS_LINKTEXT"))).click();
		waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("VIEW_NOTES_UNDER_CLAIM_TAGGING_HISTORY_LINKTEXT"), 30);
		clickLink(elementprop.getProperty("VIEW_NOTES_UNDER_CLAIM_TAGGING_HISTORY_LINKTEXT"));
		tagLabel = driver.findElement(By.xpath(elementprop.getProperty("TAG_LABEL_VIEW_NOTES_XPATH")));
		notesLabel = driver.findElement(By.xpath(elementprop.getProperty("NOTES_LABEL_VIEW_NOTES_XPATH")));
		submitButton = driver.findElement(By.xpath(elementprop.getProperty("SUBMIT_BUTTON_VIEW_NOTES_XPATH")));
		if(tagLabel != null && notesLabel!=null && submitButton!=null){
			report.report("Labels TAG, NOTES $ Submit button are displayed under view notes popup", ReportAttribute.BOLD);
			WebElement notesTextArea = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("NOTES_TEXTAREA_VIEW_NOTES_XPATH"), 30); 
			typeEditBoxByWebElement(notesTextArea, notes);
			clickOnElement(ByLocator.xpath, elementprop.getProperty("SUBMIT_BUTTON_VIEW_NOTES_XPATH"), 30);
			waitForElementVisibility(By.xpath("//table[@id='claimTagsInfoTable']/descendant::td[text()='"+notes+"']"), 30);
			typeEditBoxByWebElement(notesTextArea, "!");
			clickOnElement(ByLocator.xpath, elementprop.getProperty("SUBMIT_BUTTON_VIEW_NOTES_XPATH"), 30);
			if(isTextPresent("Only letters, digits, spaces, new line and _ . , @ $ ? * / - special characters are allowed.")){
				Thread.sleep(5000);
				report.report("User received the validation message Only letters, digits, spaces, new line and _ . , @ $ ? * / - special characters are allowed.",ReportAttribute.BOLD);
				driver.findElement(By.xpath("/html/body/div[7]/div/a/span")).click();
			}
			else{
				failurecount++;
			}
		}
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTagDropdownDisplayingOnlyTagsThoseDidntAddedEarlier() throws Exception {
		int failurecount = 0; List<String> tagsUnderClaimTags = new ArrayList<String>();
		navigateToPage();
		WebElement liveSearchIcon = waitForElementToBeClickable(ByLocator.xpath, elementprop.getProperty("MYDDE_HIC_ICON_XPATH"), 30);
		moveToElement(liveSearchIcon);
		clickLink(elementprop.getProperty("LIVESEARCH_ADVANCED_SEARCH_LINKTEXT"));
		typeEditBox(elementprop.getProperty("AS_MONTHS_AGO_TEXTBOX_NAME"), "26");
		clickButtonV2(elementprop.getProperty("AS_SEARCH_BUTTON_TYPE"));
		Thread.sleep(5000);
		driver.findElement(By.linkText(elementprop.getProperty("SEARCH_RESULTS_VIEW_TAGS_LINKTEXT"))).click();

		List<WebElement> claimTags = driver.findElements(By.xpath("//div[@id='claimTagsId']/div/label"));
		for(WebElement claimTag : claimTags){
			tagsUnderClaimTags.add(claimTag.getText());
		}
		clickButtonV2(elementprop.getProperty("ADD_TAG_BUTTON_TEXT"));
		//		List<WebElement> tagsUnderAddTag = driver.findElements(By.xpath("//select[@id='ClaimTagNameDispInputID']"));
		// pending   ---- selectGetOptions("ClaimTagNameDispInputID");
		return failurecount == 0 ? true : false;
	}

	public boolean verifyTimeFrameOptionsFunctionalityUnderAppealsReport() throws Exception {
		String actualheadertext=""; 
		int failurecount=0;

		navigateToPage();
		setTimeFrame("Overnight");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "OVERNIGHT LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FOR.*, FOR AGENCY.*"))
			failurecount++;

		setTimeFrame("Weekly");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "WEEKLY LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FROM.*, FOR AGENCY.*"))
			failurecount++;

		setTimeFrame("All (up to 18 mos ago)");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FROM.*, FOR AGENCY.*"))
			failurecount++;

		setTimeFrame("FromDate(03/03/2014):ToDate(03/03/2016)");
		actualheadertext = getAppealClaimsreportHeaderMessage();
		if(!Verify.StringMatches(actualheadertext, "LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FROM 03/03/2014 TO 03/03/2016, FOR AGENCY.*"))
			failurecount++;

		return failurecount==0?true:false;

	}

	public boolean verifyMenuOptionsAvailableUnderAppealsReport() throws Exception {
		int failurecount=0;
		navigateToPage();
		if(waitForElementVisibility(By.id("reportHICSearchList"))==null)
		{
			report.report("Unable to find HIC Search Icon", Reporter.WARNING);
			failurecount++;
		}
		if(waitForElementVisibility(By.id("timeframeList"))==null)
		{
			report.report("Unable to find Time Frame List Menu Option", Reporter.WARNING);
			failurecount++;
		}
		if(waitForElementVisibility(By.id("reportAgency"))==null)
		{
			report.report("Unable to find Agency List Menu", Reporter.WARNING);
			failurecount++;
		}

		if(waitForElementVisibility(By.id("reportPrint"))==null)
		{
			report.report("Unable to find Report Print Option", Reporter.WARNING);
			failurecount++;
		}

		if(waitForElementVisibility(By.id("reportExport"))==null)
		{
			report.report("Unable to find Report Export Menu Option", Reporter.WARNING);
			failurecount++;
		}

		return failurecount==0?true:false;				
	}


	public boolean verifyHICSearchOptionUnderAppealsReport() throws Exception{
		int failurecount=0;

		navigateToPage();
		//Verify HIC Search Option
		WebElement reporthicsearch = waitForElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH")));
		if(reporthicsearch==null)
		{
			report.report("HIC Search element not found", Reporter.WARNING);
			return false;
		}

		moveToElement(reporthicsearch);
		typeEditBox("reportHICEntry", "000002936A");
		clickButton("GO");

		Thread.sleep(3000);
		WebElement patientinformationheader = waitForElementVisibility(By.cssSelector(".headergreen"));
		if(patientinformationheader!=null){
			String headertText = patientinformationheader.getText();
			report.report("Verifying that user navigates to Patient Information Screen");
			if(!Verify.StringMatches(headertText, "PATIENT INFORMATION.*")){
				failurecount++;
				report.report("Unable to navigate to Patient Information Screen", Reporter.WARNING);
			}
		}
		else{
			report.report("Unable to navigate to patient information screen with the providerd hic: 000002936A", Reporter.WARNING);
			failurecount++;
		}
		//Verify HIC AdvacedSearch link
		reporthicsearch = waitForElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH")));
		moveToElement(reporthicsearch);
		clickLink("Advanced Search");
		Thread.sleep(3000);
		WebElement advancesearchheader = waitForElementVisibility(By.cssSelector(".headerblue"));
		if(advancesearchheader!=null){
			String headertText1 = advancesearchheader.getText();
			if(!Verify.StringMatches(headertText1, "ADVANCED SEARCH")){
				failurecount++;
				report.report("Unable to navigate to Advanced Search Screen", Reporter.WARNING);
			}

			//Check the Back Navigation
			clickOnElement(ByLocator.xpath, "//a[@id='backNav']", 20);
			String actualheadertext = getAppealClaimsreportHeaderMessage();
			if(!Verify.StringMatches(actualheadertext, "OVERNIGHT LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FOR .*, FOR AGENCY .*"))
			{
				failurecount++;
				report.report("Unable to navigate to Appeal Claims ESMD Report", Reporter.WARNING);
			}
		}
		else
		{
			report.report("Unable to navigate to Advance Search screen", Reporter.WARNING);
			failurecount++;
		}

		return failurecount==0?true:false;
	}

	public boolean verifyAgencyOptionUnderAppealsReport() throws Exception {
		navigateToPage();
		moveToElement("Agency");
		WebElement selectelement = waitForElementVisibility(By.id("reportAgencySelect"));
		if(selectelement!=null){
			Select select = new Select(selectelement);
			select.selectByIndex(0);
			String selectedagency = select.getFirstSelectedOption().getText().trim();
			clickButton("Change Agency");
			String actualheadertext = getAppealClaimsreportHeaderMessage();
			return Verify.StringMatches(actualheadertext, ".*LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT FOR.*, FOR AGENCY "+selectedagency);
		}
		else
		{
			report.report("Agency Select Drop Down not visible", Reporter.WARNING);
			return false;
		}
	}


	public boolean verifyExportOptionsUnderAppealsReport() throws Exception {
		int i=0;
		String[] expectedoptions = {"Save Appeal Submission report to PDF", "Save Appeal Submission report to Excel"};
		String[] actuals;
		navigateToPage();
		moveToElement("Export");
		List<WebElement> lsexportoptions = findElements(By.xpath("//ul[@id='reportExportMenu']/li/a"));
		actuals = new String[lsexportoptions.size()];

		for(WebElement exportoption:lsexportoptions)
			actuals[i++] = new String(exportoption.getText());

		return Verify.verifyArrayofStrings(expectedoptions, actuals, true);
	}


	public boolean verifyClaimTagMultiSelectListBoxinAdnacedSearch() throws Exception {
		List<String> expectedclaimappealtags = new ArrayList<String>();
		List<String> expectedclaimfollowuptags = new ArrayList<String>();

		List<String> actualclaimappealtags = new ArrayList<String>();
		List<String> actualclaimfollowuptags = new ArrayList<String>();

		int i=0;
		navigateToPage();
		WebElement reporthicsearch = waitForElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH")));
		moveToElement(reporthicsearch);
		clickLink("Advanced Search");
		Thread.sleep(3000);
		WebElement selectelement = waitForElementVisibility(By.id("UserClaimAppealTag"));
		Select select = new Select(selectelement);
		List<WebElement> lsclaimtagoptions = select.getOptions();
		report.report("ClaimTags displayed in UI are:");
		for(WebElement claimtag: lsclaimtagoptions){
			if(!claimtag.getText().trim().equalsIgnoreCase("All"))
				actualclaimappealtags.add(claimtag.getText().trim());
		}		

		ResultSet rs = MySQLDBUtil.getResultFromMySQLDB("select TagName from ddez.systemclaimtag where TagCategory='APPEAL'");

		while(rs.next())
			expectedclaimappealtags.add(rs.getString(1).trim());

		if(!Verify.listEquals(expectedclaimappealtags,actualclaimappealtags)){
			report.report("Expected and Actual APPEAL claim Tags are not equal", Reporter.WARNING);
			return false;
		}

		WebElement selectclaimfollowup = waitForElementVisibility(By.id("UserClaimFollowupTag"));
		Select claimfollowup = new Select(selectclaimfollowup);
		List<WebElement> lsclaimfollowuptagoptions = claimfollowup.getOptions();
		report.report("ClaimTags displayed in UI are:");
		for(WebElement claimtag: lsclaimfollowuptagoptions){
			if(!claimtag.getText().trim().equalsIgnoreCase("All"))
				actualclaimfollowuptags.add(claimtag.getText().trim());
		}		

		rs = MySQLDBUtil.getResultFromMySQLDB("select TagName from ddez.systemclaimtag where TagCategory='FOLLOWUP'");
		while(rs.next())
			expectedclaimfollowuptags.add(rs.getString(1).trim());

		if(!Verify.listEquals(actualclaimfollowuptags, expectedclaimfollowuptags)){
			report.report("Expected and Actual APPEAL claim Tags are not equal", Reporter.WARNING);
			return false;
		}
		return true;
	}



	public boolean verifyAddTag(String tagtoadd, String hic) throws Exception {
		navigateToPage();
		navigateToTStatusReport();
		
		WebElement tagelement = waitForElementVisibility(By.xpath("//table[@id='datatable']/tbody//td[a[text()='"+hic+"']]/preceding-sibling::td[2]//img"), 30);
		if(tagelement!=null){
			WebElement tagAgency = waitForElementVisibility(By.xpath("//table[@id='datatable']/tbody//td[a[text()='"+hic+"']]/preceding-sibling::td[1]"));

			//Add Tag and View Tag
			if(tagAgency!=null)
					this.tagAgencytext = tagAgency.getText();

				tagelement.click();
				//wait for the Add Tag Model dialog to visible
				WebElement addtagmodaldialog = waitForElementVisibility(By.xpath("//span[@class='ui-dialog-title' and text()='Add Tag']"), 30);
				if(addtagmodaldialog!=null){
					try{
						WebElement tagsmenu = waitForElementVisibility(By.id("ClaimTagNameDispInputID-button"));
						if(tagsmenu!=null)
							tagsmenu.click();
						//Find all the active list items from the claim tags list box
						List<WebElement> lstags = findElements(By.xpath("//ul[@id='ClaimTagNameDispInputID-menu']//a[not(contains(@aria-disabled,'true'))]"));
						//selecting the option from the list menu whose tag equals to input tagtoadd
						for(int i=0;i<lstags.size();i++){
							WebElement tag = lstags.get(i); 
							if(tag.getText().equalsIgnoreCase(tagtoadd.trim()))
								tag.click();
						}
						typeEditBox("ClaimTagNoteTextId", "Test");
						clickButton("Submit");
					}
					catch(Exception e){
						report.report("Verifying whether modal dialog present. If it's visible closing it");
						WebElement closelink = waitForElementVisibility(By.xpath("//div[contains(@class,'ui-dialog-titlebar')]/a"));
						if(closelink!=null){
							report.report("Modal dialog was found. Hence closing it");
							closelink.click();
						}
					}
				}
				else{
					report.report("Add Tag Modal Dialog not found: ",Reporter.WARNING);
					return false;
				}
			}
		else{
			report.report("Row with Add Tag for specific HIC: "+hic+" was not found in the data table",Reporter.WARNING); //Add Tag which is first available in the datatable and return the tagname
			return false;
		}
		return true;
	}

	public boolean verifyViewTag(String tagname, String hic) throws Exception {
		int failurecount=0;
		if(waitForElementVisibility(By.xpath("//table[@id='datatable']"), 2)==null){
			navigateToPage();
			navigateToTStatusReport();
		}

		WebElement viewtagslink = waitForElementVisibility(By.xpath("//table[@id='datatable']/tbody//td[a[text()='"+hic+"']]/preceding-sibling::td//a[contains(text(),'View Tags')]"), 30);

			if(viewtagslink!=null){
				viewtagslink.click();
				Thread.sleep(5000);
				WebElement claimstagheader = waitForElementVisibility(By.cssSelector(".headergreen"));
				if(claimstagheader!=null)
				{
					//VerifyClaims Tag
					String actualclaimtagname = getElementText(By.xpath("//div[@class='tag']/label[text()='"+tagname.toLowerCase()+"']"));
					if(!Verify.StringEquals(tagname, actualclaimtagname)){
						report.report("Actual and Expected Claim tags are not equal:", Reporter.WARNING);
						failurecount++;
					}

					//VerifyClaimsTag History Table
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");				
					String expecteddate = format.format(date);

					List<WebElement> lsClaimtaghistory = findElements(By.cssSelector("#claimTagHisTable  td"));
					String actualtagname=null;
					String username = null;
					String taggedon=null;
					//Get the data from the First row of claim tags history table
					if(!lsClaimtaghistory.isEmpty()){
						actualtagname = lsClaimtaghistory.get(0).getText().trim();
						username = lsClaimtaghistory.get(1).getText().trim();
						taggedon = lsClaimtaghistory.get(2).getText().trim();

						if(!Verify.StringEquals(tagname.trim(), actualtagname))
						{
							report.report("Expected and actual tagnames are not equal", Reporter.WARNING);
							failurecount++;
						}
						if(!Verify.StringEquals(username, "test.customer"))
						{
							report.report("Expected and actual usernames not equal", Reporter.WARNING);
							failurecount++;
						}
						if(!Verify.StringEquals(taggedon, expecteddate))
						{
							report.report("Expected and actual tagged on dates are not equal", Reporter.WARNING);
							failurecount++;
						}

						clickLink("View Notes");
						try{
							if(waitForElementVisibility(By.xpath("//td[text()='Test']"))==null)
							{
								report.report("View Notes doesn't match with the expected Notes: Test", Reporter.WARNING);
								failurecount++;
							}
						}
						finally{
							WebElement closelink = waitForElementVisibility(By.xpath("//div[contains(@class,'ui-dialog-titlebar')]/a"));
							if(closelink!=null)
								closelink.click();
						}
					}
					else
					{
						report.report("Cliamtag history table not found or doesn't has any data", Reporter.WARNING);
						failurecount++;
					}
				}
				else
				{
					report.report("Unable to navigate to claim tags page", Reporter.WARNING);
					failurecount++;
				}

			}
			else
			{
				report.report("view tags link not found for the Added tag", Reporter.WARNING);
				failurecount++;
			}
		return failurecount==0?true:false;
	}

	public boolean verifyDeleteTag(String tagname, String hic, String expectedalertmessage) throws Exception {
		if(waitForElementVisibility(By.xpath("//table[@id='datatable']"), 2)==null){
			navigateToPage();
			navigateToTStatusReport();
		}
		WebElement viewtagslink = waitForElementVisibility(By.xpath("//table[@id='datatable']/tbody//td[a[text()='"+hic+"']]/preceding-sibling::td//a[contains(text(),'View Tags')]"), 30);

			if(viewtagslink!=null){
				viewtagslink.click();
				Thread.sleep(5000);
				WebElement claimstagheader = waitForElementVisibility(By.cssSelector(".headergreen"));
				if(claimstagheader!=null)
				{
					WebElement deletetaglink = waitForElementVisibility(By.xpath("//label[text()='"+tagname.toLowerCase()+"']/following-sibling::a"));
					if(deletetaglink!=null){
						deletetaglink.click();
						if(!verifyAlert(expectedalertmessage))
						{
							report.report("Unable to delete the tag. Expected alert not found", Reporter.WARNING);
							return false;
						}
					}
					else{
						report.report("Unable to find a tag: "+tagname+"In Claim Tagging Information Page", Reporter.WARNING);
						return false;
					}

				}
				else
				{
					report.report("Unable to navigate to Claims Tag Information Page", Reporter.WARNING);
					return false;
				}
			}
			else{
				report.report("view tags link not found in data table", Reporter.WARNING);
				return false;
			}
		return true;
	}
	

	public boolean sendDocumentToCMS(String hic, String claimIDorDCN, String caseID, String reviewContractorName) throws Exception {
		String sExpectedAlertMessageBeforeADRSubmission = "Proceed with ADR Document Submission to Review Contractor : "+ reviewContractorName +"?";
		String sExpectedAlertMessageAfterSuccessfulADRSubmission = "Successfully processed ADR response documents Submission";
		
		AuditDocHelper helper = new AuditDocHelper();
		navigateToPage();
		String actualheadertext = getAppealClaimsreportHeaderMessage();
		if(actualheadertext!=null && actualheadertext.contains("LEVEL 1 APPEAL CLAIMS ESMD STATUS REPORT")){
			WebElement documentuploadlink = waitForElementVisibility(By.xpath("//td[a[text()='"+hic.trim()+"']]/preceding-sibling::td//a"));
			if(documentuploadlink!=null){
				documentuploadlink.click();
				selectByNameOrID("reviewContractor", reviewContractorName);
				typeEditBox("CMSClaimIDParam", claimIDorDCN);
				typeEditBox("CaseIdParam", caseID);
				
				List<String> filepath = helper.getADRFilePath(ADRFileFomat.PDF, '<');
				helper.uploadFilesAutoIT(filepath);
				clickButtonV2("Send");
				
				if(!verifyAlert(sExpectedAlertMessageBeforeADRSubmission))
				{
					report.report("Expected Alert not found", Reporter.WARNING);
					return false;
				}
				if(!verifyAlertV2(sExpectedAlertMessageAfterSuccessfulADRSubmission))
				{
					report.report("Expected Alert not found", Reporter.WARNING);
					return false;
				}
			}
			else{
				report.report("Unable to find document upload link in Appeal Claim Submission table", Reporter.WARNING);
				return false;
			}
		}
		else
		{
			report.report("Unable to navigate to Appeal Claims Submission page", Reporter.WARNING);
			return false;
		}
		return true;
	}

	/*	public void verifyFieldsInModelDialogAndAddFirstAvailableTag(int firstaddtagrownumber) {

	}*/

/*	int getFirstAddTagRowNumber(String hic){
		int i=1;
		List<WebElement> lsrows = findElements(By.xpath("//table[@id='datatable']/tbody/tr"));
		if(lsrows!=null){
			int rows = lsrows.size(); //Lets get all row numbers	
			//Iterate over all rows and whenever a row with addtag finds return that row number
			for(i=1;i<=rows;i++){
				if(waitForElementVisibility(By.xpath("//table[@id='datatable']/tbody/tr["+i+"]/td[2]//img[contains(@src,'addclaimtag')]"), 2)!=null)
					return i;
			}
		}
		else
			report.report("Search Results data table was not found: ",Reporter.WARNING);

		return -1;
	}*/

	void navigateToTStatusReport() throws Exception{
		WebElement reporthicsearch = waitForElementVisibility(By.id(elementprop.getProperty("REPORT_HIC_SEARCH")));
		moveToElement(reporthicsearch);
		clickLink("Advanced Search");
		selectByNameOrID("SelectedProfileName", "T Status Report");
		/*		WebElement searchbutton = waitForElementVisibility(By.xpath("//input[@value='Search']"));
		if(searchbutton!=null)
			moveToElement(searchbutton);*/
		clickButtonV2("Search");
	}


	/**
	 * Supply the TimeFrame Values like {Overnight, Weekly, All
			(up to 18 mos ago), FromDate(MM/DD/YYYY):ToDate(MM/DD/YYYY)}
	 * @param timeframe
	 * @throws Exception
	 */
	public void setTimeFrame(String timeframe) throws Exception{
		moveToElement(elementprop.getProperty("TIMEFRAME_LIST_MENU"));
		timeframe = timeframe.trim();
		//if the timeframe value starts with fromdate then user is going to set from and todate 
		if (timeframe.toLowerCase().startsWith("fromdate")){
			String fromdate="",todate="";
			String[] dates = timeframe.split(":");
			//getting the from and todate from the user supplied date string say, "fromdate(MM//DD//YYYY):todate(MM//DD//YYYY)"
			if(dates.length>1){
				fromdate = dates[0].substring(dates[0].indexOf("(")+1, dates[0].indexOf(")"));
				todate = dates[1].substring(dates[1].indexOf("(")+1, dates[1].indexOf(")"));;
			}
			/*typeEditBox("reportCustomDateFrom", fromdate);
			typeEditBox("reportCustomDateTo", todate);
			clickButton("reportTimeframeButton");*/
			((JavascriptExecutor) driver).executeScript("$('#reportCustomDateFrom').val('"+fromdate+"');");
			((JavascriptExecutor) driver).executeScript("$('#reportCustomDateTo').val('"+todate+"');");
			((JavascriptExecutor) driver).executeScript("$('#reportTimeframeButton').click();");
		}
		else
			clickLink(timeframe);
	}

	public String getAppealClaimsreportHeaderMessage(){
		//wait for 3 sec for the header to change and appear
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement we = waitForElementVisibility(By.cssSelector(".headerblue"));
		if(we!=null)
			return we.getText();
		return "";
	}

	@Override
	public void assertInPage() {
	}

	@Override
	public void navigateToPage() throws Exception {
		int count=0;
		while(!isTextPresent("LEVEL 1 APPEAL CLAIMS") && count++ < 3){
			HomePage.getInstance().navigateTo(Menu.ESMD, null);
			WebElement appealSubmission = waitForElementToBeClickable(ByLocator.linktext, elementprop.getProperty("APPEAL_SUBMISSION_LINKTEXT"), 30);
			if(appealSubmission != null){
				clickLink(elementprop.getProperty("APPEAL_SUBMISSION_LINKTEXT"));
				Thread.sleep(5000);
			}
		}
	}
}