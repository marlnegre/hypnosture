package com.google.hangouts.hypnosture.USER;

/**
 * Created by neil on 2/9/18.
 */

public class User {

    String userId;
    String email;
    String password;
    String fname;
    String lname;
    String sex;
    String birthday;
    String profilePicURL;

    public User(){
    }

    public User(String userId, String email, String password, String fname, String lname, String sex, String birthday, String profilePicURL) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.sex = sex;
        this.birthday = birthday;
        this.profilePicURL = profilePicURL;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

}
