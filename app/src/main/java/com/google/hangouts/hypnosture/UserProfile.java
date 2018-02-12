package com.google.hangouts.hypnosture;

import android.widget.RadioGroup;

/**
 * Created by neil on 2/9/18.
 */

public class UserProfile {

    String fullname;
    String sex;
    String birthday;
    String profilePicURL;

    public UserProfile(){

    }

    public UserProfile(String fullname, String sex, String birthday, String profilePicURL) {
        this.fullname = fullname;
        this.sex = sex;
        this.birthday = birthday;
        this.profilePicURL = profilePicURL;
    }

    public String getFull_name() {
        return fullname;
    }

    public void setFull_name(String full_name) {
        this.fullname = full_name;
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
