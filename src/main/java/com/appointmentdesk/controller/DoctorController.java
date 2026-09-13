package com.appointmentdesk.controller;

import com.appointmentdesk.dto.DayResponse;
import com.appointmentdesk.dto.DoctorResponse;
import com.appointmentdesk.dto.ErrorResponse;
import com.appointmentdesk.dto.SlotResponse;
import com.appointmentdesk.model.Doctor;
import com.appointmentdesk.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GET /api/doctors
 * GET /api/doctors/specializations
 * GET /api/doctors/{id}/slots?dayOffset=0
 *
 * This is the "DoctorService" boundary called out in the original demo's
 * architecture note — the piece that becomes its own Spring Boot app on
 * port 8081 in the microservices version.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<DoctorResponse> listDoctors() {
        return doctorService.list().stream().map(DoctorResponse::new).toList();
    }

    @GetMapping("/specializations")
    public List<String> specializations() {
        return doctorService.specializations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable String id) {
        Doctor doctor = doctorService.get(id);
        if (doctor == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Doctor not found"));
        }
        return ResponseEntity.ok(new DoctorResponse(doctor));
    }

    @GetMapping("/{id}/slots")
    public ResponseEntity<?> getSlots(@PathVariable String id, @RequestParam(defaultValue = "0") int dayOffset) {
        Doctor doctor = doctorService.get(id);
        if (doctor == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Doctor not found"));
        }
        if (dayOffset < 0 || dayOffset >= DoctorService.DAYS_AHEAD) {
            return ResponseEntity.badRequest().body(new ErrorResponse("dayOffset out of range"));
        }
        List<SlotResponse> slots = doctorService.getSlots(id, dayOffset).stream()
                .map(s -> new SlotResponse(s.getTime(), s.isAvailable()))
                .toList();
        DayResponse response = new DayResponse(
                dayOffset,
                doctorService.dayLabel(dayOffset),
                doctorService.fullDateLabel(dayOffset),
                slots
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/days-ahead")
    public int daysAhead() {
        return DoctorService.DAYS_AHEAD;
    }
}
