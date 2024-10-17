package com.i2i.ems.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.mapper.SkillMapper;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Skill;
import com.i2i.ems.repository.SkillRepository;

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

  /**
   * <p>
   * Save a skill
   * </p>
   *
   * @param skill Details of the skill to be saved
   * @return {@link Skill} Details of the skill saved
   */
  public Skill saveSkill(Skill skill) {
    try {
      return skillRepository.save(skill);
    } catch (Exception e) {
      throw new EmployeeException("Skill not saved");
    }
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
    try {
      Skill skill = SkillMapper.dtoToModel(skillDto);
      if (skillRepository.existsByName(skillDto.getName())) {
        skill = skillRepository.findByName(skillDto.getName());
      }
      Employee employee = employeeService.getEmployeeById(employeeId);
      employee.getSkills().add(skill);
      employeeService.saveEmployee(employee);
      return SkillMapper.modelToDto(skill);
    } catch (Exception e) {
      throw new EmployeeException("Skill not added");
    }
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
    try {
      return skillRepository.findByIdAndIsDeletedFalse(id);
    } catch (Exception e) {
      throw new EmployeeException("Skill not found");
    }
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
    try {
      Employee employee = employeeService.getEmployeeById(employeeId);
      return employee.getSkills().stream()
          .map(SkillMapper::modelToDto)
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new EmployeeException("Skills not found");
    }
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
    skill = saveSkill(skill);
    return SkillMapper.modelToDto(skill);
  }

  /**
   * <p>
   * Delete a skill
   * </p>
   *
   * @param employeeId id of the employee whose skill is to be deleted
   */
  public void deleteSkills(int employeeId) {
    Employee employee = employeeService.getEmployeeById(employeeId);
    if (null == employee) {
      throw new NoSuchElementException("Employee " + employeeId + " not found");
    }
    if (null == employee.getSkills()) {
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
