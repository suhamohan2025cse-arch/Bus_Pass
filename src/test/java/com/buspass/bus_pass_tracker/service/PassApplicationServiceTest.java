package com.buspass.bus_pass_tracker.service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.buspass.bus_pass_tracker.dto.PassApplicationRequest;
import com.buspass.bus_pass_tracker.entity.ApplicationStatus;
import com.buspass.bus_pass_tracker.entity.BusRoute;
import com.buspass.bus_pass_tracker.entity.Student;
import com.buspass.bus_pass_tracker.exception.BusinessRuleException;
import com.buspass.bus_pass_tracker.repository.BusRouteRepository;
import com.buspass.bus_pass_tracker.repository.PassApplicationRepository;
import com.buspass.bus_pass_tracker.repository.StudentRepository;

class PassApplicationServiceTest {
    private final PassApplicationRepository applicationRepository = mock(PassApplicationRepository.class);
    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final BusRouteRepository routeRepository = mock(BusRouteRepository.class);
    private final PassApplicationService service = new PassApplicationService(
            applicationRepository, studentRepository, routeRepository);

    private Student student;
    private BusRoute route;

    @BeforeEach
    void setUp() {
        student = mock(Student.class);
        route = mock(BusRoute.class);
        when(student.getId()).thenReturn(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(routeRepository.findById(2L)).thenReturn(Optional.of(route));
        when(applicationRepository.existsActivePass(1L, ApplicationStatus.APPROVED,
            LocalDate.now())).thenReturn(false);
    }

    @Test
    void applyRejectsStudentWithPendingApplication() {
        when(applicationRepository.existsByStudent_IdAndStatus(1L, ApplicationStatus.PENDING))
                .thenReturn(true);
        PassApplicationRequest request = new PassApplicationRequest();
        request.setStudentId(1L);
        request.setRouteId(2L);

        assertThrows(BusinessRuleException.class, () -> service.apply(request));
        verify(applicationRepository, never()).save(any());
    }
}