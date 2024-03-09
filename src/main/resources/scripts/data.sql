TRUNCATE TABLE addresses RESTART IDENTITY CASCADE;

insert into addresses (apartment, city, entrance, floor, street_name, street_number)
VALUES (1, 'Рязань', 1, 3, 'Ленина', '17'),
       (2, 'Рязань', 2, 4, 'Семашко', '17'),
       (3, 'Рязань', 3, 4, 'Гоголя', '17');
