-- 1) Extensões
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 2) USERS
CREATE TABLE users (
  id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  email           varchar(255) NOT NULL,
  password_hash   varchar(255) NOT NULL,
  cpf             varchar(14)  NOT NULL,
  first_name      varchar(100) NOT NULL,
  last_name       varchar(100) NOT NULL,
  birth_date      date         NOT NULL,
  phone           varchar(20)  NOT NULL,
  street          varchar(120) NOT NULL,
  number          varchar(10)  NOT NULL,
  complement      varchar(60),
  neighborhood    varchar(80)  NOT NULL,
  city            varchar(80)  NOT NULL,
  state           char(2)      NOT NULL,
  zip_code        varchar(10)  NOT NULL,
  status          varchar(16)  NOT NULL DEFAULT 'ACTIVE',
  created_at      timestamptz  NOT NULL DEFAULT now(),
  updated_at      timestamptz  NOT NULL DEFAULT now(),
  CONSTRAINT chk_state         CHECK (state ~ '^[A-Z]{2}$'),
  CONSTRAINT chk_user_status   CHECK (status IN ('ACTIVE','INACTIVE','BLOCKED')),
  CONSTRAINT uq_users_cpf      UNIQUE (cpf),
  CONSTRAINT chk_cpf_digits    CHECK (cpf ~ '^[0-9]{11}$')
);

-- Unicidade de e-mail case-insensitive
CREATE UNIQUE INDEX uq_users_email_ci ON users (lower(email));

-- 3) ACCOUNTS
CREATE TABLE accounts (
  id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         uuid NOT NULL,
  account_number  varchar(20)    NOT NULL,
  branch_code     varchar(10)    NOT NULL DEFAULT '0001',
  type            varchar(16)    NOT NULL DEFAULT 'CHECKING',
  currency        char(3)        NOT NULL,
  status          varchar(16)    NOT NULL DEFAULT 'ACTIVE',
  created_at      timestamptz    NOT NULL DEFAULT now(),
  CONSTRAINT fk_accounts_user      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
  CONSTRAINT uq_account_number     UNIQUE (account_number),
  CONSTRAINT chk_currency          CHECK (currency IN ('BRL','USD','EUR')),
  CONSTRAINT chk_account_type      CHECK (type IN ('CHECKING','SAVINGS')),
  CONSTRAINT chk_account_status    CHECK (status IN ('ACTIVE','BLOCKED','CLOSED'))
);

-- Índices
CREATE INDEX ix_accounts_user_id ON accounts(user_id);

-- 4) Trigger para updated_at (USERS)
CREATE OR REPLACE FUNCTION set_updated_at() RETURNS trigger AS $$
BEGIN
  NEW.updated_at := now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_set_updated_at
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION set_updated_at();
