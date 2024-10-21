package com.i2i.ems.service;

import java.util.NoSuchElementException;

import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.mapper.RoleMapper;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Role;
import com.i2i.ems.repository.RoleRepository;

/**
 * <p>
 * Service class that handles business logic related to roles
 * </p>
 */
@Service
public class RoleService {

  private static final Logger logger = LogManager.getLogger(RoleService.class);

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private EmployeeService employeeService;

  /**
   * <p>
   * Saving account details
   * </p>
   *
   * @param role Role to be saved
   * @return {@link Role} saved role details
   */
  private Role saveRole(Role role) {
    try {
      role = roleRepository.save(role);
    } catch (EmployeeException e) {
      logger.error(e);
      throw new EmployeeException("Cannot save role ", e);
    }
    return role;
  }

  /**
   * <p>
   * Retrieves the role of the given id
   * </p>
   *
   * @param id role id to be retrieved
   * @return {@link Role} role details of the given id
   */
  private Role getRole(int id) {
    logger.debug("Getting role {}", id);
    try {
      return roleRepository.findByIdAndIsDeletedFalse(id);
    } catch (Exception e) {
      logger.error(e);
      throw new EmployeeException("Cannot get role", e);
    }
  }

  /**
   * <p>
   * Adding new role for an employee
   * </p>
   *
   * @param employeeId who needs to add new role
   * @param roleDto role details to be added
   * @return {@link RoleDto} role with its id
   */
  public RoleDto addRole(int employeeId, @NonNull RoleDto roleDto) {
    logger.debug("Adding role {}", roleDto.getDesignation());
    Role role;
    try {
      Employee employee = employeeService.getEmployeeById(employeeId);
      if (null == employee) {
        throw new NoSuchElementException("Employee " + employeeId + " not found");
      }
      role = saveRole(RoleMapper.dtoToModel(roleDto));
      employee.setRole(role);
      employeeService.saveEmployee(employee);
      logger.info("Role {} added", role.getId());
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        logger.warn(e);
        throw e;
      }
      logger.error(e);
      throw new EmployeeException("Cannot add role", e);
    }
    return RoleMapper.modelToDto(role);
  }

  /**
   * <p>
   * Role of the given employee
   * </p>
   *
   * @param employeeId employee whose role to be retrieved
   * @return {@link RoleDto} retrieved role details
   */
  public RoleDto getEmployeeRole(int employeeId) {
    logger.debug("Getting role {} of an employee ", employeeId);
    Role role;
    try {
      role = employeeService.getEmployeeById(employeeId).getRole();
      if (null == role) {
        throw new NoSuchElementException("Role for employee " + employeeId + " not found");
      }
      logger.info("Role found for employee {}", employeeId);
    } catch (NoSuchElementException e) {
      logger.warn(e);
      throw e;
    } catch (Exception e) {
      logger.error(e);
      throw new EmployeeException("Cannot get role for employee " + employeeId, e);
    }
    return RoleMapper.modelToDto(role);
  }

  /**
   * <p>
   * Update the role of the employee
   * </p>
   *
   * @param employeeId employee whose role to be updated
   * @param roleDto    new role details
   * @return {@link RoleDto} updated role details
   */
  public RoleDto updateRole(int employeeId, @NonNull RoleDto roleDto) {
    logger.debug("Updating role {} of an employee ", employeeId);
    try {
      Employee employee = employeeService.getEmployeeById(employeeId);
      if (null == employee) {
        throw new NoSuchElementException("Role for employee " + employeeId + " not found");
      }
      Role role = roleRepository.findByDesignationAndDepartment(roleDto.getDesignation(), roleDto.getDepartment());
      if (null == role) {
        roleDto = addRole(employeeId, roleDto);
        employee.setRole(getRole(roleDto.getId()));
      } else {
        employee.setRole(role);
      }
      employeeService.saveEmployee(employee);
      logger.info("Role updated for employee {}", employeeId);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        logger.warn(e);
        throw e;
      }
      logger.error(e);
      throw new EmployeeException("Cannot update role", e);
    }
    return roleDto;
  }

  /**
   * <p>
   * Delete role of given employee id
   * </p>
   *
   * @param id id of the employee whose role to delete
   */
  public void deleteRole(int id) {
    logger.debug("Deleting role {}", id);
    try {
      Employee employee = employeeService.getEmployeeById(id);
      if (null == employee) {
        throw new NoSuchElementException("Employee " + id + " not found");
      }
      Role role = employee.getRole();
      if (null == role) {
        throw new NoSuchElementException("Role for employee " + id + " not found");
      }
      employee.setRole(null);
      role.setIsDeleted(true);
      saveRole(role);
      employeeService.saveEmployee(employee);
      logger.info("Role {} deleted", id);
    } catch (Exception e) {
      if (e instanceof NoSuchElementException) {
        logger.warn(e);
        throw e;
      }
      logger.error(e);
      throw new EmployeeException("Cannot delete role", e);
    }
  }
}
