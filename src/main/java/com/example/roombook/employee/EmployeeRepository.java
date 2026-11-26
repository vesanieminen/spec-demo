package com.example.roombook.employee;

import static com.example.roombook.jooq.Tables.EMPLOYEE;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

    private final DSLContext ctx;

    public EmployeeRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Creates the Employee or updates the name and email of the existing one with the same single sign-on
     * subject, and returns its id.
     */
    public long upsert(String ssoSubject, String name, String email) {
        return ctx.insertInto(EMPLOYEE)
                .set(EMPLOYEE.SSO_SUBJECT, ssoSubject)
                .set(EMPLOYEE.NAME, name)
                .set(EMPLOYEE.EMAIL, email)
                .onConflict(EMPLOYEE.SSO_SUBJECT)
                .doUpdate()
                .set(EMPLOYEE.NAME, name)
                .set(EMPLOYEE.EMAIL, email)
                .returning(EMPLOYEE.ID)
                .fetchSingle(EMPLOYEE.ID);
    }
}
