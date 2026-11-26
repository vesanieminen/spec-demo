package com.example.roombook.employee.ui;

import java.io.Serializable;

import com.example.roombook.employee.EmployeeService;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

/**
 * The Employee signed in to this Vaadin session.
 *
 * <p>Development stub until the single sign-on is decided (OQ-014, C-001): every session is signed in as the
 * same development Employee, created or updated through {@link EmployeeService#signIn} as a real sign-in would.
 */
@Component
@VaadinSessionScope
public class CurrentEmployee implements Serializable {

    static final String DEV_SSO_SUBJECT = "dev-employee";
    static final String DEV_NAME = "Development Employee";
    static final String DEV_EMAIL = "dev.employee@example.com";

    private final transient EmployeeService employeeService;
    private Long id;

    public CurrentEmployee(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * The id of the signed-in Employee.
     */
    public long id() {
        if (id == null) {
            id = employeeService.signIn(DEV_SSO_SUBJECT, DEV_NAME, DEV_EMAIL);
        }
        return id;
    }
}
