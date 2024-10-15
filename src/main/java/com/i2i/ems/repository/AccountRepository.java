package com.i2i.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.i2i.ems.model.Account;

/**
 * <p>
 * Provides methods to access account details from the database.
 * </p>
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

  /**
   * <p>
   * Finds account by employee id.
   * </p>
   *
   * @param employeeId Id of the employee.
   * @return {@link Account} Account details of the employee.
   */
  Account findByIdAndIsDeletedFalse(int employeeId);
}