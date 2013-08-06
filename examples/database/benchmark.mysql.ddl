create table Product (
	id serial not null,
	name varchar(100) not null,
	primary key (id)
) engine InnoDB
;
create table Attribute (
	id serial not null,
	name varchar(100) not null,
	value varchar(100) not null,
	primary key(id)
) engine InnoDB
;

create table Product_Attribute (
	product_id bigint(20) unsigned not null,
	attribute_id bigint(20) unsigned not null,
	primary key (product_id,attribute_id),
	foreign key (product_id) references Product(id) on delete cascade,
	foreign key (attribute_id) references Attribute(id) on delete cascade
) engine InnoDB
