package com.ems.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ems.backend.entity.Employee;
import com.ems.backend.exception.EmployeeNotFoundException;
import com.ems.backend.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Add employee
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Get employee by ID
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with ID " + id + " not found"));
    }

    // Update employee
    public Employee updateEmployee(Long id, Employee employeeDetails) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with ID " + id + " not found"));

        employee.setName(employeeDetails.getName());
        employee.setAge(employeeDetails.getAge());
        employee.setSalary(employeeDetails.getSalary());
        employee.setEmail(employeeDetails.getEmail());

        return employeeRepository.save(employee);
    }

    // Delete employee
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee with ID " + id + " not found");
        }

        employeeRepository.deleteById(id);
    }
}