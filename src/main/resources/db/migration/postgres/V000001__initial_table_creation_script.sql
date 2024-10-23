CREATE TABLE IF NOT EXISTS users_table (
    id BIGSERIAL PRIMARY KEY,
    date_created TIMESTAMP(6) DEFAULT NULL,
    last_modified TIMESTAMP(6) DEFAULT NULL,
    uuid VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_online BOOLEAN DEFAULT NULL,
    last_login TIMESTAMP(6) DEFAULT NULL,
    name VARCHAR(100) DEFAULT NULL,
    onboarding_stage VARCHAR(255) DEFAULT NULL,
    otp_code VARCHAR(255) DEFAULT NULL,
    otp_expire_time VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    user_token VARCHAR(255) DEFAULT NULL,
    user_type VARCHAR(255) DEFAULT NULL,
    verified BOOLEAN DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    date_created TIMESTAMP(6) DEFAULT NULL,
    last_modified TIMESTAMP(6) DEFAULT NULL,
    uuid VARCHAR(255) DEFAULT NULL,
    description VARCHAR(255) DEFAULT NULL,
    name VARCHAR(255) UNIQUE DEFAULT NULL,
    permissions TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS user_role (
    id BIGSERIAL PRIMARY KEY,
    date_created TIMESTAMP(6) DEFAULT NULL,
    last_modified TIMESTAMP(6) DEFAULT NULL,
    uuid VARCHAR(255) DEFAULT NULL,
    roleid BIGINT DEFAULT NULL,
    userrole BIGINT DEFAULT NULL,
    FOREIGN KEY (userrole) REFERENCES users_table (id),
    FOREIGN KEY (roleid) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS event (
    id BIGSERIAL PRIMARY KEY,
    date_created TIMESTAMP(6) DEFAULT NULL,
    last_modified TIMESTAMP(6) DEFAULT NULL,
    uuid VARCHAR(255) DEFAULT NULL,
    available_attendees_count INT NOT NULL,
    category VARCHAR(255) DEFAULT NULL,
    description TEXT,
    event_date TIMESTAMP(6) DEFAULT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_event (
    id BIGSERIAL PRIMARY KEY,
    date_created TIMESTAMP(6) DEFAULT NULL,
    last_modified TIMESTAMP(6) DEFAULT NULL,
    uuid VARCHAR(255) DEFAULT NULL,
    attendees_count INT NOT NULL,
    eventid BIGINT DEFAULT NULL,
    userevent BIGINT DEFAULT NULL,
    FOREIGN KEY (eventid) REFERENCES event (id),
    FOREIGN KEY (userevent) REFERENCES users_table (id)
);


INSERT INTO roles (date_created, last_modified, uuid, description, name, permissions)
VALUES
    (TIMESTAMP '2024-06-05 10:30:10', TIMESTAMP '2024-06-05 10:30:10', '18b56dc6-9813-440a-8c82-baf2eb10f264', 'Role for administrators', 'ROLE_ADMIN',
    '["CAN_VIEW_EVENTS", "CAN_SEARCH_EVENTS", "CAN_RESERVE_EVENT_TICKET", "CAN_CREATE_EVENT", "CAN_UPDATE_EVENT", "CAN_DELETE_EVENT", "CAN_VIEW_ALL_RESERVATIONS", "CAN_SEND_EVENT_NOTIFICATION", "CAN_VIEW_USERS", "CAN_DELETE_USERS", "CAN_ADD_ROLE", "CAN_UPDATE_ROLE", "CAN_DELETE_ROLE"]'::jsonb)
ON CONFLICT (name) DO NOTHING;  -- This handles uniqueness like INSERT IGNORE

INSERT INTO roles (date_created, last_modified, uuid, description, name, permissions)
VALUES
    (TIMESTAMP '2024-06-05 10:30:10', TIMESTAMP '2024-06-05 10:30:10', 'cae15553-1148-49c1-8e9d-f3dea70fb4d1', 'Role for users', 'ROLE_USER',
    '["CAN_VIEW_EVENTS", "CAN_SEARCH_EVENTS", "CAN_RESERVE_EVENT_TICKET"]'::jsonb)
ON CONFLICT (name) DO NOTHING;  -- This handles uniqueness like INSERT IGNORE