-- Addresses
insert into addresses (apartment, city, entrance, floor, street_name, street_number)
VALUES (1, 'Рязань', 1, 3, 'Ленина', '17'),
       (2, 'Рязань', 2, 4, 'Семашко', '17');

-- Vendors

INSERT INTO users (id, role, email, first_name, last_name, password, phone, username, address_id)
VALUES (4,'ROLE_VENDOR', 'petrov22@gmail.com', 'Сергей', 'Петров', '$2a$10$cXw6hS3jaImnq2tjoDmPHu/ae584fM2tOUmG1XfblgUj81zaoPFnG', '+79105038796', 'petrov22', 1),
       (5, 'ROLE_VENDOR', 'egorov40@gmail.com', 'Иван', 'Егоров', '$2a$10$..7XXbTVTOxnGAVfmNXdt.jTrNHOhzYsOrhZoGAjCtVE3iw1UOjxi', '+79105038777', 'egorov40',2);

INSERT INTO users (role, id, first_name, email, username, phone, last_name, password, availability, chat_id, address_id)
VALUES ('ROLE1_CUSTOMER', 1, 'Иван', 'ivanov@gmail.ru', 'customer1', '+79502589454', 'Иванов', '$2a$10$glLqIGIYiTwF4wOjZwFdOewh966/GEVrJUm2I4V.22Esz6Q8v67Wy', NULL, NULL, NULL);

INSERT INTO users (id, role, email, first_name, last_name, password, phone, username)
VALUES (3, 'ROLE_ADMIN', 'admin@gmail.com', 'Иван', 'Иванов', '$2a$10$MyqtfCuzsgr15U7e6uWpxOkExRMp.odbLKUPI5XQql5Go5J3lgql2', '+79994568787', 'admin');

INSERT INTO "users" (id, role, email, first_name, last_name, password, phone, username)
VALUES (4, 'ROLE_ADMIN', 'admin@gmail.com', 'Иван', 'Иванов', '$2a$10$MyqtfCuzsgr15U7e6uWpxOkExRMp.odbLKUPI5XQql5Go5J3lgql2', '+79994568787', 'admin');

UPDATE users SET chat_id = '0' WHERE id = 6;
UPDATE users SET phone = '+79206352885' WHERE id = 3;
UPDATE users SET role = 'ROLE_CUSTOMER' WHERE id = 123;

DELETE FROM users
WHERE id=143;

TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_items RESTART IDENTITY CASCADE;

TRUNCATE TABLE carts RESTART IDENTITY CASCADE;
TRUNCATE TABLE dishes RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;


INSERT INTO users (role, id, email, first_name, last_name, password, phone, username, address_id)
VALUES
    ('ROLE_VENDOR', 4, 'admin@example.com', 'Vendor', 'Vendor', '$2a$10$MyqtfCuzsgr15U7e6uWpxOkExRMp.odbLKUPI5XQql5Go5J3lgql2', '123-456-7890', 'adminuser', 1);
