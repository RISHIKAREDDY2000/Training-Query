package com.training.query.Training.Query.collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Document(collection = "Scheduler")
@Data
public class Scheduler {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private String cronExpression;
    private String jobname;
    private boolean enabled;
    private String time;
    private String scheduleType;
    private Date lastRunTime;
    private Date nextRunTime;
    private int dueDays;

//    @JsonProperty("id")
//    public String getIdAsString() {
//        return id != null ? id.toHexString() : null;
//    }

//    public void setIdFromString(String idStr) {
//        if (idStr != null && !idStr.isEmpty()) {
//            this.id = new ObjectId(idStr);
//        }
//    }








}
