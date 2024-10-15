package com.i2i.ems.controller;

import java.util.List;
import java.security.Principal;

import com.i2i.ems.helper.EmployeeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.helper.ForbiddenException;
import com.i2i.ems.service.EmployeeService;

/**
 * <p>
 * Controller that handles HTTP requests related to employees
 * </p>
 */
@RestController
@RequestMapping("v1/employees")
public class EmployeeController {

  private static final Logger logger = LogManager.getLogger(EmployeeController.class);

  @Autowired
  private EmployeeService employeeService;

  /**
   * <p>
   * Adding new employee
   * </p>
   *
   * @param employee employee details given by user to add
   * @return {@link EmployeeDto} added employee details with http status code 201
   * @throws ForbiddenException if the logged-in user is not changing their own details
   * @throws EmployeeException if the employee details are invalid
   */
  @PostMapping
  public ResponseEntity<EmployeeDto> addEmployee(@Validated @RequestBody EmployeeDto employee)
      throws ForbiddenException {
    logger.debug("Adding employee {}", employee.getName());
    return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Getting employee details by id
   * </p>
   *
   * @param id employee id to get details
   * @return {@link EmployeeDto} employee details with http status code 200 if employee exists
   * @throws ForbiddenException if the logged-in user is not retrieving their own details
   */
  @GetMapping("{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable int id)
      throws ForbiddenException {
    logger.debug("Getting employee {}", id);
    return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
  }

  /**
   * <p>
   * Getting all employees
   * </p>
   *
   * @param page page number
   *             default value is 0
   * @param size number of employees per page
   *             default value is 10
   * @return {@link List<EmployeeDto>} list of employees with http status code 200
   * @throws EmployeeException if the employee details are unable to retrieve
   */
  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
    logger.debug("Getting all employees");
    return new ResponseEntity<>(employeeService.getAllEmployees(page, size), HttpStatus.OK);
  }

  /**
   * <p>
   * Updating employee details
   * </p>
   *
   * @param employeeDto employee details given by user to update
   * @return {@link EmployeeDto} updated employee details with http status code 200
   * @throws EmployeeException if the employee details are invalid
   */
  @PutMapping
  public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto) {
    logger.debug("Updating employee {}", employeeDto.getId());
    return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deleting employee details by id
   * </p>
   *
   * @param id employee id to delete details
   * @return {@link HttpStatus} http status code 204 if deleted successfully
   * @throws EmployeeException if the employee details are unable to delete
   */
  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Integer id) {
    logger.debug("Deleting employee {}", id);
    employeeService.deleteEmployee(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * <p>
   * Register a new employee with their email and password.
   * </p>
   *
   * @param employeeDto EmployeeDto object with email and password.
   * @return {@link EmployeeDto} with email and id of an employee.
   * @throws EmployeeException if the employee details are invalid
   */
  @PostMapping("/register")
  public ResponseEntity<EmployeeDto> registerUser(@RequestBody EmployeeDto employeeDto) {
    logger.debug("Registering employee {}", employeeDto.getEmail());
    return new ResponseEntity<>(employeeService.createEmployee(employeeDto), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Login an employee with their email and password.
   * </p>
   *
   * @param employeeDto EmployeeDto object with email and password.
   * @return {@link EmployeeDto} with their all details and a token in header.
   * @throws EmployeeException if the employee details are invalid
   */
  @PostMapping("/login")
  public ResponseEntity<HttpStatus> loginUser(@RequestBody EmployeeDto employeeDto) {
    logger.debug("Logging in employee {}", employeeDto.getEmail());
    String token = employeeService.createSession(employeeDto);
    return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
  }
}
