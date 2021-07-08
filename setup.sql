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
	post_timestamp date not null
);

-- DML

insert into users values (default, 'Leah', 'Canavan', 'F', 'alpha','password123');
insert into users values (default, 'Richelle', 'Hunt','F','beta','password123');
insert into users values (default, 'Jorge','Olivero','M','charlie','password123');


--insert into post values (default, 1, 'Shrimp Linguini Alfredo','United States','New Orleans',null,0, 2000-08-09);
--insert into post values (default, 2, 'Cuban Sandwich', 'United States','Miami','food',5, 2010-01-19);
--insert into post values (default, 3, 'Inception', 'United States','Chicago','movie',3, 2020-08-15);
--insert into post values (default, 1, 'Vanilla Ice Cream', 'United States','Chicago',null,3, 2012-12-15);

--insert into post values (default, 1, 'Shrimp Linguini Alfredo','United States','New Orleans',null,0, 2006-01-24);
--insert into post values (default, 2, 'Cuban Sandwich', 'United States','Miami',null,5, 2003-08-15);
--insert into post values (default, 3, 'Vanilla Ice Cream', 'United States','Chicago',null,3, 2009-10-12);
