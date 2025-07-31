package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.MailTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailTemplateRepository extends MongoRepository<MailTemplate, String> {
    List<MailTemplate> findByType(String type);
    MailTemplate findByType(String type, String name);


}
