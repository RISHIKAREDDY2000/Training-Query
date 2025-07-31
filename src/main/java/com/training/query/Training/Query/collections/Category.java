package com.training.query.Training.Query.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Data
@Document(collection = "Category")
public class Category {

    @Id
    private String id;

    private String department;
    private String designation;
    private String trainingId;
    private List<String> trainingIds;
    private String quarter;


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<String> getTrainingIds() {
        return trainingIds;
    }

    public void setTrainingIds(List<String> trainingIds) {
        this.trainingIds = trainingIds;
    }
}













