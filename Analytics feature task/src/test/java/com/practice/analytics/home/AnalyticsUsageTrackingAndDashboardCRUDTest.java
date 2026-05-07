package com.practice.analytics.home;

import java.util.concurrent.TimeUnit;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.practice.analytics.administration.UsageTracking;
import com.practice.selenium.ApplicationLogin;
import com.practice.selenium.AnalyticsUi;
import com.practice.selenium.BrowserSession;
import com.practice.selenium.DomHelper;
import com.practice.selenium.ModuleNames;
import com.practice.selenium.Priority;
import com.practice.selenium.PriorityInterceptor;
import com.practice.selenium.TestEnv;

/**
 * End-to-end: log into Incent, open Analytics, open Administration &gt; Usage Tracking,
 * then create / rename / delete a custom dashboard on Business Dashboards.
 * <p>
 * Credentials: {@code analytics.gui.user} in
 * {@code src/main/resources/environment/ociqaintx/ociqaintx_users.properties} for the default env.
 */
@Listeners({ PriorityInterceptor.class })
@Priority(100)
public class AnalyticsUsageTrackingAndDashboardCRUDTest {

	private String dashboardName;
	private String dashboardRenamedName;

	@Parameters({ "environment", "application", "mode", "browser", "grs", "navigationType" })
	@BeforeClass(alwaysRun = true)
	public void loginToIncentAndOpenAnalytics(@Optional String environment, @Optional String application,
			@Optional String mode, @Optional String browser, @Optional String grs, @Optional String navigationType)
			throws Exception {
		if (environment == null || environment.isEmpty()) {
			environment = "ociqaintx";
		}
		if (application == null || application.isEmpty()) {
			application = "incent";
		}
		ApplicationLogin.setGrs(grs != null ? grs : "off");
		ApplicationLogin.setUseIam("no");

		TestEnv.setEnvironment(environment);
		TestEnv.setApplication(application);
		TestEnv.setMode(mode != null ? mode : "gui");

		BrowserSession.setBrowser(browser != null ? browser : "chrome");
		BrowserSession.setNavigationType(navigationType != null ? navigationType : "gui-new");

		ApplicationLogin login = new ApplicationLogin();
		login.loadEnvironmentProperties(environment, application);

		DomHelper.createDownloadDir();

		BrowserSession.start();

		login.loginWithGuiUserKey(environment, "analytics.gui.user");

		String loginContext = String.format(
				"Incent login — URL: %s | Username: %s | Password: %s",
				ApplicationLogin.loginUrl(),
				ApplicationLogin.guiUsername(),
				ApplicationLogin.guiPassword());
		System.out.println(loginContext);
		Reporter.log(loginContext, true);

		login.openModule(ModuleNames.ANALYTICS_BETA);
		AnalyticsUi.acceptAlert();
		BrowserSession.driver().manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		Thread.sleep(10000);
		Reporter.log("Logged in to Incent and opened Analytics.", true);
	}

	@Test(priority = 1, groups = { "analyticsUsageTracking" }, description = "Administration > Usage Tracking — load and validate")
	public void openUsageTracking() throws Exception {
		UsageTracking usage = new UsageTracking("gui");
		usage.loadUsageTracking();
		Reporter.log("Usage Tracking loaded.", true);
	}

	@Test(priority = 2, groups = { "analyticsDashboards", "createDashboard" }, description = "Create a custom Analytics dashboard", dependsOnMethods = "openUsageTracking")
	public void createDashboard() throws Exception {
		SoftAssert sa = new SoftAssert();
		dashboardName = DomHelper.uniqueId() + "_Dashboard";
		Home home = new Home("gui");
		home.createDashboard(dashboardName);
		sa.assertTrue(home.isDashboardExist(dashboardName), "Dashboard creation failed: " + dashboardName);
		sa.assertAll();
		Reporter.log("Dashboard created: " + dashboardName, true);
	}

	@Test(priority = 3, groups = { "analyticsDashboards", "renameDashboard" }, description = "Rename the custom dashboard", dependsOnMethods = "createDashboard")
	public void renameDashboard() throws Exception {
		SoftAssert sa = new SoftAssert();
		dashboardRenamedName = dashboardName + "_Renamed";
		Home home = new Home("gui");
		home.waitUntilCreateDashboardEnabled();
		home.clickDashboard(dashboardName);
		home.renameDashboard(dashboardName, dashboardRenamedName);
		sa.assertTrue(home.isDashboardExist(dashboardRenamedName), "Dashboard rename failed: " + dashboardRenamedName);
		sa.assertAll();
		Reporter.log("Dashboard renamed to: " + dashboardRenamedName, true);
	}

	@Test(priority = 4, groups = { "analyticsDashboards", "deleteDashboard" }, description = "Delete the custom dashboard", dependsOnMethods = "renameDashboard")
	public void deleteDashboard() throws Exception {
		SoftAssert sa = new SoftAssert();
		Home home = new Home("gui");
		home.deleteDashboard(dashboardRenamedName);
		sa.assertFalse(home.isDashboardExist(dashboardRenamedName), "Dashboard deletion failed: " + dashboardRenamedName + " still exists");
		sa.assertAll();
		Reporter.log("Dashboard deleted: " + dashboardRenamedName, true);
	}
}
