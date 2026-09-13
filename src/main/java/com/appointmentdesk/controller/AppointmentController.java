package com.appointmentdesk.controller;

import com.appointmentdesk.dto.AppointmentResponse;
import com.appointmentdesk.dto.CreateAppointmentRequest;
import com.appointmentdesk.dto.ErrorResponse;
import com.appointmentdesk.model.Appointment;
import com.appointmentdesk.model.Doctor;
import com.appointmentdesk.service.AppointmentService;
import com.appointmentdesk.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GET    /api/appointments
 * POST   /api/appointments
 * DELETE /api/appointments/{id}
 *
 * This is the "AppointmentService" boundary from the architecture note —
 * the piece that becomes its own Spring Boot app on port 8082, calling
 * DoctorService over REST instead of the direct method call used here.
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<AppointmentResponse> list() {
        return appointmentService.list().stream().map(this::toResponse).toList();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateAppointmentRequest request) {
        Doctor doctor = doctorService.get(request.getDoctorId());
        if (doctor == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Doctor not found"));
        }
        if (request.getDayOffset() < 0 || request.getDayOffset() >= DoctorService.DAYS_AHEAD) {
            return ResponseEntity.badRequest().body(new ErrorResponse("dayOffset out of range"));
        }
        if (request.getPatientName().trim().length() <= 1) {
            return ResponseEntity.badRequest().body(new ErrorResponse("patientName is too short"));
        }

        AppointmentService.CreateResult result = appointmentService.create(
                request.getPatientName().trim(),
                request.getDoctorId(),
                request.getDayOffset(),
                request.getTime()
        );

        if (!result.isOk()) {
            return ResponseEntity.status(409).body(new ErrorResponse(result.getError()));
        }
        return ResponseEntity.status(201).body(toResponse(result.getAppointment()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable String id) {
        boolean cancelled = appointmentService.cancel(id);
        if (!cancelled) {
            return ResponseEntity.status(404).body(new ErrorResponse("Appointment not found or already cancelled"));
        }
        return ResponseEntity.noContent().build();
    }

    private AppointmentResponse toResponse(Appointment appt) {
        Doctor doctor = doctorService.get(appt.getDoctorId());
        return new AppointmentResponse(
                appt,
                doctor,
                doctorService.dayLabel(appt.getDayOffset()),
                doctorService.fullDateLabel(appt.getDayOffset())
        );
    }
}
