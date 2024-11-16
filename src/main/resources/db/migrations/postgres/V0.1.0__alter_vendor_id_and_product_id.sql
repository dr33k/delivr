
ALTER TABLE vendor ALTER COLUMN id SET DATA TYPE BIGINT;
ALTER TABLE product ALTER COLUMN id SET DATA TYPE BIGINT;

ALTER TABLE vendor ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY;
ALTER TABLE product ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY;

ALTER TABLE app_user ALTER COLUMN vendor_id SET DATA TYPE BIGINT;
ALTER TABLE product ALTER COLUMN vendor_id SET DATA TYPE BIGINT;
ALTER TABLE product_tag ALTER COLUMN vendor_id SET DATA TYPE BIGINT;

ALTER TABLE product_product_tag ALTER COLUMN product_id SET DATA TYPE BIGINT;