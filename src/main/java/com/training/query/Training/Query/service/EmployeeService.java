package com.training.query.Training.Query.service;

import com.training.query.Training.Query.EmployeeDTO;
import com.training.query.Training.Query.collections.*;
import com.training.query.Training.Query.repository.*;
import com.training.query.Training.Query.util.QuarterUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.ZoneId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    @Lazy
    private TrainingTrackingTask trainingTrackingTask;
    @Autowired
    private TrainingAssociationService trainingAssociationService;


    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void assignTrainingsForNewEmployee(Employee emp) {

        Integer designationInt;
        try {
            designationInt = Integer.parseInt(emp.getDesignationId());
        } catch (NumberFormatException e) {
            System.err.println("Invalid designationId format: " + emp.getDesignationId());
            return;
        }


        Optional<Department> deptOpt = departmentRepository.findById(emp.getDepartmentId());
        Optional<Designation> desigOpt = designationRepository.findById(designationInt);

        if (deptOpt.isEmpty() || desigOpt.isEmpty()) {
            System.err.println("Invalid dept/designation for employee: " + emp.getId());
            return;
        }

        String deptName = deptOpt.get().getName();
        String desigTitle = desigOpt.get().getTitle();


        List<Category> categories = categoryRepository.findByDepartmentAndDesignation(deptName, desigTitle);
        if (categories == null || categories.isEmpty()) {
            System.err.println("No Category found for Dept: " + deptName + ", Desig: " + desigTitle);
            return;
        }


        for (Category category : categories) {
            for (String trainingId : category.getTrainingIds()) {
                boolean exists = trainingAssociationRepository.existsByEmployeeIdAndTrainingId(emp.getId(), trainingId);
                if (!exists) {
                    TrainingAssociation ta = new TrainingAssociation();
                    ta.setEmployeeId(emp.getId());
                    ta.setTrainingId(trainingId);
                    ta.setStatus("Assigned");

                    LocalDate today = LocalDate.now();
                    ta.setAssignedDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    ta.setStartDate(Date.from(today.plusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    ta.setDueDate(Date.from(today.plusDays(21).atStartOfDay(ZoneId.systemDefault()).toInstant()));

                    trainingAssociationRepository.save(ta);
                    System.out.println("Assigned " + trainingId + " to " + emp.getName());
                }
            }
        }
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }




    public Employee addEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setDesignationId(dto.getDesignationId());

        Employee saved = employeeRepository.save(employee);
        trainingAssociationService.assignTrainingsForNewEmployee(saved);

        return saved;
    }


}
//
//        Integer designationInt = null;
//        try {
//            designationInt = Integer.parseInt(saved.getDesignationId());
//        } catch (NumberFormatException e) {
//            System.err.println("Invalid designationId: " + saved.getDesignationId());
//            return saved;
//        }
//
//
//        Optional<Department> deptOpt = departmentRepository.findById(saved.getDepartmentId());
//        Optional<Designation> desigOpt = designationRepository.findById(designationInt);
//
//        if (deptOpt.isEmpty() || desigOpt.isEmpty()) {
//            System.err.println("Invalid dept/designation for employee: " + saved.getId());
//            return saved;
//        }
//
//        String deptName = deptOpt.get().getName();
//        String desigTitle = desigOpt.get().getTitle();
//
//
//        List<Category> categories = categoryRepository.findByDepartmentAndDesignation(deptName, desigTitle);
//        if (categories == null || categories.isEmpty()) {
//            System.err.println("No Category found for Dept: " + deptName + ", Desig: " + desigTitle);
//            return saved;
//        }
//
//
//        for (Category category : categories) {
//            for (String trainingId : category.getTrainingIds()) {
//                boolean exists = trainingAssociationRepository.existsByEmployeeIdAndTrainingId(saved.getId(), trainingId);
//                if (!exists) {
//                    TrainingAssociation ta = new TrainingAssociation();
//                    ta.setEmployeeId(saved.getId());
//                    ta.setTrainingId(trainingId);
//                    ta.setStatus("Assigned");
//
//                    LocalDate today = LocalDate.now();
//                    ta.setAssignedDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                    ta.setStartDate(Date.from(today.plusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                    ta.setDueDate(Date.from(today.plusDays(21).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
  //                 trainingAssociationRepository.save(ta);
 //                  System.out.println("Assigned " + trainingId + " to " + saved.getName());
//                    mailService.sendMailToEmployee(saved, ta, "assignment");
//
//                }
//            }
//        }
//        trainingTrackingTask.run();
//
//        return saved;
//    }
