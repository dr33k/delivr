ALTER TABLE vendor DROP CONSTRAINT vendor_incorporation_no_key;

ALTER TABLE product
ALTER COLUMN rating_sum SET DEFAULT 1,
ALTER COLUMN no_of_ratings SET DEFAULT 1;