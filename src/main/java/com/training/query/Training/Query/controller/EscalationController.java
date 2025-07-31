package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;
import com.training.query.Training.Query.service.EscalationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/escalation")
public class EscalationController {

    private final DroolsTrainingEvaluatorService droolsTrainingEvaluatorService;

    public EscalationController(DroolsTrainingEvaluatorService droolsTrainingEvaluatorService) {
        this.droolsTrainingEvaluatorService = droolsTrainingEvaluatorService;
    }

    @GetMapping("/run/escalation")
    public ResponseEntity<String> runEscalation() {
        droolsTrainingEvaluatorService.runEscalationRule();
        return ResponseEntity.ok("Escalation triggered successfully\"");
    }

}

