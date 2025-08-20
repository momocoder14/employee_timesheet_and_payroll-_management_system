package com.employee.payroll.controller;

import com.employee.payroll.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/payroll/pdf")
    public ResponseEntity<?> generatePayrollReport(
            @RequestParam Long empId,
            @RequestParam String from,
            @RequestParam String to) {

        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            if (fromDate.isAfter(toDate)) {
                return ResponseEntity.badRequest().body("Invalid date range: 'from' date is after 'to' date.");
            }

            byte[] report = reportService.generatePayrollReport(empId, fromDate, toDate);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payroll_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);

        } catch (Exception e) {
            logger.error("Error generating payroll PDF report", e);
            return ResponseEntity.status(500).body("Failed to generate payroll report.");
        }
    }

    @GetMapping("/payroll/chart")
    public ResponseEntity<?> getPayrollChartData(
            @RequestParam Long empId,
            @RequestParam String from,
            @RequestParam String to) {

        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            if (fromDate.isAfter(toDate)) {
                return ResponseEntity.badRequest().body("Invalid date range: 'from' date is after 'to' date.");
            }

            Map<String, Object> chartData = reportService.getPayrollChartData(empId, fromDate, toDate);
            return ResponseEntity.ok(chartData);

        } catch (Exception e) {
            logger.error("Error fetching payroll chart data", e);
            return ResponseEntity.status(500).body("Failed to retrieve payroll chart data.");
        }
    }
}
