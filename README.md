# Training-Query (Corporate Learning Management System)

> Backend-first Corporate Learning Management System that automates training assignment, reminders, escalations, and reporting for 100+ employees.

[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)]()
[![Angular](https://img.shields.io/badge/Angular-17-red)]()
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green)]()
[![Docker](https://img.shields.io/badge/Docker-ready-blue)]()

---

## Overview

**Training-Query (CLMS)** is a full-stack enterprise application designed to streamline corporate learning.  
It automates:
- employee training assignment,
- reminder & escalation workflows,
- and progress tracking through interactive dashboards & PDF reports.

Built with **Java 17**, **Spring Boot 3**, **MongoDB**, **Drools**, and **Angular 17**, it follows a modular microservice-inspired architecture and integrates dynamic schedulers, email templates, and rule-based automation.

---

## Core Features
- Auto-assign trainings by department/designation (Category mapping)  
- CRON schedulers for reminders + **Drools rules** for due-date escalations  
- Database-driven email (MailServer + MailTemplate) with variable placeholders  
- JWT Authentication + Role-Based Access Control (RBAC) with task-aware checks  
- 50+ PDF Reports via **JasperReports** / **DynamicReports**  
- Integrated **Swagger UI / OpenAPI Docs** + tested **Postman Collection**  
- Modular collections: Employee, Department, Designation, Scheduler, Job, MailTemplate, MailServer, Training, Category, TrainingAssociation, TrainingDuration  

---

## Tech Stack

| Layer | Technologies |
|-------|---------------|
| **Frontend** | Angular 17, Angular Material, SCSS, RxJS |
| **Backend** | Java 17, Spring Boot 3, Spring Data MongoDB, Drools, Spring Scheduler |
| **Database** | MongoDB (Compass / Atlas) |
| **Email & Reports** | JavaMail API, JasperReports, DynamicReports |
| **Build & CI/CD** | Gradle Wrapper, Jenkins, Docker |
| **Testing** | JUnit 5, Mockito |
| **Documentation** | Swagger / OpenAPI 3.0 |

---

## Quick Start

### **Prerequisites**
- JDK 17  
- MongoDB running locally or Atlas connection string  

### **Clone & Run**
```bash
git clone https://github.com/RISHIKAREDDY2000/Training-Query.git
cd Training-Query
./gradlew clean build
./gradlew bootRun
