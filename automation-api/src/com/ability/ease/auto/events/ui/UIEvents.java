package com.ability.ease.auto.events.ui;

import java.util.Observable;

public class UIEvents extends Observable{
	
	private static UIEvents testInstance = null;
	
	public static UIEvents getInstance() {
		if (testInstance == null) {
			testInstance = new UIEvents();
		}
		return testInstance;
	}
	
	
	public void closeBrowser() {
		setChanged();
		notifyObservers("close");
	}
	
	public void switchEnvironment(String envName, String port, String host) {
		setChanged();
		notifyObservers();
	}
}