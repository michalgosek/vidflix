package com.example.videly.authentication;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    ROLE_ADMIN("1"),
    ROLE_USER("2");

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}
