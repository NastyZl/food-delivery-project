DROP TABLE IF EXISTS addresses cascade;
create table addresses
(
    id            bigserial    not null,
    apartment     varchar(255) not null,
    city          varchar(255) not null,
    entrance      int4         not null,
    floor         int4         not null,
    street_name   varchar(255) not null,
    street_number varchar(255) not null,
    primary key (id)
);

DROP TABLE IF EXISTS users cascade;
create table users
(
    role         varchar(31)   not null,
    id           int8          not null,
    email        varchar(320)  not null unique,
    first_name   varchar(255)  not null,
    last_name    varchar(255)  not null,
    password     varchar(1000) not null,
    phone        varchar(30)   not null,
    username     varchar(255)  not null unique,
    availability boolean,
    address_id   int8 unique,
    primary key (id)
);
DROP SEQUENCE IF EXISTS user_sequence;
create sequence user_sequence start 1 increment 20;

alter table users
    add constraint "users_addresses_id-foreign" foreign key (address_id) references addresses;