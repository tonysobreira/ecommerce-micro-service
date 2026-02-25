ALTER TABLE orders
  ADD COLUMN IF NOT EXISTS customer_email TEXT;

UPDATE orders
SET customer_email = 'unknown@local'
WHERE customer_email IS NULL OR customer_email = '';

ALTER TABLE orders
  ALTER COLUMN customer_email SET NOT NULL;
