package com.appointmentdesk.dto;

import com.appointmentdesk.model.Doctor;

public class DoctorResponse {

    private String id;
    private String name;
    private String specialization;
    private String clinic;

    public DoctorResponse() {
    }

    public DoctorResponse(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.specialization = doctor.getSpecialization();
        this.clinic = doctor.getClinic();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
}
