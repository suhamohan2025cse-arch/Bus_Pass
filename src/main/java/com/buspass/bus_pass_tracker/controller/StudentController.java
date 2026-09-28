package com.buspass.bus_pass_tracker.controller;

import com.buspass.bus_pass_tracker.entity.Student;
import com.buspass.bus_pass_tracker.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(
            StudentService service) {

        this.service = service;
    }


    @PostMapping
    public ResponseEntity<Student> create(
            @Valid @RequestBody Student student) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(student));
    }


    @GetMapping
    public List<Student> getAll() {

        return service.getAll();
    }


    @GetMapping("/{id}")
    public Student getById(
            @PathVariable Long id) {

        return service.getById(id);
    }


    @PutMapping("/{id}")
    public Student update(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {

        return service.update(id, student);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}