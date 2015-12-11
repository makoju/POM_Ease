package com.ability.ease.eligibility;

import java.util.List;
import java.util.Map;

import com.ability.ease.auto.common.TestCommonResource;
import com.ability.ease.auto.common.UIActions;
import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.UIAttributeXMLParser;
import com.ability.ease.auto.dataStructure.common.easeScreens.Attribute;
import com.ability.ease.selenium.webdriver.AbstractPageObject;

public class EligibilityPage extends AbstractPageObject{


	public boolean verifyEligibility(Map<String, String> mapAttrVal)throws Exception{

		UIAttributeXMLParser parser = UIAttributeXMLParser.getInstance();
		List<Attribute>	lsAttributes = parser.getUIAttributesFromXMLV2
				(TestCommonResource.getTestResoucresDirPath()+"uiattributesxml\\Eligibility\\Eligibility.xml", mapAttrVal);
		UIActions eligibility = new UIActions();
		eligibility.fillScreenAttributes(lsAttributes);
		return false;
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
