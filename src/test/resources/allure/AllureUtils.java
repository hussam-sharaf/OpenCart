package com.opencart.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class AllureUtils {
    public static void attachScreenshot(Page page, String name) {
        Allure.addAttachment(name,
                new ByteArrayInputStream(page.screenshot()));
    }

    public static void attachPageSource(Page page) {
        Allure.addAttachment("Page Source",
                "text/html", page.content());
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }
}