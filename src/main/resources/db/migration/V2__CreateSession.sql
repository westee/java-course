create table session(
    id serial primary key,
    cookie varchar(50) unique not null,
    user_id int not null
)