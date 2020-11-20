create table session(
    id serial primary key,
    cookie varchar(50) unique not null,
    user_id int not null
);

insert into session (cookie, user_id) values ('test_user_1', 1);
insert into session (cookie, user_id) values ('test_user_2', 2);
insert into session (cookie, user_id) values ('test_user_3', 3);
alter sequence session_id_seq restart with 4;