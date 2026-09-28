package com.buspass.bus_pass_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.buspass.bus_pass_tracker.entity.ApplicationStatus;
import com.buspass.bus_pass_tracker.entity.PassApplication;

public interface PassApplicationRepository
        extends JpaRepository<PassApplication, Long> {

    List<PassApplication>
    findByStudentIdOrderByAppliedAtDesc(Long studentId);

    boolean existsByStudent_IdAndStatus(Long studentId, ApplicationStatus status);

    List<PassApplication>
    findByStatusOrderByAppliedAtDesc(ApplicationStatus status);

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
        FROM PassApplication a
        WHERE a.student.id = :studentId
        AND a.status = :status
        AND a.validityStart <= :today
        AND a.validityEnd >= :today
    """)
    boolean existsActivePass(
            @Param("studentId") Long studentId,
            @Param("status") ApplicationStatus status,
            @Param("today") LocalDate today
    );

    @Query("""
        SELECT a
        FROM PassApplication a
        WHERE a.status = :status
        AND a.validityEnd BETWEEN :today AND :limitDate
        ORDER BY a.validityEnd ASC
    """)
    List<PassApplication> findExpiringPasses(
            @Param("status") ApplicationStatus status,
            @Param("today") LocalDate today,
            @Param("limitDate") LocalDate limitDate
    );
}