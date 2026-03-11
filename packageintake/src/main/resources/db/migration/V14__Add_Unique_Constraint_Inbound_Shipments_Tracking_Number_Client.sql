-- Enforce uniqueness on (Tracking_Number, Client) for Inbound_Shipments.
-- Remove existing duplicates first (e.g. maintenance/remove-inbound-shipments-duplicates.sql) or this migration will fail.

IF NOT EXISTS (
    SELECT 1 FROM sys.key_constraints
    WHERE name = 'UK_Inbound_Shipments_Tracking_Number_Client'
      AND parent_object_id = OBJECT_ID('Inbound_Shipments')
)
BEGIN
    ALTER TABLE Inbound_Shipments
    ADD CONSTRAINT UK_Inbound_Shipments_Tracking_Number_Client UNIQUE (Tracking_Number, Client);
END
