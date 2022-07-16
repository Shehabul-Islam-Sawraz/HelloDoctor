package com.exercise.thesis.hellodoc.model;

import android.net.Uri;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Fiche {
    private String disease;
    private String description;
    private String treatment;
    private String type;
    private Date dateCreated;
    private String doctor;
    private Uri prescription;
    private boolean isRated;
    private String id;

    public Fiche(){

    }

    public Fiche(String disease, String description, String treatment, String type, String doctor, Date date, Uri uri) {
        this.disease = disease;
        this.description = description;
        this.treatment = treatment;
        this.type = type;
        this.doctor = doctor;
        this.dateCreated = date;
        this.prescription = uri;
        this.isRated = false;
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

    public Uri getPrescription() {
        return prescription;
    }

    public void setPrescription(Uri prescription) {
        this.prescription = prescription;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
