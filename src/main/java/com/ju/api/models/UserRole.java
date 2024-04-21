package com.ju.api.models;

public enum UserRole {
    ADMIN("admin"),
    USER("user");
    private String role;
    UserRole(String role){
        this.role = role;
    }
    public String getRoles(){
        return role;
    }
}
