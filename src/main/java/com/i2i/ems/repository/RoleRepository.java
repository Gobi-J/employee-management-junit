package com.i2i.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.i2i.ems.model.Role;

/**
 * <p>
 * Provides methods to access role details from the database.
 * </p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

  /**
   * <p>
   * Retrieve role details by id.
   * </p>
   *
   * @param id Id of the role.
   * @return {@link Role} Role details.
   */
  Role findByIdAndIsDeletedFalse(int id);

  /**
   * <p>
   * Retrieve role details by designation and department.
   * </p>
   *
   * @param designation Designation of the role.
   * @param department Department of the role.
   * @return {@link Role} Role details.
   */
  Role findByDesignationAndDepartment(String designation, String department);
}
