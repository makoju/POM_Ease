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

	public static enum ClaimStatusType{
		Any("Any"),
		Denied("Denied"),
		Rejected("Rejected"),
		RTP("RTP"),
		Suspense("Suspense"),
		Paid("Paid"),
		NotPaid("Not Paid");

		private String value;

		private ClaimStatusType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	};

	public static enum ADRFileFomat{
		PDF("PDF"),
		TIFF("TIFF"),
		TIF("TIF"),
		PDFandTIFF("PDFandTIFF");	
		
		private String value;
		private ADRFileFomat(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	};
	
	public static enum ADRFilesSize{
		LessThan35MB("<35MB"),
		GreaterThan35MB(">35MB");
		
		private String value;
		private ADRFilesSize(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	};
	
	
	public static enum ADRSubType{
		RESEND("RESEND"),
		REJECT("REJECT");
		
		private String value;
		private ADRSubType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	};


}
