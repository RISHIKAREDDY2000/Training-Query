package com.training.query.Training.Query.repository;

import com.training.query.Training.Query.collections.MailServer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailServerRepository extends MongoRepository<MailServer, String> {
    @Query("{ enabled: true }")
    MailServer findActive();
    List<MailServer> findAllByEnabledTrue();


}

