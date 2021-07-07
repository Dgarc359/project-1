-- DDL

create table users(
	user_id serial primary key,
	first_name varchar (30),
	last_name varchar (30),
	gender varchar (1),
	check (gender = 'M' or gender = 'F'),
	username varchar(30) unique not null,
	user_password varchar(30) not null
);

create table post(
	post_id serial primary key,
	user_id integer references users,
	title varchar(30) not null,
	country varchar(30),
	city varchar(30),
	tag varchar(30),
	rating integer constraint rating_less_than_five check (rating <= 5),
	constraint rating_greater_than_zero check (rating >= 0),
	post_timestamp timestamp not null
);

-- DML

insert into users values (default, 'Leah', 'Canavan', 'F', 'alpha','password123');
insert into users values (default, 'Richelle', 'Hunt','F','beta','password123');
insert into users values (default, 'Jorge','Olivero','M','charlie','password123');


insert into post values
(default, 1, 'Shrimp Linguini Alfredo','United States','New Orleans',null,0,'08-19-2000 04:05:06');
insert into post values (default, 2, 'Cuban Sandwich', 'United States','Miami','food',5,'05-30-2010 05:07:10');
insert into post values (default, 3, 'Inception', 'United States','Chicago','movie',3,'02-20-2012 10:05:06');
insert into post values (default, 1, 'Vanilla Ice Cream', 'United States','Chicago',null,3,'02-20-2012 10:05:06');

