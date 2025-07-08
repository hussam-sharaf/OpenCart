package com.opencart.stepdefinitions;

import com.microsoft.playwright.Page;
import com.opencart.pages.RegisterPage;
import com.opencart.utils.PlaywrightManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.UUID;

public class RegistrationSteps {
    private Page page;
    private RegisterPage registerPage;
    private Map<String, String> testData;

    @Step("Initialize browser and page objects")
    @Given("I am on the OpenCart registration page")
    public void navigateToRegistrationPage() {
        PlaywrightManager.createNewInstance();
        PlaywrightManager.switchToInstance(0);
        PlaywrightManager.getCurrentContext();
        page = PlaywrightManager.getCurrentPage();
        registerPage = new RegisterPage(page);
        registerPage.navigate();
        Allure.addAttachment("Page URL", "text/plain", page.url());
    }


    @Step("Register with provided details")
    @When("I register with the following details:")
    public void registerWithDetails(DataTable dataTable) {
        testData = dataTable.asMaps().get(0);
        String email = testData.get("Email").equals("unique@email.com")
                ? "test" + UUID.randomUUID() + "@example.com"
                : testData.get("Email");

        registerPage.register(
                testData.get("First Name"),
                testData.get("Last Name"),
                email,
                testData.get("Password"),
                testData.get("Confirm Password")
        );

        attachFormDataToAllure();
    }

    @Step("Attempt registration with provided details")
    @When("I attempt to register with the following details:")
    public void attemptRegistrationWithDetails(DataTable dataTable) {
        testData = dataTable.asMaps().get(0);
        registerPage.fillForm(
                testData.get("First Name"),
                testData.get("Last Name"),
                testData.get("Email"),
                testData.get("Password"),
                testData.get("Confirm Password")
        );

        if (Boolean.parseBoolean(testData.getOrDefault("Privacy Policy", "true"))) {
            registerPage.checkPrivacyPolicy();
        }

        registerPage.submitForm();
        attachFormDataToAllure();
    }

    @Step("Verify registration success")
    @Then("I should see the registration success message")
    public void verifyRegistrationSuccess() {
        Assertions.assertTrue(registerPage.isSuccessMessageVisible(),
                "Registration success message not displayed");
        Allure.addAttachment("Success Message", "text/plain",
                "Account successfully created");
    }

    @Step("Verify error messages")
    @Then("I should see the following errors:")
    public void verifyErrors(DataTable dataTable) {
        Map<String, String> expectedErrors = dataTable.asMaps().get(0);
        expectedErrors.forEach((field, expectedError) -> {
            String actualError = registerPage.getFieldError(field);
            Assertions.assertEquals(expectedError, actualError,
                    "Incorrect error message for " + field);
            Allure.addAttachment(field + " Error", "text/plain",
                    "Expected: " + expectedError + "\nActual: " + actualError);
        });
    }

    @Step("Verify specific error")
    @Then("I should see the following error:")
    public void verifyError(DataTable dataTable) {
        Map<String, String> error = dataTable.asMaps().get(0);
        String field = error.keySet().iterator().next();
        String expectedError = error.get(field);
        String actualError = registerPage.getFieldError(field);

        Assertions.assertEquals(expectedError, actualError,
                "Incorrect error message for " + field);
        Allure.addAttachment("Error Verification", "text/plain",
                "Field: " + field + "\nExpected: " + expectedError + "\nActual: " + actualError);
    }

    private void attachFormDataToAllure() {
        StringBuilder formData = new StringBuilder();
        testData.forEach((key, value) ->
                formData.append(key).append(": ").append(value).append("\n"));
        Allure.addAttachment("Form Data", "text/plain", formData.toString());
    }
}