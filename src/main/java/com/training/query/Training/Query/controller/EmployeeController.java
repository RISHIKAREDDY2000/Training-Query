package com.training.query.Training.Query.controller;

import com.training.query.Training.Query.EmployeeDTO;
import com.training.query.Training.Query.collections.Employee;
import com.training.query.Training.Query.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employees", description = "Manage employee and training assignment")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;



    @PostMapping("/employee/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeDTO dto) {
        Employee saved = employeeService.addEmployee(dto);
        return ResponseEntity.ok(saved);
    }




    @GetMapping("/all")
    @Operation(
            summary = "Get all employees",
            description = "Returns all employee records from the database."
    )
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee by ID",
            description = "Fetch a single employee by MongoDB ObjectId."
    )
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }
}