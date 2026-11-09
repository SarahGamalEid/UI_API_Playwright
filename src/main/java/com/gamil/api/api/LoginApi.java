package com.gamil.api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.HashMap;
import java.util.Map;

/**
 * API Service Client for Authentication endpoints.
 */
public class LoginApi {
    private final APIRequestContext request;
    private final ObjectMapper objectMapper;

    public LoginApi(APIRequestContext request) {
        this.request = request;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Authenticates with username/email and password.
     */
    public APIResponse login(String username, String password) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", username);
        data.put("password", password);

        return request.post("/api/login", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(data));
    }

    /**
     * Authenticates with custom endpoint.
     */
    public APIResponse login(String endpoint, String username, String password) {
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("email", username);
        data.put("password", password);

        return request.post(endpoint, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(data));
    }
}
