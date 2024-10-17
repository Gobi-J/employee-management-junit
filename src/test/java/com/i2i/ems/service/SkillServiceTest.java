package com.i2i.ems.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Skill;
import com.i2i.ems.model.Type;
import com.i2i.ems.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

  private Skill skill;
  private SkillDto skillDto;
  private Employee employee;

  @Mock
  private SkillRepository skillRepository;

  @Mock
  private EmployeeService employeeService;

  @InjectMocks
  private SkillService skillService;

  @BeforeEach
  void setUp() {
    skill = Skill.builder()
        .id(1)
        .name("Java")
        .category("Programming")
        .institute("Oracle")
        .build();

    skillDto = SkillDto.builder()
        .id(1)
        .name("Java")
        .build();

    employee = Employee.builder()
        .id(1)
        .name("Gobi")
        .dob(new Date(2003, 05, 23))
        .mobileNo(1234567890)
        .email("gobi@i2i.com")
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .skills(List.of(skill))
        .build();
  }

  @Test
  void testSaveSkillSuccess() {
    when(skillRepository.save(skill)).thenReturn(skill);
    assertEquals(skill.getName(), skillService.saveSkill(skill).getName());
    verify(skillRepository).save(skill);
  }

  @Test
  void testSaveSkillFailure() {
    when(skillRepository.save(skill)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> skillService.saveSkill(skill));
    verify(skillRepository).save(skill);
  }

  @Test
  void testAddSkillSuccess() {
    employee.setSkills(new ArrayList<>());
    when(skillRepository.existsByName(anyString())).thenReturn(false);
    when(employeeService.getEmployeeById(anyInt())).thenReturn(employee);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    SkillDto result = skillService.addSkill(skillDto, 1);
    assertNotNull(result);
    assertEquals(skillDto.getName(), result.getName());
    verify(skillRepository, times(1)).existsByName(skillDto.getName());
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(employeeService, times(1)).saveEmployee(employee);
  }

  @Test
  void testAddSkillSkillExists() {
    employee.setSkills(new ArrayList<>());
    when(skillRepository.existsByName(anyString())).thenReturn(true);
    when(skillRepository.findByName(anyString())).thenReturn(skill);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    SkillDto result = skillService.addSkill(skillDto, 1);
    assertNotNull(result);
    assertEquals(skillDto.getName(), result.getName());
    verify(skillRepository, times(1)).existsByName(skillDto.getName());
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(employeeService, times(1)).saveEmployee(employee);
  }

  @Test
  void testAddSkillFailure() {
    when(skillRepository.existsByName(skillDto.getName())).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> skillService.addSkill(skillDto, 1));
    verify(skillRepository, times(1)).existsByName(skillDto.getName());
  }

  @Test
  void testGetSkillByIdSuccess() {
    when(skillRepository.findByIdAndIsDeletedFalse(1)).thenReturn(skill);
    assertEquals(skill.getName(), skillService.getSkillById(1).getName());
    verify(skillRepository).findByIdAndIsDeletedFalse(1);
  }

  @Test
  void testGetSkillByIdFailure() {
    when(skillRepository.findByIdAndIsDeletedFalse(1)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> skillService.getSkillById(1));
    verify(skillRepository).findByIdAndIsDeletedFalse(1);
  }

  @Test
  void testGetEmployeeSkillsSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    List<SkillDto> result = skillService.getEmployeeSkills(1);
    assertNotNull(result);
    assertEquals(employee.getSkills().size(), result.size());
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeSkillsFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> skillService.getEmployeeSkills(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testUpdateSkillSuccess() {
    when(skillRepository.findByIdAndIsDeletedFalse(anyInt())).thenReturn(skill);
    when(skillRepository.save(any(Skill.class))).thenReturn(skill);
    assertEquals(skill.getName(), skillService.updateSkill(skillDto).getName());
  }

  @Test
  void testUpdateSkillSkillNotExists() {
    when(skillRepository.findByIdAndIsDeletedFalse(skillDto.getId())).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> skillService.updateSkill(skillDto));
  }

  @Test
  void testUpdateSkillFailure() {
    when(skillRepository.findByIdAndIsDeletedFalse(skillDto.getId())).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> skillService.updateSkill(skillDto));
    verify(skillRepository).findByIdAndIsDeletedFalse(skillDto.getId());
  }

  @Test
  void testDeleteSkillsSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    assertDoesNotThrow(() -> skillService.deleteSkills(1));
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(employeeService, times(1)).saveEmployee(employee);
  }

  @Test
  void testDeleteSkillsEmployeeNotExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> skillService.deleteSkills(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testDeleteSkillsEmployeeHasNoSkills() {
    employee.setSkills(null);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(NoSuchElementException.class, () -> skillService.deleteSkills(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testDeleteSkillsFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(EmployeeException.class);
    assertThrows(EmployeeException.class, () -> skillService.deleteSkills(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testDeleteSkillSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    when(skillRepository.findByIdAndIsDeletedFalse(1)).thenReturn(skill);
    assertDoesNotThrow(() -> skillService.deleteSkill(1, 1));
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(employeeService, times(1)).saveEmployee(employee);
  }
}