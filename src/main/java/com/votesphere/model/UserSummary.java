package com.votesphere.model;

import java.time.LocalDateTime;

public record UserSummary(String fullName, String username, String email, String role, LocalDateTime createdAt) {
    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
