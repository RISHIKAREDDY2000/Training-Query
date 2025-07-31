package com.training.query.Training.Query.service;
import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.repository.EmployeeRepository;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.event.DebugAgendaEventListener;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Log4j2
public class DroolsTrainingEvaluatorService {
    @Autowired
    private KieContainer kieContainer;
    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(DroolsTrainingEvaluatorService.class);


    public DroolsTrainingEvaluatorService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }
    public String evaluateEscalationRule(TrainingAssociation ta) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(ta);
        kieSession.fireAllRules();
        kieSession.dispose();

        return "Escalation rule evaluated for: " + ta.getTrainingId();
    }

//    public void runEscalationRule() {
//        KieSession kieSession = kieContainer.newKieSession();
//
//        List<TrainingAssociation> escalationList = new ArrayList<>();
//        kieSession.setGlobal("escalationList", escalationList);
//        kieSession.setGlobal("thresholdDate", Date.from(Instant.now().plus(3, ChronoUnit.DAYS)));
//
//        List<TrainingAssociation> tas = trainingAssociationRepository.findByStatusNot("Completed");
//        Date thresholdDate = Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
//        kieSession.setGlobal("thresholdDate", thresholdDate); // ✅ Now this will work
//        kieSession.setGlobal("escalationList", new ArrayList<TrainingAssociation>());
//
//        for (TrainingAssociation ta : tas) {
//            kieSession.insert(ta);
//        }
//
//
//
//
//        int fired = kieSession.fireAllRules();
//        System.out.println("Drools rules executed. Total rules fired: " + fired);
//        kieSession.fireAllRules();
//        kieSession.dispose();
//        kieSession.dispose();
//
//    }

    public void runEscalationRule() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee emp : employees) {
            List<TrainingAssociation> trainings = trainingAssociationRepository.findByEmployeeId(emp.getId());

            for (TrainingAssociation ta : trainings) {
                // Only escalate if not completed and due in ≤ 3 days
                if ("Completed".equalsIgnoreCase(ta.getStatus())) continue;
                if (ta.getDueDate() == null) continue;

                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(),
                        ta.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                if (daysLeft <= 3) {
                   log System.out.println("Escalating training for employeeId: " + emp.getId());


                }
            }
        }
    }

    public void evaluate(Employee employee, TrainingAssociation ta) {
        KieSession kieSession = kieContainer.newKieSession();
        List<TrainingAssociation> escalationList = new ArrayList<>();
        kieSession.setGlobal("escalationList", escalationList);
        kieSession.insert(employee);
        kieSession.insert(ta);
        kieSession.fireAllRules();
        kieSession.dispose();
    }


    public List<TrainingAssociation> evaluatePendingTrainings(List<TrainingAssociation> associations) {
        List<TrainingAssociation> result = new ArrayList<>();

        KieSession kieSession = kieContainer.newKieSession();

        kieSession.setGlobal("reminderList", result);
        kieSession.setGlobal("thresholdDate", Date.from(Instant.now().plus(3, ChronoUnit.DAYS)));

        associations.forEach(kieSession::insert);

        kieSession.addEventListener(new DebugAgendaEventListener());
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void beforeMatchFired(BeforeMatchFiredEvent event) {
                String ruleName = event.getMatch().getRule().getName();
                if (ruleName.contains("Escalate")) {
                    logger.info("Escalation rule fired: {} on {}", ruleName, event.getMatch().getObjects());
                }
            }
        });

        kieSession.fireAllRules();
        kieSession.dispose();

        return result;
    }
}
