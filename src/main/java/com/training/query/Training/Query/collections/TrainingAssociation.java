package com.training.query.Training.Query.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "TrainingAssociation")
public class TrainingAssociation {
    @Id
    private String id;
    private String employeeId;
    private String trainingId;
    private String status;
    private Date startDate;
    private Date dueDate;
    private Date assignedDate;
    private boolean dueInNext3Days;




    public void setDueInNext3Days(boolean dueInNext3Days) {
        this.dueInNext3Days = dueInNext3Days;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public TrainingAssociation(String employeeId, String trainingId, String status) {
        this.employeeId = employeeId;
        this.trainingId = trainingId;
        this.status = status;
    }
    public boolean isDueInNext3Days() {
        return dueInNext3Days;
    }




    public Date getDueDate(){
        return dueDate;
    }

}


