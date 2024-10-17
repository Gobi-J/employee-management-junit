package com.i2i.ems.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  private AccountDto accountDto;

  @Mock
  private AccountService accountService;

  @InjectMocks
  private AccountController accountController;

  @BeforeEach
  void setUp() {
    accountDto = AccountDto.builder()
        .id(1)
        .accountNumber("1234567890")
        .build();
  }

  @Test
  void testGetAccount() {
    when(accountService.getEmployeeAccount(anyInt())).thenReturn(accountDto);
    ResponseEntity<AccountDto> response = accountController.getAccount(1);
    assertEquals(accountDto, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(accountService).getEmployeeAccount(1);
  }

  @Test
  void testCreateAccount() {
    when(accountService.addAccount(anyInt(), any(AccountDto.class))).thenReturn(accountDto);
    ResponseEntity<AccountDto> response = accountController.createAccount(1, accountDto);
    assertEquals(accountDto, response.getBody());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(accountService).addAccount(1, accountDto);
  }

  @Test
  void testUpdateAccount() {
    when(accountService.updateAccount(anyInt(), any(AccountDto.class))).thenReturn(accountDto);
    ResponseEntity<AccountDto> response = accountController.updateAccount(1, accountDto);
    assertEquals(accountDto, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(accountService).updateAccount(1, accountDto);
  }

  @Test
  void testDeleteAccount() {
    assertEquals(HttpStatus.NO_CONTENT, accountController.deleteAccount(1).getStatusCode());
    verify(accountService).removeAccount(1);
  }
}