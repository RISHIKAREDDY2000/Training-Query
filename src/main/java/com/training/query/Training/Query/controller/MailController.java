package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.collections.MailRequest;
import com.training.query.Training.Query.collections.TrainingAssociation;
import com.training.query.Training.Query.service.EmployeeService;
import com.training.query.Training.Query.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MailRequest request) {
        Employee emp = employeeService.getEmployeeByEmail(request.getTo());
        TrainingAssociation ta = new TrainingAssociation(); // Dummy or from request
        mailService.sendMailToEmployee(emp, ta, "Training Assigned");

//        mailService.sendMailToEmployee(
//                request.getTo(),
//                request.getTemplateName(),
//                request.getVariables()
//        );
        mailService.sendEmail(
                request.getTo(),
                request.getTemplateName(),
                "Dummy email content"
        );

        return ResponseEntity.ok("Email sent successfully");
    }
//    @PostMapping("/mail/test")
//    public ResponseEntity<String> testEmail() {
//        mailService.sendEmail(
//                "your-personal-email@gmail.com",
//                "SMTP Test",
//                "This confirms SMTP is working from Training System."
//        );
//        return ResponseEntity.ok("Test email sent.");
//    }
    @PostMapping("/test")
    public ResponseEntity<String> testMail() {
        mailService.sendEmail(
                "mayalasya30@gmail.com",
                "SMTP Test",
                "This is a test email from Training Management System."
        );
        return ResponseEntity.ok("Test email sent.");
    }

}

