CREATE TABLE shipping_method (
  id UUID PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  base_cost NUMERIC(12,2) NOT NULL
);

CREATE TABLE shipment (
  id UUID PRIMARY KEY,
  order_id UUID NOT NULL,
  user_id UUID NOT NULL,
  status VARCHAR(50) NOT NULL,
  destination_address VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE tracking (
  id UUID PRIMARY KEY,
  shipment_id UUID NOT NULL,
  status VARCHAR(50) NOT NULL,
  location VARCHAR(150),
  event_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
