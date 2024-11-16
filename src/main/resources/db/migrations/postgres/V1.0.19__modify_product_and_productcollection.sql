ALTER TABLE product_collection ALTER COLUMN image SET DATA TYPE VARCHAR(300);
ALTER TABLE product_collection ALTER COLUMN image SET NOT NULL;
ALTER TABLE product_collection ALTER COLUMN image SET DEFAULT '';
ALTER TABLE product_collection DROP CONSTRAINT product_collection_image_key;

ALTER TABLE product ALTER COLUMN rating_sum SET DEFAULT 3.0