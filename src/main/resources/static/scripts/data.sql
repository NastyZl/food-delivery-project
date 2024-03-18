TRUNCATE TABLE addresses RESTART IDENTITY CASCADE;

TRUNCATE TABLE users RESTART IDENTITY CASCADE;

insert into addresses (apartment, city, entrance, floor, street_name, street_number)
VALUES (1, 'Рязань', 1, 3, 'Ленина', '17'),
       (2, 'Рязань', 2, 4, 'Семашко', '17'),
       (3, 'Рязань', 3, 4, 'Гоголя', '17');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('ROLE_CUSTOMER', 'customer@gmail.com', 'Петр', 'Петров', '$2a$10$fNHMPKlNewKBFVveV3xR6uKcYXiZ0eS3Y8wxhxtRIstCEEzpC', '+79105038777', 'customer');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('ROLE_ADMIN', 'admin@gmail.com', 'Иван', 'Иванов', '$2a$10$MyqtfCuzsgr15U7e6uWpxOkExRMp.odbLKUPI5XQql5Go5J3lgql2', '+79994568787', 'admin');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('ROLE_ADMIN', 'admin@gmail.com', 'Иван', 'Иванов', '$2a$10$MyqtfCuzsgr15U7e6uWpxOkExRMp.odbLKUPI5XQql5Go5J3lgql2', '+79994568787', 'admin');

UPDATE users SET role = 'ROLE_VENDOR' WHERE id = 162;
UPDATE users SET role = 'ROLE_CUSTOMER' WHERE id = 123;

DELETE FROM users
WHERE id=143;

TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE carts RESTART IDENTITY CASCADE;
TRUNCATE TABLE dishes RESTART IDENTITY CASCADE;
