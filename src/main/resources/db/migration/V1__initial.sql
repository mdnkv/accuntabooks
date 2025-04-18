CREATE TABLE users_user (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE auth_authentication_token (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    token_string VARCHAR(255) NOT NULL UNIQUE,
    expiration_dt TIMESTAMP NOT NULL,
    CONSTRAINT
        fk_authentication_token_user
        FOREIGN KEY (user_id)
            REFERENCES users_user(id)
            ON DELETE CASCADE
);