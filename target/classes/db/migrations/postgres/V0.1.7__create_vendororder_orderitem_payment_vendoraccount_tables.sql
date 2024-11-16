CREATE TABLE IF NOT EXISTS vendor_account(
id UUID PRIMARY KEY,
name VARCHAR(100) NOT NULL,
vendor_id BIGINT NOT NULL REFERENCES vendor(id) ON DELETE CASCADE,
account_no VARCHAR(20) NOT NULL,
subaccount_code VARCHAR(100) NOT NULL,
is_primary BOOL DEFAULT FALSE,
bank_name VARCHAR(255) NOT NULL,
currency CHAR(3) DEFAULT 'NGN',
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_order(
id UUID PRIMARY KEY,
user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
total_cost DECIMAL(7,2) NOT NULL DEFAULT 0,
currency CHAR(3) DEFAULT 'NGN',
date_created TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS vendor_order(
id UUID PRIMARY KEY,
vendor_order_no UUID NOT NULL UNIQUE,
vendor_id BIGINT NOT NULL REFERENCES vendor(id) ON DELETE CASCADE,
customer_order_id UUID NOT NULL REFERENCES customer_order(id) ON DELETE CASCADE,
total_cost DECIMAL(7,2) NOT NULL DEFAULT 0,
currency CHAR(3) DEFAULT 'NGN',
status VARCHAR(30) DEFAULT 'INITIATED',
is_paid BOOL DEFAULT FALSE,
vendor_account_id UUID NOT NULL REFERENCES vendor_account(id) ON DELETE CASCADE,
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_item (
id UUID PRIMARY KEY,
vendor_order_id UUID NOT NULL REFERENCES vendor_order(id) ON DELETE CASCADE,
product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
units SMALLINT DEFAULT 1,
price DECIMAL(7,2) NOT NULL DEFAULT 0,
currency CHAR(3) DEFAULT 'NGN',
note VARCHAR(300) DEFAULT '',
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payment(
id UUID PRIMARY KEY,
customer_order_id UUID NOT NULL REFERENCES customer_order(id) ON DELETE CASCADE,
amount DECIMAL(7,2) NOT NULL,
currency CHAR(3) DEFAULT 'NGN',
t_ref_uuid UUID NOT NULL,
is_paid BOOL DEFAULT FALSE,
vendor_account_id UUID NOT NULL REFERENCES vendor_account(id),
payment_processor VARCHAR(15) NOT NULL,
payment_processor_tx_id SMALLINT NOT NULL,
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
