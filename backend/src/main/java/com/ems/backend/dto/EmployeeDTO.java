package com.ems.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(
        name = "Employee",
        description = "Employee information used for creating, updating, and retrieving employees"
)
public class EmployeeDTO {

    @Schema(
            description = "Unique ID of the employee",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Full name of the employee",
            example = "Rahul Sharma",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(
            description = "Age of the employee",
            example = "28",
            minimum = "18",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Min(
            value = 18,
            message = "Age must be at least 18"
    )
    private int age;

    @Schema(
            description = "Annual salary of the employee",
            example = "750000",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Positive(
            message = "Salary must be greater than 0"
    )
    private double salary;

    @Schema(
            description = "Email address of the employee",
            example = "rahul.sharma@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Email is required")
    @Email(
            message = "Please enter a valid email"
    )
    private String email;

    public EmployeeDTO() {
    }

    public EmployeeDTO(
            Long id,
            String name,
            int age,
            double salary,
            String email) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}