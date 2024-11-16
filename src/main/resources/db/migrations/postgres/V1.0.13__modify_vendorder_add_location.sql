ALTER TABLE vendor_order ADD COLUMN app_user_location_id UUID REFERENCES app_user_location(id) ON DELETE CASCADE;
