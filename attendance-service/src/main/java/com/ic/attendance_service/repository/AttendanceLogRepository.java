package com.ic.attendance_service.repository;

import com.ic.attendance_service.entity.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    List<AttendanceLog> findByEmpIdAndCheckInBetween(
            UUID empId, LocalDateTime start, LocalDateTime end);
    Optional<AttendanceLog> findTopByEmpIdAndCheckOutIsNullOrderByCheckInDesc(UUID empId);


}

