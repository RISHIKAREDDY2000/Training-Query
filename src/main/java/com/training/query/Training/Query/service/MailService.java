package com.training.query.Training.Query.service;

import com.training.query.Training.Query.collections.*;
import com.training.query.Training.Query.repository.MailServerRepository;
import com.training.query.Training.Query.repository.MailTemplateRepository;
import com.training.query.Training.Query.repository.TrainingRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
//import org.kie.api.runtime.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import com.training.query.Training.Query.util.EmailUtil;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailServerRepository mailServerRepository;
    @Autowired
    private MailTemplateRepository mailTemplateRepository;
    @Autowired
    private Environment env;
    @Autowired
    private TrainingRepository trainingRepository;


        public void sendReminderToEmployee(Manager manager, Employee employee, TrainingAssociation ta) {
            MailTemplate template = mailTemplateRepository.findByType("reminder")
                    .stream()
                    .filter(t -> t.getName().equalsIgnoreCase("Training Reminder"))
                    .findFirst()
                    .orElse(null);

            MailServer config = mailServerRepository.findActive();

            if (template == null || config == null) {
                System.err.println("Missing reminder template or mail config");
                return;
            }

            String body = template.getBody()
                    .replace("{{employeeName}}", employee.getName())
                    .replace("{{trainingId}}", ta.getTrainingId())
                    .replace("{{dueDate}}", ta.getDueDate().toString());

            EmailUtil.sendEmail(config, employee.getEmail(), template.getSubject(), body);
        }

    public void sendMailToEmployee(Employee emp, TrainingAssociation ta, String Type) {
        MailTemplate template = mailTemplateRepository.findByType(Type)
                .stream()
                .filter(t -> t.getName().equalsIgnoreCase("Training Assignment"))
                .findFirst()
                .orElse(null);

        MailServer config = mailServerRepository.findActive();
        if (template == null || config == null) {
            System.err.println("Missing template or mail config");
            return;
        }

        Training training = trainingRepository.findByCustomId(ta.getTrainingId()).orElse(null);
        if (training == null) {
            System.err.println("Training not found for ID: " + ta.getTrainingId());
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ta.getStartDate());
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        Map<String, String> variables = new HashMap<>();
        variables.put("employeeName", emp.getName());
        variables.put("trainingId", ta.getTrainingId());
        variables.put("trainingTitle", training.getTitle());
        variables.put("assignedDate", ta.getAssignedDate().toString());
        variables.put("startDate", ta.getStartDate().toString());
        variables.put("dueDate", ta.getDueDate().toString());
        variables.put("month", month);
        variables.put("year", year);

        String body = EmailUtil.replaceTemplateVariables(template.getBody(), variables);
        String subject = EmailUtil.replaceTemplateVariables(template.getSubject(), variables);

        EmailUtil.sendEmail(config, emp.getEmail(), subject, body);
    }


    public void sendEscalationToManager(Manager manager, Employee emp, TrainingAssociation ta) {
        Training training = trainingRepository.findByCustomId(ta.getTrainingId()).orElse(null);
        if (training != null) {
            notifyManager(manager, emp, training, ta);
        } else {
            System.err.println("Training not found for escalation email.");
        }
    }


    public void sendEscalationToManager(
            Manager manager,
            List<TrainingAssociation> pendingTrainings,
            Map<String, Employee> employeeMap,
            Map<String, Training> trainingMap
    ) {
        MailTemplate template = mailTemplateRepository.findByType("escalation")
                .stream()
                .filter(t -> t.getName().equalsIgnoreCase("Manager Escalation"))
                .findFirst()
                .orElse(null);

        MailServer config = mailServerRepository.findActive();

        if (template == null || config == null) {
            System.err.println("Missing escalation template or mail config");
            return;
        }

        StringBuilder pendingList = new StringBuilder();
        int count = 1;
        for (TrainingAssociation ta : pendingTrainings) {
            Employee emp = employeeMap.get(ta.getEmployeeId());
            Training training = trainingMap.get(ta.getTrainingId());
            if (emp != null && training != null) {
                pendingList.append(count++).append(". Name: ").append(emp.getName()).append("\n")
                        .append("   Email: ").append(emp.getEmail()).append("\n")
                        .append("   Training: ").append(training.getTitle()).append("\n")
                        .append("   Due Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(ta.getDueDate())).append("\n\n");
            }
        }

        String body = template.getBody()
                .replace("{{managerName}}", manager.getName())
                .replace("{{pendingList}}", pendingList.toString());

        EmailUtil.sendEmail(config, manager.getEmail(), template.getSubject(), body);
    }

    public void notifyManager(Manager manager, Employee employee, Training training, TrainingAssociation ta) {
        String to = env.getProperty("app.manager.email", manager.getEmail());
        String from = env.getProperty("spring.mail.username");
        String subject = "Training Reminder: " + employee.getName();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom(from);
        msg.setSubject(subject);
        msg.setText(
                String.format(
                        "Hi %s,%n%nEmployee %s has a training '%s' (ID: %s) due on %s.%nPlease take action.",
                        manager.getName(),
                        employee.getName(),
                        training.getTitle(),
                        ta.getTrainingId(),
                        ta.getDueDate()
                )
        );
        javaMailSender.send(msg);
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("rishikareddy6188@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCompletionMail(Employee emp, TrainingAssociation ta) {
        System.out.println("Sending COMPLETION mail to: " + emp.getEmail());


        List<MailTemplate> templates = mailTemplateRepository.findByType("completion");

        MailTemplate template = templates.stream()
                .filter(t -> t.getName().equalsIgnoreCase("Training Completed"))
                .findFirst()
                .orElse(null);

        if (template == null) {
            System.err.println("No completion template found!");
            return;
        }

        Training training = trainingRepository.findByCustomId(ta.getTrainingId()).orElse(null);
        if (training == null) {
            System.err.println("Training not found for ID: " + ta.getTrainingId());
            return;
        }


        Map<String, String> variables = new HashMap<>();
        variables.put("employeeName", emp.getName());
        variables.put("trainingName", training.getTitle());
        variables.put("status", ta.getStatus());
        variables.put("completionDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));


        String subject = EmailUtil.replaceTemplateVariables(template.getSubject(), variables);
        String body = EmailUtil.replaceTemplateVariables(template.getBody(), variables);


        sendEmail(emp.getEmail(), subject, body);
    }


    public void sendTrainingAssignedMail(Employee emp, TrainingAssociation ta) {
        sendMailToEmployee(emp, ta, "assignment");
    }
}