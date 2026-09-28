package com.buspass.bus_pass_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PassApplicationRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;
    @NotNull(message = "Route ID is required")
    private Long routeId;
    @NotBlank(message = "Boarding point is required")
    private String boardingPoint;
    private String photoReference;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public String getBoardingPoint() { return boardingPoint; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }
    public String getPhotoReference() { return photoReference; }
    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }
}
