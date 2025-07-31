package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.Manager;
import com.training.query.Training.Query.service.MailService;
import com.training.query.Training.Query.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/managers")
@Tag(name = "Manager", description = "Manage escalation mappings")
public class ManagerController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private MailService mailService;


    @PostMapping
    @Operation(summary = "Create a new manager and map employees")
    public ResponseEntity<Manager> addManager(@RequestBody Manager manager) {
        Manager saved = managerService.saveManager(manager);
        return ResponseEntity.ok(saved);
    }
    @PostMapping("/mail/test-escalation")
    public ResponseEntity<String> testEscalation() {

        return ResponseEntity.ok("Escalation email sent.");
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a manager by ID")
    public ResponseEntity<Manager> getManagerById(@PathVariable String id) {
        Manager manager = managerService.getManagerById(id);
        return manager != null ? ResponseEntity.ok(manager) : ResponseEntity.notFound().build();
    }
}
