create table session(
    id serial primary key,
    cookie varchar(50) unique not null,
    user_id int not null
);

insert into session (cookie, user_id) values ('student_user_cookie', 1);
insert into session (cookie, user_id) values ('teacher_user_cookie', 2);
insert into session (cookie, user_id) values ('admin_user_cookie', 3);
alter sequence session_id_seq restart with 4;