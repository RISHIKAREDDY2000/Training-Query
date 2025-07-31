package com.training.query.Training.Query.collections;

import com.training.query.Training.Query.repository.*;
import com.training.query.Training.Query.service.EmployeeService;
import com.training.query.Training.Query.service.MailService;
import com.training.query.Training.Query.service.SchedulerService;
import com.training.query.Training.Query.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.training.query.Training.Query.collections.Job;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component("training-assignment")
public class TrainingAssignmentTask implements Runnable {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @Autowired
    private TrainingDurationRepository trainingDurationRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MailService mailService;

    public ResponseEntity<?> getTrainingsForEmployee(String empId) {
        List<TrainingAssociation> list = trainingAssociationRepository.findByEmployeeId(empId);
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().body("No trainings found: Check if Category/Department mappings exist.");
        }
        return ResponseEntity.ok(list);
    }


    private Date calculateDueDate(String department) {
        int weeks = switch (department) {
            case "Engineer" -> 2;
            case "HR" -> 3;
            default -> 2;
        };
        LocalDate dueDate = LocalDate.now().plusWeeks(weeks);
        return Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    //    @Override
//    public void run() {
//        List<Employee> employees = employeeService.getAllEmployees();
//        for (Employee emp : employees) {
//
//            String deptName = departmentRepository.findById(emp.getDepartmentId())
//                    .map(Department::getName).orElse(null);
//            String desigTitle = designationRepository.findById(emp.getDesignationId())
//                    .map(Designation::getTitle).orElse(null);
//
//
//            if (deptName == null || desigTitle == null) continue;
//            System.out.println("Emp: " + emp.getName() + ", Dept: " + deptName + ", Desig: " + desigTitle);
//
//            List<Category> categories = categoryRepository.findByDepartmentAndDesignation(deptName, desigTitle);
//
//
//            System.out.println("Matched categories: " + categories.size());
//
//            for (Category category : categories) {
//
//                for (String trainingId : category.getTrainingIds()) {
//                    boolean alreadyAssigned = trainingAssociationRepository.existsByEmployeeIdAndTrainingId(emp.getId(), trainingId);
//                    if (!alreadyAssigned) {
//                        TrainingAssociation ta = new TrainingAssociation(emp.getId(), trainingId, "ASSIGNED");
//                        Date assignedDate = new Date();
//                        ta.setAssignedDate(assignedDate);
//                        ta.setDueDate(calculateDueDate(deptName));
//                        trainingAssociationRepository.save(ta);
//
//                        TrainingDuration td = new TrainingDuration();
//                        td.setEmployeeId(emp.getId());
//                        td.setTrainingId(trainingId);
//                        td.setDuration("2-3 Weeks");
//                        td.setMonth(getCurrentQuarter());
//                        td.setYear(Calendar.getInstance().get(Calendar.YEAR));
//                        trainingDurationRepository.save(td);
//                    }
//                }
//            }
//        }
//
//        Job job = new Job();
//        job.setJobName("training assignment");
//        job.setStatus("SUCCESS");
//        job.setRunTime(new Date());
//        schedulerService.saveJobExecution(job);
//    }
//    private String getCurrentQuarter() {
//        int month = Calendar.getInstance().get(Calendar.MONTH);
//        return switch (month / 3) {
//            case 0 -> "Q1";
//            case 1 -> "Q2";
//            case 2 -> "Q3";
//            default -> "Q4";
//        };
//    }
    public void assignTrainingsToNewEmployee(Employee employee) {
        List<Training> trainings = trainingRepository.findAll(); // or based on dept/designation

        for (Training training : trainings) {
            TrainingAssociation assoc = new TrainingAssociation();
            assoc.setEmployeeId(employee.getId()); // Now String
            assoc.setTrainingId(training.getId()); // Now String
            assoc.setStatus("Assigned");
            assoc.setAssignedDate(new Date());


            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 15);
            assoc.setDueDate(cal.getTime());

            trainingAssociationRepository.save(assoc);
        }
    }

    @Override
    public void run() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            String departmentName = departmentRepository.findById(employee.getDepartmentId()).map(Department::getName).orElse(null);
            String designationTitle = designationRepository.findById(employee.getDesignationId()).map(Designation::getTitle).orElse(null);

            if (departmentName == null || designationTitle == null) continue;

            Optional<Category> categoryOpt = categoryRepository.findByDesignationAndDepartment(designationTitle, departmentName);

            if (categoryOpt.isPresent()) {
                Category category = categoryOpt.get();
                List<String> trainingIds = category.getTrainingIds();

                for (String trainingId : trainingIds) {
                    if (!trainingAssociationRepository.existsByEmployeeIdAndTrainingId(employee.getId(), trainingId)) {
                        Date assignedDate = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(assignedDate);
                        cal.add(Calendar.DAY_OF_MONTH, 7);
                        Date startDate = cal.getTime();
                        cal.add(Calendar.DAY_OF_MONTH, 14);
                        Date dueDate = cal.getTime();

                        TrainingAssociation ta = new TrainingAssociation();
                        ta.setEmployeeId(employee.getId());
                        ta.setTrainingId(trainingId);
                        ta.setStatus("Assigned");
                        ta.setAssignedDate(assignedDate);
                        ta.setStartDate(startDate);
                        ta.setDueDate(dueDate);

                        trainingAssociationRepository.save(ta);
                        mailService.sendMailToEmployee(employee, ta, "Training Assigned");
                    }
                }
            }
        }
    }
}