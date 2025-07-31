package com.training.query.Training.Query.collections;

import com.training.query.Training.Query.repository.JobRepository;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import com.training.query.Training.Query.service.*;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TrainingTrackingTask implements Runnable {

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    @Lazy
    private SchedulerService schedulerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;
    @Autowired
    private TrainingAssociationRepository taRepo;
    @Autowired
    private DroolsTrainingEvaluatorService droolsSvc;
//    @Autowired
//    private JobRepository jobRepo;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private DroolsTrainingEvaluatorService droolsTrainingEvaluatorService;


    @Autowired
    @Qualifier("droolsTrainingEvaluatorService")
    private DroolsTrainingEvaluatorService evaluator;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private KieContainer kieContainer;


//    public void runEscalationRule() {
//        List<TrainingAssociation> allAssociations = trainingAssociationRepository.findAll();
//        List<TrainingAssociation> escalationList = new ArrayList<>();
//
//        KieSession kieSession = kieContainer.newKieSession();
//        kieSession.setGlobal("escalationList", escalationList);
//
//        allAssociations.forEach(kieSession::insert);
//        kieSession.fireAllRules();
//        kieSession.dispose();
//
//
//        for (TrainingAssociation ta : escalationList) {
//
//        }
//        Job job = new Job();
//        job.setJobName("rules/training-escalation");
//        job.setStatus("COMPLETED");
//        job.setRunTime(new Date());
//        jobRepo.save(job);
//    }


    @Override
    public void run() {
        List<TrainingAssociation> allAssociations = trainingAssociationRepository.findByStatusIn(List.of("Pending", "Escalated"));
        List<TrainingAssociation> escalationList = new ArrayList<>();

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("escalationList", escalationList);
        allAssociations.forEach(kieSession::insert);
        kieSession.fireAllRules();
        kieSession.dispose();


        Map<String, Employee> employeeMap = new HashMap<>();
        Map<String, Training> trainingMap = new HashMap<>();
        Map<Manager, List<TrainingAssociation>> managerToEscalations = new HashMap<>();

        for (TrainingAssociation ta : escalationList) {
            Employee emp = employeeService.getEmployeeById(ta.getEmployeeId());
            if (emp == null) continue;

            Training training = trainingService.getTrainingById(ta.getTrainingId());
            if (training == null) continue;

            Manager manager = managerService.getManagerByEmployeeId(emp.getId());
            if (manager == null) continue;

            employeeMap.put(emp.getId(), emp);
            trainingMap.put(training.getId(), training);
            managerToEscalations.computeIfAbsent(manager, k -> new ArrayList<>()).add(ta);
        }


        for (Map.Entry<Manager, List<TrainingAssociation>> entry : managerToEscalations.entrySet()) {
            mailService.sendEscalationToManager(entry.getKey(), entry.getValue(), employeeMap, trainingMap);

            System.out.println("Escalation mail sent to: " + entry.getKey().getEmail());
        }



        for (TrainingAssociation ta : allAssociations) {
            if (!escalationList.contains(ta)) {
                Employee emp = employeeService.getEmployeeById(ta.getEmployeeId());
                if (emp == null) continue;

                Manager manager = managerService.getManagerByEmployeeId(emp.getId());
                if (manager == null) continue;

                mailService.sendReminderToEmployee(manager, emp, ta);
                System.out.println("Reminder mail sent to: " + emp.getEmail());
            }
        }

        Job job = new Job();
        job.setJobName("training-tracking");
        job.setStatus("SUCCESS");
        job.setRunTime(new Date());
        jobRepository.save(job);
    }
}