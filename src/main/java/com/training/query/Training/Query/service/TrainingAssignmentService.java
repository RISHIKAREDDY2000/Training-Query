package com.training.query.Training.Query.service;

import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingAssignmentService {

    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;

    public ResponseEntity<?> getTrainingsForEmployee(String employeeId) {
        List<TrainingAssociation> list = trainingAssociationRepository.findByEmployeeId(employeeId);
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().body("No trainings found: Check if Category/Department mappings exist.");
        }
        return ResponseEntity.ok(list);
    }
}