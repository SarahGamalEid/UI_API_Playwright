package com.gamil.ui.tests;

import com.gamil.ui.base.BaseUiTest;
import com.gamil.ui.models.UserData;
import com.gamil.ui.pages.AddUserPage;
import com.gamil.ui.pages.AdminPage;
import com.gamil.ui.pages.DashboardPage;
import com.gamil.ui.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * OrangeHRM User Management Test Suite.
 * Demonstrates:
 * 1. Page Object Model (POM)
 * 2. Factory Pattern (PlaywrightFactory)
 * 3. Builder Pattern (UserData.builder())
 * 4. Fluent Interface Pattern (Method Chaining)
 * 5. Singleton Pattern (ConfigReader)
 */
public class OrangeHrmUserManagementTest extends BaseUiTest {

    @Test(description = "Verify Admin can successfully create and search for a new user in OrangeHRM")
    public void testAddAndSearchNewUser() {
        // 1. Initialize Page Objects & navigate to URL
        LoginPage loginPage = new LoginPage(page);
        
        // 2. Login to OrangeHRM using Fluent Interface
        DashboardPage dashboardPage = loginPage
                .navigateToLoginPage()
                .loginAs("Admin", "admin123");

        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard should be visible after login");

        // 3. Click on Admin menu item
        AdminPage adminPage = dashboardPage.clickAdminMenu();
        Assert.assertTrue(adminPage.isAdminPageLoaded(), "Admin User Management page should be displayed");

        // 4. Generate unique test user data using Builder Pattern
        String uniqueUsername = "autouser_" + (System.currentTimeMillis() % 100000);
        UserData newUserData = UserData.builder()
                .userRole("Admin")
                .employeeName("a") // dynamic search query on demo instance
                .status("Enabled")
                .username(uniqueUsername)
                .password("Test@12345678")
                .confirmPassword("Test@12345678")
                .build();

        // 5. Click Add button, fill user details, and save
        AddUserPage addUserPage = adminPage.clickAddUser();
        Assert.assertTrue(addUserPage.isAddUserPageLoaded(), "Add User form should be displayed");

        adminPage = addUserPage
                .fillUserForm(newUserData)
                .clickSave();

        // 6. Search for the newly created user and verify presence
        adminPage.searchUser(uniqueUsername);
        
        boolean isFound = adminPage.isUserPresentInResults(uniqueUsername);
        Assert.assertTrue(isFound, "The newly created user [" + uniqueUsername + "] should be found in Admin search results");
    }
}
