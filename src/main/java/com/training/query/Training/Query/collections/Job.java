package com.training.query.Training.Query.collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Job")
public class Job {
    @Id
    private String id;
    private String name;
    private String cronExpression;
    private Date createdAt;
    private Date endTime;
    private String schedulerId;
    private String status;
    private String errorMessage;
    private LocalDateTime startTime;
    private Date lastRunTime;
    private LocalDateTime nextRunTime;
    private String message;
    private String jobName;
    private Date runTime;


}
