package com.google.hangouts.hypnosture;

import android.widget.RadioGroup;

/**
 * Created by neil on 2/9/18.
 */

public class UserProfile {

    String profileId;
    String full_name;
    String sex;
    String birthday;
    String profilePicURL;

    public UserProfile(){

    }

    public UserProfile(String profileId, String full_name, String sex, String birthday, String profilePicURL) {
        this.profileId = profileId;
        this.full_name = full_name;
        this.sex = sex;
        this.birthday = birthday;
        this.profilePicURL = profilePicURL;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
