package com.simca.attendance.Model;

public class Admins {

    private String email,password,username,name;

    public Admins() {
    }

    public Admins(String email, String password, String username,String name) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
