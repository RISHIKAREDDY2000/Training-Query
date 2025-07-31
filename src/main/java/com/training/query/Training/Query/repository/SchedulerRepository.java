package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Scheduler;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerRepository extends MongoRepository<Scheduler, ObjectId> {
    Optional<Scheduler> findByJobnameIgnoreCase(String jobName);

}

