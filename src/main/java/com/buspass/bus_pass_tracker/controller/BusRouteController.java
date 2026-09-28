package com.buspass.bus_pass_tracker.controller;

import com.buspass.bus_pass_tracker.entity.BusRoute;
import com.buspass.bus_pass_tracker.service.BusRouteService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class BusRouteController {

    private final BusRouteService service;

    public BusRouteController(
            BusRouteService service) {

        this.service = service;
    }


    @PostMapping
    public ResponseEntity<BusRoute> create(
            @Valid @RequestBody BusRoute route) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(route));
    }


    @GetMapping
    public List<BusRoute> getAll() {

        return service.getAll();
    }


    @GetMapping("/{id}")
    public BusRoute getById(
            @PathVariable Long id) {

        return service.getById(id);
    }


    @PutMapping("/{id}")
    public BusRoute update(
            @PathVariable Long id,
            @Valid @RequestBody BusRoute route) {

        return service.update(id, route);
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