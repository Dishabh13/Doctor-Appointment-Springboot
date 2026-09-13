package com.appointmentdesk.model;

import java.time.Instant;

/**
 * A booked appointment. Owned by the "AppointmentService" domain, which in
 * the microservice split talks to DoctorService over HTTP instead of a
 * direct in-process call.
 */
public class Appointment {

    private final String id;
    private final String patientName;
    private final String doctorId;
    private final int dayOffset;
    private final String time;
    private AppointmentStatus status;
    private final Instant createdAt;

    public Appointment(String id, String patientName, String doctorId, int dayOffset, String time) {
        this.id = id;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.dayOffset = dayOffset;
        this.time = time;
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public int getDayOffset() {
        return dayOffset;
    }

    public String getTime() {
        return time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
