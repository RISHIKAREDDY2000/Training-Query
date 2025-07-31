package com.training.query.Training.Query.config;

import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrainingEscalationScheduler {

    @Autowired
    private DroolsTrainingEvaluatorService droolsTrainingEvaluatorService;


    @Scheduled(cron = "0 0 9 * * *")
    public void runEscalationRuleDaily() {
        System.out.println("Running escalation rule at 9AM...");
        droolsTrainingEvaluatorService.runEscalationRule();
    }
}