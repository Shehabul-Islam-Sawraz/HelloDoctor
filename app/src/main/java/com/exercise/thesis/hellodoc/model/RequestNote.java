package com.exercise.thesis.hellodoc.model;

public class RequestNote {
    private String idPatient;
    private String idDoctor;

    public RequestNote(String idPatient, String idDoctor) {
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(String idDoctor) {
        this.idDoctor = idDoctor;
    }
}
