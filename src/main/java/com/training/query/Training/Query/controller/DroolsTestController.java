package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/drools")
public class DroolsTestController {

    @Autowired
    private DroolsTrainingEvaluatorService droolsTrainingEvaluatorService;

    @GetMapping("/test")
    public ResponseEntity<String> runDroolsTest() {
        droolsTrainingEvaluatorService.runEscalationRule(); // now won't be null
        return ResponseEntity.ok("Rules executed successfully");
    }
}