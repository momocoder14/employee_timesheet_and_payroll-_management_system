package com.employee.payroll.repository;

import com.employee.payroll.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployeeId(Long employeeId);
    List<Timesheet> findByEmployeeIdAndDateBetweenAndStatusIn(Long employeeId, LocalDate from, LocalDate to, List<String> statuses);

    List<Timesheet> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Timesheet> findByEmployeeIdAndStatus(Long employeeId, Timesheet.TimesheetStatus status);

    List<Timesheet> findByStatus(Timesheet.TimesheetStatus status);

    @Query("SELECT COALESCE(SUM(t.hoursWorked), 0.0) FROM Timesheet t WHERE t.employee.id = :employeeId AND t.date BETWEEN :startDate AND :endDate")
    Double sumHoursWorkedByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}