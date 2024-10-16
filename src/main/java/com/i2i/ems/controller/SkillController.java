package com.i2i.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.model.Skill;
import com.i2i.ems.service.SkillService;

/**
 * <p>
 * Controller that handles the skill related operations.
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/skills")
public class SkillController {

  @Autowired
  private SkillService skillService;

  /**
   * <p>
   * Retrieves the skill by id.
   * </p>
   *
   * @param id skill id to be retrieved.
   * @return {@link Skill} skill details with http status code 200.
   */
  @GetMapping("{id}")
  public ResponseEntity<Skill> getSkillById(@PathVariable int id) {
    return new ResponseEntity<>(skillService.getSkillById(id), HttpStatus.OK);
  }

  /**
   * <p>
   * Retrieves the skills by employee id.
   * </p>
   *
   * @param employeeId employee id to be retrieved.
   * @return {@link List<SkillDto>} list of skills with http status code 200.
   */
  @GetMapping
  public ResponseEntity<List<SkillDto>> getSkillsByEmployeeId(@PathVariable int employeeId) {
    return new ResponseEntity<>(skillService.getEmployeeSkills(employeeId), HttpStatus.OK);
  }

  /**
   * <p>
   * Adds the skill.
   * </p>
   *
   * @param skillDto   skill details to be added.
   * @param employeeId employee id to whom skill is to be added.
   * @return {@link SkillDto} skill details with http status code 201.
   */
  @PostMapping
  public ResponseEntity<SkillDto> addSkill(@RequestBody SkillDto skillDto, @PathVariable int employeeId) {
    return new ResponseEntity<>(skillService.addSkill(skillDto, employeeId), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Updates the skill details.
   * </p>
   *
   * @param skillDto   skill details to be updated.
   * @param employeeId employee id to whom skill is to be updated.
   * @return {@link SkillDto} skill details with http status code 200.
   */
  @PutMapping
  public ResponseEntity<SkillDto> updateSkill(@RequestBody SkillDto skillDto, @PathVariable int employeeId) {
    return new ResponseEntity<>(skillService.updateSkill(skillDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deletes the skill of the employee.
   * </p>
   *
   * @param employeeId employee id to whom skill is to be deleted.
   * @return {@link HttpStatus} http status code 204 if skill is deleted.
   */
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteSkills(@PathVariable int employeeId) {
    skillService.deleteSkill(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * <p>
   * Deletes the skills of the employee.
   * </p>
   *
   * @param id         skill id to be deleted.
   * @param employeeId employee id to whom skill is to be deleted.
   * @return {@link HttpStatus} http status code 204 if skill is deleted.
   */
  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteSkill(@PathVariable int id, @PathVariable int employeeId) {
    skillService.deleteSkill(id, employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}