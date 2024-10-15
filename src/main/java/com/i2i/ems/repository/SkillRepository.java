package com.i2i.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2i.ems.model.Skill;

/**
 * <p>
 * Provides methods to access skill details from the database.
 * </p>
 */
public interface SkillRepository extends JpaRepository<Skill, Integer> {

  /**
   * <p>
   * Find skill by id and isDeleted flag.
   * </p>
   *
   * @param id Id of the skill.
   * @return {@link Skill} Skill details.
   */
  Skill findByIdAndIsDeletedFalse(int id);

  /**
   * <p>
   * Find skill by name.
   * </p>
   *
   * @param name Name of the skill.
   * @return {@link Skill} Skill details.
   */
  Skill findByName(String name);

  /**
   * <p>
   * Check if skill exists by name.
   * </p>
   *
   * @param name Name of the skill.
   * @return boolean True if skill exists, else false.
   */
  boolean existsByName(String name);
}
