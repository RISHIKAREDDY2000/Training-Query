package com.training.query.Training.Query;

//package com.training.query.Training.Query.dto;

public class EmployeeTrainingCount {
    private String id; // corresponds to _id from aggregation
    private int total;

    // Getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}

