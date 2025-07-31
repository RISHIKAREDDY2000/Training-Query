package com.training.query.Training.Query.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    @Primary
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Training Management API")
//                        .version("1.0")
//                        .description("Spring Boot + MongoDB Training Scheduler System")
//                        .contact(new Contact()
//                                .name("Training System Support")
//                                .email("support@trainingsystem.com"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("http://springdoc.org")));
//    }
//}