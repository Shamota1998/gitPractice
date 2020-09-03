drop table order_device;
create table order_device
(
    od_id    serial not null,
    order_id int4   not null,
    dev_id   int4   not null,
    qty      int4,
    primary key (od_id)
);

alter table order_device
    add constraint ODeviceFK foreign key (dev_id) references device;
alter table order_device
    add constraint ODevice1FK foreign key (order_id) references orders;