package com.training.query.Training.Query.service;

import com.training.query.Training.Query.collections.Training;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.repository.TrainingAssociationRepository;
import com.training.query.Training.Query.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingAssociationRepository trainingAssociationRepository;


    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }
    public List<TrainingAssociation> getAll() {
        return trainingAssociationRepository.findAll();
    }
    public Training getTrainingById(String trainingId) {
        return trainingRepository.findById(trainingId).orElse(null);
    }

}
