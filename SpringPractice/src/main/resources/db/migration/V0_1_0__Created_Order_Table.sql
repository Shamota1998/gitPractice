drop table orders;
create table orders
(
    order_id serial not null,
    date     date   not null default CURRENT_DATE,
    user_id  int4,
    primary key (order_id)
);


alter table orders
    add constraint Usr2FK foreign key (user_id) references usr;



