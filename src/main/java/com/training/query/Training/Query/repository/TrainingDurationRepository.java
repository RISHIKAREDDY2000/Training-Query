package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.TrainingDuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingDurationRepository extends MongoRepository<TrainingDuration, String> {
}

