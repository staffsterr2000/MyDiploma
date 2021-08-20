--CREATE SEQUENCE application_user_sequence START 1 INCREMENT 1 AS BIGINT;
--CREATE SEQUENCE client_data_sequence START 1 INCREMENT 1 AS BIGINT;
--CREATE SEQUENCE doctor_data_sequence START 1 INCREMENT 1 AS BIGINT;
--CREATE SEQUENCE confirmation_token_sequence START 1 INCREMENT 1 AS BIGINT;
--CREATE SEQUENCE visit_sequence START 1 INCREMENT 1 AS BIGINT;

CREATE TABLE IF NOT EXISTS application_user (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(8) NOT NULL,
    image_link VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_account_locked BOOLEAN NOT NULL,
    is_enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS application_user_client (
    id BIGINT NOT NULL,
    client_data_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS application_user_doctor (
    id BIGINT NOT NULL,
    doctor_data_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS client_data (
    id BIGSERIAL PRIMARY KEY,
    passport_id BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS doctor_data (
    id BIGSERIAL PRIMARY KEY,
    passport_id BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    experience_age INTEGER NOT NULL,
    room INTEGER NOT NULL,
    description VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS visit (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    complaint VARCHAR(255) NOT NULL,
    accepted_at TIMESTAMP NOT NULL,
    appoints_at TIMESTAMP,
    status VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS confirmation_token (
    id BIGSERIAL PRIMARY KEY,
    app_user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL,
    confirmed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL
);

ALTER TABLE client_data
    ADD CONSTRAINT unique_client_passport_id UNIQUE (passport_id);

ALTER TABLE doctor_data
    ADD CONSTRAINT unique_doctor_passport_id UNIQUE (passport_id);

ALTER TABLE application_user_client
    ADD CONSTRAINT FK_client_data
    FOREIGN KEY (client_data_id)
    REFERENCES client_data;

ALTER TABLE application_user_doctor
    ADD CONSTRAINT FK_doctor_data
    FOREIGN KEY (doctor_data_id)
    REFERENCES doctor_data;

ALTER TABLE application_user_client
    ADD CONSTRAINT FK_application_user_client
    FOREIGN KEY (id)
    REFERENCES application_user;

ALTER TABLE application_user_doctor
    ADD CONSTRAINT FK_application_user_doctor
    FOREIGN KEY (id)
    REFERENCES application_user;

ALTER TABLE confirmation_token
    ADD CONSTRAINT FK_app_user
    FOREIGN KEY (app_user_id)
    REFERENCES application_user;

ALTER TABLE visit
       ADD CONSTRAINT FK_client_data
       FOREIGN KEY (client_id)
       REFERENCES client_data;

ALTER TABLE visit
       ADD CONSTRAINT FK_doctor_data
       FOREIGN KEY (doctor_id)
       REFERENCES doctor_data;