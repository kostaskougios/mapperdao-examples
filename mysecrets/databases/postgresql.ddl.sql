-- Please execute this using "mysecrets" user

create table "User" (
	id serial not null,
	email varchar(100) not null,
	name varchar(100) not null,
	password varchar(50) not null,
	primary key(id)
)
;
create unique index IX_User_Email on "User"(email)
;

create table Secret (
	id serial not null,
	title varchar(200) not null,
	secret text,
	user_id int not null,
	primary key (id),
	foreign key (user_id) references "User"(id) on delete cascade
)
;

create table Secret_User (
	secret_id int not null,
	user_id int not null,
	primary key (secret_id,user_id)
)
;

alter table Secret_User add constraint FK_SU_Secret foreign key(secret_id) references Secret(id) 
on delete cascade on update cascade
;

alter table Secret_User add constraint FK_SU_User foreign key(user_id) references "User"(id)
on delete cascade on update cascade
;
