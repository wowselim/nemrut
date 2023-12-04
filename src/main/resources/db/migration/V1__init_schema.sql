create table company
(
  id         uuid primary key default gen_random_uuid(),
  name       varchar(255) not null,
  created_at timestamp        default current_timestamp,
  updated_at timestamp        default current_timestamp
);

create table role
(
  id         uuid primary key default gen_random_uuid(),
  title      varchar(255) not null,
  created_at timestamp        default current_timestamp,
  updated_at timestamp        default current_timestamp
);

create table salary
(
  id         uuid      default gen_random_uuid(),
  role_id    uuid    not null,
  company_id uuid    not null,
  currency   char(3) not null,
  amount     bigint  not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp,
  primary key (id),
  foreign key (role_id) references role (id) on update cascade on delete cascade,
  foreign key (company_id) references company (id) on update cascade on delete cascade
);

CREATE TABLE user_account
(
  id         uuid      default gen_random_uuid(),
  username   varchar(255) unique not null,
  password   varchar(255)        not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp,
  primary key (id)
);

CREATE TABLE user_role
(
  user_id    uuid         not null,
  role       varchar(255) not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp,
  primary key (user_id, role),
  foreign key (user_id) references user_account (id) on update cascade on delete cascade
);

CREATE TABLE role_permission
(
  role       varchar(255) not null,
  permission varchar(255) not null,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp,
  primary key (role, permission)
);
