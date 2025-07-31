package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.collections.Scheduler;
import com.training.query.Training.Query.service.SchedulerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "Scheduler", description = "Scheduler Management APIs")
@RestController
@RequestMapping("/schedulers")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @Operation(summary = "Get all schedulers")
    @GetMapping("/all")
    public ResponseEntity<List<Scheduler>> getAllSchedulers() {
        return ResponseEntity.ok(schedulerService.getAllSchedulers());
    }

    @Operation(summary = "Create a new scheduler with CRON expression")
    @PostMapping("/create")
    public ResponseEntity<Scheduler> createScheduler(@RequestBody Scheduler scheduler) {
        Scheduler savedScheduler = schedulerService.createScheduler(scheduler);
        return ResponseEntity.ok(savedScheduler);
    }


    @Operation(summary = "Start a scheduler by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scheduler started successfully"),
            @ApiResponse(responseCode = "404", description = "Scheduler not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/start/{id}")
    public ResponseEntity<String> startScheduler(@PathVariable String id) {
        schedulerService.startScheduler(id);
        return ResponseEntity.ok("Scheduler started.");
    }

    @Operation(summary = "Stop a scheduler by ID")
    @PostMapping("/stop/{id}")
    public ResponseEntity<String> stopScheduler(@PathVariable String id) {
        schedulerService.stopScheduler(id);
        return ResponseEntity.ok("Scheduler stopped for ID: " + id);
    }
    @Operation(summary = "Update an existing scheduler")
    @PutMapping("/update")
    public ResponseEntity<Scheduler> updateScheduler(@RequestBody Scheduler scheduler) {
        Scheduler updated = schedulerService.updateScheduler(scheduler);
        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Delete a scheduler by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScheduler(@PathVariable String id) {
        schedulerService.deleteSchedulerById(id);
        return ResponseEntity.ok("Deleted scheduler: " + id);
    }


}