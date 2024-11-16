ALTER TABLE vendor
DROP COLUMN operating_hours,
ADD COLUMN weekday_hours VARCHAR(20) DEFAULT ' ',
ADD COLUMN weekend_hours VARCHAR(20) DEFAULT ' ';