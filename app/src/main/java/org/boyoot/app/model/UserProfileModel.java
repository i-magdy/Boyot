package org.boyoot.app.model;

public class UserProfileModel {

    private String userName;
    private String email;
    private String password;
    private String role;
    public UserProfileModel(){

    }

    public UserProfileModel(String userName, String email, String password, String role){
        this.userName = userName;
        this.email = email;
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
}
