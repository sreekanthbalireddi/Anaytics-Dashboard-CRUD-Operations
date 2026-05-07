package com.practice.analytics.home;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.google.common.base.Function;
import com.practice.analytics.UiConsoleTrace;
import com.practice.selenium.AnalyticsUi;
import com.practice.selenium.BrowserSession;
import com.practice.selenium.DomHelper;

/**
 * Analytics Business Dashboards: create / rename / delete custom dashboards.
 */
public class Home {

	public Home(String testtype) throws Exception {
		if (testtype.equalsIgnoreCase("gui")) {
			Thread.sleep(DomHelper.speedLimitMaxMs());
			BrowserSession.driver().switchTo().defaultContent();
			AnalyticsUi.waitForPageLoad();
			UiConsoleTrace.click(DomHelper.findById("TAB_MIDDLE_ADVANCED_ANALYTICS_DASHBOARDS_TAB", "default"),
					"Business Dashboards tab (top navigation)");
		}
	}

	public WebElement get_obj_create() throws Exception {
		return DomHelper.findByXpath(".//*[@labelid='CREATE_DASHBOARD1']", "iframecontent");
	}

	public WebElement get_obj_rename() throws Exception {
		WebElement rename = DomHelper.findByXpath(".//*[@labelid='EDIT_DASHBOARD1']", "iframecontent");
		DomHelper.waitForElementReady(rename);
		return rename;
	}

	public WebElement get_obj_delete() throws Exception {
		return DomHelper.findByXpath(".//*[@labelid='DELETE_DASHBOARD1']", "iframecontent");
	}

	public WebElement get_field_dashboard() throws Exception {
		return DomHelper.findByXpath(".//*[@id='dashboardName']", "iframeContent");
	}

	public WebElement get_obj_ok() throws Exception {
		return DomHelper.findByXpath(".//*[@id='ok']", "iframeContent");
	}

	public WebElement get_obj_delete_btn() throws Exception {
		return DomHelper.findByXpath(".//*[@id='delete']", "iframeContent");
	}

	public WebElement get_obj_create_span() throws Exception {
		return DomHelper.findByXpath(".//*[@action='create']/div[@class='k-top']/span", "iframecontent");
	}

	public WebElement get_obj_delete_span() throws Exception {
		return DomHelper.findByXpath(".//*[@action='delete']/div[@class='k-bot']/span", "iframecontent");
	}

	public WebElement get_obj_rename_span() throws Exception {
		return DomHelper.findByXpath(".//*[@action='update']/div[@class='k-mid']/span", "iframecontent");
	}

	public void createDashboard(String name) throws Exception {
		@SuppressWarnings("unused")
		SoftAssert Assert = new SoftAssert();
		waitUntilCreateDashboardEnabled();
		UiConsoleTrace.click(get_obj_create(), "Create dashboard (toolbar)");

		UiConsoleTrace.type(get_field_dashboard(), name, "Dashboard name (create modal)");
		UiConsoleTrace.click(get_obj_ok(), "OK — save new dashboard");
		AnalyticsUi.waitForPageLoad();
		waitUntilDashboardListed(name);
	}

	public void renameDashboard(String oldName, String newName) throws Exception {
		@SuppressWarnings("unused")
		SoftAssert Assert = new SoftAssert();
		AnalyticsUi.waitForPageLoad();
		waitUntilRenameDashboardEnabled();
		UiConsoleTrace.click(get_obj_rename(), "Edit / rename dashboard (toolbar)");
		Thread.sleep(DomHelper.speedLimitMaxMs());
		UiConsoleTrace.log("Clear dashboard name field (rename modal)");
		DomHelper.clearElement(get_field_dashboard());
		Thread.sleep(DomHelper.speedLimitMinMs());
		UiConsoleTrace.type(get_field_dashboard(), newName, "Dashboard name (rename modal)");
		Thread.sleep(DomHelper.speedLimitMinMs());
		UiConsoleTrace.click(get_obj_ok(), "OK — save renamed dashboard");
		AnalyticsUi.waitForPageLoad();
		waitUntilDashboardListed(newName);
	}

	public void deleteDashboard(String name) throws Exception {
		@SuppressWarnings("unused")
		SoftAssert Assert = new SoftAssert();
		waitUntilCreateDashboardEnabled();
		Thread.sleep(DomHelper.speedLimitMaxMs());
		clickDashboard(name);
		AnalyticsUi.waitForPageLoad();
		waitUntilDeleteDashboardEnabled();
		UiConsoleTrace.click(get_obj_delete(), "Delete dashboard (toolbar)");
		Thread.sleep(DomHelper.speedLimitMaxMs());
		UiConsoleTrace.click(get_obj_delete_btn(), "Delete — confirm in modal");
		AnalyticsUi.waitForPageLoad();
		waitUntilDashboardRemoved(name);
	}

	/** Waits until the dashboard no longer appears in the tree (after delete). */
	public void waitUntilDashboardRemoved(String name) throws Exception {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(BrowserSession.driver())
				.withTimeout(Duration.ofSeconds(AnalyticsUi.waitTimeoutSeconds()))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);

		wait.until(driver -> {
			try {
				return !dashboardTextMatches(name);
			} catch (Exception e) {
				return false;
			}
		});
	}

	public void waitUntilCreateDashboardEnabled() {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(BrowserSession.driver())
				.withTimeout(Duration.ofSeconds(AnalyticsUi.waitTimeoutSeconds()))
				.pollingEvery(Duration.ofSeconds(AnalyticsUi.pollingIntervalSeconds()))
				.ignoring(NoSuchElementException.class);

		Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = null;
				try {
					element = get_obj_create_span();
				} catch (Exception e) {
					UiConsoleTrace.log("Wait Create Dashboard: toolbar span not visible yet");
					e.printStackTrace();
					return false;
				}
				String className = element.getAttribute("className");
				if (className.contains("k-state-disabled")) {
					UiConsoleTrace.log("Wait Create Dashboard: still disabled (" + element.getText() + ")");
					return false;
				} else
					return true;
			}

			public <V> java.util.function.Function<V, Boolean> compose(
					java.util.function.Function<? super V, ? extends WebDriver> before) {
				return null;
			}

			public <V> java.util.function.Function<WebDriver, V> andThen(
					java.util.function.Function<? super Boolean, ? extends V> after) {
				return null;
			}

			public <T> java.util.function.Function<T, T> identity() {
				return null;
			}
		};
		wait.until(f);
	}

	public void waitUntilDeleteDashboardEnabled() {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(BrowserSession.driver())
				.withTimeout(Duration.ofSeconds(AnalyticsUi.waitTimeoutSeconds()))
				.pollingEvery(Duration.ofSeconds(AnalyticsUi.pollingIntervalSeconds()))
				.ignoring(NoSuchElementException.class);

		Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = null;
				try {
					element = get_obj_delete_span();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String className = element.getAttribute("className");
				if (className.contains("k-state-disabled")) {
					return false;
				} else
					return true;
			}

			public <V> java.util.function.Function<V, Boolean> compose(
					java.util.function.Function<? super V, ? extends WebDriver> before) {
				return null;
			}

			public <V> java.util.function.Function<WebDriver, V> andThen(
					java.util.function.Function<? super Boolean, ? extends V> after) {
				return null;
			}

			public <T> java.util.function.Function<T, T> identity() {
				return null;
			}
		};
		wait.until(f);
	}

	public void waitUntilRenameDashboardEnabled() {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(BrowserSession.driver())
				.withTimeout(Duration.ofSeconds(AnalyticsUi.waitTimeoutSeconds()))
				.pollingEvery(Duration.ofSeconds(AnalyticsUi.pollingIntervalSeconds()))
				.ignoring(NoSuchElementException.class);

		Function<WebDriver, Boolean> f = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				WebElement element = null;
				try {
					element = get_obj_rename_span();
				} catch (Exception e) {
					e.printStackTrace();
				}
				String className = element.getAttribute("className");
				if (className.contains("k-state-disabled")) {
					return false;
				} else
					return true;
			}

			public <V> java.util.function.Function<V, Boolean> compose(
					java.util.function.Function<? super V, ? extends WebDriver> before) {
				return null;
			}

			public <V> java.util.function.Function<WebDriver, V> andThen(
					java.util.function.Function<? super Boolean, ? extends V> after) {
				return null;
			}

			public <T> java.util.function.Function<T, T> identity() {
				return null;
			}
		};
		wait.until(f);
	}

	public boolean isDashboardExist(String name) throws Exception {
		boolean found = dashboardTextMatches(name);
		if (found) {
			Reporter.log("Dashboard " + name + " is found", 2, true);
		} else {
			Reporter.log("Dashboard " + name + " is not found", 2, true);
		}
		return found;
	}

	private boolean dashboardTextMatches(String name) throws Exception {
		String normalized = normalizeDashboardLabel(name);
		List<WebElement> ele = DomHelper.findElements(".//*[@change='true']", "iframecontent");
		for (WebElement e : ele) {
			if (normalized.equals(normalizeDashboardLabel(e.getText()))) {
				return true;
			}
		}
		return false;
	}

	private static String normalizeDashboardLabel(String raw) {
		if (raw == null) {
			return "";
		}
		return raw.replace('\u00a0', ' ').trim().replaceAll("\\s+", " ");
	}

	/** Waits for the Business Dashboards tree to show the name after create/rename (UI can lag behind the modal close). */
	public void waitUntilDashboardListed(String name) throws Exception {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(BrowserSession.driver())
				.withTimeout(Duration.ofSeconds(AnalyticsUi.waitTimeoutSeconds()))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class);

		wait.until(driver -> {
			try {
				return dashboardTextMatches(name);
			} catch (Exception e) {
				return false;
			}
		});
	}

	public void clickDashboard(String name) throws Exception {

		UiConsoleTrace.log("Select dashboard in tree: \"" + name + "\"");
		WebElement dashboard = DomHelper.findByXpath("//span[@labelid='" + name + "']", "iframecontent");
		UiConsoleTrace.moveTo(dashboard, "Dashboard row (hover before select)");
		UiConsoleTrace.click(dashboard, "Dashboard row — select");
	}
}
