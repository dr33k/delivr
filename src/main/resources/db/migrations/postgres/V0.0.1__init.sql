CREATE TABLE IF NOT EXISTS vendor(
id INTEGER PRIMARY KEY,
vendor_no UUID UNIQUE NOT NULL,
org_name VARCHAR UNIQUE NOT NULL,
mgmt_email VARCHAR UNIQUE NOT NULL,
phone_nos VARCHAR NOT NULL,
password VARCHAR(255) NOT NULL,
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS app_user(
id INTEGER PRIMARY KEY,
user_no UUID UNIQUE NOT NULL,
fname VARCHAR(40) NOT NULL,
lname VARCHAR(40) NOT NULL,
email VARCHAR UNIQUE NOT NULL,
phone_no VARCHAR(15) NOT NULL,
_password VARCHAR(255) NOT NULL,
address VARCHAR(200) NOT NULL,
city VARCHAR(30) NOT NULL,
_state VARCHAR(30) NOT NULL,
postal_code VARCHAR(10),
country VARCHAR(50) NOT NULL,
_role VARCHAR(10) NOT NULL DEFAULT 'CUSTOMER',
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product(
id INTEGER PRIMARY KEY,
product_no UUID UNIQUE NOT NULL,
title VARCHAR(100) UNIQUE NOT NULL,
description TEXT,
price DECIMAL(7,2) NOT NULL DEFAULT 0,
date_created TIMESTAMP WITH TIME ZONE NOT NULL,
date_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);