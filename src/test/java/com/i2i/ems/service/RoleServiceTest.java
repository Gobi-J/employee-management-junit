package com.i2i.ems.service;

import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Role;
import com.i2i.ems.model.Type;
import com.i2i.ems.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  private RoleDto roleDto;
  private Role role;
  private Employee employee;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private EmployeeService employeeService;

  @InjectMocks
  private RoleService roleService;

  @BeforeEach
  void setUp() {
    roleDto = RoleDto.builder()
        .designation("Software Engineer")
        .build();

    role = Role.builder()
        .id(1)
        .designation("Software Engineer")
        .build();

    employee = Employee.builder()
        .id(1)
        .name("Gobi")
        .dob(new Date(2003-5-23))
        .mobileNo(1234567890)
        .email("gobi@i2i.com")
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .role(role)
        .build();
  }

  @Test
  void testAddRoleSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(roleRepository.save(any(Role.class))).thenReturn(role);
    RoleDto result = roleService.addRole(1, roleDto);
    assertNotNull(result);
    assertEquals(roleDto.getDesignation(), result.getDesignation());
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(roleRepository, times(1)).save(any(Role.class));
  }

  @Test
  void testAddRoleEmployeeNotExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> roleService.addRole(1, roleDto));
    verify(employeeService).getEmployeeById(1);
  }

  @Test
  void testAddRoleFailure() {
    when(employeeService.getEmployeeById(anyInt())).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> roleService.addRole(1, roleDto));
    verify(employeeService).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeRoleSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    RoleDto result = roleService.getEmployeeRole(1);
    assertNotNull(result);
    assertEquals(role.getDesignation(), result.getDesignation());
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeRoleNotExists() {
    employee.setRole(null);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(NoSuchElementException.class, () -> roleService.getEmployeeRole(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeRoleFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> roleService.getEmployeeRole(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testUpdateRoleSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(roleRepository.findByDesignationAndDepartment(roleDto.getDesignation(), roleDto.getDepartment())).thenReturn(role);
    RoleDto result = roleService.updateRole(1, roleDto);
    assertNotNull(result);
    assertEquals(roleDto.getDesignation(), result.getDesignation());
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(roleRepository, times(1)).findByDesignationAndDepartment(roleDto.getDesignation(), roleDto.getDepartment());
  }

  @Test
  void testUpdateRoleEmployeeNotExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> roleService.updateRole(1, roleDto));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testUpdateRoleFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> roleService.updateRole(1, roleDto));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testDeleteRoleSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(roleRepository.save(any(Role.class))).thenReturn(role);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    assertDoesNotThrow(() -> roleService.deleteRole(1));
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(roleRepository, times(1)).save(any(Role.class));
    verify(employeeService, times(1)).saveEmployee(employee);
  }

  @Test
  void testDeleteRoleEmployeeNotExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> roleService.deleteRole(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testDeleteRoleRoleNotExists() {
    employee.setRole(null);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(NoSuchElementException.class, () -> roleService.deleteRole(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testDeleteRoleFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> roleService.deleteRole(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }
}