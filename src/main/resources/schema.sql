DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

CREATE TABLE application_user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    login      VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL
);

CREATE TABLE role
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        varchar(30)  NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES application_user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);