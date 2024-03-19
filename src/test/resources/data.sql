-- Password for all users is 111

-- Vendors

INSERT INTO users (role, email, first_name, last_name, password, phone, username, address_id)
VALUES ('ROLE_VENDOR', 'petrov22@gmail.com', 'Сергей', 'Петров', '$2a$10$xju/nVCQVSEV1oWToLHBj.hbtyiQOD5SG2BUJbCl6tmQuHLQpEUA6', '+79105038796', 'petrov22', '?'),
       ('ROLE_VENDOR', 'egorov40@gmail.com', 'Иван', 'Егоров', '$2a$10$xju/nVCQVSEV1oWToLHBj.hbtyiQOD5SG2BUJbCl6tmQuHLQpEUA6', '+79105038777', 'egorov40','?');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('ROLE_ADMIN', 'admin@gmail.com', 'Иван', 'Иванов', '$2a$10$xju/nVCQVSEV1oWToLHBj.hbtyiQOD5SG2BUJbCl6tmQuHLQpEUA6', '+79994568787', 'admin');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('ROLE_ADMIN', 'admin@gmail.com', 'Иван', 'Иванов', '$2a$10$xju/nVCQVSEV1oWToLHBj.hbtyiQOD5SG2BUJbCl6tmQuHLQpEUA6', '+79994568787', 'admin');
