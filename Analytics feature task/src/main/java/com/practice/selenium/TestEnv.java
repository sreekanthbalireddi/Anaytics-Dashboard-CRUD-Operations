package com.practice.selenium;

import com.xactly.xcommons.presetup.PreSetup;

/**
 * Test suite environment (environment name, app, mode).
 */
public final class TestEnv {

	private TestEnv() {
	}

	public static void setEnvironment(String environment) {
		PreSetup.setEnvironment(environment);
	}

	public static void setApplication(String application) {
		PreSetup.setApplication(application);
	}

	public static void setMode(String mode) {
		PreSetup.setMode(mode);
	}
}
