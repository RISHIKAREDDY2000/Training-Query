package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Department;
import com.training.query.Training.Query.collections.Designation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DesignationRepository extends MongoRepository<Designation, Integer> {

    Optional<Designation> findById(String id);


}
