package com.google.hangouts.hypnosture.USER;

import com.google.hangouts.hypnosture.model.Statistics;

/**
 * Created by neil on 2/9/18.
 */

public class User {

    String userId;
    String email;
    String password;
    String fname;
    String lname;

    public User(){
    }

    public User(String userId, String email, String password, String fname, String lname) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
