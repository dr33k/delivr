CREATE TABLE IF NOT EXISTS email_verification_token(
user_no UUID PRIMARY KEY,
token VARCHAR(255),
_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
user_id UUID UNIQUE NOT NULL,
CONSTRAINT token_user_id FOREIGN KEY(user_id) REFERENCES app_user(id) ON DELETE CASCADE
);