CREATE ROLE benchmark LOGIN ENCRYPTED PASSWORD 'md504c89ef3d5c509291438348a395913d0'
   VALID UNTIL 'infinity';

CREATE DATABASE benchmark
  WITH ENCODING='UTF8'
       OWNER=benchmark
       CONNECTION LIMIT=-1;

create table Product (
	id serial not null,
	name varchar(100) not null,
	primary key (id)
)
;
create table Attribute (
	id serial not null,
	name varchar(100) not null,
	value varchar(100) not null,
	primary key(id)
)
;

create table Product_Attribute (
	product_id int not null,
	attribute_id int not null,
	primary key (product_id,attribute_id),
	foreign key (product_id) references Product(id) on delete cascade on update cascade,
	foreign key (attribute_id) references Attribute(id) on delete cascade on update cascade
)
