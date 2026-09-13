package com.appointmentdesk.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateAppointmentRequest {

    @NotBlank(message = "patientName is required")
    private String patientName;

    @NotBlank(message = "doctorId is required")
    private String doctorId;

    @Min(value = 0, message = "dayOffset must be >= 0")
    private int dayOffset;

    @NotBlank(message = "time is required")
    private String time;

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

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
