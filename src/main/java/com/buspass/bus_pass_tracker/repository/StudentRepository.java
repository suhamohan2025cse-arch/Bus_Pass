package com.buspass.bus_pass_tracker.repository;

import com.buspass.bus_pass_tracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository
        extends JpaRepository<Student, Long> {

    boolean existsByRegisterNumber(String registerNumber);
}