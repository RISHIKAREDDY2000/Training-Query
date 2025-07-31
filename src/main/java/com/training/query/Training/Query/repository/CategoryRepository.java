package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByDepartmentAndDesignation(String department, String designation);
    Optional<Category> findByDesignationAndDepartment(String designation, String department);

}

