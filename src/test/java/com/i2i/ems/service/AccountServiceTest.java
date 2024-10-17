package com.i2i.ems.service;

import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.model.Account;
import com.i2i.ems.model.Employee;
import com.i2i.ems.model.Type;
import com.i2i.ems.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  private AccountDto accountDto;
  private Account account;
  private Employee employee;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private EmployeeService employeeService;

  @InjectMocks
  private AccountService accountService;

  @BeforeEach
  void setUp() {
    accountDto = AccountDto.builder()
        .id(1)
        .accountNumber("1234567890")
        .build();

    account = Account.builder()
        .id(1)
        .accountNumber("1234567890")
        .bankName("ICICI")
        .ifscCode("ICIC0000001")
        .build();

    employee = Employee.builder()
        .id(1)
        .name("Gobi")
        .dob(new Date(2003, 05, 23))
        .mobileNo(1234567890)
        .email("gobi@i2i.com")
        .isDeleted(false)
        .userType(Type.EMPLOYEE)
        .account(account)
        .build();
  }

  @Test
  void testSaveAccountSuccess() {
    when(accountRepository.save(any(Account.class))).thenReturn(account);
    Account result = accountService.saveAccount(account);
    assertNotNull(result);
    assertEquals(account.getAccountNumber(), result.getAccountNumber());
    assertEquals(account.getId(), result.getId());
    verify(accountRepository, times(1)).save(any(Account.class));
  }

  @Test
  void testSaveAccountFailure() {
    when(accountRepository.save(account)).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> accountService.saveAccount(account));
    verify(accountRepository, times(1)).save(account);
  }

  @Test
  void testAddAccountSuccess() {
    employee.setAccount(null);
    when(accountRepository.save(any(Account.class))).thenReturn(account);
    when(employeeService.getEmployeeById(anyInt())).thenReturn(employee);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    AccountDto result = accountService.addAccount(10, accountDto);
    assertNotNull(result);
    assertEquals(accountDto.getAccountNumber(), result.getAccountNumber());
    assertEquals(accountDto.getId(), result.getId());
    verify(accountRepository, times(1)).save(any(Account.class));
    verify(employeeService, times(1)).getEmployeeById(10);
    verify(employeeService, times(1)).saveEmployee(employee);
  }

  @Test
  void testAddAccountAlreadyExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(DuplicateKeyException.class, () -> accountService.addAccount(1, accountDto));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testAddAccountFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> accountService.addAccount(1, accountDto));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeAccountSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    AccountDto result = accountService.getEmployeeAccount(1);
    assertNotNull(result);
    assertEquals(account.getAccountNumber(), result.getAccountNumber());
    assertEquals(account.getId(), result.getId());
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeAccountEmployeeNotExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> accountService.getEmployeeAccount(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeAccountAccountNotExists() {
    employee.setAccount(null);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(NoSuchElementException.class, () -> accountService.getEmployeeAccount(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testGetEmployeeAccountFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> accountService.getEmployeeAccount(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testUpdateAccountSuccess() {
    when(employeeService.getEmployeeById(anyInt())).thenReturn(employee);
    when(accountRepository.save(any(Account.class))).thenReturn(account);
    when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
    AccountDto result = accountService.updateAccount(1, accountDto);
    assertNotNull(result);
    assertEquals(accountDto.getAccountNumber(), result.getAccountNumber());
    assertEquals(accountDto.getId(), result.getId());
    verify(employeeService, times(3)).getEmployeeById(1);
    verify(accountRepository, times(1)).save(account);
    verify(employeeService, times(2)).saveEmployee(employee);
  }

  @Test
  void testUpdateAccountAccountNotExists() {
    employee.setAccount(null);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(NoSuchElementException.class, () -> accountService.updateAccount(1, accountDto));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testUpdateAccountFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> accountService.updateAccount(1, accountDto));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testRemoveAccountSuccess() {
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    when(accountRepository.save(account)).thenReturn(account);
    when(employeeService.saveEmployee(employee)).thenReturn(employee);
    accountService.removeAccount(1);
    verify(employeeService, times(1)).getEmployeeById(1);
    verify(accountRepository, times(1)).save(account);
    verify(employeeService, times(1)).saveEmployee(employee);
  }

  @Test
  void testRemoveAccountEmployeeNotExists() {
    when(employeeService.getEmployeeById(1)).thenReturn(null);
    assertThrows(NoSuchElementException.class, () -> accountService.removeAccount(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testRemoveAccountAccountNotExists() {
    employee.setAccount(null);
    when(employeeService.getEmployeeById(1)).thenReturn(employee);
    assertThrows(NoSuchElementException.class, () -> accountService.removeAccount(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }

  @Test
  void testRemoveAccountFailure() {
    when(employeeService.getEmployeeById(1)).thenThrow(new EmployeeException("Error occurred with server"));
    assertThrows(EmployeeException.class, () -> accountService.removeAccount(1));
    verify(employeeService, times(1)).getEmployeeById(1);
  }
}