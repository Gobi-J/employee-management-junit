package com.i2i.ems.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Data transfer object that maps the account details.
 * </p>
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
  private int id;
  @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]+)*$", message = "Bank Name should contain only alphabets")
  private String bankName;
  @Pattern(regexp = "^\\d+$", message = "Account number should contain only numbers")
  private String accountNumber;
  @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "IFSC code should contain only alphanumeric elements")
  private String ifscCode;
}