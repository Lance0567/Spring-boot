drop database customerdb;
drop user customer;
create user customer with password 'password';
create database customerdb with template=template0 owner=customer;
\connect customerdb;
alter default  privileges  grant all on tables to customer;
alter default  privileges  grant all on sequences to customer;

create table users(
                      user_id integer primary key not null,
                      email varchar(30) not null,
                      username varchar(20) not null,
                      password text not null
);

create table item(
                     item_id integer primary key not null,
                     item_name varchar(20) not null,
                     category_name varchar(20) not null,
                     price numeric(18, 2) not null
);
alter table item add constraint cat_users_fk
foreign key (item_id) references users(user_id);

create table transaction(
                            transaction_id integer primary key not null,
                            category_id integer not null,
                            user_id integer not null,
                            amount numeric(18, 2) not null,
                            note varchar(50) not null,
                            transaction_date bigint not null
);
alter table transaction add constraint cat_transaction_fk
foreign key (category_id) references transaction(category_id);
alter table transaction add constraint transaction_users_fk
foreign  key (user_id) references users(user_id);

create sequence users_seq increment 1 start 1;
create sequence item_seq increment 1 start 1;
create sequence transaction_seq increment 1 start 1000;
