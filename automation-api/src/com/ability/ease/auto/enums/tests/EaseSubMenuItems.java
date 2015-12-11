package com.ability.ease.auto.enums.tests;

public class EaseSubMenuItems {

	public static enum MyAccountSubMenu {

		PersonalInformation("Personal Information"),
		ChangePassword("Change Password"),
		SetupAlerts("Setup Alerts"),
		ChangeSchedule("Change Schedule"),
		ChangeFISSDDESettings("Change FISS/DDE Settings"),
		DisableEase("Disable Ease"),
		ResumeEase("Resume Ease"),
		EASEDashboard("EASE Dashboard");
		private String value;

		private MyAccountSubMenu(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	};

	public static enum UserActionType {

		Read("Read"),
		Modify("Modify");
		private String value;

		private UserActionType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	};

}
