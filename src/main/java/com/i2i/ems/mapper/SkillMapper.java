package com.i2i.ems.mapper;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.model.Skill;

/**
 * <p>
 * Mapper class that maps the skill details.
 * </p>
 */
public class SkillMapper {

  /**
   * <p>
   * Maps the skill DTO to skill model.
   * </p>
   *
   * @param skillDto id, name, category and institute of the skill
   * @return {@link Skill} all details of a skill
   */
  public static Skill dtoToModel(SkillDto skillDto) {
    return Skill.builder()
        .id(skillDto.getId())
        .name(skillDto.getName())
        .category(skillDto.getCategory())
        .institute(skillDto.getInstitute())
        .isDeleted(false)
        .build();
  }

  /**
   * <p>
   * Maps the skill model to skill DTO.
   * </p>
   *
   * @param skill all details of a skill
   * @return {@link SkillDto} id and name of the skill
   */
  public static SkillDto modelToDto(Skill skill) {
    return SkillDto.builder()
        .id(skill.getId())
        .name(skill.getName())
        .build();
  }
}
