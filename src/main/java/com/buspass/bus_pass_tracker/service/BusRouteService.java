package com.buspass.bus_pass_tracker.service;

import com.buspass.bus_pass_tracker.entity.BusRoute;
import com.buspass.bus_pass_tracker.exception.BusinessRuleException;
import com.buspass.bus_pass_tracker.exception.ResourceNotFoundException;
import com.buspass.bus_pass_tracker.repository.BusRouteRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusRouteService {

    private final BusRouteRepository repository;

    public BusRouteService(
            BusRouteRepository repository) {

        this.repository = repository;
    }

    public BusRoute create(BusRoute route) {

        if (repository.existsByRouteNumber(
                route.getRouteNumber())) {

            throw new BusinessRuleException(
                    "Route number already exists"
            );
        }

        return repository.save(route);
    }

    public List<BusRoute> getAll() {
        return repository.findAll();
    }

    public BusRoute getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus route not found with id: " + id
                        )
                );
    }

    public BusRoute update(Long id, BusRoute input) {

        BusRoute existing = getById(id);

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

    public void delete(Long id) {

        BusRoute route = getById(id);

        repository.delete(route);
    }
}