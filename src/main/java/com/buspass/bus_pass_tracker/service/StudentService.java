package com.buspass.bus_pass_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.buspass.bus_pass_tracker.entity.Student;
import com.buspass.bus_pass_tracker.exception.BusinessRuleException;
import com.buspass.bus_pass_tracker.exception.ResourceNotFoundException;
import com.buspass.bus_pass_tracker.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // Create Student
    public Student create(Student student) {

        // Validate student name
        if (student.getName() == null || student.getName().isBlank()) {
            throw new BusinessRuleException(
                    "Student name is required"
            );
        }

        // Validate register number
        if (student.getRegisterNumber() == null
                || student.getRegisterNumber().isBlank()) {

            throw new BusinessRuleException(
                    "Register number is required"
            );
        }

        // Check duplicate register number
        if (repository.existsByRegisterNumber(
                student.getRegisterNumber())) {

            throw new BusinessRuleException(
                    "Register number already exists"
            );
        }

        return repository.save(student);
    }

    // Get all students
    public List<Student> getAll() {
        return repository.findAll();
    }

    // Get student by ID
    public Student getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + id
                        )
                );
    }

    // Update student
    public Student update(Long id, Student input) {

        Student existing = getById(id);

        // Validate student name
        if (input.getName() == null || input.getName().isBlank()) {
            throw new BusinessRuleException(
                    "Student name is required"
            );
        }

        // Validate register number
        if (input.getRegisterNumber() == null
                || input.getRegisterNumber().isBlank()) {

            throw new BusinessRuleException(
                    "Register number is required"
            );
        }

        // Check duplicate register number
        if (!existing.getRegisterNumber()
                .equals(input.getRegisterNumber())
                && repository.existsByRegisterNumber(
                        input.getRegisterNumber())) {

            throw new BusinessRuleException(
                    "Register number already exists"
            );
        }

        existing.setName(input.getName());
        existing.setRegisterNumber(input.getRegisterNumber());
        existing.setDepartment(input.getDepartment());
        existing.setYear(input.getYear());
        existing.setEmail(input.getEmail());
        existing.setPhone(input.getPhone());

        return repository.save(existing);
    }

    // Delete student
    public void delete(Long id) {

        Student student = getById(id);

        repository.delete(student);
    }
}