package com.ems.backend;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.ems.backend.dto.EmployeeDTO;
import com.ems.backend.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@Tag(
        name = "Employee Management",
        description = "APIs for managing employees"
)
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Test backend
    @Operation(
            summary = "Check backend status",
            description = "Checks whether the Employee Management System backend is running."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Backend is running successfully"
    )
    @GetMapping("/")
    public ResponseEntity<String> home() {

        return ResponseEntity.ok(
                "Employee Management System Backend is Running!"
        );
    }

    // Get all employees
    @Operation(
            summary = "Get all employees",
            description = "Retrieves a list containing all employees stored in the database."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully"
            )
    })
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

        List<EmployeeDTO> employees =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }

    // Add employee
    @Operation(
            summary = "Add a new employee",
            description = "Creates a new employee and stores the employee information in the database."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid employee data"
            )
    })
    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> addEmployee(
            @Valid
            @RequestBody
            EmployeeDTO employeeDTO) {

        EmployeeDTO savedEmployee =
                employeeService.addEmployee(employeeDTO);

        return new ResponseEntity<>(
                savedEmployee,
                HttpStatus.CREATED
        );
    }

    // Get employee by ID
    @Operation(
            summary = "Get employee by ID",
            description = "Retrieves a specific employee using the employee ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee found successfully",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(

            @Parameter(
                    description = "Unique ID of the employee",
                    example = "1"
            )
            @PathVariable Long id) {

        EmployeeDTO employee =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }

    // Update employee
    @Operation(
            summary = "Update an employee",
            description = "Updates the details of an existing employee using the employee ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid employee data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(

            @Parameter(
                    description = "Unique ID of the employee to update",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid
            @RequestBody
            EmployeeDTO employeeDTO) {

        EmployeeDTO updatedEmployee =
                employeeService.updateEmployee(
                        id,
                        employeeDTO
                );

        return ResponseEntity.ok(updatedEmployee);
    }

    // Delete employee
    @Operation(
            summary = "Delete an employee",
            description = "Deletes an existing employee from the database using the employee ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(

            @Parameter(
                    description = "Unique ID of the employee to delete",
                    example = "1"
            )
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}