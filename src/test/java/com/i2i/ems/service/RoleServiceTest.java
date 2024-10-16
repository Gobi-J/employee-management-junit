package com.i2i.ems.service;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Role;
import com.i2i.ems.model.Type;
import com.i2i.ems.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {

  private RoleDto roleDto;
  private Role role;
  private Employee employee;

  @Mock
  private RoleRepository roleRepository;
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
        .dob(new Date(2003, 05, 23))
        .mobileNo(1234567890)
        .email("gobi@i2i.com")
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .role(role)
        .build();
  }

  @Test
  void addRole() {
  }

  @Test
  void deleteRole() {
  }

  @Test
  void getEmployeeRole() {
  }

  @Test
  void updateRole() {
  }
}