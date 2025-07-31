package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ManagerRepository extends MongoRepository<Manager, String> {
    Manager findByEmployeeIdsContaining(String employeeId);
    Manager findByEmployeeIdsContains(String employeeId);
    Manager findByDeptId(int deptId);
    Optional<Manager> findByDeptId(String deptId);

}

