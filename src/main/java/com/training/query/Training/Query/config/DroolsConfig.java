package com.training.query.Training.Query.config;

import com.training.query.Training.Query.service.DroolsTrainingEvaluatorService;

import org.kie.api.builder.*;


import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String RULES_PATH = "rules/math-rules.drl";
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();


        kieFileSystem.write(ResourceFactory.newClassPathResource("rules/training-escalation.drl"));


        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("Drools errors:\n" + results.getMessages());
        }

        KieRepository kieRepository = kieServices.getRepository();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }


}

