package com.i2i.ems.service;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Type;
import com.i2i.ems.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private Employee employee;

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    employee = Employee.builder()
        .id(1)
        .name("Gobi")
        .dob(new Date(2003, 05, 23))
        .mobileNo(1234567890)
        .email("gobi@i2i.com")
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .build();
  }

  @Test
  void testLoadUserByUsernameSuccess() {
    when(employeeRepository.findByEmail(anyString())).thenReturn(employee);
    assertEquals(employee.getEmail(), userService.loadUserByUsername("gobi@i2i.com").getUsername());
  }

  @Test
  void testLoadUserByUsernameNotExists() {
    when(employeeRepository.findByEmail(anyString())).thenReturn(null);
    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("gobi@i2i.com"));
  }

  @Test
  void testLoadUserByUsernameFailure() {
    when(employeeRepository.findByEmail(anyString())).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> userService.loadUserByUsername("gobi@i2i.com"));
  }
}