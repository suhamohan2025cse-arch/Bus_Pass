package com.buspass.bus_pass_tracker.controller;

import com.buspass.bus_pass_tracker.dto.ApprovalRequest;
import com.buspass.bus_pass_tracker.dto.PassApplicationRequest;
import com.buspass.bus_pass_tracker.dto.RejectionRequest;

import com.buspass.bus_pass_tracker.entity.ApplicationStatus;
import com.buspass.bus_pass_tracker.entity.PassApplication;

import com.buspass.bus_pass_tracker.service.PassApplicationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class PassApplicationController {

    private final PassApplicationService service;


    public PassApplicationController(
            PassApplicationService service) {

        this.service = service;
    }


    // Student submits application

    @PostMapping
    public ResponseEntity<PassApplication> apply(
            @Valid @RequestBody
            PassApplicationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.apply(request));
    }


    // Admin/student can see all applications

    @GetMapping
    public List<PassApplication> getAll() {

        return service.getAll();
    }


    // Get one application

    @GetMapping("/{id}")
    public PassApplication getById(
            @PathVariable Long id) {

        return service.getById(id);
    }


    // Student tracks applications

    @GetMapping("/student/{studentId}")
    public List<PassApplication> getByStudent(
            @PathVariable Long studentId) {

        return service.getByStudent(studentId);
    }


    // Filter by status

    @GetMapping("/status/{status}")
    public List<PassApplication> getByStatus(
            @PathVariable ApplicationStatus status) {

        return service.getByStatus(status);
    }


    // Admin approves

    @PutMapping("/{id}/approve")
    public PassApplication approve(
            @PathVariable Long id,
            @Valid @RequestBody
            ApprovalRequest request) {

        return service.approve(id, request);
    }


    // Admin rejects

    @PutMapping("/{id}/reject")
    public PassApplication reject(
            @PathVariable Long id,
            @Valid @RequestBody
            RejectionRequest request) {

        return service.reject(id, request);
    }


    // Expiring passes

    @GetMapping("/expiring")
    public List<PassApplication> getExpiring(
            @RequestParam(defaultValue = "30")
            int days) {

        return service.getExpiringPasses(days);
    }
}