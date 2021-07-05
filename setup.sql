-- DDL

create table users(
	user_id serial primary key,
	username varchar(30) unique,
	user_password varchar(30)
);

create table post(
	post_id serial primary key,
	title varchar(30),
	country varchar(30),
	city varchar(30),
	tag varchar(30),
	rating numeric constraint rating_less_than_five check (rating <= 5),
	constraint rating_greater_than_zero check (rating >= 0)
);

create table user_post (
	surrogate_id serial primary key,
	users_id integer references users,
	post_id integer references post
);

-- DMLinsert into users values (default, 'alpha','password123');

insert into users values (default, 'beta','password123');
insert into users values (default, 'charlie','password123');insert into post values (default,'Shrimp Linguini Alfredo','United States','New Orleans',0);
insert into post values (default, 'Cuban Sandwich', 'United States','Miami',5);
insert into post values (default, 'Vanilla Ice Cream', 'United States', 'Chicago',3);insert into user_post values (default, 1, 1);
insert into user_post values (default, 2, 2);
insert into user_post values (default, 3, 3);