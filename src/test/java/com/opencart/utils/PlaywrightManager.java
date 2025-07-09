package com.opencart.utils;

import com.microsoft.playwright.*;

import java.util.*;

public class PlaywrightManager {

    public static Browser browser;
    private static Page page;
    //private static Browser browser;
    public static BrowserContext context;
    private static Map<Integer, BrowserContext> contexts = new HashMap<>();
    private static Map<Integer, Page> pages = new HashMap<>();
    private static int currentInstanceId = -1;


    public static void setPage(Page page) {
        PlaywrightManager.page = page;
    }

    public static Page getPage() {
        return page;
    }

    public static BrowserContext getContext() {
        if (context == null) {
            Playwright playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)); // Set to false for visible browser
            context = browser.newContext();
            page = context.newPage();
            page.evaluate("window.scrollTo(0, document.body.scrollHeight);");

        }
        return context;
    }

    public static synchronized int createNewInstance() {
        if (browser == null) {
            Playwright playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
            );
            // Set to false for visible browser
        }
        int instanceId = contexts.size();
        BrowserContext context = browser.newContext(new Browser.NewContextOptions());
        Page page = context.newPage();
        contexts.put(instanceId, context);
        pages.put(instanceId, page);
        return instanceId;
    }

    public static synchronized void switchToInstance(int instanceId) {
        if (contexts.containsKey(instanceId)) {
            context=contexts.get(instanceId);
            currentInstanceId = instanceId;
        } else {
            throw new IllegalArgumentException("Instance ID does not exist: " + instanceId);
        }

    }

    public static synchronized Page getPageById(int instanceId) {
        return pages.get(instanceId);
    }



    public static Page getCurrentPage() {
        if (currentInstanceId == -1) {
            throw new IllegalStateException("No instance is currently selected.");
        }
        return pages.get(currentInstanceId);
    }

    public static void setCurrentPage(Page page) {
        if (currentInstanceId == -1) {
            throw new IllegalStateException("No instance is currently selected.");
        }
        pages.put(currentInstanceId, page);
    }

    public static void getCurrentContext() {
        if (currentInstanceId == -1) {
            throw new IllegalStateException("No instance is currently selected.");
        }
        contexts.get(currentInstanceId);
    }

    public static synchronized void closeInstance(int instanceId) {
        if (contexts.containsKey(instanceId)) {
            contexts.get(instanceId).close();
            contexts.remove(instanceId);
            pages.remove(instanceId);
        }
    }

    public static synchronized void closeAll() {
        Set<Integer> keys = new HashSet<>(contexts.keySet());
        for (Integer instanceId : keys) {
            closeInstance(instanceId);
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
    }

    public static void closeBrowser() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
    }
}