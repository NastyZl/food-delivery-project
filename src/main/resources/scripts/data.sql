TRUNCATE TABLE addresses RESTART IDENTITY CASCADE;

insert into addresses (apartment, city, entrance, floor, street_name, street_number)
VALUES (1, 'Рязань', 1, 3, 'Ленина', '17'),
       (2, 'Рязань', 2, 4, 'Семашко', '17'),
       (3, 'Рязань', 3, 4, 'Гоголя', '17');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('customer', 'customer@gmail.com', 'Петр', 'Петров', 1, '+79105038777', 'customer');

INSERT INTO "users" (role, email, first_name, last_name, password, phone, username)
VALUES ('admin', 'admin@gmail.com', 'Иван', 'Иванов', 1, '+79994568787', 'admin');

