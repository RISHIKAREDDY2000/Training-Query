package com.training.query.Training.Query.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Employee")
public class Employee {



    @Id
    private String id;
    private String name;
    //private String deptId;
    private String departmentId;
    private String designationId;




//    public String getDeptId() {
//        return deptId;
//    }


    private String email;

}
