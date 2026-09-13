package com.appointmentdesk.model;

/**
 * A single bookable time slot for a doctor on a given day offset.
 */
public class Slot {

    private final String time;
    private final boolean available;

    public Slot(String time, boolean available) {
        this.time = time;
        this.available = available;
    }

    public String getTime() {
        return time;
    }

    public boolean isAvailable() {
        return available;
    }
}
