create table "user" (
    id serial primary key,
    username varchar(20) unique not null,
    encrypted_password varchar(20) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);

create table role (
    id serial primary key,
    name varchar(20) unique not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);

insert into role (id, name) values(1, '学生');
insert into role (id, name) values(2, '老师');
insert into role (id, name) values(3, '管理员');

create table user_role (
    id serial primary key,
    user_id integer not null,
    role_id integer not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);

insert into "user" (id, username, encrypted_password) values (1, '学生', 123456);
insert into "user" (id, username, encrypted_password) values (2, '老师',123456);
insert into "user" (id, username, encrypted_password) values (3, '管理员',123456);
insert into user_role (id, user_id, role_id) values (1, 1, 1);
insert into user_role (id, user_id, role_id) values (2, 2, 2);
insert into user_role (id, user_id, role_id) values (3, 3, 3);

create table permission (
    id serial primary key,
    name varchar(50) not null,
    role_id integer not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    status varchar(10) not null default 'OK'
);

insert into permission ( name, role_id) values ('登录用户', 1);
insert into permission ( name, role_id) values ('登录用户', 2);
insert into permission ( name, role_id) values ('登录用户', 3);
insert into permission ( name, role_id) values ('上传课程', 2);
insert into permission ( name, role_id) values ('上传课程', 3);
insert into permission ( name, role_id) values ('管理用户', 3);