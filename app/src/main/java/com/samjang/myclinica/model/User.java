package com.samjang.myclinica.model;

import java.util.Date;

public class User {

    String email;
    String firstName;
    Date sessionExpiryDate;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }

}
