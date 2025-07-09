package com.opencart.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.github.cdimascio.dotenv.Dotenv;
import io.qameta.allure.Step;

public class RegisterPage {
    static Dotenv dotenv = Dotenv.load();
    private final Page page;

    // Locators
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator privacyPolicyCheckbox;
    private final Locator submitButton;
    private final Locator successMessage;

    // Error messages
    private final Locator firstNameError;
    private final Locator lastNameError;
    private final Locator emailError;
    private final Locator passwordError;
    private final Locator privacyPolicyError;
    private final Locator newsletterCheckbox ;

    public RegisterPage(Page page) {
        this.page = page;
        this.firstNameInput = page.locator("#input-firstname");
        this.lastNameInput = page.locator("#input-lastname");
        this.emailInput = page.locator("#input-email");
        this.passwordInput = page.locator("#input-password");
        this.privacyPolicyCheckbox = page.locator("input[name='agree']");
        this.newsletterCheckbox = page.locator("input[@id='input-newsletter']");

        this.submitButton = page.locator("input[type='submit']");
        this.successMessage = page.locator("text=Your Account Has Been Created!");

        this.firstNameError = page.locator("//div[@id='error-firstname']");
        this.lastNameError = page.locator("//div[@id='error-lastname']");
        this.emailError = page.locator("//div[@id='error-email']");
        this.passwordError = page.locator("//div[@id='error-password']");
        this.privacyPolicyError = page.locator("//*[@id='account-register']/div[1]");
    }

    @Step("Navigate to registration page")
    public void navigate() {
        page.navigate(dotenv.get("BASE_URL"));
        page.locator("text=My Account").click();
        page.locator("text=Register").click();
    }

    @Step("Fill registration form")
    public void fillForm(String firstName, String lastName, String email,
                          String password, String confirmPassword) {
        if (firstName != null) firstNameInput.fill(firstName);
        if (lastName != null) lastNameInput.fill(lastName);
        if (email != null) emailInput.fill(email);
        if (password != null) passwordInput.fill(password);
    }

    @Step("Check privacy policy")
    public void checkPrivacyPolicy() {
        privacyPolicyCheckbox.check();
    }

    @Step("Subscribe to Newsletter")
    public void subscribeNewsLetter() {
        newsletterCheckbox.check();
    }

    @Step("Submit registration form")
    public void submitForm() {
        submitButton.click();
    }

    @Step("Complete registration")
    public void register(String firstName, String lastName, String email,
                          String password, String confirmPassword) {
        fillForm(firstName, lastName, email, password, confirmPassword);
        checkPrivacyPolicy();
        submitForm();
    }

    @Step("Verify success message visibility")
    public boolean isSuccessMessageVisible() {
        return successMessage.isVisible();
    }

    @Step("Get field error message")
    public String getFieldError(String field) {
        switch(field) {
            case "First Name":
                return firstNameError.textContent().trim();
            case "Last Name":
                return lastNameError.textContent().trim();
            case "Email":
                return emailError.textContent().trim();
            case "Password":
                return passwordError.textContent().trim();
            case "Privacy Policy":
                return privacyPolicyError.textContent().trim();
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }
}