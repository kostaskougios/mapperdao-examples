create table Attribute (
	id serial not null,
	name varchar(100) not null,
	value varchar(100) not null,
	primary key(id)
)
;

create table Category (
	id serial not null,
	name varchar(100) not null,
	parent_id int,
	primary key(id),
	foreign key(parent_id) references Category(id) on delete cascade
)
;

create table Product (
	id serial not null,
	title varchar(100) not null,
	description text not null,
	primary key(id)
)
;
create table Product_Attribute (
	product_id int not null,
	attribute_id int not null,
	foreign key (product_id) references Product(id) on delete cascade,
	foreign key (attribute_id) references Attribute(id) on delete cascade
)

;
create table Price (
	currency varchar(3) not null,
	unitprice decimal(6,3),
	saleprice decimal(6,3),
	product_id int not null,
	primary key (product_id,currency),
	foreign key (product_id) references Product(id) on delete cascade
)
;

create table Product_Category (
	product_id int not null,
	category_id int not null,
	primary key(product_id,category_id),
	foreign key(product_id) references Product(id) on delete cascade,
	foreign key(category_id) references Category(id) on delete cascade
)
;

create unique index IX_Attribute_name_value on Attribute(name,value)

;