# Training-Query (CLMS)

Backend-first Corporate Learning Management System that automates training assignment, reminders/escalations, and reporting for 100+ employees.

## Features
- Auto-assign trainings by department/designation (category mapping)
- CRON schedulers for reminders + Drools rules for due-date escalations
- DB-driven email (MailServer + MailTemplate) with placeholders
- JWT + RBAC with task-aware checks (approve only when status = SUBMITTED)
- 50+ PDF reports (JasperReports/DynamicReports)
- Swagger/OpenAPI docs + Postman collection

## Tech Stack
Java 17, Spring Boot (Web, Data MongoDB, Validation, Scheduler, Security), Drools, MongoDB, JasperReports/DynamicReports, Gradle Wrapper, Jenkins, Docker, JUnit 5/Mockito, Swagger/OpenAPI.

## Quick Start

Prerequisites: JDK 17, MongoDB running locally (or provide a connection string).

```bash
git clone https://github.com/RISHIKAREDDY2000/Training-Query.git
cd Training-Query
./gradlew clean build
./gradlew bootRun
# Swagger UI -> http://localhost:8080/swagger-ui/index.html

