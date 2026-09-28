package com.buspass.bus_pass_tracker.service;

import com.buspass.bus_pass_tracker.entity.Student;
import com.buspass.bus_pass_tracker.exception.BusinessRuleException;
import com.buspass.bus_pass_tracker.exception.ResourceNotFoundException;
import com.buspass.bus_pass_tracker.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student) {

        if (repository.existsByRegisterNumber(
                student.getRegisterNumber())) {

            throw new BusinessRuleException(
                    "Register number already exists"
            );
        }

        return repository.save(student);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + id
                        )
                );
    }

    public Student update(Long id, Student input) {

        Student existing = getById(id);

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

    public void delete(Long id) {

        Student student = getById(id);

        repository.delete(student);
    }
}