package com.google.hangouts.hypnosture.ADMIN;

/**
 * Created by neil on 2/26/18.
 */

public class Verification {
    String verificationID;
    String verificationPIN;
    public Verification(){

    }
    public Verification(String verificationID, String verificationPIN) {
        this.verificationID = verificationID;
        this.verificationPIN = verificationPIN;
    }

    public String getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(String verificationID) {
        this.verificationID = verificationID;
    }

    public String getVerificationPIN() {
        return verificationPIN;
    }

    public void setVerificationPIN(String verificationPIN) {
        this.verificationPIN = verificationPIN;
    }
}
