package com.google.hangouts.hypnosture.ADMIN;

/**
 * Created by neil on 2/23/18.
 */

public class Admin {

    String adminID;
    String adminEmail;
    String adminPassword;
    String adminFname;
    String adminSex;
    String adminBirthday;
    String adminProfilePicURL;

    public Admin(){

    }

    public Admin(String adminID, String adminEmail, String adminPassword, String adminFname, String adminSex, String adminBirthday, String adminProfilePicURL) {
        this.adminID = adminID;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
        this.adminFname = adminFname;
        this.adminSex = adminSex;
        this.adminBirthday = adminBirthday;
        this.adminProfilePicURL = adminProfilePicURL;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminFname() {
        return adminFname;
    }

    public void setAdminFname(String adminFname) {
        this.adminFname = adminFname;
    }

    public String getAdminSex() {
        return adminSex;
    }

    public void setAdminSex(String adminSex) {
        this.adminSex = adminSex;
    }

    public String getAdminBirthday() {
        return adminBirthday;
    }

    public void setAdminBirthday(String adminBirthday) {
        this.adminBirthday = adminBirthday;
    }

    public String getAdminProfilePicURL() {
        return adminProfilePicURL;
    }

    public void setAdminProfilePicURL(String adminProfilePicURL) {
        this.adminProfilePicURL = adminProfilePicURL;
    }
}
