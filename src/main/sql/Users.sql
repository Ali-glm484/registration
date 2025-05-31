use registration_system_db;

create table users (
    id int primary key auto_increment,
    first_name nvarchar(50) not null,
    last_name nvarchar(50) not null,
    age int not null,
    email nvarchar(50) unique not null,
    password nvarchar(50) not null
);