package com.i2i.ems.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.service.RoleService;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

  private RoleDto roleDto;

  @Mock
  private RoleService roleService;

  @InjectMocks
  private RoleController roleController;

  @BeforeEach
  void setUp() {
    roleDto = RoleDto.builder()
        .designation("Software Engineer")
        .build();
  }

  @Test
  void testGetRole() {
    when(roleService.getEmployeeRole(anyInt())).thenReturn(roleDto);
    ResponseEntity<RoleDto> response = roleController.getRole(1);
    assertEquals(roleDto, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(roleService).getEmployeeRole(1);
  }

  @Test
  void testCreateRole() {
    when(roleService.addRole(anyInt(), any(RoleDto.class))).thenReturn(roleDto);
    ResponseEntity<RoleDto> response = roleController.createRole(1, roleDto);
    assertEquals(roleDto, response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(roleService).addRole(1, roleDto);
  }

  @Test
  void testUpdateRole() {
    when(roleService.updateRole(anyInt(), any(RoleDto.class))).thenReturn(roleDto);
    ResponseEntity<RoleDto> response = roleController.updateRole(1, roleDto);
    assertEquals(roleDto, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(roleService).updateRole(1, roleDto);
  }

  @Test
  void testDeleteRole() {
    assertEquals(HttpStatus.NO_CONTENT, roleController.deleteRole(1).getStatusCode());
    verify(roleService).deleteRole(1);
  }
}