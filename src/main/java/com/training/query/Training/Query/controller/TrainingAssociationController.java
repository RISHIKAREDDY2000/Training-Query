package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import com.training.query.Training.Query.service.EmployeeService;
import com.training.query.Training.Query.service.TrainingAssociationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/training-association")
public class TrainingAssociationController {

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TrainingAssociationService trainingAssociationService;


    @PostMapping
    @Operation(summary = "Assign training to an employee")
    public ResponseEntity<Map<String, String>> assignTraining(@RequestBody TrainingAssociation association) {
        trainingAssociationService.assignTraining(association);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Training assigned successfully");
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update-status/{id}")
    @Operation(summary = "Update training status for a specific training association")
    public ResponseEntity<String> updateTrainingStatus(@PathVariable String id, @RequestParam("status") String status) {
        trainingAssociationService.updateTrainingStatus(id, status);
        return ResponseEntity.ok("Training status updated successfully");
    }

    @GetMapping("/by-employee/{employeeId}/training/{trainingId}")
    @Operation(summary = "Fetch training association by employeeId and trainingId")
    public ResponseEntity<List<TrainingAssociation>> getByEmployeeAndTraining(
            @PathVariable String employeeId,
            @PathVariable String trainingId) {
        List<TrainingAssociation> associations = trainingAssociationService
                .getByEmployeeIdAndTrainingId(employeeId, trainingId);
        return ResponseEntity.ok(associations);
    }

    @GetMapping("/by-employee/{employeeId}")
    @Operation(summary = "Fetch all trainings assigned to an employee")
    public ResponseEntity<List<TrainingAssociation>> getByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(trainingAssociationService.getByEmployeeId(employeeId));
    }
}