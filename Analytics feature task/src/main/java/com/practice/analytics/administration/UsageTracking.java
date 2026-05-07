package com.practice.analytics.administration;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.practice.analytics.UiConsoleTrace;
import com.practice.selenium.AnalyticsUi;
import com.practice.selenium.DomHelper;

public class UsageTracking {

	public static ArrayList<ArrayList> usageTrackingBefore;
	public static ArrayList<ArrayList> usageTrackingAfter;

	public UsageTracking(String testtype) throws Exception {
		if (testtype.equalsIgnoreCase("gui")) {
			DomHelper.setTimeoutSeconds(120);
			new AdministrationTab(testtype);
			UiConsoleTrace.clickElement(
					DomHelper.findById("A_ADVANCED_ANALYTICS_ADMINISTRATION_TAB_USAGE_TRACKING"),
					"Usage Tracking (Administration sidebar)");
			get_dashboardoption_icon();
		}
	}

	public WebElement get_dashboardoption_icon() throws Exception {
		return DomHelper.findById("uberBar_dashboardpageoptions", "iframecontent");
	}

	public WebElement get_refresh_option() throws Exception {
		return DomHelper.findById("idPageRefresh", "iframecontent");
	}

	public WebElement get_body_obj() throws Exception {
		return DomHelper.findByTagName("body", "iframecontent");
	}

	public List<WebElement> get_usernames() throws Exception {
		return DomHelper.findElements("//*[contains(@class,'PTRHC0')]", "iframecontent");
	}

	public List<WebElement> get_query_count() throws Exception {
		return DomHelper.findElements(".//*[contains(@class,'PTDC')]", "iframecontent");
	}

	public void clickRefresh() throws Exception {

		UiConsoleTrace.clickElement(get_dashboardoption_icon(), "Dashboard page options (gear / menu)");
		Thread.sleep(2000);
		UiConsoleTrace.clickElement(get_refresh_option(), "Refresh page (from options menu)");
		UiConsoleTrace.log("Accept confirmation alert (after refresh)");
		DomHelper.acceptAlert();
		UiConsoleTrace.log("Wait: page busy / search indicator (waitForSearchImg)");
		AnalyticsUi.waitForSearchSpinner();
	}

	public String getUsageTrackingBodyText() throws Exception {
		clickRefresh();
		Thread.sleep(1000);
		String bodyText = get_body_obj().getText();
		return bodyText;

	}

	public void loadUsageTracking() throws Exception {

		UiConsoleTrace.log("Load Usage Tracking: refresh grid and validate report sections");
		String content = getUsageTrackingBodyText();

		if (content.contains("View Display Error")) {
			Reporter.log("View Display Error", true);
			throw new Exception("View Display Error");
		}
		if (content.contains("Error 500--Internal Server Error")) {
			Reporter.log("Error 500--Internal Server Error", true);
			throw new Exception("Error 500--Internal Server Error");

		}
		if (content.contains("No Results")) {
			Reporter.log("Contains reports with No Results", true);
		}
		UsageTracking.usageTrackingBefore = fetchUsageTrackingValues();
		if (UsageTracking.usageTrackingBefore != null)
			Reporter.log("Usage Tracking :" + UsageTracking.usageTrackingBefore.toString());
		else
			Reporter.log("Usage Tracking :" + "<No Value>");
		if (isUsageTrackingLoaded(content)) {
			Reporter.log("Usage Tracking is loaded successfully", true);

		} else {
			String reportsNotFound = getMissingReports(content);
			Reporter.log("Usage Tracking loading is failed,Failed to load " + reportsNotFound, true);
			throw new Exception("Failed to load Reports " + reportsNotFound);

		}

	}

	public void verifyUsageTracking() throws Exception {

		String content = getUsageTrackingBodyText();

		if (content.contains("View Display Error")) {
			Reporter.log("View Display Error", true);
			throw new Exception("View Display Error");
		}
		if (content.contains("Error 500--Internal Server Error")) {
			Reporter.log("Error 500--Internal Server Error", true);
			throw new Exception("Error 500--Internal Server Error");

		}
		if (content.contains("No Results")) {
			Reporter.log("Contains reports with No Results", true);
		}
		UsageTracking.usageTrackingAfter = fetchUsageTrackingValues();
		Reporter.log(" ", true);
		if (UsageTracking.usageTrackingBefore != null)
			Reporter.log("Usage Tracking Before:" + UsageTracking.usageTrackingBefore.toString());
		else
			Reporter.log("Usage Tracking Before:" + "<No Value>");
		Reporter.log("  Usage Tracking After:" + UsageTracking.usageTrackingAfter.toString(), true);
		if (!UsageTracking.compareUsageTrackingValues(UsageTracking.usageTrackingBefore, UsageTracking.usageTrackingAfter))
			throw new Exception("Query Count is not updated");
		if (isUsageTrackingLoaded(content)) {
			Reporter.log("Usage Tracking is loaded and updated successfully", true);

		} else {
			String reportsNotFound = getMissingReports(content);
			Reporter.log("Usage Tracking loading is failed,Failed to load " + reportsNotFound, true);
			throw new Exception("Failed to load Reports " + reportsNotFound);

		}
	}

	public boolean isUsageTrackingLoaded(String bodyText) {
		boolean loaded = false;
		if (bodyText.contains("Reports by Hour") && bodyText.contains("Report Count by Day of Week")
				&& bodyText.contains("Reports by User")) {
			loaded = true;
		} else {
			loaded = false;
		}
		return loaded;
	}

	public String getMissingReports(String bodyText) {
		String text = "";
		if (!bodyText.contains("Reports by Hour"))
			text += "<Reports by Hour> ";
		if (!bodyText.contains("Report Count by Day of Week"))
			text += "<Report Count by Day of Week> ";
		if (!bodyText.contains("Reports by User"))
			text += "<Reports by User>";

		return text;
	}

	public ArrayList<ArrayList> fetchUsageTrackingValues() {

		ArrayList<ArrayList> ar = new ArrayList<ArrayList>();
		ArrayList<String> row;
		int index = 0;
		try {
			Thread.sleep(1000);
			List<WebElement> col1 = get_usernames();
			List<WebElement> col2 = get_query_count();
			for (index = 0; index < col1.size(); index++) {
				row = new ArrayList<String>();
				row.add(col1.get(index).getText());
				row.add(col2.get(index).getText());
				ar.add(row);
			}
		} catch (Exception e) {
			Reporter.log("" + e, true);
		}
		Reporter.log("Total " + index + " Records are visible in the report 'Reports by User' in Usage tracking");

		return ar;
	}

	public static boolean compareUsageTrackingValues(ArrayList<ArrayList> before, ArrayList<ArrayList> after) {
		boolean status = false;
		if (after.size() > before.size()) {
			Reporter.log("Number of records updated from  " + before.size() + " to  " + after.size());
			status = true;
		} else if (after.size() == before.size()) {
			for (int i = 0; i < before.size(); i++) {
				ArrayList<String> bf = before.get(i);
				for (int j = 0; j < after.size(); j++) {
					ArrayList<String> af = after.get(j);
					if (bf.get(0).equals(af.get(0))) {
						if (!bf.get(1).equals(af.get(1))) {
							Reporter.log("Query count is updated from '" + bf.get(1) + "' to '" + af.get(1) + "' for the user '"
									+ af.get(0) + "'", true);
							status = true;
						}
					}
				}
			}
		}

		return status;

	}

}
