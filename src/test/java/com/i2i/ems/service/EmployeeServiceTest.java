package com.i2i.ems.service;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.helper.UnAuthorizedException;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Type;
import com.i2i.ems.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  private EmployeeDto employeeDto;

  private Employee employee;

  @InjectMocks
  private EmployeeService employeeService;

  @BeforeEach
  void setUp() {

    employeeDto = EmployeeDto.builder()
        .name("Gobi")
        .email("gobi@i2i.com")
        .mobileNo(1234567890)
        .UUID("1234")
        .age(20)
        .build();

    employee = Employee.builder()
        .id(10)
        .name("Gobi")
        .dob(new Date(2003, 05, 23))
        .mobileNo(1234567890)
        .email("gobi@i2i.com")
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .build();
  }

//  @AfterEach
//  void tearDown() {
//    employeeDto = null;
//    employee = null;
//  }

  @Test
  void testSaveEmployeeSuccess() {
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
    Employee result = employeeService.saveEmployee(employee);
    assertNotNull(result);
    assertEquals(employee.getEmail(), result.getEmail());
    assertEquals(employee.getId(), result.getId());
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void testSaveEmployeeFailure() {
    when(employeeRepository.save(employee)).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.saveEmployee(employee));
    verify(employeeRepository, times(1)).save(employee);
  }

  @Test
  void testAddEmployeeSuccess() {
    when(employeeRepository.existsByEmail(anyString())).thenReturn(true);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
    EmployeeDto result = employeeService.addEmployee(employeeDto);
    assertNotNull(result);
    assertEquals(employee.getEmail(), result.getEmail());
    verify(employeeRepository, times(1)).existsByEmail(anyString());
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void testAddEmployeeNotExists() {
    when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
    assertThrows(NoSuchElementException.class, () -> employeeService.addEmployee(employeeDto));
    verify(employeeRepository, times(1)).existsByEmail(anyString());
  }

  @Test
  void testAddEmployeeFailure() {
    when(employeeRepository.existsByEmail(anyString())).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.addEmployee(employeeDto));
    verify(employeeRepository, times(1)).existsByEmail(anyString());
  }

  @Test
  void testGetEmployeeSuccess() {
    when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(employee);
    EmployeeDto result = employeeService.getEmployee(10);
    assertNotNull(result);
    assertEquals(employee.getEmail(), result.getEmail());
    verify(employeeRepository, times(1)).findByIdAndIsDeletedFalse(anyInt());
  }

  @Test
  void testGetEmployeeNotExists() {
    when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(11));
    verify(employeeRepository, times(1)).findByIdAndIsDeletedFalse(anyInt());
  }

  @Test
  void testGetEmployeeFailure() {
    when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.getEmployee(12));
    verify(employeeRepository, times(1)).findByIdAndIsDeletedFalse(anyInt());
  }

  @Test
  void testGetAllEmployeesSuccess() {
    when(employeeRepository.findAllByIsDeletedFalse(any(Pageable.class))).thenReturn(List.of(employee));
    assertEquals(1, employeeService.getAllEmployees(0, 1).size());
    verify(employeeRepository, times(1)).findAllByIsDeletedFalse(any(Pageable.class));
  }

  @Test
  void testGetAllEmployeesFailure() {
    when(employeeRepository.findAllByIsDeletedFalse(any(Pageable.class))).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.getAllEmployees(0, 1));
    verify(employeeRepository, times(1)).findAllByIsDeletedFalse(any(Pageable.class));
  }

  @Test
  void testUpdateEmployeeSuccess() {
    when(employeeRepository.findByEmail(anyString())).thenReturn(employee);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
    EmployeeDto result = employeeService.updateEmployee(employeeDto);
    assertNotNull(result);
    assertEquals(employee.getEmail(), result.getEmail());
    verify(employeeRepository, times(1)).findByEmail(anyString());
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void testUpdateEmployeeFailure() {
    when(employeeRepository.findByEmail(anyString())).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.updateEmployee(employeeDto));
    verify(employeeRepository, times(1)).findByEmail(anyString());
  }

  @Test
  void testDeleteEmployeeSuccess() {
    when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(employee);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
    employeeService.deleteEmployee(10);
    verify(employeeRepository, times(1)).findByIdAndIsDeletedFalse(anyInt());
    verify(employeeRepository, times(1)).save(any(Employee.class));
  }

  @Test
  void testDeleteEmployeeNotExists() {
    when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> employeeService.deleteEmployee(11));
    verify(employeeRepository, times(1)).findByIdAndIsDeletedFalse(anyInt());
  }

  @Test
  void testDeleteEmployeeFailure() {
    when(employeeRepository.findByIdAndIsDeletedFalse(anyInt())).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.deleteEmployee(10));
    verify(employeeRepository, times(1)).findByIdAndIsDeletedFalse(anyInt());
  }

  @Test
  void testCreateEmployeeSuccess() {
    when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
    when(passwordEncoder.encode(any())).thenReturn("password");
    EmployeeDto result = employeeService.createEmployee(employeeDto);
    assertNotNull(result);
    assertEquals(employee.getEmail(), result.getEmail());
    verify(employeeRepository, times(1)).existsByEmail(anyString());
    verify(employeeRepository, times(1)).save(any(Employee.class));
    verify(passwordEncoder, times(1)).encode(anyString());
  }

  @Test
  void testCreateEmployeeAlreadyExists() {
    when(employeeRepository.existsByEmail(anyString())).thenReturn(true);
    assertThrows(DuplicateKeyException.class, () -> employeeService.createEmployee(employeeDto));
    verify(employeeRepository, times(1)).existsByEmail(anyString());
  }

  @Test
  void testCreateEmployeeFailure() {
    when(employeeRepository.existsByEmail(anyString())).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> employeeService.createEmployee(employeeDto));
    verify(employeeRepository, times(1)).existsByEmail(anyString());
  }

  @Test
  void testCreateSessionSuccess() {
    when(authenticationManager.authenticate(any())).thenReturn(null);
    assertInstanceOf(String.class, employeeService.createSession(employeeDto));
    verify(authenticationManager, times(1)).authenticate(any());
  }

  @Test
  void testCreateSessionFailure() {
    when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));
    assertThrows(UnAuthorizedException.class, () -> employeeService.createSession(employeeDto));
    verify(authenticationManager, times(1)).authenticate(any());
  }
}
