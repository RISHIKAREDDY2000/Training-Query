package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {

    Optional<Department> findById(String id);


}
