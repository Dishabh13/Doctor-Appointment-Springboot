package com.appointmentdesk.model;

/**
 * Doctor profile. Owned by the "DoctorService" domain — in the original
 * single-page demo this lived in a client-side object; here it is the
 * DoctorService's own entity.
 */
public class Doctor {

    private final String id;
    private final String name;
    private final String specialization;
    private final String clinic;

    public Doctor(String id, String name, String specialization, String clinic) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.clinic = clinic;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getClinic() {
        return clinic;
    }
}
