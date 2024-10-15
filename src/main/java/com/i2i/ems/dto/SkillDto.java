package com.i2i.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Data transfer object that maps the skill details.
 * </p>
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto {
  private int id;
  private String name;
  private String category;
  private String institute;
}