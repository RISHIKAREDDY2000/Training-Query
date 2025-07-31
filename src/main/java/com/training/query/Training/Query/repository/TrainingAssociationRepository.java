package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.EmployeeTrainingCount;
import com.training.query.Training.Query.collections.TrainingAssociation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingAssociationRepository extends MongoRepository<TrainingAssociation, String> {


    List<TrainingAssociation> findByStatusNot(String status);


    List<TrainingAssociation> findByStatusIn(List<String> statuses);
    //List<TrainingAssociation> findByEmployeeIdAndAssignedDateBetween(String employeeId, Date from, Date to);

    //List<TrainingAssociation> findByEmployeeIdAndAssignedDateBetween(String employeeId, Date from, Date to);

    List<TrainingAssociation> findByEmployeeIdAndAssignedDateBetween(String employeeId, Date from, Date to);

    List<TrainingAssociation> findByEmployeeId(String employeeId);
    List<TrainingAssociation> findByEmployeeIdAndTrainingId(String employeeId, String trainingId);
    boolean existsByEmployeeIdAndTrainingId(String employeeId, String trainingId);
}
