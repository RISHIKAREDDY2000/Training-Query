package com.training.query.Training.Query.service;


import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EscalationService {



    @Autowired
    private TrainingAssociationRepository trainingRepo;

    public List<String> evaluateEscalations() {
        List<TrainingAssociation> all = trainingRepo.findAll();
        List<String> escalated = new ArrayList<>();

        return escalated;
    }
}
