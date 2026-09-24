package com.ems.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ems.backend.dto.EmployeeDTO;
import com.ems.backend.exception.EmployeeNotFoundException;
import com.ems.backend.exception.GlobalExceptionHandler;
import com.ems.backend.service.EmployeeService;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void testGetAllEmployees() throws Exception {

        EmployeeDTO employee =
                new EmployeeDTO(
                        1L,
                        "Santosh",
                        23,
                        58000,
                        "santosh13@gmail.com"
                );

        when(employeeService.getAllEmployees())
                .thenReturn(List.of(employee));

        mockMvc.perform(
                get("/employees")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Santosh"))
        .andExpect(jsonPath("$[0].age").value(23))
        .andExpect(jsonPath("$[0].salary").value(58000))
        .andExpect(
                jsonPath("$[0].email")
                        .value("santosh13@gmail.com")
        );
    }

    @Test
    void testGetEmployeeById() throws Exception {

        EmployeeDTO employee =
                new EmployeeDTO(
                        1L,
                        "Santosh",
                        23,
                        58000,
                        "santosh13@gmail.com"
                );

        when(employeeService.getEmployeeById(1L))
                .thenReturn(employee);

        mockMvc.perform(
                get("/employees/1")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Santosh"))
        .andExpect(jsonPath("$.age").value(23))
        .andExpect(jsonPath("$.salary").value(58000))
        .andExpect(
                jsonPath("$.email")
                        .value("santosh13@gmail.com")
        );
    }

    @Test
    void testGetEmployeeByIdWhenEmployeeDoesNotExist()
            throws Exception {

        when(employeeService.getEmployeeById(999L))
                .thenThrow(
                        new EmployeeNotFoundException(
                                "Employee with ID 999 not found"
                        )
                );

        mockMvc.perform(
                get("/employees/999")
        )
        .andExpect(status().isNotFound())
        .andExpect(
                jsonPath("$.status")
                        .value(404)
        )
        .andExpect(
                jsonPath("$.message")
                        .value(
                                "Employee with ID 999 not found"
                        )
        );
    }

    @Test
    void testAddEmployee() throws Exception {

        EmployeeDTO request =
                new EmployeeDTO(
                        null,
                        "Rahul",
                        28,
                        75000,
                        "rahul@gmail.com"
                );

        EmployeeDTO response =
                new EmployeeDTO(
                        2L,
                        "Rahul",
                        28,
                        75000,
                        "rahul@gmail.com"
                );

        when(employeeService.addEmployee(any(EmployeeDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        )
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("Rahul"))
        .andExpect(jsonPath("$.age").value(28))
        .andExpect(jsonPath("$.salary").value(75000))
        .andExpect(
                jsonPath("$.email")
                        .value("rahul@gmail.com")
        );
    }

    @Test
    void testAddEmployeeWithInvalidData()
            throws Exception {

        EmployeeDTO invalidEmployee =
                new EmployeeDTO(
                        null,
                        "",
                        15,
                        -5000,
                        "invalid-email"
                );

        mockMvc.perform(
                post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        invalidEmployee
                                )
                        )
        )
        .andExpect(status().isBadRequest())
        .andExpect(
                jsonPath("$.status")
                        .value(400)
        )
        .andExpect(
                jsonPath("$.message")
                        .value("Validation failed")
        );
    }

    @Test
    void testUpdateEmployee() throws Exception {

        EmployeeDTO request =
                new EmployeeDTO(
                        1L,
                        "Santosh Updated",
                        24,
                        65000,
                        "santosh.updated@gmail.com"
                );

        EmployeeDTO response =
                new EmployeeDTO(
                        1L,
                        "Santosh Updated",
                        24,
                        65000,
                        "santosh.updated@gmail.com"
                );

        when(
                employeeService.updateEmployee(
                        eq(1L),
                        any(EmployeeDTO.class)
                )
        )
        .thenReturn(response);

        mockMvc.perform(
                put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(
                jsonPath("$.name")
                        .value("Santosh Updated")
        )
        .andExpect(jsonPath("$.age").value(24))
        .andExpect(jsonPath("$.salary").value(65000))
        .andExpect(
                jsonPath("$.email")
                        .value(
                                "santosh.updated@gmail.com"
                        )
        );
    }

    @Test
    void testUpdateEmployeeWhenEmployeeDoesNotExist()
            throws Exception {

        EmployeeDTO request =
                new EmployeeDTO(
                        999L,
                        "Test Employee",
                        25,
                        50000,
                        "test@gmail.com"
                );

        when(
                employeeService.updateEmployee(
                        eq(999L),
                        any(EmployeeDTO.class)
                )
        )
        .thenThrow(
                new EmployeeNotFoundException(
                        "Employee with ID 999 not found"
                )
        );

        mockMvc.perform(
                put("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        request
                                )
                        )
        )
        .andExpect(status().isNotFound())
        .andExpect(
                jsonPath("$.status")
                        .value(404)
        )
        .andExpect(
                jsonPath("$.message")
                        .value(
                                "Employee with ID 999 not found"
                        )
        );
    }

    @Test
    void testDeleteEmployee() throws Exception {

        doNothing()
                .when(employeeService)
                .deleteEmployee(1L);

        mockMvc.perform(
                delete("/employees/1")
        )
        .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteEmployeeWhenEmployeeDoesNotExist()
            throws Exception {

        doThrow(
                new EmployeeNotFoundException(
                        "Employee with ID 999 not found"
                )
        )
        .when(employeeService)
        .deleteEmployee(999L);

        mockMvc.perform(
                delete("/employees/999")
        )
        .andExpect(status().isNotFound())
        .andExpect(
                jsonPath("$.status")
                        .value(404)
        )
        .andExpect(
                jsonPath("$.message")
                        .value(
                                "Employee with ID 999 not found"
                        )
        );
    }
}