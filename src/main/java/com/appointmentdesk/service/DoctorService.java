package com.appointmentdesk.service;

import com.appointmentdesk.model.Doctor;
import com.appointmentdesk.model.Slot;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Owns doctor profiles and slot availability.
 *
 * In the original single-page demo this was a client-side "DoctorService"
 * object. Here it is a standalone Spring bean, exposed over REST by
 * DoctorController — exactly the piece the architecture note describes
 * lifting into its own Spring Boot app on its own port, with
 * AppointmentService calling it over HTTP instead of a direct method call.
 */
@Service
public class DoctorService {

    public static final int DAYS_AHEAD = 6;

    private static final List<String> TIMES = List.of("09:00", "10:00", "11:30", "14:00", "15:30", "17:00");
    private static final List<String> DAY_LABELS = List.of("Today", "Tomorrow");

    private final List<Doctor> doctors = List.of(
            new Doctor("d1", "Dr. Meera Iyer", "General Medicine", "Sunrise Family Clinic"),
            new Doctor("d2", "Dr. Arjun Nair", "Cardiology", "Heartwell Cardiac Centre"),
            new Doctor("d3", "Dr. Fatima Sheikh", "Dermatology", "ClearSkin Dermatology"),
            new Doctor("d4", "Dr. Ravi Kulkarni", "Pediatrics", "Little Sprouts Pediatric Care"),
            new Doctor("d5", "Dr. Ananya Desai", "Orthopedics", "Bone & Joint Institute")
    );

    // Mutable "database" of taken slots (doctorId|dayOffset|time), seeded
    // deterministically so the demo doesn't show every slot open, exactly
    // like the JS hash-based seeding in the original.
    private final Set<String> taken = ConcurrentHashMap.newKeySet();

    public DoctorService() {
        seedTakenSlots();
    }

    private void seedTakenSlots() {
        for (Doctor doc : doctors) {
            for (int dayOffset = 0; dayOffset < DAYS_AHEAD; dayOffset++) {
                for (String time : TIMES) {
                    String key = slotKey(doc.getId(), dayOffset, time);
                    if (Math.floorMod(hash(key), 5) == 0) {
                        taken.add(key);
                    }
                }
            }
        }
    }

    // Same simple string hash as the JS version (h = h*31 + charCode, unsigned 32-bit).
    private static long hash(String str) {
        long h = 0;
        for (int i = 0; i < str.length(); i++) {
            h = (h * 31 + str.charAt(i)) & 0xFFFFFFFFL;
        }
        return h;
    }

    private static String slotKey(String doctorId, int dayOffset, String time) {
        return doctorId + "|" + dayOffset + "|" + time;
    }

    public List<Doctor> list() {
        return doctors;
    }

    public Doctor get(String id) {
        return doctors.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public List<String> specializations() {
        // LinkedHashSet preserves first-seen order, matching the JS Set behaviour.
        Set<String> specs = new LinkedHashSet<>();
        doctors.forEach(d -> specs.add(d.getSpecialization()));
        return specs.stream().collect(Collectors.toList());
    }

    public List<Slot> getSlots(String doctorId, int dayOffset) {
        return TIMES.stream()
                .map(t -> new Slot(t, !taken.contains(slotKey(doctorId, dayOffset, t))))
                .collect(Collectors.toList());
    }

    public boolean checkAvailability(String doctorId, int dayOffset, String time) {
        return !taken.contains(slotKey(doctorId, dayOffset, time));
    }

    public void markBooked(String doctorId, int dayOffset, String time) {
        taken.add(slotKey(doctorId, dayOffset, time));
    }

    public void markFreed(String doctorId, int dayOffset, String time) {
        taken.remove(slotKey(doctorId, dayOffset, time));
    }

    public LocalDate dateForOffset(int offset) {
        return LocalDate.now().plusDays(offset);
    }

    public String dayLabel(int offset) {
        if (offset < DAY_LABELS.size()) {
            return DAY_LABELS.get(offset);
        }
        LocalDate d = dateForOffset(offset);
        return d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                + ", " + d.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                + " " + d.getDayOfMonth();
    }

    public String fullDateLabel(int offset) {
        LocalDate d = dateForOffset(offset);
        return d.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH));
    }
}
