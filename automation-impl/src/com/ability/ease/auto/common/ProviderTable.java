package com.ability.ease.auto.common;


public class ProviderTable {
	
		protected String sName;
		protected String sIntermediary;
		protected String sProviderID;
		protected String sAddress;
		protected String sCity;
		protected String sState;
		protected String sZip;
		protected String sPhone;

		//Constructor
		public ProviderTable(){
			sName = null;
			sIntermediary = null;
			sProviderID = null;
			sAddress = null;
			sCity = null;
			sState = null;
			sZip = null;
			sPhone = null;
		}
		
		
		//Getters and setters
		

		public String getsName() {
			return sName;
		}

		public void setsName(String sName) {
			this.sName = sName;
		}

		public String getsIntermediary() {
			return sIntermediary;
		}

		public void setsIntermediary(String sIntermediary) {
			this.sIntermediary = sIntermediary;
		}

		public String getsProviderID() {
			return sProviderID;
		}

		public void setsProviderID(String sProviderID) {
			this.sProviderID = sProviderID;
		}

		public String getsAddress() {
			return sAddress;
		}

		public void setsAddress(String sAddress) {
			this.sAddress = sAddress;
		}

		public String getsCity() {
			return sCity;
		}

		public void setsCity(String sCity) {
			this.sCity = sCity;
		}

		public String getsState() {
			return sState;
		}

		public void setsState(String sState) {
			this.sState = sState;
		}

		public String getsZip() {
			return sZip;
		}

		public void setsZip(String sZip) {
			this.sZip = sZip;
		}

		public String getsPhone() {
			return sPhone;
		}

		public void setsPhone(String sPhone) {
			this.sPhone = sPhone;
		}


}
