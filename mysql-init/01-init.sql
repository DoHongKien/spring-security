-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS `role` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS `user_role` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user`(id),
    FOREIGN KEY (role_id) REFERENCES `role`(id)
);

CREATE TABLE IF NOT EXISTS `customer` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    date_of_birth DATE,
    status VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS `refresh_token` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    device_id VARCHAR(255),
    is_revoked VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    revoked_at TIMESTAMP NULL
);

-- Insert initial roles
INSERT INTO `role` (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM `role` WHERE name = 'ADMIN');

INSERT INTO `role` (name)
SELECT 'USER'
WHERE NOT EXISTS (SELECT 1 FROM `role` WHERE name = 'USER');

-- Insert admin user (password: admin123)
INSERT INTO `user` (username, password, email, phone, status)
SELECT 'admin', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'admin@example.com', '0123456789', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE username = 'admin');
-- Insert user user (password: user)
INSERT INTO `user` (username, password, email, phone, status)
SELECT 'user', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'user@example.com', '0123456789', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE username = 'user');

-- Assign admin role to admin user
INSERT INTO `user_role` (user_id, role_id)
SELECT u.id, r.id
FROM `user` u, `role` r
WHERE u.username = 'admin' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM `user_role` ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);
-- Assign user role to user user
INSERT INTO `user_role` (user_id, role_id)
SELECT u.id, r.id
FROM `user` u, `role` r
WHERE u.username = 'user' AND r.name = 'USER'
AND NOT EXISTS (
    SELECT 1 FROM `user_role` ur 
    WHERE ur.user_id = u.id AND ur.role_id = r.id
);

-- Insert sample customer
INSERT INTO `customer` (username, full_name, gender, date_of_birth, status, email, phone_number)
SELECT 'customer1', 'John Doe', 'MALE', '1990-01-01', 'ACTIVE', 'customer1@example.com', '0987654321'
WHERE NOT EXISTS (SELECT 1 FROM `customer` WHERE username = 'customer1');

-- Add more tables and data as needed 