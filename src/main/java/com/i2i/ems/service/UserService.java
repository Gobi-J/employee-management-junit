package com.i2i.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.i2i.ems.model.Employee;
import com.i2i.ems.repository.EmployeeRepository;

/**
 * <p>
 * Service class that handles business logic related to user.
 * </p>
 */
@Service
public class UserService implements UserDetailsService {

  @Autowired
  private EmployeeRepository employeeRepository;

  /**
   * <p>
   * Load user by username which is an email.
   * </p>
   *
   * @param username Email of the user.
   * @return {@link UserDetails} Details of the user.
   * @throws UsernameNotFoundException If user is not found.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Employee employee = employeeRepository.findByEmail(username);
    if (employee == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return employee;
  }
}