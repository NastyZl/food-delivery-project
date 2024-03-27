-- Addresses
insert into addresses (apartment, city, entrance, floor, street_name, street_number)
VALUES (1, 'Рязань', 1, 3, 'Ленина', '17'),
       (2, 'Рязань', 2, 4, 'Семашко', '17');

-- Vendors

INSERT INTO users (role, email, first_name, last_name, password, phone, username, address_id, is_locked)
VALUES ('ROLE_VENDOR', 'petrov22@gmail.com', 'Сергей', 'Петров',
        '$2a$10$cXw6hS3jaImnq2tjoDmPHu/ae584fM2tOUmG1XfblgUj81zaoPFnG', '+79105038796', 'petrov22', 1, false),
       ('ROLE_VENDOR', 'egorov40@gmail.com', 'Иван', 'Егоров',
        '$2a$10$..7XXbTVTOxnGAVfmNXdt.jTrNHOhzYsOrhZoGAjCtVE3iw1UOjxi', '+79105038777', 'egorov40', 2, false);

-- Customer
INSERT INTO users (role, first_name, email, username, phone, last_name, password, is_locked)
VALUES ('ROLE_CUSTOMER', 'Иван', 'ivanov@gmail.ru', 'customer1', '+79502589454', 'Иванов',
        '$2a$10$glLqIGIYiTwF4wOjZwFdOewh966/GEVrJUm2I4V.22Esz6Q8v67Wy', false);

-- Courier
INSERT INTO users (role, first_name, email, username, phone, last_name, password, availability, chat_id, is_locked)
VALUES ('ROLE_COURIER', 'Настя', 'azlatovchena20@mail.ru', 'courier_1', '+79105038707', 'Златовчена',
        '$2a$10$6u8ItWkmsX6Fzm9f28iAd.sBwtETrv6MBjqD9z1oPI.DltrOXY5a6', 'false', 1232153246, false);

INSERT INTO users (role, first_name, email, username, phone, last_name, password, is_locked)
VALUES ('ROLE_ADMIN', 'Настя', 'a20@mail.ru', 'admin', '+79105038707', 'Златовчена',
        '$2a$12$ctQWKixhg3xrm.EzWfanNeg1SW7dEgYkQHzyGzIyDQjRktFErN75W', false);

-- INSERT INTO dishes (current_price, dishname, description, is_deleted, vendor_id, img_path, quantity)
-- VALUES (300, 'Картофельные драники', 'Нежнейший картофель с ярким сметанным соусом', 'false', 21, 'draniki.jpg', 40),
--        (500, 'Рыбные котлеты', 'Котлеты из щуки с салом на сковороде', 'false', 21, 'katlet.jpg', 40);
-- INSERT INTO dishes (current_price, dishname, description, is_deleted, vendor_id, img_path, quantity)
-- VALUES (400, 'Блинчики с мясом', 'Тонкие блинчики на молоке с сочным фаршем из индейки', 'false', 21, 'blin.jpg', 40),
--        (100, '«Летнее наслаждение»', 'Помидоры, огурцы и зелень с оливковым маслом и пряностью', 'false', 1,
--         'salat.jpg', 5),
--        (500, 'Куриные отбивные', 'Куриное филе в панировке с томатным соусом', 'false', 1, 'otbiv.jpg', 6);
-- truncate addresses cascade;
INSERT INTO dishes (current_price, description , dishname, img_path, quantity, vendor_id, is_deleted)
VALUES (100, 'Помидоры, огурцы и зелень с оливковым маслом и пряностью', 'Летнее наслаждение', 'salat.jpg', 5, 1,
        false),
       (600, 'Рыба с томатами, домашним майонезом и сырной корочкой', 'Филе минтая', 'mintai.jpg', 10, 1, false),
       (400, 'Узбекский плов из баранины с овощами', 'Плов', 'plov.jpg', 10, 1, false),
       (350, 'Кабачки с куриным фаршем и томатным соусом', 'Лодочки из кабачков', 'kabach.jpg', 10, 1, false),
       (400, 'Салат с сыром, рыбной консервой, яйцами и луком', 'Мимоза', 'mimoza.jpg', 10, 1, false),
       (500, 'Перец с говяжьим фаршем и зеленью', 'Перец фаршированный', 'perec.jpg', 10, 1, false),
       (400, 'Суп на основе свёклы со сметаной и салом', 'Борщ', 'borsch.jpg', 4, 21, false),
       (400, 'Насыпной пирог со спелой вишней', 'Пирог', 'pirog.jpg', 10, 21, false),
       (300, 'Нежнейший картофель с ярким сметанным соусом', 'Картофельные драники', 'draniki.jpg', 39, 21, false),
       (500, 'Котлеты из щуки с салом на сковороде', 'Рыбные котлеты', 'katlet.jpg', 39, 21, false),
       (270, 'C соленым огурцом, капустой, свеклой и морковью', 'Винегрет', 'vinegret.jpg', 3, 21, false),
       (400, 'Тонкие блинчики на молоке с сочным фаршем из индейки', 'Блинчики с мясом', 'blin.jpg', 36, 21, false),
       (500, 'Куриное филе в панировке с томатным соусом', 'Куриные отбивные', 'otbiv.jpg', 5, 1, false),
       (300, 'Суп из говядины с рисом, грецкими орехами', 'Суп харчо', 'cup.jpg', 4, 1, false);