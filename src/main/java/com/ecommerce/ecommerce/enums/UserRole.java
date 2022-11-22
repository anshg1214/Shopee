package com.ecommerce.ecommerce.enums;

public enum UserRole {
    CUSTOMER("CUSTOMER"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
