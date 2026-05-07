package com.practice.selenium;

import com.xactly.xcommons.app.LoginToApplication;

/**
 * Application login flow (loads env properties and drives the Incent shell).
 */
public final class ApplicationLogin {

	private final LoginToApplication delegate = new LoginToApplication();

	public static void setGrs(String grs) {
		LoginToApplication.setGrs(grs);
	}

	public static void setUseIam(String useIam) {
		LoginToApplication.setUseIAM(useIam);
	}

	public static String loginUrl() {
		return LoginToApplication.getUrl();
	}

	public static String guiUsername() {
		return LoginToApplication.getReloadusername();
	}

	public static String guiPassword() {
		return LoginToApplication.getReloadpassword();
	}

	public void loadEnvironmentProperties(String environment, String application) throws Exception {
		delegate.setProperties(environment, application);
	}

	public void loginWithGuiUserKey(String environment, String usersPropertyKey) throws Exception {
		delegate.reloadloginToIncent(environment, usersPropertyKey);
	}

	public void openModule(String moduleLabel) throws Exception {
		delegate.switchToModule(moduleLabel);
	}
}
