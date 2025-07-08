package com.opencart.hooks;

import com.microsoft.playwright.Page;
import com.opencart.utils.AllureUtils;
import com.opencart.utils.PlaywrightManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

public class Hooks {
    private final Page page = PlaywrightManager.getPage();

    @Before
    public void beforeScenario(Scenario scenario) {
        Allure.label("feature", "User Registration");
        Allure.label("framework", "Playwright");
        Allure.link("OpenCart Demo", "https://demo.opencart.com/");
        Allure.description("Test scenario: " + scenario.getName());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        // Attach screenshot for every step in debug mode
        if (System.getProperty("debug", "false").equals("true")) {
            AllureUtils.attachScreenshot(page, "Step Screenshot");
        }

        // Always attach screenshot on failure
        if (scenario.isFailed()) {
            AllureUtils.attachScreenshot(page, "Failed Step Screenshot");
            AllureUtils.attachPageSource(page);
            Allure.addAttachment("Failed Step",
                    "text/plain", scenario.getLine() + ": " + scenario.getName());
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            AllureUtils.attachScreenshot(page, "Final Page Screenshot");
            Allure.addAttachment("Scenario Status",
                    "text/plain", "FAILED: " + scenario.getName());
        } else {
            Allure.addAttachment("Scenario Status",
                    "text/plain", "PASSED: " + scenario.getName());
        }
        PlaywrightManager.closeBrowser();
    }
}