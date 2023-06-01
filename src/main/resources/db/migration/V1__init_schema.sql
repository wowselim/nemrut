create table company
(
  id         uuid primary key default gen_random_uuid(),
  name       varchar(255),
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp
);

create table role (
  id uuid    primary key default gen_random_uuid(),
  title      varchar(255),
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp
);

create table salary (
  id uuid         primary key default gen_random_uuid(),
  role_id uuid    references role(id) on update cascade on delete cascade not null,
  company_id uuid references company(id) on update cascade on delete cascade not null,
  currency        char(3) not null,
  amount          bigint not null,
  created_at      timestamp default current_timestamp,
  updated_at      timestamp default current_timestamp
);
