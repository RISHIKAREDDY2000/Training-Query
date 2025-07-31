package com.training.query.Training.Query.service;

import com.training.query.Training.Query.collections.*;
import com.training.query.Training.Query.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.training.query.Training.Query.util.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingAssociationService {

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ManagerService managerService;

    public List<TrainingAssociation> getByEmployeeId(String employeeId) {
        return trainingAssociationRepository.findByEmployeeId(employeeId);
    }
    public List<TrainingAssociation> getByEmployeeIdAndTrainingId(String employeeId, String trainingId) {
        return trainingAssociationRepository.findByEmployeeIdAndTrainingId(employeeId, trainingId);
    }


    public TrainingAssociation updateStatus(String id, String status) {
        System.out.println("Searching TrainingAssociation with ID: " + id);
        Optional<TrainingAssociation> optional = trainingAssociationRepository.findById(id);

        if (optional.isEmpty()) {
            throw new RuntimeException("TrainingAssociation with ID " + id + " not found");
        }

        TrainingAssociation ta = optional.get();
        ta.setStatus(status);
        TrainingAssociation updated = trainingAssociationRepository.save(ta);

        Employee emp = employeeRepository.findById(ta.getEmployeeId()).orElse(null);

        if (emp != null) {
            if ("PENDING".equalsIgnoreCase(status)) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(),
                        ta.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                if (daysLeft <= 3) {
                    Manager manager = managerService.getManagerByEmployeeId(emp.getId());
                    mailService.sendReminderToEmployee(manager, emp, ta);
                }
            } else if ("COMPLETED".equalsIgnoreCase(status)) {
                System.out.println("Calling sendCompletionMail...");
                mailService.sendCompletionMail(emp, ta);
            }
        }

        return updated;
    }

    public void assignTraining(Employee emp, Training training, Date assignedDate, Date startDate, Date dueDate) {
        TrainingAssociation ta = new TrainingAssociation();
        ta.setEmployeeId(emp.getId());
        ta.setTrainingId(training.getId());
        ta.setAssignedDate(assignedDate);
        ta.setStartDate(startDate);
        ta.setDueDate(dueDate);
        ta.setStatus("Assigned");
        ta.setDueInNext3Days(false);

        trainingAssociationRepository.save(ta);
        mailService.sendTrainingAssignedMail(emp, ta);
    }

    public TrainingAssociation assignTraining(TrainingAssociation ta) {
        Employee emp = employeeRepository.findById(ta.getEmployeeId()).orElse(null);
        Training training = trainingRepository.findById(ta.getTrainingId()).orElse(null);
        if (emp == null || training == null) return null;

        assignTraining(emp, training, ta.getAssignedDate(), ta.getStartDate(), ta.getDueDate());
        return ta;
    }

    public void assignTrainingsForNewEmployee(Employee emp) {
        Integer designationInt;
        try {
            designationInt = Integer.parseInt(emp.getDesignationId());
        } catch (NumberFormatException e) {
            System.err.println("Invalid designationId: " + emp.getDesignationId());
            return;
        }

        Optional<Department> deptOpt = departmentRepository.findById(emp.getDepartmentId());
        Optional<Designation> desigOpt = designationRepository.findById(designationInt);

        if (deptOpt.isEmpty() || desigOpt.isEmpty()) {
            System.err.println("Invalid department or designation for: " + emp.getName());
            return;
        }

        String deptName = deptOpt.get().getName();
        String desigTitle = desigOpt.get().getTitle();
        List<Category> categories = categoryRepository.findByDepartmentAndDesignation(deptName, desigTitle);

        if (categories == null || categories.isEmpty()) {
            System.err.println("No category found for department/designation");
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
                    ta.setDueInNext3Days(false);

                    trainingAssociationRepository.save(ta);
                    mailService.sendTrainingAssignedMail(emp, ta);
                }
            }
        }
    }

    public TrainingAssociation updateTrainingStatus(String id, String newStatus) {
        System.out.println("update status for ID = " + id + ", status = " + newStatus);
        Optional<TrainingAssociation> optional = trainingAssociationRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("TrainingAssociation with ID " + id + " not found");
        }

        TrainingAssociation ta = optional.get();
        ta.setStatus(newStatus);
        trainingAssociationRepository.save(ta);

        Employee emp = employeeRepository.findById(ta.getEmployeeId()).orElse(null);
        if (emp != null) {
            if ("COMPLETED".equalsIgnoreCase(newStatus)) {
                mailService.sendCompletionMail(emp, ta);
            } else if ("PENDING".equalsIgnoreCase(newStatus)) {
                Manager mgr = managerService.getManagerByEmployeeId(emp.getId());
                mailService.sendReminderToEmployee(mgr, emp, ta);
            }
        }

        return ta;
    }
}
