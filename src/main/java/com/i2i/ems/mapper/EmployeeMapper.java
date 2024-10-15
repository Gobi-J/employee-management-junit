package com.i2i.ems.mapper;

import java.util.Date;
import java.util.stream.Collectors;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.model.Account;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Role;
import com.i2i.ems.model.Type;
import com.i2i.ems.util.DateUtil;

/**
 * <p>
 * Mapper class that maps the employee details into required format.
 * </p>
 */
public class EmployeeMapper {

  /**
   * <p>
   * Maps the employee model to employee DTO.
   * </p>
   *
   * @param employee all details of an employee
   * @return {@link EmployeeDto} id, name, email, mobile number and age of the employee
   */
  public static EmployeeDto modelsToDtos(Employee employee) {
    return EmployeeDto.builder()
        .UUID(employee.getUUID())
        .name(employee.getName())
        .email(employee.getEmail())
        .mobileNo(employee.getMobileNo())
        .age(DateUtil.getYearsBetween(employee.getDob(), new Date()))
        .build();
  }

  /**
   * <p>
   * Maps the employee model to employee DTO.
   * </p>
   *
   * @param employee all details of an employee
   * @return {@link EmployeeDto} id, name, email, mobile number, age, role, account and skills of the employee
   */
  public static EmployeeDto modelToDto(Employee employee) {
    return EmployeeDto.builder()
        .UUID(employee.getUUID())
        .name(employee.getName())
        .email(employee.getEmail())
        .mobileNo(employee.getMobileNo())
        .age(DateUtil.getYearsBetween(employee.getDob(), new Date()))
        .role(null != employee.getRole() ? RoleDto.builder()
            .id(employee.getRole().getId())
            .designation(employee.getRole().getDesignation())
            .build() : null)
        .account(null != employee.getAccount() ? AccountDto.builder()
            .id(employee.getAccount().getId())
            .accountNumber(employee.getAccount().getAccountNumber())
            .build() : null)
        .skills(null != employee.getSkills() ? employee.getSkills().stream().
            map(SkillMapper::modelToDto)
            .collect(Collectors.toList()) : null)
        .build();
  }

  /**
   * <p>
   * Maps the employee DTO to employee model.
   * </p>
   *
   * @param employeeDto id, name, date of birth, email, mobile number, role, account and skills of the employee
   * @return {@link Employee} all details of an employee
   */
  public static Employee dtoToModel(EmployeeDto employeeDto) {
    return Employee.builder()
        .id(employeeDto.getId())
        .name(employeeDto.getName())
        .dob(employeeDto.getDob())
        .mobileNo(employeeDto.getMobileNo())
        .email(employeeDto.getEmail())
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .role(null != employeeDto.getRole() ? Role.builder()
            .id(employeeDto.getRole().getId())
            .designation(employeeDto.getRole().getDesignation())
            .level(employeeDto.getRole().getLevel())
            .department(employeeDto.getRole().getDepartment())
            .isDeleted(false)
            .build() : null)
        .account(null != employeeDto.getAccount() ? Account.builder()
            .id(employeeDto.getAccount().getId())
            .accountNumber(employeeDto.getAccount().getAccountNumber())
            .bankName(employeeDto.getAccount().getBankName())
            .ifscCode(employeeDto.getAccount().getIfscCode())
            .isDeleted(false)
            .build() : null)
        .skills(null != employeeDto.getSkills() ? employeeDto.getSkills()
            .stream().map(SkillMapper::dtoToModel)
            .collect(Collectors.toList()) : null)
        .build();
  }
}