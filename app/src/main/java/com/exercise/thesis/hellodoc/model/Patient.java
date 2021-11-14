package com.exercise.thesis.hellodoc.model;

import android.net.Uri;

public class Patient {
    private String fullName;
    private String email;
    private String mblNum;
    private String weight;
    private String height;
    private String bloodType;
    private Uri photoUrl;

    public Patient() {

    }

    public Patient(String fullName, String email, String mblNum, Uri image) {
        this.fullName = fullName;
        this.email = email;
        this.mblNum = mblNum;
        this.weight = "";
        this.height = "";
        this.bloodType = "";
        this.photoUrl = image;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
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
