package com.gamil.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Design Patterns: Page Object Model (POM) & Fluent Interface
 * Encapsulates the OrangeHRM Admin User Management Page.
 */
public class AdminPage extends BasePage {

    // Locators using robust scoped filters
    private final Locator systemUsersHeading = page.locator("//h5[contains(., 'System Users')]");
    private final Locator addButton = page.locator("//button[contains(., 'Add')]");
    private final Locator searchUsernameInput = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("Username")).locator("input").first();
    private final Locator searchButton = page.locator("button[type='submit']");
    private final Locator resetButton = page.locator("//button[contains(., 'Reset')]");
    private final Locator tableRecords = page.locator(".oxd-table-card");
    private final Locator toastMessage = page.locator(".oxd-toast");

    public AdminPage(Page page) {
        super(page);
    }

    public boolean isAdminPageLoaded() {
        waitForElementVisible(systemUsersHeading);
        return systemUsersHeading.isVisible();
    }

    /**
     * Clicks the Add button and navigates to the Add User Page.
     */
    public AddUserPage clickAddUser() {
        waitForElementVisible(addButton);
        addButton.click();
        return new AddUserPage(page);
    }

    /**
     * Searches for a user by username in the Admin System Users table.
     */
    public AdminPage searchUser(String username) {
        waitForElementVisible(searchUsernameInput);
        searchUsernameInput.fill(username);
        searchButton.click();
        page.waitForTimeout(3000); // Allow table search to complete
        return this;
    }

    /**
     * Checks if the user is present in the search results table.
     */
    public boolean isUserPresentInResults(String username) {
        Locator userInTable = page.locator(".oxd-table-body").getByText(username, new Locator.GetByTextOptions().setExact(false));
        try {
            userInTable.first().waitFor(new Locator.WaitForOptions().setTimeout(10000));
            return userInTable.count() > 0;
        } catch (Exception e) {
            return page.locator(".oxd-table-card").filter(new Locator.FilterOptions().setHasText(username)).count() > 0;
        }
    }

    public int getSearchResultsCount() {
        return tableRecords.count();
    }

    public AdminPage resetSearch() {
        resetButton.click();
        page.waitForTimeout(1000);
        return this;
    }

    public boolean isToastSuccessVisible() {
        try {
            toastMessage.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(6000));
            return toastMessage.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
