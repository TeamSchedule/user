create sequence user_sequence start 1 increment 1;
create table app_user
(
  id            int8 not null,
  confirmed     boolean,
  creation_date timestamp,
  email         varchar(255),
  login         varchar(255),
  password      varchar(255),
  primary key (id)
);
alter table app_user
  add constraint UK_irayhia1ygarvmv7apksctnqn unique (login);