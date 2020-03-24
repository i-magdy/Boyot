package org.boyoot.app.model;

public class UserProfileModel {

    private String userName;
    private String email;
    private String phone;
    private String userId;
    private String password;
    private String role;
    public UserProfileModel(){

    }

    public UserProfileModel(String userName, String email, String phone, String userId, String password, String role) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
