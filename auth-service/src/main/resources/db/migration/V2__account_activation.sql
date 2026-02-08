ALTER TABLE user_accounts
ADD COLUMN IF NOT EXISTS activated_at TIMESTAMP NULL;

CREATE TABLE IF NOT EXISTS activation_tokens (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL UNIQUE REFERENCES user_accounts(id),
  token_hash TEXT NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  used_at TIMESTAMP NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_activation_tokens_token_hash ON activation_tokens(token_hash);
