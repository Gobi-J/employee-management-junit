package com.i2i.ems.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.service.AccountService;

/**
 * <p>
 * Controller that handles the account related operations.
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/accounts")
public class AccountController {

  @Autowired
  private AccountService accountService;

  /**
   * <p>
   * Retrieves the account details of the employee
   * </p>
   *
   * @param employeeId employee id whose account details are to be retrieved
   * @return {@link AccountDto} account object of the employee with http status code 200
   * @throws EmployeeException if employee account is not found
   */
  @GetMapping
  public ResponseEntity<AccountDto> getAccount(@PathVariable int employeeId)
      throws EmployeeException {
    return new ResponseEntity<>(accountService.getEmployeeAccount(employeeId), HttpStatus.OK);
  }

  /**
   * <p>
   * Adds new account to the employee
   * </p>
   *
   * @param employeeId employee id to whom account is to be added
   * @param accountDto account details to be added
   * @return {@link AccountDto} added account details with http status code 201
   * @throws EmployeeException     if any error occurs while adding account
   * @throws DuplicateKeyException if account already exists for the employee
   */
  @PostMapping
  public ResponseEntity<AccountDto> createAccount(@PathVariable int employeeId,
                                                  @Validated @RequestBody AccountDto accountDto)
      throws EmployeeException, DuplicateKeyException {
    return new ResponseEntity<>(accountService.addAccount(employeeId, accountDto), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Updates the account details of the employee
   * </p>
   *
   * @param employeeId employee id whose account details are to be updated
   * @param accountDto account details to be updated
   * @return {@link AccountDto} updated account details with http status code 200
   * @throws EmployeeException      if any error occurs while updating account
   * @throws NoSuchElementException if account not found
   */
  @PutMapping
  public ResponseEntity<AccountDto> updateAccount(@PathVariable int employeeId,
                                                  @Validated @RequestBody AccountDto accountDto)
      throws EmployeeException {
    return new ResponseEntity<>(accountService.updateAccount(employeeId, accountDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deletes the account details of the employee
   * </p>
   *
   * @param employeeId employee id whose account details are to be deleted
   * @return {@link HttpStatus} http status code 204
   * @throws EmployeeException      if any error occurs while deleting account
   * @throws NoSuchElementException if account not found
   */
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteAccount(@PathVariable int employeeId)
      throws EmployeeException {
    accountService.removeAccount(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}