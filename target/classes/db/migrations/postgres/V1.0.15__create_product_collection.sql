DROP TABLE product_product_tag;
DROP TABLE product_tag;
ALTER TABLE payment ALTER COLUMN payment_processor_tx_id DROP NOT NULL;

CREATE TABLE IF NOT EXISTS product_collection(
id UUID PRIMARY KEY,
name VARCHAR(100) UNIQUE NOT NULL,
image TEXT UNIQUE NOT NULL DEFAULT ' ',
vendor_id INTEGER NOT NULL,
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT product_collection_vendor_id_fk
	FOREIGN KEY(vendor_id) REFERENCES vendor(id)
	ON DELETE CASCADE
);

ALTER TABLE product ADD COLUMN product_collection_id UUID REFERENCES product_collection(id) ON DELETE CASCADE;