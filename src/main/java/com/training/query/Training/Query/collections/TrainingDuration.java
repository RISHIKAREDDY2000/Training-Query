package com.training.query.Training.Query.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Data
@Document(collection = "TrainingDuration")
public class TrainingDuration {
    @Id
    private String id;
    private String employeeId;
    private String trainingId;
    private String duration;
    private String month;
    private int year;
}
