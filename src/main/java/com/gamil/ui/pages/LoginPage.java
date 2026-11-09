package com.gamil.ui.pages;

import com.gamil.ui.config.ConfigReader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Design Patterns: Page Object Model (POM) & Fluent Interface
 * Encapsulates the OrangeHRM Login Page elements and actions.
 */
public class LoginPage extends BasePage {

    // Locators
    private final Locator usernameInput = page.locator("input[name='username']");
    private final Locator passwordInput = page.locator("input[name='password']");
    private final Locator loginButton = page.locator("button[type='submit']");
    private final Locator loginTitle = page.locator(".orangehrm-login-title");
    private final Locator invalidCredentialsAlert = page.locator(".oxd-alert-content-text");

    public LoginPage(Page page) {
        super(page);
    }

    /**
     * Navigates to the base login URL configured in properties.
     */
    public LoginPage navigateToLoginPage() {
        String url = ConfigReader.getInstance().getProperty("ui.baseUrl", "https://opensource-demo.orangehrmlive.com/");
        page.navigate(url);
        waitForElementVisible(usernameInput);
        return this;
    }

    public LoginPage enterUsername(String username) {
        usernameInput.fill(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordInput.fill(password);
        return this;
    }

    /**
     * Clicks the login button and returns the DashboardPage.
     */
    public DashboardPage clickLogin() {
        loginButton.click();
        return new DashboardPage(page);
    }

    /**
     * Convenience method to perform full login in one fluent step.
     */
    public DashboardPage loginAs(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public boolean isLoginTitleVisible() {
        return loginTitle.isVisible();
    }

    public String getErrorMessage() {
        return invalidCredentialsAlert.innerText();
    }
}
