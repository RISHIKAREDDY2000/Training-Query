package com.training.query.Training.Query.collections;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "MailTemplate")
@Data
public class MailTemplate {
    @Id
    private String id;
    private String name;
    private String subject;
    private String body;
    private String type;
}

