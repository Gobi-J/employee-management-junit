package com.i2i.ems.controller;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.model.Skill;
import com.i2i.ems.service.SkillService;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

  private SkillDto skillDto;
  private Skill skill;

  @Mock
  private SkillService skillService;

  @InjectMocks
  private SkillController skillController;

  @BeforeEach
  void setUp() {
    skillDto = SkillDto.builder()
        .id(1)
        .name("Java")
        .build();

    skill = Skill.builder()
        .id(1)
        .name("Java")
        .build();
  }

  @Test
  void testGetSkillById() {
    when(skillService.getSkillById(anyInt())).thenReturn(skill);
    ResponseEntity<Skill> response = skillController.getSkillById(1);
    assertEquals(skill, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(skillService).getSkillById(1);
  }

  @Test
  void testGetSkillsByEmployeeId() {
    when(skillService.getEmployeeSkills(anyInt())).thenReturn(List.of(skillDto));
    ResponseEntity<List<SkillDto>> response = skillController.getSkillsByEmployeeId(1);
    assertEquals(List.of(skillDto), response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(skillService).getEmployeeSkills(1);
  }

  @Test
  void testAddSkill() {
    when(skillService.addSkill(any(SkillDto.class), anyInt())).thenReturn(skillDto);
    ResponseEntity<SkillDto> response = skillController.addSkill(skillDto, 1);
    assertEquals(skillDto, response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(skillService).addSkill(skillDto, 1);
  }

  @Test
  void testUpdateSkill() {
    when(skillService.updateSkill(any(SkillDto.class))).thenReturn(skillDto);
    ResponseEntity<SkillDto> response = skillController.updateSkill(skillDto, 1);
    assertEquals(skillDto, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(skillService).updateSkill(skillDto);
  }

  @Test
  void testDeleteSkill() {
    assertEquals(HttpStatus.NO_CONTENT, skillController.deleteSkill(1, 1).getStatusCode());
    verify(skillService).deleteSkill(1, 1);
  }

  @Test
  void testDeleteSkills() {
    assertEquals(HttpStatus.NO_CONTENT, skillController.deleteSkills(1).getStatusCode());
    verify(skillService).deleteSkill(1);
  }
}