package com.gamil.ui.pages;

import com.gamil.ui.models.UserData;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Design Patterns: Page Object Model (POM) & Fluent Interface
 * Encapsulates the OrangeHRM Add User Page form interactions.
 */
public class AddUserPage extends BasePage {

    // Locators using robust scoped filters
    private final Locator addUserHeading = page.locator("//h6[contains(., 'Add User')]");
    private final Locator userRoleDropdown = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("User Role")).locator(".oxd-select-text").first();
    private final Locator employeeNameInput = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("Employee Name")).locator("input").first();
    private final Locator employeeAutocompleteOption = page.locator("div[role='listbox'] div[role='option']");
    private final Locator statusDropdown = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("Status")).locator(".oxd-select-text").first();
    private final Locator usernameInput = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("Username")).locator("input").first();
    private final Locator passwordInput = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("Password")).locator("input[type='password']").first();
    private final Locator confirmPasswordInput = page.locator(".oxd-input-group").filter(new Locator.FilterOptions().setHasText("Confirm Password")).locator("input[type='password']").first();
    private final Locator saveButton = page.locator("button[type='submit']");
    private final Locator cancelButton = page.locator("//button[contains(., 'Cancel')]");

    public AddUserPage(Page page) {
        super(page);
    }

    public boolean isAddUserPageLoaded() {
        waitForElementVisible(addUserHeading);
        return addUserHeading.isVisible();
    }

    public AddUserPage selectUserRole(String roleName) {
        waitForElementVisible(userRoleDropdown);
        userRoleDropdown.click();
        page.locator("div[role='listbox'] div[role='option']").filter(new Locator.FilterOptions().setHasText(roleName)).first().click();
        return this;
    }

    public AddUserPage enterEmployeeName(String employeePrefix) {
        waitForElementVisible(employeeNameInput);
        employeeNameInput.fill(employeePrefix);
        page.waitForTimeout(2000); // Allow OrangeHRM autocomplete API to respond
        try {
            employeeAutocompleteOption.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(8000));
            employeeAutocompleteOption.first().click();
        } catch (Exception e) {
            employeeNameInput.press("ArrowDown");
            page.waitForTimeout(1000);
            if (employeeAutocompleteOption.count() > 0) {
                employeeAutocompleteOption.first().click();
            }
        }
        return this;
    }

    public AddUserPage selectStatus(String status) {
        waitForElementVisible(statusDropdown);
        statusDropdown.click();
        page.locator("div[role='listbox'] div[role='option']").filter(new Locator.FilterOptions().setHasText(status)).first().click();
        return this;
    }

    public AddUserPage enterUsername(String username) {
        waitForElementVisible(usernameInput);
        usernameInput.fill(username);
        return this;
    }

    public AddUserPage enterPassword(String password) {
        waitForElementVisible(passwordInput);
        passwordInput.fill(password);
        return this;
    }

    public AddUserPage enterConfirmPassword(String confirmPassword) {
        waitForElementVisible(confirmPasswordInput);
        confirmPasswordInput.fill(confirmPassword);
        return this;
    }

    /**
     * Fills the entire Add User form using the UserData model (Builder Pattern).
     */
    public AddUserPage fillUserForm(UserData userData) {
        selectUserRole(userData.getUserRole());
        enterEmployeeName(userData.getEmployeeName());
        selectStatus(userData.getStatus());
        enterUsername(userData.getUsername());
        enterPassword(userData.getPassword());
        enterConfirmPassword(userData.getConfirmPassword());
        return this;
    }

    /**
     * Clicks the save button and returns the AdminPage.
     */
    public AdminPage clickSave() {
        saveButton.click();
        try {
            page.waitForURL("**/admin/viewSystemUsers", new Page.WaitForURLOptions().setTimeout(12000));
        } catch (Exception e) {
            page.waitForTimeout(4000);
        }
        return new AdminPage(page);
    }

    public AdminPage clickCancel() {
        cancelButton.click();
        return new AdminPage(page);
    }
}
