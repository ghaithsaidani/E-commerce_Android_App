package com.example.techapp.modeles;

import java.util.Date;

public class User {
    private String username,password,email;
    private String birthday;
    private String imageurl;
    private int phone;

    public User(String username, String email, String password,int phone, String birthday) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.phone=phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
