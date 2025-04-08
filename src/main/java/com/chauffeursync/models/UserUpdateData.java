package com.chauffeursync.models;

public class UserUpdateData {
    public String name;
    public String email;
    public String roleId;

    public UserUpdateData(String name, String email, String roleId) {
        this.name = name;
        this.email = email;
        this.roleId = roleId;
    }
}
