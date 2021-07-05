create table tableA (
    id int primary key,
    stringCol varchar(200) not null,
    numericCol numeric,
    booleanCol boolean,
    dateCol date
);

insert into tableA values (1, 'first', 10, 1, '2021-07-05');
insert into tableA values (2, 'second', 10, 0, '2021-07-05');
insert into tableA values (3, 'third', 20, 1, '2021-07-06');

