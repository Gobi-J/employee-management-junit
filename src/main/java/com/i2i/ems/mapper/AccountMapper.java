package com.i2i.ems.mapper;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.model.Account;

/**
 * <p>
 * Mapper class that maps the account details.
 * </p>
 */
public class AccountMapper {

  /**
   * <p>
   * Maps the account model to account DTO.
   * </p>
   *
   * @param account all details of an account
   * @return {@link AccountDto} id and account number of the account
   */
  public static AccountDto modelToDto(Account account) {
    return AccountDto.builder()
        .id(account.getId())
        .accountNumber(account.getAccountNumber())
        .build();
  }

  /**
   * <p>
   * Maps the account DTO to account model.
   * </p>
   *
   * @param accountDto id, bank name, account number and IFSC code of the account
   * @return {@link Account}
   * all details of an account
   */
  public static Account dtoToModel(AccountDto accountDto) {
    return Account.builder()
        .accountNumber(accountDto.getAccountNumber())
        .bankName(accountDto.getBankName())
        .ifscCode(accountDto.getIfscCode())
        .isDeleted(false)
        .build();
  }
}
