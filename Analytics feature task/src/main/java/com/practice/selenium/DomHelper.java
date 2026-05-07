package com.practice.selenium;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.xactly.xcommons.selenium.SeleniumHelperClass;

/**
 * DOM and browser helpers (delegates to the bundled automation support library).
 */
public final class DomHelper {

	private DomHelper() {
	}

	public static void createDownloadDir() throws IOException {
		SeleniumHelperClass.createDownloadDir();
	}

	public static int speedLimitMaxMs() {
		return SeleniumHelperClass.system_SpeedlimitMAX;
	}

	public static int speedLimitMinMs() {
		return SeleniumHelperClass.system_SpeedlimitMIN;
	}

	public static void setTimeoutSeconds(int seconds) {
		SeleniumHelperClass.timeout = seconds;
	}

	public static WebElement findById(String id) throws Exception {
		return SeleniumHelperClass.findWebElementbyid(id);
	}

	public static WebElement findById(String id, String frameContext) throws Exception {
		return SeleniumHelperClass.findWebElementbyid(id, frameContext);
	}

	public static WebElement findByXpath(String xpath, String frameContext) throws Exception {
		return SeleniumHelperClass.findWebElementbyXpath(xpath, frameContext);
	}

	public static boolean waitForElementReady(WebElement element) {
		return SeleniumHelperClass.waitForElmentToBeReady(element);
	}

	public static void clearElement(WebElement element) {
		SeleniumHelperClass.clearElement(element);
	}

	public static WebElement findByTagName(String tag, String frameContext) throws Exception {
		return SeleniumHelperClass.findWebElementbyTagName(tag, frameContext);
	}

	public static List<WebElement> findElements(String xpath, String frameContext) throws Exception {
		return SeleniumHelperClass.findWebElements(xpath, frameContext);
	}

	public static void switchToDefaultContent() {
		SeleniumHelperClass.switchToDefaultContent();
	}

	public static void acceptAlert() {
		SeleniumHelperClass.acceptAlert();
	}

	public static void click(WebElement element) {
		SeleniumHelperClass.click(element);
	}

	public static void moveToElement(WebElement element) {
		SeleniumHelperClass.moveToElement(element);
	}

	public static String uniqueId() {
		return SeleniumHelperClass.setUniqueId();
	}
}
