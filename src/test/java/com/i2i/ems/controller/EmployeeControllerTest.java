package com.i2i.ems.controller;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

  @Mock
  private EmployeeService employeeService;

  private EmployeeDto employeeDto;

  @InjectMocks
  private EmployeeController employeeController;

  @BeforeEach
  void setUp() {
    employeeDto = EmployeeDto.builder()
        .id(1)
        .name("Gobi")
        .build();
  }

  @Test
  void testAddEmployee() {
    when(employeeService.addEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);
    ResponseEntity<EmployeeDto> response = employeeController.addEmployee(employeeDto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(employeeDto, response.getBody());
    verify(employeeService, times(1)).addEmployee(any(EmployeeDto.class));
  }


  @Test
  void testGetEmployee() {
    when(employeeService.getEmployee(anyInt())).thenReturn(employeeDto);
    ResponseEntity<EmployeeDto> response = employeeController.getEmployee(1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(employeeDto, response.getBody());
    verify(employeeService, times(1)).getEmployee(1);
  }

  @Test
  void testGetAllEmployees() {
    List<EmployeeDto> employees = Arrays.asList(employeeDto);
    when(employeeService.getAllEmployees(0, 1)).thenReturn(employees);
    ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees(0, 1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(employees, response.getBody());
    verify(employeeService, times(1)).getAllEmployees(0, 1);
  }

  @Test
  void testUpdateEmployee() {
    when(employeeService.updateEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);
    ResponseEntity<EmployeeDto> response = employeeController.updateEmployee(employeeDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(employeeDto, response.getBody());
    verify(employeeService, times(1)).updateEmployee(employeeDto);
  }

  @Test
  void testDeleteEmployee() {
    ResponseEntity<HttpStatus> response = employeeController.deleteEmployee(1);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(employeeService, times(1)).deleteEmployee(1);
  }

  @Test
  void testRegisterUser() {
    when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);
    ResponseEntity<EmployeeDto> response = employeeController.registerUser(employeeDto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(employeeDto, response.getBody());
    verify(employeeService, times(1)).createEmployee(any(EmployeeDto.class));
  }

  @Test
  void testLoginUser() {
    when(employeeService.createSession(any(EmployeeDto.class))).thenReturn("String.valueOf(String.class)");
    ResponseEntity<HttpStatus> response = employeeController.loginUser(employeeDto);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(employeeService, times(1)).createSession(any(EmployeeDto.class));
  }
}