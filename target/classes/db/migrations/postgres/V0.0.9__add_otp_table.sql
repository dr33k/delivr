CREATE TABLE IF NOT EXISTS otp(
pin INTEGER PRIMARY KEY,
username VARCHAR(255) NOT NULL,
_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);