-- DDL

create table users(
	user_id serial primary key,
	username varchar(30) unique,
	user_password varchar(30)
);

create table post(
	post_id serial primary key,
	user_id integer references users,
	title varchar(30),
	country varchar(30),
	city varchar(30),
	tag varchar(30),
	rating integer constraint rating_less_than_five check (rating <= 5),
	constraint rating_greater_than_zero check (rating >= 0)
);

-- DML

insert into users values (default, 'alpha','password123');
insert into users values (default, 'beta','password123');
insert into users values (default, 'charlie','password123');


insert into post values (default, 1, 'Shrimp Linguini Alfredo','United States','New Orleans',null,0);
insert into post values (default, 2, 'Cuban Sandwich', 'United States','Miami',null,5);
insert into post values (default, 3, 'Vanilla Ice Cream', 'United States','Chicago',null,3);