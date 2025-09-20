create extension if not exists "uuid-ossp";

create table if not exists clients (
  id uuid primary key default uuid_generate_v4(),
  full_name text not null,
  email text unique,
  phone text,
  notes text,
  created_at timestamptz not null default now()
);

create table if not exists workout_plans (
  id uuid primary key default uuid_generate_v4(),
  client_id uuid not null references clients(id) on delete cascade,
  title text not null,
  start_date date,
  end_date date,
  status text not null default 'ACTIVE',
  created_at timestamptz not null default now()
);


