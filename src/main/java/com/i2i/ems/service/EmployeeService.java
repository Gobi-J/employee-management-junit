package com.i2i.ems.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.helper.ForbiddenException;
import com.i2i.ems.helper.UnAuthorizedException;
import com.i2i.ems.mapper.EmployeeMapper;
import com.i2i.ems.model.Account;
import com.i2i.ems.model.Employee;
import com.i2i.ems.repository.EmployeeRepository;
import com.i2i.ems.util.JwtTokenUtil;

import lombok.NonNull;


/**
 * <p>
 * Service class that handles business logic related to employees
 * </p>
 *
 * @author Gobi
 * @version 1.0
 */
@Service
public class EmployeeService {

  private static final Logger logger = LogManager.getLogger(EmployeeService.class);

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  /**
   * <p>
   * Saves the employee to the database
   * </p>
   *
   * @param employee employee details to be saved
   * @return {@link Employee}saved employee details
   * @throws EmployeeException if employee cannot be saved
   */
  public Employee saveEmployee(@NonNull Employee employee) throws EmployeeException {
    logger.debug("Saving employee {}", employee.getName());
    try {
      employee = employeeRepository.save(employee);
      logger.info("Employee {} saved successfully", employee.getId());
    } catch (Exception e) {
      logger.error("Cannot save employee {}", employee.getName(), e);
      throw new EmployeeException("Cannot save employee with name " + employee.getName(), e);
    }
    return employee;
  }

  /**
   * <p>
   * Adds details to currently logged in employee
   * </p>
   *
   * @param employeeDto employee details given by user to add
   * @return {@link EmployeeDto} added employee details
   * @throws ForbiddenException     if employee is not authorized to add details of other employee
   * @throws NoSuchElementException if employee is not found with given email
   * @throws EmployeeException      if any other error occurs while adding employee
   */
  public EmployeeDto addEmployee(@NonNull EmployeeDto employeeDto) throws ForbiddenException {
    logger.debug("Adding employee {}", employeeDto.getName());
    Employee employee;
    try {
      employee = EmployeeMapper.dtoToModel(employeeDto);
      if (!employeeRepository.existsByEmail(employeeDto.getEmail())) {
        throw new NoSuchElementException("Employee not present. Register first.");
      }
      employee.setPassword(employee.getPassword());
      employee.setId(employee.getId());
      employee.setUUID(UUID.randomUUID().toString());
      saveEmployee(employee);
      logger.info("Employee {} added successfully", employeeDto.getId());
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        logger.error("Employee {} not present", employeeDto.getName());
        throw e;
      }
      logger.error("Cannot add employee {}", employeeDto.getName(), e);
      throw new EmployeeException("Cannot save employee with name " + employeeDto.getName(), e);
    }
    return EmployeeMapper.modelToDto(employee);
  }
  
  /**
   * <p>
   * Gets employee details by id
   * </p>
   *
   * @param id employee id to get details
   * @return {@link Employee} employee details of given id
   * @throws ForbiddenException if employee is not authorized to view other employee details
   * @throws EmployeeException  if any other error occurs while getting employee
   */
  public EmployeeDto getEmployee(int id) throws ForbiddenException, EmployeeException {
    logger.debug("Getting employee {}", id);
    Employee employee = getEmployeeById(id);
//    if (!email.equals(employee.getEmail())) {
//      throw new ForbiddenException("You are not authorized to view this employee");
//    }
    logger.info("Returning employee {}", employee.getId());
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Gets all employees of requested size and page
   * </p>
   *
   * @param page page number to get employees
   * @param size number of employees to get in a page
   * @return {@link List<EmployeeDto>} details of all employees
   * @throws EmployeeException if any error occurs while getting employees
   */
  public List<EmployeeDto> getAllEmployees(int page, int size) {
    logger.debug("Getting all employees");
    List<Employee> employees;
    try {
      Pageable pageable = PageRequest.of(page, size);
      employees = employeeRepository.findAllByIsDeletedFalse(pageable);
      logger.info("Returning employees list");
    } catch (Exception e) {
      logger.error("Cannot get all employees", e);
      throw new EmployeeException("Cannot getting all employees", e);
    }
    return employees.stream()
        .map(EmployeeMapper::modelsToDtos)
        .collect(Collectors.toList());
  }

  /**
   * <p>
   * Updates employee details
   * If id is not given, it will be fetched from email
   * </p>
   *
   * @param employeeDto employee details to be updated
   * @return {@link EmployeeDto} updated employee details
   * @throws EmployeeException if any error occurs while updating employee
   */
  public EmployeeDto updateEmployee(@NonNull EmployeeDto employeeDto) {
    logger.debug("Updating employee {}", employeeDto.getName());
    try {
      if (employeeDto.getId() == 0) {
        employeeDto.setId(employeeRepository.findByEmail(employeeDto.getEmail()).getId());
      }
      employeeDto = EmployeeMapper.modelToDto(saveEmployee(EmployeeMapper.dtoToModel(employeeDto)));
      logger.info("Employee {} updated successfully", employeeDto.getId());
    } catch (Exception e) {
      logger.error("Cannot update employee {}", employeeDto.getName(), e);
      throw new EmployeeException("Cannot updating employee " + employeeDto.getId(), e);
    }
    return employeeDto;
  }

  /**
   * <p>
   * Deletes employee by id
   * </p>
   *
   * @param id employee id to delete
   * @throws EmployeeException      if any error occurs while deleting employee
   * @throws NoSuchElementException if employee is not found with given id
   */
  public void deleteEmployee(int id) {
    logger.debug("Deleting employee {}", id);
    try {
      Employee employee = getEmployeeById(id);
      employee.setIsDeleted(true);
      Account account = employee.getAccount();
      if (null != account) {
        account.setIsDeleted(true);
      }
      saveEmployee(employee);
      logger.info("Employee {} deleted successfully", id);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        logger.error("Employee {} not found", id);
        throw e;
      }
      logger.error("Cannot delete employee {}", id, e);
      throw new EmployeeException("Cannot delete employee " + id, e);
    }
  }

  /**
   * <p>
   * Gets employee by id
   * </p>
   *
   * @param id employee id to get details
   * @return {@link Employee} employee details of given id
   * @throws NoSuchElementException if employee is not found with given id
   * @throws EmployeeException      if any other error occurs while getting employee
   */
  protected Employee getEmployeeById(int id) {
    logger.debug("Getting employee {}", id);
    Employee employee;
    try {
      employee = employeeRepository.findByIdAndIsDeletedFalse(id);
      if (null == employee) {
        throw new NoSuchElementException("Employee with id " + id + " not found");
      }
      logger.info("Returning employee {}", employee.getId());
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        logger.error("Employee {} not found", id);
        throw e;
      }
      logger.error("Cannot get employee {}", id, e);
      throw new EmployeeException("Cannot get employee " + id, e);
    }
    return employee;
  }

  /**
   * <p>
   * Registers a new employee
   * </p>
   *
   * @param employeeDto employee details to be registered
   * @return {@link EmployeeDto} registered employee details
   * @throws DuplicateKeyException if employee with same email already exists
   * @throws EmployeeException     if any other error occurs while registering employee
   */
  public EmployeeDto createEmployee(EmployeeDto employeeDto) {
    logger.debug("Registering employee {}", employeeDto.getEmail());
    Employee employee;
    try {
      employee = EmployeeMapper.dtoToModel(employeeDto);
      if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
        throw new DuplicateKeyException("Employee with the same email already exists");
      }
      employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
      saveEmployee(employee);
      logger.info("Employee {} registered successfully", employee.getId());
    } catch (DuplicateKeyException e) {
      logger.error("Employee {} already exists", employeeDto.getEmail());
      throw e;
    } catch (Exception e) {
      logger.error("Cannot register employee {}", employeeDto.getEmail(), e);
      throw new EmployeeException("Cannot register employee with email " + employeeDto.getEmail(), e);
    }
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Logs in an employee
   * </p>
   *
   * @param employeeDto employee details to login
   * @return {@link String} jwt token
   * @throws UnAuthorizedException if email or password is invalid
   */
  public String createSession(EmployeeDto employeeDto) {
    try {
      authenticationManager
          .authenticate(
              new UsernamePasswordAuthenticationToken(
                  employeeDto.getEmail(), employeeDto.getPassword()
              )
          );
      return JwtTokenUtil.generateAccessToken(employeeDto.getEmail());
    } catch (BadCredentialsException e) {
      throw new UnAuthorizedException("Invalid email or password");
    }
  }
}