package com.i2i.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Model class that maps the account details.
 * </p>
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String bankName;
  private String accountNumber;
  private String ifscCode;
  private Boolean isDeleted;
}