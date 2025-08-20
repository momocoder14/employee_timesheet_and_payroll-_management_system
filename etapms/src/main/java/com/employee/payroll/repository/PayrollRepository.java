package com.employee.payroll.repository;

import com.employee.payroll.entity.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollRecord, Long> {
    List<PayrollRecord> findByEmployeeId(Long employeeId);

    List<PayrollRecord> findByPayDateBetween(LocalDate startDate, LocalDate endDate);

    List<PayrollRecord> findByEmployeeIdAndPayDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<PayrollRecord> findByPayPeriod(String payPeriod);

    @Query("SELECT COALESCE(SUM(pr.grossPay), 0.0) FROM PayrollRecord pr WHERE pr.employee.id = :employeeId")
    Double sumGrossPayByEmployeeId(Long employeeId);

    @Query("SELECT COALESCE(SUM(pr.netPay), 0.0) FROM PayrollRecord pr WHERE pr.employee.id = :employeeId")
    Double sumNetPayByEmployeeId(Long employeeId);

}