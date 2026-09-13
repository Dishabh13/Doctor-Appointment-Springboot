package com.appointmentdesk.dto;

import java.util.List;

public class DayResponse {

    private int dayOffset;
    private String dayLabel;
    private String fullDateLabel;
    private List<SlotResponse> slots;

    public DayResponse() {
    }

    public DayResponse(int dayOffset, String dayLabel, String fullDateLabel, List<SlotResponse> slots) {
        this.dayOffset = dayOffset;
        this.dayLabel = dayLabel;
        this.fullDateLabel = fullDateLabel;
        this.slots = slots;
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

    public List<SlotResponse> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotResponse> slots) {
        this.slots = slots;
    }
}
