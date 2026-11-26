package com.example.roombook.employee;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Employees as the single sign-on provides them.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates or updates the Employee from the single sign-on data at sign-in and returns the Employee's id.
     */
    @Transactional
    public long signIn(String ssoSubject, String name, String email) {
        return repository.upsert(ssoSubject, name, email);
    }
}
