ALTER TABLE vendor
ADD COLUMN IF NOT EXISTS operating_hours TEXT,
DROP COLUMN IF EXISTS password;