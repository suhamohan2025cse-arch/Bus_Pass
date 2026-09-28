# Student Bus Pass Application Tracker

Simple academic Spring Boot project for managing student bus-pass applications.

## Main entities
- Student
- BusRoute
- PassApplication

## Technology
- Java 17
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA / Hibernate
- MySQL
- Maven
- HTML/CSS/JavaScript

## Run
1. Install Java 17, Maven and MySQL.
2. Check `src/main/resources/application.properties` and set your MySQL username/password.
3. Run `mvn spring-boot:run` or run `BusPassTrackerApplication`.
4. Open `http://localhost:8082/`.

The database `buspass_db` is created automatically when the MySQL user has permission.

## Main APIs
- POST/GET/GET by id/PUT/DELETE `/api/students`
- POST/GET/GET by id/PUT/DELETE `/api/routes`
- POST/GET/GET by id `/api/applications`
- GET `/api/applications/student/{studentId}`
- GET `/api/applications/status/{status}`
- PUT `/api/applications/{id}/approve`
- PUT `/api/applications/{id}/reject`
- GET `/api/applications/expiring?days=30`

## Business rules
1. A student cannot create a new application while they already have an unexpired APPROVED pass.
2. REJECTED applications require a rejection reason.
3. APPROVED applications receive a unique pass number and six-month validity.

## Application JSON
{
  "studentId": 1,
  "routeId": 1,
  "boardingPoint": "Central Bus Stand",
  "photoReference": "student_photo_01.jpg"
}

## Approval JSON
{
  "adminRemark": "Documents verified"
}

## Rejection JSON
{
  "rejectionReason": "Invalid document",
  "adminRemark": "Please submit a valid document"
}
