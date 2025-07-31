package com.training.query.Training.Query.collections;

import lombok.Data;

import java.util.Map;

@Data
public class MailRequest {
    private String to;
    private String templateName;
    private Map<String, String> variables;
}
