package com.practice.selenium;

import com.xactly.xcommons.selenium.AnalyticsHelperClass;

/**
 * Waits and lightweight Analytics-shell helpers.
 */
public final class AnalyticsUi {

	private AnalyticsUi() {
	}

	public static void acceptAlert() {
		AnalyticsHelperClass.acceptAlert();
	}

	public static void waitForPageLoad() throws InterruptedException {
		AnalyticsHelperClass.waitForPageLoad();
	}

	public static boolean waitForSearchSpinner() throws Exception {
		return AnalyticsHelperClass.waitForSearchImg();
	}

	public static int waitTimeoutSeconds() {
		return AnalyticsHelperClass.timeout;
	}

	public static int pollingIntervalSeconds() {
		return AnalyticsHelperClass.pollingtime;
	}
}
