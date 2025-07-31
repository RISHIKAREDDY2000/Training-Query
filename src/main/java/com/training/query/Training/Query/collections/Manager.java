package com.training.query.Training.Query.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Manager")
public class Manager {
    @Id
    private String id;
    private String name;
    private String email;
    private String deptId;
    private List<String> employeeIds;
}