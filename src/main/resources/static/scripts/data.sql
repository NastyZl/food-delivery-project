-- Addresses
insert into addresses (apartment, city, entrance, floor, street_name, street_number)
VALUES (1, 'Рязань', 1, 3, 'Ленина', '17'),
       (2, 'Рязань', 2, 4, 'Семашко', '17');

-- Vendors

INSERT INTO users (id, role, email, first_name, last_name, password, phone, username, address_id)
VALUES (4,'ROLE_VENDOR', 'petrov22@gmail.com', 'Сергей', 'Петров', '$2a$10$cXw6hS3jaImnq2tjoDmPHu/ae584fM2tOUmG1XfblgUj81zaoPFnG', '+79105038796', 'petrov22', 1),
       (5, 'ROLE_VENDOR', 'egorov40@gmail.com', 'Иван', 'Егоров', '$2a$10$..7XXbTVTOxnGAVfmNXdt.jTrNHOhzYsOrhZoGAjCtVE3iw1UOjxi', '+79105038777', 'egorov40',2);

-- Customer
INSERT INTO users (role, id, first_name, email, username, phone, last_name, password, availability, chat_id, address_id)
VALUES ('ROLE_CUSTOMER', 1, 'Иван', 'ivanov@gmail.ru', 'customer1', '+79502589454', 'Иванов', '$2a$10$glLqIGIYiTwF4wOjZwFdOewh966/GEVrJUm2I4V.22Esz6Q8v67Wy', NULL, NULL, NULL);

-- Courier
INSERT INTO users (role, id, first_name, email, username, phone, last_name, password, availability, chat_id, address_id)
VALUES ('ROLE_COURIER', 6, 'Настя', 'azlatovchena20@mail.ru', 'courier_1', '+79105038707', 'Златовчена', '$2a$10$6u8ItWkmsX6Fzm9f28iAd.sBwtETrv6MBjqD9z1oPI.DltrOXY5a6', 'false', 1232153246, NULL);

INSERT INTO dishes (id, current_price, dishname, description, is_deleted, vendor_id, img_path, quantity)
VALUES (1, 300, 'Картофельные драники', 'Нежнейший картофель с ярким сметанным соусом', 'false', 5, 'draniki.jpg', 40),
       (2, 500, 'Рыбные котлеты', 'Котлеты из щуки с салом на сковороде', 'false', 5, 'katlet.jpg', 40);
INSERT INTO dishes (id, current_price, dishname, description, is_deleted, vendor_id, img_path, quantity)
VALUES (3, 400, 'Блинчики с мясом', 'Тонкие блинчики на молоке с сочным фаршем из индейки', 'false', 5, 'blin.jpg', 40),
       (4, 100, 'Салат «Летнее наслаждение»', 'Помидоры, огурцы и зелень с оливковым маслом и пряностью', 'false', 4, 'salat.jpg', 5),
       (5, 500, 'Куриные отбивные', 'Куриное филе в панировке с томатным соусом', 'false', 4, 'otbiv.jpg', 6);

TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_items RESTART IDENTITY CASCADE;

TRUNCATE TABLE carts RESTART IDENTITY CASCADE;
TRUNCATE TABLE dishes RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;
