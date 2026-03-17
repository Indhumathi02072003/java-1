package com.ic.attendance_service.repository;

import com.ic.attendance_service.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, UUID> {}
