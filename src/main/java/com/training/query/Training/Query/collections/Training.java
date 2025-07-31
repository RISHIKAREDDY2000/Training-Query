package com.training.query.Training.Query.collections;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Training")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    private String departmentId;
    private String designationId;
    private String id;
    private String title;
    private Date createdOn;

}

