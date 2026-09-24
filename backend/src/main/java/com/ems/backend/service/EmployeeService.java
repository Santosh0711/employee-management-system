package com.ems.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ems.backend.dto.EmployeeDTO;
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
    public List<EmployeeDTO> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Add employee
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {

        Employee employee = convertToEntity(employeeDTO);

        Employee savedEmployee =
                employeeRepository.save(employee);

        return convertToDTO(savedEmployee);
    }

    // Get employee by ID
    public EmployeeDTO getEmployeeById(Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee with ID "
                                                + id
                                                + " not found"
                                )
                        );

        return convertToDTO(employee);
    }

    // Update employee
    public EmployeeDTO updateEmployee(
            Long id,
            EmployeeDTO employeeDTO) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Employee with ID "
                                                + id
                                                + " not found"
                                )
                        );

        employee.setName(employeeDTO.getName());
        employee.setAge(employeeDTO.getAge());
        employee.setSalary(employeeDTO.getSalary());
        employee.setEmail(employeeDTO.getEmail());

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return convertToDTO(updatedEmployee);
    }

    // Delete employee
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {

            throw new EmployeeNotFoundException(
                    "Employee with ID "
                            + id
                            + " not found"
            );
        }

        employeeRepository.deleteById(id);
    }

    // Convert Entity to DTO
    private EmployeeDTO convertToDTO(Employee employee) {

        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getAge(),
                employee.getSalary(),
                employee.getEmail()
        );
    }

    // Convert DTO to Entity
    private Employee convertToEntity(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();

        employee.setName(employeeDTO.getName());
        employee.setAge(employeeDTO.getAge());
        employee.setSalary(employeeDTO.getSalary());
        employee.setEmail(employeeDTO.getEmail());

        return employee;
    }
}