package com.exercise.thesis.hellodoc.viewmodel;

public class Hour {
    private String patient;
    private String chosen;
    private String patientName;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Hour(String patient) {
        this.patient = patient;
        chosen = "false";
    }
    public Hour(){

    }
    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }
}
