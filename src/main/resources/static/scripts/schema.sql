-- address
DROP TABLE IF EXISTS addresses cascade;
create table addresses
(
    id            bigserial primary key,
    apartment     varchar(255) not null,
    city          varchar(255) not null,
    entrance      int4         not null,
    floor         int4         not null,
    street_name   varchar(255) not null,
    street_number varchar(255) not null
);

-- sequence users
DROP SEQUENCE IF EXISTS user_sequence;
create sequence user_sequence increment by 20;

-- users
DROP TABLE IF EXISTS users cascade;
create table users
(
    role         varchar(31)   not null,
    id           int8          not null DEFAULT nextval('user_sequence')
        primary key,
    email        varchar(320)  not null
        constraint uk_email
            unique,
    first_name   varchar(255)  not null,
    last_name    varchar(255)  not null,
    password     varchar(1000) not null,
    phone        varchar(30)   not null,
    username     varchar(255)  not null
        constraint uk_username
            unique,
    availability boolean,
    chat_id      bigint,
    address_id   bigint
        constraint fk_address_id
            references addresses
);

-- dishes
DROP TABLE IF EXISTS dishes cascade;
create table dishes
(
    id            bigserial
        primary key,
    current_price double precision not null,
    description   varchar(255),
    dishname      varchar(255)     not null,
    img_path      varchar(255)     not null,
    quantity      integer          not null,
    vendor_id     bigint
        constraint fk_vendor_id
            references users,
    is_deleted    boolean
);

-- carts
DROP TABLE IF EXISTS carts cascade;
create table carts
(
    id          bigserial
        primary key,
    total_items integer,
    total_prise double precision,
    cust_id     bigint
        constraint fk_customer_id
            references users
);

-- cart_items
DROP TABLE IF EXISTS cart_items cascade;
create table cart_items
(
    id         bigserial
        primary key,
    quantity   integer,
    unit_price double precision,
    cart_id    bigint
        constraint fk_cart_id
            references carts,
    dish_id    bigint
        constraint fk_dish_id
            references dishes
);

-- orders
DROP TABLE IF EXISTS orders cascade;
create table orders
(
    id            bigserial
        primary key,
    cust_address  varchar(255)     not null,
    delivery_date timestamp,
    order_date    timestamp,
    order_status  varchar(255)     not null,
    payment_type  varchar(255)     not null,
    total_amount  double precision not null,
    courier_id    bigint           not null
        constraint fk_courier_id
            references users,
    cust_id       bigint           not null
        constraint fk_customer_id
            references users,
    vendor_id     bigint           not null
        constraint fk_vendor_id
            references users
);

-- order_items
DROP TABLE IF EXISTS order_items cascade;
create table order_items
(
    id       bigserial
        primary key,
    quantity integer not null,
    dish_id  bigint
        constraint fk_dish_id
            references dishes,
    order_id bigint  not null
        constraint fk_order_id
            references orders
);
