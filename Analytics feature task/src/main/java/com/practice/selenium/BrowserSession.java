package com.practice.selenium;

import org.openqa.selenium.WebDriver;

import com.xactly.xcommons.selenium.SetWebDrivers;

/**
 * WebDriver lifecycle for GUI tests.
 */
public final class BrowserSession {

	private BrowserSession() {
	}

	public static void setBrowser(String name) {
		SetWebDrivers.setBrowser(name);
	}

	public static void setNavigationType(String type) {
		SetWebDrivers.setNavigationType(type);
	}

	public static WebDriver driver() {
		return SetWebDrivers.getDriver();
	}

	/** Starts the browser using configured {@link #setBrowser(String)} / navigation type. */
	public static void start() throws Exception {
		new SetWebDrivers();
	}
}
