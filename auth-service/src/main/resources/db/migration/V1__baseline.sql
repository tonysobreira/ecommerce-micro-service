CREATE TABLE IF NOT EXISTS user_accounts (
  id UUID PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  roles TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  deleted_at TIMESTAMP NULL,
  activated_at TIMESTAMP NULL
);
