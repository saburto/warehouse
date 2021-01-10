create table if not exists Article (
  art_id int primary key,
  name varchar(255) not null,
  stock bigint not null
);

create table if not exists Product (
  name varchar(255) not null,
  primary key (name)
);


create table if not exists Containing_Articles (
  name_product varchar(255) not null,
  art_id int not null,
  amount int not null,
  primary key (name_product, art_id),
  foreign key (name_product) references Product(name),
  foreign key (art_id) references Article(art_id)
);
