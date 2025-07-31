package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.Training;
import com.training.query.Training.Query.collections.TrainingAssignmentTask;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.repository.TrainingRepository;
import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;
import com.training.query.Training.Query.service.TrainingAssociationService;
import com.training.query.Training.Query.service.TrainingService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/trainings")
@Tag(name = "Trainings", description = "Operations related to trainings")
public class TrainingController {
    @Autowired
    private DroolsTrainingEvaluatorService evaluator;


    @Autowired
    private TrainingService trainingService;


    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingAssignmentTask trainingAssignmentTask;

    @GetMapping("/escalate")
    public List<TrainingAssociation> escalatePending() {
        List<TrainingAssociation> all = trainingService.getAll();
        return evaluator.evaluatePendingTrainings(all);
    }
    @GetMapping("/all")
    public List<TrainingAssociation> getAll() {
        return trainingService.getAll();
    }

    @PostMapping
    public ResponseEntity<Training> addTraining(@RequestBody Training training) {
        Training saved = trainingRepository.save(training);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/assign-training")
    public ResponseEntity<String> assignTraining() {
        trainingAssignmentTask.run(); // Or call via SchedulerService if managed
        return ResponseEntity.ok("Training assignment executed.");
    }
}

