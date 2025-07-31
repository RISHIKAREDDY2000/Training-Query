package com.training.query.Training.Query;

import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.collections.Manager;
import com.training.query.Training.Query.collections.Training;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.controller.TrainingController;
import com.training.query.Training.Query.repository.ManagerRepository;
import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;
import com.training.query.Training.Query.service.EmployeeService;
import com.training.query.Training.Query.service.MailService;
import com.training.query.Training.Query.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EscalationScheduler {
    @Autowired private TrainingController trainingController;

    @Autowired private MailService mailService;
    @Autowired private DroolsTrainingEvaluatorService evaluator;
    @Autowired private EmployeeService employeeService;
    @Autowired private TrainingService trainingService;
    @Autowired private ManagerRepository managerRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    public void dailyCheck() {
        List<TrainingAssociation> all = trainingService.getAll();
        List<TrainingAssociation> escalations = evaluator.evaluatePendingTrainings(all);

        for (TrainingAssociation ta : escalations) {
            Employee employee = employeeService.getEmployeeById(ta.getEmployeeId());
            Training training = trainingService.getTrainingById(ta.getTrainingId());
            Optional<Manager> mgrOpt = managerRepository.findByDeptId(employee.getDepartmentId());
            mgrOpt.ifPresent(manager ->
                    mailService.notifyManager(manager, employee, training, ta)
            );

        }
        }
    }
