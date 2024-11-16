ALTER TABLE vendor_account RENAME bank_name TO bank_code;
ALTER TABLE vendor_account ALTER COLUMN bank_code SET DATA TYPE VARCHAR(5);
ALTER TABLE vendor_account ALTER COLUMN bank_code SET NOT NULL;

ALTER TABLE vendor_account RENAME currency TO country;
ALTER TABLE vendor_account ALTER COLUMN country SET DATA TYPE VARCHAR(3);
ALTER TABLE vendor_account ALTER COLUMN country SET NOT NULL;
ALTER TABLE vendor_account ALTER COLUMN country SET DEFAULT 'NG';