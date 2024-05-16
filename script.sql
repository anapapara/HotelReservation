create table users
(
    id   integer primary key,
    name varchar
);
create table rooms
(
    id          integer primary key
    room_number varchar,
    type        integer,
    price       double,
    is_available boolean,
    hotel_id    integer
        constraint rooms_hotels_id_fk
            references hotels
);
create table hotels
(
    id        integer primary key,
    name      varchar,
    latitude  varchar,
    longitude varchar
);
create table reservations
(
    id          integer primary key
    user_id     integer
        constraint fk_user references users,
    hotel_id    integer
        constraint fk_hotel references hotels,
    room_number varchar
        constraint fk_room references rooms (room_number),
    start_date  date,
    end_date    date
);
