
-- GymPandro – Schema v1 (PostgreSQL)
-- Matches the user's schematic with a unified Users table and Managed fields on each table.

-- ==============================
-- Extensions
-- ==============================
create extension if not exists "uuid-ossp";

-- ==============================
-- Helper: auto-update modifiedAt
-- ==============================
create or replace function set_modified_at() returns trigger as $$
begin
  new."modifiedAt" := now();
  return new;
end;
$$ language plpgsql;

-- ==============================
-- TABLE Roles
-- ==============================
create table if not exists roles (
  id           uuid primary key default uuid_generate_v4(),
  name         text not null unique,                -- nome breve del ruolo (es. "TRAINER")
  description  text,                                -- descrizione estesa o note
  "createdAt"  timestamptz not null default now(),  -- Managed.createdAt
  "createdBy"  uuid,
  "modifiedAt" timestamptz not null default now(),  -- Managed.modifiedAt
  "modifiedBy" uuid
);

-- Trigger to maintain modifiedAt
drop trigger if exists trg_roles_modified_at on roles;
create trigger trg_roles_modified_at
before update on roles
for each row execute function set_modified_at();

-- ==============================
-- TABLE Users (trainer + clienti)
-- ==============================
create table if not exists users (
  id           uuid primary key default uuid_generate_v4(),
  username     text not null unique,                -- nome utente per l’accesso
  name         text not null,                       -- nome
  surname      text not null,                       -- cognome
  phone        text,                                -- telefono
  email        text not null unique,                -- email
  "passwordHash" text,                              -- hash password (opzionale per clienti senza login)
  "isActive"   boolean not null default true,       -- abilitazione account
  role_id      uuid not null references roles(id) on delete restrict, -- ruolo (TRAINER/CLIENT/ADMIN)
  assigned_to  uuid references users(id) on delete set null,          -- trainer di riferimento (solo clienti)
  notes        text,                                -- note aggiuntive
  "createdAt"  timestamptz not null default now(),  -- Managed.createdAt
  "createdBy"  uuid references users(id) on delete set null, -- Managed.createdBy
  "modifiedAt" timestamptz not null default now(),  -- Managed.modifiedAt
  "modifiedBy" uuid references users(id) on delete set null  -- Managed.ModifiedBy
);

-- Trigger to maintain modifiedAt
drop trigger if exists trg_users_modified_at on users;
create trigger trg_users_modified_at
before update on users
for each row execute function set_modified_at();

-- Helpful indexes
create index if not exists idx_users_assigned_to on users(assigned_to);
create index if not exists idx_users_role on users(role_id);

alter table roles
  add constraint roles_createdby_fkey  foreign key ("createdBy")  references users(id) on delete set null,
  add constraint roles_modifiedby_fkey foreign key ("modifiedBy") references users(id) on delete set null;

-- ==============================
-- TABLE Exercises (libreria)
-- ==============================
create table if not exists exercises (
  id           uuid primary key default uuid_generate_v4(),
  name         text not null,                       -- nome esercizio (es. "Panca Piana")
  equipment    text,                                -- attrezzatura (bilanciere, manubri, etc.)
  description  text,                                -- descrizione / note
  "createdAt"  timestamptz not null default now(),
  "createdBy"  uuid references users(id) on delete set null,
  "modifiedAt" timestamptz not null default now(),
  "modifiedBy" uuid references users(id) on delete set null
);

drop trigger if exists trg_exercises_modified_at on exercises;
create trigger trg_exercises_modified_at
before update on exercises
for each row execute function set_modified_at();

-- ==============================
-- TABLE Sheets (schede di allenamento)
-- ==============================
create table if not exists sheets (
  id           uuid primary key default uuid_generate_v4(),
  user_id      uuid not null references users(id) on delete cascade,  -- cliente a cui è assegnata
  title        text not null,                          -- titolo scheda
  "startDate"  date,                                   -- data inizio (opzionale)
  "endDate"    date,                                   -- data fine (opzionale)
  status       text not null default 'ACTIVE',         -- ACTIVE | ARCHIVED
  "createdAt"  timestamptz not null default now(),
  "createdBy"  uuid references users(id) on delete set null,
  "modifiedAt" timestamptz not null default now(),
  "modifiedBy" uuid references users(id) on delete set null,
  constraint chk_sheets_status check (status in ('ACTIVE','ARCHIVED'))
);

drop trigger if exists trg_sheets_modified_at on sheets;
create trigger trg_sheets_modified_at
before update on sheets
for each row execute function set_modified_at();

create index if not exists idx_sheets_user on sheets(user_id);
create index if not exists idx_sheets_status on sheets(status);

-- ==============================
-- TABLE SheetExercises (dettaglio scheda)
-- ==============================
create table if not exists sheet_exercises (
  id           uuid primary key default uuid_generate_v4(),
  sheet_id     uuid not null references sheets(id) on delete cascade,    -- scheda di riferimento
  exercise_id  uuid not null references exercises(id) on delete restrict,-- esercizio collegato
  sets         integer not null,                                         -- serie previste
  reps         integer not null,                                         -- ripetizioni previste
  "restSec"    text,                                                     -- recupero in secondi (testo come da richiesta)
  notes        text,                                                     -- note specifiche
  "createdAt"  timestamptz not null default now(),
  "createdBy"  uuid references users(id) on delete set null,
  "modifiedAt" timestamptz not null default now(),
  "modifiedBy"  uuid references users(id) on delete set null
);

drop trigger if exists trg_sheet_exercises_modified_at on sheet_exercises;
create trigger trg_sheet_exercises_modified_at
before update on sheet_exercises
for each row execute function set_modified_at();

create index if not exists idx_sheet_exercises_sheet on sheet_exercises(sheet_id);
create index if not exists idx_sheet_exercises_exercise on sheet_exercises(exercise_id);

-- ==============================
-- Seed opzionale ruoli di base (commenta se non serve)
-- ==============================
insert into roles (id, name, description)
values
  (uuid_generate_v4(), 'ADMIN',   'Accesso completo'),
  (uuid_generate_v4(), 'TRAINER', 'Gestione clienti e schede'),
  (uuid_generate_v4(), 'CLIENT',  'Accesso alle proprie schede')
on conflict (name) do nothing;
