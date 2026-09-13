# Appointment Desk — Spring Boot port

A Spring Boot port of the single-page "Appointment Desk" demo: browse doctors by
specialization, view their open slots, and book an appointment in three steps.

The original demo simulated two services (`DoctorService` and `AppointmentService`)
as plain JS objects in one HTML file. This port keeps the same two-service split,
just as real Spring beans/controllers instead — exactly the seam the original's
architecture note pointed at for a microservices split.

## Run it

Requires Java 17+ and Maven.

```bash
mvn spring-boot:run
```

Then open http://localhost:8080 — the same UI as the original, now backed by a
real REST API instead of in-memory JS objects.

## Project layout

```
src/main/java/com/appointmentdesk/
  model/        Doctor, Slot, Appointment, AppointmentStatus
  service/      DoctorService, AppointmentService  (the two "microservices")
  controller/   DoctorController, AppointmentController, GlobalExceptionHandler
  dto/          Request/response shapes for the REST API
src/main/resources/
  static/index.html   Same UI as the original demo, calling the API via fetch()
  application.properties
```

## API

### Doctors (`DoctorController` — would become its own Spring Boot app on :8081)

| Method | Path                              | Description                          |
|--------|-----------------------------------|---------------------------------------|
| GET    | `/api/doctors`                    | List all doctors                     |
| GET    | `/api/doctors/specializations`    | Distinct specializations             |
| GET    | `/api/doctors/{id}`                | Get one doctor                       |
| GET    | `/api/doctors/{id}/slots?dayOffset=0` | Slots for a doctor on a given day (0 = today, up to `days-ahead - 1`) |
| GET    | `/api/doctors/days-ahead`          | How many days ahead slots are shown  |

### Appointments (`AppointmentController` — would become its own Spring Boot app on :8082)

| Method | Path                     | Description                                             |
|--------|--------------------------|----------------------------------------------------------|
| GET    | `/api/appointments`      | List all appointments, most recent first                |
| POST   | `/api/appointments`      | Book an appointment (body: `patientName`, `doctorId`, `dayOffset`, `time`) |
| DELETE | `/api/appointments/{id}` | Cancel an appointment and free its slot                 |

`AppointmentService.create()` calls `DoctorService.checkAvailability()` before
booking, and `DoctorService.markBooked()` / `markFreed()` on booking/cancellation —
the same handshake as the original demo. To split this into two real
microservices, move `DoctorService`/`DoctorController` into their own Spring
Boot app, keep `AppointmentService`/`AppointmentController` in the other, and
replace the direct method call with a `RestClient`/`WebClient` call to the
first app's `/api/doctors/...` endpoints.

## Notes on the port

- Both services keep their state in memory (a `List`/`Set` guarded with basic
  synchronization), just like the original's in-memory JS arrays/Sets — there's
  no database. Restarting the app resets everything, including the deterministic
  "pre-existing bookings" seed (same hash-based seeding as the original, ported
  to Java).
- Validation (`@Valid` + `GlobalExceptionHandler`) replaces the client-side-only
  checks from the HTML version, since a real API can't trust the caller.
