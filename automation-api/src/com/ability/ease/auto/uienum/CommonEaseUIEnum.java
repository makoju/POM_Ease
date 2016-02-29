package com.ability.ease.auto.uienum;

public class CommonEaseUIEnum {

	public enum UIAttributeStyle {
		Text("Text"),
		Table("Table"),
		DropDown("DropDown"),
		CheckBox("CheckBox"),
		Button("Button"),
		Submit("Submit"),
		Link("Link"),
		SetupAlert("SetupAlert"),
		Image("Image"),
		DropDownList("DropDownList"),
		TextArea("TextArea"),
		LinkWithNoText("LinkWithNoText"),
		ClaimLineUB04("ClaimLineUB04"),
		ConditionOccurruenceCodes("ConditionOccurruenceCodes"),
		SpanCodes("SpanCodes"),
		FillLocatorValues("FillLocatorValues"),
		FillValuesUB04("FillValuesUB04"),
		Timeframe("Timeframe"),
		Agency("Agency"),
		MultiSelectAgency("MultiSelectAgency"),
		ServiceDate("ServiceDate"),
		CustomSchedule("CustomSchedule");
		private String value;
		private UIAttributeStyle(String value){
			this.value = value;
		}
		public String getValue(){
			return value;
		}
	}
}