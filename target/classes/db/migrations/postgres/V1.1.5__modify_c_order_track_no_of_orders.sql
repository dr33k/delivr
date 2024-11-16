ALTER TABLE customer_order
ADD COLUMN no_of_vendor_orders SMALLINT NOT NULL DEFAULT 0,
ADD COLUMN no_of_ready_vendor_orders SMALLINT NOT NULL DEFAULT 0;