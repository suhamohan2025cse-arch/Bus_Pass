package com.buspass.bus_pass_tracker.repository;

import com.buspass.bus_pass_tracker.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRouteRepository
        extends JpaRepository<BusRoute, Long> {

    boolean existsByRouteNumber(String routeNumber);
}