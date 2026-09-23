package com.ems.backend;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ems.backend.entity.Employee;
import com.ems.backend.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Test backend
    @GetMapping("/")
    public ResponseEntity<String> home() {

        return ResponseEntity.ok(
                "Employee Management System Backend is Running!"
        );
    }

    // Get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {

        List<Employee> employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }

    // Add employee
    @PostMapping("/employees")
    public ResponseEntity<Employee> addEmployee(
            @Valid @RequestBody Employee employee) {

        Employee savedEmployee = employeeService.addEmployee(employee);

        return new ResponseEntity<>(
                savedEmployee,
                HttpStatus.CREATED
        );
    }

    // Get employee by ID
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id) {

        Employee employee = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }

    // Update employee
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {

        Employee updatedEmployee =
                employeeService.updateEmployee(id, employee);

        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete employee
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}