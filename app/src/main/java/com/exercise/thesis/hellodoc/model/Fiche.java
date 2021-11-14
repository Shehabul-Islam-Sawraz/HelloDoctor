package com.exercise.thesis.hellodoc.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Fiche {
    private String disease;
    private String description;
    private String treatment;
    private String type;
    private Date dateCreated;
    private String doctor;

    public Fiche(){

    }

    public Fiche(String disease, String description, String treatment, String type, String doctor, Date date) {
        this.disease = disease;
        this.description = description;
        this.treatment = treatment;
        this.type = type;
        this.doctor = doctor;
        this.dateCreated = date;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
