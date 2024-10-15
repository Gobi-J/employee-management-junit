package com.i2i.ems.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.mapper.SkillMapper;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Skill;
import com.i2i.ems.repository.SkillRepository;

import lombok.NonNull;

/**
 * <p>
 * Service class that handles business logic related to skills
 * </p>
 */
@Service
public class SkillService {

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private EmployeeService employeeService;

  public Skill saveSkill(Skill skill) {
    return skillRepository.save(skill);
  }

  /**
   * <p>
   * Add a skill to an employee
   * </p>
   *
   * @param skillDto   Details of the skill to be added
   * @param employeeId id of the employee to whom the skill is to be added
   * @return {@link SkillDto} Details of the skill added
   */
  public SkillDto addSkill(SkillDto skillDto, int employeeId) {
    Skill skill = SkillMapper.dtoToModel(skillDto);
    if (skillRepository.existsByName(skillDto.getName())) {
      skill = skillRepository.findByName(skillDto.getName());
    }
    Employee employee = employeeService.getEmployeeById(employeeId);
    employee.getSkills().add(skill);
    employeeService.saveEmployee(employee);
    return SkillMapper.modelToDto(skill);
  }

  /**
   * <p>
   * Get all skills
   * </p>
   *
   * @return {@link List<SkillDto>} List of all skills
   */
  public List<SkillDto> getAllSkills() {
    return skillRepository.findAll().stream()
        .map(SkillMapper::modelToDto)
        .collect(Collectors.toList());
  }

  /**
   * <p>
   * Get a skill by id
   * </p>
   *
   * @param id id of the skill to be fetched
   * @return {@link Skill} Details of the skill fetched
   */
  public Skill getSkillById(int id) {
    return skillRepository.findByIdAndIsDeletedFalse(id);
  }

  /**
   * <p>
   * Get all skills of an employee
   * </p>
   *
   * @param employeeId id of the employee whose skills are to be fetched
   * @return {@link List<SkillDto>} List of all skills of the employee
   */
  public List<SkillDto> getEmployeeSkills(int employeeId) {
    Employee employee = employeeService.getEmployeeById(employeeId);
    return employee.getSkills().stream()
        .map(SkillMapper::modelToDto)
        .collect(Collectors.toList());
  }

  /**
   * <p>
   * Update a skill
   * </p>
   *
   * @param skillDto Details of the skill to be updated
   * @return {@link SkillDto} Details of the skill updated
   */
  public SkillDto updateSkill(@NonNull SkillDto skillDto) {
    Skill skill = getSkillById(skillDto.getId());
    if (skill == null) {
      throw new NoSuchElementException("Skill not found");
    }
    skill = SkillMapper.dtoToModel(skillDto);
    return SkillMapper.modelToDto(saveSkill(skill));
  }

  /**
   * <p>
   * Delete a skill
   * </p>
   *
   * @param employeeId id of the employee whose skill is to be deleted
   */
  public void deleteSkill(int employeeId) {
    Employee employee = employeeService.getEmployeeById(employeeId);
    if (null == employee) {
      throw new NoSuchElementException("Employee " + employeeId + " not found");
    }
    if (employee.getSkills().isEmpty()) {
      throw new NoSuchElementException("Employee " + employeeId + " has no skills");
    }
    employee.setSkills(null);
    employeeService.saveEmployee(employee);
  }

  /**
   * <p>
   * Delete a skill of an employee
   * </p>
   *
   * @param id         id of the skill to be deleted
   * @param employeeId employeeId of the employee whose skill is to be deleted
   */
  public void deleteSkill(int id, int employeeId) {
    Skill skill = getSkillById(id);
    if (skill == null) {
      throw new NoSuchElementException("Skill not found");
    }
    Employee employee = employeeService.getEmployeeById(employeeId);
    employee.setSkills(
        employee.getSkills()
            .stream()
            .filter(s -> !s.getName().equals(skill.getName()))
            .collect(Collectors.toList())
    );
    employeeService.saveEmployee(employee);
  }
}
