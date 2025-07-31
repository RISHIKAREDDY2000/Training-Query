package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
}

