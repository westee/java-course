create table course_order (
    id      serial primary key,
    course_id int,
    user_id int,
    price   int,
    created_at  timestamp not null default  now(),
    updated_at  timestamp not null default  now(),
    status      varchar(10) not null default 'UNPAID' -- UNPAID PAID DELETED
)