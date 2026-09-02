package com.pooja.employee_management_system.repository;

import com.pooja.employee_management_system.entity.Attendance;
import com.pooja.employee_management_system.entity.AttendanceStatus;
import com.pooja.employee_management_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Check whether attendance is already marked today
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);

    // Employee attendance history
    List<Attendance> findByEmployeeOrderByDateDesc(Employee employee);

    // Attendance by date
    List<Attendance> findByDate(LocalDate date);

    // Attendance by status
    List<Attendance> findByStatus(AttendanceStatus status);

    // Attendance by date and status
    List<Attendance> findByDateAndStatus(LocalDate date, AttendanceStatus status);

    // Count Present
    long countByDateAndStatus(LocalDate date, AttendanceStatus status);

    // Count attendance of an employee by status
    long countByEmployeeAndStatus(Employee employee, AttendanceStatus status);

    // Monthly attendance of employee
    List<Attendance> findByEmployeeAndDateBetween(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate
    );
}
