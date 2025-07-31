package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Training;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends MongoRepository<Training, String> {
    List<Training> findByDepartmentIdAndDesignationId(String departmentId, String designationId);

    @Query("{ 'id': ?0 }")
    Optional<Training> findByCustomId(String id);


}
