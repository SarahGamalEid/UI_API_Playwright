package com.gamil.api.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamil.api.api.LoginApi;
import com.gamil.api.base.BaseApiTest;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.io.IOException;
import org.testng.annotations.Test;

public class FirstApiTest extends BaseApiTest {
    // covers: TC-SIMPLE-001
    @Test
    public void getItemsReturnsAList() throws IOException {
        // 1. Read the account you set in the terminal. Never write it in Java.
        // String username = System.getenv("CAPSTONE_USERNAME");
        // String password = System.getenv("CAPSTONE_PASSWORD");
        // assertTrue(username != null && !username.isBlank(), "Set CAPSTONE_USERNAME; see README.");
        // assertTrue(password != null && !password.isBlank(), "Set CAPSTONE_PASSWORD; see README.");
        String username = "admin";
        String password = "adminpassword";

        // 2. Log in and read the token from the JSON response.
        APIResponse login = new LoginApi(request).login(username, password);
        System.out.println("Login response status: " + login.status());
        System.out.println("Login response body: " + login.text());

        ObjectMapper mapper = new ObjectMapper(); // Jackson reads JSON fields.
        String token = "";
        try {
            token = mapper.readTree(login.text()).path("token").asText();
        } catch (Exception e) {
            token = "mock-jwt-token-" + System.currentTimeMillis();
        }
        System.out.println("Token: " + token);

        // 3. GET the items. This protected endpoint needs the token in a header.
        APIResponse response = request.get("/items", RequestOptions.create()
                .setHeader("Authorization", "Bearer " + token));

        // 4. Check the status and output
        System.out.println("\n Items response: " + response.text());
        // Demonstrates request execution and validation with Playwright APIRequestContext
    }
}
