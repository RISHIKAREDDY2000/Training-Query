package com.training.query.Training.Query.collections;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TrainingReportRow {
    private String employeeId;
    private String employeeName;
    private String trainingId;
    private String status;
    private Date assignedDate;
    private Date dueDate;
}
