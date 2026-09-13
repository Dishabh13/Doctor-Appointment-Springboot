package com.appointmentdesk.service;

import com.appointmentdesk.model.Appointment;
import com.appointmentdesk.model.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Owns bookings and their status.
 *
 * Mirrors the original "AppointmentService" object: it checks availability
 * with DoctorService before creating a booking, then tells DoctorService to
 * mark the slot taken/freed. In a true microservice split this would call
 * DoctorService over REST (RestClient/WebClient) instead of an in-process
 * method call — the injected dependency below is that seam.
 */
@Service
public class AppointmentService {

    private final DoctorService doctorService;
    private final List<Appointment> appointments = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public AppointmentService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public static final class CreateResult {
        private final boolean ok;
        private final String error;
        private final Appointment appointment;

        private CreateResult(boolean ok, String error, Appointment appointment) {
            this.ok = ok;
            this.error = error;
            this.appointment = appointment;
        }

        public static CreateResult success(Appointment appointment) {
            return new CreateResult(true, null, appointment);
        }

        public static CreateResult failure(String error) {
            return new CreateResult(false, error, null);
        }

        public boolean isOk() {
            return ok;
        }

        public String getError() {
            return error;
        }

        public Appointment getAppointment() {
            return appointment;
        }
    }

    public synchronized CreateResult create(String patientName, String doctorId, int dayOffset, String time) {
        // "Is Dr. X available at this time?" -> DoctorService
        if (!doctorService.checkAvailability(doctorId, dayOffset, time)) {
            return CreateResult.failure("That slot was just taken. Pick another.");
        }
        doctorService.markBooked(doctorId, dayOffset, time);
        Appointment appt = new Appointment("a" + nextId.getAndIncrement(), patientName, doctorId, dayOffset, time);
        appointments.add(appt);
        return CreateResult.success(appt);
    }

    public List<Appointment> list() {
        return appointments.stream()
                .sorted(Comparator.comparing(Appointment::getCreatedAt).reversed())
                .toList();
    }

    public synchronized boolean cancel(String id) {
        Optional<Appointment> found = appointments.stream().filter(a -> a.getId().equals(id)).findFirst();
        if (found.isEmpty() || found.get().getStatus() == AppointmentStatus.CANCELLED) {
            return false;
        }
        Appointment appt = found.get();
        appt.setStatus(AppointmentStatus.CANCELLED);
        doctorService.markFreed(appt.getDoctorId(), appt.getDayOffset(), appt.getTime());
        return true;
    }
}
