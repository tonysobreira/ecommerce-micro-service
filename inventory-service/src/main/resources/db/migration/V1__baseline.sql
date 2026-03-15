CREATE TABLE inventory (
  id BIGSERIAL PRIMARY KEY,
  product_id UUID NOT NULL UNIQUE,
  available_quantity INTEGER NOT NULL,
  reserved_quantity INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE stock_reservation (
  id BIGSERIAL PRIMARY KEY,
  order_id UUID NOT NULL,
  product_id UUID NOT NULL,
  quantity INTEGER NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE stock_movement (
  id BIGSERIAL PRIMARY KEY,
  product_id UUID NOT NULL,
  quantity INTEGER NOT NULL,
  type VARCHAR(50) NOT NULL,
  reason VARCHAR(255),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
