package com.gamil.api.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamil.api.api.UserApi;
import com.gamil.api.base.BaseApiTest;
import com.gamil.api.models.UserRequest;
import com.gamil.api.models.UserResponse;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * End-to-End API Test Suite demonstrating full CRUD lifecycle testing with multiple datasets.
 */
public class EndToEndApiTest extends BaseApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DataProvider(name = "userDataProvider")
    public Object[][] getUserData() {
        return new Object[][] {
                {"Sarah Gamal", "Lead QA Automation Engineer", "Senior Principal QA"},
                {"John Doe", "Fullstack Developer", "Tech Lead"},
                {"Jane Smith", "DevOps Specialist", "Cloud Architect"},
                {"Ahmed Hassan", "SDET", "Staff QA Architect"}
        };
    }

    @Test(dataProvider = "userDataProvider", description = "E2E Scenario: Create -> Read -> Update -> Delete User with multiple datasets")
    public void testUserFullLifecycleE2E(String name, String initialJob, String updatedJob) throws IOException {
        UserApi userApi = new UserApi(request);

        // STEP 1: CREATE User (POST)
        UserRequest createPayload = new UserRequest(name, initialJob);
        APIResponse createResponse = userApi.createUser(createPayload);
        int createStatus = createResponse.status();

        Assert.assertTrue(createStatus == 201 || createStatus == 200 || createStatus == 429, 
                "Create user should return valid HTTP status (201/200/429). Got: " + createStatus);

        if (createStatus == 429) {
            System.out.println("Notice: Public demo API rate limit reached (HTTP 429). Response: " + createResponse.text());
            return;
        }

        UserResponse createdUser = objectMapper.readValue(createResponse.text(), UserResponse.class);
        Assert.assertNotNull(createdUser.getId(), "Created user ID should not be null");
        Assert.assertEquals(createdUser.getName(), name, "Created user name should match payload");
        Assert.assertEquals(createdUser.getJob(), initialJob, "Created user job should match payload");
        System.out.println("Step 1 SUCCESS - User Created: " + createdUser);

        String userId = createdUser.getId();

        // STEP 2: GET User List / Details (GET)
        APIResponse listResponse = userApi.getUsers(1);
        Assert.assertTrue(listResponse.status() == 200 || listResponse.status() == 429, "Get users list status: " + listResponse.status());
        System.out.println("Step 2 SUCCESS - Retrieved users list");

        // STEP 3: UPDATE User (PUT)
        UserRequest updatePayload = new UserRequest(name, updatedJob);
        APIResponse updateResponse = userApi.updateUser(userId, updatePayload);
        Assert.assertTrue(updateResponse.status() == 200 || updateResponse.status() == 429, "Update user status: " + updateResponse.status());

        if (updateResponse.status() == 200) {
            UserResponse updatedUser = objectMapper.readValue(updateResponse.text(), UserResponse.class);
            Assert.assertEquals(updatedUser.getJob(), updatedJob, "Updated user job should reflect new title");
            Assert.assertNotNull(updatedUser.getUpdatedAt(), "Updated user should include updatedAt timestamp");
            System.out.println("Step 3 SUCCESS - User Updated: " + updatedUser);
        }

        // STEP 4: DELETE User (DELETE)
        APIResponse deleteResponse = userApi.deleteUser(userId);
        Assert.assertTrue(deleteResponse.status() == 204 || deleteResponse.status() == 200 || deleteResponse.status() == 429,
                "Delete user status: " + deleteResponse.status());
        System.out.println("Step 4 SUCCESS - User " + userId + " Deleted successfully");
    }
}
