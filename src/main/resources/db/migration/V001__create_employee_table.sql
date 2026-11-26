CREATE SEQUENCE employee_seq START WITH 1 INCREMENT BY 1 CACHE 50;

CREATE TABLE employee
(
    id          BIGINT DEFAULT nextval('employee_seq') PRIMARY KEY,
    sso_subject VARCHAR(255) NOT NULL,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    CONSTRAINT employee_sso_subject_uk UNIQUE (sso_subject),
    CONSTRAINT employee_email_uk UNIQUE (email),
    CONSTRAINT employee_email_ck CHECK (email ~ '^[^@[:space:]]+@[^@[:space:]]+$')
);
