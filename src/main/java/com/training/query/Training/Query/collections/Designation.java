package com.training.query.Training.Query.collections;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Designation")
@Data
public class Designation {
    @Id
    private Integer id;

    private String title;
    private String designationId;
}

