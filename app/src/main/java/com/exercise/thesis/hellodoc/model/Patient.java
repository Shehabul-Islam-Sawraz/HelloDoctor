package com.exercise.thesis.hellodoc.model;

public class Patient {
    private String fullName;
    private String email;
    private String mblNum;
    private String dateAffected;
    private String situationFamiliar;
    private String weight;
    private String height;
    private String bloodType;

    public Patient() {

    }

    public Patient(String fullName, String email, String mblNum) {
        this.fullName = fullName;
        this.email = email;
        this.mblNum = mblNum;
        this.dateAffected = "";
        this.situationFamiliar = "";
        this.weight = "";
        this.height = "";
        this.bloodType = "";
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

    public String getSituationFamiliar() {
        return situationFamiliar;
    }

    public void setSituationFamiliar(String situationFamiliar) {
        this.situationFamiliar = situationFamiliar;
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

    public String getDateAffected() {
        return dateAffected;
    }

    public void setDateAffected(String dateAffected) {
        this.dateAffected = dateAffected;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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
