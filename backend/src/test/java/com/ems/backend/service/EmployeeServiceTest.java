package com.ems.backend.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ems.backend.dto.EmployeeDTO;
import com.ems.backend.entity.Employee;
import com.ems.backend.exception.EmployeeNotFoundException;
import com.ems.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {

        employee = new Employee();

        employee.setId(1L);
        employee.setName("Santosh");
        employee.setAge(23);
        employee.setSalary(58000);
        employee.setEmail("santosh13@gmail.com");

        employeeDTO = new EmployeeDTO(
                1L,
                "Santosh",
                23,
                58000,
                "santosh13@gmail.com"
        );
    }

    @Test
    void testGetAllEmployees() {

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));

        List<EmployeeDTO> employees =
                employeeService.getAllEmployees();

        assertNotNull(employees);

        assertEquals(1, employees.size());

        assertEquals(
                "Santosh",
                employees.get(0).getName()
        );

        assertEquals(
                23,
                employees.get(0).getAge()
        );

        verify(employeeRepository).findAll();
    }

    @Test
    void testAddEmployee() {

        Employee savedEmployee = new Employee();

        savedEmployee.setId(2L);
        savedEmployee.setName("Rahul");
        savedEmployee.setAge(28);
        savedEmployee.setSalary(75000);
        savedEmployee.setEmail("rahul@gmail.com");

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeDTO result =
                employeeService.addEmployee(
                        new EmployeeDTO(
                                null,
                                "Rahul",
                                28,
                                75000,
                                "rahul@gmail.com"
                        )
                );

        assertNotNull(result);

        assertEquals(
                2L,
                result.getId()
        );

        assertEquals(
                "Rahul",
                result.getName()
        );

        assertEquals(
                28,
                result.getAge()
        );

        assertEquals(
                75000,
                result.getSalary()
        );

        assertEquals(
                "rahul@gmail.com",
                result.getEmail()
        );

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeDTO result =
                employeeService.getEmployeeById(1L);

        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Santosh",
                result.getName()
        );

        assertEquals(
                "santosh13@gmail.com",
                result.getEmail()
        );

        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetEmployeeByIdWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.getEmployeeById(999L)
                );

        assertEquals(
                "Employee with ID 999 not found",
                exception.getMessage()
        );

        verify(employeeRepository).findById(999L);
    }

    @Test
    void testUpdateEmployee() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        EmployeeDTO updatedDTO =
                new EmployeeDTO(
                        1L,
                        "Santosh Updated",
                        24,
                        65000,
                        "santosh.updated@gmail.com"
                );

        EmployeeDTO result =
                employeeService.updateEmployee(
                        1L,
                        updatedDTO
                );

        assertNotNull(result);

        assertEquals(
                "Santosh Updated",
                result.getName()
        );

        assertEquals(
                24,
                result.getAge()
        );

        assertEquals(
                65000,
                result.getSalary()
        );

        assertEquals(
                "santosh.updated@gmail.com",
                result.getEmail()
        );

        verify(employeeRepository).findById(1L);

        verify(employeeRepository).save(employee);
    }

    @Test
    void testUpdateEmployeeWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.updateEmployee(
                                999L,
                                employeeDTO
                        )
                );

        assertEquals(
                "Employee with ID 999 not found",
                exception.getMessage()
        );

        verify(employeeRepository).findById(999L);
    }

    @Test
    void testDeleteEmployee() {

        when(employeeRepository.existsById(1L))
                .thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository)
                .existsById(1L);

        verify(employeeRepository)
                .deleteById(1L);
    }

    @Test
    void testDeleteEmployeeWhenEmployeeDoesNotExist() {

        when(employeeRepository.existsById(999L))
                .thenReturn(false);

        EmployeeNotFoundException exception =
                assertThrows(
                        EmployeeNotFoundException.class,
                        () -> employeeService.deleteEmployee(999L)
                );

        assertEquals(
                "Employee with ID 999 not found",
                exception.getMessage()
        );

        verify(employeeRepository)
                .existsById(999L);
    }
}