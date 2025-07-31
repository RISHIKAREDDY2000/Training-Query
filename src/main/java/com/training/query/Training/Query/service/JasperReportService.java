package com.training.query.Training.Query.service;


import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.collections.TrainingReportRow;
import com.training.query.Training.Query.repository.EmployeeRepository;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf;

@Service
public class JasperReportService {

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public void generateAllEmployeeReport(HttpServletResponse response) throws Exception {
        List<TrainingAssociation> allAssociations = trainingAssociationRepository.findAll();
        List<Map<String, Object>> reportData = new ArrayList<>();

        for (TrainingAssociation ta : allAssociations) {
            Map<String, Object> row = new HashMap<>();
            row.put("employeeId", ta.getEmployeeId());

            Employee emp = employeeRepository.findById(ta.getEmployeeId()).orElse(null);
            row.put("employeeName", emp != null ? emp.getName() : "N/A");

            row.put("trainingId", ta.getTrainingId());
            row.put("status", ta.getStatus().toLowerCase());


            row.put("assignedDate", ta.getAssignedDate());
            row.put("dueDate", ta.getDueDate());

            reportData.add(row);
        }

        InputStream reportStream = getClass().getResourceAsStream("/reports/training-report.jrxml");
        if (reportStream == null) {
            throw new FileNotFoundException("Jasper report file not found: /reports/training-report.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Training Management System");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"training_report.pdf\"");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        out.flush();
        out.close();
    }

    public void generateReportByEmployee(String employeeId, HttpServletResponse response) {
        try {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            if (employee == null) {
                throw new RuntimeException("Employee not found with ID: " + employeeId);
            }

            List<TrainingAssociation> associations = trainingAssociationRepository.findByEmployeeId(employeeId);
            List<Map<String, Object>> reportData = new ArrayList<>();

            for (TrainingAssociation ta : associations) {
                Map<String, Object> row = new HashMap<>();
                row.put("employeeId", employee.getId());
                row.put("employeeName", employee.getName());
                row.put("trainingId", ta.getTrainingId());
                row.put("status", ta.getStatus().toLowerCase());
                row.put("assignedDate", ta.getAssignedDate());
                row.put("dueDate", ta.getDueDate());
                reportData.add(row);
            }

            InputStream reportStream = getClass().getResourceAsStream("/reports/training-report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("employeeId", employee.getId());
            parameters.put("employeeName", employee.getName());
            parameters.put("createdBy", "Training Management System");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=employee_training_report.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate employee report: " + e.getMessage(), e);
        }
    }

    public void generateReportByDateRange(Date fromDate, Date toDate, HttpServletResponse response) {
        try {
            List<TrainingAssociation> filteredAssociations = trainingAssociationRepository.findAll().stream()
                    .filter(ta -> ta.getAssignedDate() != null &&
                            !ta.getAssignedDate().before(fromDate) &&
                            !ta.getAssignedDate().after(toDate))
                    .toList();

            List<Map<String, Object>> reportData = new ArrayList<>();

            for (TrainingAssociation ta : filteredAssociations) {
                Employee emp = employeeRepository.findById(ta.getEmployeeId()).orElse(null);
                Map<String, Object> row = new HashMap<>();
                row.put("employeeId", ta.getEmployeeId());
                row.put("employeeName", emp != null ? emp.getName() : "N/A");
                row.put("trainingId", ta.getTrainingId());
                row.put("status", ta.getStatus().toLowerCase());
                row.put("assignedDate", ta.getAssignedDate());
                row.put("dueDate", ta.getDueDate());
                reportData.add(row);
            }

            InputStream reportStream = getClass().getResourceAsStream("/reports/training-report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fromDate", fromDate.toString());
            parameters.put("toDate", toDate.toString());
            parameters.put("createdBy", "Training Management System");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=date_filtered_report.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate date range report: " + e.getMessage(), e);
        }
    }
}