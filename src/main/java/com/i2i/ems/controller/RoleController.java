package com.i2i.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.service.RoleService;

/**
 * <p>
 * Controller that handles the role related operations.
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/roles")
public class RoleController {

  @Autowired
  private RoleService roleService;

  /**
   * <p>
   * Retrieves the role of the employee
   * </p>
   *
   * @param employeeId employee id whose role details are to be retrieved
   * @return {@link RoleDto} role details of the employee with http status code 200
   */
  @GetMapping
  public ResponseEntity<RoleDto> getRole(@PathVariable int employeeId) {
    return new ResponseEntity<>(roleService.getEmployeeRole(employeeId), HttpStatus.OK);
  }

  /**
   * <p>
   * Creates the role of the employee
   * </p>
   *
   * @param employeeId employee id to whom role is to be added
   * @param roleDto    role details to be added
   * @return {@link RoleDto} created role details with http status code 201
   */
  @PostMapping
  public ResponseEntity<RoleDto> createRole(@PathVariable int employeeId, @Validated @RequestBody RoleDto roleDto) {
    return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Updates the role of the employee
   * </p>
   *
   * @param employeeId employee id whose role is to be updated
   * @param roleDto    role details to be updated
   * @return {@link RoleDto} updated role details with http status code 200
   */
  @PutMapping
  public ResponseEntity<RoleDto> updateRole(@PathVariable int employeeId, @Validated @RequestBody RoleDto roleDto) {
    return new ResponseEntity<>(roleService.updateRole(employeeId, roleDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deletes the role of the employee
   * </p>
   *
   * @param employeeId employee id whose role is to be deleted
   * @return {@link HttpStatus} status of the operation with http status code 204
   */
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteRole(@PathVariable int employeeId) {
    roleService.deleteRole(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
