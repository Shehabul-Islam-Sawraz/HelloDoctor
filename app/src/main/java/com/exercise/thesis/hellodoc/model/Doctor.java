package com.exercise.thesis.hellodoc.model;

import android.net.Uri;

public class Doctor {
    private String fullName;
    private String email;
    private String phoneNum;
    private String address;
    private String specialities;
    private Uri image;
    private String about;
    private String fees;
    private String avgRating;
    private int noOfRating;

    public Doctor() {

    }

    public Doctor(String fullName, String email, String address,Uri image) {
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.phoneNum = "";
        this.specialities = "";
        this.image = image;
        this.about = "";
        this.fees = "";
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public int getNoOfRating() {
        return noOfRating;
    }

    public void setNoOfRating(int noOfRating) {
        this.noOfRating = noOfRating;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
