package com.gamil.api.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamil.api.api.UserApi;
import com.gamil.api.base.BaseApiTest;
import com.gamil.api.models.UserRequest;
import com.microsoft.playwright.APIResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Data-Driven API Test reading test datasets from JSON file.
 */
public class DataDrivenApiTest extends BaseApiTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DataProvider(name = "jsonUserDataProvider")
    public Object[][] getJsonUserData() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testdata/users.json")) {
            if (is == null) {
                throw new RuntimeException("testdata/users.json not found in test resources!");
            }
            List<UserRequest> users = objectMapper.readValue(is, new TypeReference<List<UserRequest>>() {});
            Object[][] data = new Object[users.size()][1];
            for (int i = 0; i < users.size(); i++) {
                data[i][0] = users.get(i);
            }
            return data;
        }
    }

    @Test(dataProvider = "jsonUserDataProvider", description = "Data-driven test to create users from JSON source")
    public void testCreateUserFromJsonData(UserRequest userRequest) {
        UserApi userApi = new UserApi(request);

        APIResponse response = userApi.createUser(userRequest);
        System.out.println("DataDriven Test Response: " + response.text());
        int status = response.status();

        Assert.assertTrue(status == 201 || status == 200 || status == 429,
                "Expected status 200/201/429 but got " + status);
        if (status == 201 || status == 200) {
            Assert.assertTrue(response.text().contains(userRequest.getName()),
                    "Response body should contain user name: " + userRequest.getName());
        }
    }
}
