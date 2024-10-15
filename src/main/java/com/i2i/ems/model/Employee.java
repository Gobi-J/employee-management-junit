package com.i2i.ems.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * Model class that maps the employee details.
 * Contains all associated fields of an employee.
 * </p>
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String UUID;
  private String name;
  private Date dob;
  private String email;

  @Column(name = "mobile_no")
  private long mobileNo;
  private Boolean isDeleted;
  @Enumerated(EnumType.STRING)
  private Type userType;
  private String password;

  @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinColumn(name = "account_id")
  private Account account;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
  @JoinColumn(name = "role_id")
  private Role role;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
  @JoinTable(name = "employee_skill",
      joinColumns = @JoinColumn(name = "employee_id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id"))
  private List<Skill> skills;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(userType.name()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}