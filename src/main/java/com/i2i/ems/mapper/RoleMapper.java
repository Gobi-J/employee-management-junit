package com.i2i.ems.mapper;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.model.Role;

/**
 * <p>
 * Mapper class that maps the role details.
 * </p>
 */
public class RoleMapper {

  /**
   * <p>
   * Maps the role model to role DTO.
   * </p>
   *
   * @param role all details of a role
   * @return {@link RoleDto} id, designation, level and department of the role
   */
  public static RoleDto modelToDto(Role role) {
    return RoleDto.builder()
        .id(role.getId())
        .designation(role.getDesignation())
        .build();
  }

  /**
   * <p>
   * Maps the role DTO to role model.
   * </p>
   *
   * @param roleDto id, designation, level and department of the role
   * @return {@link Role} all details of a role
   */
  public static Role dtoToModel(RoleDto roleDto) {
    return Role.builder()
        .id(roleDto.getId())
        .designation(roleDto.getDesignation())
        .level(roleDto.getLevel())
        .department(roleDto.getDepartment())
        .isDeleted(false)
        .build();
  }
}
