package com.employee.payroll.service;

import com.employee.payroll.entity.PayrollRecord;
import com.employee.payroll.repository.PayrollRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private PayrollRepository payrollRepo;

    public byte[] generatePayrollReport(Long empId, LocalDate from, LocalDate to) throws JRException {
        List<PayrollRecord> records = payrollRepo.findByEmployeeIdAndPayDateBetween(empId, from, to);

        InputStream reportTemplate = getClass().getResourceAsStream("/reports/payroll_report_template.jrxml");
        if (reportTemplate == null) {
            throw new JRException("Report template not found");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(records);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("EmployeeId", empId);
        parameters.put("FromDate", from.toString());
        parameters.put("ToDate", to.toString());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Map<String, Object> getPayrollChartData(Long empId, LocalDate from, LocalDate to) {
        List<PayrollRecord> records = payrollRepo.findByEmployeeIdAndPayDateBetween(empId, from, to);

        List<String> labels = new ArrayList<>();
        List<Double> grossPay = new ArrayList<>();
        List<Double> netPay = new ArrayList<>();
        List<Double> tax = new ArrayList<>();

        for (PayrollRecord r : records) {
            labels.add(r.getPayPeriod());
            grossPay.add(r.getGrossPay());
            netPay.add(r.getNetPay());
            tax.add(r.getTax());
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("grossPay", grossPay);
        chartData.put("netPay", netPay);
        chartData.put("tax", tax);

        return chartData;
    }

}