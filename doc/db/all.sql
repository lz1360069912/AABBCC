create table user
(
    id   int auto_increment
        primary key,
    name varchar(20) null,
    pwd  varchar(20) null
);

insert into user
values (null, '小黑', '123456');