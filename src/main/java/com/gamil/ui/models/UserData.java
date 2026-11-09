package com.gamil.ui.models;

/**
 * Design Pattern: Builder Pattern
 * Encapsulates complex user data construction with flexible default and custom values.
 */
public class UserData {
    private final String userRole;
    private final String employeeName;
    private final String status;
    private final String username;
    private final String password;
    private final String confirmPassword;

    private UserData(Builder builder) {
        this.userRole = builder.userRole;
        this.employeeName = builder.employeeName;
        this.status = builder.status;
        this.username = builder.username;
        this.password = builder.password;
        this.confirmPassword = builder.confirmPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userRole = "Admin";
        private String employeeName = "a"; // OrangeHRM search query
        private String status = "Enabled";
        private String username;
        private String password;
        private String confirmPassword;

        public Builder userRole(String userRole) {
            this.userRole = userRole;
            return this;
        }

        public Builder employeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public UserData build() {
            if (this.username == null || this.username.isBlank()) {
                this.username = "testuser_" + System.currentTimeMillis() % 100000;
            }
            if (this.password == null || this.password.isBlank()) {
                this.password = "Admin@123456";
            }
            if (this.confirmPassword == null || this.confirmPassword.isBlank()) {
                this.confirmPassword = this.password;
            }
            return new UserData(this);
        }
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userRole='" + userRole + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
