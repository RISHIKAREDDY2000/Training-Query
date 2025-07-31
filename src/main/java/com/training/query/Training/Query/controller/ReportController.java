package com.training.query.Training.Query.controller;



import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import com.training.query.Training.Query.service.EmployeeService;
import com.training.query.Training.Query.service.JasperReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private JasperReportService jasperReportService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @GetMapping("/all")
    @Operation(summary = "Generate report for all employee training records")
    public void generateAllReport(HttpServletResponse response) throws Exception {
        jasperReportService.generateAllEmployeeReport(response);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Generate report for a specific employee")
    public void generateReportByEmployee(@PathVariable String employeeId, HttpServletResponse response) throws Exception {
        jasperReportService.generateReportByEmployee(employeeId, response);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Generate report filtered by assigned date range")
    public void generateReportByDateRange(
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            HttpServletResponse response) throws Exception {
        jasperReportService.generateReportByDateRange(fromDate, toDate, response);
    }
}