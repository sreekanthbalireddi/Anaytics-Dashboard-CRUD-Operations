package com.practice.analytics;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebElement;

import com.practice.selenium.DomHelper;

/**
 * Prints Analytics GUI steps to stdout (IDE Run console) for clicks, typing, and hovers
 * used by this project’s page objects.
 */
public final class UiConsoleTrace {

	private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

	private UiConsoleTrace() {
	}

	public static void log(String action) {
		System.out.println("[Analytics UI " + LocalTime.now().format(TIME) + "] " + action);
	}

	public static void click(WebElement element, String description) throws Exception {
		log("Click → " + description);
		DomHelper.click(element);
	}

	public static void clickElement(WebElement element, String description) {
		log("Click → " + description);
		element.click();
	}

	public static void moveTo(WebElement element, String description) throws Exception {
		log("Move to → " + description);
		DomHelper.moveToElement(element);
	}

	public static void type(WebElement element, String text, String description) {
		log("Type → " + description + ": \"" + text + "\"");
		element.sendKeys(text);
	}
}
