package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.TrainingTrackingTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-tracking")
@Tag(name = "Training Tracking", description = "Trigger training reminders and escalations")
public class TrainingTrackingController {

    @Autowired
    private TrainingTrackingTask trainingTrackingTask;

    @Operation(summary = "Trigger training escalation check")
    @PostMapping("/start")
    public ResponseEntity<String> triggerTracking() {
        trainingTrackingTask.run();
        return ResponseEntity.ok("Training tracking job executed successfully.");
    }

}
