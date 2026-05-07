package com.practice.analytics.administration;

import org.openqa.selenium.WebElement;

import com.practice.analytics.UiConsoleTrace;
import com.practice.selenium.AnalyticsUi;
import com.practice.selenium.DomHelper;

public class AdministrationTab {

	public AdministrationTab(String testtype) throws Exception {

		if (testtype.equalsIgnoreCase("gui")) {
			AnalyticsUi.waitForPageLoad();
			UiConsoleTrace.log("Navigation: switch to default content (before Administration tab)");
			DomHelper.switchToDefaultContent();
			WebElement tab_setup = DomHelper.findById("TAB_MIDDLE_ADVANCED_ANALYTICS_ADMINISTRATION_TAB", "none");
			DomHelper.waitForElementReady(tab_setup);
			UiConsoleTrace.click(tab_setup, "Administration tab (top navigation)");
		}
	}

}
