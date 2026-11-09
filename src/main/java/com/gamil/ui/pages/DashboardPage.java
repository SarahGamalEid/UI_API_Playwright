package com.gamil.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Design Patterns: Page Object Model (POM) & Fluent Interface
 * Encapsulates the OrangeHRM Dashboard Page elements and sidebar navigation.
 */
public class DashboardPage extends BasePage {

    // Locators
    private final Locator headerTitle = page.locator(".oxd-topbar-header-breadcrumb h6");
    private final Locator adminMenuLink = page.locator("a[href*='/admin/viewAdminModule']");
    private final Locator userDropdown = page.locator(".oxd-userdropdown-name");

    public DashboardPage(Page page) {
        super(page);
    }

    public boolean isDashboardLoaded() {
        waitForElementVisible(headerTitle);
        return headerTitle.isVisible();
    }

    public String getHeaderTitle() {
        return headerTitle.innerText();
    }

    public boolean isUserLoggedIn() {
        return userDropdown.isVisible();
    }

    /**
     * Navigates to the Admin module and returns AdminPage.
     */
    public AdminPage clickAdminMenu() {
        waitForElementVisible(adminMenuLink);
        adminMenuLink.click();
        return new AdminPage(page);
    }
}
