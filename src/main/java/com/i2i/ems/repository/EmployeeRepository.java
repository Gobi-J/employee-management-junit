package com.i2i.ems.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.Email;

import com.i2i.ems.model.Employee;

/**
 * <p>
 * Provides methods to access employee details from the database.
 * </p>
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

  /**
   * <p>
   * Retrieves all the employees from the database.
   * </p>
   *
   * @param pageable Pageable object to get the page details.
   * @return {@link Page<Employee>} Details of the employees in the page.
   */
  Page<Employee> findAllByIsDeletedFalse(Pageable pageable);

  /**
   * <p>
   * Retrieves the employees from the given id.
   * </p>
   *
   * @param id Id of the employee.
   * @return {@link Employee} Details of the employee.
   */
  Employee findByIdAndIsDeletedFalse(Integer id);

  /**
   * <p>
   * Checks if the employee exists with given mobileNo or email.
   * </p>
   *
   * @param mobileNo Mobile number of the employee.
   * @param email Email of the employee.
   * @return boolean True if the employee exists, else false.
   */
  boolean existsByMobileNoOrEmailAndIsDeletedFalse(long mobileNo, String email);

  /**
   * <p>
   * Retrieves the employees from the given email.
   * </p>
   *
   * @param email Email of the employee.
   * @return {@link Employee} Details of the employee.
   */
  Employee findByEmail(String email);

  /**
   * <p>
   * Checks if the employee exists with the given email.
   * </p>
   *
   * @param email Email of the employee.
   * @return boolean True if the employee exists, else false.
   */
  boolean existsByEmailAndIsDeletedFalse(String email);
}