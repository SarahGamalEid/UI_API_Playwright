package com.gamil.api.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamil.api.models.UserRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

/**
 * Service API client for User Management CRUD operations.
 */
public class UserApi {
    private final APIRequestContext request;
    private final ObjectMapper objectMapper;

    public UserApi(APIRequestContext request) {
        this.request = request;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * POST /api/users - Create new user
     */
    public APIResponse createUser(UserRequest user) {
        return request.post("/api/users", RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(user));
    }

    /**
     * GET /api/users/{id} - Get single user
     */
    public APIResponse getUser(String id) {
        return request.get("/api/users/" + id);
    }

    /**
     * GET /api/users?page={page} - List users
     */
    public APIResponse getUsers(int page) {
        return request.get("/api/users?page=" + page);
    }

    /**
     * PUT /api/users/{id} - Update user
     */
    public APIResponse updateUser(String id, UserRequest user) {
        return request.put("/api/users/" + id, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(user));
    }

    /**
     * PATCH /api/users/{id} - Partially update user
     */
    public APIResponse patchUser(String id, UserRequest user) {
        return request.patch("/api/users/" + id, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(user));
    }

    /**
     * DELETE /api/users/{id} - Delete user
     */
    public APIResponse deleteUser(String id) {
        return request.delete("/api/users/" + id);
    }
}
