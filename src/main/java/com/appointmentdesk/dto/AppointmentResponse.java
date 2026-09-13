package com.appointmentdesk.dto;

import com.appointmentdesk.model.Appointment;
import com.appointmentdesk.model.Doctor;

public class AppointmentResponse {

    private String id;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String specialization;
    private String clinic;
    private int dayOffset;
    private String dayLabel;
    private String fullDateLabel;
    private String time;
    private String status;
    private long createdAt;

    public AppointmentResponse() {
    }

    public AppointmentResponse(Appointment appt, Doctor doctor, String dayLabel, String fullDateLabel) {
        this.id = appt.getId();
        this.patientName = appt.getPatientName();
        this.doctorId = appt.getDoctorId();
        this.doctorName = doctor != null ? doctor.getName() : "Unknown";
        this.specialization = doctor != null ? doctor.getSpecialization() : "";
        this.clinic = doctor != null ? doctor.getClinic() : "";
        this.dayOffset = appt.getDayOffset();
        this.dayLabel = dayLabel;
        this.fullDateLabel = fullDateLabel;
        this.time = appt.getTime();
        this.status = appt.getStatus().name();
        this.createdAt = appt.getCreatedAt().toEpochMilli();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public String getFullDateLabel() {
        return fullDateLabel;
    }

    public void setFullDateLabel(String fullDateLabel) {
        this.fullDateLabel = fullDateLabel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
