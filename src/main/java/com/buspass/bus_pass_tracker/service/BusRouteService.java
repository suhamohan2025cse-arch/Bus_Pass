package com.buspass.bus_pass_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.buspass.bus_pass_tracker.entity.BusRoute;
import com.buspass.bus_pass_tracker.exception.BusinessRuleException;
import com.buspass.bus_pass_tracker.exception.ResourceNotFoundException;
import com.buspass.bus_pass_tracker.repository.BusRouteRepository;

@Service
public class BusRouteService {

    private final BusRouteRepository repository;

    public BusRouteService(
            BusRouteRepository repository) {

        this.repository = repository;
    }

    // Create Bus Route
    public BusRoute create(BusRoute route) {

        if (route.getRouteNumber() == null
                || route.getRouteNumber().isBlank()) {

            throw new BusinessRuleException(
                    "Route number is required"
            );
        }

        if (route.getRouteName() == null
                || route.getRouteName().isBlank()) {

            throw new BusinessRuleException(
                    "Route name is required"
            );
        }

        if (route.getBoardingPoint() == null
                || route.getBoardingPoint().isBlank()) {

            throw new BusinessRuleException(
                    "Boarding point is required"
            );
        }

        if (route.getDestination() == null
                || route.getDestination().isBlank()) {

            throw new BusinessRuleException(
                    "Destination is required"
            );
        }

        if (repository.existsByRouteNumber(
                route.getRouteNumber())) {

            throw new BusinessRuleException(
                    "Route number already exists"
            );
        }

        return repository.save(route);
    }

    // Get all routes
    public List<BusRoute> getAll() {
        return repository.findAll();
    }

    // Get route by ID
    public BusRoute getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus route not found with id: " + id
                        )
                );
    }

    // Update route
    public BusRoute update(Long id, BusRoute input) {

        BusRoute existing = getById(id);

        if (input.getRouteNumber() == null
                || input.getRouteNumber().isBlank()) {

            throw new BusinessRuleException(
                    "Route number is required"
            );
        }

        if (input.getRouteName() == null
                || input.getRouteName().isBlank()) {

            throw new BusinessRuleException(
                    "Route name is required"
            );
        }

        if (input.getBoardingPoint() == null
                || input.getBoardingPoint().isBlank()) {

            throw new BusinessRuleException(
                    "Boarding point is required"
            );
        }

        if (input.getDestination() == null
                || input.getDestination().isBlank()) {

            throw new BusinessRuleException(
                    "Destination is required"
            );
        }

        if (!existing.getRouteNumber()
                .equals(input.getRouteNumber())
                && repository.existsByRouteNumber(
                        input.getRouteNumber())) {

            throw new BusinessRuleException(
                    "Route number already exists"
            );
        }

        existing.setRouteNumber(
                input.getRouteNumber()
        );

        existing.setRouteName(
                input.getRouteName()
        );

        existing.setBoardingPoint(
                input.getBoardingPoint()
        );

        existing.setDestination(
                input.getDestination()
        );

        existing.setDistanceKm(
                input.getDistanceKm()
        );

        return repository.save(existing);
    }

    // Delete route
    public void delete(Long id) {

        BusRoute route = getById(id);

        repository.delete(route);
    }
}