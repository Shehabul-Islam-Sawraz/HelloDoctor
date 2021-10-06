package com.exercise.thesis.hellodoc.model;

public class Patient {
    private String fullName;
    private String email;
    private String mblNum;

    public Patient() {

    }

    public Patient(String fullName, String email, String mblNum) {
        this.fullName = fullName;
        this.email = email;
        this.mblNum = mblNum;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMblNum() {
        return mblNum;
    }

    public void setMblNum(String mblNum) {
        this.mblNum = mblNum;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", mobile number ='" + mblNum + '\'' +
                '}';
    }
}
